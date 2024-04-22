package zfg.lang.operation;

import zfg.lang.primitive.Bit;

public final class Lnt {
  private Lnt() {}
  public static interface I<T extends I<T>> { public Bit lnt(); }
  public static final <T extends I<T>> Bit lnt(final T a) { return a.lnt(); }
  public static final int    bit(final int    a) { return a ^ 1; }
  public static final Bit bit(final Bit a) { return Bit.of(bit(a.value)); }
}
