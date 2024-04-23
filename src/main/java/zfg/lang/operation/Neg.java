package zfg.lang.operation;

import zfg.lang.primitive.F32;
import zfg.lang.primitive.F64;
import zfg.lang.primitive.I08;
import zfg.lang.primitive.I16;
import zfg.lang.primitive.I32;
import zfg.lang.primitive.I64;
import zfg.lang.primitive.Val;

public final class Neg {
  private Neg() {}

  public static final int    i08(final int    a) { return (byte)-a; }
  public static final int    i16(final int    a) { return (short)-a; }
  public static final int    i32(final int    a) { return -a; }
  public static final long   i64(final long   a) { return -a; }
  public static final float  f32(final float  a) { return -a; }
  public static final double f64(final double a) { return -a; }
  public static final I08 i08(final I08 a) { return I08.of(i08(a.value)); }
  public static final I16 i16(final I16 a) { return I16.of(i16(a.value)); }
  public static final I32 i32(final I32 a) { return I32.of(i32(a.value)); }
  public static final I64 i64(final I64 a) { return I64.of(i64(a.value)); }
  public static final F32 f32(final F32 a) { return F32.of(f32(a.value)); }
  public static final F64 f64(final F64 a) { return F64.of(f64(a.value)); }

  @SuppressWarnings("unchecked")
  public static final Val neg(final Val a) { return ((I<Val>)a).neg(); }
  public static interface I<T extends Val> { public Val neg(); }
}
