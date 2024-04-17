package zfg.core.primative;

public final class U64 {
  public static final long MIN_VALUE = 0x00000000_00000000L; // 0
  public static final long MAX_VALUE = 0xFFFFFFFF_FFFFFFFFL; // 18,446,744,073,709,551,615
  public static final boolean isValid(final long value) { return true; }
  public static final void throwIfInvalid(final long value) { }
  public static final U64 of(final long value) { return new U64(value); }

  public final long value;
  public U64(final long value) { this.value = value; }

  @Override public final int hashCode() { return (int)(value ^ (value >>> 32)); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U64 && this.value == ((U64)that).value); }
  @Override public final String toString() { return String.format("%su64", Long.toUnsignedString(value, 10)); }
}
