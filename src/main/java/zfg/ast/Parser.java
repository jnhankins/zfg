package zfg.ast;

import static zfg.antlr.ZfgLexer.BIT;
import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.F32;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.I08;
import static zfg.antlr.ZfgLexer.I16;
import static zfg.antlr.ZfgLexer.I32;
import static zfg.antlr.ZfgLexer.I64;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.LET;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgLexer.U08;
import static zfg.antlr.ZfgLexer.U16;
import static zfg.antlr.ZfgLexer.U32;
import static zfg.antlr.ZfgLexer.U64;
import static zfg.antlr.ZfgLexer.VAR;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.AssignmentStmtContext;
import zfg.antlr.ZfgParser.CompilationUnitContext;
import zfg.antlr.ZfgParser.DeclarationContext;
import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.FunctionCallStmtContext;
import zfg.antlr.ZfgParser.FunctionReturnStmtContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.GroupedExprContext;
import zfg.antlr.ZfgParser.IdentifierContext;
import zfg.antlr.ZfgParser.IfElseStmtContext;
import zfg.antlr.ZfgParser.InfixOpExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.LoopBreakStmtContext;
import zfg.antlr.ZfgParser.LoopContinueStmtContext;
import zfg.antlr.ZfgParser.LoopForStmtContext;
import zfg.antlr.ZfgParser.LoopStmtContext;
import zfg.antlr.ZfgParser.LoopWhileStmtContext;
import zfg.antlr.ZfgParser.NamedTypeContext;
import zfg.antlr.ZfgParser.PostfixOpExprContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.VariableExprContext;
import zfg.ast.Node.CompilationUnit;
import zfg.ast.Node.Const;
import zfg.ast.Node.VarRef;
import zfg.lang.primitive.Val;

public final class Parser {
  public static final class ParserException extends RuntimeException {
    public ParserException(final ParserRuleContext ctx, final String message) { this(ctx, message, null); }
    public ParserException(final ParserRuleContext ctx, final Throwable cause) { this(ctx, null, cause); }
    public ParserException(final ParserRuleContext ctx, final String message, final Throwable cause) { super(message, cause); }
  }

  public static Node parse(final Path path) {
    try { return parse(CharStreams.fromPath(path)); }
    catch (final Exception e) { throw new RuntimeException(e); }
  }

  public static Node parse(final String source, final String sourceName) {
    return parse(CharStreams.fromString(source, sourceName));
  }

  public static Node parse(final CharStream source) {
    System.out.println("source: " + source.getSourceName());
    System.out.println(">" + source.toString().replaceAll("\\r?\\n", "\n>"));

    // Lexical Analysis
    final ZfgLexer zfgLexer = new ZfgLexer(source);
    final CommonTokenStream tokens = new CommonTokenStream(zfgLexer);
    System.out.println("tokens: " + PrettyPrint.toPrettyTokensString(zfgLexer, tokens));

    // Syntax Analysis
    final ZfgParser zfgParser = new ZfgParser(tokens);
    final CompilationUnitContext parsed = zfgParser.compilationUnit();
    System.out.println("parsed: " + parsed.toStringTree(zfgParser));
    System.out.println("tree:\n" + PrettyPrint.toPrettyTreeString(zfgParser, parsed));

    // Semantic Analysis
    final Parser parser = new Parser();
    final Node root = parser.visitCompilationUnit(parsed);
    System.out.println("parsed: " + root);
    System.out.println("tree: " + PrettyPrint.toPrettyTreeString(root));

    return root;
  }

  // TODO: fully qualified names (i.e. id.size() > 1
  // TODO: use before declaration (i.e. fun f(): i32 = g(); fun g() i32 = 1; }
  private static sealed abstract class Scope {
    private final Map<String, Node> table = new HashMap<>();
    final Node resolve(final String name) { return table.get(name); }
    static final class Module extends Scope {}
    static final class Function extends Scope {}
    static final class Block extends Scope {}
  }
  private final Stack<Scope> scopes = new Stack<>();
  private final Node resolveSymbol(final String name) {
    for (int s = scopes.size() - 1; s >= 0; s -= 1) {
      final Scope scope = scopes.get(s);
      final Node node = scope.resolve(name);
      if (node != null) return node;
    }
    return null;
  }

	public final Node visitCompilationUnit(final CompilationUnitContext ctx) {
    // Push scope
    final Scope.Module moduleScope = new Scope.Module();
    scopes.push(moduleScope);
    // Post-order traversal
    final List<Node> children = new ArrayList<>();
    for (final StatementContext statement : ctx.statement())
      children.add(visitStatement(statement));
    // Pop scope
    scopes.pop();
    // Return
    return new CompilationUnit(children);
  }

  public final Node visitStatement(final StatementContext ctx) {
    return switch (ctx) {
      case DeclarationStmtContext    stmt -> visitDeclarationStmt(stmt);
      case AssignmentStmtContext     stmt -> visitAssignmentStmt(stmt);
      case FunctionCallStmtContext   stmt -> visitFunctionCallStmt(stmt);
      case IfElseStmtContext         stmt -> visitIfElseStmt(stmt);
      case LoopStmtContext           stmt -> visitLoopStmt(stmt);
      case LoopWhileStmtContext      stmt -> visitLoopWhileStmt(stmt);
      case LoopForStmtContext        stmt -> visitLoopForStmt(stmt);
      case LoopBreakStmtContext      stmt -> visitLoopBreakStmt(stmt);
      case LoopContinueStmtContext   stmt -> visitLoopContinueStmt(stmt);
      case FunctionReturnStmtContext stmt -> visitFunctionReturnStmt(stmt);
      default -> throw new AssertionError();
    };
  }
  private Node visitDeclarationStmt(DeclarationStmtContext stmt) {
    // Post-order traversal
    final Node child = visitExpression(stmt.expression());
    final Type childType = child.type();

    // Type checking
    final Type outType = visitType(stmt.type());
    // Check muttable/immutable
    final boolean immu = switch (stmt.modifier.getType()) {
      case LET -> true;
      case VAR -> false;
      default -> throw new AssertionError();
    };
  }
  private Node visitAssignmentStmt(AssignmentStmtContext stmt) {
    throw new UnsupportedOperationException();
  }
  private Node visitFunctionCallStmt(FunctionCallStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }
  private Node visitIfElseStmt(IfElseStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }
  private Node visitLoopStmt(LoopStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }
  private Node visitLoopWhileStmt(LoopWhileStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }
  private Node visitLoopForStmt(LoopForStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }
  private Node visitLoopBreakStmt(LoopBreakStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }
  private Node visitLoopContinueStmt(LoopContinueStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }
  private Node visitFunctionReturnStmt(FunctionReturnStmtContext stmt) {
    throw new UnsupportedOperationException("todo later");
  }


  public final Node visitExpression(final ZfgParser.ExpressionContext ctx) {
    return switch (ctx) {
      case FunctionCallExprContext expr -> visitFunctionCallExpr(expr);
      case VariableExprContext     expr -> visitVariableExpr(expr);
      case LiteralExprContext      expr -> visitLiteralExpr(expr);
      case GroupedExprContext      expr -> visitGroupedExpr(expr);
      case PostfixOpExprContext    expr -> visitPostfixOpExpr(expr);
      case PrefixOpExprContext     expr -> visitPrefixOpExpr(expr);
      case InfixOpExprContext      expr -> visitInfixOpExpr(expr);
      case AssignmentExprContext   expr -> visitAssignmentExpr(expr);
      default -> throw new AssertionError();
    };
  }

  public final Node visitFunctionCallExpr(final FunctionCallExprContext ctx) {
    throw new UnsupportedOperationException("todo later");
  }

  public final Node visitVariableExpr(final VariableExprContext ctx) {
    // Post-order traversal
    final String name = ctx.getText();
    // Resolve the symbol
    final Node referent = resolveSymbol(name);
    if (referent == null) throw new ParserException(ctx, "Unresolved symbol");
    // Type checking
    final Type outType = referent.type();
    // Constant folding
    if (referent instanceof Const) return referent;
    // Return
    return new VarRef(name, referent, outType);
  }

  public final Node visitLiteralExpr(final LiteralExprContext ctx) {
    final String str = ctx.lit.getText();
    return switch (ctx.lit.getType()) {
      case BitLit -> Val.parseBit(str)
          .map(val -> new Const(val, Type.bit))
          .orElseThrow(() -> new ParserException(ctx, "Invalid bit literal"));
      case IntLit -> Val.parseInt(str, hasContiguousNegPrefix(ctx))
          .map(val -> new Const(val, Type.of(val)))
          .orElseThrow(() -> new ParserException(ctx, "Invalid int literal"));
      case FltLit -> Val.parseFlt(str)
          .map(val -> new Const(val, Type.of(val)))
          .orElseThrow(() -> new ParserException(ctx, "Invalid flt literal"));
      default -> throw new AssertionError();
    };
  }

  /** @see Literal.parseInt */
  private boolean hasContiguousNegPrefix(final LiteralExprContext ctx) {
    if (!(ctx.parent instanceof PostfixOpExprContext)) return false;
    final Token op = ((PostfixOpExprContext) ctx.parent).op;
    return op.getType() == SUB && op.getStopIndex() + 1 == ctx.getStart().getStartIndex();
  }

  public final Node visitGroupedExpr(final GroupedExprContext ctx) {
    return visitExpression(ctx.expression());
  }

  public final Node visitPostfixOpExpr(final PostfixOpExprContext ctx) {
    throw new UnsupportedOperationException("todo later");
  }

  public final Node visitPrefixOpExpr(final PrefixOpExprContext ctx) {
    throw new UnsupportedOperationException("todo later");
  }

  public final Node visitInfixOpExpr(final InfixOpExprContext ctx) {
    throw new UnsupportedOperationException();
  }

  public final Node visitAssignmentExpr(final AssignmentExprContext ctx) {
    throw new UnsupportedOperationException();
  }



  public final List<String> visitIdentifier(final IdentifierContext ctx) {
    final List<ParseTree> children = ctx.children;
    final int nChildren = children.size();
    final List<String> path = new ArrayList<>(nChildren / 2 + 1);
    for (int i = 0; i < nChildren; i += 2)
      path.add(children.get(i).getText());
    return path;
  }



  public final Type visitType(final TypeContext ctx) {
    // Note: type-checking must be done in the parent node for implicit casting
    return switch (ctx) {
      case PrimitiveTypeContext t -> visitPrimativeType(t);
      case FunctionTypeContext t -> visitFunctionType(t)
      case NamedTypeContext t -> {
        // Named types are resolved aginst the symbol table
        throw new UnsupportedOperationException();
      }
      default -> throw new AssertionError();
    };
  }

  public final Type visitPrimativeType(final PrimitiveTypeContext ctx) {
    return switch (ctx.token.getType()) {
      case BIT -> Type.bit;
      case U08 -> Type.u08;
      case U16 -> Type.u16;
      case U32 -> Type.u32;
      case U64 -> Type.u64;
      case I08 -> Type.i08;
      case I16 -> Type.i16;
      case I32 -> Type.i32;
      case I64 -> Type.i64;
      case F32 -> Type.f32;
      default -> throw new AssertionError();
    };
  }

  public final Type visitFunctionType(final FunctionTypeContext ctx, final Type childType) {
    // Function argument types
    for (final DeclarationContext arg : ctx.declaration()) {
      final String  argName = arg.id.getText();
      final Type    argType = visitType(arg.type(), null); // do not allow infered types
      final boolean argImmu = switch (arg.modifier.getType()) {
        case LET -> true;
        case VAR -> false;
        default -> throw new AssertionError();
      };
      // TODO add arg to function scope
    }

    // Function return type
    final Type retType = visitType(ctx.type(), childType);

    //
    return new Type.Fun(retType);
  }


}
