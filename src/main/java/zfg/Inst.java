package zfg;

import java.util.Objects;

public sealed interface Inst {
  @Override public int hashCode();
  @Override public boolean equals(final Object obj);
  @Override public String toString();
  public default String toString(final boolean pretty) { return toString(); }
  public default void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(this); }
  public Type type();

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Virtual Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Cannot be instantiated

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Primitive Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed abstract class Primitive implements Inst {
    @Override public abstract int hashCode();
    @Override public abstract boolean equals(final Object obj);
    @Override public abstract String toString();
  }

  public static final class Bit extends Primitive {
    public final int value;
    Bit(final int value) {
      assert 0 <= value && value <= 1;
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(Bit.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof Bit that && that.value == value); }
    @Override public String toString() { return value + "bit"; }
    @Override public Type type() { return Types.BIT; }
  }

  public static final class U08 implements Inst {
    public final int value;
    U08(final int value) {
      assert 0 <= value && value <= 0xFF;
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(U08.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof U08 that && that.value == value); }
    @Override public String toString() { return value + "u08"; }
    @Override public Type type() { return Types.U08; }
  }

  public static final class U16 implements Inst {
    public final int value;
    U16(final int value) {
      assert 0 <= value && value <= 0xFFFF;
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(U16.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof U16 that && that.value == value); }
    @Override public String toString() { return value + "u16"; }
    @Override public Type type() { return Types.U16; }
  }

  public static final class U32 implements Inst {
    public final int value;
    U32(final int value) {
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(U32.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof U32 that && that.value == value); }
    @Override public String toString() { return value + "u32"; }
    @Override public Type type() { return Types.U32; }
  }

  public static final class U64 implements Inst {
    public final long value;
    U64(final long value) {
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(U64.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof U64 that && that.value == value); }
    @Override public String toString() { return value + "u64"; }
    @Override public Type type() { return Types.U64; }
  }

  public static final class I08 implements Inst {
    public final int value;
    I08(final int value) {
      assert Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE;
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(I08.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof I08 that && that.value == value); }
    @Override public String toString() { return value + "i08"; }
    @Override public Type type() { return Types.I08; }
  }

  public static final class I16 implements Inst {
    public final int value;
    I16(final int value) {
      assert Short.MIN_VALUE <= value && value <= Short.MAX_VALUE;
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(I16.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof I16 that && that.value == value); }
    @Override public String toString() { return value + "i16"; }
    @Override public Type type() { return Types.I16; }
  }

  public static final class I32 implements Inst {
    public final int value;
    I32(final int value) {
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(I32.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof I32 that && that.value == value); }
    @Override public String toString() { return value + "i32"; }
    @Override public Type type() { return Types.I32; }
  }

  public static final class I64 implements Inst {
    public final long value;
    I64(final long value) {
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(I64.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof I64 that && that.value == value); }
    @Override public String toString() { return value + "i64"; }
    @Override public Type type() { return Types.I64; }
  }

  public static final class F32 implements Inst {
    public final float value;
    F32(final float value) {
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(F32.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof F32 that && that.value == value); }
    @Override public String toString() { return value + "f32"; }
    @Override public Type type() { return Types.F32; }
  }

  public static final class F64 implements Inst {
    public final double value;
    F64(final double value) {
      this.value = value;
    }
    @Override public int hashCode() { return Objects.hash(F64.class, value); }
    @Override public boolean equals(final Object obj) { return this == obj || (obj instanceof F64 that && that.value == value); }
    @Override public String toString() { return value + "f64"; }
    @Override public Type type() { return Types.F64; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Compound Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // TODO
}
