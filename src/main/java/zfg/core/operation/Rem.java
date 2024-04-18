package zfg.core.operation;

import zfg.core.primative.I08;
import zfg.core.primative.I16;
import zfg.core.primative.I32;
import zfg.core.primative.I64;
import zfg.core.primative.U08;
import zfg.core.primative.U16;
import zfg.core.primative.U32;
import zfg.core.primative.U64;
import zfg.core.primative.Val;
import zfg.core.primative.F32;
import zfg.core.primative.F64;

public final class Rem {
  private Rem() {}
  private static final int    u08(final int    a, final int    b) { return a % b; }
  private static final int    u16(final int    a, final int    b) { return a % b; }
  private static final int    u32(final int    a, final int    b) { return (int)((a & 0xFFFFFFFFL) % (b | 0xFFFFFFFFL)); }
  private static final long   u64(final long   a, final long   b) { return Long.remainderUnsigned(a, b); }
  private static final int    i08(final int    a, final int    b) { return a % b; }
  private static final int    i16(final int    a, final int    b) { return a % b; }
  private static final int    i32(final int    a, final int    b) { return a % b; }
  private static final long   i64(final long   a, final long   b) { return a % b; }
  private static final float  f32(final float  a, final float  b) { return a % b; }
  private static final double f64(final double a, final double b) { return a % b; }
  public static final U08 u08(final U08 a, final U08 b) { return U08.of(u08(a.value, b.value)); }
  public static final U16 u16(final U16 a, final U16 b) { return U16.of(u16(a.value, b.value)); }
  public static final U32 u32(final U32 a, final U32 b) { return U32.of(u32(a.value, b.value)); }
  public static final U64 u64(final U64 a, final U64 b) { return U64.of(u64(a.value, b.value)); }
  public static final I08 i08(final I08 a, final I08 b) { return I08.of(i08(a.value, b.value)); }
  public static final I16 i16(final I16 a, final I16 b) { return I16.of(i16(a.value, b.value)); }
  public static final I32 i32(final I32 a, final I32 b) { return I32.of(i32(a.value, b.value)); }
  public static final I64 i64(final I64 a, final I64 b) { return I64.of(i64(a.value, b.value)); }
  public static final F32 f32(final F32 a, final F32 b) { return F32.of(f32(a.value, b.value)); }
  public static final F64 f64(final F64 a, final F64 b) { return F64.of(f64(a.value, b.value)); }
  public static interface I<V extends Val> { public V rem(final V rhs); }
}
