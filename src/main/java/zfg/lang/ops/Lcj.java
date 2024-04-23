package zfg.lang.ops;

import zfg.lang.val.Bit;
import zfg.lang.val.Val;

public final class Lcj {
  private Lcj() {}

  public static final int    bit(final int    a, final int    b) { return a & b; }
  public static final Bit bit(final Bit a, final Bit b) { return Bit.of(bit(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final Bit lcj(final Val a, final Val b) { return ((I<Val>)a).lcj(b); }
  public static interface I<T extends Val> { public Bit lcj(final T that); }
}
