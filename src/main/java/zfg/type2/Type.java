package zfg.type2;

/**
 * Type
 * ├─Vrt     : Virtual (does not exist at runtime)
 * │ ├─Unk   : Unknown (e.g. type inferening has not completed yet)
 * │ └─Err   : Error   (e.g. type inferening or type checking failed)
 * ├─Pri     : Primitive (passed by value)
 * │ ├─Bit   : 1-bit unsigned integer, (boolean, true of false, 0 or 1)
 * │ ├─U08   : 8-bit unsigned integer
 * │ ├─U16   : 16-bit unsigned integer
 * │ ├─U32   : 32-bit unsigned integer
 * │ ├─U64   : 64-bit unsigned integer
 * │ ├─I08   : 8-bit signed integer
 * │ ├─I16   : 16-bit signed integer
 * │ ├─I32   : 32-bit signed integer
 * │ ├─I64   : 64-bit signed integer
 * │ ├─F32   : 32-bit floating-point number
 * │ └─F64   : 64-bit floating-point number
 * ├─Ref     : Reference (passed by reference)
 * │ ├─Com   : Composite
 * │ │ ├─Arr : Array
 * │ │ ├─Tup : Pointer
 * │ │ └─Rec : Record
 * │ └─Fun   : Function
 * ├─Fun   : Function
 *
 *
 */
public interface Type {

  /** Returns a string representation of this type. */
  public String toString();

  /** Appends the string representation of this type to the given StringBuilder and returns it. */
  public StringBuilder toString(final StringBuilder buf);
}
