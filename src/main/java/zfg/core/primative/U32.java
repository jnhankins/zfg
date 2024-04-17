package zfg.core.primative;

public final class U32 {
  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0xFFFFFFFF; // 4,294,967,295
  public static final boolean isValid(final int value) { return true; }
  public static final void throwIfInvalid(final int value) { }
  public static final U32 of(final int value) { return new U32(value); }

  public final int value;
  public U32(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U32 && this.value == ((U32)that).value); }
  @Override public final String toString() { return String.format("%su32", Integer.toUnsignedString(value, 10)); }
}
