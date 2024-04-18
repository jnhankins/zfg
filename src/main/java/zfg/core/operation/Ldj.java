package zfg.core.operation;

import zfg.core.primative.Bit;
import zfg.core.primative.Val;

public final class Ldj {
  private Ldj() {}
  private static final int    bit(final int    a, final int    b) { return (a | b) & 1; }
  public static final Bit bit(final Bit a, final Bit b) { return Bit.of(bit(a.value, b.value)); }
  public static interface I<V extends Val> { public V ldj(final V rhs); }
}
