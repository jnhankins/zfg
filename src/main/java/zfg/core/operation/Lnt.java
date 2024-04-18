package zfg.core.operation;

import zfg.core.primative.Bit;
import zfg.core.primative.Val;

public final class Lnt {
  private Lnt() {}
  private static final int    bit(final int    a) { return (~a) & 1; }
  public static final Bit bit(final Bit a) { return Bit.of(bit(a.value)); }
  public static interface I<V extends Val> { public V lnt(); }
}
