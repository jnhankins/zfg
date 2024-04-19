package zfg.ast;

import zfg.core.primative.Val;

public sealed abstract class Node {
  private final Type type;
  protected Node(final Type type) { this.type = type; }
  public final Type type() { return type; }

  public static non-sealed abstract class LeafNode extends Node {
    public LeafNode(final Type type) { super(type); }
  }

  public static non-sealed abstract class UnaryNode extends Node {
    private final Node child;
    public UnaryNode(final Node child, final Type type) {
      super(type);
      this.child = child;
    }
    public final Node child() { return child; }
  }

  public static non-sealed class BinaryNode extends Node {
    private final Node lhs;
    private final Node rhs;
    public BinaryNode(final Node lhs, final Node rhs, final Type type) {
      super(type);
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public final Node lhs() { return lhs; }
    public final Node rhs() { return rhs; }
  }

  public static final class Const extends LeafNode {
    private final Val val;
    public Const(final Val val, final Type type) {
      super(type);
      if (!Type.of(val).equals(type)) throw new AssertionError();
      this.val = val;
    }
    public final Val val() { return val; }
  }

  public static final class Widen extends UnaryNode {
    public Widen(final Node child, final Type type) { super(child, type); }
  }

  public static final class AddOp extends BinaryNode {
    public AddOp(final Node lhs, final Node rhs, final Type type) { super(lhs, rhs, type); }
  }
  public static final class SubOp extends BinaryNode {
    public SubOp(final Node lhs, final Node rhs, final Type type) { super(lhs, rhs, type); }
  }
  public static final class MulOp extends BinaryNode {
    public MulOp(final Node lhs, final Node rhs, final Type type) { super(lhs, rhs, type); }
  }
  public static final class DivOp extends BinaryNode {
    public DivOp(final Node lhs, final Node rhs, final Type type) { super(lhs, rhs, type); }
  }
  public static final class ModOp extends BinaryNode {
    public ModOp(final Node lhs, final Node rhs, final Type type) { super(lhs, rhs, type); }
  }
}
