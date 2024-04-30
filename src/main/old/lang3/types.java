package zfg.old.lang3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Types
 *  ├─Vrt   : Virtual type (cannot be instantiated in memory; doesn't exist at runtime)
 *  │ ├─Unk : Unknown type (e.g. type inferrencing is not yet complete)
 *  │ └─Err : Error type (e.g. type inferrencing failed or type checking failed)
 *  ├─Pri   : Primitive type (passed by value)
 *  │ ├─Bit : 1-bit unsigned integer (boolean, true or false, 0 or 1)
 *  │ ├─U08 : 8-bit unsigned integer
 *  │ ├─U16 : 16-bit unsigned integer
 *  │ ├─U32 : 32-bit unsigned integer
 *  │ ├─U64 : 64-bit unsigned integer
 *  │ ├─I08 : 8-bit signed integer
 *  │ ├─I16 : 16-bit signed integer
 *  │ ├─I32 : 32-bit signed integer
 *  │ ├─I64 : 64-bit signed integer
 *  │ ├─F32 : 32-bit floating-point number
 *  │ └─F64 : 64-bit floating-point number
 *  ├─Ref   : Reference type (passed by reference)
 *  │ ├─Arr : Array type (data accessed by offset)
 *  │ ├─Rec : Record type (data accessed by key)
 *  │ └─Fun : Function type (no data)
 *  ├─Nom   : Named type (alias for a primitive, reference, or unit type and adds methods and type bounds)
 *  └─Unt   : Unit type (empty record of zero size)
 */
public final class types {
  private types() {}

  /** Type interface */
  public static sealed interface Type {
    public String toDebugString();
    public void toDebugString(final StringBuilder buf, final Set<NameType> seen);
  }

  /** Primitive Type interface */
  public static sealed interface PriType extends Type {
    public int ordinal();
    public default int flag() { return 1 << ordinal(); }    // FF_IIII_UUUU_B
    public default boolean isArithmetic() { return (flag() & 0b11_1111_1111_0) != 0; }
    public default boolean isBitwise()    { return (flag() & 0b00_1111_1111_0) != 0; }
  }

  /** Type abstract base implementation */
  private static sealed abstract class TypeBase implements Type {

    @Override public String toDebugString() {
      final StringBuilder buf = new StringBuilder();
      toDebugString(buf, new HashSet<>());
      return buf.toString();
    }

    @Override public void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append(this);
    }

    @Override public String toString() {
      return toDebugString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Virtual Types (cannot be instantiated in memory; doesn't exist at runtime)
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Type is unknown (e.g. type inferrencing is not yet complete) */
  public static final class UnkType extends TypeBase {
    private UnkType() {}
    @Override public String toString() { return "Unk"; }
  }

  /** Type is erroneous (e.g. type inferrencing failed or type checking failed) */
  public static final class ErrType extends TypeBase {
    private ErrType() {}
    @Override public String toString() { return "Err"; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Primitive Types (passed by value)
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** 1-bit unsigned integer (boolean, true or false, 0 or 1) */
  public static final class BitType extends TypeBase implements PriType {
    private BitType() {}
    @Override public int ordinal() { return 0; }
    @Override public String toString() { return "Bit"; }
  }

  /** 8-bit unsigned integer */
  public static final class U08Type extends TypeBase implements PriType {
    private U08Type() {}
    @Override public int ordinal() { return 1; }
    @Override public String toString() { return "U08"; }
  }

  /** 16-bit unsigned integer */
  public static final class U16Type extends TypeBase implements PriType {
    private U16Type() {}
    @Override public int ordinal() { return 2; }
    @Override public String toString() { return "U16"; }
  }

  /** 32-bit unsigned integer */
  public static final class U32Type extends TypeBase implements PriType {
    private U32Type() {}
    @Override public int ordinal() { return 3; }
    @Override public String toString() { return "U32"; }
  }

  /** 64-bit unsigned integer */
  public static final class U64Type extends TypeBase implements PriType {
    private U64Type() {}
    @Override public int ordinal() { return 4; }
    @Override public String toString() { return "U64"; }
  }

  /** 8-bit signed integer */
  public static final class I08Type extends TypeBase implements PriType {
    private I08Type() {}
    @Override public int ordinal() { return 5; }
    @Override public String toString() { return "I08"; }
  }

  /** 16-bit signed integer */
  public static final class I16Type extends TypeBase implements PriType {
    private I16Type() {}
    @Override public int ordinal() { return 6; }
    @Override public String toString() { return "I16"; }
  }

  /** 32-bit signed integer */
  public static final class I32Type extends TypeBase implements PriType {
    private I32Type() {}
    @Override public int ordinal() { return 7; }
    @Override public String toString() { return "I32"; }
  }

  /** 64-bit signed integer */
  public static final class I64Type extends TypeBase implements PriType {
    private I64Type() {}
    @Override public int ordinal() { return 8; }
    @Override public String toString() { return "I64"; }
  }

  /** 32-bit floating-point number */
  public static final class F32Type extends TypeBase implements PriType {
    private F32Type() {}
    @Override public int ordinal() { return 9; }
    @Override public String toString() { return "F32"; }
  }

  /** 64-bit floating-point number */
  public static final class F64Type extends TypeBase implements PriType {
    private F64Type() {}
    @Override public int ordinal() { return 10; }
    @Override public String toString() { return "F64"; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Reference Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Array type */
  public static final class ArrType extends TypeBase {
    public static final int UNKNOWN_LENGTH = -1;
    public final Type elementType;
    public final int length;
    private ArrType(final Type elemenType) {
      assert elemenType != null;
      this.elementType = elemenType;
      this.length = UNKNOWN_LENGTH;
    }
    private ArrType(final Type elemenType, final int length) {
      assert elemenType != null;
      assert length >= 0;
      this.elementType = elemenType;
      this.length = length;
    }
    @Override public void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append("Arr(");
      elementType.toDebugString(buf, seen);
      if (length != UNKNOWN_LENGTH) {
        buf.append(", ");
        buf.append(length);
      }
      buf.append(")");
    }
  }

  /** Record type */
  public static sealed class RecType extends TypeBase permits UntType {
    public final boolean[] muts;
    public final String[]  names;
    public final Type[]    types;
    private RecType() {
      this.muts = new boolean[0];
      this.names = new String[0];
      this.types = new Type[0];
    }
    private RecType(final boolean[] muts, final String[] names, final Type[] types) {
      assert muts != null;
      assert names != null;
      assert types != null;
      assert muts.length > 0;
      assert muts.length == names.length;
      assert muts.length == types.length;
      assert Arrays.stream(names).allMatch(zfg.old.lang3.names::isLowerSnakeCase);
      assert Arrays.stream(names).distinct().count() == names.length;
      assert Arrays.stream(types).allMatch(Objects::nonNull);
      this.muts = muts;
      this.names = names;
      this.types = types;
    }
    @Override public void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append("Rec(");
      for (int i = 0; i < muts.length; i++) {
        if (i > 0) buf.append(", ");
        buf.append(muts[i] ? "mut " : "let ");
        buf.append(names[i]);
        buf.append(" ");
        types[i].toDebugString(buf, seen);
      }
      buf.append(")");
    }
  }

  /** Function type */
  public static final class FunType extends TypeBase {
    public final RecType paramsType;
    public final Type    resultType;
    private FunType(final RecType paramsType, final Type resultType) {
      assert paramsType != null;
      assert resultType != null;
      this.paramsType = paramsType;
      this.resultType = resultType;
    }
    @Override public void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append("Fun(");
      paramsType.toDebugString(buf, seen);
      buf.append(", ");
      resultType.toDebugString(buf, seen);
      buf.append(")");
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Special Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Unit type (empty record of zero size) */
  public static final class UntType extends RecType {
    private UntType() { super(); }
    @Override public String toString() { return "Unit"; }
  }

  /** Named type (alias for a primitive, reference, or unit type and adds methods and type bounds) */
  public static final class NameType extends TypeBase {
    public final String name;
    public       Type   type;
    private NameType(final String name) {
      assert names.isUpperCamelCase(name);
      this.name = name;
      this.type = unk;
    }
    private NameType(final String name, final Type type) {
      assert names.isUpperCamelCase(name);
      assert type != null && type != unk;
      this.name = name;
      this.type = type;
    }
    public void bind(final Type type) {
      assert type != null && type != unk;
      assert this.type == unk;
      this.type = type;
    }
    @Override public void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append("Name(");
      buf.append(name);
      buf.append(", ");
      if (seen.add(this)) type.toDebugString(buf, seen);
      else                buf.append("...");
      buf.append(")");
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Type Singletons and Constructors
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Singleton Types
  public static final UnkType unk = new UnkType();
  public static final ErrType err = new ErrType();
  public static final BitType bit = new BitType();
  public static final U08Type u08 = new U08Type();
  public static final U16Type u16 = new U16Type();
  public static final U32Type u32 = new U32Type();
  public static final U64Type u64 = new U64Type();
  public static final I08Type i08 = new I08Type();
  public static final I16Type i16 = new I16Type();
  public static final I32Type i32 = new I32Type();
  public static final I64Type i64 = new I64Type();
  public static final F32Type f32 = new F32Type();
  public static final F64Type f64 = new F64Type();
  public static final UntType unt = new UntType();

  // Constructed Types
  public static final ArrType arr(final Type elemenType) {
    return new ArrType(elemenType);
  }
  public static final ArrType arr(final Type elemenType, final int length) {
    return new ArrType(elemenType, length);
  }
  public static final RecType rec(final boolean[] muts, final String[] names, final Type[] types) {
    return new RecType(muts, names, types);
  }
  public static final FunType fun(final RecType paramsType, final Type resultType) {
    return new FunType(paramsType, resultType);
  }
  public static final NameType name(final String name) {
    return new NameType(name);
  }
  public static final NameType name(final String name, final Type type) {
    return new NameType(name, type);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Helper Functions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final boolean isArithmetic(final Type type) {
    return
      type == u08 || type == u16 || type == u32 || type == u64 ||
      type == i08 || type == i16 || type == i32 || type == i64 ||
      type == f32 || type == f64;
  }
  public static final boolean isBitwise(final Type type) {
    return
      type == u08 || type == u16 || type == u32 || type == u64 ||
      type == i08 || type == i16 || type == i32 || type == i64;
  }
  public static final boolean isAssignableTo(final Type from, final Type to) {
    return (
      (from == u08 && (to == from || to == u16 || to == u32 || to == u64 || to == i16 || to == i32 || to == i64 || to == f32 || to == f64)) ||
      (from == u16 && (to == from ||              to == u32 || to == u64 ||              to == i32 || to == i64 || to == f32 || to == f64)) ||
      (from == u32 && (to == from ||                           to == u64 ||                           to == i64 || to == f32 || to == f64)) ||
      (from == u64 && (to == from ||                                                                               to == f32 || to == f64)) ||
      (from == i08 && (to == from ||                                        to == i16 || to == i32 || to == i64 || to == f32 || to == f64)) ||
      (from == i16 && (to == from ||                                                     to == i32 || to == i64 || to == f32 || to == f64)) ||
      (from == i32 && (to == from ||                                                                  to == i64 || to == f32 || to == f64)) ||
      (from == i64 && (to == from ||                                                                               to == f32 || to == f64)) ||
      (from == f32 && (to == from ||                                                                                            to == f64)) ||
      (from == f64 && (to == from))
    );
  }
}
