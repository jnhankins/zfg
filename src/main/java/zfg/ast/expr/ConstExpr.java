package zfg.ast.expr;

import zfg.ast.types.Type;
import zfg.num.Lit;

public class ConstExpr implements Expr {
  private final Type type;
  private final Lit value;

  public ConstExpr(final Type type, final Lit value) {
    this.type = type;
    this.value = value;
  }

  public Type type() { return type; }
  public Lit value() { return value; }
}
