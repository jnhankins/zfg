package zfg.ast.node;

public interface BinExpr extends Expr {
  public Expr lhs();
  public Expr rhs();
}
