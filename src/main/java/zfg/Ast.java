package zfg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
      sb.append('#').append(idx);
      final Type type = type();
      if (type != null) {
        sb.append('[');
        type.toString(sb);
        sb.append(']');
      }
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
  // References
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class VarRef {
    public static enum Site { Static, Member, Local }
    public final Type type;
    public final Site site;
    public final String name;
    public final String owner; // null if the site is Local
    public final int index;    // -1 if the site is not Local

    public VarRef(final Type type, final Site site, final String name, final int index) {
      assert type != null;
      assert site != null && site == Site.Local;
      assert name != null && Names.isLowerSnakeCase(name);
      assert 0x00000000 <= index && index <= 0x0000FFFF;
      this.type = type;
      this.site = site;
      this.name = name;
      this.owner = null;
      this.index = index;
    }

    public VarRef(final Type type, final Site site, final String name, final String owner) {
      assert type != null;
      assert site != null && (site == Site.Static || site == Site.Member);
      assert name != null && Names.isLowerSnakeCase(name);
      assert owner != null;
      // TODO: Validate: https://docs.oracle.com/javase/specs/jls/se16/html/jls-6.html
      assert owner.split("\\.").length >= 1;
      this.type = type;
      this.site = site;
      this.name = name;
      this.owner = owner;
      this.index = -1;
    }
  }

  public static final class FunRef {
    public final Type.Fun type;
    public final String name;
    public final String owner;
    // TODO:
    // INVOKEVIRTUAL - Invoke instance method; dispatch based on class
    // INVOKESTATIC  - Invoke a class (static) method
    // INVOKESPECIAL - direct invocation of instance initialization methods and methods of the current class and its supertypes
    // INVOKEINTERFACE - Invoke interface method

    public FunRef(
      final Type.Fun type,
      final String name,
      final String owner
    ) {
      assert type != null;
      assert name != null && Names.isLowerSnakeCase(name);
      assert owner != null;
      // TODO: Validate: https://docs.oracle.com/javase/specs/jls/se16/html/jls-6.html
      assert owner.split("\\.").length >= 1;
      this.type = type;
      this.name = name;
      this.owner = owner;
    }
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

  public static final class TypeDecl extends AstBase implements Stmt {
    public final Type.Nom type;
    public TypeDecl(final Type.Nom type) {
      assert type != null;
      this.type = type;
    }
    @Override public StringBuilder toString(
      final StringBuilder sb,
      final Map<Object, Integer> seen
    ) {
      if (toSelfString(sb, seen)) return sb;
      sb.append('[');
      type.toString(sb);
      return sb.append(']');
    }
    @Override public Type type() { return null; }
  }
}
