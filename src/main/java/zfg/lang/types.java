package zfg.lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class types {
  private types() {}

  public static sealed interface Type permits
      // Virtual types (cannot be instantiated in memory; doesn't exist at runtime)
      UnkType, ErrType,
      // Primitive types (passed by value)
      BitType, U08Type, U16Type, U32Type, U64Type, I08Type, I16Type, I32Type, I64Type, F32Type, F64Type,
      // Reference types (passed by reference)
      ArrType, // Data structure whose elements are accessed by index
      RecType, // Data structure whose elements are accessed by name keys
      FunType, // Function
      // Special types
      UnitType, // Empty record of zero size
      NameType  // Alias for a primitive, reference, or unit type and adds methods and type bounds
  {
    public default String toDebugString() {
      final StringBuilder buf = new StringBuilder();
      toDebugString(buf, new HashSet<>());
      return buf.toString();
    }
    public default void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append(this);
    }
  }

  // Virtual types
  public static final class UnkType implements Type {
    private UnkType() {}
    @Override public String toString() { return "Unk"; }
  }
  public static final class ErrType implements Type {
    private ErrType() {}
    @Override public String toString() { return "Err"; }
  }

  // Primitive types
  public static final class BitType implements Type {
    private BitType() {}
    @Override public String toString() { return "Bit"; }
  }
  public static final class U08Type implements Type {
    private U08Type() {}
    @Override public String toString() { return "U08"; }
  }
  public static final class U16Type implements Type {
    private U16Type() {}
    @Override public String toString() { return "U16"; }
  }
  public static final class U32Type implements Type {
    private U32Type() {}
    @Override public String toString() { return "U32"; }
  }
  public static final class U64Type implements Type {
    private U64Type() {}
    @Override public String toString() { return "U64"; }
  }
  public static final class I08Type implements Type {
    private I08Type() {}
    @Override public String toString() { return "I08"; }
  }
  public static final class I16Type implements Type {
    private I16Type() {}
    @Override public String toString() { return "I16"; }
  }
  public static final class I32Type implements Type {
    private I32Type() {}
    @Override public String toString() { return "I32"; }
  }
  public static final class I64Type implements Type {
    private I64Type() {}
    @Override public String toString() { return "I64"; }
  }
  public static final class F32Type implements Type {
    private F32Type() {}
    @Override public String toString() { return "F32"; }
  }
  public static final class F64Type implements Type {
    private F64Type() {}
    @Override public String toString() { return "F64"; }
  }

  // Reference types
  public static final class ArrType implements Type {
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
    @Override public String toString() { return toDebugString(); }
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
  public static sealed class RecType implements Type permits UnitType {
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
      assert Arrays.stream(names).allMatch(zfg.lang.names::isLowerSnakeCase);
      assert Arrays.stream(names).distinct().count() == names.length;
      assert Arrays.stream(types).allMatch(Objects::nonNull);
      this.muts = muts;
      this.names = names;
      this.types = types;
    }
    @Override public String toString() { return toDebugString(); }
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
  public static final class FunType implements Type {
    public final RecType paramsType;
    public final Type    resultType;
    private FunType(final RecType paramsType, final Type resultType) {
      assert paramsType != null;
      assert resultType != null;
      this.paramsType = paramsType;
      this.resultType = resultType;
    }
    @Override public String toString() { return toDebugString(); }
    @Override public void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append("Fun(");
      paramsType.toDebugString(buf, seen);
      buf.append(", ");
      resultType.toDebugString(buf, seen);
      buf.append(")");
    }
  }

  // Special types
  @SuppressWarnings("unused")
  public static final class UnitType extends RecType implements Type {
    private UnitType() { super(); }
    @Override public String toString() { return "Unit"; }
  }
  public static final class NameType implements Type {
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
    @Override public String toString() { return toDebugString(); }
    @Override public void toDebugString(final StringBuilder buf, final Set<NameType> seen) {
      buf.append("Name(");
      buf.append(name);
      buf.append(", ");
      if (seen.add(this)) type.toDebugString(buf, seen);
      else                buf.append("...");
      buf.append(")");
    }
  }

  // Virtual types (cannot be instantiated in memory)
  public static final UnkType unk = new UnkType();
  public static final ErrType err = new ErrType();

  // Primitive types (memory contains the value directly)
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

  // Reference types (memory contains a reference/pointer/address to the value)
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

  // Special Types
  public static final UnitType unit = new UnitType();
  public static final NameType name(final String name) {
    return new NameType(name);
  }
  public static final NameType name(final String name, final Type type) {
    return new NameType(name, type);
  }
}