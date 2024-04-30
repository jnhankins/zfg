package zfg.lang.ops;

import zfg.lang.val.Val;
import zfg.old.lang3.val.Bit;

public final class Ldj {
  private Ldj() {}

  public static final int    bit(final int    a, final int    b) { return a | b; }
  public static final BitType bit(final BitType a, final BitType b) { return BitType.of(bit(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final BitType ldj(final Val a, final Val b) { return ((I<Val>)a).ldj(b); }
  public static interface I<T extends Val> { public BitType ldj(final T that); }
}
