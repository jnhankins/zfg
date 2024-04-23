package zfg.lang.operation;

import zfg.lang.primitive.Bit;
import zfg.lang.primitive.Val;

public final class Ldj {
  private Ldj() {}

  public static final int    bit(final int    a, final int    b) { return a | b; }
  public static final Bit bit(final Bit a, final Bit b) { return Bit.of(bit(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final Bit ldj(final Val a, final Val b) { return ((I<Val>)a).ldj(b); }
  public static interface I<T extends Val> { public Bit ldj(final T that); }
}
