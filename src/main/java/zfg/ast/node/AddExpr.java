package zfg.ast.node;

import zfg.ast.type.Type;

public final class AddExpr implements BinExpr {
  private final Expr lhs;
  private final Expr rhs;
  private final Type type;

  public AddExpr(final Expr lhs, final Expr rhs, final Type type) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.type = type;
  }

  @Override public final Type type() { return type; }
  @Override public final Expr lhs() { return lhs; }
  @Override public final Expr rhs() { return rhs; }
}
