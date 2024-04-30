package zfg.lang.ops;

import zfg.lang.val.Val;
import zfg.old.lang3.val.F32;
import zfg.old.lang3.val.F64;
import zfg.old.lang3.val.I08;
import zfg.old.lang3.val.I16;
import zfg.old.lang3.val.I32;
import zfg.old.lang3.val.I64;

public final class Neg {
  private Neg() {}

  public static final int    i08(final int    a) { return (byte)-a; }
  public static final int    i16(final int    a) { return (short)-a; }
  public static final int    i32(final int    a) { return -a; }
  public static final long   i64(final long   a) { return -a; }
  public static final float  f32(final float  a) { return -a; }
  public static final double f64(final double a) { return -a; }
  public static final I08Type i08(final I08Type a) { return I08Type.of(i08(a.value)); }
  public static final I16Type i16(final I16Type a) { return I16Type.of(i16(a.value)); }
  public static final I32Type i32(final I32Type a) { return I32Type.of(i32(a.value)); }
  public static final I64Type i64(final I64Type a) { return I64Type.of(i64(a.value)); }
  public static final F32Type f32(final F32Type a) { return F32Type.of(f32(a.value)); }
  public static final F64Type f64(final F64Type a) { return F64Type.of(f64(a.value)); }

  @SuppressWarnings("unchecked")
  public static final Val neg(final Val a) { return ((I<Val>)a).neg(); }
  public static interface I<T extends Val> { public Val neg(); }
}
