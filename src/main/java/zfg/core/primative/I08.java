package zfg.core.primative;

public final class I08 {
  public static final int MIN_VALUE = 0xFFFFFF80; // -128
  public static final int MAX_VALUE = 0x0000007F; // +127
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for i08 [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final I08 of(final int value) { throwIfInvalid(value); return CACHE[value - MIN_VALUE]; }
  private static final I08[] CACHE = new I08[MAX_VALUE - MIN_VALUE + 1];
  static { for (int i = 0; i < CACHE.length; i++) CACHE[i] = new I08(i + MIN_VALUE); }

  public final int value;
  public I08(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I08 && this.value == ((I08)that).value); }
  @Override public final String toString() { return String.format("%+di08", value); }
}
