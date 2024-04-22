package zfg.lang.operation;

public class Conversion {
  private Conversion() {}


  public static final int    bitToBit(final int a)    { return a; }           // no-op
  public static final int    bitToU08(final int a)    { return a; }           // zero extend
  public static final int    bitToU16(final int a)    { return a; }           // zero extend
  public static final int    bitToU32(final int a)    { return a; }           // zero extend
  public static final long   bitToU64(final long a)   { return (long) a; }    // zero extend
  public static final int    bitToI08(final int a)    { return a; }           // zero extend
  public static final int    bitToI16(final int a)    { return a; }           // zero extend
  public static final int    bitToI32(final int a)    { return a; }           // zero extend
  public static final long   bitToI64(final long a)   { return (long) a; }    // zero extend
  public static final float  bitToF32(final float a)  { return (float) a; }   // i2f
  public static final double bitToF64(final double a) { return (double) a; }  // i2d

  public static final int    u08ToU08(final int a)    { return a; }           // no-op
  public static final int    u08ToU16(final int a)    { return a; }           // zero extend
  public static final int    u08ToU32(final int a)    { return a; }           // zero extend
  public static final long   u08ToU64(final int a)    { return (long) a; }    // zero extend
  public static final int    u08ToI08(final int a)    { return a; }           // reinterpret
  public static final int    u08ToI16(final int a)    { return a; }           // zero extend
  public static final int    u08ToI32(final int a)    { return a; }           // zero extend
  public static final long   u08ToI64(final int a)    { return (long) a; };   // zero extend
  public static final float  u08ToF32(final int a)    { return (float) a; }   // i2f
  public static final double u08ToF64(final int a)    { return (double) a; }  // i2d

  public static final int    u16ToU08(final int a)    { return a & 0xFF; }    // truncate
  public static final int    u16ToU16(final int a)    { return a; }           // no-op
  public static final int    u16ToU32(final int a)    { return a; }           // zero extend
  public static final long   u16ToU64(final int a)    { return (long) a; }    // zero extend
  public static final int    u16ToI08(final int a)    { return a & 0xFF; }    // truncate
  public static final int    u16ToI16(final int a)    { return a; }           // reinterpret
  public static final int    u16ToI32(final int a)    { return a; }           // zero extend
  public static final long   u16ToI64(final int a)    { return (long) a; }    // zero extend
  public static final float  u16ToF32(final int a)    { return (float) a; }   // i2f
  public static final double u16ToF64(final int a)    { return (double) a; }  // i2d

  public static final int    u32ToU08(final int a)    { return a & 0xFF; }           // truncate
  public static final int    u32ToU16(final int a)    { return a & 0xFFFF; }         // truncate
  public static final int    u32ToU32(final int a)    { return a; }                  // no-op
  public static final long   u32ToU64(final int a)    { return a & 0xFFFFFFFFL; }    // zero etend
  public static final int    u32ToI08(final int a)    { return a & 0xFF; }           // truncate
  public static final int    u32ToI16(final int a)    { return a & 0xFFFF; }         // truncate
  public static final int    u32ToI32(final int a)    { return a; }                  // reinterpret
  public static final long   u32ToI64(final int a)    { return a & 0xFFFFFFFFL; }    // zero extend
  public static final float  u32ToF32(final int a)    { return (float)(a & 0xFFFFFFFFL); }  // l2f
  public static final double u32ToF64(final int a)    { return (double)(a & 0xFFFFFFFFL); } // l2d

  public static final int    u64ToU08(final long a)   { return ((int) a) & 0xFF; }   // truncate
  public static final int    u64ToU16(final long a)   { return ((int) a) & 0xFFFF; } // truncate
  public static final int    u64ToU32(final long a)   { return (int) a; }            // truncate
  public static final long   u64ToU64(final long a)   { return a; }                  // no-op
  public static final int    u64ToI08(final long a)   { return ((int) a) & 0xFF; }   // truncate
  public static final int    u64ToI16(final long a)   { return ((int) a) & 0xFFFF; } // truncate
  public static final int    u64ToI32(final long a)   { return (int) a; }            // truncate
  public static final long   u64ToI64(final long a)   { return a; }                  // reinterpret
  public static final float  u64ToF32(final long a)   { return a < 0 ?  (float)(a & 0x7FFFFFFFFFFFFFFFL) + 0x1p63f : (float) a; }
  public static final double u64ToF64(final long a)   { return a < 0 ? (double)(a & 0x7FFFFFFFFFFFFFFFL) + 0x1p63d : (float) a; }

  public static final int    i08ToU08(final int a)    { return a; }          // reinterpret
  public static final int    i08ToU16(final int a)    { return a & 0xFFFF; } // sign extend
  public static final int    i08ToU32(final int a)    { return a; }          // sign extend
  public static final long   i08ToU64(final int a)    { return (long) a; }   // sign extend
  public static final int    i08ToI08(final int a)    { return a; }          // no-op
  public static final int    i08ToI16(final int a)    { return a; }          // sign extend
  public static final int    i08ToI32(final int a)    { return a; }          // sign extend
  public static final long   i08ToI64(final int a)    { return (long) a; }   // sign extend
  public static final float  i08ToF32(final int a)    { return (float) a; }  // i2f
  public static final double i08ToF64(final int a)    { return (double) a; } // i2d

  public static final int    i16ToU08(final int a)    { return a & 0xFF; }    // truncate


}
