package zfg;

import zfg.types.Type;
import java.util.Objects;
import java.util.Set;

public final class insts {
  private insts() {}

  public static interface Inst {
    public Type type();
    @Override public int hashCode();
    @Override public boolean equals(final Object obj);
    @Override public String toString();
    public void toString(final StringBuilder sb);
    public void toString(final StringBuilder sb, final Set<Object> seen);
  }

  public static final class BitInst implements Inst {
    public final int value;
    private BitInst(final int value) {
      assert 0 <= value && value <= 1;
      this.value = value;
    }
    public Type type() { return types.U08; }
    @Override public int hashCode() { return Objects.hash(BitInst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof BitInst that && value == that.value; }
    @Override public String toString() { return value + "bit"; }
    @Override public void toString(final StringBuilder sb) { sb.append(value).append("bit"); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { toString(sb); }
  }

  public static final class U08Inst implements Inst {
    public final int value;
    private U08Inst(final int value) {
      assert 0 <= value && value <= 0xFF;
      this.value = value;
    }
    public Type type() { return types.U08; }
    @Override public int hashCode() { return Objects.hash(U08Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof U08Inst that && value == that.value; }
    @Override public String toString() { return value + "u08"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class U16Inst implements Inst {
    public final int value;
    private U16Inst(final int value) {
      assert 0 <= value && value <= 0xFFFF;
      this.value = value;
    }
    public Type type() { return types.U16; }
    @Override public int hashCode() { return Objects.hash(U16Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof U16Inst that && value == that.value; }
    @Override public String toString() { return value + "u16"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class U32Inst implements Inst {
    public final int value;
    private U32Inst(final int value) {
      this.value = value;
    }
    public Type type() { return types.U32; }
    @Override public int hashCode() { return Objects.hash(U32Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof U32Inst that && value == that.value; }
    @Override public String toString() { return Integer.toUnsignedString(value) + "u32"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class U64Inst implements Inst {
    public final long value;
    private U64Inst(final long value) {
      this.value = value;
    }
    public Type type() { return types.U64; }
    @Override public int hashCode() { return Objects.hash(U64Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof U64Inst that && value == that.value; }
    @Override public String toString() { return Long.toUnsignedString(value) + "u64"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class I08Inst implements Inst {
    public final int value;
    private I08Inst(final int value) {
      assert Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE;
      this.value = value;
    }
    public Type type() { return types.I08; }
    @Override public int hashCode() { return Objects.hash(I08Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof I08Inst that && value == that.value; }
    @Override public String toString() { return value + "i08"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class I16Inst implements Inst {
    public final int value;
    private I16Inst(final int value) {
      assert Short.MIN_VALUE <= value && value <= Short.MAX_VALUE;
      this.value = value;
    }
    public Type type() { return types.I16; }
    @Override public int hashCode() { return Objects.hash(I16Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof I16Inst that && value == that.value; }
    @Override public String toString() { return value + "i16"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class I32Inst implements Inst {
    public final int value;
    private I32Inst(final int value) {
      this.value = value;
    }
    public Type type() { return types.I32; }
    @Override public int hashCode() { return Objects.hash(I32Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof I32Inst that && value == that.value; }
    @Override public String toString() { return value + "i32"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class I64Inst implements Inst {
    public final long value;
    private I64Inst(final long value) {
      this.value = value;
    }
    public Type type() { return types.I64; }
    @Override public int hashCode() { return Objects.hash(I64Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof I64Inst that && value == that.value; }
    @Override public String toString() { return value + "i64"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class F32Inst implements Inst {
    public final float value;
    private F32Inst(final float value) {
      this.value = value;
    }
    public Type type() { return types.F32; }
    @Override public int hashCode() { return Objects.hash(F32Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof F32Inst that && value == that.value; }
    @Override public String toString() { return value + "f32"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final class F64Inst implements Inst {
    public final double value;
    private F64Inst(final double value) {
      this.value = value;
    }
    public Type type() { return types.F64; }
    @Override public int hashCode() { return Objects.hash(F64Inst.class, value); }
    @Override public boolean equals(final Object obj) { return obj instanceof F64Inst that && value == that.value; }
    @Override public String toString() { return value + "f64"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Object> seen) { sb.append(this); }
  }

  public static final BitInst bit(int    value) { return new BitInst(value); }
  public static final U08Inst u08(int    value) { return new U08Inst(value); }
  public static final U16Inst u16(int    value) { return new U16Inst(value); }
  public static final U32Inst u32(int    value) { return new U32Inst(value); }
  public static final U64Inst u64(long   value) { return new U64Inst(value); }
  public static final I08Inst i08(int    value) { return new I08Inst(value); }
  public static final I16Inst i16(int    value) { return new I16Inst(value); }
  public static final I32Inst i32(int    value) { return new I32Inst(value); }
  public static final I64Inst i64(long   value) { return new I64Inst(value); }
  public static final F32Inst f32(float  value) { return new F32Inst(value); }
  public static final F64Inst f64(double value) { return new F64Inst(value); }
}
