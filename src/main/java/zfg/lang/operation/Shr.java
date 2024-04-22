package zfg.lang.operation;

import zfg.lang.primitive.I08;
import zfg.lang.primitive.I16;
import zfg.lang.primitive.I32;
import zfg.lang.primitive.I64;
import zfg.lang.primitive.U08;
import zfg.lang.primitive.U16;
import zfg.lang.primitive.U32;
import zfg.lang.primitive.U64;

public final class Shr {
  private Shr() {}
  public static interface I<T extends I<T>> { public T shr(final I32 that); }
  public static final <T extends I<T>> T shr(final T a, final I32 b) { return a.shr(b); }
  // note: shifting uses the lower log2(width(a)) bytes of b
  public static final int    u08(final int    a, final int    b) { return a >>> (b & 0x7); } // zero extend
  public static final int    u16(final int    a, final int    b) { return a >>> (b & 0xF); } // zero extend
  public static final int    u32(final int    a, final int    b) { return a >>> b; }         // zero extend
  public static final long   u64(final long   a, final int    b) { return a >>> b; }         // zero extend
  public static final int    i08(final int    a, final int    b) { return a >> (b & 0x7); }  // sign extend
  public static final int    i16(final int    a, final int    b) { return a >> (b & 0xF); }  // sign extend
  public static final int    i32(final int    a, final int    b) { return a >> b; }          // sign extend
  public static final long   i64(final long   a, final int    b) { return a >> b; }          // sign extend
  public static final U08 u08(final U08 a, final I32 b) { return U08.of(u08(a.value, b.value)); }
  public static final U16 u16(final U16 a, final I32 b) { return U16.of(u16(a.value, b.value)); }
  public static final U32 u32(final U32 a, final I32 b) { return U32.of(u32(a.value, b.value)); }
  public static final U64 u64(final U64 a, final I32 b) { return U64.of(u64(a.value, b.value)); }
  public static final I08 i08(final I08 a, final I32 b) { return I08.of(i08(a.value, b.value)); }
  public static final I16 i16(final I16 a, final I32 b) { return I16.of(i16(a.value, b.value)); }
  public static final I32 i32(final I32 a, final I32 b) { return I32.of(i32(a.value, b.value)); }
  public static final I64 i64(final I64 a, final I32 b) { return I64.of(i64(a.value, b.value)); }
}
