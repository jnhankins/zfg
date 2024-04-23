package zfg.lang.ops;

import zfg.lang.val.Bit;
import zfg.lang.val.Val;

public final class Lnt {
  private Lnt() {}

  public static final int    bit(final int    a) { return a ^ 1; }
  public static final Bit bit(final Bit a) { return Bit.of(bit(a.value)); }

  @SuppressWarnings("unchecked")
  public static final Bit lnt(final Val a) { return ((I<Val>)a).lnt(); }
  public static interface I<T extends Val> { public Bit lnt(); }
}
