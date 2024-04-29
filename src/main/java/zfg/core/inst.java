// package zfg.core;

// public final class inst {

//   /** An instance of a data type */
//   public sealed interface Inst<T extends type.Type> permits
//     Bit, U08, U16, U32, U64, I08, I16, I32, I64, F32, F64 {
//     // returns the type of this instance
//     public T type();
//   }

//   /** Instance of {@link type.Bit} */
//   public static final class Bit implements Inst<type.Bit> {
//     // value and constructor
//     public final int value;
//     protected Bit(final int value) { this.value = value; }

//     // type
//     public type.Bit type() { return type.bit; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%dbit", value); }
//     public int hashCode() { return value; }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof Bit other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.U08} */
//   public static final class U08 implements Inst<type.U08> {
//     // value and constructor
//     public final int value;
//     protected U08(final int value) { this.value = value; }

//     // type
//     public type.U08 type() { return type.u08; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%du08", value); }
//     public int hashCode() { return value; }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof U08 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.U16} */
//   public static final class U16 implements Inst<type.U16> {
//     // value and constructor
//     public final int value;
//     protected U16(final int value) { this.value = value; }

//     // type
//     public type.U16 type() { return type.u16; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%du16", value); }
//     public int hashCode() { return value; }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof U16 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.U32} */
//   public static final class U32 implements Inst<type.U32> {
//     // value and constructor
//     public final int value;
//     protected U32(final int value) { this.value = value; }

//     // type
//     public type.U32 type() { return type.u32; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%su32", Integer.toUnsignedString(value)); }
//     public int hashCode() { return value; }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof U32 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.U64} */
//   public static final class U64 implements Inst<type.U64> {
//     // value and constructor
//     public final long value;
//     protected U64(final long value) { this.value = value; }

//     // type
//     public type.U64 type() { return type.u64; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%su64", Long.toUnsignedString(value)); }
//     public int hashCode() { return (int)(value ^ (value >>> 32)); }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof U64 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.I08} */
//   public static final class I08 implements Inst<type.I08> {
//     // value and constructor
//     public final int value;
//     protected I08(final int value) { this.value = value; }

//     // type
//     public type.I08 type() { return type.i08; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%di08", value); }
//     public int hashCode() { return value; }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof I08 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.I16} */
//   public static final class I16 implements Inst<type.I16> {
//     // value and constructor
//     public final int value;
//     protected I16(final int value) { this.value = value; }

//     // type
//     public type.I16 type() { return type.i16; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%di16", value); }
//     public int hashCode() { return value; }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof I16 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.I32} */
//   public static final class I32 implements Inst<type.I32> {
//     // value and constructor
//     public final int value;
//     protected I32(final int value) { this.value = value; }

//     // type
//     public type.I32 type() { return type.i32; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%di32", value); }
//     public int hashCode() { return value; }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof I32 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.I64} */
//   public static final class I64 implements Inst<type.I64> {
//     // value and constructor
//     public final long value;
//     protected I64(final long value) { this.value = value; }

//     // type
//     public type.I64 type() { return type.i64; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%di64", value); }
//     public int hashCode() { return (int)(value ^ (value >>> 32)); }
//     public boolean equals(final Object that) {
//       return (this == that || (that instanceof I64 other && value == other.value));
//     }
//   }

//   /** Instance of {@link type.F32} */
//   public static final class F32 implements Inst<type.F32> {
//     // value and constructor
//     public final float value;
//     protected F32(final float value) { this.value = value; }

//     // type
//     public type.F32 type() { return type.f32; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%+gf32", value); }
//     public int hashCode() { return Float.floatToIntBits(value); }
//     public boolean equals(final Object that) {
//       return (this == that || (
//         that instanceof F32 other &&
//         Float.floatToIntBits(value) == Float.floatToIntBits(other.value)
//       ));
//     }
//   }

//   /** Instance of {@link type.F64} */
//   public static final class F64 implements Inst<type.F64> {
//     // value and constructor
//     public final double value;
//     protected F64(final double value) { this.value = value; }

//     // type
//     public type.F64 type() { return type.f64; }

//     // toString, equals, and hashCode
//     public String toString() { return String.format("%+gf64", value); }
//     public int hashCode() {
//       final long h = Double.doubleToLongBits(value);
//       return (int)(h ^ (h >>> 32));
//     }
//     public boolean equals(final Object that) {
//       return (this == that || (
//         that instanceof F64 other &&
//         Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value)
//       ));
//     }
//   }

//   // module
//   private inst() {}
// }
