package zfg.core.primative;

public final class U08 {
  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0x000000FF; // 255
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for u08 [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final U08 of(final int value) { throwIfInvalid(value); return CACHE[value + MIN_VALUE]; }
  private static final U08[] CACHE = new U08[MAX_VALUE - MIN_VALUE + 1];
  static { for (int i = MIN_VALUE; i <= MAX_VALUE; i++) CACHE[i] = new U08(i); }

  public final int value;
  public U08(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U08 && this.value == ((U08)that).value); }
  @Override public final String toString() { return String.format("%su08", Integer.toUnsignedString(value, 10)); }
}
