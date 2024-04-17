package zfg.core.primative;

public final class I64 {
  public static final long MIN_VALUE = 0x80000000_00000000L; // -9,223,372,036,854,775,808
  public static final long MAX_VALUE = 0x7FFFFFFF_FFFFFFF0L; // +9,223,372,036,854,775,807
  public static final boolean isValid(final long value) { return true; }
  public static final void throwIfInvalid(final long value) { }
  public static final I64 of(final long value) { return new I64(value); }

  public final long value;
  public I64(final long value) { this.value = value; }

  @Override public final int hashCode() { return (int)(value ^ (value >>> 32)); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I64 && this.value == ((I64)that).value); }
  @Override public final String toString() { return String.format("%+di64", value); }
}
