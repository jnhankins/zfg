package zfg.ast;

import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.INC;
import static zfg.antlr.ZfgLexer.DEC;
import static zfg.antlr.ZfgLexer.ADD;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgLexer.CMP;
import static zfg.antlr.ZfgLexer.GTN;
import static zfg.antlr.ZfgLexer.GEQ;
import static zfg.antlr.ZfgLexer.LEQ;
import static zfg.antlr.ZfgLexer.LTN;
import static zfg.antlr.ZfgLexer.NOT;
import static zfg.antlr.ZfgLexer.MUL;
import static zfg.antlr.ZfgLexer.DIV;
import static zfg.antlr.ZfgLexer.REM;
import static zfg.antlr.ZfgLexer.MOD;
import static zfg.antlr.ZfgLexer.SHL;
import static zfg.antlr.ZfgLexer.SHR;
import static zfg.antlr.ZfgLexer.AND;
import static zfg.antlr.ZfgLexer.XOR;
import static zfg.antlr.ZfgLexer.IOR;
import static zfg.antlr.ZfgLexer.EQL;
import static zfg.antlr.ZfgLexer.NEQ;
import static zfg.antlr.ZfgLexer.EQR;
import static zfg.antlr.ZfgLexer.NER;


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
import zfg.ast.expr.Expr;
import zfg.ast.expr.Expr.LiteralExpr;
import zfg.ast.types.PriType;
import zfg.ast.types.Type;
import zfg.num.Bit;
import zfg.num.Flt;
import zfg.num.Int;

public class Parser {
  public static class ParserException extends RuntimeException {
    public ParserException(final ParserRuleContext ctx, final String message) {
      this(ctx, message, null);
    }
    public ParserException(final ParserRuleContext ctx, final Throwable cause) {
      this(ctx, null, cause);
    }
    public ParserException(final ParserRuleContext ctx, final String message, final Throwable cause) {
      super(message, cause);
    }
  }

  public Expr visitExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case PathExprContext sub -> visitPathExpr(sub);
      case LiteralExprContext sub -> visitLiteralExpr(sub);
      case GroupExprContext sub -> visitGroupExpr(sub);
      case PostfixExprContext sub -> visitPostfixExpr(sub);
      case PrefixExprContext sub -> visitPrefixExpr(sub);
      case InfixExprContext sub -> visitInfixExpr(sub);
      case AssignExprContext sub -> visitAssignExpr(sub);
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
    Expr lhs = visitExpression(ctx.lhs);
    Expr rhs = visitExpression(ctx.rhs);
    // Type checking
    final PriType resultType = switch (lhs.type(), rhs.type()) {
      case (PriType lhsType, PriType rhsType) -> {

      }
      default -> throw new ParserException(ctx, "Cannot add non-privitive non-numeric types: " + lhs.type() + " + " + rhs.type());
    }
    final Type lhsType = lhs.type();
    final Type rhsType = rhs.type();
    if (!(lhsType instanceof PriType) || !(rhsType instanceof PriType))
      throw new ParserException(ctx, "Cannot add non-privitive non-numeric types: " + lhsType + " + " + rhsType);
    final PriType lhsPriType = (PriType) lhsType;
    final PriType rhsPriType = (PriType) rhsType;
    if (!lhsPriType.isNum() || !rhsPriType.isNum())
      throw new ParserException(ctx, "Cannot add non-numeric types: " + lhsPriType + " + " + rhsPriType);
    if ((lhsPriType.isSignedInt() && !rhsPriType.isSignedInt()) || (!lhsPriType.isSignedInt() && rhsPriType.isSignedInt()))
      throw new ParserException(ctx, "Cannot add signed and unsigned integers. Consider casting: " + lhsPriType + " + " + rhsPriType);
    // Result type
    final PriType

    // Constant propogation

    // Simplification
    throw new UnsupportedOperationException("TODO");
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
