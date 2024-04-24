package zfg.parse;


import zfg.core.inst;
import zfg.core.type.Type;

public final class node {

  /** A ZFG controll flow graph node. */
  public static sealed interface Node {
    // type
    public Type type();

    // toSelfString and toTreeString
    public default String toSelfString() { return toSelfString(new StringBuilder()).toString(); }
    public default String toTreeString() { return toTreeString(new StringBuilder()).toString(); }
    public StringBuilder toSelfString(final StringBuilder buf);
    public StringBuilder toTreeString(final StringBuilder buf);
  }


  /** A node with no children. */
  private static sealed abstract class Leaf<T> implements Node {
    private final Type type;
    private final T value;

    private Leaf(final Type type, final T value) {
      this.type = type;
      this.value = value;
    }

    // getters
    public final Type type() { return type; }
    public final T value() { return value; }

    // toString, toSelfString, and toTreeString
    public final String toString() { return toSelfString(); }
    public final StringBuilder toTreeString(final StringBuilder buf) { return toSelfString(buf); }
    public final StringBuilder toSelfString(final StringBuilder buf) {
      buf.append(getClass().getSimpleName());
      buf.append("[");
      buf.append(type);
      buf.append("]");
      if (value != null) {
        buf.append("(");
        buf.append(value);
        buf.append(")");
      }
      return buf;
    }
  }

  /** A constant expression. */
  public static final class Const extends Leaf<inst.Inst<?>> {
    public static final Const err = new Const(zfg.core.type.err, null);
    public static Const of(final inst.Inst<?> value) { return new Const(value.type(), value); }
    private Const(final Type type, final inst.Inst<?> value) { super(type, value); }
  }

  // module
  private node() {}
}
