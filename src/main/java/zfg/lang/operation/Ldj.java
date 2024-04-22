package zfg.lang.operation;

import zfg.lang.primitive.Bit;

public final class Ldj {
  private Ldj() {}
  public static interface I<T extends I<T>> { public Bit ldj(final T that); }
  public static final <T extends I<T>> Bit ldj(final T a, final T b) { return a.ldj(b); }
  public static final int    bit(final int    a, final int    b) { return a | b; }
  public static final Bit bit(final Bit a, final Bit b) { return Bit.of(bit(a.value, b.value)); }
}
