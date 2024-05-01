package zfg;

import java.util.Set;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class nodes {
  private nodes() {}

  public static interface Node {
    public types.Type type();
    @Override public String toString();
    public void toString(final StringBuilder sb);
    public void toString(final StringBuilder sb, final Set<Object> seen);
  }
  public static interface MvWriter {
    public void write(final MethodVisitor mv);
  }

  public static final Error error = new Error();
  public static final class Error implements Node {
    private Error() {}
    @Override public types.Type type() { return types.Err; }
    @Override public String toString() { return "Error"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class Const implements Node, MvWriter {
    public final insts.Inst value;

    public Const(final insts.Inst value) {
      assert value != null;
      this.value = value;
    }

    @Override public types.Type type() { return value.type(); }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { sb.append("Const("); value.toString(sb); sb.append(")");}
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { value.toString(sb); }

    @Override public void write(final MethodVisitor mv) {
      switch (value) {
        case insts.BitInst inst -> visitI(mv, inst.value);
        case insts.U08Inst inst -> visitI(mv, inst.value);
        case insts.U16Inst inst -> visitI(mv, inst.value);
        case insts.U32Inst inst -> visitI(mv, inst.value);
        case insts.U64Inst inst -> visitL(mv, inst.value);
        case insts.I08Inst inst -> visitI(mv, inst.value);
        case insts.I16Inst inst -> visitI(mv, inst.value);
        case insts.I32Inst inst -> visitI(mv, inst.value);
        case insts.I64Inst inst -> visitL(mv, inst.value);
        case insts.F32Inst inst -> visitF(mv, inst.value);
        case insts.F64Inst inst -> visitD(mv, inst.value);
        // TODO arr, tup, rec, fun, etc.
        default -> throw new AssertionError();
      };
    }
    private static void visitI(final MethodVisitor mv, final int i32) {
      if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + i32); return; }
      if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, i32); return; }
      if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, i32); return; }
      mv.visitLdcInsn((Integer) i32);
    }
    private static void visitL(final MethodVisitor mv, final long i64) {
      if (i64 == 0x0000000000000000L) { mv.visitInsn(Opcodes.LCONST_0); return; }
      if (i64 == 0x0000000000000001L) { mv.visitInsn(Opcodes.LCONST_1); return; }
      if (0xFFFFFFFFFFFFFF80L <= i64 && i64 <= 0x000000000000007FL) { mv.visitIntInsn(Opcodes.BIPUSH, (int)i64); mv.visitInsn(Opcodes.I2L); return; }
      if (0xFFFFFFFFFFFF8000L <= i64 && i64 <= 0x0000000000007FFFL) { mv.visitIntInsn(Opcodes.SIPUSH, (int)i64); mv.visitInsn(Opcodes.I2L); return; }
      mv.visitLdcInsn((Long) i64);
    }
    private static void visitF(final MethodVisitor mv, final float f32) {
      if (f32 == 0.0f) { mv.visitInsn(Opcodes.FCONST_0); return; }
      if (f32 == 1.0f) { mv.visitInsn(Opcodes.FCONST_1); return; }
      if (f32 == 2.0f) { mv.visitInsn(Opcodes.FCONST_2); return; }
      final int i32 = (int) f32;
      if (i32 == f32) {
        if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + i32); mv.visitInsn(Opcodes.I2F); return; }
        if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, i32); mv.visitInsn(Opcodes.I2F); return; }
        if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, i32); mv.visitInsn(Opcodes.I2F); return; }
      }
      mv.visitLdcInsn((Float) f32);
    }
    private static void visitD(final MethodVisitor mv, final double f64) {
      if (f64 == 0.0) { mv.visitInsn(Opcodes.DCONST_0); return; }
      if (f64 == 1.0) { mv.visitInsn(Opcodes.DCONST_1); return; }
      final int i32 = (int) f64;
      if (i32 == f64) {
        if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + i32); mv.visitInsn(Opcodes.I2D); return; }
        if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, i32); mv.visitInsn(Opcodes.I2D); return; }
        if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, i32); mv.visitInsn(Opcodes.I2D); return; }
      }
      mv.visitLdcInsn((Double) f64);
    }
  }

  public static final class InfixOp implements Node {
    public static enum Op {
      Add, Sub, Mul, Div, Rem, Mod,
      And, Ior, Xor,
      Shl, Shr,
      Cmp,
      Eql, Neq, Ltn, Gtn, Leq, Geq,
      Lcj, Ldj;
    }
    public final types.Type type;
    public final Node lhs;
    public final Node rhs;
    public final Op op;

    public InfixOp(final types.Type type, final Node lhs, final Node rhs, final Op op) {
      assert type != null;
      assert lhs != null;
      assert rhs != null;
      assert op != null;
      this.type = type;
      this.lhs = lhs;
      this.rhs = rhs;
      this.op = op;
    }

    @Override public types.Type type() { return type; }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    // @Override public void toString(final StringBuilder sb) { sb.append("InfixOp("); lhs.toString(sb); sb.append(' ').append(op).append(' '); rhs.toString(sb); sb.append(')'); }
    // @Override public void toString(final StringBuilder sb, final Set<Object> seen) { left.toString(sb, seen); sb.append(' ').append(op).append(' '); right.toString(sb, seen); }

    @Override public void write(final MethodVisitor mv) {
      switch (type.kind()) {
        case types.Kind.I32: {
          switch (op) {
            case Op.Add: { lhs.write(mv); rhs.write(mv); mv.visitInsn(Opcodes.IADD); break; }
            case Op.Sub: { lhs.write(mv); rhs.write(mv); mv.visitInsn(Opcodes.ISUB); break; }
            case Op.Mul: { lhs.write(mv); rhs.write(mv); mv.visitInsn(Opcodes.IMUL); break; }
            case Op.Div: { lhs.write(mv); rhs.write(mv); mv.visitInsn(Opcodes.IDIV); break; }
            case Op.Rem: { lhs.write(mv); rhs.write(mv); mv.visitInsn(Opcodes.IREM); break; }
            // :: ((a % b) + a) % b
            // [a, b] swap
            // [b, a] dup2
            // [b, a, b, a] swap
            // [b, a, a, b] irem
            // [b, a, (a % b)] iadd
            // [b, (a % b + a)] swap
            // [(a % b + a), b] irem

            // :: int c = a % b; if (c < 0) c += b;
            // 0: [a, b] dub_x1
            // 1: [b, a, b] irem
            // 2: [b, (a % b)] dup
            // 3: [b, (a % b), (a % b)] ifge +N
            // 4: [b, (a % b)] iadd
            // 5: [b, (a % b + b)] swap


            // [a, b] dup2
            // [a, b, a, b] irem
            // [a, b, (a % b)]


            // [a, b] dup_x1
            // [b, a, b]
            // [b, a, a, b] irem
            // [b, a, (a % b)]     OR     [b, (a % b), a]        iadd -> [b, (a % b + a)]
            // [b, (a % b + a)] swap -> [(a % b + a), b]
            // [(a % b + a), b)] irem -> [((a % b + a) % b)]


            // a b;
            // a b %
            // a b % a +
            // a b % a + b %
            case Op.Mod: { lhs.write(mv); rhs.write(mv); mv.visitInsn(Opcodes.IREM); mv.visitInsn(Opcodes.DUP); mv.bbreak; } // (a % b + a) % b //
          }
          break;
        }
      }

      switch (op) {
        case Add: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.IADD); mv.visitIntInsn(Opcodes.SIPUSH, 255); mv.visitInsn(Opcodes.IAND);}
          case types.U16Type t -> { mv.visitInsn(Opcodes.IADD); mv.visitInsn(Opcodes.I2C); }
          case types.U32Type t -> { mv.visitInsn(Opcodes.IADD); }
          case types.U64Type t -> { mv.visitInsn(Opcodes.LADD); }
          case types.I08Type t -> { mv.visitInsn(Opcodes.IADD); mv.visitInsn(Opcodes.I2B); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.IADD); mv.visitInsn(Opcodes.I2S); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.IADD); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LADD); }
          case types.F32Type t -> { mv.visitInsn(Opcodes.FADD); }
          case types.F64Type t -> { mv.visitInsn(Opcodes.DADD); }
          default -> throw new AssertionError();
        } break;
        case Sub: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.ISUB); mv.visitIntInsn(Opcodes.SIPUSH, 255); mv.visitInsn(Opcodes.IAND); }
          case types.U16Type t -> { mv.visitInsn(Opcodes.ISUB); mv.visitInsn(Opcodes.I2C); }
          case types.U32Type t -> { mv.visitInsn(Opcodes.ISUB); }
          case types.U64Type t -> { mv.visitInsn(Opcodes.LSUB); }
          case types.I08Type t -> { mv.visitInsn(Opcodes.ISUB); mv.visitInsn(Opcodes.I2B); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.ISUB); mv.visitInsn(Opcodes.I2S); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.ISUB); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LSUB); }
          case types.F32Type t -> { mv.visitInsn(Opcodes.FSUB); }
          case types.F64Type t -> { mv.visitInsn(Opcodes.DSUB); }
          default -> throw new AssertionError();
        } break;
        case Mul: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.IMUL); mv.visitIntInsn(Opcodes.SIPUSH, 255); mv.visitInsn(Opcodes.IAND); }
          case types.U16Type t -> { mv.visitInsn(Opcodes.IMUL); mv.visitInsn(Opcodes.I2C); }
          case types.U32Type t -> { mv.visitInsn(Opcodes.IMUL); }
          case types.U64Type t -> { mv.visitInsn(Opcodes.LMUL); }
          case types.I08Type t -> { mv.visitInsn(Opcodes.IMUL); mv.visitInsn(Opcodes.I2B); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.IMUL); mv.visitInsn(Opcodes.I2S); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.IMUL); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LMUL); }
          case types.F32Type t -> { mv.visitInsn(Opcodes.FMUL); }
          case types.F64Type t -> { mv.visitInsn(Opcodes.DMUL); }
          default -> throw new AssertionError();
        } break;
        case Div: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.IDIV); mv.visitIntInsn(Opcodes.SIPUSH, 255); mv.visitInsn(Opcodes.IAND); }
          case types.U16Type t -> { mv.visitInsn(Opcodes.IDIV); mv.visitInsn(Opcodes.I2C); }
          case types.U32Type t -> throw new UnsupportedOperationException("TODO"); // TODO
          case types.U64Type t -> throw new UnsupportedOperationException("TODO"); // TODO
          case types.I08Type t -> { mv.visitInsn(Opcodes.IDIV); mv.visitInsn(Opcodes.I2B); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.IDIV); mv.visitInsn(Opcodes.I2S); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.IDIV); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LDIV); }
          case types.F32Type t -> { mv.visitInsn(Opcodes.FDIV); }
          case types.F64Type t -> { mv.visitInsn(Opcodes.DDIV); }
          default -> throw new AssertionError();
        } break;
        case Rem: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.IREM); mv.visitIntInsn(Opcodes.SIPUSH, 255); mv.visitInsn(Opcodes.IAND); }
          case types.U16Type t -> { mv.visitInsn(Opcodes.IREM); mv.visitInsn(Opcodes.I2C); }
          case types.U32Type t -> throw new UnsupportedOperationException("TODO"); // TODO
          case types.U64Type t -> throw new UnsupportedOperationException("TODO"); // TODO
          case types.I08Type t -> { mv.visitInsn(Opcodes.IREM); mv.visitInsn(Opcodes.I2B); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.IREM); mv.visitInsn(Opcodes.I2S); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.IREM); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LREM); }
          case types.F32Type t -> { mv.visitInsn(Opcodes.FREM); }
          case types.F64Type t -> { mv.visitInsn(Opcodes.DREM); }
          default -> throw new AssertionError();
        } break;
        case Mod: throw new UnsupportedOperationException("TODO"); // TODO
        case And: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.IAND); }
          case types.U16Type t -> { mv.visitInsn(Opcodes.IAND); }
          case types.U32Type t -> { mv.visitInsn(Opcodes.IAND); }
          case types.U64Type t -> { mv.visitInsn(Opcodes.LAND); }
          case types.I08Type t -> { mv.visitInsn(Opcodes.IAND); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.IAND); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.IAND); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LAND); }
          default -> throw new AssertionError();
        } break;
        case Ior: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.IOR); }
          case types.U16Type t -> { mv.visitInsn(Opcodes.IOR); }
          case types.U32Type t -> { mv.visitInsn(Opcodes.IOR); }
          case types.U64Type t -> { mv.visitInsn(Opcodes.LOR); }
          case types.I08Type t -> { mv.visitInsn(Opcodes.IOR); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.IOR); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.IOR); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LOR); }
          default -> throw new AssertionError();
        } break;
        case Xor: switch (type) {
          case types.U08Type t -> { mv.visitInsn(Opcodes.IXOR); }
          case types.U16Type t -> { mv.visitInsn(Opcodes.IXOR); }
          case types.U32Type t -> { mv.visitInsn(Opcodes.IXOR); }
          case types.U64Type t -> { mv.visitInsn(Opcodes.LXOR); }
          case types.I08Type t -> { mv.visitInsn(Opcodes.IXOR); }
          case types.I16Type t -> { mv.visitInsn(Opcodes.IXOR); }
          case types.I32Type t -> { mv.visitInsn(Opcodes.IXOR); }
          case types.I64Type t -> { mv.visitInsn(Opcodes.LXOR); }
          default -> throw new AssertionError();
        } break;
      }
    }
  }
}
