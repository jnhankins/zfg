package zfg.core;

import java.util.Set;

import zfg.core.types.Type;

public final class insts {
  private insts() {}

  public static sealed interface Inst permits
      // Virtual instances
      UnkInst, ErrInst,
      // Primitive instances
      BitInst,
      U08Inst, U16Inst, U32Inst, U64Inst, I08Inst, I16Inst, I32Inst, I64Inst, F32Inst, F64Inst,
      // Reference instances
      ArrInst, RecInst, FunInst,
      // Special instances
      UnitInst, NameInst
  {
    public Type type();
    
    public default StringBuilder toString(final StringBuilder buf, final Set<NameInst> seen) {
      return buf.append(this);
    }
  }

  // Virtual instances
  public static final class UnkInst implements Inst {
    private UnkInst() {}
    @Override public Type type() { return types.unk; }
    @Override public String toString() { return "unk"; }
  }
  public static final class ErrInst implements Inst {
    private ErrInst() {}
    @Override public Type type() { return types.err; }
    @Override public String toString() { return "err"; }
  }

  // Primitive instances
  public static final class BitInst implements Inst {
    public final int value;
    private BitInst(final int value) {
      assert value == 0 || value == 1;
      this.value = value;
    }
    @Override public Type type() { return types.bit; }
    @Override public String toString() { return value + "bit"; }
  }
  public static final class U08Inst implements Inst {
    public final int value;
    private U08Inst(final int value) {
      assert 0 <= value && value <= 0xff;
      this.value = value;
    }
    @Override public Type type() { return types.u08; }
    @Override public String toString() { return value + "u08"; }
  }
  public static final class U16Inst implements Inst {
    public final int value;
    private U16Inst(final int value) {
      assert 0 <= value && value <= 0xffff;
      this.value = value;
    }
    @Override public Type type() { return types.u16; }
    @Override public String toString() { return value + "u16"; }
  }
  public static final class U32Inst implements Inst {
    public final int value;
    private U32Inst(final int value) { this.value = value; }
    @Override public Type type() { return types.u32; }
    @Override public String toString() { return Integer.toUnsignedString(value) + "u32"; }
  }
  public static final class U64Inst implements Inst {
    public final long value;
    private U64Inst(final long value) { this.value = value; }
    @Override public Type type() { return types.u64; }
    @Override public String toString() { return Long.toUnsignedString(value) + "u64"; }
  }
  public static final class I08Inst implements Inst {
    public final int value;
    private I08Inst(final int value) {
      assert -0x80 <= value && value <= 0x7f;
      this.value = value;
    }
    @Override public Type type() { return types.i08; }
    @Override public String toString() { return value + "i08"; }
  }
  public static final class I16Inst implements Inst {
    public final int value;
    private I16Inst(final int value) {
      assert -0x8000 <= value && value <= 0x7fff;
      this.value = value;
    }
    @Override public Type type() { return types.i16; }
    @Override public String toString() { return value + "i16"; }
  }
  public static final class I32Inst implements Inst {
    public final int value;
    private I32Inst(final int value) { this.value = value; }
    @Override public Type type() { return types.i32; }
    @Override public String toString() { return value + "i32"; }
  }
  public static final class I64Inst implements Inst {
    public final long value;
    private I64Inst(final long value) { this.value = value; }
    @Override public Type type() { return types.i64; }
    @Override public String toString() { return value + "i64"; }
  }
  public static final class F32Inst implements Inst {
    public final float value;
    private F32Inst(final float value) { this.value = value; }
    @Override public Type type() { return types.f32; }
    @Override public String toString() { return value + "f32"; }
  }
  public static final class F64Inst implements Inst {
    public final double value;
    private F64Inst(final double value) { this.value = value; }
    @Override public Type type() { return types.f64; }
    @Override public String toString() { return value + "f64"; }
  }

  // Virtual instances
  public static final UnkInst unk = new UnkInst();
  public static final ErrInst err = new ErrInst();

  // Primitive instances
  private static final BitInst bit0 = new BitInst(0);
  private static final BitInst bit1 = new BitInst(1);
  private static final U08Inst[] u08s = new U08Inst[256];
  private static final I08Inst[] i08s = new I08Inst[256];
  public static BitInst bit(final int value) {
    assert value == 0 || value == 1;
    return value == 0 ? bit0 : bit1;
  }
  public static U08Inst u08(final int value) {
    final U08Inst cached = u08s[value];
    return cached != null ? cached : (u08s[value] = new U08Inst(value));
  }
  public static U16Inst u16(final int value) { return new U16Inst(value); }
  public static U32Inst u32(final int value) { return new U32Inst(value); }
  public static U64Inst u64(final long value) { return new U64Inst(value); }
  public static I08Inst i08(final int value) {
    final int key = value + 128;
    final I08Inst cached = i08s[key];
    return cached != null ? cached : (i08s[key] = new I08Inst(value));
  }
  public static I16Inst i16(final int value) { return new I16Inst(value); }
  public static I32Inst i32(final int value) { return new I32Inst(value); }
  public static I64Inst i64(final long value) { return new I64Inst(value); }
  public static F32Inst f32(final float value) { return new F32Inst(value); }
  public static F64Inst f64(final double value) { return new F64Inst(value); }


}
