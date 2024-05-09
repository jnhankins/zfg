package zfg;

import static zfg.literals.parseBitLit;
import static zfg.literals.parseFltLit;
import static zfg.literals.parseIntLit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.insts.I32Inst;
import zfg.insts.Inst;
import zfg.nodes.AssignmentNode;
import zfg.nodes.BinaryExprNode;
import zfg.nodes.ConstantNode;
import zfg.nodes.NaryExprNode;
import zfg.nodes.Node;
import zfg.nodes.VariableNode;
import zfg.types.RecType;
import zfg.types.Type;
import zfg.types.TypeCache;
import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser.AlgebraAssignmentContext;
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
import zfg.antlr.ZfgParser.BitwiseAssignmentContext;
import zfg.antlr.ZfgParser.BitwiseChianExprContext;
import zfg.antlr.ZfgParser.BitwiseCompareOpdContext;
import zfg.antlr.ZfgParser.BitwiseExprContext;
import zfg.antlr.ZfgParser.BitwiseExpressionContext;
import zfg.antlr.ZfgParser.BitwiseInfixExprContext;
import zfg.antlr.ZfgParser.BitwiseLogicalOpdContext;
import zfg.antlr.ZfgParser.BwShiftAssignmentContext;
import zfg.antlr.ZfgParser.CompareChianExprContext;
import zfg.antlr.ZfgParser.CompareExprContext;
import zfg.antlr.ZfgParser.CompareExpressionContext;
import zfg.antlr.ZfgParser.CompareInfixExprContext;
import zfg.antlr.ZfgParser.CompareLogicalOpdContext;
import zfg.antlr.ZfgParser.CompareOperandContext;
import zfg.antlr.ZfgParser.DefinitionStatementContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunTypeContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.IncDecAssignmentContext;
import zfg.antlr.ZfgParser.IncDecExprContext;
import zfg.antlr.ZfgParser.InvocationExprContext;
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
import zfg.antlr.ZfgParser.PostfixSuccesorContext;
import zfg.antlr.ZfgParser.PrecedenceExprContext;
import zfg.antlr.ZfgParser.PrefixExprContext;
import zfg.antlr.ZfgParser.PrefixSuccesorContext;
import zfg.antlr.ZfgParser.PriTypeContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.RecTypeContext;
import zfg.antlr.ZfgParser.RecordTypeContext;
import zfg.antlr.ZfgParser.ScopeContext;
import zfg.antlr.ZfgParser.SetAssignmentContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.SuccessorAssignmentContext;
import zfg.antlr.ZfgParser.TupTypeContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.UnambigCompareOpdContext;
import zfg.antlr.ZfgParser.UnambigExprContext;
import zfg.antlr.ZfgParser.UnambigExpressionContext;
import zfg.antlr.ZfgParser.UnambigLogicalOpdContext;
import zfg.antlr.ZfgParser.VariableExprContext;

public final class Parser {
  public Parser() {}


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

  private Node parseAssignmentStatement(final AssignmentStatementContext ctx) {
    return parseAssignment(ctx.child, AssignmentNode.Mode.STMT);
  }

  private Node parseInvocationStatement(final InvocationStatementContext ctx) {
    throw new UnsupportedOperationException("TODO"); // TODO
    // return parseInvocation(ctx.child, InvocationNode.Mode.STMT);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Node parseExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case UnambigExprContext expr -> parseUnambigExpression(expr.expr);
      case AlgebraExprContext expr -> parseAlgebraExpression(expr.expr);
      case BitwiseExprContext expr -> parseBitwiseExpression(expr.expr);
      case CompareExprContext expr -> parseCompareExpression(expr.expr);
      case LogicalExprContext expr -> parseLogicalExpression(expr.expr);
      default -> throw new AssertionError();
    };
  }

  private Node parseUnambigExpression(final UnambigExpressionContext ctx) {
    return switch (ctx) {
      case LiteralExprContext    expr -> parseLiteralExpr(expr);
      case VariableExprContext   expr -> parseVariableExpr(expr);
      case PrecedenceExprContext expr -> parsePrecedenceExpr(expr);
      case AssignmentExprContext expr -> parseAssignmentExpr(expr);
      case InvocationExprContext expr -> parseInvocationExpr(expr);
      case IncDecExprContext     expr -> parseIncDecExprContext(expr);
      case PrefixExprContext     expr -> parsePrefixExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Node parseAlgebraExpression(final AlgebraExpressionContext ctx) {
    return switch (ctx) {
      case AlgebraInfixExprAAContext expr -> parseAlgebraInfixExpr(
        ctx, expr.lhs, expr.rhs, expr.op,
        parseAlgebraExpression(expr.lhs),
        parseAlgebraExpression(expr.rhs));
      case AlgebraInfixExprAUContext expr -> parseAlgebraInfixExpr(
        ctx, expr.lhs, expr.rhs, expr.op,
        parseAlgebraExpression(expr.lhs),
        parseUnambigExpression(expr.rhs));
      case AlgebraInfixExprUAContext expr -> parseAlgebraInfixExpr(
        ctx, expr.lhs, expr.rhs, expr.op,
        parseUnambigExpression(expr.lhs),
        parseAlgebraExpression(expr.rhs));
      case AlgebraInfixExprUUContext expr -> parseAlgebraInfixExpr(
        ctx, expr.lhs, expr.rhs, expr.op,
        parseUnambigExpression(expr.lhs),
        parseUnambigExpression(expr.rhs));
      default -> throw new AssertionError();
    };
  }

  private Node parseBitwiseExpression(final BitwiseExpressionContext ctx) {
    return switch (ctx) {
      case BitwiseChianExprContext expr -> parseBitwiseChianExpr(expr);
      case BitwiseInfixExprContext expr -> parseBitwiseInfixExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Node parseCompareExpression(final CompareExpressionContext ctx) {
    return switch (ctx) {
      case CompareChianExprContext expr -> parseCompareChianExpr(expr);
      case CompareInfixExprContext expr -> parseCompareInfixExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Node parseLogicalExpression(final LogicalExpressionContext ctx) {
    return switch (ctx) {
      case LogicalChianExprContext expr -> parseLogicalChianExpr(expr);
      default -> throw new AssertionError();
    };
  }

  private Node parseLiteralExpr(final LiteralExprContext ctx) {
    return parseLiteral(ctx.expr);
  }

  private Node parseVariableExpr(final VariableExprContext ctx) {
    return parseVariablePath(ctx.expr, Mode.READ);
  }

  private Node parsePrecedenceExpr(final PrecedenceExprContext ctx) {
    return parseExpression(ctx.expr);
  }

  private Node parseAssignmentExpr(final AssignmentExprContext ctx) {
    return parseAssignment(ctx.expr, AssignmentNode.Mode.EXPR);
  }

  private Node parseInvocationExpr(final InvocationExprContext ctx) {
    throw new UnsupportedOperationException("TODO"); // TODO
    // return parseAssignment(ctx.expr, InvocationNode.Mode.EXPR);
  }

  private Node parseIncDecExpr(final IncDecExprContext ctx) {
    return successorAssignment(ctx.expr, AssignmentNode.Mode.EXPR);
  }

  private Node parsePrefixExpr(final PrefixExprContext ctx) {
    // 1. Parse the operands
    // 2. Parse the operator
    // 3. Operand type checking and error propagation
    // 4. Output type deduction
    // 5. Implicit casting
    // 6. Construct the node
    throw new UnsupportedOperationException("TODO"); // TODO
  }

  private Node parseAlgebraInfixExpr(
    final ParserRuleContext ctx,
    final ParserRuleContext ctxLhs,
    final ParserRuleContext ctxRhs,
    final Token ctxOp,
    final Node lhs,
    final Node rhs
  ) {
    // 1. Parse the operands
    // N/A (already parsed in the caller method)
    // 2. Parse the operator
    final BinaryExprNode.Op op = switch (ctxOp.getType()) {
      case ZfgLexer.ADD -> BinaryExprNode.Op.ADD;
      case ZfgLexer.SUB -> BinaryExprNode.Op.SUB;
      case ZfgLexer.MUL -> BinaryExprNode.Op.MUL;
      case ZfgLexer.DIV -> BinaryExprNode.Op.DIV;
      case ZfgLexer.REM -> BinaryExprNode.Op.REM;
      case ZfgLexer.MOD -> BinaryExprNode.Op.MOD;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctxOp, ctxLhs, ctxRhs, lhs, rhs, ALGEBRA_ACCEPTS)) return nodes.error;
    // 4. Output type deduction
    final Type outType = commonType(ctx, ctxOp, lhs, rhs);
    if (outType == types.Err) return nodes.error;
    // 5. Implicit casting
    final Node lhsOpd = castIfNeeded(outType, lhs);
    final Node rhsOpd = castIfNeeded(outType, rhs);
    // 6. Construct the node
    return new BinaryExprNode(outType, op, lhsOpd, rhsOpd);
  }

  private Node parseBitwiseChianExpr(final BitwiseChianExprContext ctx) {
    final List<UnambigExpressionContext> children = ctx.opd;
    final int childrenSize = children.size();
    // 1. Parse the operands
    final List<Node> operands = new ArrayList<>(childrenSize);
    for (int i = 0; i < childrenSize; i++) operands.add(parseUnambigExpression(children.get(i)));
    // 2. Parse the operator
    final NaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.AND -> NaryExprNode.Op.AND;
      case ZfgLexer.IOR -> NaryExprNode.Op.IOR;
      case ZfgLexer.XOR -> NaryExprNode.Op.XOR;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, children, operands, BITWISE_ACCEPTS)) return nodes.error;
    // 4. Output type deduction
    final Type outType = commonType(ctx, ctx.op, operands);
    if (outType == types.Err) return nodes.error;
    // 5. Implicit casting
    for (int i = 0; i < childrenSize; i++) operands.set(i, castIfNeeded(outType, operands.get(i)));
    // 6. Construct the node
    return new NaryExprNode(outType, op, operands);
  }

  private Node parseBitwiseInfixExpr(final BitwiseInfixExprContext ctx) {
    // 1. Parse the operands
    final Node lhs = parseUnambigExpression(ctx.lhs);
    final Node rhs = parseUnambigExpression(ctx.rhs);
    // 2. Parse the operator
    final BinaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.SHL -> BinaryExprNode.Op.SHL;
      case ZfgLexer.SHR -> BinaryExprNode.Op.SHR;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, ctx.lhs, ctx.rhs, lhs, rhs, BWSHIFT_ACCEPTS)) return nodes.error;
    // 4. Output type deduction
    final Type outType = lhs.type();
    // 5. Implicit casting
    final Node lhsOpd = lhs;
    final Node rhsOpd = castIfNeeded(types.I32, rhs);
    // 6. Construct the node
    return new BinaryExprNode(outType, op, lhsOpd, rhsOpd);
  }

  private Node parseCompareChianExpr(final CompareChianExprContext ctx) {
    // 1. Parse the operands
    // 2. Parse the operator
    // 3. Operand type checking and error propagation
    // 4. Output type deduction
    // 5. Implicit casting
    // 6. Construct the node
    throw new UnsupportedOperationException("TODO"); // TODO
  }

  private Node parseCompareInfixExpr(final CompareInfixExprContext ctx) {
    // 1. Parse the operands
    final Node lhs = parseCompareOperand(ctx.lhs);
    final Node rhs = parseCompareOperand(ctx.rhs);
    // 2. Parse the operator
    final BinaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.TWC -> BinaryExprNode.Op.TWC;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, ctx.lhs, ctx.rhs, lhs, rhs, COMPARE_ACCEPTS)) return nodes.error;
    // 4. Output type deduction
    final Type outType = types.I08;
    // 5. Implicit casting
    final Type commonType = commonType(ctx, ctx.op, lhs, rhs);
    if (commonType == types.Err) return nodes.error;
    final Node lhsOpd = castIfNeeded(commonType, lhs);
    final Node rhsOpd = castIfNeeded(commonType, rhs);
    // 6. Construct the node
    return new BinaryExprNode(outType, op, lhsOpd, rhsOpd);
  }

  private Node parseCompareOperand(final CompareOperandContext ctx) {
    return switch (ctx) {
      case BitwiseCompareOpdContext opd -> parseBitwiseExpression(opd.expr);
      case AlgebraCompareOpdContext opd -> parseAlgebraExpression(opd.expr);
      case UnambigCompareOpdContext opd -> parseUnambigExpression(opd.expr);
      default -> throw new AssertionError();
    };
  }

  private Node parseLogicalChianExpr(final LogicalChianExprContext ctx) {
    final List<LogicalOperandContext> children = ctx.opd;
    final int childrenSize = children.size();
    // 1. Parse the operands
    final List<Node> operands = new ArrayList<>(childrenSize);
    for (int i = 0; i < childrenSize; i++) operands.add(parseLogicalOperand(children.get(i)));
    // 2. Parse the operator
    final NaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.LCJ -> NaryExprNode.Op.LCJ;
      case ZfgLexer.LDJ -> NaryExprNode.Op.LDJ;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, children, operands, LOGICAL_ACCEPTS)) return nodes.error;
    // 4. Output type deduction
    final Type outType = types.Bit;
    // 5. Implicit casting
    // N/A
    // 6. Construct the node
    return new NaryExprNode(outType, op, operands);
  }

  private Node parseLogicalOperand(final LogicalOperandContext ctx) {
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

  private Node parseAssignment(final AssignmentContext ctx, final AssignmentNode.Mode mode) {
    return switch (ctx) {
      case SetAssignmentContext     assign -> parseSetAssignment(assign, mode);
      case AlgebraAssignmentContext assign -> parseAlgebraAssignment(assign, mode);
      case BitwiseAssignmentContext assign -> parseBitwiseAssignment(assign, mode);
      case BwShiftAssignmentContext assign -> parseBwShiftAssignment(assign, mode);
      case IncDecAssignmentContext  assign -> parseIncDecAssignment(assign, mode);
      default -> throw new AssertionError();
    };
  }

  private Node parseSetAssignment(
    final SetAssignmentContext ctx,
    final AssignmentNode.Mode mode
  ) {
    // 1. Parse the operands
    final VariableNode lhs = parseVariablePath(ctx.lhs, Mode.READ);
    final Node rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    switch (ctx.op.getType()) {
      case ZfgLexer.SETA: break;
      default: throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isAssignableType(ctx, ctx.op, lhs, rhs)) return nodes.error;
    // 4. Output type deduction
    final Type outType = lhs.type();
    // 5. Implicit casting
    final Node rhsOpd = castIfNeeded(outType, rhs);
    // 6. Construct the node
    return new AssignmentNode(mode, lhs, rhsOpd);
  }

  private Node parseAlgebraAssignment(
    final AlgebraAssignmentContext ctx,
    final AssignmentNode.Mode mode
  ) {
    // 1. Parse the operands
    final VariableNode lhs = parseVariablePath(ctx.lhs, Mode.READWRITE);
    final Node rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    final BinaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.ADDA -> BinaryExprNode.Op.ADD;
      case ZfgLexer.SUBA -> BinaryExprNode.Op.SUB;
      case ZfgLexer.MULA -> BinaryExprNode.Op.MUL;
      case ZfgLexer.DIVA -> BinaryExprNode.Op.DIV;
      case ZfgLexer.REMA -> BinaryExprNode.Op.REM;
      case ZfgLexer.MODA -> BinaryExprNode.Op.MOD;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, ctx.lhs, ctx.rhs, lhs, rhs, ALGEBRA_ACCEPTS)) return nodes.error;
    if (!isAssignableType(ctx, ctx.op, lhs, rhs)) return nodes.error;
    // 4. Output type deduction
    final Type outType = lhs.type();
    // 5. Implicit casting
    final Node rhsOpd = castIfNeeded(outType, rhs);
    // 6. Construct the expression and assignment nodes
    final Node expr = new BinaryExprNode(outType, op, lhs, rhsOpd);
    return new AssignmentNode(mode, lhs, expr);
  }

  private Node parseBitwiseAssignment(
    final BitwiseAssignmentContext ctx,
    final AssignmentNode.Mode mode
  ) {
    // 1. Parse the operands
    final VariableNode lhs = parseVariablePath(ctx.lhs, Mode.READWRITE);
    final Node rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    final NaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.ANDA -> NaryExprNode.Op.AND;
      case ZfgLexer.IORA -> NaryExprNode.Op.IOR;
      case ZfgLexer.XORA -> NaryExprNode.Op.XOR;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, ctx.lhs, ctx.rhs, lhs, rhs, BITWISE_ACCEPTS)) return nodes.error;
    if (!isAssignableType(ctx, ctx.op, lhs, rhs)) return nodes.error;
    // 4. Output type deduction
    final Type outType = lhs.type();
    // 5. Implicit casting
    final Node rhsOpd = castIfNeeded(outType, rhs);
    // 6. Construct the expression and assignment nodes
    final Node expr = new NaryExprNode(outType, op, List.of(lhs, rhsOpd));
    return new AssignmentNode(mode, lhs, expr);
  }

  private Node parseBwShiftAssignment(
    final BwShiftAssignmentContext ctx,
    final AssignmentNode.Mode mode
  ) {
    // 1. Parse the operands
    final VariableNode lhs = parseVariablePath(ctx.lhs, Mode.READWRITE);
    final Node rhs = parseExpression(ctx.rhs);
    // 2. Parse the operator
    final BinaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.SHLA -> BinaryExprNode.Op.SHL;
      case ZfgLexer.SHRA -> BinaryExprNode.Op.SHR;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, ctx.lhs, ctx.rhs, lhs, rhs, BITWISE_ACCEPTS)) return nodes.error;
    // 4. Output type deduction
    final Type outType = lhs.type();
    // 5. Implicit casting
    final Node rhsOpd = castIfNeeded(types.I32, rhs);
    // 6. Construct the expression node
    final Node expr = new BinaryExprNode(outType, op, lhs, rhsOpd);
    return new AssignmentNode(mode, lhs, expr);
  }

  private Node successorAssignment(
    final SuccessorAssignmentContext ctx,
    final AssignmentNode.Mode mode
  ) {
    return switch (ctx) {
      case PostfixSuccesorContext suc -> parsePostfixSuccessor(suc, mode);
      case PrefixSuccesorContext suc -> parsePrefixSuccessor(suc, mode);
      default -> throw new AssertionError();
    };
  }

  private Node parsePostfixSuccessor(
    final PostfixSuccesorContext ctx,
    final AssignmentNode.Mode mode
  ) {
    // 1. Parse the operand
    final VariableNode lhs = parseVariablePath(ctx.lhs, Mode.READWRITE);
    final Node rhs = ConstantNode()
    // 2. Parse the operator
    final BinaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.INC -> BinaryExprNode.Op.ADD;
      case ZfgLexer.DEC -> BinaryExprNode.Op.SUB;
      default -> throw new AssertionError();
    };
    // 3. Operand type checking and error propagation
    if (!isValidOperands(ctx.op, ctx.lhs, lhs, lhs, nodes.one)) return nodes.error;
    // 4. Output type deduction
    final Type outType = lhs.type();
    // 5. Implicit casting
    // N/A
    // 6. Construct the expression and assignment nodes
    final Node expr = new BinaryExprNode(outType, op, lhs, nodes.one);
    return new AssignmentNode(mode, lhs, expr);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Literals
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Node parseLiteral(final LiteralContext ctx) {
    return switch (ctx) {
      case NumericLitContext lit -> parseNumericLit(lit.lit);
      default -> throw new AssertionError();
    };
  }

  private Node parseNumericLit(final NumericLiteralContext ctx) {
    final String text = ctx.token.getText();
    final int    type = ctx.token.getType();
    switch (type) {
      case ZfgLexer.BitLit: {
        final Inst parsed = parseBitLit(text);
        if (parsed != null) return new ConstantNode(parsed);
        err(ctx, "Invalid bit literal: \"" + text + "\"");
        return nodes.error;
      }
      case ZfgLexer.IntLit: {
        final boolean hasMinusPrefix =
            ctx.parent instanceof PrefixExprContext parent &&
            parent.op.getType() == ZfgLexer.SUB &&
            parent.op.getStopIndex() + 1 == ctx.getStart().getStartIndex();
        final Inst parsed = parseIntLit(text, hasMinusPrefix);
        if (parsed != null) return new ConstantNode(parsed);
        err(ctx, "Invalid int literal: \"" + text + "\"");
        return nodes.error;
      }
      case ZfgLexer.FltLit: {
        final Inst parsed = parseFltLit(text);
        if (parsed != null) return new ConstantNode(parsed);
        err(ctx, "Invalid flt literal: \"" + text + "\"");
        return nodes.error;
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
    // Construct the tuple type
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
    // Construct the tuple type
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
    // Construct the tuple type
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
    // Construct the array type
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
  // Symbol Table
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private static enum Mode { READ, WRITE, READWRITE };
  private VariableNode parseVariablePath(final PathContext ctx, final Mode mode) {
    throw new UnsupportedOperationException("TODO");
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Helper Functions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Node castIfNeeded(final Type targetType, final Node node) {
    final Type nodeType = node.type();
    return nodeType.equals(targetType) ? node : new CastNode(targetType, node);
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
    final List<Node>        operandNodes
  ) {
    final int operandsSize = operandNodes.size();
    assert operandsSize >= 2;
    // Get the common type
    Type commonType = operandNodes.get(0).type();
    for (int i = 1; i < operandsSize && commonType != types.Err; i++)
      commonType = commonType(commonType, operandNodes.get(i).type());
    // Report the error if a common type cannot be found
    if (commonType == types.Err) {
      final StringBuilder sb = new StringBuilder();
      sb.append("invalid operands for operator \"");
      sb.append(operatorToken);
      sb.append("\": ");
      for (int i = 0; i < operandsSize; i++) {
        if (i > 0) sb.append(i == operandsSize - 1 ? ", and " : ", ");
        operandNodes.get(i).type().toString(sb);
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
    final List<Node>                        operandNodes,
    final EnumSet<types.Kind>               operatorAccepts
  ) {
    boolean hasError = true;
    final int operandsSize = operandNodes.size();
    for (int i = 0; i < operandsSize; i++) {
      final ParserRuleContext operandCtx = operandCtxs.get(i);
      final Node operandNode = operandNodes.get(i);
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
    if (operandNode == nodes.error) return false;
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
