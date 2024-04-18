package zfg.ast.node;

import zfg.core.primative.Val;
import zfg.ast.type.Type;

public final class ConExpr implements Expr {
  private final Val val;
  private final Type type;

  public ConExpr(final Val val, final Type type) {
    this.val = val;
    this.type = type;
  }

  public final Val val() { return val; }
  @Override public final Type type() { return type; }
}
