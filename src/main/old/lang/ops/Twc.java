package zfg.lang.ops;

import zfg.lang.val.Val;
import zfg.old.lang3.val.Bit;
import zfg.old.lang3.val.F32;
import zfg.old.lang3.val.F64;
import zfg.old.lang3.val.I08;
import zfg.old.lang3.val.I16;
import zfg.old.lang3.val.I32;
import zfg.old.lang3.val.I64;
import zfg.old.lang3.val.U08;
import zfg.old.lang3.val.U16;
import zfg.old.lang3.val.U32;
import zfg.old.lang3.val.U64;

public final class Twc {
  private Twc() {}

  public static final int    bit(final int    a, final int    b) { return a < b ? -1 : a > b ? 1 : 0; }
  public static final int    u08(final int    a, final int    b) { return a < b ? -1 : a > b ? 1 : 0; }
  public static final int    u16(final int    a, final int    b) { return a < b ? -1 : a > b ? 1 : 0; }
  public static final int    u32(final int    a, final int    b) { final int c = a - b + 0x80000000; return c < 0 ? -1 : c > 0 ? 1 : 0; }
  public static final int    u64(final long   a, final long   b) { final long c = a - b + 0x80000000_00000000L; return c < 0 ? -1 : c > 0 ? 1 : 0; }
  public static final int    i08(final int    a, final int    b) { return a < b ? -1 : a > b ? 1 : 0; }
  public static final int    i16(final int    a, final int    b) { return a < b ? -1 : a > b ? 1 : 0; }
  public static final int    i32(final int    a, final int    b) { return a < b ? -1 : a > b ? 1 : 0; }
  public static final int    i64(final long   a, final long   b) { return a < b ? -1 : a > b ? 1 : 0; }
  public static final int    f32(final float  a, final float  b) { return Float.compare(a, b); }
  public static final int    f64(final double a, final double b) { return Double.compare(a, b); }
  public static final I32Type bit(final BitType a, final BitType b) { return I32Type.of(bit(a.value, b.value)); }
  public static final I32Type u08(final U08Type a, final U08Type b) { return I32Type.of(u08(a.value, b.value)); }
  public static final I32Type u16(final U16Type a, final U16Type b) { return I32Type.of(u16(a.value, b.value)); }
  public static final I32Type u32(final U32Type a, final U32Type b) { return I32Type.of(u32(a.value, b.value)); }
  public static final I32Type u64(final U64Type a, final U64Type b) { return I32Type.of(u64(a.value, b.value)); }
  public static final I32Type i08(final I08Type a, final I08Type b) { return I32Type.of(i08(a.value, b.value)); }
  public static final I32Type i16(final I16Type a, final I16Type b) { return I32Type.of(i16(a.value, b.value)); }
  public static final I32Type i32(final I32Type a, final I32Type b) { return I32Type.of(i32(a.value, b.value)); }
  public static final I32Type i64(final I64Type a, final I64Type b) { return I32Type.of(i64(a.value, b.value)); }
  public static final I32Type f32(final F32Type a, final F32Type b) { return I32Type.of(f32(a.value, b.value)); }
  public static final I32Type f64(final F64Type a, final F64Type b) { return I32Type.of(f64(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final I32Type twc(final Val a, final Val b) { return ((I<Val>)a).twc(b); }
  public static interface I<T extends Val> { public I32Type twc(final T that); }
}
