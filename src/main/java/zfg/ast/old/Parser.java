package zfg.ast.old;

import static zfg.antlr.ZfgLexer.ADD;
import static zfg.antlr.ZfgLexer.AND;
import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.CMP;
import static zfg.antlr.ZfgLexer.DEC;
import static zfg.antlr.ZfgLexer.DIV;
import static zfg.antlr.ZfgLexer.EQL;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.GTE;
import static zfg.antlr.ZfgLexer.GTN;
import static zfg.antlr.ZfgLexer.INC;
import static zfg.antlr.ZfgLexer.IOR;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.LTE;
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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.antlr.ZfgParser.AssignExprContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.GroupExprContext;
import zfg.antlr.ZfgParser.InfixExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.PathContext;
import zfg.antlr.ZfgParser.PathExprContext;
import zfg.antlr.ZfgParser.PostfixExprContext;
import zfg.antlr.ZfgParser.PrefixExprContext;
import zfg.ast.Literal;
import zfg.ast.Type;
import zfg.ast.Type.Ixx;
import zfg.ast.Type.Num;
import zfg.ast.Type.Uxx;
import zfg.ast.old.Expr.Const;
import zfg.core.operation.Add;
import zfg.core.operation.Mul;
import zfg.core.operation.Sub;
import zfg.core.primative.Bit;
import zfg.core.primative.F32;
import zfg.core.primative.F64;
import zfg.core.primative.I08;
import zfg.core.primative.I16;
import zfg.core.primative.I32;
import zfg.core.primative.I64;
import zfg.core.primative.U08;
import zfg.core.primative.U16;
import zfg.core.primative.U32;
import zfg.core.primative.U64;
import zfg.core.primative.Val;

public class Parser {
  public static class ParserException extends RuntimeException {
    public ParserException(final ParserRuleContext ctx, final String message) { this(ctx, message, null); }
    public ParserException(final ParserRuleContext ctx, final Throwable cause) { this(ctx, null, cause); }
    public ParserException(final ParserRuleContext ctx, final String message, final Throwable cause) { super(message, cause); }
  }

  public Expr visitExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case PathExprContext    sub -> visitPathExpr(sub);
      case LiteralExprContext sub -> visitLiteralExpr(sub);
      case GroupExprContext   sub -> visitGroupExpr(sub);
      case PostfixExprContext sub -> visitPostfixExpr(sub);
      case PrefixExprContext  sub -> visitPrefixExpr(sub);
      case InfixExprContext   sub -> visitInfixExpr(sub);
      case AssignExprContext  sub -> visitAssignExpr(sub);
      default -> throw new AssertionError();
    };
  }

  public Expr visitPathExpr(final PathExprContext ctx) {
    final PathContext pathCtx = ctx.path();
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitLiteralExpr(final LiteralExprContext ctx) {
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
    if (!(ctx.parent instanceof PrefixExprContext)) return false;
    final Token op = ((PrefixExprContext) ctx.parent).op;
    return op.getType() == SUB && op.getStopIndex() == ctx.getStart().getStartIndex() - 1;
  }

  public Expr visitGroupExpr(final GroupExprContext ctx) {
    return visitExpression(ctx.expression());
  }

  public Expr visitPostfixExpr(final PostfixExprContext ctx) {
    return switch (ctx.op.getType()) {
      case INC -> visitPostIncExpr(ctx); // a++
      case DEC -> visitPostDecExpr(ctx); // a--
      default -> throw new AssertionError();
    };
  }

  public Expr visitPrefixExpr(final PrefixExprContext ctx) {
    return switch (ctx.op.getType()) {
      case INC -> visitPreIncExpr(ctx); // ++a
      case DEC -> visitPreDecExpr(ctx); // --a
      case ADD -> visitPosExpr(ctx); // +a
      case SUB -> visitNegExpr(ctx); // -a
      case NOT -> visitNotExpr(ctx); // !a
      default -> throw new AssertionError();
    };
  }

  public Expr visitInfixExpr(final InfixExprContext ctx) {
    return switch (ctx.op.getType()) {
      case MUL -> visitMulExpr(ctx); // a * b
      case DIV -> visitDivExpr(ctx); // a / b
      case REM -> visitRemExpr(ctx); // a % b
      case MOD -> visitModExpr(ctx); // a %% b
      case ADD -> visitAddExpr(ctx); // a + b
      case SUB -> visitSubExpr(ctx); // a - b
      case SHL -> visitShlExpr(ctx); // a << b
      case SHR -> visitShrExpr(ctx); // a >> b
      case AND -> visitAndExpr(ctx); // a & b
      case XOR -> visitXorExpr(ctx); // a ^ b
      case IOR -> visitIorExpr(ctx); // a | b
      case CMP -> visitCmpExpr(ctx); // a <=> b
      case LTN -> visitLtnExpr(ctx); // a < b
      case LTE -> visitLteExpr(ctx); // a <= b
      case GTN -> visitGtnExpr(ctx); // a > b
      case GTE -> visitGteExpr(ctx); // a >= b
      case EQL -> visitEqlExpr(ctx); // a == b
      case NEQ -> visitNeqExpr(ctx); // a != b
      default -> throw new AssertionError();
    };
  }

  public Expr visitAssignExpr(final AssignExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitPostIncExpr(final PostfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitPostDecExpr(final PostfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitPreIncExpr(final PrefixExprContext ctx) {
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitPreDecExpr(final PrefixExprContext ctx) {
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitPosExpr(final PrefixExprContext ctx) {
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitNegExpr(final PrefixExprContext ctx) {
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitNotExpr(final PrefixExprContext ctx) {
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  static class Pair<A, B> {
    public final A a;
    public final B b;
    public Pair(final A a, final B b) {
      this.a = a;
      this.b = b;
    }
  }


  public Expr visitAddExpr(final InfixExprContext ctx) {
    return handleBinOpExpr(ctx, "add", Add::apply, (lhs, lhsVal, rhs, rhsVal, outType) -> {
      // Simplification
      return new Expr.AddOp(lhs, rhs, outType);
    });
  }

  public Expr visitSubExpr(final InfixExprContext ctx) {
    return handleBinOpExpr(ctx, "subtract", Sub::apply, (lhs, lhsVal, rhs, rhsVal, outType) -> {
      // Simplification
      return new Expr.SubOp(lhs, rhs, outType);
    });
  }

  public Expr visitMulExpr(final InfixExprContext ctx) {
    return handleBinOpExpr(ctx, "multiply", Mul::apply, (lhs, lhsVal, rhs, rhsVal, outType) -> {
      // Simplification
      return new Expr.MulOp(lhs, rhs, outType);
    });
  }

  public Expr visitDivExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitRemExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitModExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitAndExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitXorExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitIorExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitCmpExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitShlExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitShrExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitLtnExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitGtnExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitLteExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitGteExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitEqlExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitNeqExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitEqrExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitNerExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }



  public Expr handleBinOpExpr(
    final InfixExprContext ctx,
    final String name,
    final BinConstEval eval,
    final BinExprFactory factory
  ) {
    // Post-order traversal
    final Expr lhsExpr = visitExpression(ctx.lhs);
    final Expr rhsExpr = visitExpression(ctx.rhs);

    // Type checking
    final Type lhsType = lhsExpr.type();
    final Type rhsType = rhsExpr.type();
    if (
      // Both input type must be numeric
      !(lhsType instanceof Type.Num) ||
      !(rhsType instanceof Type.Num) ||
      // The input type must not both be bit
      !(lhsType instanceof Type.Bit && lhsType instanceof Type.Bit) ||
      // If one type is ixx and the other is uxx, than the ixx type must be wider than the uxx type
      (rhsType instanceof Type.Ixx && lhsType instanceof Type.Uxx && rhsType.nbits() <= lhsType.nbits()) ||
      (lhsType instanceof Type.Ixx && rhsType instanceof Type.Uxx && lhsType.nbits() <= rhsType.nbits())
    ) throw new ParserException(ctx, String.format(
      "Cannot apply operator \"%s\" to types: %s and %s", name, lhsType, rhsType
    ));

    // Determine output type
    final Type outType = lhsType.order() >= rhsType.order() ? lhsType : rhsType;

    // Implicit type conversion
    final Expr lhs = lhsExpr.type().equals(outType) ? lhsExpr : new Expr.Widen(lhsExpr, outType);
    final Expr rhs = rhsExpr.type().equals(outType) ? rhsExpr : new Expr.Widen(rhsExpr, outType);

    // Constant folding
    final Val lhsVal = lhs instanceof Const ? ((Const) lhs).val() : null;
    final Val rhsVal = rhs instanceof Const ? ((Const) rhs).val() : null;
    if (lhsVal != null && rhsVal != null) return new Const(eval.eval(lhsVal, rhsVal), outType);

    // Simplification
    // TODO: IEEE 754 floating point rules need to be applied here
    // if (isZero(rhsVal)) return lhs; //   0 + rhs => rhs
    // if (isZero(lhsVal)) return rhs; // lhs + 0   => rhs
    // if (isNaN(rhsVal))  return rhs; // NaN + rhs => NaN
    // if (isNaN(lhsVal))  return rhs; // lhs + NaN => NaN

    // Return the expression
    return new Expr.SubOp(lhs, rhs, outType);
  }

  @FunctionalInterface
  private static interface BinConstEval {
    Val eval(Val lhs, final Val rhs);
  }

  @FunctionalInterface
  private static interface BinExprFactory {
    Expr create(Expr lhs, final Val lhsVal, final Expr rhs, final Val rhsVal, final Type outType);
  }


  private boolean isZero(final Val val) {
    return switch (val) {
      case Bit v -> v.value == 0;
      case U08 v -> v.value == 0;
      case U16 v -> v.value == 0;
      case U32 v -> v.value == 0;
      case U64 v -> v.value == 0L;
      case I08 v -> v.value == 0;
      case I16 v -> v.value == 0;
      case I32 v -> v.value == 0;
      case I64 v -> v.value == 0L;
      case F32 v -> v.value == 0.0f;
      case F64 v -> v.value == 0.0d;
      default -> throw new AssertionError();
    };
  }

  private boolean isOne(final Val val) {
    return switch (val) {
      case Bit v -> v.value == 1;
      case U08 v -> v.value == 1;
      case U16 v -> v.value == 1;
      case U32 v -> v.value == 1;
      case U64 v -> v.value == 1L;
      case I08 v -> v.value == 1;
      case I16 v -> v.value == 1;
      case I32 v -> v.value == 1;
      case I64 v -> v.value == 1L;
      case F32 v -> v.value == 1.0f;
      case F64 v -> v.value == 1.0d;
      default -> throw new AssertionError();
    };
  }

  private boolean isNaN(final Val val) {
    return switch (val) {
      case F32 v -> Float.isNaN(v.value);
      case F64 v -> Double.isNaN(v.value);
      default -> false;
    };
  }
}
