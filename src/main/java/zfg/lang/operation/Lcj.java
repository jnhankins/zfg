package zfg.lang.operation;

import zfg.lang.primitive.Bit;

public final class Lcj {
  private Lcj() {}
  public static interface I<T extends I<T>> { public Bit lcj(final T that); }
  public static final <T extends I<T>> Bit lcj(final T a, final T b) { return a.lcj(b); }
  public static final int    bit(final int    a, final int    b) { return a & b; }
  public static final Bit bit(final Bit a, final Bit b) { return Bit.of(bit(a.value, b.value)); }
}
