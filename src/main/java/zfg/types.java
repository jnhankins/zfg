package zfg;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class types {
  private types() {}

  public static enum Kind {
    // Virtual Types (not instantiable)
    UNK, ERR,
    // Primitive Data Types (pass by value)
    BIT, U08, U16, U32, U64, I08, I16, I32, I64, F32, F64,
    // Composite Data Types and Function Type (pass by reference)
    ARR, TUP, REC, FUN, NOM;
  }

  public static sealed interface Type {
    public Kind kind();
    public default String descriptor() { return getDescriptor(this); };
    public boolean isAssignableTo(final Type type);
    @Override public int hashCode();
    @Override public boolean equals(final Object obj);
    @Override public String toString();
    public void toString(final StringBuilder sb);
    public void toString(final StringBuilder sb, final Set<Type> seen);
  }

  public static final class UnkType implements Type {
    private UnkType() {}
    @Override public Kind kind() { return Kind.UNK; }
    @Override public boolean isAssignableTo(final Type type) { throw new UnsupportedOperationException(); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "Unk"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class ErrType implements Type {
    private ErrType() {}
    @Override public Kind kind() { return Kind.ERR; }
    @Override public boolean isAssignableTo(final Type type) { throw new UnsupportedOperationException(); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "Err"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class BitType implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.BIT);
    private BitType() {}
    @Override public Kind kind() { return Kind.BIT; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public String toString() { return "Bit"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class U08Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.U08, Kind.U16, Kind.U32, Kind.U64, Kind.I16, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    private U08Type() {}
    @Override public Kind kind() { return Kind.U08; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "U08"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class U16Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.U16, Kind.U32, Kind.U64, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    private U16Type() {}
    @Override public Kind kind() { return Kind.U16; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "U16"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class U32Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.U32, Kind.U64, Kind.I64, Kind.F64);
    private U32Type() {}
    @Override public Kind kind() { return Kind.U32; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "U32"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class U64Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.U64);
    private U64Type() {}
    @Override public Kind kind() { return Kind.U64; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "U64"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class I08Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I08, Kind.I16, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    private I08Type() {}
    @Override public Kind kind() { return Kind.I08; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "I08"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class I16Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I16, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    private I16Type() {}
    @Override public Kind kind() { return Kind.I16; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "I16"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class I32Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I32, Kind.I64, Kind.F64);
    private I32Type() {}
    @Override public Kind kind() { return Kind.I32; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "I32"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class I64Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I64);
    private I64Type() {}
    @Override public Kind kind() { return Kind.I64; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "I64"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class F32Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.F32, Kind.F64);
    private F32Type() {}
    @Override public Kind kind() { return Kind.F32; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "F32"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class F64Type implements Type {
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.F64);
    private F64Type() {}
    @Override public Kind kind() { return Kind.F64; }
    @Override public boolean isAssignableTo(final Type type) { return ASSIGNABLE.contains(type.kind()); }
    @Override public int hashCode() { return System.identityHashCode(this); }
    @Override public boolean equals(final Object obj) { return this == obj; }
    @Override public String toString() { return "F64"; }
    @Override public void toString(final StringBuilder sb) { sb.append(this); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) { sb.append(this); }
  };

  public static final class ArrType implements Type {
    private static final EnumSet<Kind> SIZED = EnumSet.of(Kind.BIT, Kind.U08, Kind.U16, Kind.U32, Kind.U64, Kind.I08, Kind.I16, Kind.I32, Kind.I64, Kind.F32, Kind.F64 );
    public static final int UNKNOWN_LENGTH = -1;
    public final Type elementType;
    public final int length;
    private ArrType(final Type type) {
      assert type != null && type != Err && type != Unk;
      this.elementType = type;
      this.length = UNKNOWN_LENGTH;
    }
    private ArrType(final Type type, final int length) {
      assert type != null && type != Err && type != Unk;
      assert length >= 0;
      this.elementType = type;
      this.length = length;
    }
    @Override public Kind kind() { return Kind.ARR; }
    @Override public boolean isAssignableTo(final Type type) {
      // The other type must be an array
      if (!(type instanceof ArrType)) return false;
      final ArrType that = (ArrType) type;
      // Either the other array must have an unknown length or the lengths must match.
      // Otherwise the other array requires a sepcific length this array cannot guarantee.
      if (that.length != UNKNOWN_LENGTH && length != that.length) return false;
      // If the element type is a value type, it must match exactly.
      // This is because we have to use type-specific jvm instructions to access array
      if (SIZED.contains(elementType.kind())) return elementType.equals(that.elementType);
      // If the element type is a reference type, it must be assignable to the other element type.
      return elementType.isAssignableTo(that.elementType);
    }
    @Override public int hashCode() {
      return Objects.hash(
        ArrType.class,
        elementType,
        length
      );
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (
        obj instanceof ArrType that &&
        elementType.equals(that.elementType) &&
        length == that.length
      ));
    }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashSet<>()); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) {
      sb.append("Arr(");
      elementType.toString(sb, seen);
      if (length != UNKNOWN_LENGTH) {
        sb.append("; ");
        sb.append(length);
      }
      sb.append(")");
    }
  }

  public static final class TupType implements Type {
    public final boolean[] muts;
    public final Type[]    types;
    private TupType() {
      this.muts = new boolean[0];
      this.types = new Type[0];
    }
    private TupType(final boolean[] muts, final Type[] types) {
      assert muts != null;
      assert types != null;
      assert types.length == muts.length;
      assert Arrays.stream(types).allMatch(t -> t != null && t != Err && t != Unk);
      this.muts = muts;
      this.types = types;
    }
    @Override public Kind kind() { return Kind.TUP; }
    @Override public boolean isAssignableTo(final Type type) {
      // TODO
      throw new UnsupportedOperationException();
    }
    @Override public int hashCode() {
      return Objects.hash(
        TupType.class,
        Arrays.hashCode(muts),
        Arrays.hashCode(types)
      );
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (
        obj instanceof TupType that &&
        Arrays.equals(muts, that.muts) &&
        Arrays.equals(types, that.types)
      ));
    }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashSet<>()); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) {
      sb.append("(");
      for (int i = 0; i < types.length; i++) {
        if (i > 0) sb.append(", ");
        sb.append(muts[i] ? "mut " : "let ");
        types[i].toString(sb, seen);
      }
      sb.append(")");
    }
  }

  public static final class RecType implements Type {
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
      assert muts.length >= 0;
      assert names != null;
      assert names.length == muts.length;
      assert Arrays.stream(names).allMatch(zfg.names::isLowerSnakeCase);
      assert Arrays.stream(names).distinct().count() == names.length;
      assert types != null;
      assert types.length == muts.length;
      assert Arrays.stream(types).allMatch(t -> t != null && t != Err && t != Unk);
      this.muts = muts;
      this.names = names;
      this.types = types;
    }
    @Override public Kind kind() { return Kind.REC; }
    @Override public boolean isAssignableTo(final Type type) {
      // TODO
      throw new UnsupportedOperationException();
    }
    @Override public int hashCode() {
      return Objects.hash(
        RecType.class,
        Arrays.hashCode(muts),
        Arrays.hashCode(names),
        Arrays.hashCode(types)
      );
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (
        obj instanceof RecType that &&
        Arrays.equals(muts, that.muts) &&
        Arrays.equals(names, that.names) &&
        Arrays.equals(types, that.types)
      ));
    }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashSet<>()); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) {
      sb.append("(");
      for (int i = 0; i < types.length; i++) {
        if (i > 0) sb.append(", ");
        sb.append(muts[i] ? "mut " : "let ");
        sb.append(names[i]);
        sb.append(" ");
        types[i].toString(sb, seen);
      }
      sb.append(")");
    }
  }

  public static final class FunType implements Type {
    public final RecType paramsType;
    public final Type returnType;
    private FunType(final RecType paramsType, final Type returnType) {
      assert paramsType != null;
      assert returnType != null && returnType != Err && returnType != Unk;
      this.paramsType = paramsType;
      this.returnType = returnType;
    }
    @Override public Kind kind() { return Kind.FUN; }
    @Override public boolean isAssignableTo(final Type type) {
      // TODO
      throw new UnsupportedOperationException();
    }
    @Override public int hashCode() {
      return Objects.hash(
        FunType.class,
        paramsType,
        returnType
      );
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (
        obj instanceof FunType that &&
        paramsType.equals(that.paramsType) &&
        returnType.equals(that.returnType)
      ));
    }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashSet<>()); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) {
      paramsType.toString(sb, seen);
      sb.append("=>");
      returnType.toString(sb, seen);
    }
  }

  public static final class NomType implements Type {
    public final String fqn;
    public final Type aliasedType;
    private NomType(final String fqn) {
      assert fqn != null;
      assert names.isUpperCamelCase(fqn); // TODO fqn with path components
      this.fqn = fqn;
      this.aliasedType = Unk;
    }
    private NomType(final String fqn, final Type aliasedType) {
      assert fqn != null;
      assert names.isUpperCamelCase(fqn); // TODO fqn with path components
      assert aliasedType != null && aliasedType != Err && aliasedType != Unk;
      this.fqn = fqn;
      this.aliasedType = aliasedType;
    }
    @Override public Kind kind() { return Kind.NOM; }
    @Override public boolean isAssignableTo(final Type type) {
      // TODO
      throw new UnsupportedOperationException();
    }
    @Override public int hashCode() {
      return Objects.hash(
        NomType.class,
        fqn
      );
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (
        obj instanceof NomType that &&
        fqn.equals(that.fqn)
      ));
    }
    @Override public String toString() { final StringBuilder sb = new StringBuilder(); toString(sb); return sb.toString(); }
    @Override public void toString(final StringBuilder sb) { toString(sb, new HashSet<>()); }
    @Override public void toString(final StringBuilder sb, final Set<Type> seen) {
      sb.append("<");
      sb.append(fqn);
      sb.append(">");
      if (seen.add(this)) aliasedType.toString(sb, seen);
    }
  }


  public static final UnkType Unk = new UnkType();
  public static final ErrType Err = new ErrType();
  public static final BitType Bit = new BitType();
  public static final U08Type U08 = new U08Type();
  public static final U16Type U16 = new U16Type();
  public static final U32Type U32 = new U32Type();
  public static final U64Type U64 = new U64Type();
  public static final I08Type I08 = new I08Type();
  public static final I16Type I16 = new I16Type();
  public static final I32Type I32 = new I32Type();
  public static final I64Type I64 = new I64Type();
  public static final F32Type F32 = new F32Type();
  public static final F64Type F64 = new F64Type();
  public static final RecType Unit = new RecType();

  public static final class TypeCache {
    private final Map<Type, Type> cache = new HashMap<>();

    public TypeCache() {}

    public final Type Arr(final boolean mut, final Type elementType) {
      final Type type = new ArrType(mut, elementType);
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Arr(final boolean mut, final Type elementType, final int length) {
      final Type type = new ArrType(mut, elementType, length);
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Tup(final boolean[] muts, final Type[] types) {
      final Type type = withoutUnitFields(new TupType(muts, types));
      if (type == null) return Unit;
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Rec(final boolean[] muts, final String[] names, final Type[] types) {
      final Type type = withoutUnitFields(new RecType(muts, names, types));
      if (type == null) return Unit;
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Fun(final RecType paramsType, final Type resultType) {
      final Type type = new FunType(paramsType, resultType);
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Nom(final String fqn) {
      final Type type = new NomType(fqn);
      assert !cache.containsKey(type);
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Nom(final String fqn, final Type aliasedType) {
      final Type type = new NomType(fqn, aliasedType);
      assert !cache.containsKey(type);
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
  }

  private static final TupType withoutUnitFields(final TupType tup) {
    final boolean[] muts  = tup.muts;
    final Type[]    types = tup.types;
    final int len = muts.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i] == Unit) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (types[j] != Unit) {
            muts[i] = muts[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return null;
        // Otherwise, return a new tuple without the unit fields
        return new TupType(
          Arrays.copyOf(muts, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return tup;
  }

  private static final RecType withoutUnitFields(final RecType rec) {
    final boolean[] muts  = rec.muts;
    final String[]  names = rec.names;
    final Type[]    types = rec.types;
    final int len = muts.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i] == Unit) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (types[j] != Unit) {
            muts[i] = muts[j];
            names[i] = names[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return null;
        // Otherwise, return a new record without the unit fields
        return new RecType(
          Arrays.copyOf(muts, i),
          Arrays.copyOf(names, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return rec;
  }

  private static final String getDescriptor(Type type) {
    while (type instanceof NomType nom) type = nom.aliasedType;
    return switch (type.kind()) {
      case BIT -> "Z";
      case U08 -> "B"; // no native unsigned byte type in JVM, use signed byte
      case U16 -> "C";
      case U32 -> "I";
      case U64 -> "J"; // no native unsigned long type in JVM, use signed long
      case I08 -> "B";
      case I16 -> "S";
      case I32 -> "I";
      case I64 -> "J";
      case F32 -> "F";
      case F64 -> "D";
      case ARR -> "[" + getArrayDescriptor(((ArrType) type).elementType);
      case FUN -> {
        final FunType fun = (FunType) type;
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < fun.paramsType.types.length; i++)
          sb.append(getErasureDescriptor(fun.paramsType.types[i]));
        sb.append(")");
        if (fun.returnType == Unit) sb.append("V");
        else sb.append(getErasureDescriptor(fun.returnType));
        yield sb.toString();

      }
      case TUP -> {
        final TupType tup = (TupType) type;
        final StringBuilder sb = new StringBuilder();
        sb.append("Lzfg.Tup");
        for (int i = 0; i < tup.types.length; i++)
          sb.append(getErasureDescriptor(tup.types[i]));
        sb.append(";");
        yield sb.toString();
      }
      case REC -> {
        final RecType rec = (RecType) type;
        final StringBuilder sb = new StringBuilder();
        sb.append("Lzfg.Rec");
        for (int i = 0; i < rec.types.length; i++)
          sb.append(getErasureDescriptor(rec.types[i]));
        sb.append(";");
        yield sb.toString();
      }
      case NOM -> throw new AssertionError();
      case UNK, ERR -> throw new UnsupportedOperationException();
    };
  }

  private static final String getArrayDescriptor(Type type) {
    while (type instanceof NomType nom) type = nom.aliasedType;
    return switch (type.kind()) {
      case BIT -> "Z";
      case U08 -> "B"; // no native unsigned byte type in JVM, use signed byte
      case U16 -> "C";
      case U32 -> "I";
      case U64 -> "J"; // no native unsigned long type in JVM, use signed long
      case I08 -> "B";
      case I16 -> "S";
      case I32 -> "I";
      case I64 -> "J";
      case F32 -> "F";
      case F64 -> "D";
      case ARR, TUP, REC, FUN -> "Ljava/lang/Object;";
      case NOM -> throw new AssertionError();
      case UNK, ERR -> throw new UnsupportedOperationException();
    };
  }

  private static final String getErasureDescriptor(Type type) {
    while (type instanceof NomType nom) type = nom.aliasedType;
    return switch (type.kind()) {
      case BIT, U08, I08 -> "B";
      case U16, I16 -> "S";
      case U32, I32 -> "I";
      case U64, I64 -> "J";
      case F32 -> "F";
      case F64 -> "D";
      case ARR, TUP, REC, FUN  -> "A";
      case NOM -> throw new AssertionError();
      case UNK, ERR -> throw new UnsupportedOperationException();
    };
  }
}
