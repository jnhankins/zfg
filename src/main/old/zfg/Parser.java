package zfg;

import static zfg.literals.parseBitLit;
import static zfg.literals.parseFltLit;
import static zfg.literals.parseIntLit;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.insts.I32Inst;
import zfg.insts.Inst;
import zfg.nodes.AssignExpr;
import zfg.nodes.AssignStmt;
import zfg.nodes.BinaryExpr;
import zfg.nodes.CastExpr;
import zfg.nodes.ConstantExpr;
import zfg.nodes.Expr;
import zfg.nodes.FunCallExpr;
import zfg.nodes.FunCallStmt;
import zfg.nodes.FunRef;
import zfg.nodes.NaryExpr;
import zfg.nodes.Node;
import zfg.nodes.Stmt;
import zfg.nodes.UnaryExpr;
import zfg.nodes.VarLoadExpr;
import zfg.nodes.VarRef;
import zfg.types.RecType;
import zfg.types.Type;
import zfg.types.TypeCache;
import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AlgebraAssignContext;
import zfg.antlr.ZfgParser.AlgebraCompareOpdContext;
import zfg.antlr.ZfgParser.AlgebraExprContext;
import zfg.antlr.ZfgParser.AlgebraExpressionContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprAAContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprAUContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprUAContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprUUContext;
import zfg.antlr.ZfgParser.AlgebraLogicalOpdContext;
import zfg.antlr.ZfgParser.AnyTypeContext;
import zfg.antlr.ZfgParser.ArrTypeContext;
import zfg.antlr.ZfgParser.ArrayTypeContext;
import zfg.antlr.ZfgParser.AssignmentContext;
import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.AssignmentStatementContext;
import zfg.antlr.ZfgParser.BitwiseAssignContext;
import zfg.antlr.ZfgParser.BitwiseChianExprContext;
import zfg.antlr.ZfgParser.BitwiseCompareOpdContext;
import zfg.antlr.ZfgParser.BitwiseExprContext;
import zfg.antlr.ZfgParser.BitwiseExpressionContext;
import zfg.antlr.ZfgParser.BitwiseInfixExprContext;
import zfg.antlr.ZfgParser.BitwiseLogicalOpdContext;
import zfg.antlr.ZfgParser.BwShiftAssignContext;
import zfg.antlr.ZfgParser.CompareChianExprContext;
import zfg.antlr.ZfgParser.CompareExprContext;
import zfg.antlr.ZfgParser.CompareExpressionContext;
import zfg.antlr.ZfgParser.CompareInfixExprContext;
import zfg.antlr.ZfgParser.CompareLogicalOpdContext;
import zfg.antlr.ZfgParser.CompareOperandContext;
import zfg.antlr.ZfgParser.CrementAssignContext;
import zfg.antlr.ZfgParser.CrementAssignmentContext;
import zfg.antlr.ZfgParser.CrementExprContext;
import zfg.antlr.ZfgParser.DefinitionStatementContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunCallExprContext;
import zfg.antlr.ZfgParser.FunTypeContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.InvocationContext;
import zfg.antlr.ZfgParser.InvocationStatementContext;
import zfg.antlr.ZfgParser.LiteralContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.LogicalChianExprContext;
import zfg.antlr.ZfgParser.LogicalExprContext;
import zfg.antlr.ZfgParser.LogicalExpressionContext;
import zfg.antlr.ZfgParser.LogicalOperandContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.NumericLitContext;
import zfg.antlr.ZfgParser.NumericLiteralContext;
import zfg.antlr.ZfgParser.PathContext;
import zfg.antlr.ZfgParser.PrecedenceExprContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.PrefixSuccesorContext;
import zfg.antlr.ZfgParser.PriTypeContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.RecTypeContext;
import zfg.antlr.ZfgParser.RecordTypeContext;
import zfg.antlr.ZfgParser.ScopeContext;
import zfg.antlr.ZfgParser.SettingAssignContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.SuffixSuccesorContext;
import zfg.antlr.ZfgParser.TupTypeContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.UnambigCompareOpdContext;
import zfg.antlr.ZfgParser.UnambigExprContext;
import zfg.antlr.ZfgParser.UnambigExpressionContext;
import zfg.antlr.ZfgParser.UnambigLogicalOpdContext;
import zfg.antlr.ZfgParser.VarLoadExprContext;

public final class Parser {

  public static interface Result {
    public static final record Val(Node value) implements Result {}
    public static final record Err(List<Error> errors) implements Result {}
  }

  public static Result parse(final Path path) {
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
    final ZfgLexer zfgLexer = new ZfgLexer(source);
    final CommonTokenStream tokens = new CommonTokenStream(zfgLexer);
    System.out.println("tokens: " + PrettyPrint.toPrettyTokensString(zfgLexer, tokens));

    // Syntax Analysis
    final ZfgParser zfgParser = new ZfgParser(tokens);
    final ModuleContext parsed = zfgParser.module();
    System.out.println("parsed: " + parsed.toStringTree(zfgParser));
    System.out.println("tree:\n" + PrettyPrint.toPrettyTreeString(zfgParser, parsed));

    // Semantic Analysis
    final Parser parser = new Parser();
    final Node root = parser.parseModule(parsed);
    final List<Error> errors = parser.errors();
    System.out.println("parsed: " + root);
    System.out.println("tree: " + root);
    System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
    return errors.isEmpty() ? new Result.Val(root) : new Result.Err(errors);
  }

  private Parser() {}


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Errors
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Parser error */
  private static final class Error {
    final ParserRuleContext ctx; // The most speicific context for where the error occurred
    final String msg;            // The error message

    private Error(final ParserRuleContext ctx, final String msg) {
      this.ctx = ctx;
      this.msg = msg;
    }

    @Override public String toString() {
      final int line = ctx.start.getLine();
      final int column = ctx.start.getCharPositionInLine();
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
  // Module, Scopes, Statements
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private void parseModule(final ModuleContext ctx) {
    final List<Node> statements = ctx.body != null
      ? parseScope(ctx.body)
      : Collections.emptyList();
    // TODO...
  }

  private List<Node> parseScope(final ScopeContext ctx) {
    final List<Node> statements; {
      final List<StatementContext> children = ctx.statements;
      final int childrenSize = children.size();
      statements = new ArrayList<>();
      for (int i = 0; i < childrenSize; i++) {
        final Node statementNode = parseStatement(children.get(i));
        statements.add(statementNode);
      }
    }
    return statements;
  }

  private Node parseStatement(final StatementContext ctx) {
    return switch (ctx) {
      case DefinitionStatementContext stmt -> parseDefinitionStatement(stmt);
      case AssignmentStatementContext stmt -> parseAssignmentStatement(stmt);
      case InvocationStatementContext stmt -> parseInvocationStatement(stmt);
      default -> throw new AssertionError();
    };
  }

  private Node parseDefinitionStatement(final DefinitionStatementContext ctx) {
    throw new UnsupportedOperationException("TODO"); // TODO
  }

  private Stmt parseAssignmentStatement(final AssignmentStatementContext ctx) {
    final Node node = parseAssignment(ctx.child, false);
    assert node == null || node instanceof Stmt;
    return (Stmt) node;
  }

  private Node parseInvocationStatement(final InvocationStatementContext ctx) {
    final Node node = parseInvocation(ctx.child, false);
    assert node == null || node instanceof Stmt;
    return (Stmt) node;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Expr parseExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case UnambigExprContext expr -> parseUnambigExpression(expr.expr);
      case AlgebraExprContext expr -> parseAlgebraExpression(expr.expr);
      case BitwiseExprContext expr -> parseBitwiseExpression(expr.expr);
      case CompareExprContext expr -> parseCompareExpression(expr.expr);
      case LogicalExprContext expr -> parseLogicalExpression(expr.expr);
      default -> throw new AssertionError();
    };
  }

  private Expr parseUnambigExpression(final UnambigExpressionContext ctx) {
    return switch (ctx) {
      case CrementExprContext    expr -> parseCrementExpr(expr);
      case FunCallExprContext    expr -> parseFunCallExpr(expr);
      case VarLoadExprContext    expr -> parseVarLoadExpr(expr);
      case LiteralExprContext    expr -> parseLiteralExpr(expr);
      case PrefixOpExprContext   expr -> parsePrefixOpExpr(expr);
      case AssignmentExprContext expr -> parseAssignmentExpr(expr);
      case PrecedenceExprContext expr -> parsePrecedenceExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Expr parseCrementExpr(final CrementExprContext ctx) {
    final Node node = parseCrementAssignment(ctx.expr, true);
    assert node == null || node instanceof Expr;
    return (Expr) node;
  }

  private Expr parseFunCallExpr(final FunCallExprContext ctx) {
    final Node node = parseInvocation(ctx.expr, false);
    assert node == null || node instanceof Expr;
    return (Expr) node;
  }

  private Expr parseVarLoadExpr(final VarLoadExprContext ctx) {
    final VarRef var = parseVarRef(ctx.expr, Mode.READ);
    return var == null ? null : new VarLoadExpr(var);
  }

  private Expr parseLiteralExpr(final LiteralExprContext ctx) {
    final Inst val = parseLiteral(ctx.expr);
    return val == null ? null : new ConstantExpr(val);
  }

  private Expr parsePrefixOpExpr(final PrefixOpExprContext ctx) {
    // 1. Parse the operands
    final Expr opd = parseUnambigExpression(ctx.rhs);
    // 2. Parse the operator
    final UnaryExpr.Opr opr = switch (ctx.opr.getType()) {
      case ZfgLexer.ADD -> null;
      case ZfgLexer.SUB -> UnaryExpr.Opr.NEG;
      case ZfgLexer.NOT -> UnaryExpr.Opr.NOT;
      case ZfgLexer.LNT -> UnaryExpr.Opr.LNT;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (opd == null) return null;
    // 4. Operand type checking
    switch (opr) {
      case null:
        if (!isValidOperand(ctx.opr, ctx.rhs, opd, ALGEBRA_ACCEPTS)) return null;
        return opd; // <-- early escape for unary plus since it's a no-op
      case UnaryExpr.Opr.NEG:
        if (!isValidOperand(ctx.opr, ctx.rhs, opd, ALGEBRA_ACCEPTS)) return null;
        break;
      case UnaryExpr.Opr.NOT:
        if (!isValidOperand(ctx.opr, ctx.rhs, opd, BITWISE_ACCEPTS)) return null;
        break;
      case UnaryExpr.Opr.LNT:
        if (!isValidOperand(ctx.opr, ctx.rhs, opd, LOGICAL_ACCEPTS)) return null;
        break;
      default: throw new AssertionError();
    }
    // 5. Output type deduction
    final Type outType = opd.type();
    // 6. Implicit casting
    // N/A
    // 7. Construct the node
    return new UnaryExpr(outType, opr, opd);
  }

  private Expr parseAssignmentExpr(final AssignmentExprContext ctx) {
    final Node node = parseAssignment(ctx.expr, true);
    assert node == null || node instanceof Expr;
    return (Expr) node;
  }

  private Expr parsePrecedenceExpr(final PrecedenceExprContext ctx) {
    return parseExpression(ctx.expr);
  }

  private Expr parseAlgebraExpression(final AlgebraExpressionContext ctx) {
    return switch (ctx) {
      case AlgebraInfixExprAAContext expr -> parseAlgebraExpr(expr, expr.lhs, expr.rhs, expr.opr);
      case AlgebraInfixExprAUContext expr -> parseAlgebraExpr(expr, expr.lhs, expr.rhs, expr.opr);
      case AlgebraInfixExprUAContext expr -> parseAlgebraExpr(expr, expr.lhs, expr.rhs, expr.opr);
      case AlgebraInfixExprUUContext expr -> parseAlgebraExpr(expr, expr.lhs, expr.rhs, expr.opr);
      default -> throw new AssertionError();
    };
  }

  private Expr parseAlgebraExpr(
    final ParserRuleContext ctx,
    final ParserRuleContext operand0,
    final ParserRuleContext operand1,
    final Token             oparator
  ) {
    // 1. Parse the operands
    final Expr lhs = parseAlgebraOperand(operand0);
    final Expr rhs = parseAlgebraOperand(operand1);
    // 2. Parse the operator
    final BinaryExpr.Opr opr = switch (oparator.getType()) {
      case ZfgLexer.ADD -> BinaryExpr.Opr.ADD;
      case ZfgLexer.SUB -> BinaryExpr.Opr.SUB;
      case ZfgLexer.MUL -> BinaryExpr.Opr.MUL;
      case ZfgLexer.DIV -> BinaryExpr.Opr.DIV;
      case ZfgLexer.REM -> BinaryExpr.Opr.REM;
      case ZfgLexer.MOD -> BinaryExpr.Opr.MOD;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (lhs == null || rhs == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(oparator, operand0, operand1, lhs, rhs, ALGEBRA_ACCEPTS)) return null;
    // 5. Output type deduction
    final Type outType = commonType(ctx, oparator, lhs, rhs);
    if (outType == types.Err) return null;
    // 6. Implicit casting
    final Expr lhsOpd = castIfNeeded(outType, lhs);
    final Expr rhsOpd = castIfNeeded(outType, rhs);
    // 7. Construct the node
    return new BinaryExpr(outType, opr, lhsOpd, rhsOpd);
  }

  private Expr parseAlgebraOperand(final ParserRuleContext ctx) {
    return switch (ctx) {
      case AlgebraExpressionContext opd -> parseAlgebraExpression(opd);
      case UnambigExpressionContext opd -> parseUnambigExpression(opd);
      default -> throw new AssertionError();
    };
  }

  private Expr parseBitwiseExpression(final BitwiseExpressionContext ctx) {
    return switch (ctx) {
      case BitwiseChianExprContext expr -> parseBitwiseChianExpr(expr);
      case BitwiseInfixExprContext expr -> parseBitwiseInfixExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Expr parseBitwiseChianExpr(final BitwiseChianExprContext ctx) {
    final List<UnambigExpressionContext> operands = ctx.opd;
    final int operandsSize = operands.size();
    // 1. Parse the operands
    final Expr[] opds = new Expr[operandsSize];
    for (int i = 0; i < operandsSize; i++) opds[i] = parseUnambigExpression(operands.get(i));
    // 2. Parse the operator
    final NaryExpr.Opr opr = switch (ctx.opr.getType()) {
      case ZfgLexer.AND -> NaryExpr.Opr.AND;
      case ZfgLexer.IOR -> NaryExpr.Opr.IOR;
      case ZfgLexer.XOR -> NaryExpr.Opr.XOR;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    for (int i = 0; i < operandsSize; i++) if (opds[i] == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(ctx.opr, operands, opds, BITWISE_ACCEPTS)) return null;
    // 5. Output type deduction
    final Type outType = commonType(ctx, ctx.opr, opds);
    if (outType == types.Err) return null;
    // 6. Implicit casting
    for (int i = 0; i < operandsSize; i++) opds[i] = castIfNeeded(outType, opds[i]);
    // 7. Construct the node
    return new NaryExpr(outType, opr, opds);
  }

  private Expr parseBitwiseInfixExpr(final BitwiseInfixExprContext ctx) {
    // 1. Parse the operands
    final Expr lhs = parseUnambigExpression(ctx.lhs);
    final Expr rhs = parseUnambigExpression(ctx.rhs);
    // 2. Parse the operator
    final BinaryExpr.Opr opr = switch (ctx.opr.getType()) {
      case ZfgLexer.SHL -> BinaryExpr.Opr.SHL;
      case ZfgLexer.SHR -> BinaryExpr.Opr.SHR;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (lhs == null || rhs == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(ctx.opr, ctx.lhs, ctx.rhs, lhs, rhs, BWSHIFT_ACCEPTS)) return null;
    // 5. Output type deduction
    final Type outType = lhs.type();
    // 6. Implicit casting
    final Expr lhsOpd = lhs;
    final Expr rhsOpd = castIfNeeded(types.I32, rhs);
    // 7. Construct the node
    return new BinaryExpr(outType, opr, lhsOpd, rhsOpd);
  }

  private Expr parseCompareExpression(final CompareExpressionContext ctx) {
    return switch (ctx) {
      case CompareChianExprContext expr -> parseCompareChianExpr(expr);
      case CompareInfixExprContext expr -> parseCompareInfixExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Expr parseCompareChianExpr(final CompareChianExprContext ctx) {
    // 1. Parse the operands
    // 2. Parse the operator
    // 3. Error propagation
    // 4. Operand type checking
    // 5. Output type deduction
    // 6. Implicit casting
    // 7. Construct the node
    throw new UnsupportedOperationException("TODO"); // TODO
  }

  private Expr parseCompareInfixExpr(final CompareInfixExprContext ctx) {
    // 1. Parse the operands
    final Expr lhs = parseCompareOperand(ctx.lhs);
    final Expr rhs = parseCompareOperand(ctx.rhs);
    // 2. Parse the operator
    final BinaryExpr.Opr opr = switch (ctx.opr.getType()) {
      case ZfgLexer.TWC -> BinaryExpr.Opr.TWC;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (lhs == null || rhs == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(ctx.opr, ctx.lhs, ctx.rhs, lhs, rhs, COMPARE_ACCEPTS)) return null;
    // 5. Output type deduction
    final Type outType = types.I08;
    // 6. Implicit casting
    final Type commonType = commonType(ctx, ctx.opr, lhs, rhs);
    if (commonType == types.Err) return null;
    final Expr lhsOpd = castIfNeeded(commonType, lhs);
    final Expr rhsOpd = castIfNeeded(commonType, rhs);
    // 7. Construct the node
    return new BinaryExpr(outType, opr, lhsOpd, rhsOpd);
  }

  private Expr parseCompareOperand(final CompareOperandContext ctx) {
    return switch (ctx) {
      case BitwiseCompareOpdContext opd -> parseBitwiseExpression(opd.expr);
      case AlgebraCompareOpdContext opd -> parseAlgebraExpression(opd.expr);
      case UnambigCompareOpdContext opd -> parseUnambigExpression(opd.expr);
      default -> throw new AssertionError();
    };
  }

  private Expr parseLogicalExpression(final LogicalExpressionContext ctx) {
    return switch (ctx) {
      case LogicalChianExprContext expr -> parseLogicalChianExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Expr parseLogicalChianExpr(final LogicalChianExprContext ctx) {
    final List<LogicalOperandContext> children = ctx.opd;
    final int childrenSize = children.size();
    // 1. Parse the operands
    final Expr[] opds = new Expr[childrenSize];
    for (int i = 0; i < childrenSize; i++) opds[i] = parseLogicalOperand(children.get(i));
    // 2. Parse the operator
    final NaryExpr.Opr opr = switch (ctx.opr.getType()) {
      case ZfgLexer.LCJ -> NaryExpr.Opr.LCJ;
      case ZfgLexer.LDJ -> NaryExpr.Opr.LDJ;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    for (int i = 0; i < childrenSize; i++) if (opds[i] == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(ctx.opr, children, opds, LOGICAL_ACCEPTS)) return null;
    // 5. Output type deduction
    final Type outType = types.Bit;
    // 6. Implicit casting
    // N/A
    // 7. Construct the node
    return new NaryExpr(outType, opr, opds);
  }

  private Expr parseLogicalOperand(final LogicalOperandContext ctx) {
    return switch (ctx) {
      case CompareLogicalOpdContext opd -> parseCompareExpression(opd.expr);
      case BitwiseLogicalOpdContext opd -> parseBitwiseExpression(opd.expr);
      case AlgebraLogicalOpdContext opd -> parseAlgebraExpression(opd.expr);
      case UnambigLogicalOpdContext opd -> parseUnambigExpression(opd.expr);
      default -> throw new AssertionError();
    };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Assignment
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Node parseAssignment(final AssignmentContext ctx, final boolean isExpr) {
    return switch (ctx) {
      case SettingAssignContext assign -> parseSettingAssign(assign, isExpr);
      case AlgebraAssignContext assign -> parseAlgebraAssign(assign, isExpr);
      case BitwiseAssignContext assign -> parseBitwiseAssign(assign, isExpr);
      case BwShiftAssignContext assign -> parseBwShiftAssign(assign, isExpr);
      case CrementAssignContext assign -> parseCrementAssign(assign, isExpr);
      default -> throw new AssertionError();
    };
  }

  private Node parseSettingAssign(final SettingAssignContext ctx, final boolean isExpr) {
    // 1. Parse the operands
    final VarRef var = parseVarRef(ctx.lhs, Mode.WRITE);
    final VarLoadExpr lhs = var == null ? null : new VarLoadExpr(var);
    final Expr rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    switch (ctx.opr.getType()) {
      case ZfgLexer.SETA: break;
      default: throw new AssertionError();
    };
    // 3. Error propagation
    if (lhs == null || rhs == null) return null;
    // 4. Operand type checking
    if (!isAssignableType(ctx, ctx.opr, lhs, rhs)) return null;
    // 5. Output type deduction
    final Type outType = lhs.type();
    // 6. Implicit casting
    final Expr opd = castIfNeeded(outType, rhs);
    // 7. Construct the node
    return isExpr
      ? new AssignExpr(AssignExpr.Mode.SET_GET, var, opd)
      : new AssignStmt(var, opd);
  }

  private Node parseAlgebraAssign(final AlgebraAssignContext ctx, final boolean isExpr) {
    // 1. Parse the operands
    final VarRef var = parseVarRef(ctx.lhs, Mode.READWRITE);
    final VarLoadExpr lhs = var == null ? null : new VarLoadExpr(var);
    final Expr rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    final BinaryExpr.Opr opr = switch (ctx.opr.getType()) {
      case ZfgLexer.ADDA -> BinaryExpr.Opr.ADD;
      case ZfgLexer.SUBA -> BinaryExpr.Opr.SUB;
      case ZfgLexer.MULA -> BinaryExpr.Opr.MUL;
      case ZfgLexer.DIVA -> BinaryExpr.Opr.DIV;
      case ZfgLexer.REMA -> BinaryExpr.Opr.REM;
      case ZfgLexer.MODA -> BinaryExpr.Opr.MOD;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (lhs == null || rhs == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(ctx.opr, ctx.lhs, ctx.rhs, lhs, rhs, ALGEBRA_ACCEPTS)) return null;
    if (!isAssignableType(ctx, ctx.opr, lhs, rhs)) return null;
    // 5. Output type deduction
    final Type outType = lhs.type();
    // 6. Implicit casting
    final Expr opd = castIfNeeded(outType, rhs);
    // 7. Construct the node
    final Expr expr = new BinaryExpr(outType, opr, lhs, opd);
    return isExpr
      ? new AssignExpr(AssignExpr.Mode.SET_GET, var, expr)
      : new AssignStmt(var, expr);
  }

  private Node parseBitwiseAssign(final BitwiseAssignContext ctx, final boolean isExpr) {
    // 1. Parse the operands
    final VarRef var = parseVarRef(ctx.lhs, Mode.READWRITE);
    final VarLoadExpr lhs = var == null ? null : new VarLoadExpr(var);
    final Expr rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    final NaryExpr.Opr op = switch (ctx.opr.getType()) {
      case ZfgLexer.ANDA -> NaryExpr.Opr.AND;
      case ZfgLexer.IORA -> NaryExpr.Opr.IOR;
      case ZfgLexer.XORA -> NaryExpr.Opr.XOR;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (lhs == null || rhs == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(ctx.opr, ctx.lhs, ctx.rhs, lhs, rhs, BITWISE_ACCEPTS)) return null;
    if (!isAssignableType(ctx, ctx.opr, lhs, rhs)) return null;
    // 5. Output type deduction
    final Type outType = lhs.type();
    // 6. Implicit casting
    final Expr opd = castIfNeeded(outType, rhs);
    // 7. Construct the node
    final Expr expr = new NaryExpr(outType, op, new Expr[]{lhs, opd});
    return isExpr
      ? new AssignExpr(AssignExpr.Mode.SET_GET, var, expr)
      : new AssignStmt(var, expr);
  }

  private Node parseBwShiftAssign(final BwShiftAssignContext ctx, final boolean isExpr) {
    // 1. Parse the operands
    final VarRef var = parseVarRef(ctx.lhs, Mode.READWRITE);
    final VarLoadExpr lhs = var == null ? null : new VarLoadExpr(var);
    final Expr rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    final BinaryExpr.Opr opr = switch (ctx.opr.getType()) {
      case ZfgLexer.SHLA -> BinaryExpr.Opr.SHL;
      case ZfgLexer.SHRA -> BinaryExpr.Opr.SHR;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (lhs == null || rhs == null) return null;
    // 4. Operand type checking
    if (!isValidOperands(ctx.opr, ctx.lhs, ctx.rhs, lhs, rhs, BITWISE_ACCEPTS)) return null;
    // 5. Output type deduction
    final Type outType = lhs.type();
    // 6. Implicit casting
    final Expr opd = castIfNeeded(types.I32, rhs);
    // 7. Construct the node
    final Expr expr = new BinaryExpr(outType, opr, lhs, opd);
    return isExpr
      ? new AssignExpr(AssignExpr.Mode.SET_GET, var, expr)
      : new AssignStmt(var, expr);
  }

  private Node parseCrementAssign(final CrementAssignContext ctx, final boolean isExpr) {
    return parseCrementAssignment(ctx.assign, isExpr);
  }

  private Node parseCrementAssignment(final CrementAssignmentContext ctx, final boolean isExpr) {
    return switch (ctx) {
      case SuffixSuccesorContext suc -> parseCrementAssignment(
        AssignExpr.Mode.GET_SET, suc.opr, suc.lhs, isExpr);
      case PrefixSuccesorContext suc -> parseCrementAssignment(
        AssignExpr.Mode.SET_GET, suc.opr, suc.lhs, isExpr);
      default -> throw new AssertionError();
    };
  }

  private Node parseCrementAssignment(
    final AssignExpr.Mode mode,
    final Token           operator,
    final PathContext     operand,
    final boolean         isExpr
  ) {
    // 1. Parse the operands
    final VarRef var = parseVarRef(operand, Mode.READWRITE);
    final VarLoadExpr opd = var == null ? null : new VarLoadExpr(var);
    // 2. Parse the operator
    final UnaryExpr.Opr opr = switch (operator.getType()) {
      case ZfgLexer.INC -> UnaryExpr.Opr.INC;
      case ZfgLexer.DEC -> UnaryExpr.Opr.DEC;
      default -> throw new AssertionError();
    };
    // 3. Error propagation
    if (var == null || opd == null) return null;
    // 4. Operand type checking and error propagation
    if (!isValidOperand(operator, operand, opd, ALGEBRA_ACCEPTS)) return null;
    // 5. Output type deduction
    final Type outType = opd.type();
    // 6. Implicit casting
    // N/A
    // 7. Construct the node
    final Expr expr = new UnaryExpr(outType, opr, opd);
    return isExpr
      ? new AssignExpr(mode, var, expr)
      : new AssignStmt(var, expr);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Literals
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Inst parseLiteral(final LiteralContext ctx) {
    return switch (ctx) {
      case NumericLitContext lit -> parseNumericLit(lit.lit);
      default -> throw new AssertionError();
    };
  }

  private Inst parseNumericLit(final NumericLiteralContext ctx) {
    final String text = ctx.token.getText();
    final int    type = ctx.token.getType();
    switch (type) {
      case ZfgLexer.BitLit: {
        final Inst parsed = parseBitLit(text);
        if (parsed == null) err(ctx, "Invalid bit literal: \"" + text + "\"");
        return parsed;
      }
      case ZfgLexer.IntLit: {
        final boolean hasMinusPrefix =
            ctx.parent instanceof PrefixOpExprContext parent &&
            parent.opr.getType() == ZfgLexer.SUB &&
            parent.opr.getStopIndex() + 1 == ctx.getStart().getStartIndex();
        final Inst parsed = parseIntLit(text, hasMinusPrefix);
        if (parsed == null) err(ctx, "Invalid int literal: \"" + text + "\"");
        return parsed;
      }
      case ZfgLexer.FltLit: {
        final Inst parsed = parseFltLit(text);
        if (parsed == null) err(ctx, "Invalid flt literal: \"" + text + "\"");
        return parsed;
      }
      default: throw new AssertionError();
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private final TypeCache typeCache = new TypeCache();

  private Type parseAnyType(final AnyTypeContext ctx) {
    return switch (ctx) {
      case FunTypeContext type -> parseFunctionType(type.type);
      case RecTypeContext type -> parseRecordType(type.type);
      case TupTypeContext type -> parseTupleType(type.type);
      case ArrTypeContext type -> parseArrayType(type.type);
      case PriTypeContext type -> parsePrimitiveType(type.type);
      default -> throw new AssertionError();
    };
  }

  private Type parseFunctionType(final FunctionTypeContext ctx) {
    final List<Token>          fieldModifiers = ctx.parameterModifiers;
    final List<Token>          fieldNames = ctx.parameterNames;
    final List<AnyTypeContext> fieldTypes = ctx.parameterTypes;
    final int fieldCount = fieldModifiers.size();
    assert fieldNames.size() == fieldCount;
    assert fieldTypes.size() == fieldCount;
    // Parse the parameter modifiers
    final boolean[] muts = new boolean[fieldCount];
    for (int i = 0; i < fieldCount; i++) muts[i] = parseTypeModifier(fieldModifiers.get(i));
    // Parse the parameter names
    final String[] names = new String[fieldCount];
    for (int i = 0; i < fieldCount; i++) names[i] = fieldNames.get(i).getText();
    // Parse the parameter types
    final Type[] typez = new Type[fieldCount];
    for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
    // Parse the reutrn type
    final Type returnType = parseAnyType(ctx.returnType);
    // Unique names check
    if (!uniqueNames(names)) return types.Err;
    // Error propagation
    for (int i = 0; i < fieldCount; i++) if (typez[i] == types.Err) return types.Err;
    if (returnType == types.Err) return types.Err;
    // Construct the node
    final RecType parametersType = (RecType) typeCache.Rec(muts, names, typez);
    return typeCache.Fun(parametersType, returnType);
  }

  private Type parseRecordType(final RecordTypeContext ctx) {
    final List<Token>          fieldModifiers = ctx.fieldModifiers;
    final List<Token>          fieldNames = ctx.fieldNames;
    final List<AnyTypeContext> fieldTypes = ctx.fieldTypes;
    final int fieldCount = fieldModifiers.size();
    assert fieldNames.size() == fieldCount;
    assert fieldTypes.size() == fieldCount;
    // Parse the modifiers
    final boolean[] muts = new boolean[fieldCount];
    for (int i = 0; i < fieldCount; i++) muts[i] = parseTypeModifier(fieldModifiers.get(i));
    // Parse the names
    final String[] names = new String[fieldCount];
    for (int i = 0; i < fieldCount; i++) names[i] = fieldNames.get(i).getText();
    // Parse the types
    final Type[] typez = new Type[fieldCount];
    for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
    // Unique names check
    if (!uniqueNames(names)) return types.Err;
    // Error propagation
    for (int i = 0; i < fieldCount; i++) if (typez[i] == types.Err) return types.Err;
    // Construct the node
    return typeCache.Rec(muts, names, typez);
  }

  private Type parseTupleType(final TupleTypeContext ctx) {
    final List<Token>          fieldModifiers = ctx.fieldModifiers;
    final List<AnyTypeContext> fieldTypes = ctx.fieldTypes;
    final int fieldCount = fieldModifiers.size();
    assert fieldTypes.size() == fieldCount;
    // Parse the modifiers
    final boolean[] muts = new boolean[fieldCount];
    for (int i = 0; i < fieldCount; i++) muts[i] = parseTypeModifier(fieldModifiers.get(i));
    // Parse the types
    final Type[] typez = new Type[fieldCount];
    for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
    // Error propagation
    for (int i = 0; i < fieldCount; i++) if (typez[i] == types.Err) return types.Err;
    // Construct the node
    return typeCache.Tup(muts, typez);
  }

  private Type parseArrayType(final ArrayTypeContext ctx) {
    // Parse the modifier
    final boolean mut = switch (ctx.elementsModifier.getType()) {
      case ZfgLexer.LET -> false;
      case ZfgLexer.MUT -> true;
      default -> throw new AssertionError();
    };
    // Parse the type
    final Type type = parseAnyType(ctx.elementsType);
    // Parse the length
    final int length;
    if (ctx.elementCount == null) {
      length = -1;
    } else if (parseIntLit(ctx.elementCount.getText(), false) instanceof I32Inst i32) {
      length = i32.value;
    } else {
      err(ctx, "Invalid array length: " + ctx.elementCount.getText());
      length = -2;
    }
    // Error propagation
    if (type == types.Err || length == -2) return types.Err;
    // Construct the node
    return length == -1
      ? typeCache.Arr(mut, type)
      : typeCache.Arr(mut, type, length);
  }

  private Type parsePrimitiveType(final PrimitiveTypeContext ctx) {
    return switch (ctx.token.getType()) {
      case ZfgLexer.BIT -> types.Bit;
      case ZfgLexer.I08 -> types.I08;
      case ZfgLexer.I16 -> types.I16;
      case ZfgLexer.I32 -> types.I32;
      case ZfgLexer.I64 -> types.I64;
      case ZfgLexer.U08 -> types.U08;
      case ZfgLexer.U16 -> types.U16;
      case ZfgLexer.U32 -> types.U32;
      case ZfgLexer.U64 -> types.U64;
      case ZfgLexer.F32 -> types.F32;
      case ZfgLexer.F64 -> types.F64;
      default -> throw new AssertionError();
    };
  }

  private static boolean parseTypeModifier(final Token token) {
    return switch (token.getType()) {
      case ZfgLexer.LET -> false;
      case ZfgLexer.MUT -> true;
      default -> throw new AssertionError();
    };
  }

  private static boolean uniqueNames(final String[] names) {
    final int length = names.length;
    if (length < 32) {
      for (int i = length - 1; i >= 1; i -= 1) {
        final String name = names[i];
        for (int j = i - 1; j >= 0; j -= 1) if (name.equals(names[j])) return false;
      }
      return true;
    } else {
      final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
      for (int i = 0; i < length; i++) if (!set.add(names[i])) return false;
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Function Invocation
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Node parseInvocation(final InvocationContext ctx, final boolean isExpr) {
    final List<ExpressionContext> operands = ctx.arguments;
    final int operandsSize = operands.size();
    // 1. Parse the operands
    final Expr[] opds = new Expr[operandsSize];
    for (int i = 0; i < operandsSize; i++) opds[i] = parseExpression(operands.get(i));
    // 2. Parse the operator
    final FunRef fun = parseFunRef(ctx.fun, Mode.READ);
    // 3. Error propagation
    if (fun == null) return null;
    for (int i = 0; i < operandsSize; i++) if (opds[i] == null) return null;
    // 4. Operand type checking
    if (!isValidParameters(ctx, fun.type.paramsType.types, opds)) return null;
    // 4. Output type deduction
    // N/A
    // 5. Implicit casting
    for (int i = 0; i < operandsSize; i++)
      opds[i] = castIfNeeded(fun.type.paramsType.types[i], opds[i]);
    // 6. Construct the node
    return isExpr
      ? new FunCallExpr(fun, opds)
      : new FunCallStmt(fun, opds);
  }

  private boolean isValidParameters(
    final InvocationContext ctx,
    final FunRef            operator,
    final Expr[]            operands
  ) {
    if (isValidParameters(ctx, operator.type.paramsType.types, operands)) return true;

    final StringBuilder sb = new StringBuilder();
    sb.append("invalid arguments for function \"");
    sb.append(operator.owner);
    sb.append(".");
    sb.append(operator.name);
    operator.type.paramsType.toString(sb);
    sb.append("\": (");
    for (int i = 0; i < operands.length; i++) {
      if (i > 0) sb.append(",")
      operands[i].type().toString(sb);
    }
    sb.append(")");
    err(ctx, sb.toString());
    return false;
  }


  private static boolean isValidParameters(
    final InvocationContext ctx,
    final Type[]            expected,
    final Expr[]            operands
  ) {
    // TODO: think about mutablity requirements
    // TODO: named parameters
    // TODO: varargs
    if (operands.length != expected.length) return false;
    for (int i = 0; i < operands.length; i++)
      if (!operands[i].type().isAssignableTo(expected[i])) return false;
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Symbol Table
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private static enum Mode { READ, WRITE, READWRITE };
  private VarRef parseVarRef(final PathContext ctx, final Mode mode) {
    throw new UnsupportedOperationException("TODO");
  }

  private FunRef parseFunRef(final PathContext ctx, final Mode mode) {
    throw new UnsupportedOperationException("TODO");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Helper Functions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Expr castIfNeeded(final Type targetType, final Expr node) {
    final Type nodeType = node.type();
    return nodeType.equals(targetType) ? node : new CastExpr(targetType, node);
  }

  private boolean isAssignableType(
    final ParserRuleContext operatorCtx,
    final Token             operatorToken,
    final Node              operandNode0,
    final Node              operandNode1
  ) {
    final Type operandType0 = operandNode0.type();
    final Type operandType1 = operandNode1.type();
    // Check if type1 is assignable to type0
    if (operandType1.isAssignableTo(operandType0)) return true;
    // Report the error if a type1 is not assignable to type0
    final StringBuilder sb = new StringBuilder();
    sb.append("invalid operands for operator \"");
    sb.append(operatorToken);
    sb.append("\": ");
    operandType0.toString(sb);
    sb.append(" and ");
    operandType1.toString(sb);
    err(operatorCtx, sb.toString());
    return false;
  }

  private Type commonType(
    final ParserRuleContext operatorCtx,
    final Token             operatorToken,
    final Node              operandNode0,
    final Node              operandNode1
  ) {
    final Type operandType0 = operandNode0.type();
    final Type operandType1 = operandNode1.type();

    // Get the common type
    final Type commonType = commonType(operandNode0.type(), operandNode1.type());
    // Report the error if a common type cannot be found
    if (commonType == types.Err) {
      final StringBuilder sb = new StringBuilder();
      sb.append("invalid operands for operator \"");
      sb.append(operatorToken);
      sb.append("\": ");
      operandType0.toString(sb);
      sb.append(" and ");
      operandType1.toString(sb);
      err(operatorCtx, sb.toString());
    }
    // Return the common or error type
    return commonType;
  }

  private Type commonType(
    final ParserRuleContext operatorCtx,
    final Token             operatorToken,
    final Node[]            operandNodes
  ) {
    final int operandsSize = operandNodes.length;
    assert operandsSize >= 2;
    // Get the common type
    Type commonType = operandNodes[0].type();
    for (int i = 1; i < operandsSize && commonType != types.Err; i++)
      commonType = commonType(commonType, operandNodes[i].type());
    // Report the error if a common type cannot be found
    if (commonType == types.Err) {
      final StringBuilder sb = new StringBuilder();
      sb.append("invalid operands for operator \"");
      sb.append(operatorToken);
      sb.append("\": ");
      for (int i = 0; i < operandsSize; i++) {
        if (i > 0) sb.append(i == operandsSize - 1 ? ", and " : ", ");
        operandNodes[i].type().toString(sb);
      }
      err(operatorCtx, sb.toString());
    }
    // Return the common or error type
    return commonType;
  }

  private Type commonType(final Type a, final Type b) {
    return b.isAssignableTo(a) ? a : a.isAssignableTo(b) ? b : types.Err;
  }

  private boolean isValidOperands(
    final Token               operatorToken,
    final ParserRuleContext   operandCtx0,
    final ParserRuleContext   operandCtx1,
    final Node                operandNode0,
    final Node                operandNode1,
    final EnumSet<types.Kind> operatorAccepts
  ) {
    return isValidOperand(operatorToken, operandCtx0, operandNode0, operatorAccepts) &
           isValidOperand(operatorToken, operandCtx1, operandNode1, operatorAccepts);
  }

  private boolean isValidOperands(
    final Token                             operatorToken,
    final List<? extends ParserRuleContext> operandCtxs,
    final Node[]                            operandNodes,
    final EnumSet<types.Kind>               operatorAccepts
  ) {
    boolean hasError = true;
    final int operandsSize = operandNodes.length;
    for (int i = 0; i < operandsSize; i++) {
      final ParserRuleContext operandCtx = operandCtxs.get(i);
      final Node operandNode = operandNodes[i];
      hasError &= isValidOperand(operatorToken, operandCtx, operandNode, LOGICAL_ACCEPTS);
    }
    return hasError;
  }

  private boolean isValidOperand(
    final Token               operatorToken,
    final ParserRuleContext   operandCtx,
    final Node                operandNode,
    final EnumSet<types.Kind> operatorAccepts
  ) {
    if (operandNode == null) return false;
    final Type operandType = operandNode.type();
    if (operatorAccepts.contains(operandType.kind())) return true;
    err(operandCtx, "invalid operand for operator \"" + operatorToken + "\": " + operandType);
    return false;
  }

  private static final EnumSet<types.Kind> ALGEBRA_ACCEPTS = EnumSet.of(
    types.Kind.I08, types.Kind.I16, types.Kind.I32, types.Kind.I64,
    types.Kind.U08, types.Kind.U16, types.Kind.U32, types.Kind.U64,
    types.Kind.F32, types.Kind.F64
  );

  private static final EnumSet<types.Kind> BITWISE_ACCEPTS = EnumSet.of(
    types.Kind.BIT,
    types.Kind.I08, types.Kind.I16, types.Kind.I32, types.Kind.I64,
    types.Kind.U08, types.Kind.U16, types.Kind.U32, types.Kind.U64
  );

  private static final EnumSet<types.Kind> BWSHIFT_ACCEPTS = EnumSet.of(
    types.Kind.I08, types.Kind.I16, types.Kind.I32, types.Kind.I64,
    types.Kind.U08, types.Kind.U16, types.Kind.U32, types.Kind.U64
  );

  private static final EnumSet<types.Kind> COMPARE_ACCEPTS = EnumSet.of(
    types.Kind.BIT,
    types.Kind.I08, types.Kind.I16, types.Kind.I32, types.Kind.I64,
    types.Kind.U08, types.Kind.U16, types.Kind.U32, types.Kind.U64,
    types.Kind.F32, types.Kind.F64
  );

  private static final EnumSet<types.Kind> LOGICAL_ACCEPTS = EnumSet.of(
    types.Kind.BIT
  );
}
