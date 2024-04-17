package zfg.core.primative;

public final class I32 {
  public static final int MIN_VALUE = 0x80000000; // -2,147,483,648
  public static final int MAX_VALUE = 0x7FFFFFFF; // +2,147,483,647
  public static final boolean isValid(final int value) { return true; }
  public static final void throwIfInvalid(final int value) { }
  public static final I32 of(final int value) { return new I32(value); }

  public final int value;
  public I32(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I32 && this.value == ((I32)that).value); }
  @Override public final String toString() { return String.format("%+di32", value); }
}
