package zfg.ast.expr;

import zfg.ast.types.Type;

public class AddExpr implements Expr {
  private final Expr lhs;
  private final Expr rhs;
  private final Type type;

  public AddExpr(final Expr lhs, final Expr rhs, final Type type) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.type = type;
  }

  public Expr lhs() { return lhs; }
  public Expr rhs() { return rhs; }
  public Type type() { return type; }
}
