package zfg;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import zfg.nodes.Node;

public final class CodeGen {
  public CodeGen() {}

  private ClassVisitor cv;
  private MethodVisitor mv;

  public void visit(final Node node) {
    node.accept(this);
  }

  public void pushI(final int i32) {
    mv.visitMaxs(i32, i32);
    assert mv != null;
    if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + i32); return; }
    if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, i32); return; }
    if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, i32); return; }
    mv.visitLdcInsn((Integer) i32);
  }

  public void pushL(final long i64) {
    assert mv != null;
    if (i64 == 0x0000000000000000L) { mv.visitInsn(Opcodes.LCONST_0); return; }
    if (i64 == 0x0000000000000001L) { mv.visitInsn(Opcodes.LCONST_1); return; }
    if (0xFFFFFFFFFFFFFF80L <= i64 && i64 <= 0x000000000000007FL) { mv.visitIntInsn(Opcodes.BIPUSH, (int)i64); mv.visitInsn(Opcodes.I2L); return; }
    if (0xFFFFFFFFFFFF8000L <= i64 && i64 <= 0x0000000000007FFFL) { mv.visitIntInsn(Opcodes.SIPUSH, (int)i64); mv.visitInsn(Opcodes.I2L); return; }
    mv.visitLdcInsn((Long) i64);
  }

  public void pushF(final float f32) {
    assert mv != null;
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

  public void pushD(final double f64) {
    assert mv != null;
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

  public static enum OpCode {
    Add, Sub, Mul, Div, Rem, Mod,
    And, Ior, Xor,
    Shl, Shr,
    Cmp,
    Eql, Neq, Ltn, Gtn, Leq, Geq,
    Lcj, Ldj;
  }
  public void visitInfixOp(final types.Kind kind, final Node lhs, final Node rhs, final OpCode op) {
    switch (op) {
      case OpCode.Add: switch (kind) {
        case U08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IADD); mv.visitIntInsn(Opcodes.SIPUSH, FF); mv.visitInsn(Opcodes.IAND); break;
        case U16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IADD); mv.visitInsn(Opcodes.I2C); break;
        case U32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IADD); break;
        case U64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LADD); break;
        case I08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IADD); mv.visitInsn(Opcodes.I2B); break;
        case I16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IADD); mv.visitInsn(Opcodes.I2S); break;
        case I32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IADD); break;
        case I64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LADD); break;
        case F32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.FADD); break;
        case F64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.DADD); break;
        default: throw new AssertionError();
      } break;
      case OpCode.Sub: switch (kind) {
        case U08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.ISUB); mv.visitIntInsn(Opcodes.SIPUSH, FF); mv.visitInsn(Opcodes.IAND); break;
        case U16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.ISUB); mv.visitInsn(Opcodes.I2C); break;
        case U32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.ISUB); break;
        case U64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LSUB); break;
        case I08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.ISUB); mv.visitInsn(Opcodes.I2B); break;
        case I16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.ISUB); mv.visitInsn(Opcodes.I2S); break;
        case I32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.ISUB); break;
        case I64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LSUB); break;
        case F32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.FSUB); break;
        case F64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.DSUB); break;
        default: throw new AssertionError();
      } break;
      case OpCode.Mul: switch (kind) {
        case U08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IMUL); mv.visitIntInsn(Opcodes.SIPUSH, FF); mv.visitInsn(Opcodes.IAND); break;
        case U16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IMUL); mv.visitInsn(Opcodes.I2C); break;
        case U32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IMUL); break;
        case U64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LMUL); break;
        case I08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IMUL); mv.visitInsn(Opcodes.I2B); break;
        case I16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IMUL); mv.visitInsn(Opcodes.I2S); break;
        case I32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IMUL); break;
        case I64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LMUL); break;
        case F32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.FMUL); break;
        case F64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.DMUL); break;
        default: throw new AssertionError();
      } break;
      case OpCode.Div: switch (kind) {
        case U08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IDIV); break;
        case U16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IDIV); break;
        case U32: visit(lhs); visit(rhs); mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "divideUnsigned", "(II)I", false); break;
        case U64: visit(lhs); visit(rhs); mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long",    "divideUnsigned", "(JJ)J", false); break;
        case I08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IDIV); break;
        case I16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IDIV); break;
        case I32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IDIV); break;
        case I64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LDIV); break;
        case F32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.FDIV); break;
        case F64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.DDIV); break;
        default: throw new AssertionError();
      } break;
      case OpCode.Rem: switch (kind) {
        case U08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IREM); break;
        case U16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IREM); break;
        case U32: visit(lhs); visit(rhs); mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "remainderUnsigned", "(II)I", false); break;
        case U64: visit(lhs); visit(rhs); mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long",    "remainderUnsigned", "(JJ)J", false); break;
        case I08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IREM); break;
        case I16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IREM); break;
        case I32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IREM); break;
        case I64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.LREM); break;
        case F32: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.FREM); break;
        case F64: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.DREM); break;
        default: throw new AssertionError();
      } break;
      case OpCode.Mod: switch (kind) {
        case U08: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IREM); break;
        case U16: visit(lhs); visit(rhs); mv.visitInsn(Opcodes.IREM); break;
        case U32: visit(lhs); visit(rhs); mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "remainderUnsigned", "(II)I", false); break;
        case U64: visit(lhs); visit(rhs); mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long",    "remainderUnsigned", "(JJ)J", false); break;
        case I08, I16, I32: // a mod b = (a % b + b) % b
          visit(lhs);                    // Stack: ..., a
          visit(rhs);                    // Stack: ..., a, b
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., b, b, a, b
          mv.visitInsn(Opcodes.IREM);    // Stack: ..., b, b, (a % b)
          mv.visitInsn(Opcodes.IADD);    // Stack: ..., b, (a % b + b)
          mv.visitInsn(Opcodes.SWAP);    // Stack: ..., (a % b + b), b
          mv.visitInsn(Opcodes.IREM);    // Stack: ..., ((a % b + b) % b)
          break;
        case I64: // a mod b = (a % b + b) % b
          visit(lhs);                    // Stack: ..., a
          visit(rhs);                    // Stack: ..., a, b
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., b, a, b
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., b, b, a, b
          mv.visitInsn(Opcodes.LREM);    // Stack: ..., b, b, (a % b)
          mv.visitInsn(Opcodes.LADD);    // Stack: ..., b, (a % b + b)
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., (a % b + b), b, (a % b + b)
          mv.visitInsn(Opcodes.POP2);    // Stack: ..., (a % b + b), b
          mv.visitInsn(Opcodes.LREM);    // Stack: ..., ((a % b + b) % b)
          break;
        case F32: // a mod b = (a % b + b) % b
          visit(lhs);                    // Stack: ..., a
          visit(rhs);                    // Stack: ..., a, b
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., b, b, a, b
          mv.visitInsn(Opcodes.FREM);    // Stack: ..., b, b, (a % b)
          mv.visitInsn(Opcodes.FADD);    // Stack: ..., b, (a % b + b)
          mv.visitInsn(Opcodes.SWAP);    // Stack: ..., (a % b + b), b
          mv.visitInsn(Opcodes.FREM);    // Stack: ..., ((a % b + b) % b)
          break;
        case F64: // a mod b = (a % b + b) % b
          visit(lhs);                    // Stack: ..., a
          visit(rhs);                    // Stack: ..., a, b
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., b, a, b
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., b, b, a, b
          mv.visitInsn(Opcodes.DREM);    // Stack: ..., b, b, (a % b)
          mv.visitInsn(Opcodes.DADD);    // Stack: ..., b, (a % b + b)
          mv.visitInsn(Opcodes.DUP2_X2); // Stack: ..., (a % b + b), b, (a % b + b)
          mv.visitInsn(Opcodes.POP2);    // Stack: ..., (a % b + b), b
          mv.visitInsn(Opcodes.DREM);    // Stack: ..., ((a % b + b) % b)
          break;
        default: throw new AssertionError();
      } break;
      default: throw new AssertionError();
    }
  }

  // Masks
  private static final Integer FF        = 0xFF;
  private static final Long    FFFFFFFFL = 0xFFFFFFFFL;

  // Intrinsics:
  // java/lang/Short
  //   reverseBytes (S)S
  // java/lang/Char
  //   reverseBytes (C)C
  // java/lang/Integer
  //   toString (I)Ljava/lang/String;
  //   compareUnsigned (II)I
  //   divideUnsigned (II)I
  //   remainderUnsigned (II)I
  //   numberOfLeadingZeros (I)I
  //   numberOfTrailingZeros (I)I
  //   bitCount (I)I
  //   reverse (I)I
  //   compress (II)I
  //   expand (II)I
  //   reverseBytes (I)I
  // java/lang/Long
  //   compareUnsigned (JJ)I
  //   divideUnsigned (JJ)J
  //   remainderUnsigned (JJ)J
  //   numberOfLeadingZeros (J)I
  //   numberOfTrailingZeros (J)I
  //   bitCount (J)I
  //   reverse (J)J
  //   compress (JJ)J
  //   expand (JJ)J
  //   reverseBytes (J)J
  // java/lang/Float
  //   isInfinite (F)Z
  //   isFinite (F)Z
  //   floatToIntBits (F)I
  //   floatToRawIntBits (F)I
  //   intBitsToFloat (I)F
  //   float16ToFloat (S)F
  //   floatToFloat16 (F)S
  // java/lang/Double
  //   isInfinite (D)Z
  //   isFinite (D)Z
  //   doubleToLongBits (D)J
  //   doubleToRawLongBits (D)J
  //   longBitsToDouble (J)D
  // java/lang/Math
  //   sin (D)D
  //   cos (D)D
  //   tan (D)D
  //   exp (D)D
  //   log (D)D
  //   log10 (D)D
  //   ceil (D)D
  //   floor (D)D
  //   rint (D)D
  //   atan2 (DD)D
  //   pow (DD)D
  //   round (F)I
  //   round (D)J
  //   addExact (II)I
  //   addExact (JJ)J
  //   subtractExact (II)I
  //   subtractExact (JJ)J
  //   multiplyExact (II)I
  //   multiplyExact (JJ)J
  //   incrementExact (I)I
  //   incrementExact (J)J
  //   decrementExact (I)I
  //   decrementExact (J)J
  //   negateExact (I)I
  //   negateExact (J)J
  //   multiplyHigh (JJ)J
  //   unsignedMultiplyHigh (JJ)J
  //   abs (I)I
  //   abs (J)J
  //   abs (F)F
  //   abs (D)D
  //   max (II)I
  //   max (JJ)J
  //   max (FF)F
  //   max (DD)D
  //   min (II)I
  //   min (JJ)J
  //   min (FF)F
  //   min (DD)D
  //   fma (DDD)D
  //   fma (FFF)F
  //   signum (D)D
  //   signum (F)F
  //   copySign (DD)D
  //   copySign (FF)F


}
