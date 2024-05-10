package zfg.lang.ops;

import zfg.lang.val.Val;
import zfg.old.lang3.val.I08;
import zfg.old.lang3.val.I16;
import zfg.old.lang3.val.I32;
import zfg.old.lang3.val.I64;
import zfg.old.lang3.val.U08;
import zfg.old.lang3.val.U16;
import zfg.old.lang3.val.U32;
import zfg.old.lang3.val.U64;

public final class Shr {
  private Shr() {}

  // note: shifting uses the lower log2(width(a)) bits of b
  public static final int    u08(final int    a, final int    b) { return a >>> (b & 0x7); } // zero extend
  public static final int    u16(final int    a, final int    b) { return a >>> (b & 0xF); } // zero extend
  public static final int    u32(final int    a, final int    b) { return a >>> b; }         // zero extend
  public static final long   u64(final long   a, final int    b) { return a >>> b; }         // zero extend
  public static final int    i08(final int    a, final int    b) { return a >> (b & 0x7); }  // sign extend
  public static final int    i16(final int    a, final int    b) { return a >> (b & 0xF); }  // sign extend
  public static final int    i32(final int    a, final int    b) { return a >> b; }          // sign extend
  public static final long   i64(final long   a, final int    b) { return a >> b; }          // sign extend
  public static final U08Type u08(final U08Type a, final I32Type b) { return U08Type.of(u08(a.value, b.value)); }
  public static final U16Type u16(final U16Type a, final I32Type b) { return U16Type.of(u16(a.value, b.value)); }
  public static final U32Type u32(final U32Type a, final I32Type b) { return U32Type.of(u32(a.value, b.value)); }
  public static final U64Type u64(final U64Type a, final I32Type b) { return U64Type.of(u64(a.value, b.value)); }
  public static final I08Type i08(final I08Type a, final I32Type b) { return I08Type.of(i08(a.value, b.value)); }
  public static final I16Type i16(final I16Type a, final I32Type b) { return I16Type.of(i16(a.value, b.value)); }
  public static final I32Type i32(final I32Type a, final I32Type b) { return I32Type.of(i32(a.value, b.value)); }
  public static final I64Type i64(final I64Type a, final I32Type b) { return I64Type.of(i64(a.value, b.value)); }

  @SuppressWarnings("unchecked")
  public static final Val shr(final Val a, final I32Type b) { return ((I<Val>)a).shr(b); }
  public static interface I<T extends Val> { public T shr(final I32Type that); }
}
