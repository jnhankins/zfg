package zfg.lang.operation;

import zfg.lang.primitive.Bit;
import zfg.lang.primitive.Val;

public final class Lnt {
  private Lnt() {}

  public static final int    bit(final int    a) { return a ^ 1; }
  public static final Bit bit(final Bit a) { return Bit.of(bit(a.value)); }

  @SuppressWarnings("unchecked")
  public static final Bit lnt(final Val a) { return ((I<Val>)a).lnt(); }
  public static interface I<T extends Val> { public Bit lnt(); }
}
