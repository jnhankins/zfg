package zfg.lang.ops;

import zfg.lang.val.Val;
import zfg.old.lang3.val.Bit;
import zfg.old.lang3.val.I08;
import zfg.old.lang3.val.I16;
import zfg.old.lang3.val.I32;
import zfg.old.lang3.val.I64;
import zfg.old.lang3.val.U08;
import zfg.old.lang3.val.U16;
import zfg.old.lang3.val.U32;
import zfg.old.lang3.val.U64;

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
  public static final BitType bit(final BitType a) { return BitType.of(bit(a.value)); }
  public static final U08Type u08(final U08Type a) { return U08Type.of(u08(a.value)); }
  public static final U16Type u16(final U16Type a) { return U16Type.of(u16(a.value)); }
  public static final U32Type u32(final U32Type a) { return U32Type.of(u32(a.value)); }
  public static final U64Type u64(final U64Type a) { return U64Type.of(u64(a.value)); }
  public static final I08Type i08(final I08Type a) { return I08Type.of(i08(a.value)); }
  public static final I16Type i16(final I16Type a) { return I16Type.of(i16(a.value)); }
  public static final I32Type i32(final I32Type a) { return I32Type.of(i32(a.value)); }
  public static final I64Type i64(final I64Type a) { return I64Type.of(i64(a.value)); }

  @SuppressWarnings("unchecked")
  public static final Val not(final Val a) { return ((I<Val>)a).not(); }
  public static interface I<T extends Val> { public Val not(); }
}
