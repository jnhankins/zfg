package zfg;

import java.util.HashMap;
import java.util.Map;

import zfg.insts.Inst;
import zfg.types.FunType;
import zfg.types.Type;

public final class nodes {
  private nodes() {}

  public static sealed interface Node {
    public Type type();
    public StringBuilder toString(final StringBuilder sb, final Map<Object, Integer> seen);
    public StringBuilder toString(final StringBuilder sb);
  }

  private static sealed abstract class BaseNode implements Node {
    @Override public final StringBuilder toString(final StringBuilder sb, final Map<Object, Integer> seen) { return _toString(this, null, null); }
    @Override public final StringBuilder toString(final StringBuilder sb) { return _toString(this, null, null); }
    @Override public final String toString() { return _toString(this, null, null).toString(); }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Error Node
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Do we ever need the partially built tree containing error nodes?

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // References
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class VarRef {
    public static enum Site { Static, Member, Local }
    public final Type type;
    public final Site site;
    public final String name;
    public final String owner; // null if the site is Local
    public final int index; // -1 if the site is not Local

    public VarRef(final Type type, final Site site, final String name, final int index) {
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

    public VarRef(final Type type, final Site site, final String name, final String owner) {
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
  }

  public static final class FunRef {
    public final FunType type;
    public final String name;
    public final String owner;
    // TODO:
    // INVOKEVIRTUAL - Invoke instance method; dispatch based on class
    // INVOKESTATIC  - Invoke a class (static) method
    // INVOKESPECIAL - direct invocation of instance initialization methods and methods of the current class and its supertypes
    // INVOKEINTERFACE - Invoke interface method

    public FunRef(
      final FunType type,
      final String name,
      final String owner
    ) {
      assert type != null;
      assert name != null && names.isLowerSnakeCase(name);
      assert owner != null;
      // TODO: Validate: https://docs.oracle.com/javase/specs/jls/se16/html/jls-6.html
      assert owner.split("\\.").length >= 1;
      this.type = type;
      this.name = name;
      this.owner = owner;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed interface Expr extends Node {}

  public static final class ConstantExpr extends BaseNode implements Expr {
    public final Inst val;
    public ConstantExpr(final Inst val) {
      assert val != null;
      this.val = val;
    }
    @Override public Type type() { return val.type(); }
  }

  public static final class VarLoadExpr extends BaseNode implements Expr {
    public final VarRef var;
    public VarLoadExpr(final VarRef var) {
      assert var != null;
      this.var = var;
    }
    @Override public Type type() { return var.type; }
  }

  public static final class FunCallExpr extends BaseNode implements Expr {
    public final FunRef fun;
    public final Expr[] args;
    public FunCallExpr(final FunRef fun, final Expr[] args) {
      assert fun != null;
      assert args != null;
      assert args.length == fun.type.paramsType.muts.length;
      this.fun = fun;
      this.args = args;
    }
    @Override public Type type() { return fun.type.returnType; }
  }

  public static final class CastExpr extends BaseNode implements Expr {
    public final Type type;
    public final Expr opd;
    public CastExpr(final Type type, final Expr opd) {
      assert type != null;
      assert opd != null;
      this.type = type;
      this.opd = opd;
    }
    @Override public Type type() { return type; }
  }

  public static final class UnaryExpr extends BaseNode implements Expr {
    public static enum Opr { INC, DEC, NEG, NOT, LNT; }
    public final Type type;
    public final Opr  opr;
    public final Expr opd;
    public UnaryExpr(final Type type, final Opr opr, final Expr opd) {
      assert type != null;
      assert opr != null;
      assert opd != null;
      this.type = type;
      this.opr = opr;
      this.opd = opd;
    }
    @Override public Type type() { return type; }
  }

  public static final class BinaryExpr extends BaseNode implements Expr  {
    public static enum Opr { ADD, SUB, MUL, DIV, REM, MOD, SHL, SHR, TWC; }
    public final Type type;
    public final Opr  opr;
    public final Expr lhs;
    public final Expr rhs;
    public BinaryExpr(final Type type, final Opr opr, final Expr lhs, final Expr rhs) {
      assert type != null;
      assert opr != null;
      assert lhs != null;
      assert rhs != null;
      this.type = type;
      this.opr = opr;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    @Override public Type type() { return type; }
  }

  public static final class NaryExpr extends BaseNode implements Expr  {
    public static enum Opr { AND, IOR, XOR, LCJ, LDJ; }
    public final Type type;
    public final Opr  opr;
    public final Expr[] opds;
    public NaryExpr(final Type type, final Opr opr, final Expr[] opds) {
      assert type != null;
      assert opr != null;
      assert opds != null;
      assert opds.length >= 2;
      this.type = type;
      this.opr = opr;
      this.opds = opds;
    }
    @Override public Type type() { return type; }
  }

  public static final class CompareExpr extends BaseNode implements Expr  {
    public static enum Opr { EQL, NEQ, LTN, GTN, LEQ, GEQ; }
    public final Opr[]  oprs;
    public final Expr[] opds;
    public CompareExpr(final Opr[] oprs, final Expr[] opds) {
      assert oprs != null;
      assert oprs.length >= 1;
      assert opds != null;
      assert opds.length == oprs.length + 1;
      this.oprs = oprs;
      this.opds = opds;
    }
    @Override public Type type() { return types.Bit; }
  }

  public static final class AssignExpr extends BaseNode implements Expr {
    public static enum Mode { GET_SET, SET_GET };
    public final VarRef lhs;
    public final Expr rhs;
    public AssignExpr(final Mode mode, final VarRef lhs, final Expr rhs) {
      assert mode != null;
      assert lhs != null;
      assert rhs != null && rhs.type().equals(lhs.type);
      this.lhs = lhs;
      this.rhs = rhs;
    }
    @Override public Type type() { return lhs.type; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statements
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed interface Stmt extends Node {}

  public static final class AssignStmt extends BaseNode implements Stmt {
    public final VarRef lhs;
    public final Expr rhs;
    public AssignStmt(final VarRef lhs, final Expr rhs) {
      assert lhs != null;
      assert rhs != null && rhs.type().equals(lhs.type);
      this.lhs = lhs;
      this.rhs = rhs;
    }
    @Override public Type type() { return types.Unit; }
  }

  public static final class FunCallStmt extends BaseNode implements Stmt {
    public final FunRef fun;
    public final Expr[] args;
    public FunCallStmt(final FunRef fun, final Expr[] args) {
      assert fun != null;
      assert args != null;
      assert args.length == fun.type.paramsType.muts.length;
      this.fun = fun;
      this.args = args;
    }
    @Override public Type type() { return types.Unit; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // toString
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private static StringBuilder _toString(
    final Node node,
    final StringBuilder _sb,
    final Map<Object, Integer> _seen
  ) {
    final StringBuilder sb = _sb == null ? new StringBuilder() : _sb;
    final Map<Object, Integer> seen = _seen == null ? new HashMap<>() : _seen;
    if (seen.get(node) instanceof Integer idx) {
      sb.append(node.getClass().getSimpleName());
      sb.append("#").append(idx);
      sb.append("[").append(node.type()).append("]");
    } else {
      final int idx = seen.size();
      seen.put(node, idx);
      sb.append(node.getClass().getSimpleName());
      sb.append("#").append(idx);
      sb.append("[").append(node.type()).append("]");
      switch (node) {
        case Error error: {
          break;
        }
        case ConstantExpr constExpr: {
          sb.append('(');
          constExpr.val.toString(sb);
          sb.append(')');
          break;
        }
        case CastExpr castExpr: {
          sb.append('(');
          castExpr.opd.toString(sb, seen);
          sb.append(')');
          break;
        }
        case VarLoadExpr variableExpr: {
          sb.append('(');
          sb.append(variableExpr.var.site);
          sb.append(", ");
          if (variableExpr.var.site == VarRef.Site.Local) {
            sb.append(variableExpr.var.name);
            sb.append(", ");
            sb.append(variableExpr.var.index);
          } else {
            sb.append(variableExpr.var.name);
            sb.append(", ");
            sb.append(variableExpr.var.owner);
          }
          sb.append(')');
          break;
        }
        case FunCallExpr funCallExpr: {
          sb.append('(');
          sb.append(funCallExpr.fun.name);
          sb.append(", ");
          sb.append(funCallExpr.fun.owner);
          for (int i = 0; i < funCallExpr.args.length; i++) {
            sb.append(", ");
            _toString(funCallExpr.args[i], _sb, _seen);
          }
          sb.append(')');
          break;
        }
        case UnaryExpr unaryExpr: {
          sb.append('(');
          sb.append(unaryExpr.opr);
          sb.append(", ");
          _toString(unaryExpr.opd, _sb, _seen);
          sb.append(')');
          break;
        }
        case BinaryExpr binaryExpr: {
          sb.append('(');
          sb.append(binaryExpr.opr);
          sb.append(", ");
          _toString(binaryExpr.lhs, _sb, _seen);
          sb.append(", ");
          _toString(binaryExpr.rhs, _sb, _seen);
          sb.append(')');
          break;
        }
        case NaryExpr logicExpr: {
          sb.append('(');
          sb.append(logicExpr.opr);
          for (int i = 0; i < logicExpr.opds.length; i++) {
            sb.append(", ");
            _toString(logicExpr.opds[i], _sb, _seen);
          }
          sb.append(')');
          break;
        }
        case CompareExpr compareExpr: {
          sb.append('(');
          sb.append(compareExpr.opds[0]);
          for (int i = 0; i < compareExpr.opds.length; i++) {
            sb.append(", ");
            sb.append(compareExpr.oprs[i]);
            sb.append(", ");
            _toString(compareExpr.opds[i + 1], _sb, _seen);
          }
          break;
        }
        case AssignExpr assignExpr: {
          sb.append('(');
          sb.append(assignExpr.lhs);
          sb.append(", ");
          _toString(assignExpr.rhs, _sb, _seen);
          sb.append(')');
          break;
        }
        case AssignStmt assignStmt: {
          sb.append('(');
          sb.append(assignStmt.lhs);
          sb.append(", ");
          _toString(assignStmt.rhs, _sb, _seen);
          sb.append(')');
          break;
        }
        case FunCallStmt funCallStmt: {
          sb.append('(');
          sb.append(funCallStmt.fun.name);
          sb.append(", ");
          sb.append(funCallStmt.fun.owner);
          for (int i = 0; i < funCallStmt.args.length; i++) {
            sb.append(", ");
            _toString(funCallStmt.args[i], _sb, _seen);
          }
          sb.append(')');
          break;
        }
      }
      node.toString(sb, seen);
    }
    return sb;
  }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Function Nodes
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public static final class FunctionDeclNode implements Node {
//     public final Type type;
//     public final String name;
//     public final Node[] body;

//     public FunctionDeclNode(final Type type, final String name, final Node[] body) {
//       assert type != null;
//       assert name != null && names.isLowerSnakeCase(name);
//       assert body != null;
//       this.type = type;
//       this.name = name;
//       this.body = body;
//     }

//     @Override public Type type() { return type; }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
//       if (seen.get(this) instanceof Integer idx) {
//         sb.append("FunctionDecl");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//       } else {
//         final int idx = seen.size();
//         seen.put(this, idx);
//         sb.append("FunctionDecl");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//         sb.append('(');
//         sb.append(name);
//         sb.append(", ");
//         sb.append('[');
//         for (int i = 0; i < body.length; i++) {
//           if (i > 0) { sb.append(", "); }
//           body[i].toString(sb, seen);
//         }
//         sb.append(']');
//         sb.append(')');
//       }
//     }
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Statement Nodes
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public static final class ReturnStmtNode implements Node {
//     public final Node expr;

//     public ReturnStmtNode() {
//       this.expr = null;
//     }

//     public ReturnStmtNode(final Node expr) {
//       assert expr != null;
//       this.expr = expr;
//     }

//     @Override public Type type() { return expr == null ? types.Unit : expr.type(); }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
//       if (seen.get(this) instanceof Integer idx) {
//         sb.append("ReturnStmt");
//         sb.append("#").append(idx);
//         sb.append("[").append(type()).append("]");
//       } else {
//         final int idx = seen.size();
//         seen.put(this, idx);
//         sb.append("ReturnStmt");
//         sb.append("#").append(idx);
//         sb.append("[").append(type()).append("]");
//         if (expr != null) {
//           sb.append('(');
//           expr.toString(sb, seen);
//           sb.append(')');
//         }
//       }
//     }
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Expressions
//   //////////////////////////////////////////////////////////////////////////////////////////////////


//   public static final class ConstantNode implements Node {
//     public final insts.Inst value;

//     public ConstantNode(final insts.Inst value) {
//       assert value != null;
//       this.value = value;
//     }

//     @Override public Type type() { return value.type(); }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { sb.append("ConstantExpr("); value.toString(sb); sb.append(")");}
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) { value.toString(sb); }
//   }

//   public static final class VariableNode implements Node {
//     public static enum Site { Static, Member, Local }
//     public final Type type;
//     public final Site site;
//     public final String name;
//     public final String owner; // null if the site is Local
//     public final int index; // -1 if the site is not Local

//     public VariableNode(final Type type, final Site site, final String name, final int index) {
//       assert type != null;
//       assert site != null && site == Site.Local;
//       assert name != null && names.isLowerSnakeCase(name);
//       assert 0x00000000 <= index && index <= 0x0000FFFF;
//       this.type = type;
//       this.site = site;
//       this.name = name;
//       this.owner = null;
//       this.index = index;
//     }

//     public VariableNode(final Type type, final Site site, final String name, final String owner) {
//       assert type != null;
//       assert site != null && (site == Site.Static || site == Site.Member);
//       assert name != null && names.isLowerSnakeCase(name);
//       assert owner != null;
//       // TODO: Validate: https://docs.oracle.com/javase/specs/jls/se16/html/jls-6.html
//       assert owner.split("\\.").length >= 1;
//       this.type = type;
//       this.site = site;
//       this.name = name;
//       this.owner = owner;
//       this.index = -1;
//     }

//     @Override public Type type() { return type; }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
//       if (seen.get(this) instanceof Integer idx) {
//         sb.append("VariableExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//       } else {
//         final int idx = seen.size();
//         seen.put(this, idx);
//         sb.append("VariableExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//         sb.append('(');
//         sb.append(site.name());
//         sb.append(", ");
//         if (site == Site.Local) {
//           sb.append(name);
//           sb.append(", ");
//           sb.append(index);
//         } else {
//           sb.append(name);
//           sb.append(", ");
//           sb.append(owner);
//         }
//         sb.append(')');
//       }

//       final int idx;
//       if (seen.get(this) instanceof Integer i) { idx = i; }
//       else { idx = seen.size(); seen.put(this, idx); }
//       sb.append("VariableExpr");
//       sb.append('#').append(idx);
//       sb.append('[').append(type).append(']');
//       sb.append('(');
//       sb.append(site.name());
//       sb.append(", ");
//       if (site == Site.Local) {
//         sb.append(name);
//         sb.append(", ");
//         sb.append(index);
//       } else {
//         sb.append(name);
//         sb.append(", ");
//         sb.append(owner);
//       }
//       sb.append(')');
//     }
//   }


//   public static final class BinaryExprNode implements Node {
//     public static enum Op {
//       ADD, SUB, MUL, DIV, REM, MOD,
//       SHL, SHR,
//       TWC,
//     }
//     public final Type type;
//     public final Op op;
//     public final Node lhs;
//     public final Node rhs;

//     public BinaryExprNode(final Type type, final Op op, final Node lhs, final Node rhs) {
//       assert type != null;
//       assert op != null;
//       assert lhs != null;
//       assert rhs != null;
//       this.type = type;
//       this.op = op;
//       this.lhs = lhs;
//       this.rhs = rhs;
//     }

//     @Override public Type type() { return type; }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
//       if (seen.get(this) instanceof Integer idx) {
//         sb.append("BinaryExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//       } else {
//         final int idx = seen.size();
//         seen.put(this, idx);
//         sb.append("BinaryExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//         sb.append('(');
//         sb.append(op.name());
//         sb.append(", ");
//         lhs.toString(sb, seen);
//         sb.append(", ");
//         rhs.toString(sb, seen);
//         sb.append(')');
//       }
//     }
//   }

//   public static final class NaryExprNode implements Node {
//     public static enum Op {
//       AND, IOR, XOR,
//       LCJ, LDJ;
//     }
//     public final Type type;
//     public final Op op;
//     public final List<Node> opds;

//     public NaryExprNode(final Type type, final Op op, final List<Node> opds) {
//       assert type != null;
//       assert op != null;
//       assert opds != null && opds.size() >= 2 && opds.stream().allMatch(Objects::nonNull);
//       this.type = type;
//       this.op = op;
//       this.opds = opds;
//     }

//     @Override public Type type() { return type; }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
//       if (seen.get(this) instanceof Integer idx) {
//         sb.append("NaryExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//       } else {
//         final int idx = seen.size();
//         seen.put(this, idx);
//         sb.append("NaryExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//         sb.append('(');
//         sb.append(op.name());
//         final int n = opds.size();
//         for (int i = 0; i < n; i++) {
//           final Node opd = opds.get(i);
//           sb.append(", ");
//           opd.toString(sb, seen);
//         }
//         sb.append(')');
//       }
//     }
//   }

//   public static final class CompareExprNode implements Node {
//     public static enum Op { EQL, NEQ, LTN, GTN, LEQ, GEQ }
//     public final Type type;
//     public final List<Op> ops;
//     public final List<Node> opds;

//     public CompareExprNode(final Type type, final List<Op> ops, final List<Node> opds) {
//       assert type != null;
//       assert ops != null && ops.size() >= 2 && ops.stream().allMatch(Objects::nonNull);
//       assert opds != null && opds.size() == ops.size() + 1 && opds.stream().allMatch(Objects::nonNull);
//       this.type = type;
//       this.ops = ops;
//       this.opds = opds;
//     }

//     @Override public Type type() { return type; }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
//       if (seen.get(this) instanceof Integer idx) {
//         sb.append("NaryExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//       } else {
//         final int idx = seen.size();
//         seen.put(this, idx);
//         sb.append("NaryExpr");
//         sb.append("#").append(idx);
//         sb.append("[").append(type).append("]");
//         sb.append('(');
//         sb.append(op.name());
//         final int n = opds.size();
//         for (int i = 0; i < n; i++) {
//           final Node opd = opds.get(i);
//           sb.append(", ");
//           opd.toString(sb, seen);
//         }
//         sb.append(')');
//       }
//     }
//   }

//   public static final class AssignmentNode implements Node {
//     public static enum Mode { EXPR, STMT }
//     public final Mode mode;
//     public final VariableNode lhs;
//     public final Node rhs;

//     public AssignmentNode(final Mode mode, final VariableNode lhs, final Node rhs) {
//       assert mode != null;
//       assert lhs != null;
//       assert rhs != null && rhs.type().equals(lhs.type());
//       this.mode = mode;
//       this.lhs = lhs;
//       this.rhs = rhs;
//     }

//     @Override public Type type() { return mode == Mode.EXPR ? lhs.type() : types.Unit; }
//     @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
//     @Override public void toString(final StringBuilder sb) { toString(sb, new HashMap<>()); }
//     @Override public void toString(final StringBuilder sb, final Map<Object, Integer> seen) {
//       if (seen.get(this) instanceof Integer idx) {
//         sb.append("Assignment");
//         sb.append("#").append(idx);
//         sb.append("[").append(type()).append("]");
//       } else {
//         final int idx = seen.size();
//         seen.put(this, idx);
//         sb.append("Assignment");
//         sb.append("#").append(idx);
//         sb.append("[").append(type()).append("]");
//         sb.append('(');
//         sb.append(mode.name());
//         sb.append(", ");
//         lhs.toString(sb, seen);
//         sb.append(", ");
//         rhs.toString(sb, seen);
//         sb.append(')');
//       }
//     }

//   }
}
