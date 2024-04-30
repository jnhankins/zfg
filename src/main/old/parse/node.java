package zfg.old.parse;

import java.util.List;

import zfg.old.core.inst;
import zfg.old.core.type;
import zfg.old.core.type.Type;

public final class node {

  /** A ZFG controll flow graph (CFG) node. */
  public static sealed interface Node {
    public Type type();
    public default String toSelfString() { return toSelfString(new StringBuilder()).toString(); }
    public default String toTreeString() { return toTreeString(new StringBuilder()).toString(); }
    public StringBuilder toSelfString(final StringBuilder buf);
    public StringBuilder toTreeString(final StringBuilder buf);
  }
  /** Leaf node mixin */
  public static sealed interface Leaf<T> extends Node {
    public T value();
    public default StringBuilder toSelfString(final StringBuilder buf) {
      buf.append(getClass().getSimpleName());
      buf.append("[");
      buf.append(type());
      buf.append("]");
      if (value() != null) {
        buf.append("(");
        buf.append(value());
        buf.append(")");
      }
      return buf;
    }
    public default StringBuilder toTreeString(final StringBuilder buf) {
      return toSelfString(buf);
    }
  }
  /** Unary node mixin */
  public static sealed interface Unary<T extends Node> extends Node {
    public T child();
    public default StringBuilder toSelfString(final StringBuilder buf) {
      buf.append(getClass().getSimpleName());
      buf.append("[");
      buf.append(type());
      buf.append("]");
      return buf;
    }
    public default StringBuilder toTreeString(final StringBuilder buf) {
      toSelfString(buf);
      buf.append("(");
      child().toTreeString(buf);
      buf.append(")");
      return buf;
    }
  }
  // /** Binary node mixin */
  // public static sealed interface Binary<L extends Node, R extends Node> extends Node {
  //   public L lhs();
  //   public R rhs();
  //   public default StringBuilder toSelfString(final StringBuilder buf) {
  //     buf.append(getClass().getSimpleName());
  //     buf.append("[");
  //     buf.append(type());
  //     buf.append("]");
  //     return buf;
  //   }
  //   public default StringBuilder toTreeString(final StringBuilder buf) {
  //     toSelfString(buf);
  //     buf.append("(");
  //     lhs().toTreeString(buf);
  //     buf.append(",");
  //     rhs().toTreeString(buf);
  //     buf.append(")");
  //     return buf;
  //   }
  // }

  /** Node base class class. */
  public static sealed abstract class BaseNode implements Node {
    public final type.Type type;
    public final Type type() { return type; }
    protected BaseNode(final Type type) { this.type = type; }
    public final String toString() { return toSelfString(); }
  }
  /** Leaf node base class */
  private static sealed abstract class LeafNode<T> extends BaseNode implements Leaf<T> {
    public final T value;
    public final T value() { return value; }
    protected LeafNode(final type.Type type, final T value) {
      super(type);
      this.value = value;
    }
  }
  // /** Unary node base class */
  // private static sealed abstract class UnaryNode<T extends Node> extends BaseNode implements Unary<T> {
  //   public final T child;
  //   public final T child() { return child; }
  //   protected UnaryNode(final type.Type type, final T child) {
  //     super(type);
  //     this.child = child;
  //   }
  // }
  // /** Binary node base class */
  // private static sealed abstract class BinaryNode<L extends Node, R extends Node> extends BaseNode implements Binary<L, R>  {
  //   public final L lhs;
  //   public final L lhs() { return lhs; }
  //   public final R rhs;
  //   public final R rhs() { return rhs; }
  //   protected BinaryNode(final type.Type type, final L lhs, final R rhs) {
  //     super(type);
  //     this.lhs = lhs;
  //     this.rhs = rhs;
  //   }
  // }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Functions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** A function local variable */
  public static sealed class LocalVariable extends LeafNode<String> {
    public LocalVariable(final Type type, final String name) { super(type, name); }
  }

  /** A function argument */
  public static final class FunctionArgument extends LocalVariable {
    public FunctionArgument(final Type type, final String name) { super(type, name); }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statements
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** A node that manipulates the stack but does not leave a value on the stack */
  public static sealed abstract class Statement extends BaseNode {
    public static final Statement err = new ErrorStmt();
    protected Statement(final Type type) { super(type); }
  }
  /** A leaf statement */
  private static sealed abstract class LeafStmt<T> extends Statement implements Leaf<T> {
    public final T value;
    public final T value() { return value; }
    protected LeafStmt(final type.Type type, final T value) {
      super(type);
      this.value = value;
    }
  }
  /** An error statement */
  private static final class ErrorStmt extends LeafStmt<Void> {
    private ErrorStmt() { super(zfg.old.core.type.err, null); }
  }
  /** A unary statement */
  private static sealed abstract class UnaryStmt<T extends Node> extends Statement implements Unary<T> {
    public final T child;
    public final T child() { return child; }
    protected UnaryStmt(final type.Type type, final T child) {
      super(type);
      this.child = child;
    }
  }
  // /** An binary statement */
  // private static sealed abstract class BinaryStmt extends Expression implements Binary  {
  //   public final Expression lhs;
  //   public final Expression rhs;
  //   public final Expression lhs() { return lhs; }
  //   public final Expression rhs() { return rhs; }
  //   protected BinaryExpr(final type.Type type, final Expression lhs, final Expression rhs) {
  //     super(type);
  //     this.lhs = lhs;
  //     this.rhs = rhs;
  //   }
  // }

  /** A function declaration statement */
  public static final class Function extends Statement {
    public final String name;
    public final List<node.LocalVariable> vars;
    public final List<node.Statement> body;
    public Function(
      final Type type,
      final String name,
      final List<node.LocalVariable> vars,
      final List<node.Statement> body
    ) {
      super(type);
      this.name = name;
      this.vars = vars;
      this.body = body;
    }

    // toSelfString, and toTreeString
    public final StringBuilder toSelfString(final StringBuilder buf) {
      buf.append(getClass().getSimpleName());
      buf.append("[");
      buf.append(type);
      buf.append("](\"");
      buf.append(name);
      buf.append("\")");
      return buf;
    }
    public final StringBuilder toTreeString(final StringBuilder buf) { return toSelfString(buf); }
  }

  /** A function return statement */
  public static final class FunctionReturnStmt extends UnaryStmt<Expression> {
    public FunctionReturnStmt(final Expression child) { super(child.type, child); }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////


  /** A node that manipultes the stack leaves a value on the stack. */
  public static sealed abstract class Expression extends BaseNode {
    public static final Expression err = new ErrorExpr();
    protected Expression(final Type type) { super(type); }
  }
  /** An error expression */
  private static final class ErrorExpr extends Expression {
    private ErrorExpr() { super(zfg.old.core.type.err); }
  }
  /** A expression with no children. */
  private static sealed abstract class LeafExpr<T> extends Expression implements Leaf<T> {
    public final T value;
    public final T value() { return value; }
    protected LeafExpr(final type.Type type, final T value) {
      super(type);
      this.value = value;
    }
  }
  // /** An expression with one child expression. */
  // private static sealed abstract class UnaryExpr extends Expression implements Unary<Expression> {
  //   public final Expression child;
  //   public final Expression child() { return child; }
  //   protected UnaryExpr(final type.Type type, final Expression child) {
  //     super(type);
  //     this.child = child;
  //   }
  // }
  // /** An expression with two children: lhs and rhs. */
  // private static sealed abstract class BinaryExpr extends Expression implements Binary<Expression, Expression>  {
  //   public final Expression lhs;
  //   public final Expression rhs;
  //   public final Expression lhs() { return lhs; }
  //   public final Expression rhs() { return rhs; }
  //   protected BinaryExpr(final type.Type type, final Expression lhs, final Expression rhs) {
  //     super(type);
  //     this.lhs = lhs;
  //     this.rhs = rhs;
  //   }
  // }

  /** A constant expression. */
  public static final class ConstExpr extends LeafExpr<inst.Inst<?>>  {
    private ConstExpr(final Type type, final inst.Inst<?> value) { super(type, value); }
    public  ConstExpr(final inst.Inst<?> value) { super(value.type(), value); }
  }






  //////////////////////////////////////////////////////////////////////////////////////////////////
  // module
  private node() {}
}
