package zfg;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

import zfg.antlr.ZfgContext;
import zfg.antlr.ZfgLexer;

public class Ast {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expression
  //////////////////////////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expression
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed abstract class Expr {
    public final ZfgContext ctx;
    public Type type = Types.UNK;

    protected Expr(final ZfgContext ctx) {
      assert ctx != null;
      this.ctx = ctx;
    }

    public abstract Type evaluateType(final Parser.EmitError err);
  }

  public static final class Error extends Expr {
    public Error(final ZfgContext ctx) {
      super(ctx);
      this.type = Types.ERR;
    }

    @Override
    public Type evaluateType(final Parser.EmitError err) {
      return type;
    }
  }

  public static final class Literal extends Expr {
    public final Inst value;

    public Literal(final ZfgContext ctx, final Inst value) {
      super(ctx);
      assert value != null;
      this.value = value;
      this.type = value.type();
    }

    @Override
    public Type evaluateType(final Parser.EmitError err) {
      return type;
    }
  }

  public static sealed abstract class BinaryExpr<Opr extends Op> extends Expr {
    public final Opr opr;
    public final Expr lhs;
    public final Expr rhs;
    private Type lhsType;
    private Type rhsType;

    public BinaryExpr(
      final ZfgContext ctx,
      final Opr opr,
      final Expr lhs,
      final Expr rhs
    ) {
      super(ctx);
      assert opr != null;
      assert lhs != null;
      assert rhs != null;
      this.opr = opr;
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public final Type evaluateType(final Parser.EmitError err) {
      if (!type.hasUnknown()) return type;
      boolean inputChanged = false;
      inputChanged |= lhsType != (lhsType = lhs.type);
      inputChanged |= rhsType != (rhsType = rhs.type);
      if (!inputChanged) return type;
      if (hasError(lhsType, rhsType)) return type = Types.ERR;
      if (hasUnknown(lhsType, rhsType)) return type = Types.UNK;
      type = hasUnacceptable(accepts(), lhsType, rhsType)
        ? Types.ERR
        : evaluateType(lhsType, rhsType);
      if (type == Types.ERR) emitOpArgsTypeError(err, ctx, opr, lhsType, rhsType);
      return type;
    }

    protected abstract EnumSet<Type.Kind> accepts();
    protected abstract Type evaluateType(final Type lhsType, final Type rhsType);
  }

  public static sealed abstract class NaryExpr<Opr extends Op> extends Expr {
    public final Opr opr;
    public final Expr[] opds;
    private Type[] opdTypes;

    public NaryExpr(
      final ZfgContext ctx,
      final Opr opr,
      final Expr[] opds
    ) {
      super(ctx);
      assert opr != null;
      assert opds != null;
      assert opds.length >= 2;
      assert Arrays.stream(opds).allMatch(Objects::nonNull);
      this.opr = opr;
      this.opds = opds;
      this.opdTypes = new Type[opds.length];
    }

    @Override
    public final Type evaluateType(final Parser.EmitError err) {
      if (!type.hasUnknown()) return type;
      boolean inputChanged = false;
      for (int i = 0; i < opds.length; i++)
        inputChanged |= opdTypes[i] != (opdTypes[i] = opds[i].type);
      if (!inputChanged) return type;
      if (hasError(opdTypes)) return type = Types.ERR;
      if (hasUnknown(opdTypes)) return type = Types.UNK;
      type = hasUnacceptable(accepts(), opdTypes)
        ? Types.ERR
        : evaluateType(opdTypes);
      if (type == Types.ERR) emitOpArgsTypeError(err, ctx, opr, opdTypes);
      return type;
    }

    protected abstract EnumSet<Type.Kind> accepts();
    protected abstract Type evaluateType(final Type[] opdTypes);
  }

  public static final class AlgebraicExpr extends BinaryExpr<AlgebraicExpr.Opr> {
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

    public AlgebraicExpr(final ZfgContext ctx, final Opr opr, final Expr lhs, final Expr rhs) {
      super(ctx, opr, lhs, rhs);
    }

    @Override
    protected EnumSet<Type.Kind> accepts() {
      return ALGABRAIC_OPERANDS;
    }

    @Override
    protected Type evaluateType(final Type lhsType, final Type rhsType) {
      return commonType(lhsType, rhsType);
    }
  }

  public static final class ShiftExpr extends BinaryExpr<ShiftExpr.Opr> {
    public static enum Opr implements Op {
      SHL(ZfgLexer.SHL),
      SHR(ZfgLexer.SHR);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    public ShiftExpr(final ZfgContext ctx, final Opr opr, final Expr lhs, final Expr rhs) {
      super(ctx, opr, lhs, rhs);
    }

    @Override
    protected EnumSet<Type.Kind> accepts() {
      return BITWISE_OPERANDS;
    }

    @Override
    protected Type evaluateType(final Type lhsType, final Type rhsType) {
      return lhsType;
    }
  }

  public static final class BitwiseExpr extends NaryExpr<BitwiseExpr.Opr> {
    public static enum Opr implements Op {
      AND(ZfgLexer.AND),
      IOR(ZfgLexer.IOR),
      XOR(ZfgLexer.XOR);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    public BitwiseExpr(final ZfgContext ctx, final Opr opr, final Expr[] opds) {
      super(ctx, opr, opds);
    }

    @Override
    protected EnumSet<Type.Kind> accepts() {
      return BITWISE_OPERANDS;
    }

    @Override
    protected Type evaluateType(final Type[] opdTypes) {
      return commonType(opdTypes);
    }
  }

  public static final class ThreeWayCmpExpr extends BinaryExpr<ThreeWayCmpExpr.Opr> {
    public static enum Opr implements Op {
      TWC(ZfgLexer.TWC);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    public ThreeWayCmpExpr(final ZfgContext ctx, final Opr opr, final Expr lhs, final Expr rhs) {
      super(ctx, opr, lhs, rhs);
    }

    @Override
    protected EnumSet<Type.Kind> accepts() {
      return COMPARISON_OPERANDS;
    }

    @Override
    protected Type evaluateType(final Type lhsType, final Type rhsType) {
      return Types.I08;
    }
  }

  public static final class ComparisonExpr extends Expr {
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

    public final Opr[] oprs;
    public final Expr[] opds;
    private Type[] opdTypes;

    public ComparisonExpr(
      final ZfgContext ctx,
      final Opr[] oprs,
      final Expr[] opds
    ) {
      super(ctx);
      assert oprs != null;
      assert oprs.length >= 1;
      assert Arrays.stream(oprs).allMatch(Objects::nonNull);
      assert opds != null;
      assert opds.length == oprs.length + 1;
      assert Arrays.stream(opds).allMatch(Objects::nonNull);
      this.oprs = oprs;
      this.opds = opds;
      this.opdTypes = new Type[opds.length];
    }

    @Override
    public final Type evaluateType(final Parser.EmitError err) {
      if (!type.hasUnknown()) return type;
      boolean inputChanged = false;
      for (int i = 0; i < opds.length; i++)
        inputChanged |= opdTypes[i] != (opdTypes[i] = opds[i].type);
      if (!inputChanged) return type;
      if (hasError(opdTypes)) return type = Types.ERR;
      if (hasUnknown(opdTypes)) return type = Types.UNK;
      type = Types.BIT;
      for (int i = 0; i < oprs.length; i++) {
        final Opr opr = oprs[i];
        final Type lhsType = opdTypes[i];
        final Type rhsType = opdTypes[i+1];
        if (
          hasUnacceptable(COMPARISON_OPERANDS, lhsType, rhsType) ||
          commonType(opdTypes[i], opdTypes[i+1]) == Types.ERR
        ) {
          type = Types.ERR;
          emitOpArgsTypeError(err, ctx, opr, lhsType, rhsType);
        }
      }
      return type;
    }
  }

  public static final class LogicalExpr extends NaryExpr<LogicalExpr.Opr> {
    public static enum Opr implements Op {
      LCJ(ZfgLexer.LCJ),
      LDJ(ZfgLexer.LDJ);
      final int ttype;
      Opr(final int ttype) { this.ttype = ttype; }
      @Override public int ttype() { return ttype; }
    }

    public LogicalExpr(final ZfgContext ctx, final Opr opr, final Expr[] opds) {
      super(ctx, opr, opds);
    }

    @Override
    protected EnumSet<Type.Kind> accepts() {
      return LOGICAL_OPERANDS;
    }

    @Override
    protected Type evaluateType(final Type[] opdTypes) {
      return Types.BIT;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Helpers
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private static interface Op {
    int ttype();
  }

  private static boolean hasError(final Type typeA, final Type typeB) {
    return typeA.hasError() || typeB.hasError();
  }
  private static boolean hasError(final Type... types) {
    for (int i = 0; i < types.length; i++) if (types[i].hasError()) return true;
    return true;
  }

  private static boolean hasUnknown(final Type typeA, final Type typeB) {
    return typeA.hasUnknown() || typeB.hasUnknown();
  }
  private static boolean hasUnknown(final Type... types) {
    for (int i = 0; i < types.length; i++) if (types[i].hasUnknown()) return true;
    return false;
  }

  private static boolean hasUnacceptable(EnumSet<Type.Kind> accepts, final Type typeA, final Type typeB) {
    return !accepts.contains(typeA.kind()) || !accepts.contains(typeB.kind());
  }
  private static boolean hasUnacceptable(EnumSet<Type.Kind> accepts, final Type... types) {
    for (int i = 0; i < types.length; i++) if (!accepts.contains(types[i].kind())) return true;
    return false;
  }

  private static Type commonType(final Type typeA, final Type typeB) {
    return typeA == typeB || typeB.isAssignableTo(typeA)
      ? typeA
      : typeA.isAssignableTo(typeB)
      ? typeB
      : Types.ERR;
  }

  private static Type commonType(final Type... types) {
    Type common = types[0];
    for (int i = 1; i < types.length; i++) {
      common = commonType(common, types[i]);
      if (common == Types.ERR) return common;
    }
    return common;
  }

  private static void emitOpArgsTypeError(
    final Parser.EmitError err,
    final ZfgContext ctx,
    final Op op,
    final Type... types
  ) {
    final StringBuilder sb = new StringBuilder();
    sb.append("The operator ");
    sb.append(ZfgLexer.VOCABULARY.getLiteralName(op.ttype()));
    final int length = types.length;
    if (length == 1) {
      sb.append(" is undefined for argument type ");
      types[0].appendTo(sb, false);
    } else if (length == 2) {
      sb.append(" is undefined for argument types ");
      types[0].appendTo(sb, false);
      sb.append(" and ");
      types[1].appendTo(sb, false);
    } else {
      sb.append(" is undefined for argument types ");
      final int last = length - 1;
      for (int i = 0; i < last; i++) {
        types[i].appendTo(sb, false);
        sb.append(", ");
      }
      sb.append("and ");
      types[last].appendTo(sb, false);
    }
    sb.append(".");
    err.emit(ctx, sb.toString());
  }

  private static EnumSet<Type.Kind> ALGABRAIC_OPERANDS = EnumSet.of(
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
  private static EnumSet<Type.Kind> BITWISE_OPERANDS = EnumSet.of(
    Type.Kind.U08,
    Type.Kind.U16,
    Type.Kind.U32,
    Type.Kind.U64,
    Type.Kind.I08,
    Type.Kind.I16,
    Type.Kind.I32,
    Type.Kind.I64
  );
  private static EnumSet<Type.Kind> COMPARISON_OPERANDS = EnumSet.of(
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
  private static EnumSet<Type.Kind> LOGICAL_OPERANDS = EnumSet.of(
    Type.Kind.BIT
  );
}
