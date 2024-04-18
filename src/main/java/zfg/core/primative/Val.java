package zfg.core.primative;

/**
 * val          : value
 *  ├─ref       : reference
 *  │ ├─arr     : data accessed by offset
 *  │ ├─obj     : data accessed by key
 *  │ └─fun     : function, no data
 *  └─num       : number
 *    ├─bit     : 0 or 1
 *    ├─int     : integeral
 *    │ ├─uxx   : unsigned integer
 *    │ │ ├─u08 : unsigned 8-bit integer
 *    │ │ ├─u16 : unsigned 16-bit integer
 *    │ │ ├─u32 : unsigned 32-bit integer
 *    │ │ └─u64 : unsigned 64-bit integer
 *    │ └─ixx   : signed integer
 *    │   ├─i08 : signed 8-bit integer
 *    │   ├─i16 : signed 16-bit integer
 *    │   ├─i32 : signed 32-bit integer
 *    │   └─i64 : signed 64-bit integer
 *    └─fxx     : floating point
 *      ├─f32   : 32-bit floating point
 *      └─f64   : 64-bit floating point
 */
public interface Val {
  public static interface Ref extends Val {}
  public static interface Num extends Val {}
  public static interface Int extends Num {}
  public static interface Uxx extends Int {}
  public static interface Ixx extends Int {}
  public static interface Fxx extends Num {}
}
