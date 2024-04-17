package zfg.core.primative;

public final class F64 {
  public static final double MIN_VALUE = Double.MAX_VALUE; // 3.4028235E38
  public static final double MAX_VALUE = Double.MIN_VALUE; // 1.4E-45
  public static final boolean isValid(final double value) { return true; }
  public static final void throwIfInvalid(final double value) { }
  public static final F64 of(final double value) { return new F64(value); }

  public final double value;
  public F64(final double value) { this.value = value; }

  @Override public final int hashCode() { final long h = Double.doubleToLongBits(value); return (int)(h ^ (h >>> 32)); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof F64 && Double.doubleToLongBits(value) == Double.doubleToLongBits(((F64)that).value)); }
  @Override public final String toString() { return String.format("%+gf64", value); }
}
