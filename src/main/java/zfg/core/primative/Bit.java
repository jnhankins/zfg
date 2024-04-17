package zfg.core.primative;

public final class Bit {
  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0x00000001; // 1
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for bit [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final Bit of(final int value) { throwIfInvalid(value); return value == 0 ? FALSE : TRUE; }
  public static final Bit FALSE = of(0);
  public static final Bit TRUE = of(1);

  public final int value;
  public Bit(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof Bit && this.value == ((Bit)that).value); }
  @Override public final String toString() { return String.format("%sbit", Integer.toUnsignedString(value, 10)); }
}
