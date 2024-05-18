package zfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import zfg.antlr.ZfgContext;

public sealed interface Ast {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Module
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class Module implements Ast {
    public final ZfgContext ctx;
    public final String fqn;
    public final Stmt[] stmts;

    public Module(final ZfgContext ctx, final String fqn, final Stmt[] stmts) {
      assert ctx != null;
      assert fqn != null;
      assert Names.isModulePath(fqn);
      assert stmts != null;
      assert Arrays.stream(stmts).noneMatch(Objects::isNull);
      this.ctx = ctx;
      this.fqn = fqn;
      this.stmts = stmts;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expression
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed abstract class Expr implements Ast {
    public final ZfgContext ctx;
    public Type type; // Mutable

    protected Expr(final ZfgContext ctx, final Type type) {
      assert ctx != null;
      assert type != null;
      this.ctx = ctx;
      this.type = type;
    }
  }

  public static final class ErrorExpr extends Expr {
    public ErrorExpr(final ZfgContext ctx) {
      super(ctx, Types.ERR);
    }
  }

  public static final class ConstExpr extends Expr {
    public final Inst val;
    public ConstExpr(final ZfgContext ctx, final Inst val) {
      super(ctx, val.type());
      assert val != null;
      this.val = val;
    }
  }

  public static final class UnaryExpr extends Expr {
    public static enum Opr { POS, NEG, NOT, LNT }
    public final Expr opd;
    public final Opr opr;
    public UnaryExpr(final ZfgContext ctx, final Type type, final Expr opd, final Opr opr) {
      super(ctx, type);
      assert opd != null;
      assert opr != null;
      this.opd = opd;
      this.opr = opr;
    }
  }

  public static final class BinaryExpr extends Expr {
    public static enum Opr { ADD, SUB, MUL, DIV, REM, MOD, SHL, SHR, TWC}
    public final Opr opr;
    public final Expr lhs;
    public final Expr rhs;
    public BinaryExpr(final ZfgContext ctx, final Type type, final Opr opr, final Expr lhs, final Expr rhs) {
      super(ctx, type);
      assert opr != null;
      assert lhs != null;
      assert rhs != null;
      this.opr  = opr;
      this.lhs = lhs;
      this.rhs = rhs;
    }
  }

  public static final class NaryExpr extends Expr {
    public static enum Opr { AND, IOR, XOR, LCJ, LDJ }
    public final Opr opr;
    public final Expr[] opds;
    public NaryExpr(final ZfgContext ctx, final Type type, final Opr opr, final Expr[] opds) {
      super(ctx, type);
      assert opr != null;
      assert opds != null;
      assert opds.length >= 2;
      assert Arrays.stream(opds).noneMatch(Objects::isNull);
      this.opr = opr;
      this.opds = opds;
    }
  }

  public static final class CompareExpr extends Expr {
    public static enum Opr { EQL, NEQ, LTN, LEQ, GTN, GEQ }
    public final Opr[] oprs;
    public final Expr[] opds;
    public CompareExpr(final ZfgContext ctx, final Type type, final Opr[] oprs, final Expr[] opds) {
      super(ctx, type);
      assert oprs != null;
      assert oprs.length >= 1;
      assert Arrays.stream(oprs).noneMatch(Objects::isNull);
      assert opds != null;
      assert opds.length == oprs.length + 1;
      assert Arrays.stream(opds).noneMatch(Objects::isNull);
      this.oprs = oprs;
      this.opds = opds;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // References
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // public static final class VarRef {
  //   public final ZfgContext ctx;
  //   public final String name;
  //   public Type type;

  //   public VarRef(final ZfgContext ctx, final String name, final Type type) {
  //     assert ctx != null;
  //     assert name != null;
  //     assert Names.isLowerSnakeCase(name);
  //     this.ctx = ctx;
  //     this.name = name;
  //     this.type = type;
  //   }
  // }

  // public static final class FunRef {
  //   public final ZfgContext ctx;
  //   public final String name;
  //   public Type type;

  //   public FunRef(final ZfgContext ctx, final String name) {
  //     assert ctx != null;
  //     assert name != null;
  //     assert Names.isLowerSnakeCase(name);
  //     this.ctx = ctx;
  //     this.name = name;
  //   }
  // }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statement
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed interface Stmt extends Ast {}

  public static enum Modifier { PUB, LET, MUT }

  public static final class TypeDecl implements Stmt {
    public final ZfgContext ctx;
    public final Modifier mod;
    public final Type.Nom type;
    public TypeDecl(final ZfgContext ctx, final Modifier mod, final Type.Nom type) {
      assert ctx != null;
      assert mod != null;
      assert type != null;
      this.ctx = ctx;
      this.mod = mod;
      this.type = type;
    }

    public void bindType(final Type type) {
      assert type != null;
      assert type != Types.ERR;
      this.type.bind(type);
    }
  }

  public static final class FunDecl implements Stmt {
    public final ZfgContext ctx;
    public final Modifier mod;
    public final String name;
    public Type type;
    public Stmt[] stmts;
    public List<ReturnStmt> returnStmts;

    public FunDecl(final ZfgContext ctx, final Modifier mod, final String name, final Type type) {
      assert ctx != null;
      assert mod != null;
      assert name != null;
      assert Names.isLowerSnakeCase(name);
      assert type instanceof Type.Unk || type instanceof Type.Fun;
      this.ctx = ctx;
      this.mod = mod;
      this.name = name;
      this.type = type;
    }

    public void bindType(final Type type) {
      assert type != null;
      assert type instanceof Type.Unk || type instanceof Type.Fun;
      this.type = type;
    }

    public void bindStmts(final Stmt[] stmts) {
      assert this.stmts == null;
      assert stmts != null;
      assert Arrays.stream(stmts).noneMatch(Objects::isNull);
      this.stmts = stmts;
    }

    public void addReturnStmt(final ReturnStmt stmt) {
      assert stmt != null;
      if (returnStmts == null) returnStmts = new ArrayList<>();
      returnStmts.add(stmt);
    }
  }

  public static final class VarDecl implements Stmt {
    public final ZfgContext ctx;
    public final Modifier mod;
    public final String name;
    public Type type;
    public Expr expr;

    public VarDecl(final ZfgContext ctx, final Modifier mod, final String name, final Type type) {
      assert ctx != null;
      assert mod != null;
      assert name != null;
      assert Names.isLowerSnakeCase(name);
      assert type != Types.ERR;
      this.ctx = ctx;
      this.mod = mod;
      this.name = name;
      this.type = Types.UNK;
    }

    public void bindType(final Type type) {
      assert type != null;
      assert type != Types.ERR;
      this.type = type;
    }

    public void bindExpr(final Expr expr) {
      assert expr != null;
      this.expr = expr;
    }
  }

  public static final class ReturnStmt implements Stmt {
    public final ZfgContext ctx;
    public final FunDecl fun;
    public final Expr expr;
    public ReturnStmt(final ZfgContext ctx, final FunDecl fun) {
      assert ctx != null;
      assert fun != null;
      this.ctx = ctx;
      this.fun = fun;
      this.expr = null;
    }
    public ReturnStmt(final ZfgContext ctx, final FunDecl fun, final Expr expr) {
      assert ctx != null;
      assert fun != null;
      assert expr != null;
      this.ctx = ctx;
      this.fun = fun;
      this.expr = expr;
    }
  }
}
