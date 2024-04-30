package zfg.lang.ops;

import zfg.lang.val.Val;
import zfg.old.lang3.val.Bit;

public final class Lnt {
  private Lnt() {}

  public static final int    bit(final int    a) { return a ^ 1; }
  public static final BitType bit(final BitType a) { return BitType.of(bit(a.value)); }

  @SuppressWarnings("unchecked")
  public static final BitType lnt(final Val a) { return ((I<Val>)a).lnt(); }
  public static interface I<T extends Val> { public BitType lnt(); }
}
