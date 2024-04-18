package zfg.ast.expr;

import zfg.ast.type.Type;

public class ImplCastExpr implements Expr {
  private final Expr expr;
  private final Type type;

  public ImplCastExpr(final Expr expr, final Type type) {
    this.expr = expr;
    this.type = type;
  }

  public Expr expr() { return expr; }
  public Type type() { return type; }
}
