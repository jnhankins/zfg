package zfg;

import java.util.HashMap;
import java.util.Map;

public final class nodes {
  private nodes() {}

  public static interface Node {
    public types.Type type();
    @Override public String toString();
    public void toString(final StringBuilder sb);
    public void toString(final StringBuilder sb, final Map<Object, Integer> seen);
  }

  public static final Error error = new Error();
  public static final class Error implements Node {
    private Error() {}
    @Override public types.Type type() { return types.Err; }
    @Override public String toString() { return "Error"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) { sb.append(this); }
  }

  public static final class Const implements Node {
    public final insts.Inst value;

    public Const(final insts.Inst value) {
      assert value != null;
      this.value = value;
    }

    @Override public types.Type type() { return value.type(); }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { sb.append("Const("); value.toString(sb); sb.append(")");}
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) { value.toString(sb); }
  }

  public static final class InfixOp implements Node {
    public static enum Op {
      Add, Sub, Mul, Div, Rem, Mod,
      And, Ior, Xor,
      Shl, Shr,
      Cmp,
      Eql, Neq, Ltn, Gtn, Leq, Geq,
      Lcj, Ldj;
    }
    public final types.Type type;
    public final Node lhs;
    public final Node rhs;
    public final Op op;

    public InfixOp(final types.Type type, final Node lhs, final Node rhs, final Op op) {
      assert type != null;
      assert lhs != null;
      assert rhs != null;
      assert op != null;
      this.type = type;
      this.lhs = lhs;
      this.rhs = rhs;
      this.op = op;
    }

    @Override public types.Type type() { return type; }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
      if (seen.get(this) instanceof Integer idx) {
        sb.append("InfixOp#");
        sb.append(idx);
      } else {
        final int idx = seen.size();
        seen.put(this, idx);
        sb.append("InfixOp#");
        sb.append(idx);
        sb.append('(');
        sb.append(op.name());
        sb.append(", ");
        lhs.toString(sb, seen);
        sb.append(", ");
        rhs.toString(sb, seen);
        sb.append(')');
      }
    }
  }
}
