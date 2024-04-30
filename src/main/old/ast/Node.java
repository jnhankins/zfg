package zfg.old.ast;

import java.util.List;

import zfg.lang.val.Val;

public sealed interface Node {
  public Type type();
  public default String selfString() { return selfString(new StringBuilder()).toString(); }
  public default String treeString() { return treeString(new StringBuilder()).toString(); }
  public StringBuilder selfString(final StringBuilder buf);
  public StringBuilder treeString(final StringBuilder buf);

  public static sealed abstract class Leaf<T> implements Node {
    protected final Type type;
    protected final T value;
    protected Leaf(final Type type, final T value) {
      this.type = type;
      this.value = value;
    }
    public final T value() { return value; };
    @Override public final Type type() { return type; }
    @Override public final StringBuilder selfString(final StringBuilder buf) {
      buf.append((this instanceof Error ? ((Error)this).clazz : getClass()).getSimpleName());
      buf.append("[");
      buf.append(type.getClass().getSimpleName());
      buf.append("](");
      buf.append(value == null ? "null" : value.toString());
      buf.append(")");
      return buf;
    }
    @Override public final StringBuilder treeString(final StringBuilder buf) {
      return selfString(buf);
    }

    public static final class Error extends Leaf<String> {
      protected final Class<? extends Leaf<?>> clazz;
      public Error(final Class<? extends Leaf<?>> clazz, final String msg) {
        super(Type.err, msg);
        this.clazz = clazz;
      }
    }
  }

  public static sealed abstract class Unary implements Node {
    protected final Type type;
    protected final Node child;
    protected Unary(final Type type, final Node child) {
      this.type = type;
      this.child = child;
    }
    public final Node child() { return child; }
    @Override public final Type type() { return type; }
    @Override public final StringBuilder selfString(final StringBuilder buf) {
      buf.append(getClass().getSimpleName());
      buf.append("[");
      buf.append(type.getClass().getSimpleName());
      buf.append("]");
      return buf;
    }
    @Override public final StringBuilder treeString(final StringBuilder buf) {
      selfString(buf);
      buf.append("(");
      child.treeString(buf);
      buf.append(")");
      return buf;
    }
  }

  public static sealed abstract class Binary implements Node {
    protected final Type type;
    protected final Node lhs;
    protected final Node rhs;
    protected Binary(final Type type, final Node lhs, final Node rhs) {
      this.type = type;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public final Node lhs() { return lhs; }
    public final Node rhs() { return rhs; }
    @Override public final Type type() { return type; }
    @Override public final StringBuilder selfString(final StringBuilder buf) {
      buf.append(getClass().getSimpleName());
      buf.append("[");
      buf.append(type.getClass().getSimpleName());
      buf.append("]");
      return buf;
    }
    @Override public final StringBuilder treeString(final StringBuilder buf) {
      selfString(buf);
      buf.append("(");
      lhs.treeString(buf);
      buf.append(",");
      rhs.treeString(buf);
      buf.append(")");
      return buf;
    }
  }

  public static sealed abstract class Nary implements Node {
    protected final Type type;
    protected final List<Node> children;
    protected Nary(final Type type, final List<Node> children) {
      this.type = type;
      this.children = children;
    }
    public final List<Node> children() { return children; }
    @Override public final Type type() { return type; }
    @Override public final StringBuilder selfString(final StringBuilder buf) {
      buf.append(getClass().getSimpleName());
      buf.append("[");
      buf.append(type.getClass().getSimpleName());
      buf.append("]");
      return buf;
    }
    @Override public final StringBuilder treeString(final StringBuilder buf) {
      selfString(buf);
      buf.append("(");
      for (int i = 0; i < children.size(); i++) {
        if (i > 0) buf.append(",");
        children.get(i).treeString(buf);
      }
      buf.append(")");
      return buf;
    }
  }

  // Constant value
  public static final class Const extends Leaf<Val> {
    public Const(final Type type, final Val val) { super(type, val); }
  }

  // Explicit cast
  public static sealed class Cast extends Unary {
    public Cast(final Type type, final Node child) { super(type, child); }
  }

  // Implicit cast
  public static final class ImplCast extends Cast {
    public ImplCast(final Type type, final Node child) { super(type, child); }
  }

  // Unary Identity: +a
  public static final class Ident extends Unary {
    public Ident(final Type type, final Node child) { super(type, child); }
  }

  // Unary Negation: -a
  public static final class Neg extends Unary {
    public Neg(final Type type, final Node child) { super(type, child); }
  }

  // Bitwise Complement: ~a
  public static final class Not extends Unary {
    public Not(final Type type, final Node child) { super(type, child); }
  }

  // Logical Not: !a
  public static final class Lnt extends Unary {
    public Lnt(final Type type, final Node child) { super(type, child); }
  }

  // Arithmetic Addition: a + b
  public static final class Add extends Binary {
    public Add(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Arithmetic Subtraction: a - b
  public static final class Sub extends Binary {
    public Sub(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Arithmetic Multiplication: a * b
  public static final class Mul extends Binary {
    public Mul(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Arithmetic Division: a / b
  public static final class Div extends Binary {
    public Div(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Arithmetic Remainder: a % b
  public static final class Rem extends Binary {
    public Rem(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Arithmetic Modulus: a %% b
  public static final class Mod extends Binary {
    public Mod(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Bitwise AND: a & b
  public static final class And extends Binary {
    public And(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Bitwise OR: a | b
  public static final class Ior extends Binary {
    public Ior(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Bitwise XOR: a ^ b
  public static final class Xor extends Binary {
    public Xor(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Bitwise Left Shift: a << b
  public static final class Shl extends Binary {
    public Shl(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Bitwise Right Shift: a >> b
  public static final class Shr extends Binary {
    public Shr(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Relational Three-Way Comparison: a <=> b
  public static final class Twc extends Binary {
    public Twc(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Relational Less-Than: a < b
  public static final class Ltn extends Binary {
    public Ltn(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Relational Greater-Than: a > b
  public static final class Gtn extends Binary {
    public Gtn(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Relational Less-Than-Or-Equal: a <= b
  public static final class Leq extends Binary {
    public Leq(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Relational Greater-Than-Or-Equal: a >= b
  public static final class Geq extends Binary {
    public Geq(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Relational Equal: a == b
  public static final class Eql extends Binary {
    public Eql(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Relational Not-Equal: a != b
  public static final class Neq extends Binary {
    public Neq(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Logical AND (short-circuiting): a && b
  public static final class Lcj extends Binary {
    public Lcj(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // Logical OR (short-circuiting): a || b
  public static final class Ldj extends Binary {
    public Ldj(final Type type, final Node lhs, final Node rhs) { super(type, lhs, rhs); }
  }

  // TODO remove this when we have a an actual implementation for selaed class Nary
  public static final class TempTODO extends Nary {
    public TempTODO(final Type type, final List<Node> children) { super(type, children); }
  }
}
