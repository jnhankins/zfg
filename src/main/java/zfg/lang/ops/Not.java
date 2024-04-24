package zfg.lang.ops;

import zfg.lang.val.Bit;
import zfg.lang.val.I08;
import zfg.lang.val.I16;
import zfg.lang.val.I32;
import zfg.lang.val.I64;
import zfg.lang.val.U08;
import zfg.lang.val.U16;
import zfg.lang.val.U32;
import zfg.lang.val.U64;
import zfg.lang.val.Val;

public final class Not {
  private Not() {}

  public static final int    bit(final int    a) { return a ^ 1; }
  public static final int    u08(final int    a) { return a ^ 0xFF; }
  public static final int    u16(final int    a) { return a ^ 0xFFFF; }
  public static final int    u32(final int    a) { return ~a; }
  public static final long   u64(final long   a) { return ~a; }
  public static final int    i08(final int    a) { return a ^ 0xFF; }
  public static final int    i16(final int    a) { return a ^ 0xFFFF; }
  public static final int    i32(final int    a) { return ~a; }
  public static final long   i64(final long   a) { return ~a; }
  public static final Bit bit(final Bit a) { return Bit.of(bit(a.value)); }
  public static final U08 u08(final U08 a) { return U08.of(u08(a.value)); }
  public static final U16 u16(final U16 a) { return U16.of(u16(a.value)); }
  public static final U32 u32(final U32 a) { return U32.of(u32(a.value)); }
  public static final U64 u64(final U64 a) { return U64.of(u64(a.value)); }
  public static final I08 i08(final I08 a) { return I08.of(i08(a.value)); }
  public static final I16 i16(final I16 a) { return I16.of(i16(a.value)); }
  public static final I32 i32(final I32 a) { return I32.of(i32(a.value)); }
  public static final I64 i64(final I64 a) { return I64.of(i64(a.value)); }

  @SuppressWarnings("unchecked")
  public static final Val not(final Val a) { return ((I<Val>)a).not(); }
  public static interface I<T extends Val> { public Val not(); }
}