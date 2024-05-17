package zfg;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AlgebraExpressionContext;
import zfg.antlr.ZfgParser.ArrayLiteralContext;
import zfg.antlr.ZfgParser.ArrayTypeContext;
import zfg.antlr.ZfgParser.AssignmentContext;
import zfg.antlr.ZfgParser.BitwiseChainExpressionContext;
import zfg.antlr.ZfgParser.BitwiseExpressionContext;
import zfg.antlr.ZfgParser.BitwiseShiftExpressionContext;
import zfg.antlr.ZfgParser.BivariateAssignmentContext;
import zfg.antlr.ZfgParser.BivariateAssignmentExpressionContext;
import zfg.antlr.ZfgParser.CompareChainExpressionContext;
import zfg.antlr.ZfgParser.CompareExpressionContext;
import zfg.antlr.ZfgParser.CompareOperandContext;
import zfg.antlr.ZfgParser.CompareThreeWayExpressionContext;
import zfg.antlr.ZfgParser.DeclarationContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallContext;
import zfg.antlr.ZfgParser.FunctionCallExpressionContext;
import zfg.antlr.ZfgParser.FunctionDeclarationContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.LiteralContext;
import zfg.antlr.ZfgParser.LiteralExpressionContext;
import zfg.antlr.ZfgParser.LogicalExpressionContext;
import zfg.antlr.ZfgParser.LogicalOperandContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.NamedTypeContext;
import zfg.antlr.ZfgParser.NumericLiteralContext;
import zfg.antlr.ZfgParser.ParentheticalExpressionContext;
import zfg.antlr.ZfgParser.PostfixAssignmentContext;
import zfg.antlr.ZfgParser.PostfixAssignmentExpressionContext;
import zfg.antlr.ZfgParser.PrefixAssignmentContext;
import zfg.antlr.ZfgParser.PrefixAssignmentExpressionContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.RecordLiteralContext;
import zfg.antlr.ZfgParser.RecordTypeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.StatementsContext;
import zfg.antlr.ZfgParser.TupleLiteralContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.TypeDeclarationContext;
import zfg.antlr.ZfgParser.UnambiguousExpressionContext;
import zfg.antlr.ZfgParser.UnaryExpressionContext;
import zfg.antlr.ZfgParser.VariableContext;
import zfg.antlr.ZfgParser.VariableDeclarationContext;
import zfg.antlr.ZfgParser.VariableExpressionContext;
import zfg.antlr.ZfgToken;
import zfg.antlr.ZfgTokenFactory;

public class Parser2 {

  public static interface Result {
    public static final record Val(Ast value) implements Result {}
    public static final record Err(List<Error> errors) implements Result {}
  }

  public static Result parse(final java.nio.file.Path path) {
    try { return parse(CharStreams.fromPath(path)); }
    catch (final Exception e) { throw new RuntimeException(e); }
  }

  public static Result parse(final String source, final String sourceName) {
    return parse(CharStreams.fromString(source, sourceName));
  }

  public static Result parse(final CharStream source) {
    System.out.println("source: " + source.getSourceName());
    System.out.println(">" + source.toString().replaceAll("\\r?\\n", "\n>"));

    // Lexical Analysis
    final ZfgLexer tokenLexer = new ZfgLexer(source);
    tokenLexer.setTokenFactory(new ZfgTokenFactory());
    final CommonTokenStream tokenStream = new CommonTokenStream(tokenLexer);
    System.out.println("tokens: " + PrettyPrint.tokenStream(tokenLexer, tokenStream));

    // Syntax Analysis
    final ZfgParser syntaxParser = new ZfgParser(tokenStream);
    final ParseTree syntaxTree = syntaxParser.module();
    System.out.println("parsed: " + syntaxTree.toStringTree(syntaxParser));
    System.out.println("tree:\n" + PrettyPrint.syntaxTree(syntaxParser, syntaxTree));

    // Semantic Analysis
    final ParseTreeWalker walker = new ParseTreeWalker();
    final Parser2 listener = new Parser2();
    walker.walk(listener, syntaxTree);

    // // Semantic Analysis
    // final Parser2 parser = new Parser2();
    // final Ast.Module root = parser.parseModule(parsed, source.getSourceName());
    // final List<Error> errors = parser.errors();
    // System.out.println("parsed: " + root);
    // System.out.println("tree: " + root);
    // System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
    // return errors.isEmpty() ? new Result.Val(root) : new Result.Err(errors);
    return null;
  }

  private Parser2() {}

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Errors
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Parser error */
  private static final class Error {
    final ParserRuleContext ctx; // The most speicific context for where the error occurred
    final String msg; // The error message

    private Error(final ParserRuleContext ctx, final String msg) {
      this.ctx = ctx;
      this.msg = msg;
    }

    @Override public String toString() {
      final int line = ((ZfgToken) ctx.start).line;
      final int column = ((ZfgToken) ctx.start).column;
      return String.format("%d:%d: %s", line, column, msg);
    }
  }

  /** List of all reported errors */
  private final List<Error> errors = new ArrayList<>();

  /** Get the list of errors */
  public List<Error> errors() { return errors; }

  /** Report an error */
  private void err(final Error err) { errors.add(err); }
  private void err(final ParserRuleContext ctx, final String msg) { err(new Error(ctx, msg)); }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Module
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitModule(final ModuleContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitModule'");
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statements
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitStatements(final StatementsContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitStatements'");
  }

  public void visitStatement(final StatementContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitStatement'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Declarations
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitDeclaration(final DeclarationContext ctx) {}

  public void visitTypeDeclaration(final TypeDeclarationContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitTypeDeclaration'");
  }

  public void visitFunctionDeclaration(final FunctionDeclarationContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionDeclaration'");
  }

  public void visitVariableDeclaration(final VariableDeclarationContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitVariableDeclaration'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Ast.Expr visitExpression(final ExpressionContext ctx) {
    return ctx.parsed = switch (ctx.children.get(0)) {
      case UnambiguousExpressionContext alt -> visitUnambiguousExpression(alt);
      case AlgebraExpressionContext alt -> visitAlgebraExpression(alt);
      case BitwiseExpressionContext alt -> visitBitwiseExpression(alt);
      case CompareExpressionContext alt -> visitCompareExpression(alt);
      case LogicalExpressionContext alt -> visitLogicalExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr visitUnambiguousExpression(final UnambiguousExpressionContext ctx) {
    return ctx.parsed = switch (ctx) {
      case PostfixAssignmentExpressionContext alt -> visitPostfixAssignmentExpression(alt);
      case PrefixAssignmentExpressionContext alt -> visitPrefixAssignmentExpression(alt);
      case FunctionCallExpressionContext alt -> visitFunctionCallExpression(alt);
      case VariableExpressionContext alt -> visitVariableExpression(alt);
      case LiteralExpressionContext alt -> visitLiteralExpression(alt);
      case UnaryExpressionContext alt -> visitUnaryExpression(alt);
      case BivariateAssignmentExpressionContext alt -> visitBivariateAssignmentExpression(alt);
      case ParentheticalExpressionContext alt -> visitExpression(alt.exp);
      default -> throw new AssertionError();
    }
  }

  public Ast.Expr visitPostfixAssignmentExpression(final PostfixAssignmentExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitPostfixAssignmentExpression'");
  }

  public Ast.Expr visitPrefixAssignmentExpression(final PrefixAssignmentExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitPrefixAssignmentExpression'");
  }

  public Ast.Expr visitFunctionCallExpression(final FunctionCallExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCallExpression'");
  }

  public Ast.Expr visitVariableExpression(final VariableExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitVariableExpression'");
  }

  public Ast.Expr visitLiteralExpression(final LiteralExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitUnaryExpression'");
  }

  public Ast.Expr visitUnaryExpression(final UnaryExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitUnaryExpression'");
  }

  public Ast.Expr visitBivariateAssignmentExpression(final BivariateAssignmentExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitBivariateAssignmentExpression'");
  }

  public Ast.Expr visitAlgebraExpression(final AlgebraExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitAlgebraExpression'");
  }

  public Ast.Expr visitBitwiseExpression(final BitwiseExpressionContext ctx) {
    return ctx.parsed = switch (ctx) {
      case BitwiseChainExpressionContext alt -> visitBitwiseChainExpression(alt);
      case BitwiseShiftExpressionContext alt -> visitBitwiseShiftExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr visitBitwiseChainExpression(final BitwiseChainExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitBitwiseChainExpression'");
  }

  public Ast.Expr visitBitwiseShiftExpression(final BitwiseShiftExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitBitwiseShiftExpression'");
  }

  public Ast.Expr visitCompareExpression(final CompareExpressionContext ctx) {
    return ctx.parsed = switch (ctx) {
      case CompareChainExpressionContext alt -> visitCompareChainExpression(alt);
      case CompareThreeWayExpressionContext alt -> visitCompareThreeWayExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr visitCompareChainExpression(final CompareChainExpressionContext ctx) {
    // Parse the operands
    final int length = ctx.opds.size();
    final Ast.Expr[] opds = new Ast.Expr[length];
    for (int i = 0; i < length; i++) opds[i] = visitCompareOperand(ctx.opds.get(i));
    // Parse the operators
    final Ast.CompareExpr.Opr[] oprs = new Ast.CompareExpr.Opr[length-1];
    for (int i = 0; i < oprs.length; i++) oprs[i] = switch (((ZfgToken) ctx.oprs.get(i)).type) {
      case ZfgLexer.EQL -> Ast.CompareExpr.Opr.EQL;
      case ZfgLexer.NEQ -> Ast.CompareExpr.Opr.NEQ;
      case ZfgLexer.LTN -> Ast.CompareExpr.Opr.LTN;
      case ZfgLexer.LEQ -> Ast.CompareExpr.Opr.LEQ;
      case ZfgLexer.GTN -> Ast.CompareExpr.Opr.GTN;
      case ZfgLexer.GEQ -> Ast.CompareExpr.Opr.GEQ;
      default -> throw new AssertionError();
    };
    throw new UnsupportedOperationException("Unimplemented method 'visitCompareChainExpression'");
  }

  public Ast.Expr visitCompareThreeWayExpression(final CompareThreeWayExpressionContext ctx) {
    // Parse the operands
    final Ast.Expr lhs = visitCompareOperand(ctx.lhs);
    final Ast.Expr rhs = visitCompareOperand(ctx.rhs);
    // Parse the operator
    final Ast.BinaryExpr.Opr = switch (((ZfgToken) ctx.opr).type) {
      case ZfgLexer.TWC -> Ast.BinaryExpr.Opr.TWC;
      default -> throw new AssertionError();
    };
    throw new UnsupportedOperationException("Unimplemented method 'visitCompareThreeWayExpression'");
  }

  public Ast.Expr visitCompareOperand(final CompareOperandContext ctx) {
    return ctx.parsed = switch (ctx.children.get(0)) {
      case BitwiseExpressionContext alt -> visitBitwiseExpression(alt);
      case AlgebraExpressionContext alt -> visitAlgebraExpression(alt);
      case UnambiguousExpressionContext alt -> visitUnambiguousExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr visitLogicalExpression(final LogicalExpressionContext ctx) {
    // Parse the operands
    final int length = ctx.opds.size();
    final Ast.Expr[] opds = new Ast.Expr[length];
    for (int i = 0; i < length; i++) opds[i] = visitLogicalOperand(ctx.opds.get(i));
    // Parse the operator
    final Ast.NaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
      case ZfgLexer.LCJ -> Ast.NaryExpr.Opr.LCJ;
      case ZfgLexer.LDJ -> Ast.NaryExpr.Opr.LDJ;
      default -> throw new AssertionError();
    };
    throw new UnsupportedOperationException("Unimplemented method 'visitLogicalExpression'");
  }
  public static enum LogicalOpr { LCJ, LDJ }

  public Ast.Expr visitLogicalOperand(final LogicalOperandContext ctx) {
    return ctx.parsed = switch (ctx.children.get(0)) {
      case CompareExpressionContext alt -> visitCompareExpression(alt);
      case BitwiseExpressionContext alt -> visitBitwiseExpression(alt);
      case AlgebraExpressionContext alt -> visitAlgebraExpression(alt);
      case UnambiguousExpressionContext alt -> visitUnambiguousExpression(alt);
      default -> throw new AssertionError();
    };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Assignments
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitAssignment(final AssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitAssignment'");
  }

  public void visitBivariateAssignment(final BivariateAssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitBivariateAssignment'");
  }

  public void visitPostfixAssignment(final PostfixAssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitPostfixAssignment'");
  }

  public void visitPrefixAssignment(final PrefixAssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitPrefixAssignment'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Literals
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Inst visitLiteral(final LiteralContext ctx) {
    return ctx.parsed = switch (ctx.children.get(0)) {
      case NumericLiteralContext alt -> visitNumericLiteral(alt);
      case RecordLiteralContext alt -> visitRecordLiteral(alt);
      case TupleLiteralContext alt -> visitTupleLiteral(alt);
      case ArrayLiteralContext alt -> visitArrayLiteral(alt);
      default -> throw new AssertionError();
    };
  }

  public Inst visitNumericLiteral(final NumericLiteralContext ctx) {
    final String text = ctx.token.getText();
    return ctx.parsed = switch (((ZfgToken) ctx.token).type) {
      case ZfgLexer.BitLit -> {
        final Inst parsed = Literals.parseBitLit(text);
        if (parsed == null) err(ctx, "Invalid bit literal: \"" + text + "\"");
        yield parsed;
      }
      case ZfgLexer.IntLit -> {
        final boolean hasMinusPrefix =
            ctx.parent instanceof UnaryExpressionContext parent &&
            ((ZfgToken) parent.opr).type == ZfgLexer.SUB &&
            ((ZfgToken) parent.opr).stopIndex + 1 == ((ZfgToken) ctx.start).startIndex;
        final Inst parsed = Literals.parseIntLit(text, hasMinusPrefix);
        if (parsed == null) err(ctx, "Invalid int literal: \"" + text + "\"");
        yield parsed;
      }
      case ZfgLexer.FltLit -> {
        final Inst parsed = Literals.parseFltLit(text);
        if (parsed == null) err(ctx, "Invalid flt literal: \"" + text + "\"");
        yield parsed;
      }
      default -> throw new AssertionError();
    };
  }

  public Inst visitRecordLiteral(final RecordLiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitRecordLiteral'");
  }

  public Inst visitTupleLiteral(final TupleLiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitTupleLiteral'");
  }

  public Inst visitArrayLiteral(final ArrayLiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitArrayLiteral'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Type visitType(final TypeContext ctx) {
    return ctx.parsed = switch (ctx.children.get(0)) {
      case PrimitiveTypeContext alt -> visitPrimitiveType(alt);
      case FunctionTypeContext alt -> visitFunctionType(alt);
      case RecordTypeContext alt -> visitRecordType(alt);
      case TupleTypeContext alt -> visitTupleType(alt);
      case ArrayTypeContext alt -> visitArrayType(alt);
      case NamedTypeContext alt -> visitNamedType(alt);
      default -> throw new AssertionError();
    };
  }

  public Type visitPrimitiveType(final PrimitiveTypeContext ctx) {
    return ctx.parsed = switch (((ZfgToken) ctx.token).type) {
      case ZfgLexer.BIT -> Types.BIT;
      case ZfgLexer.I08 -> Types.I08;
      case ZfgLexer.I16 -> Types.I16;
      case ZfgLexer.I32 -> Types.I32;
      case ZfgLexer.I64 -> Types.I64;
      case ZfgLexer.U08 -> Types.U08;
      case ZfgLexer.U16 -> Types.U16;
      case ZfgLexer.U32 -> Types.U32;
      case ZfgLexer.U64 -> Types.U64;
      case ZfgLexer.F32 -> Types.F32;
      case ZfgLexer.F64 -> Types.F64;
      default -> throw new AssertionError();
    };
  }

  public Type visitFunctionType(final FunctionTypeContext ctx) {
    // Post-order traversal
    final Type paramsType = visitRecordType(ctx.paramsType);
    final Type returnType = visitType(ctx.returnType);
    // Error checking
    if (paramsType == Types.ERR || returnType == Types.ERR) return ctx.parsed = Types.ERR;
    // Type inference
    return ctx.parsed = Types.Fun(paramsType, returnType);
  }

  public Type visitRecordType(final RecordTypeContext ctx) {
    // Post-order traversal
    final int length = ctx.length;
    final boolean[] muts = new boolean[length];
    for (int i = 0; i < muts.length; i++) muts[i] = ctx.muts.get(i) == null;
    final String[] names = new String[length];
    for (int i = 0; i < names.length; i++) names[i] = ctx.names.get(i).getText();
    final Type[] types = new Type[length];
    for (int i = 0; i < types.length; i++) types[i] = visitType(ctx.types.get(i));
    // Error checking
    if (hasDuplicates(names)) {
      err(ctx, "Record field names must be unique");
      return ctx.parsed = Types.ERR;
    }
    for (int i = 0; i < types.length; i++) if (types[i] == Types.ERR) return ctx.parsed = Types.ERR;
    // Type inference
    return ctx.parsed = Types.Rec(muts, names, types);
  }

  public Type visitTupleType(final TupleTypeContext ctx) {
    // Post-order traversal
    final int length = ctx.length;
    final boolean[] muts = new boolean[length];
    for (int i = 0; i < muts.length; i++) muts[i] = ctx.muts.get(i) == null;
    final Type[] types = new Type[length];
    for (int i = 0; i < types.length; i++) types[i] = visitType(ctx.types.get(i));
    // Error checking
    for (int i = 0; i < types.length; i++) if (types[i] == Types.ERR) return ctx.parsed = Types.ERR;
    // Type inference
    return ctx.parsed = Types.Tup(muts, types);
  }

  public Type visitArrayType(final ArrayTypeContext ctx) {
    // Post-order traversal
    final boolean mut = ctx.mut != null;
    final Type type = visitType(ctx.elem);
    final int length = switch (ctx.length) {
      case null -> Type.Arr.UNKNOWN_LENGTH;
      case Token t when Literals.parseIntLit(t.getText(), false) instanceof Inst.I32 i -> i.value;
      default -> Integer.MIN_VALUE;
    };
    // Error checking
    if (length < Type.Arr.UNKNOWN_LENGTH) {
      err(ctx, "Array length must be an i32 literal");
      return ctx.parsed = Types.ERR;
    }
    if (type == Types.ERR) return ctx.parsed = Types.ERR;
    // Type inference
    return ctx.parsed = length == -1 ? Types.Arr(mut, type) : Types.Arr(mut, type, length);
  }

  public Type visitNamedType(final NamedTypeContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitNamedType'");
  }

  private static boolean hasDuplicates(final String[] names) {
    final int length = names.length;
    if (length < 32) {
      for (int i = length - 1; i >= 1; i -= 1) {
        final String name = names[i];
        for (int j = i - 1; j >= 0; j -= 1) {
          if (name.equals(names[j])) {
            return true;
          }
        }
      }
      return false;
    } else {
      final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
      for (int i = 0; i < length; i++) {
        if (!set.add(names[i])) {
          return true;
        }
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Function Call
  //////////////////////////////////////////////////////////////////////////////////////////////////


  public void visitFunctionCall(final FunctionCallContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Variable
  //////////////////////////////////////////////////////////////////////////////////////////////////


  public void visitVariable(final VariableContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitVariable'");
  }
}
