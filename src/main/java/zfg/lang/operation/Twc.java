package zfg.lang.operation;

import zfg.lang.primitive.Bit;
import zfg.lang.primitive.F32;
import zfg.lang.primitive.F64;
import zfg.lang.primitive.I08;
import zfg.lang.primitive.I16;
import zfg.lang.primitive.I32;
import zfg.lang.primitive.I64;
import zfg.lang.primitive.U08;
import zfg.lang.primitive.U16;
import zfg.lang.primitive.U32;
import zfg.lang.primitive.U64;
import zfg.lang.primitive.Val;

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
  public static final I32 bit(final Bit a, final Bit b) { return I32.of(bit(a.value, b.value)); }
  public static final I32 u08(final U08 a, final U08 b) { return I32.of(u08(a.value, b.value)); }
  public static final I32 u16(final U16 a, final U16 b) { return I32.of(u16(a.value, b.value)); }
  public static final I32 u32(final U32 a, final U32 b) { return I32.of(u32(a.value, b.value)); }
  public static final I32 u64(final U64 a, final U64 b) { return I32.of(u64(a.value, b.value)); }
  public static final I32 i08(final I08 a, final I08 b) { return I32.of(i08(a.value, b.value)); }
  public static final I32 i16(final I16 a, final I16 b) { return I32.of(i16(a.value, b.value)); }
  public static final I32 i32(final I32 a, final I32 b) { return I32.of(i32(a.value, b.value)); }
  public static final I32 i64(final I64 a, final I64 b) { return I32.of(i64(a.value, b.value)); }
  public static final I32 f32(final F32 a, final F32 b) { return I32.of(f32(a.value, b.value)); }
  public static final I32 f64(final F64 a, final F64 b) { return I32.of(f64(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final I32 twc(final Val a, final Val b) { return ((I<Val>)a).twc(b); }
  public static interface I<T extends Val> { public I32 twc(final T that); }
}
