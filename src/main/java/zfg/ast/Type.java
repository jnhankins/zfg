package zfg.ast;

/**
 * val          : value
 * ├─num       : number
 * │ ├─int     : integeral
 * │ │ ├─bit   : 0 or 1
 * │ │ ├─uxx   : unsigned integer
 * │ │ │ ├─u08 : unsigned 8-bit integer
 * │ │ │ ├─u16 : unsigned 16-bit integer
 * │ │ │ ├─u32 : unsigned 32-bit integer
 * │ │ │ └─u64 : unsigned 64-bit integer
 * │ │ └─ixx   : signed integer
 * │ │   ├─i08 : signed 8-bit integer
 * │ │   ├─i16 : signed 16-bit integer
 * │ │   ├─i32 : signed 32-bit integer
 * │ │   └─i64 : signed 64-bit integer
 * │ └─flt     : floating point
 * │   └─fxx   : floating point
 * │     ├─f32 : 32-bit floating point
 * │     └─f64 : 64-bit floating point
 * └─ref       : reference
 *   ├─arr     : data accessed by offset
 *   ├─obj     : data accessed by key
 *   └─fun     : function, no data
 */
public sealed interface Type {
  public int fsize(); // frame size
  public int nbits(); // nominal number of bits
  public int order(); // type-widening order

  public static sealed interface Wxx extends Type {}
  public static sealed interface W01 extends Wxx { public default int fsize() { return 1; }; public default int nbits() { return 1; } }
  public static sealed interface W08 extends Wxx { public default int fsize() { return 1; }; public default int nbits() { return 8; } }
  public static sealed interface W16 extends Wxx { public default int fsize() { return 1; }; public default int nbits() { return 16; } }
  public static sealed interface W32 extends Wxx { public default int fsize() { return 1; }; public default int nbits() { return 32; } }
  public static sealed interface W64 extends Wxx { public default int fsize() { return 2; }; public default int nbits() { return 64; } }

  public static sealed interface Val extends    Type     {}
  public static sealed interface Num extends    Val      {}
  public static sealed interface Int extends    Num      {}
  public static final  class     Bit implements Int, W01 { private Bit() {} public int order() { return 0; } }
  public static sealed interface Uxx extends    Int, Wxx {}
  public static final  class     U08 implements Uxx, W08 { private U08() {} public int order() { return 1; } }
  public static final  class     U16 implements Uxx, W16 { private U16() {} public int order() { return 2; } }
  public static final  class     U32 implements Uxx, W32 { private U32() {} public int order() { return 3; } }
  public static final  class     U64 implements Uxx, W64 { private U64() {} public int order() { return 4; } }
  public static sealed interface Ixx extends    Int, Wxx {}
  public static final  class     I08 implements Ixx, W08 { private I08() {} public int order() { return 5; } }
  public static final  class     I16 implements Ixx, W16 { private I16() {} public int order() { return 6; } }
  public static final  class     I32 implements Ixx, W32 { private I32() {} public int order() { return 7; } }
  public static final  class     I64 implements Ixx, W64 { private I64() {} public int order() { return 8; } }
  public static sealed interface Flt extends    Num      {}
  public static sealed interface Fxx extends    Flt, Wxx {}
  public static final  class     F32 implements Fxx, W32 { private F32() {} public int order() { return 9; } }
  public static final  class     F64 implements Fxx, W64 { private F64() {} public int order() { return 10; } }
  public static non-sealed interface Ref extends    Val {} // TODO

  public static final Bit bit = new Bit();
  public static final U08 u08 = new U08();
  public static final U16 u16 = new U16();
  public static final U32 u32 = new U32();
  public static final U64 u64 = new U64();
  public static final I08 i08 = new I08();
  public static final I16 i16 = new I16();
  public static final I32 i32 = new I32();
  public static final I64 i64 = new I64();
  public static final F32 f32 = new F32();
  public static final F64 f64 = new F64();

  public static Type of(final zfg.lang.primitive.Val val) {
    return switch (val) {
      case zfg.lang.primitive.Bit x -> bit;
      case zfg.lang.primitive.U08 x -> u08;
      case zfg.lang.primitive.U16 x -> u16;
      case zfg.lang.primitive.U32 x -> u32;
      case zfg.lang.primitive.U64 x -> u64;
      case zfg.lang.primitive.I08 x -> i08;
      case zfg.lang.primitive.I16 x -> i16;
      case zfg.lang.primitive.I32 x -> i32;
      case zfg.lang.primitive.I64 x -> i64;
      case zfg.lang.primitive.F32 x -> f32;
      case zfg.lang.primitive.F64 x -> f64;
      default -> throw new AssertionError();
    };
  }
}
