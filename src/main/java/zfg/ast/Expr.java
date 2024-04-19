package zfg.ast;

import zfg.core.primative.Val;

public sealed interface Expr {
  public Type type();

  public static abstract class Base implements Expr {
    private final Type type;
    public Base(final Type type) { this.type = type; }
    public final @Override Type type() { return type; }
  }

  /** Constant expression. */
  public static final class Const implements Expr {
    private final Type type;
    private final Val val;
    public Const(final Val val, final Type type) {
      // TODO: Remove me for performance gains
      if (!Type.of(val).equals(type)) throw new AssertionError();
      this.val = val;
      this.type = type;
    }
    public final Val val() { return val; }
    public final Type type() { return type; }
  }

  /** Widen expression. Implicitly converts a "smaller" type to a "larger" type. */
  public static final class Widen implements Expr {
    private final Expr expr;
    private final Type type;
    public Widen(final Expr expr, final Type type) {
      this.expr = expr;
      this.type = type;
    }
    public final Expr expr() { return expr; }
    public final Type type() { return type; }
  }

  /** Binary index operation expression. */
  public static sealed abstract class InfOp implements Expr {
    protected final Expr lhs;
    protected final Expr rhs;
    protected final Type type;
    protected InfOp(final Expr lhs, final Expr rhs, final Type type) {
      this.lhs = lhs;
      this.rhs = rhs;
      this.type = type;
    }
    public final Expr lhs() { return lhs; }
    public final Expr rhs() { return rhs; }
    public final Type type() { return type; }
  }

  public static final class AddOp extends InfOp {
    public AddOp(final Expr lhs, final Expr rhs, final Type type) { super(lhs, rhs, type); }
  }

  public static final class SubOp extends InfOp {
    public SubOp(final Expr lhs, final Expr rhs, final Type type) { super(lhs, rhs, type); }
  }

  public static final class MulOp extends InfOp {
    public MulOp(final Expr lhs, final Expr rhs, final Type type) { super(lhs, rhs, type); }
  }

  public static final class DivOp extends InfOp {
    public DivOp(final Expr lhs, final Expr rhs, final Type type) { super(lhs, rhs, type); }
  }

  public static final class RemOp extends InfOp {
    public RemOp(final Expr lhs, final Expr rhs, final Type type) { super(lhs, rhs, type); }
  }
}
