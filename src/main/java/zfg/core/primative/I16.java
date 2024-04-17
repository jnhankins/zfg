package zfg.core.primative;

public final class I16 {
  public static final int MIN_VALUE = 0xFFFF8000; // -32,768
  public static final int MAX_VALUE = 0x00007FFF; // +32,767
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for i16 [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final I16 of(final int value) { throwIfInvalid(value); return new I16(value); }

  public final int value;
  public I16(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I16 && this.value == ((I16)that).value); }
  @Override public final String toString() { return String.format("%+di16", value); }
}
