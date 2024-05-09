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

public final class And {
  private And() {}

  public static final int    bit(final int    a, final int    b) { return a & b; }
  public static final int    u08(final int    a, final int    b) { return a & b; }
  public static final int    u16(final int    a, final int    b) { return a & b; }
  public static final int    u32(final int    a, final int    b) { return a & b; }
  public static final long   u64(final long   a, final long   b) { return a & b; }
  public static final int    i08(final int    a, final int    b) { return a & b; }
  public static final int    i16(final int    a, final int    b) { return a & b; }
  public static final int    i32(final int    a, final int    b) { return a & b; }
  public static final long   i64(final long   a, final long   b) { return a & b; }
  public static final BitType bit(final BitType a, final BitType b) { return BitType.of(bit(a.value, b.value)); }
  public static final U08Type u08(final U08Type a, final U08Type b) { return U08Type.of(u08(a.value, b.value)); }
  public static final U16Type u16(final U16Type a, final U16Type b) { return U16Type.of(u16(a.value, b.value)); }
  public static final U32Type u32(final U32Type a, final U32Type b) { return U32Type.of(u32(a.value, b.value)); }
  public static final U64Type u64(final U64Type a, final U64Type b) { return U64Type.of(u64(a.value, b.value)); }
  public static final I08Type i08(final I08Type a, final I08Type b) { return I08Type.of(i08(a.value, b.value)); }
  public static final I16Type i16(final I16Type a, final I16Type b) { return I16Type.of(i16(a.value, b.value)); }
  public static final I32Type i32(final I32Type a, final I32Type b) { return I32Type.of(i32(a.value, b.value)); }
  public static final I64Type i64(final I64Type a, final I64Type b) { return I64Type.of(i64(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final Val and(final Val a, final Val b) { return ((I<Val>)a).and(b); }
  public static interface I<T extends Val> { public T and(final T that); }
}