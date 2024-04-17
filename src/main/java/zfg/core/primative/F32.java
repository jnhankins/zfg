package zfg.core.primative;

public final class F32 {
  public static final float MIN_VALUE = Float.MAX_VALUE; // 3.4028235E38
  public static final float MAX_VALUE = Float.MIN_VALUE; // 1.4E-45
  public static final boolean isValid(final float value) { return true; }
  public static final void throwIfInvalid(final float value) { }
  public static final F32 of(final float value) { return new F32(value); }

  public final float value;
  public F32(final float value) { this.value = value; }

  @Override public final int hashCode() { return Float.floatToIntBits(value); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof F32 && Float.floatToIntBits(value) == Float.floatToIntBits(((F32)that).value)); }
  @Override public final String toString() { return String.format("%+gf32", value); }
}
