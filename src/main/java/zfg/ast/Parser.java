package zfg.ast;

import static zfg.antlr.ZfgLexer.ADD;
import static zfg.antlr.ZfgLexer.AND;
import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.CMP;
import static zfg.antlr.ZfgLexer.DEC;
import static zfg.antlr.ZfgLexer.DIV;
import static zfg.antlr.ZfgLexer.EQL;
import static zfg.antlr.ZfgLexer.EQR;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.GEQ;
import static zfg.antlr.ZfgLexer.GTN;
import static zfg.antlr.ZfgLexer.INC;
import static zfg.antlr.ZfgLexer.IOR;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.LEQ;
import static zfg.antlr.ZfgLexer.LTN;
import static zfg.antlr.ZfgLexer.MOD;
import static zfg.antlr.ZfgLexer.MUL;
import static zfg.antlr.ZfgLexer.NEQ;
import static zfg.antlr.ZfgLexer.NER;
import static zfg.antlr.ZfgLexer.NOT;
import static zfg.antlr.ZfgLexer.REM;
import static zfg.antlr.ZfgLexer.SHL;
import static zfg.antlr.ZfgLexer.SHR;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgLexer.XOR;

import org.antlr.v4.runtime.ParserRuleContext;

import zfg.antlr.ZfgParser.AssignExprContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.GroupExprContext;
import zfg.antlr.ZfgParser.InfixExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.PathContext;
import zfg.antlr.ZfgParser.PathExprContext;
import zfg.antlr.ZfgParser.PostfixExprContext;
import zfg.antlr.ZfgParser.PrefixExprContext;
import zfg.ast.node.ConExpr;
import zfg.ast.node.Expr;
import zfg.ast.type.Type;
import zfg.ast.type.ValType;
import zfg.core.operation.Add;
import zfg.core.primative.Bit;
import zfg.core.primative.Val;
import zfg.core.primative.Val.Int;

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
      case BitLit -> Bit.parseBit(str)
        .map(val -> new LiteralExpr(ctx, val))
        .orElseThrow(() -> new RuntimeException("Invalid bit literal: " + ctx));
      case IntLit -> Int.parseInt(str)
        .map(val -> new LiteralExpr(ctx, val))
        .orElseThrow(() -> new RuntimeException("Invalid int literal: " + ctx));
      case FltLit -> Flt.parseFlt(str)
        .map(val -> new LiteralExpr(ctx, val))
        .orElseThrow(() -> new RuntimeException("Invalid flt literal: " + ctx));
      default -> throw new AssertionError();
    };
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
      case GTN -> visitGtnExpr(ctx); // a > b
      case LEQ -> visitLeqExpr(ctx); // a <= b
      case GEQ -> visitGeqExpr(ctx); // a >= b
      case EQL -> visitEqlExpr(ctx); // a == b
      case NEQ -> visitNeqExpr(ctx); // a != b
      case EQR -> visitEqrExpr(ctx); // a === b
      case NER -> visitNerExpr(ctx); // a !== b
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

  public Expr visitAddExpr(final InfixExprContext ctx) {
    // Post-order traversal
    final Expr lhsExpr = visitExpression(ctx.lhs);
    final Expr rhsExpr = visitExpression(ctx.rhs);
    // Type checking
    // - Both input types must be be bit, uxx, ixx, or fxx
    // - Cannot both be bit
    // - If one type is uxx and the other is ixx, than the ixx must be wider than the uxx
    final Type lhsType = lhsExpr.type();
    final Type rhsType = rhsExpr.type();
    if (!(lhsType instanceof ValType)) throw new RuntimeException();
    if (!(rhsType instanceof ValType)) throw new RuntimeException();
    final ValType lhsValType = (ValType) lhsType;
    final ValType rhsValType = (ValType) rhsType;
    //
    if (
      (lhsValType == ValType.bit && rhsValType == ValType.bit)) ||
      (lhsValType.flags & IXX != 0 == ValType.bit && rhsValType == ValType.bit))
      {
      throw new ParserException(ctx, String.format(
        "Cannot apply operator \"%s\" to types: %s and %s", "add", lhsValType, rhsValType
      ));
    }

    final ValType outValType = lhsValType.compareTo(rhsValType) >= 0 ? lhsValType : rhsValType;

    // Implicit type conversion
    final Expr lhs = implCast(lhsExpr, outValType);
    final Expr rhs = implCast(rhsExpr, outValType);
    // Constant folding
    if (lhs instanceof ConExpr && rhs instanceof ConExpr) {
      final Val lhsVal = ((ConExpr) lhs).val();
      final Val rhsVal = ((ConExpr) rhs).val();
      @SuppressWarnings("unchecked")
      final Val outVal = ((Add.I<Val>)lhsVal).add(rhsVal);
      return new ConExpr(outVal, outValType);
    }
    // Return
    return new AddExpr(lhs, rhs, outValType);
  }

  public Expr visitSubExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitMulExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
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

  public Expr visitLeqExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitGeqExpr(final InfixExprContext ctx) {
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
}

// Order
// Logic
// Arith
// Shift


// Ordered:     Cmp(<=>), Gtn(>), Geq(>=), Leq(<=), Ltn(<), Eql(==), Neq(!=)
// Arithmetic:  Neg(-.), Add(+), Sub(-), Mul(*), Div(/), Rem(%), Mod(&&)
// Bitwise:     Not(!), And(&), Xor(^), Ior(|), Shl(<<), Shr(>>)
// ShortCircut: And(&&), Ior(||)

// set bit n     a |  (1 << b)
// unset bit n   a & ~(1 << b)
// toggle bit n  a ^  (1 << b)
// test bit n    (a >>> b) & 1 != 0

// NOT      !a   ,    ~a
// AND    a && b ,   a & b
// NAND !(a && b), ~(a & b)
// IOR    a || b ,   a | b
// XOR    a != b ,   a ^ b
// NOR  !(a || b), ~(a | b)
// NXOR   a == b ,    a ^ b
// XNOR ~(a ^ b)

/**
 * a0b0 a0b1
 * a1b0 a1b1
          nor
  F                    !a
  0 0    1 0    0 1    1 1
  0 0    0 0    0 0    0 0
  a&!b   !b     a^b  !(a&b)
  0 0    1 0    0 1    1 1
  1 0    1 0    1 0    1 0
  a&b   !(a^b)  b      !a|b
  0 0    1 0    0 1    1 1
  0 1    0 1    0 1    0 1

  a      a|!b   a|b    T
  0 0    1 0    0 1    1 1
  1 1    1 1    1 1    1 1

  (!a && !b) || (a && b)
  !(!(!a && !b) && !(a && b))
  !((a || b) && (!a || !b))


*/

