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

public final class Ltn {
  private Ltn() {}

  // TODO: Test performance difference: (a - b) <<< 31
  public static final int    bit(final int    a, final int    b) { return a < b ? 1 : 0; }
  public static final int    u08(final int    a, final int    b) { return a < b ? 1 : 0; }
  public static final int    u16(final int    a, final int    b) { return a < b ? 1 : 0; }
  public static final int    u32(final int    a, final int    b) { return (a + 0x80000000) < (b + 0x80000000) ? 1 : 0; }
  public static final int    u64(final long   a, final long   b) { return (a + 0x80000000_00000000L) < (b + 0x80000000_00000000L) ? 1 : 0; }
  public static final int    i08(final int    a, final int    b) { return a < b ? 1 : 0; }
  public static final int    i16(final int    a, final int    b) { return a < b ? 1 : 0; }
  public static final int    i32(final int    a, final int    b) { return a < b ? 1 : 0; }
  public static final int    i64(final long   a, final long   b) { return a < b ? 1 : 0; }
  public static final int    f32(final float  a, final float  b) { return a < b ? 1 : 0; }
  public static final int    f64(final double a, final double b) { return a < b ? 1 : 0; }
  public static final BitType bit(final BitType a, final BitType b) { return BitType.of(bit(a.value, b.value)); }
  public static final BitType u08(final U08Type a, final U08Type b) { return BitType.of(u08(a.value, b.value)); }
  public static final BitType u16(final U16Type a, final U16Type b) { return BitType.of(u16(a.value, b.value)); }
  public static final BitType u32(final U32Type a, final U32Type b) { return BitType.of(u32(a.value, b.value)); }
  public static final BitType u64(final U64Type a, final U64Type b) { return BitType.of(u64(a.value, b.value)); }
  public static final BitType i08(final I08Type a, final I08Type b) { return BitType.of(i08(a.value, b.value)); }
  public static final BitType i16(final I16Type a, final I16Type b) { return BitType.of(i16(a.value, b.value)); }
  public static final BitType i32(final I32Type a, final I32Type b) { return BitType.of(i32(a.value, b.value)); }
  public static final BitType i64(final I64Type a, final I64Type b) { return BitType.of(i64(a.value, b.value)); }
  public static final BitType f32(final F32Type a, final F32Type b) { return BitType.of(f32(a.value, b.value)); }
  public static final BitType f64(final F64Type a, final F64Type b) { return BitType.of(f64(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final BitType ltn(final Val a, final Val b) { return ((I<Val>)a).ltn(b); }
  public static interface I<T extends Val> { public BitType ltn(final T that); }
}
