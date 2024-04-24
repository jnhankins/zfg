package zfg.core;

public final class type {

  /** A data type */
  public static sealed interface Type permits Err,
    Bit, U08, U16, U32, U64, I08, I16, I32, I64, F32, F64
    {}

  public static final class Err implements Type {
    // singleton
    private Err() {}

    // toString, equals, and hashCode
    public String toString() { return "err"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }
  }

  /** Type "bit": 1-bit unsigned integer (boolean, true or false, 0 or 1) */
  public static final class Bit implements Type {
    // singleton
    private Bit() {}

    // toString, equals, and hashCode
    public String toString() { return "bit"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance cache
    private static final inst.Bit[] cache = new inst.Bit[] { new inst.Bit(0), new inst.Bit(1) };

    // instance constructors
    public static inst.Bit ofUnchecked(final int value) { return cache[value]; }
    public static inst.Bit of(final int value) {
      if (value < 0x00000000 || value > 0x00000001)
        throw new IllegalArgumentException(String.format("Invalid bit value: %#08X", value));
      return ofUnchecked(value);
    }
  }

  /** Type "u08": 8-bit unsigned integer */
  public static final class U08 implements Type {
    // singleton
    private U08() {}

    // toString, equals, and hashCode
    public String toString() { return "u08"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance cache
    private static final inst.U08[] cache = new inst.U08[256];
    static { for (int i = 0; i < 256; i++) cache[i] = new inst.U08(i); }

    // instance constructors
    public static inst.U08 ofUnchecked(final int value) { return cache[value]; }
    public static inst.U08 of(final int value) {
      if (value < 0x00000000 || value > 0x000000FF)
        throw new IllegalArgumentException(String.format("Invalid u08 value: %#08X", value));
      return ofUnchecked(value);
    }
  }

  /** Type "u16": 16-bit unsigned integer */
  public static final class U16 implements Type {
    // singleton
    private U16() {}

    // toString, equals, and hashCode
    public String toString() { return "u16"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.U16 ofUnchecked(final int value) { return new inst.U16(value); }
    public static inst.U16 of(final int value) {
      if (value < 0x00000000 || value > 0x0000FFFF)
        throw new IllegalArgumentException(String.format("Invalid u16 value: %#08X", value));
      return ofUnchecked(value);
    }
  }

  /** Type "u32": 32-bit unsigned integer */
  public static final class U32 implements Type {
    // singleton
    private U32() {}

    // toString, equals, and hashCode
    public String toString() { return "u32"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.U32 ofUnchecked(final int value) { return new inst.U32(value); }
    public static inst.U32 of(final int value) { return ofUnchecked(value); }
  }

  /** Type "u64": 64-bit unsigned integer */
  public static final class U64 implements Type {
    // singleton
    private U64() {}

    // toString, equals, and hashCode
    public String toString() { return "u64"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.U64 ofUnchecked(final long value) { return new inst.U64(value); }
    public static inst.U64 of(final long value) { return ofUnchecked(value); }
  }

  /** Type "i08": 8-bit signed integer */
  public static final class I08 implements Type {
    // singleton
    private I08() {}

    // toString, equals, and hashCode
    public String toString() { return "i08"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance cache
    private static final inst.I08[] cache = new inst.I08[256];
    static { for (int i = 0; i < 256; i++) cache[i] = new inst.I08(i-128); }

    // instance constructors
    public static inst.I08 ofUnchecked(final int value) { return cache[value + 128]; }
    public static inst.I08 of(final int value) {
      if (value < -0x00000080 || value > 0x0000007F)
        throw new IllegalArgumentException(String.format("Invalid i08 value: %#08X", value));
      return ofUnchecked(value);
    }
  }

  /** Type "i16": 16-bit signed integer */
  public static final class I16 implements Type {
    // singleton
    private I16() {}

    // toString, equals, and hashCode
    public String toString() { return "i16"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.I16 ofUnchecked(final int value) { return new inst.I16(value); }
    public static inst.I16 of(final int value) {
      if (value < -0x00008000 || value > 0x00007FFF)
        throw new IllegalArgumentException(String.format("Invalid i16 value: %#08X", value));
      return ofUnchecked(value);
    }
  }

  /** Type "i32": 32-bit signed integer */
  public static final class I32 implements Type {
    // singleton
    private I32() {}

    // toString, equals, and hashCode
    public String toString() { return "i32"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.I32 ofUnchecked(final int value) { return new inst.I32(value); }
    public static inst.I32 of(final int value) { return ofUnchecked(value); }
  }

  /** Type "i64": 64-bit signed integer */
  public static final class I64 implements Type {
    // singleton
    private I64() {}

    // toString, equals, and hashCode
    public String toString() { return "i64"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.I64 ofUnchecked(final long value) { return new inst.I64(value); }
    public static inst.I64 of(final long value) { return ofUnchecked(value); }
  }

  /** Type "f32": 32-bit floating-point number */
  public static final class F32 implements Type {
    // singleton
    private F32() {}

    // toString, equals, and hashCode
    public String toString() { return "f32"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.F32 ofUnchecked(final float value) { return new inst.F32(value); }
    public static inst.F32 of(final float value) { return ofUnchecked(value); }
  }

  /** Type "f64": 64-bit floating-point number */
  public static final class F64 implements Type {
    // singleton
    private F64() {}

    // toString, equals, and hashCode
    public String toString() { return "f64"; }
    public boolean equals(final Object that) { return this == that; }
    public int hashCode() { return System.identityHashCode(this); }

    // instance constructors
    public static inst.F64 ofUnchecked(final double value) { return new inst.F64(value); }
    public static inst.F64 of(final double value) { return ofUnchecked(value); }
  }

  // primitive type singletons
  public static final Err err = new Err();
  public static final Bit bit = new Bit();
  public static final U08 u08 = new U08();
  public static final U16 u16 = new U16();
  public static final U32 u32 = new U32();
  public static final U64 u64 = new U64();
  public static final I08 i08 = new I08();
  public static final I16 i16 = new I16();
  public static final I32 i32 = new I32();
  public static final I64 i64 = new I64();
  public static final F32 f32 = new F32();
  public static final F64 f64 = new F64();


  // enumeration of primitive types
  public static enum Kind { BIT, U08, U16, U32, U64, I08, I16, I32, I64, F32, F64 }

  /** Type of function */
  public static final class Fun {
    public final Type   returnType;
    public final Type[] paramTypes;

    public Fun(final Type returnType, final Type... paramTypes) {
      this.returnType = returnType;
      this.paramTypes = paramTypes;
    }

    // toString, equals, and hashCode
    public String toString() {
      final StringBuilder buf = new StringBuilder();
      buf.append("(");
      for (int i = 0; i < paramTypes.length; i++) {
        if (i > 0) sb.append(", ");
        buf.append(paramTypes[i]);
      }
      buf.append(")");
      buf.append(returnType);
      return buf.toString();
    }

  }

  public static Fun fun(final Type returnType, final Type... parameterTypes) {
    return new Fun(returnType, parameterTypes);
  }

  // module
  private type() {}
}
