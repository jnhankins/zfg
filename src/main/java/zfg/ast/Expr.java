package zfg.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import zfg.num.Lit;

public interface Expr {


  public static class LiteralExpr implements Expr {
    public final Lit value;

    public LiteralExpr(final ParserRuleContext ctx, final Lit value) {
      this.value = value;
    }
  }

}
