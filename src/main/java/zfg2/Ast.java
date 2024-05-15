package zfg2;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Arrays;

public sealed interface Ast {
  @Override String toString();
  StringBuilder toString(final StringBuilder sb);
  StringBuilder toString(final StringBuilder sb, final Map<Object, Integer> seen);
  boolean toSelfString(final StringBuilder sb, final Map<Object, Integer> seen);
  Type type();

  public static sealed abstract class AstBase implements Ast {
    @Override public final String toString() {
      return toString(new StringBuilder()).toString();
    }
    @Override public final StringBuilder toString(final StringBuilder sb) {
      return toString(sb, new HashMap<>());
    }
    @Override public final boolean toSelfString(
      final StringBuilder sb,
      final Map<Object, Integer> seen
    ) {
      final Integer found = seen.get(this);
      final int idx;
      if (found != null)  idx = found;
      else seen.put(this, idx = seen.size());
      sb.append(getClass().getSimpleName());
      sb.append("#").append(idx);
      sb.append("[").append(type()).append("]");
      return found != null;
    }
    @Override public abstract StringBuilder toString(
      final StringBuilder sb,
      final Map<Object, Integer> seen
    );
    @Override public abstract Type type();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Module
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class Module extends AstBase {
    public final String fqn;
    public final Stmt[] stmts;

    public Module(final String fqn, final Stmt[] stmts) {
      assert fqn != null;
      assert true; // TODO: validate fqn
      assert stmts != null;
      assert Arrays.stream(stmts).noneMatch(Objects::isNull);
      this.fqn   = fqn;
      this.stmts = stmts;
    }
    @Override public StringBuilder toString(
      final StringBuilder sb,
      final Map<Object, Integer> seen
    ) {
      if (toSelfString(sb, seen)) return sb;
      sb.append('(');
      sb.append('"').append(fqn).append('"');
      sb.append(", ");
      sb.append('[');
      for (int i = 0; i < stmts.length; i++) {
        if (i > 0) sb.append(", ");
        stmts[i].toString(sb, seen);
      }
      sb.append(']');
      return sb.append(')');
    }
    @Override public Type type() { return null; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expression
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed interface Expr extends Ast {}

  public static final class ConstExpr extends AstBase implements Expr {
    public final Inst val;
    public ConstExpr(final Inst val) {
      assert val != null;
      this.val = val;
    }
    @Override public StringBuilder toString(
      final StringBuilder sb,
      final Map<Object, Integer> seen
    ) {
      if (toSelfString(sb, seen)) return sb;
      sb.append('(');
      val.toString(sb);
      return sb.append(')');
    }
    @Override public Type type() { return val.type(); }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statement
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed interface Stmt extends Ast {}

  public static final class ExprStmt extends AstBase implements Stmt {
    public final Expr expr;
    public ExprStmt(final Expr expr) {
      assert expr != null;
      this.expr = expr;
    }
    @Override public StringBuilder toString(
      final StringBuilder sb,
      final Map<Object, Integer> seen
    ) {
      if (toSelfString(sb, seen)) return sb;
      sb.append('(');
      expr.toString(sb);
      return sb.append(')');
    }
    @Override public Type type() { return null; }
  }
}
