package zfg.old.parse;

public class type {
  public static sealed interface Type permits Vrt, Val, Ref {}

  public static sealed interface Vrt extends Type permits Err, Unk, Unit {}
  public static sealed interface Val extends Type permits Bit, Int, Flt {}
  public static sealed interface Ref extends Type permits Arr, Rec, Fun {}
  public static sealed interface Int extends Val permits Uxx, Ixx {}
  public static sealed interface Uxx extends Int permits U08, U16, U32, U64 {}
  public static sealed interface Ixx extends Int permits I08, I16, I32, I64 {}
  public static sealed interface Flt extends Val permits Fxx {}
  public static sealed interface Fxx extends Flt permits F32, F64 {}

  public static final class Err implements Vrt {
    private Err() {}
    @Override public String toString() { return "Err"; }
  }
  public static final class Unk implements Vrt {
    private Unk() {}
    @Override public String toString() { return "unk"; }
  }
  public static final class Unit extends Rec implements Vrt {
    private Unit() {}
    @Override public String toString() { return "unit"; }
  }

  public static final class Bit implements Val {
    private Bit() {}
    @Override public String toString() { return "bit"; }
  }
  public static final class U08 implements Uxx {
    private U08() {}
    @Override public String toString() { return "u08"; }
  }
  public static final class U16 implements Uxx {
    private U16() {}
    @Override public String toString() { return "u16"; }
  }
  public static final class U32 implements Uxx {
    private U32() {}
    @Override public String toString() { return "u32"; }
  }
  public static final class U64 implements Uxx {
    private U64() {}
    @Override public String toString() { return "u64"; }
  }
  public static final class I08 implements Ixx {
    private I08() {}
    @Override public String toString() { return "i08"; }
  }
  public static final class I16 implements Ixx {
    private I16() {}
    @Override public String toString() { return "i16"; }
  }
  public static final class I32 implements Ixx {
    private I32() {}
    @Override public String toString() { return "i32"; }
  }
  public static final class I64 implements Ixx {
    private I64() {}
    @Override public String toString() { return "i64"; }
  }
  public static final class F32 implements Fxx {
    private F32() {}
    @Override public String toString() { return "f32"; }
  }
  public static final class F64 implements Fxx {
    private F64() {}
    @Override public String toString() { return "f64"; }
  }

  public static final class Arr implements Ref {
    public final int UNKNOWN_LENGTH = -1;
    public final Type elementType;
    public final int length;
    public Arr(final Type elem) { this.elem = elem; this.size = -1; }
    public Arr(final Type elem, final int size) { this.elem = elem; this.size = size; }
    @Override public String toString() { return elem + "[" + (size < 0 ? "" : size) + "]"; }
  }
  public static class Rec implements Ref {
    public final boolean[] isMut;
    public final String[]  names;
    public final Type[]    types;
    @Override public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("(");
      for (int i = 0; i < names.length; i++) {
        if (i > 0) sb.append(", ");
        if (isMut[i]) sb.append("mut ");
        sb.append(names[i]);
        sb.append(" ");
        sb.append(types[i]);
      }
      sb.append(")");
      return sb.toString();
    }
  }
  public static final class Fun implements Ref {
    public final Rec  paramTypes;
    public final Type returnType;
    @Override public String toString() { return "fun(" + paramTypes + "," + returnType + ")"; }
  }

  // Virtual types (no instances)
  public static final Err err = new Err();
  public static final Unk unk = new Unk();
  public static final Unit unit = new Unit(); // both virtual and empty record
  // Primitive types
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
  // Reference types
  public static final Arr arr(final Type elem) { return new Arr(elem); }
  public static final Arr arr(final Type elem, final int size) { return new Arr(elem, size); }
  public static final Rec rec(final Map<String, Type> fields) { return new Rec(fields); }
  public static final Fun fun(final Rec )
  // return
  // params

}
