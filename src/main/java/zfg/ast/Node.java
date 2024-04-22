package zfg.ast;

import java.util.List;

import zfg.lang.primitive.Val;

public sealed abstract class Node {
  protected final Type type;
  protected Node(final Type type) {
    this.type = type;
  }
  public final Type type() { return type; }
  public abstract String getText();

  private static final String className(final Object obj) {
    return obj.getClass().getSimpleName();
  }
  private static final String nodeText(final Node node) {
    return className(node) + "[" + className(node.type) + "]";
  }
  private static final String nodeText(final Node node, final String text) {
    return className(node) + "[" + text + ";" + className(node.type) + "]";
  }

  public static sealed abstract class Leaf extends Node {
    public Leaf(final Type type) {
      super(type);
    }
    @Override public final String toString() { return getText(); }
  }

  public static sealed abstract class Unary extends Node {
    protected final Node child;
    protected Unary(final Node child, final Type type) {
      super(type);
      this.child = child;
    }
    public final Node child() { return child; }
    @Override public final String getText() { return nodeText(this); }
    @Override public final String toString() { return getText() + "(" + child + ")";}
  }

  public static sealed abstract class Binary extends Node {
    protected final Node lhs;
    protected final Node rhs;
    protected Binary(final Node lhs, final Node rhs, final Type type) {
      super(type);
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public final Node lhs() { return lhs; }
    public final Node rhs() { return rhs; }
    @Override public final String getText() { return nodeText(this); }
    @Override public final String toString() { return getText() + "(" + lhs + "," + rhs + ")"; }
  }

  public static sealed abstract class Nary extends Node {
    protected final List<Node> children;
    protected Nary(final List<Node> children, final Type type) {
      super(type);
      this.children = children;
    }
    public final List<Node> children() { return children; }
    @Override public final String getText() { return nodeText(this); }
    @Override public final String toString() {
      final StringBuilder buf = new StringBuilder();
      buf.append(getText());
      buf.append("(");
      for (int i = 0; i < children.size(); i++) {
        if (i > 0) buf.append(",");
        buf.append(children.get(i));
      }
      buf.append(")");
      return buf.toString();
    }
  }

  // Compilation unit
  public static final class CompilationUnit extends Nary {
    public CompilationUnit(final List<Node> children) {
      super(children, null); // NULL type is intentional
    }
  }

  // Constant value
  public static final class Const extends Leaf {
    private final Val val;
    public Const(final Val val, final Type type) {
      super(type);
      if (!Type.of(val).equals(type)) throw new AssertionError();
      this.val = val;
    }
    public final Val val() { return val; }
    @Override public final String getText() { return nodeText(this, val.toString()); }
  }

  // Variable reference
  public static final class VarRef extends Leaf {
    private final String name;
    private final Node referent;
    public VarRef(final String name, final Node referent, final Type type) {
      super(type);
      this.name = name;
      this.referent = referent;
    }
    public final String name() { return name; }
    public final Node referent() { return referent; }
    @Override public final String getText() { return nodeText(this, "\"" + name + "\""); }
  }

  // Implicit type conversion from "smaller" type to "larger" type
  public static final class Widen extends Unary {
    public Widen(final Node child, final Type type) {
      super(child, type);
    }
  }

  // Addition: lhs + rhs
  public static final class AddOp extends Binary {
    public AddOp(final Node lhs, final Node rhs, final Type type) {
      super(lhs, rhs, type);
    }
  }

  // Subtraction: lhs - rhs
  public static final class SubOp extends Binary {
    public SubOp(final Node lhs, final Node rhs, final Type type) {
      super(lhs, rhs, type);
    }
  }

  // Multiplication: lhs * rhs
  public static final class MulOp extends Binary {
    public MulOp(final Node lhs, final Node rhs, final Type type) {
      super(lhs, rhs, type);
    }
  }

  // Division: lhs / rhs
  public static final class DivOp extends Binary {
    public DivOp(final Node lhs, final Node rhs, final Type type) {
      super(lhs, rhs, type);
    }
  }

}
