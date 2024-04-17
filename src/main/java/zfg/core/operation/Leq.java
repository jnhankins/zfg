package zfg.core.operation;

import zfg.core.primative.Bit;
import zfg.core.primative.F32;
import zfg.core.primative.F64;
import zfg.core.primative.I08;
import zfg.core.primative.I16;
import zfg.core.primative.I32;
import zfg.core.primative.I64;
import zfg.core.primative.U08;
import zfg.core.primative.U16;
import zfg.core.primative.U32;
import zfg.core.primative.U64;

public final class Leq {
  private Leq() {}
  // TODO: Test performance difference: (b - a) >>> 31
  private static final int    bit(final int    a, final int    b) { return a <= b ? 1 : 0; }
  private static final int    u08(final int    a, final int    b) { return a <= b ? 1 : 0; }
  private static final int    u16(final int    a, final int    b) { return a <= b ? 1 : 0; }
  private static final int    u32(final int    a, final int    b) { return (a + 0x80000000) <= (b + 0x80000000) ? 1 : 0; }
  private static final int    u64(final long   a, final long   b) { return (a + 0x80000000_00000000L) <= (b + 0x80000000_00000000L) ? 1 : 0; }
  private static final int    i08(final int    a, final int    b) { return a <= b ? 1 : 0; }
  private static final int    i16(final int    a, final int    b) { return a <= b ? 1 : 0; }
  private static final int    i32(final int    a, final int    b) { return a <= b ? 1 : 0; }
  private static final int    i64(final long   a, final long   b) { return a <= b ? 1 : 0; }
  private static final int    f32(final float  a, final float  b) { return a <= b ? 1 : 0; }
  private static final int    f64(final double a, final double b) { return a <= b ? 1 : 0; }
  public static final Bit bit(final U08 a, final U08 b) { return Bit.of(bit(a.value, b.value)); }
  public static final Bit u08(final U08 a, final U08 b) { return Bit.of(u08(a.value, b.value)); }
  public static final Bit u16(final U16 a, final U16 b) { return Bit.of(u16(a.value, b.value)); }
  public static final Bit u32(final U32 a, final U32 b) { return Bit.of(u32(a.value, b.value)); }
  public static final Bit u64(final U64 a, final U64 b) { return Bit.of(u64(a.value, b.value)); }
  public static final Bit i08(final I08 a, final I08 b) { return Bit.of(i08(a.value, b.value)); }
  public static final Bit i16(final I16 a, final I16 b) { return Bit.of(i16(a.value, b.value)); }
  public static final Bit i32(final I32 a, final I32 b) { return Bit.of(i32(a.value, b.value)); }
  public static final Bit i64(final I64 a, final I64 b) { return Bit.of(i64(a.value, b.value)); }
  public static final Bit f32(final F32 a, final F32 b) { return Bit.of(f32(a.value, b.value)); }
  public static final Bit f64(final F64 a, final F64 b) { return Bit.of(f64(a.value, b.value)); }
}
