package zfg;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

import org.antlr.v4.runtime.tree.TerminalNode;

import zfg.antlr.ZfgContext;
import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser.ComparisonExpressionContext;


public final class Ast {
  private Ast() {}

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statements
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static interface Stmt {
    ZfgContext ctx();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Declarations
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class TypeDecl implements Stmt, Symbol {
    public final ZfgContext ctx;
    public final Modifier mod;
    public final String name;
    public final Type.Nom type;

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Modifier mod() { return mod; }
    @Override public String name() { return name; }
    @Override public Type type() { return type; }

    public TypeDecl(final ZfgContext ctx, final Modifier mod, final String name) {
      assert ctx != null;
      assert mod != null;
      assert name != null;
      assert Names.isUpperCamelCase(name);
      this.ctx = ctx;
      this.mod = mod;
      this.name = name;
      this.type = Types.nom(name, Types.UNK);
    }
  }

  public static final class FunDecl implements Stmt, Symbol {
    public final ZfgContext ctx;
    public final Modifier mod;
    public final String name;
    private Type type = Types.UNK;

    public FunDecl(final ZfgContext ctx, final Modifier mod, final String name) {
      assert ctx != null;
      assert mod != null;
      assert name != null;
      assert Names.isLowerSnakeCase(name);
      this.ctx = ctx;
      this.mod = mod;
      this.name = name;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Modifier mod() { return mod; }
    @Override public String name() { return name; }
    @Override public Type type() { return type; }
  }

  public static final class VarDecl implements Stmt, Symbol {
    public final ZfgContext ctx;
    public final Modifier mod;
    public final String name;
    private Type type = Types.UNK;

    public VarDecl(final ZfgContext ctx, final Modifier mod, final String name) {
      assert ctx != null;
      assert mod != null;
      assert name != null;
      assert Names.isLowerSnakeCase(name);
      this.ctx = ctx;
      this.mod = mod;
      this.name = name;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Modifier mod() { return mod; }
    @Override public String name() { return name; }
    @Override public Type type() { return type; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed interface Expr {
    ZfgContext ctx();
    Type type();
    Type evaluateType(final ParserErrorEmitter emit);
  }

  public static final class ErrorExpr implements Expr {
    public final ZfgContext ctx;

    public ErrorExpr(final ZfgContext ctx) {
      assert ctx != null;
      this.ctx = ctx;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return Types.ERR; }
    @Override public Type evaluateType(final ParserErrorEmitter emit) { return Types.ERR; }
  }

  public static final class LiteralExpr implements Expr {
    public final ZfgContext ctx;
    public final Inst inst;

    public LiteralExpr(final ZfgContext ctx, final Inst inst) {
      assert ctx != null;
      assert inst != null;
      this.ctx = ctx;
      this.inst = inst;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return inst.type(); }
    @Override public Type evaluateType(final ParserErrorEmitter emit) { return inst.type(); }
  }

  public static final class SymbolExpr implements Expr {
    public final ZfgContext ctx;
    public final Symbol symbol;

    public SymbolExpr(final ZfgContext ctx, final Symbol symbol) {
      assert ctx != null;
      assert symbol != null;
      this.ctx = ctx;
      this.symbol = symbol;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return symbol.type(); }
    @Override public Type evaluateType(final ParserErrorEmitter emit) { return symbol.type(); }
  }

  public static final class FieldAccessExpr implements Expr {
    public final ZfgContext ctx;
    public final Expr opd;
    public final String field;
    private Type objType = null;
    private Type type = Types.UNK;

    public FieldAccessExpr(final ZfgContext ctx, final Expr obj, final String field) {
      assert ctx != null;
      assert obj != null;
      assert field != null;
      assert Names.isLowerSnakeCase(field);
      this.ctx = ctx;
      this.opd = obj;
      this.field = field;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if (objType == (objType = opd.type())) return type;
      if (objType == Types.ERR) return type = Types.ERR;
      if (objType == Types.UNK) return type = Types.UNK;
      type = switch (objType.kind()) {
        case BIT, U08, U16, U32, U64, I08, I16, I32, I64, F32, F64 -> {
          emit.err(ctx, "The primitive type " + objType + " does not have a field '" + field + "'.");
          yield Types.ERR;
        }
        case ARR -> {
          emit.err(ctx, "The array type " + objType + " does not have a field '" + field + "'.");
          yield Types.ERR;
        }
        case TUP -> {
          emit.err(ctx, "The tuple type " + objType + " does not have a field '" + field + "'.");
          yield Types.ERR;
        }
        case REC -> {

        }
        case NOM -> {

        }
      };
    }
  }

  public static final class UnaryExpr implements Expr {
    public static enum Opr implements Op {
      POS(ZfgLexer.ADD),
      NEG(ZfgLexer.SUB),
      NOT(ZfgLexer.NOT),
      LNT(ZfgLexer.LNT);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    public final ZfgContext ctx;
    public final Opr opr;
    public final Expr opd;
    private Type opdType = null;
    private Type type = Types.UNK;

    public UnaryExpr(final ZfgContext ctx, final Opr opr, final Expr opd) {
      assert ctx != null;
      assert opr != null;
      assert opd != null;
      this.ctx = ctx;
      this.opr = opr;
      this.opd = opd;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if (opdType == (opdType = opd.type())) return type;
      if (opdType == Types.ERR) return type = Types.ERR;
      if (opdType == Types.UNK) return type = Types.UNK;
      type = switch (opr) {
        case POS, NEG -> AlgebraicExpr.OPERANDS.contains(opdType.kind()) ? opdType : Types.ERR;
        case NOT -> BitwiseExpr.OPERANDS.contains(opdType.kind()) ? opdType : Types.ERR;
        case LNT -> opdType == Types.BIT ? Types.BIT : Types.ERR;
      };
      if (type == Types.ERR) emit.err(ctx, typeErr(opr, opdType));
      return type;
    }
  }

  public static final class AlgebraicExpr implements Expr {
    public static enum Opr implements Op {
      ADD(ZfgLexer.ADD),
      SUB(ZfgLexer.SUB),
      MUL(ZfgLexer.MUL),
      DIV(ZfgLexer.DIV),
      REM(ZfgLexer.REM),
      MOD(ZfgLexer.MOD);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    private static EnumSet<Type.Kind> OPERANDS = EnumSet.of(
      Type.Kind.U08,
      Type.Kind.U16,
      Type.Kind.U32,
      Type.Kind.U64,
      Type.Kind.I08,
      Type.Kind.I16,
      Type.Kind.I32,
      Type.Kind.I64,
      Type.Kind.F32,
      Type.Kind.F64
    );

    public final ZfgContext ctx;
    public final Opr opr;
    public final Expr lhs;
    public final Expr rhs;
    private Type lhsType = null;
    private Type rhsType = null;
    private Type type = Types.UNK;

    public AlgebraicExpr(final ZfgContext ctx, final Opr opr, final Expr lhs, final Expr rhs) {
      assert ctx != null;
      assert opr != null;
      assert lhs != null;
      assert rhs != null;
      this.ctx = ctx;
      this.opr = opr;
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if ((lhsType == (lhsType = lhs.type())) & (rhsType == (rhsType = rhs.type()))) return type;
      if (lhsType == Types.ERR || rhsType == Types.ERR) return type = Types.ERR;
      if (lhsType == Types.UNK || rhsType == Types.UNK) return type = Types.UNK;
      type = OPERANDS.contains(lhsType.kind()) && OPERANDS.contains(rhsType.kind())
        ? commonType(lhsType, rhsType)
        : Types.ERR;
      if (type == Types.ERR) emit.err(ctx, typeErr(opr, lhsType, rhsType));
      return type;
    }
  }

  public static final class BitwiseExpr implements Expr {
    public static enum Opr implements Op {
      AND(ZfgLexer.AND),
      IOR(ZfgLexer.IOR),
      XOR(ZfgLexer.XOR);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    private static EnumSet<Type.Kind> OPERANDS = EnumSet.of(
      Type.Kind.BIT,
      Type.Kind.U08,
      Type.Kind.U16,
      Type.Kind.U32,
      Type.Kind.U64,
      Type.Kind.I08,
      Type.Kind.I16,
      Type.Kind.I32,
      Type.Kind.I64
    );

    public final ZfgContext ctx;
    public final Opr opr;
    public final Expr[] opds;
    private final Type[] opdTypes;
    private Type type = Types.UNK;

    public BitwiseExpr(final ZfgContext ctx, final Opr opr, final Expr[] opds) {
      assert ctx != null;
      assert opr != null;
      assert opds != null;
      assert opds.length >= 2;
      assert Arrays.stream(opds).allMatch(Objects::nonNull);
      this.ctx = ctx;
      this.opr = opr;
      this.opds = opds;
      this.opdTypes = new Type[opds.length];
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if (checkTypeChanges(opdTypes, opds)) return type;
      for (int i = 0; i < opdTypes.length; i++) if (opdTypes[i] == Types.ERR) return type = Types.ERR;
      for (int i = 0; i < opdTypes.length; i++) if (opdTypes[i] == Types.UNK) return type = Types.UNK;
      type = checkOperands(OPERANDS, opdTypes) ? commonType(opdTypes) : Types.ERR;
      if (type == Types.ERR) emit.err(ctx, typeErr(opr, opdTypes));
      return type;
    }
  }

  public static final class ShiftExpr implements Expr {
    public static enum Opr implements Op {
      SHL(ZfgLexer.SHL),
      SHR(ZfgLexer.SHR);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    private static EnumSet<Type.Kind> OPERANDS = EnumSet.of(
      Type.Kind.U08,
      Type.Kind.U16,
      Type.Kind.U32,
      Type.Kind.U64,
      Type.Kind.I08,
      Type.Kind.I16,
      Type.Kind.I32,
      Type.Kind.I64
    );

    public final ZfgContext ctx;
    public final Opr opr;
    public final Expr lhs;
    public final Expr rhs;
    private Type lhsType = null;
    private Type rhsType = null;
    private Type type = Types.UNK;

    public ShiftExpr(final ZfgContext ctx, final Opr opr, final Expr lhs, final Expr rhs) {
      assert ctx != null;
      assert opr != null;
      assert lhs != null;
      assert rhs != null;
      this.ctx = ctx;
      this.opr = opr;
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if ((lhsType == (lhsType = lhs.type())) & (rhsType == (rhsType = rhs.type()))) return type;
      if (lhsType == Types.ERR || rhsType == Types.ERR) return type = Types.ERR;
      if (lhsType == Types.UNK || rhsType == Types.UNK) return type = Types.UNK;
      type = OPERANDS.contains(lhsType.kind()) && OPERANDS.contains(rhsType.kind())
        ? lhsType
        : Types.ERR;
      if (type == Types.ERR) emit.err(ctx, typeErr(opr, lhsType, rhsType));
      return type;
    }
  }

  public static final class ThreeWayCompExpr implements Expr {
    public static enum Opr implements Op {
      TWC(ZfgLexer.TWC);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    private static EnumSet<Type.Kind> OPERANDS = EnumSet.of(
      Type.Kind.BIT,
      Type.Kind.U08,
      Type.Kind.U16,
      Type.Kind.U32,
      Type.Kind.U64,
      Type.Kind.I08,
      Type.Kind.I16,
      Type.Kind.I32,
      Type.Kind.I64,
      Type.Kind.F32,
      Type.Kind.F64
    );

    public final ZfgContext ctx;
    public final Opr opr;
    public final Expr lhs;
    public final Expr rhs;
    private Type lhsType = null;
    private Type rhsType = null;
    private Type type = Types.UNK;

    public ThreeWayCompExpr(final ZfgContext ctx, final Opr opr, final Expr lhs, final Expr rhs) {
      assert ctx != null;
      assert opr != null;
      assert lhs != null;
      assert rhs != null;
      this.ctx = ctx;
      this.opr = opr;
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if ((lhsType == (lhsType = lhs.type())) & (rhsType == (rhsType = rhs.type()))) return type;
      if (lhsType == Types.ERR || rhsType == Types.ERR) return type = Types.ERR;
      if (lhsType == Types.UNK || rhsType == Types.UNK) return type = Types.UNK;
      type = OPERANDS.contains(lhsType.kind()) && OPERANDS.contains(rhsType.kind())
        ? Types.I08
        : Types.ERR;
      if (type == Types.ERR) emit.err(ctx, typeErr(opr, lhsType, rhsType));
      return type;
    }
  }

  public static final class ComparisonExpr implements Expr {
    public static enum Opr implements Op {
      EQL(ZfgLexer.EQL),
      NEQ(ZfgLexer.NEQ),
      LTN(ZfgLexer.LTN),
      LEQ(ZfgLexer.LEQ),
      GTN(ZfgLexer.GTN),
      GEQ(ZfgLexer.GEQ);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    private static EnumSet<Type.Kind> OPERANDS = EnumSet.of(
      Type.Kind.BIT,
      Type.Kind.U08,
      Type.Kind.U16,
      Type.Kind.U32,
      Type.Kind.U64,
      Type.Kind.I08,
      Type.Kind.I16,
      Type.Kind.I32,
      Type.Kind.I64,
      Type.Kind.F32,
      Type.Kind.F64
    );

    public final ZfgContext ctx;
    public final Opr[] oprs;
    public final Expr[] opds;
    private final Type[] opdTypes;
    private Type type = Types.UNK;

    public ComparisonExpr(final ZfgContext ctx, final Opr[] oprs, final Expr[] opds) {
      assert ctx != null;
      assert oprs != null;
      assert oprs.length >= 1;
      assert Arrays.stream(oprs).allMatch(Objects::nonNull);
      assert opds != null;
      assert opds.length == opds.length + 1;
      assert Arrays.stream(opds).allMatch(Objects::nonNull);
      this.ctx = ctx;
      this.oprs = oprs;
      this.opds = opds;
      this.opdTypes = new Type[opds.length];
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if (checkTypeChanges(opdTypes, opds)) return type;
      for (int i = 0; i < opdTypes.length; i++) if (opdTypes[i] == Types.ERR) return type = Types.ERR;
      for (int i = 0; i < opdTypes.length; i++) if (opdTypes[i] == Types.UNK) return type = Types.UNK;
      type = Types.BIT;
      for (int i = 0; i < oprs.length; i++) {
        final Type lhs = opdTypes[i];
        final Type rhs = opdTypes[i+1];
        if (
          !OPERANDS.contains(lhs.kind()) ||
          !OPERANDS.contains(rhs.kind()) ||
          commonType(lhs, rhs) == Types.ERR
        ) {
          emit.err(oprCtx(i), typeErr(oprs[i], lhs, rhs));
          type = Types.ERR;
        }
      }
      return type;
    }

    private ZfgContext oprCtx(final int i) {
      return switch (ctx) {
        case final ComparisonExpressionContext cmpCtx -> {
          final int j = i * 2;
          final ZfgContext   lhs = (ZfgContext)   cmpCtx.getChild(j);
          final TerminalNode opr = (TerminalNode) cmpCtx.getChild(j+1);
          final ZfgContext   rhs = (ZfgContext)   cmpCtx.getChild(j+2);
          final ZfgContext oprCtx = new ZfgContext();
          oprCtx.setParent(cmpCtx);
          oprCtx.addChild(lhs);
          oprCtx.addChild(opr);
          oprCtx.addChild(rhs);
          oprCtx.start = lhs.start;
          oprCtx.stop = rhs.stop;
          yield oprCtx;
        }
        case null, default -> ctx;
      };
    }
  }

  public static final class LogicalExpr implements Expr {
    public static enum Opr implements Op {
      LCJ(ZfgLexer.LCJ),
      LDJ(ZfgLexer.LDJ);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    public final ZfgContext ctx;
    public final Opr opr;
    public final Expr[] opds;
    private final Type[] opdTypes;
    private Type type = Types.UNK;

    public LogicalExpr(final ZfgContext ctx, final Opr opr, final Expr[] opds) {
      assert ctx != null;
      assert opr != null;
      assert opds != null;
      assert opds.length >= 2;
      assert Arrays.stream(opds).allMatch(Objects::nonNull);
      this.ctx = ctx;
      this.opr = opr;
      this.opds = opds;
      this.opdTypes = new Type[opds.length];
    }

    @Override public ZfgContext ctx() { return ctx; }
    @Override public Type type() { return type; }

    @Override
    public Type evaluateType(final ParserErrorEmitter emit) {
      if (type != Types.UNK) return type;
      if (checkTypeChanges(opdTypes, opds)) return type;
      for (int i = 0; i < opdTypes.length; i++) if (opdTypes[i] == Types.ERR) return type = Types.ERR;
      for (int i = 0; i < opdTypes.length; i++) if (opdTypes[i] == Types.UNK) return type = Types.UNK;
      type = checkOperands(Types.BIT, opdTypes) ? Types.BIT : Types.ERR;
      if (type == Types.ERR) emit.err(ctx, typeErr(opr, opdTypes));
      return type;
    }
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Helpers
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static interface Op {
    int ttype();
    default String symbol() { return ZfgLexer.VOCABULARY.getLiteralName(ttype()); }
  }

  private static boolean checkTypeChanges(final Type[] types, final Expr[] exprs) {
    boolean unchanged = false;
    for (int i = 0; i < types.length; i++) unchanged &= types[i] == (types[i] = exprs[i].type());
    return unchanged;
  }

  private static boolean checkOperands(final Type type, final Type[] types) {
    for (int i = 0; i < types.length; i++) if (types[i] != type) return false;
    return true;
  }

  private static boolean checkOperands(final EnumSet<Type.Kind> set, final Type[] types) {
    for (int i = 0; i < types.length; i++) if (!set.contains(types[i].kind())) return false;
    return true;
  }

  public static Type commonType(final Type lhs, final Type rhs) {
    return lhs == rhs ||
        (rhs == Types.BIT && BitwiseExpr.OPERANDS.contains(lhs.kind())) || rhs.isAssignableTo(lhs)
      ? lhs
      : (lhs == Types.BIT && BitwiseExpr.OPERANDS.contains(rhs.kind())) || lhs.isAssignableTo(rhs)
      ? rhs
      : Types.ERR;
  }

  public static Type commonType(final Type[] types) {
    Type common = types[0];
    for (int i = 1; i < types.length; i++) common = commonType(common, types[i]);
    return common;
  }

  private static String typeErr(final Op op, final Type opd) {
    final StringBuilder sb = new StringBuilder()
      .append("The operator ")
      .append(op.symbol())
      .append(" is undefined for argument of type ");
    opd.appendTo(sb);
    sb.append(".");
    return sb.toString();
  }

  private static String typeErr(final Op op, final Type lhs, final Type rhs) {
    final StringBuilder sb = new StringBuilder()
      .append("The operator ")
      .append(op.symbol())
      .append(" is undefined for arguments of type ");
    lhs.appendTo(sb);
    sb.append(" and ");
    rhs.appendTo(sb);
    sb.append(".");
    return sb.toString();
  }

  private static String typeErr(final Op op, final Type[] opds) {
    return switch (opds.length) {
      case 1 -> typeErr(op, opds[0]);
      case 2 -> typeErr(op, opds[0], opds[1]);
      default -> {
        final StringBuilder sb = new StringBuilder()
          .append("The operator ")
          .append(op.symbol())
          .append(" is undefined for arguments of type ");
        final int last = opds.length - 1;
        for (int i = 0; i < last; i++) {
          opds[i].appendTo(sb);
          sb.append(", ");
        }
        sb.append("and ");
        opds[last].appendTo(sb);
        sb.append(".");
        yield sb.toString();
      }
    };
  }
}
