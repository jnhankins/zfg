package zfg.core.primative;

public final class U16 {
  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0x0000FFFF; // 65,535
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for u16 [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final U16 of(final int value) { throwIfInvalid(value); return new U16(value); }

  public final int value;
  public U16(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U16 && this.value == ((U16)that).value); }
  @Override public final String toString() { return String.format("%su16", Integer.toUnsignedString(value, 10)); }
}
