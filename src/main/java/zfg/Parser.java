package zfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.nodes.BinaryExprNode;
import zfg.nodes.CompareExprNode;
import zfg.nodes.LogicalChainOp;
import zfg.nodes.NaryExprNode;
import zfg.nodes.Node;
import zfg.types.Type;
import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser.AlgebraCompareOpdContext;
import zfg.antlr.ZfgParser.AlgebraExprContext;
import zfg.antlr.ZfgParser.AlgebraExpressionContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprAAContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprAUContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprUAContext;
import zfg.antlr.ZfgParser.AlgebraInfixExprUUContext;
import zfg.antlr.ZfgParser.AlgebraLogicalOpdContext;
import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.AssignmentStatementContext;
import zfg.antlr.ZfgParser.BitwiseChianExprContext;
import zfg.antlr.ZfgParser.BitwiseCompareOpdContext;
import zfg.antlr.ZfgParser.BitwiseExprContext;
import zfg.antlr.ZfgParser.BitwiseExpressionContext;
import zfg.antlr.ZfgParser.BitwiseInfixExprContext;
import zfg.antlr.ZfgParser.BitwiseLogicalOpdContext;
import zfg.antlr.ZfgParser.CompareChianExprContext;
import zfg.antlr.ZfgParser.CompareExprContext;
import zfg.antlr.ZfgParser.CompareExpressionContext;
import zfg.antlr.ZfgParser.CompareInfixExprContext;
import zfg.antlr.ZfgParser.CompareLogicalOpdContext;
import zfg.antlr.ZfgParser.CompareOperandContext;
import zfg.antlr.ZfgParser.DefinitionStatementContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.FunctionCallStatementContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.LogicalChianExprContext;
import zfg.antlr.ZfgParser.LogicalExprContext;
import zfg.antlr.ZfgParser.LogicalExpressionContext;
import zfg.antlr.ZfgParser.LogicalOperandContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.PostfixExprContext;
import zfg.antlr.ZfgParser.PrecedenceExprContext;
import zfg.antlr.ZfgParser.PrefixExprContext;
import zfg.antlr.ZfgParser.ScopeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.UnambigCompareOpdContext;
import zfg.antlr.ZfgParser.UnambigExprContext;
import zfg.antlr.ZfgParser.UnambigExpressionContext;
import zfg.antlr.ZfgParser.UnambigLogicalOpdContext;
import zfg.antlr.ZfgParser.VariablePathExprContext;

public final class Parser {
  /** Parser error */
  public static final class Error {
    final ParserRuleContext ctx; // The most speicific context for where the error occurred
    final String msg;            // The error message

    public Error(final ParserRuleContext ctx, final String msg) {
      this.ctx = ctx;
      this.msg = msg;
    }

    @Override public String toString() {
      final int line = ctx.start.getLine();
      final int column = ctx.start.getCharPositionInLine();
      return String.format("%d:%d: %s", line, column, msg);
    }
  }

  private final List<Error> errors = new ArrayList<>();

  public Parser() {}

  /** Get the list of errors */
  public List<Error> errors() { return errors; }

  /** Report an error */
  private void err(final Error err) { errors.add(err); }
  private void err(final ParserRuleContext ctx, final String msg) { err(new Error(ctx, msg)); }




  public void parseModule(final ModuleContext ctx) {
    final List<Node> statements = ctx.body != null
      ? parseScope(ctx.body)
      : Collections.emptyList();
    // TODO...
  }

  public List<Node> parseScope(final ScopeContext ctx) {
    final List<Node> statements; {
      final List<StatementContext> children = ctx.statement();
      final int childrenSize = children.size();
      statements = new ArrayList<>();
      for (int i = 0; i < childrenSize; i++) {
        final Node statementNode = parseStatement(children.get(i));
        statements.add(statementNode);
      }
    }
    return statements;
  }

  public Node parseStatement(final StatementContext ctx) {
    return switch (ctx) {
      case DefinitionStatementContext   stmt -> parseDefinitionStatement(stmt);
      case AssignmentStatementContext   stmt -> parseAssignmentStatement(stmt);
      case FunctionCallStatementContext stmt -> parseFunctionCallStatement(stmt);
      default -> throw new AssertionError();
    };
  }

  public Node parseDefinitionStatement(final DefinitionStatementContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  public Node parseAssignmentStatement(final AssignmentStatementContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  public Node parseFunctionCallStatement(final FunctionCallStatementContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Node parseExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case UnambigExprContext expr -> parseUnambigExpression(expr.expr);
      case AlgebraExprContext expr -> parseAlgebraExpression(expr.expr);
      case BitwiseExprContext expr -> parseBitwiseExpression(expr.expr);
      case CompareExprContext expr -> parseCompareExpression(expr.expr);
      case LogicalExprContext expr -> parseLogicalExpression(expr.expr);
      default -> throw new AssertionError();
    };
  }
  public Node parseUnambigExpression(final UnambigExpressionContext ctx) {
    return switch (ctx) {
      case LiteralExprContext      expr -> parseLiteralExpr(expr);
      case VariablePathExprContext expr -> parseVariablePathExpr(expr);
      case FunctionCallExprContext expr -> parseFunctionCallExpr(expr);
      case PostfixExprContext      expr -> parsePostfixExpr(expr);
      case PrefixExprContext       expr -> parsePrefixExpr(expr);
      case PrecedenceExprContext   expr -> parsePrecedenceExpr(expr);
      case AssignmentExprContext   expr -> parseAssignmentExpr(expr);
      default -> throw new AssertionError();
    };
  }
  public Node parseAlgebraExpression(final AlgebraExpressionContext ctx) {
    return switch (ctx) {
      case AlgebraInfixExprAAContext expr -> parseAlgebraInfixExpr(expr);
      case AlgebraInfixExprAUContext expr -> parseAlgebraInfixExpr(expr);
      case AlgebraInfixExprUAContext expr -> parseAlgebraInfixExpr(expr);
      case AlgebraInfixExprUUContext expr -> parseAlgebraInfixExpr(expr);
      default: throw new AssertionError();
    };
  }
  public Node parseBitwiseExpression(final BitwiseExpressionContext ctx) {
    return switch (ctx) {
      case BitwiseChianExprContext expr -> parseBitwiseChianExpr(expr);
      case BitwiseInfixExprContext expr -> parseBitwiseInfixExpr(expr);
      default -> throw new AssertionError();
    };
  }
  public Node parseCompareExpression(final CompareExpressionContext ctx) {
    return switch (ctx) {
      case CompareChianExprContext expr -> parseCompareChianExpr(expr);
      case CompareInfixExprContext expr -> parseCompareInfixExpr(expr);
      default -> throw new AssertionError();
    };
  }
  public Node parseLogicalExpression(final LogicalExpressionContext ctx) {
    return switch (ctx) {
      case LogicalChianExprContext expr -> parseLogicalChianExpr(expr);
      default -> throw new AssertionError();
    };
  }

  public Node parsePrecedenceExpr(final PrecedenceExprContext ctx) {
    return parseExpression(ctx.expr);
  }
  public Node parseLiteralExpr(final LiteralExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parseVariablePathExpr(final VariablePathExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parseFunctionCallExpr(final FunctionCallExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parsePostfixExpr(final PostfixExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parsePrefixExpr(final PrefixExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parseAssignmentExpr(final AssignmentExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parseAlgebraInfixExpr(final AlgebraInfixExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parseBitwiseChianExpr(final BitwiseChianExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parseBitwiseInfixExpr(final BitwiseInfixExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
  public Node parseCompareChianExpr(final CompareChianExprContext ctx) {
    final List<CompareOperandContext> children = ctx.compareOperand();
    final int childrenSize = children.size();

    // Parse the operands
    final List<Node> operands = new ArrayList<>(childrenSize);
    for (int i = 0; i < childrenSize; i++) {
      operands.add(parseCompareOperand(children.get(i)));
    }

    // Parse the operator
    final LogicalChainOp op = switch(ctx.op.getType()) {
      case ZfgLexer.LCJ -> LogicalChainOp.LCJ;
      case ZfgLexer.LDJ -> LogicalChainOp.LDJ;
      default -> throw new AssertionError();
    };

    // Operand checking and error propagation
    boolean hasError = false;
    for (int i = 0; i < childrenSize; i++)
      hasError |= isValidOperand(ctx.op, children.get(i), operands.get(i), COMPARE_ACCEPTS);
    if (hasError) return nodes.error;

    // Implicit casting
    // N/A

    // Construct the node
    return new CompareExprNode(types.Bit, op, operands);
  }
  public Node parseCompareInfixExpr(final CompareInfixExprContext ctx) {
    // Parse the operands
    final Node lhs = parseCompareOperand(ctx.lhs);
    final Node rhs = parseCompareOperand(ctx.rhs);

    // Parse the operator
    final BinaryExprNode.Op op = switch (ctx.op.getType()) {
      case ZfgLexer.TWC -> BinaryExprNode.Op.TWC;
      default -> throw new AssertionError();
    };

    // Operand type checking and error propagation
    if (
      !isValidOperand(ctx.op, ctx.lhs, lhs, COMPARE_ACCEPTS) |
      !isValidOperand(ctx.op, ctx.rhs, rhs, COMPARE_ACCEPTS)
    ) return nodes.error;

    // Implicit casting
    final Type commonType = commonType(ctx, ctx.op, lhs.type(), rhs.type());
    if (commonType == types.Err) return nodes.error;
    final Node lhsOpd = castIfNeeded(commonType, lhs);
    final Node rhsOpd = castIfNeeded(commonType, rhs);

    // Construct the node
    return new BinaryExprNode(types.I08, op, lhsOpd, rhsOpd);
  }

  public Node parseCompareOperand(final CompareOperandContext ctx) {
    return switch (ctx) {
      case BitwiseCompareOpdContext opd -> parseBitwiseExpression(opd.expr);
      case AlgebraCompareOpdContext opd -> parseAlgebraExpression(opd.expr);
      case UnambigCompareOpdContext opd -> parseUnambigExpression(opd.expr);
      default -> throw new AssertionError();
    };
  }

  public Node parseLogicalChianExpr(final LogicalChianExprContext ctx) {
    final List<LogicalOperandContext> children = ctx.logicalOperand();
    final int childrenSize = children.size();

    // Parse the operands
    final List<Node> operands = new ArrayList<>(childrenSize);
    for (int i = 0; i < childrenSize; i++) {
      operands.add(parseLogicalOperand(children.get(i)));
    }

    // Parse the operator
    final NaryExprNode.Op op = switch(ctx.op.getType()) {
      case ZfgLexer.LCJ -> NaryExprNode.Op.LCJ;
      case ZfgLexer.LDJ -> NaryExprNode.Op.LDJ;
      default -> throw new AssertionError();
    };

    // Operand type checking and error propagation
    boolean hasError = false;
    for (int i = 0; i < childrenSize; i++)
      hasError |= isValidOperand(ctx.op, children.get(i), operands.get(i), LOGICAL_ACCEPTS);
    if (hasError) return nodes.error;

    // Implicit casting
    // N/A

    // Construct the node
    return new NaryExprNode(types.Bit, op, operands);
  }

  public Node parseLogicalOperand(final LogicalOperandContext ctx) {
    return switch (ctx) {
      case CompareLogicalOpdContext opd -> parseCompareExpression(opd.expr);
      case BitwiseLogicalOpdContext opd -> parseBitwiseExpression(opd.expr);
      case AlgebraLogicalOpdContext opd -> parseAlgebraExpression(opd.expr);
      case UnambigLogicalOpdContext opd -> parseUnambigExpression(opd.expr);
      default -> throw new AssertionError();
    };
  }

  private Type commonType(
    final ParserRuleContext operatorCtx,
    final Token             operatorToken,
    final Type a,
    final Type b
  ) {
    if (a.equals(b)) return a;
    if (a.isAssignableTo(b)) return b;
    if (b.isAssignableTo(a)) return a;
    err(operatorCtx, "invalid operands for operator\"" + operatorToken + "\":" + a + " and " + b);
    return types.Err;
  }

  private Node castIfNeeded(final Type targetType, final Node node) {
    final Type nodeType = node.type();
    return nodeType.equals(targetType) ? node : new CastNode(targetType, node);
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
    err(operandCtx, "invalid operand for operator\"" + operatorToken + "\": " + operandType);
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
