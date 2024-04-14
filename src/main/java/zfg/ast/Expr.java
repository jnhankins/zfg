package zfg.ast;

import java.util.Objects;

public abstract class Expr {

  public static abstract class UnaryExpr<C> extends Expr {
    public final C child;

    public UnaryExpr(final C child) {
      Objects.requireNonNull(child);
      this.child = child;
    }
  }

  public static class PosExpr extends UnaryExpr<Expr> {
    public PosExpr(final Expr child) {
      super(child);
    }
  }

  public static class NegExpr extends UnaryExpr<Expr> {
    public NegExpr(final Expr child) {
      super(child);
    }
  }

  public static abstract class BinaryExpr<L, R> extends Expr {
    public final L lhs;
    public final R rhs;

    public BinaryExpr(final L lhs, final R rhs) {
      Objects.requireNonNull(lhs);
      Objects.requireNonNull(rhs);
      this.lhs = lhs;
      this.rhs = rhs;
    }
  }




}
