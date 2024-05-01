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
      final types.Type type = value.type();
      switch (value) {
        case insts.BitInst bit -> {
          final int value = bit.value;
          assert 0 <= value && value <= 1;
          switch (value) {
            case 0 -> mv.visitInsn(Opcodes.ICONST_0);
            case 1 -> mv.visitInsn(Opcodes.ICONST_1);
            default -> throw new AssertionError();
          }
        }
        case insts.U08Inst u08 -> {
          final int value = u08.value;
          assert 0 <= value && value <= 0xFF;
          switch (value) {
            case 0 -> mv.visitInsn(Opcodes.ICONST_0);
            case 1 -> mv.visitInsn(Opcodes.ICONST_1);
            case 2 -> mv.visitInsn(Opcodes.ICONST_2);
            case 3 -> mv.visitInsn(Opcodes.ICONST_3);
            case 4 -> mv.visitInsn(Opcodes.ICONST_4);
            case 5 -> mv.visitInsn(Opcodes.ICONST_5);
            default -> {
              if   (u08.value <= 0x7F) mv.visitIntInsn(Opcodes.BIPUSH, u08.value);
              else                     mv.visitIntInsn(Opcodes.SIPUSH, u08.value);
            }
          }
        }
        case insts.U16Inst u16 -> {
          final int value = u16.value;
          assert 0 <= value && value <= 0xFFFF;
          switch (u16.value) {
            case 0 -> mv.visitInsn(Opcodes.ICONST_0);
            case 1 -> mv.visitInsn(Opcodes.ICONST_1);
            case 2 -> mv.visitInsn(Opcodes.ICONST_2);
            case 3 -> mv.visitInsn(Opcodes.ICONST_3);
            case 4 -> mv.visitInsn(Opcodes.ICONST_4);
            case 5 -> mv.visitInsn(Opcodes.ICONST_5);
            default -> {
              if      (u16.value <= Byte.MAX_VALUE)  mv.visitIntInsn(Opcodes.BIPUSH, u16.value);
              else if (u16.value <= Short.MAX_VALUE) mv.visitIntInsn(Opcodes.SIPUSH, u16.value);
              else {
                mv.visitIntInsn(Opcodes.SIPUSH, u16.value); // sign extended
                mv.visitInsn(Opcodes.I2C);                  // trunctate to 16 bits with hi 0s
              }
            }
          }
        }
        case inst.U32Inst u32 -> {
          final int value = u32.value;
          switch (value) {
            case 0 -> mv.visitInsn(Opcodes.ICONST_0);
            case 1 -> mv.visitInsn(Opcodes.ICONST_1);
            case 2 -> mv.visitInsn(Opcodes.ICONST_2);
            case 3 -> mv.visitInsn(Opcodes.ICONST_3);
            case 4 -> mv.visitInsn(Opcodes.ICONST_4);
            case 5 -> mv.visitInsn(Opcodes.ICONST_5);
            default -> mv.visitLdcInsn(u32.value);
          }
        }
        default -> throw new AssertionError();
      }
    }

    private void writeUxx(final MethodVisitor mv, final int u) {
      // u08: [   0, 255], u16: [     0, 65535]
      // i08: [-128, 127], i16: [-32768, 32767]
      // handles -65,535 through 65,535 (inclusive) in most 2 instructions and 4 bytes
      //               -1 .. 5                : iconst_n
      if (0xFFFFFFFF <= u && u <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + u); return; }
      //             -128 .. 127              : bipush <n>
      if (0xFFFFFF80 <= u && u <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, u); return; }
      //          -32,768 .. 32,767           : sipush <b> <b>
      if (0xFFFF8000 <= u && u <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, u); return; }
      //           32,769 .. 65,535           : sipush <b> <b>; i2c
      if (0x00008000 <= u && u <= 0x0000FFFF) { mv.visitIntInsn(Opcodes.SIPUSH, u); mv.visitInsn(Opcodes.I2C); return; }
      //          -65,535 .. -32,769           : sipush <b> <b>; neg
      if (0xFFFF8001 <= u && u <= 0xFFFF7FFF) {
        final int v = -u;
        if      (u <= 0x0005) { mv.visitInsn(Opcodes.ICONST_0 + v); return; }
        else if (u <= 0x007F) { mv.visitIntInsn(Opcodes.BIPUSH, v); return; }
        else                    mv.visitIntInsn(Opcodes.SIPUSH, v);
        mv.visitInsn(Opcodes.INEG);
      }
      



    }
  }
}

// 0x00000000 = iconst_0
// 0x00000001 = iconst_1
// 0x00000002 = iconst_2
// 0x00000003 = iconst_3
// 0x00000004 = iconst_4
// 0x00000005 = iconst_5
// 0x00000006 = bipush 06
// ...
// 0x0000007F = bipush 7F
// 0x00000080 = sipush 00 80
// ...
// 0x00007FFF = sipush 7F FF
// 0x00008000 = sipush 80 00; i2c
// ...
// 0x0000FFFF = sipush FF FF; i2c

// 0x00010000 = iconst_1; bipush 10; ishl
// 0x00010001 = iconst_1; bipush 10; ishl; iconst_1; ior
// 0x00010002 = iconst_1; bipush 10; ishl; iconst_2; ior
// 0x00010003 = iconst_1; bipush 10; ishl; iconst_3; ior
// 0x00010004 = iconst_1; bipush 10; ishl; iconst_4; ior
// 0x00010005 = iconst_1; bipush 10; ishl; iconst_5; ior
// 0x00010006 = iconst_1; bipush 10; ishl; bipush 06; ior
// ...
// 0x0001007F = iconst_1; bipush 10; ishl; bipush 7F; ior
// 0x00010080 = iconst_1; bipush 10; ishl; sipush 00 80; ior
// ...
// 0x00017FFF = iconst_1; bipush 10; ishl; sipush 7F FF; ior
// 0x00018000 = iconst_1; bipush 10; ishl; sipush 80 00; ior


// 0xFFFF8000 = sipush 80 00
// ...
// 0xFFFFFF7F = sipush FF 7F

// 0xFFFFFF80 = bipush 80
// ..
// 0xFFFFFFFE = bipush FE

// 0xFFFFFFFF = iconst_m1

