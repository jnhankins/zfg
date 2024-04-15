package zfg.ast;

import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.INC;
import static zfg.antlr.ZfgLexer.DEC;
import static zfg.antlr.ZfgLexer.ADD;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgParser.CMP;
import static zfg.antlr.ZfgParser.GT;
import static zfg.antlr.ZfgParser.LE;
import static zfg.antlr.ZfgParser.LT;
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
import static zfg.antlr.ZfgLexer.EQ;
import static zfg.antlr.ZfgLexer.NE;
import static zfg.antlr.ZfgLexer.EQR;
import static zfg.antlr.ZfgLexer.NER;


import org.antlr.v4.runtime.CharStreams;
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
import zfg.ast.Expr.LiteralExpr;
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
    // TODO
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
      case INC -> visitPostIncExpr(ctx);
      case DEC -> visitPostDecExpr(ctx);
      default -> throw new AssertionError();
    };
  }

  public Expr visitPrefixExpr(final PrefixExprContext ctx) {
    return switch (ctx.op.getType()) {
      case INC -> visitPreIncExpr(ctx);
      case DEC -> visitPreDecExpr(ctx);
      case ADD -> visitPosExpr(ctx);
      case SUB -> visitNegExpr(ctx);
      case NOT -> visitNotExpr(ctx);
      default -> throw new AssertionError();
    };
  }

  public Expr visitInfixExpr(final InfixExprContext ctx) {
    return switch (ctx.op.getType()) {
      case MUL -> visitMulExpr(ctx);
      case DIV -> visitDivExpr(ctx);
      case REM -> visitRemExpr(ctx);
      case MOD -> visitModExpr(ctx);
      case ADD -> visitAddExpr(ctx);
      case SUB -> visitSubExpr(ctx);
      case SHL -> visitShlExpr(ctx);
      case SHR -> visitShrExpr(ctx);
      case AND -> visitAndExpr(ctx);
      case XOR -> visitXorExpr(ctx);
      case IOR -> visitIorExpr(ctx);
      case CMP -> visitCmpExpr(ctx);
      case LT  -> visitLtExpr(ctx);
      case GT  -> visitGtExpr(ctx);
      case LE  -> visitLeExpr(ctx);
      case GE  -> visitGeExpr(ctx);
      case EQ  -> visitEqExpr(ctx);
      case NE  -> visitNeExpr(ctx);
      case EQR -> visitEqrExpr(ctx);
      case NER -> visitNerExpr(ctx);
      default -> throw new AssertionError();
    }
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

  public Expr visitAddExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitSubExpr(final InfixExprContext ctx) {
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

  public Expr visitLtExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitGtExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitLeExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitGeExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitEqExpr(final InfixExprContext ctx) {
    final Expr lhs = visitExpression(ctx.lhs);
    final Expr rhs = visitExpression(ctx.rhs);
    throw new UnsupportedOperationException("TODO");
  }

  public Expr visitNeExpr(final InfixExprContext ctx) {
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
