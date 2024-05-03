package zfg;

import java.util.HashMap;
import java.util.Map;

import zfg.types.Type;

public final class nodes {
  private nodes() {}

  public static interface Node {
    public Type type();
    @Override public String toString();
    public void toString(final StringBuilder sb);
    public void toString(final StringBuilder sb, final Map<Object, Integer> seen);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Error Node
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final Error error = new Error();
  public static final class Error implements Node {
    private Error() {}
    @Override public Type type() { return types.Err; }
    @Override public String toString() { return "Error"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) { sb.append(this); }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Function Nodes
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class FunctionDeclNode implements Node {
    public final Type type;
    public final String name;
    public final Node[] body;

    public FunctionDeclNode(final Type type, final String name, final Node[] body) {
      assert type != null;
      assert name != null && names.isLowerSnakeCase(name);
      assert body != null;
      this.type = type;
      this.name = name;
      this.body = body;
    }

    @Override public Type type() { return type; }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
      if (seen.get(this) instanceof Integer idx) {
        sb.append("FunctionDecl");
        sb.append("#").append(idx);
        sb.append("[").append(type).append("]");
      } else {
        final int idx = seen.size();
        seen.put(this, idx);
        sb.append("FunctionDecl");
        sb.append("#").append(idx);
        sb.append("[").append(type).append("]");
        sb.append('(');
        sb.append(name);
        sb.append(", ");
        sb.append('[');
        for (int i = 0; i < body.length; i++) {
          if (i > 0) { sb.append(", "); }
          body[i].toString(sb, seen);
        }
        sb.append(']');
        sb.append(')');
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statement Nodes
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class ReturnStmtNode implements Node {
    public final Node expr;

    public ReturnStmtNode() {
      this.expr = null;
    }

    public ReturnStmtNode(final Node expr) {
      assert expr != null;
      this.expr = expr;
    }

    @Override public Type type() { return expr == null ? types.Unit : expr.type(); }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
      if (seen.get(this) instanceof Integer idx) {
        sb.append("ReturnStmt");
        sb.append("#").append(idx);
        sb.append("[").append(type()).append("]");
      } else {
        final int idx = seen.size();
        seen.put(this, idx);
        sb.append("ReturnStmt");
        sb.append("#").append(idx);
        sb.append("[").append(type()).append("]");
        if (expr != null) {
          sb.append('(');
          expr.toString(sb, seen);
          sb.append(')');
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////


  public static final class ConstantExprNode implements Node {
    public final insts.Inst value;

    public ConstantExprNode(final insts.Inst value) {
      assert value != null;
      this.value = value;
    }

    @Override public Type type() { return value.type(); }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { sb.append("ConstantExpr("); value.toString(sb); sb.append(")");}
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) { value.toString(sb); }
  }

  public static final class VariableExprNode implements Node {
    public static enum Site { Static, Member, Local }
    public final Type type;
    public final Site site;
    public final String name;
    public final String owner; // null if the site is Local
    public final int index; // -1 if the site is not Local

    public VariableExprNode(final Type type, final Site site, final String name, final int index) {
      assert type != null;
      assert site != null && site == Site.Local;
      assert name != null && names.isLowerSnakeCase(name);
      assert 0x00000000 <= index && index <= 0x0000FFFF;
      this.type = type;
      this.site = site;
      this.name = name;
      this.owner = null;
      this.index = index;
    }

    public VariableExprNode(final Type type, final Site site, final String name, final String owner) {
      assert type != null;
      assert site != null && (site == Site.Static || site == Site.Member);
      assert name != null && names.isLowerSnakeCase(name);
      assert owner != null;
      // TODO: Validate: https://docs.oracle.com/javase/specs/jls/se16/html/jls-6.html
      assert owner.split("\\.").length >= 1;
      this.type = type;
      this.site = site;
      this.name = name;
      this.owner = owner;
      this.index = -1;
    }

    @Override public Type type() { return type; }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
      if (seen.get(this) instanceof Integer idx) {
        sb.append("VariableExpr");
        sb.append("#").append(idx);
        sb.append("[").append(type).append("]");
      } else {
        final int idx = seen.size();
        seen.put(this, idx);
        sb.append("VariableExpr");
        sb.append("#").append(idx);
        sb.append("[").append(type).append("]");
        sb.append('(');
        sb.append(site.name());
        sb.append(", ");
        if (site == Site.Local) {
          sb.append(name);
          sb.append(", ");
          sb.append(index);
        } else {
          sb.append(name);
          sb.append(", ");
          sb.append(owner);
        }
        sb.append(')');
      }

      final int idx;
      if (seen.get(this) instanceof Integer i) { idx = i; }
      else { idx = seen.size(); seen.put(this, idx); }
      sb.append("VariableExpr");
      sb.append('#').append(idx);
      sb.append('[').append(type).append(']');
      sb.append('(');
      sb.append(site.name());
      sb.append(", ");
      if (site == Site.Local) {
        sb.append(name);
        sb.append(", ");
        sb.append(index);
      } else {
        sb.append(name);
        sb.append(", ");
        sb.append(owner);
      }
      sb.append(')');
    }
  }

  public static final class BinaryExprNode implements Node {
    public static enum Op {
      Add, Sub, Mul, Div, Rem, Mod,
      And, Ior, Xor,
      Shl, Shr,
      Cmp,
      Eql, Neq, Ltn, Gtn, Leq, Geq,
      Lcj, Ldj;
    }
    public final Type type;
    public final Node lhs;
    public final Node rhs;
    public final Op op;

    public BinaryExprNode(final Type type, final Node lhs, final Node rhs, final Op op) {
      assert type != null;
      assert lhs != null;
      assert rhs != null;
      assert op != null;
      this.type = type;
      this.lhs = lhs;
      this.rhs = rhs;
      this.op = op;
    }

    @Override public Type type() { return type; }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
    @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
      if (seen.get(this) instanceof Integer idx) {
        sb.append("BinaryExpr");
        sb.append("#").append(idx);
        sb.append("[").append(type).append("]");
      } else {
        final int idx = seen.size();
        seen.put(this, idx);
        sb.append("BinaryExpr");
        sb.append("#").append(idx);
        sb.append("[").append(type).append("]");
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
