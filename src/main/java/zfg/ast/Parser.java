package zfg.ast;

import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.SUB;

import java.lang.foreign.MemorySegment.Scope;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.AssignmentStmtContext;
import zfg.antlr.ZfgParser.CompilationUnitContext;
import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.FunctionCallStmtContext;
import zfg.antlr.ZfgParser.FunctionReturnStmtContext;
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
import zfg.antlr.ZfgParser.PostfixOpExprContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.VariableExprContext;
import zfg.ast.Node.Const;
import zfg.ast.Type.Val;

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

  private static sealed interface Symbol {}
  private static final class LocVar implements Symbol {
    private final String name;
    private final Val type;
    private final int offset;
    LocVar(final String name, final Val type, final int offset) {
      this.name = name;
      this.type = type;
      this.offset = offset;
    }
    private final String name() { return name; }
    private final Val type() { return type; }
    private final int offset() { return offset; }
  }

  private static class SymbolTable {
    private final Stack<Scope> scopes = new Stack<>();

    void pushScope() { scopes.push(null); }
    void popScope() { scopes.pop(); }
    void get(final String name) { throw new UnsupportedOperationException(); }
    void define(final String name) { throw new UnsupportedOperationException(); }
  }

  public final SymbolTable symbolTable = new SymbolTable();

	public final Node visitCompilationUnit(final CompilationUnitContext ctx) {
    final List<Node> children = new ArrayList<>();
    for (final StatementContext statement : ctx.statement())
      children.add(visitStatement(statement));
    return new Node.CompilationUnit(children);
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
    throw new UnsupportedOperationException();
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
    throw new UnsupportedOperationException();
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
    final Symbol id = visitIdentifier(ctx.identifier());


    throw new UnsupportedOperationException();
  }

  public final Node visitLiteralExpr(final LiteralExprContext ctx) {
    final String str = ctx.lit.getText();
    return switch (ctx.lit.getType()) {
      case BitLit -> Literal.parseBit(str)
          .map(val -> new Const(val, Type.bit))
          .orElseThrow(() -> new ParserException(ctx, "Invalid bit literal"));
      case IntLit -> Literal.parseInt(str, hasContiguousNegPrefix(ctx))
          .map(val -> new Const(val, Type.of(val)))
          .orElseThrow(() -> new ParserException(ctx, "Invalid int literal"));
      case FltLit -> Literal.parseFlt(str)
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

  public final Symbol visitIdentifier(final IdentifierContext ctx) {
    final List<ParseTree> children = ctx.children;
    final int nChildren = children.size();

    final List<String> path = new ArrayList<>(nChildren / 2 + 1);
    for (int i = 0; i < nChildren; i += 2) {
      path.add(((TerminalNode) children.get(i)).getSymbol().getText());
    }

    return symbolTable.resolve(ctx, path);

  }




	// public final Node visitNonretBlock(final NonretBlockContext ctx) {
  //   // Post-order traversal
  //   final List<Node> children = new ArrayList<>();
  //   for (final NonretStatementContext statement : ctx.nonretStatement())
  //     children.add(visitNonretStatement(statement));
  //   // Type inference
  //   final Val
  //   return new Block(children, Unit);
  // }
	// public final Node visitReturnBlock(final ReturnBlockContext ctx) {
  //   final List<Node> children = new ArrayList<>();
  //   for (final NonretStatementContext statement : ctx.nonretStatement())
  //     children.add(visitNonretStatement(statement));
  //   return new Block(children);
  // }
	// public final Node visitNonretStatement(final NonretStatementContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitReturnStatement(final ReturnStatementContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitVariableDeclaration(final VariableDeclarationContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitFunctionDeclaration(final FunctionDeclarationContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitExpression(final ExpressionContext ctx) {
  //   return switch (ctx) {
  //     case InvocationExprContext inv -> throw new UnsupportedOperationException();
  //     case VariableExprContext   var -> throw new UnsupportedOperationException();
  //     case LiteralExprContext    lit -> throw new UnsupportedOperationException();
  //     case GroupedExprContext    grp -> visitExpression(grp.expression());
  //     case PostfixOpExprContext  suf -> throw new UnsupportedOperationException();
  //     case PrefixOpExprContext   pre -> throw new UnsupportedOperationException();
  //     case InfixOpExprContext    inf -> throw new UnsupportedOperationException();
  //     case AssignmentExprContext asn -> throw new UnsupportedOperationException();
  //     default -> throw new AssertionError();
  //   };
  // }

  /**
   * class Node {
   * if predicate { Node } elif q { y } then y else Node
   * }
   */

	// public final Node visitInvocationExpr(final InvocationExprContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitVariableExpr(final VariableExprContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitLiteralExpr(final LiteralExprContext ctx) {
  //   return visitChildren(ctx);
  // }

	// public final Node visitExpression(final ExpressionContext ctx) {
  //   return visitChildren(ctx);
  // }

	// public final Node visitGroupedExpr(final GroupedExprContext ctx) {
  //   return visitChildren(ctx);
  // }

	// public final Node visitPostfixOpExpr(final PostfixOpExprContext ctx) {
  //   return visitChildren(ctx);
  // }

	// public final Node visitPrefixOpExpr(final LiteralExprContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitInfixOpExpr(final InfixOpExpr ctx) {
  //   return visitChildren(ctx);
  // }


	// public final Node visitPathExpr(final PathExprContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitPrefixExpr(final PrefixExprContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitAssignExpr(final AssignExprContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitPath(final PathContext ctx) {
  //   return visitChildren(ctx);
  // }
	// public final Node visitIdentifier(final IdentifierContext ctx) {
  //   return visitChildren(ctx);
  // }


  public final Node visit(final ParseTree tree) { throw new AssertionError(); }
  public final Node visitChildren(final RuleNode node) { throw new AssertionError(); }
  public Node visitTerminal(final TerminalNode node) { throw new AssertionError(); }

}
