package zfg.ast.expr;

import zfg.ast.type.Type;

public class ConstExpr implements Expr {
  private final Type type;
  private final Object value; // TODO

  public ConstExpr(final Type type, final Object value) {
    this.type = type;
    this.value = value;
  }

  public Type type() { return type; }
  public Object value() { return value; }
}
