package zfg.ast;

import static zfg.antlr.ZfgLexer.ADD;
import static zfg.antlr.ZfgLexer.AND;
import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.CMP;
import static zfg.antlr.ZfgLexer.DEC;
import static zfg.antlr.ZfgLexer.DIV;
import static zfg.antlr.ZfgLexer.EQL;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.GEQ;
import static zfg.antlr.ZfgLexer.GTN;
import static zfg.antlr.ZfgLexer.INC;
import static zfg.antlr.ZfgLexer.IOR;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.LCJ;
import static zfg.antlr.ZfgLexer.LDJ;
import static zfg.antlr.ZfgLexer.LEQ;
import static zfg.antlr.ZfgLexer.LNT;
import static zfg.antlr.ZfgLexer.LTN;
import static zfg.antlr.ZfgLexer.MOD;
import static zfg.antlr.ZfgLexer.MUL;
import static zfg.antlr.ZfgLexer.NEQ;
import static zfg.antlr.ZfgLexer.NOT;
import static zfg.antlr.ZfgLexer.REM;
import static zfg.antlr.ZfgLexer.SHL;
import static zfg.antlr.ZfgLexer.SHR;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgLexer.XOR;

import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.GroupedExprContext;
import zfg.antlr.ZfgParser.InfixOpExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.PostfixOpExprContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.VariableExprContext;
import zfg.ast.Node.Const;
import zfg.lang.primitive.Val;

public final class ParseExpr {
  private ParseExpr() {}

  public static final Node visitExpression(final Parser parser, final ExpressionContext ctx) {
    return switch (ctx) {
      case FunctionCallExprContext expr -> visitFunctionCallExpr(parser, expr);
      case VariableExprContext     expr -> visitVariableExpr(parser, expr);
      case LiteralExprContext      expr -> visitLiteralExpr(parser, expr);
      case GroupedExprContext      expr -> visitGroupedExpr(parser, expr);
      case PostfixOpExprContext    expr -> visitPostfixOpExpr(parser, expr);
      case PrefixOpExprContext     expr -> visitPrefixOpExpr(parser, expr);
      case InfixOpExprContext      expr -> visitInfixOpExpr(parser, expr);
      case AssignmentExprContext   expr -> visitAssignmentExpr(parser, expr);
      default -> throw new AssertionError();
    };
  }

  public static final Node visitLiteralExpr(final Parser parser, final LiteralExprContext ctx) {
    final String str = ctx.lit.getText();
    return switch (ctx.lit.getType()) {
      case BitLit -> switch (zfg.lang.primitive.Parser.parseBit(str)) {
        case null -> parser.error(ctx, "Invalid bit literal");
        case Val val -> new Const(val, Type.bit);
      };
      case IntLit -> {
        final boolean hasNegativeSignPrefix = switch (ctx.parent) {
          case PrefixOpExprContext parent ->
            parent.op.getType() == SUB &&
            parent.op.getStopIndex() + 1 == ctx.getStart().getStartIndex();
          default -> false;
        };
        yield switch (zfg.lang.primitive.Parser.parseInt(str, hasNegativeSignPrefix)) {
          case null -> parser.error(ctx, "Invalid int literal");
          case Val val -> new Const(val, Type.of(val));
        };
      }
      case FltLit -> switch (zfg.lang.primitive.Parser.parseFlt(str)) {
        case null -> parser.error(ctx, "Invalid flt literal");
        case Val val -> new Const(val, Type.of(val));
      };
      default -> throw new AssertionError();
    };
  }

  public static final Node visitVariableExpr(final Parser parser, final VariableExprContext ctx) {
    return switch
    throw new UnsupportedOperationException("Unimplemented method 'visitVariableExpr'");
  }

  public static final Node visitFunctionCallExpr(final Parser parser, final FunctionCallExprContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCallExpr'");
  }

  public static final Node visitGroupedExpr(final Parser parser, final GroupedExprContext ctx) {
    return visitExpression(parser, ctx.expression());
  }

  public static final Node visitPrefixOpExpr(final Parser parser, final PrefixOpExprContext ctx) {
    return switch (ctx.op.getType()) {
      case ADD -> visitPrefixPosOpExpr(parser, ctx); // +a: identity
      case SUB -> visitPrefixNegOpExpr(parser, ctx); // -a: negation
      case NOT -> visitPrefixNotOpExpr(parser, ctx); // ~a: bitwise not
      case LNT -> visitPrefixLntOpExpr(parser, ctx); // !a: logical not
      case INC -> visitPrefixIncOpExpr(parser, ctx); // ++a: pre-increment
      case DEC -> visitPrefixDecOpExpr(parser, ctx); // --a: pre-decrement
      default -> throw new AssertionError();
    };
  }

  public static final Node visitPostfixOpExpr(final Parser parser, final PostfixOpExprContext ctx) {
    return switch (ctx.op.getType()) {
      case INC -> visitPostfixIncOpExpr(parser, ctx); // a++: post-increment
      case DEC -> visitPostfixDecOpExpr(parser, ctx); // a--: post-decrement
      default -> throw new AssertionError();
    };
  }

  public static final Node visitInfixOpExpr(final Parser parser, final InfixOpExprContext ctx) {
    return switch (ctx.op.getType()) {
      // Arithemetic
      case ADD -> visitInfixAddOpExpr(parser, ctx); // a + b: addition
      case SUB -> visitInfixSubOpExpr(parser, ctx); // a - b: subtraction
      case MUL -> visitInfixMulOpExpr(parser, ctx); // a * b: multiplication
      case DIV -> visitInfixDivOpExpr(parser, ctx); // a / b: division
      case REM -> visitInfixRemOpExpr(parser, ctx); // a % b: remainder
      case MOD -> visitInfixModOpExpr(parser, ctx); // a %% b: modulus
      // Bitwise Logical
      case AND -> visitInfixAndOpExpr(parser, ctx); // a & b: bitwise and
      case IOR -> visitInfixIorOpExpr(parser, ctx); // a | b: bitwise inclusive or
      case XOR -> visitInfixXorOpExpr(parser, ctx); // a ^ b: bitwise exclusive or
      // Bitwise Shift
      case SHL -> visitInfixShlOpExpr(parser, ctx); // a << b: bitwise shift left
      case SHR -> visitInfixShrOpExpr(parser, ctx); // a >> b: bitwise shift right
      // Three-way Comparison
      case CMP -> visitInfixCmpOpExpr(parser, ctx); // a <=> b: three-way comparison
      // Relational
      case LTN -> visitInfixLtnOpExpr(parser, ctx); // a < b: less than
      case GTN -> visitInfixGtnOpExpr(parser, ctx); // a > b: greater than
      case LEQ -> visitInfixLeqOpExpr(parser, ctx); // a <= b: less than or equal
      case GEQ -> visitInfixGeqOpExpr(parser, ctx); // a >= b: greater than or equal
      case EQL -> visitInfixEqlOpExpr(parser, ctx); // a == b: equal
      case NEQ -> visitInfixNeqOpExpr(parser, ctx); // a != b: not equal
      // Logical Short-circuit
      case LCJ -> visitInfixLcjOpExpr(parser, ctx); // a && b: short-cuirting logical conjunction
      case LDJ -> visitInfixLdjOpExpr(parser, ctx); // a || b: short-cuirting logical disjunction
      default -> throw new AssertionError();
    };
  }

  public static final Node visitAssignmentExpr(final Parser parser, final AssignmentExprContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitAssignmentExpr'");
  }
}
