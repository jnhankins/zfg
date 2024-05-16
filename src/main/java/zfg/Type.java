package zfg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public sealed interface Type {
  @Override int hashCode();
  @Override boolean equals(final Object obj);
  @Override String toString();
  StringBuilder toString(final StringBuilder sb);
  StringBuilder toString(final StringBuilder sb, final Set<Object> seen);
  boolean isUnit();
  default boolean isVirtual() { return this instanceof Virtual; }
  default boolean isPrimitive() { return this instanceof Primitive; }
  default boolean isComposite() { return this instanceof Composite; }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Virtual Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed abstract class Virtual implements Type {
    @Override public final int hashCode() { return System.identityHashCode(this); }
    @Override public final boolean equals(final Object obj) { return obj == this; }
    @Override public abstract String toString();
    @Override public final StringBuilder toString(final StringBuilder sb) { return sb.append(this); }
    @Override public final StringBuilder toString(final StringBuilder sb, final Set<Object> seen) { return sb.append(this); }
    @Override public final boolean isUnit() { return false; }
  }

  public static final class Err extends Virtual {
    Err() {}
    @Override public String toString() { return "err"; }
  }

  public static final class Unk extends Virtual {
    Unk() {}
    @Override public String toString() { return "unk"; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Primitive Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed abstract class Primitive implements Type {
    @Override public final int hashCode() { return System.identityHashCode(this); }
    @Override public final boolean equals(final Object obj) { return obj == this; }
    @Override public abstract String toString();
    @Override public final StringBuilder toString(final StringBuilder sb) { return sb.append(this); }
    @Override public final StringBuilder toString(final StringBuilder sb, final Set<Object> seen) { return sb.append(this); }
    @Override public final boolean isUnit() { return false; }
  }

  public static final class Bit extends Primitive {
    Bit() {}
    @Override public String toString() { return "bit"; }
  }

  public static final class U08 extends Primitive {
    U08() {}
    @Override public String toString() { return "u08"; }
  }

  public static final class U16 extends Primitive {
    U16() {}
    @Override public String toString() { return "u16"; }
  }

  public static final class U32 extends Primitive {
    U32() {}
    @Override public String toString() { return "u32"; }
  }

  public static final class U64 extends Primitive {
    U64() {}
    @Override public String toString() { return "u64"; }
  }

  public static final class I08 extends Primitive {
    I08() {}
    @Override public String toString() { return "i08"; }
  }

  public static final class I16 extends Primitive {
    I16() {}
    @Override public String toString() { return "i16"; }
  }

  public static final class I32 extends Primitive {
    I32() {}
    @Override public String toString() { return "i32"; }
  }

  public static final class I64 extends Primitive {
    I64() {}
    @Override public String toString() { return "i64"; }
  }

  public static final class F32 extends Primitive {
    F32() {}
    @Override public String toString() { return "f32"; }
  }

  public static final class F64 extends Primitive {
    F64() {}
    @Override public String toString() { return "f64"; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Composite Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed abstract class Composite implements Type {
    @Override public abstract int hashCode();
    @Override public abstract boolean equals(final Object obj);
    @Override public final String toString() { return toString(new StringBuilder()).toString(); }
    @Override public final StringBuilder toString(final StringBuilder sb) { return toString(sb, new HashSet<>()); }
    @Override public abstract StringBuilder toString(final StringBuilder sb, final Set<Object> seen);
  }

  public static final class Arr extends Composite {
    public static final int UNKNOWN_LENGTH = -1;
    public final boolean imut;
    public final Type    type;
    public final int     length;

    Arr(final boolean imut, final Type type) {
      assert type != null;
      assert !type.isVirtual();
      this.imut = imut;
      this.type = type;
      this.length = UNKNOWN_LENGTH;
    }

    Arr(final boolean imut, final Type type, final int length) {
      assert type != null;
      assert !type.isVirtual();
      assert length >= 0;
      this.imut = imut;
      this.type = type;
      this.length = length;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Arr.class,
        imut,
        type,
        length
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Arr that &&
        this.imut == that.imut &&
        this.type.equals(that.type) &&
        this.length == that.length
      );
    }

    @Override
    public StringBuilder toString(final StringBuilder sb, final Set<Object> seen) {
      sb.append("Arr(");
      sb.append(imut ? "let " : "mut ");
      type.toString(sb, seen);
      if (length != UNKNOWN_LENGTH) sb.append(", ").append(length);
      return sb.append(')');
    }

    @Override
    public final boolean isUnit() {
      return length == 0;
    }
  }

  public static final class Tup extends Composite {
    public final boolean[] imuts;
    public final Type[]    types;

    Tup() {
      imuts = new boolean[0];
      types = new Type[0];
    }

    Tup(final boolean[] imuts, final Type[] types) {
      assert imuts != null;
      assert imuts.length >= 1;
      assert types != null;
      assert types.length == imuts.length;
      assert Arrays.stream(types).noneMatch(Objects::isNull);
      assert Arrays.stream(types).noneMatch(t -> t.isVirtual());
      this.imuts = imuts;
      this.types = types;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Tup.class,
        Arrays.hashCode(imuts),
        Arrays.hashCode(types)
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Tup that &&
        Arrays.equals(imuts, that.imuts) &&
        Arrays.equals(types, that.types)
      );
    }

    @Override
    public StringBuilder toString(final StringBuilder sb, final Set<Object> seen) {
      sb.append('(');
      for (int i = 0; i < types.length; i++) {
        if (i > 0) sb.append(", ");
        sb.append(imuts[i] ? "let " : "mut ");
        types[i].toString(sb, seen);
      }
      sb.append(')');
      return sb;
    }

    @Override
    public final boolean isUnit() {
      return imuts.length == 0;
    }
  }

  public static final class Rec extends Composite {
    public final boolean[] imuts;
    public final String[]  names;
    public final Type[]    types;

    Rec() {
      imuts = new boolean[0];
      names = new String[0];
      types = new Type[0];
    }

    Rec(final boolean[] imuts, final String[] names, final Type[] types) {
      assert imuts != null;
      assert imuts.length >= 1;
      assert names != null;
      assert names.length == imuts.length;
      assert Arrays.stream(names).allMatch(Names::isLowerSnakeCase);
      assert Arrays.stream(names).distinct().count() == names.length;
      assert types != null;
      assert types.length == imuts.length;
      assert Arrays.stream(types).noneMatch(Objects::isNull);
      assert Arrays.stream(types).noneMatch(t -> t.isVirtual());
      this.imuts = imuts;
      this.names = names;
      this.types = types;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Rec.class,
        Arrays.hashCode(imuts),
        Arrays.hashCode(names),
        Arrays.hashCode(types)
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Rec that &&
        Arrays.equals(imuts, that.imuts) &&
        Arrays.equals(names, that.names) &&
        Arrays.equals(types, that.types)
      );
    }

    @Override
    public StringBuilder toString(final StringBuilder sb, final Set<Object> seen) {
      sb.append('(');
      for (int i = 0; i < types.length; i++) {
        if (i > 0) sb.append(", ");
        sb.append(imuts[i] ? "let " : "mut ");
        sb.append(names[i]).append(" ");
        types[i].toString(sb, seen);
      }
      sb.append(')');
      return sb;
    }

    @Override
    public final boolean isUnit() {
      return imuts.length == 0;
    }
  }

  public static final class Fun extends Composite {
    public final Type paramsType;
    public final Type returnType;

    Fun() {
      paramsType = Types.UNK;
      returnType = Types.UNK;
    }

    Fun(final Type paramsType, final Type returnType) {
      assert paramsType != null;
      assert paramsType instanceof Rec || paramsType instanceof Tup;
      assert returnType != null;
      assert !returnType.isVirtual();
      this.paramsType = paramsType;
      this.returnType = returnType;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Fun.class,
        paramsType,
        returnType
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Fun that &&
        this.paramsType.equals(that.paramsType) &&
        this.returnType.equals(that.returnType)
      );
    }

    @Override
    public StringBuilder toString(final StringBuilder sb, final Set<Object> seen) {
      paramsType.toString(sb, seen);
      sb.append(" ");
      returnType.toString(sb, seen);
      return sb;
    }

    @Override
    public final boolean isUnit() {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Nominal (Alias) Type
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class Nom implements Type {
    public final String name;
    public       Type   type;

    Nom(final String name) {
      assert name != null;
      assert Names.isUpperCamelCase(name);
      this.name = name;
      this.type = Types.UNK;
    }

    Nom(final String name, final Type type) {
      assert name != null;
      assert Names.isUpperCamelCase(name);
      assert type != null;
      assert !type.isVirtual();
      this.name = name;
      this.type = type;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Nom.class,
        name
      );
    }

    public void bind(final Type type) {
      assert type != null;
      assert !type.isVirtual();
      this.type = type;
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Nom that &&
        this.name.equals(that.name)
      );
    }

    @Override
    public final String toString() {
      return toString(new StringBuilder()).toString();
    }

    @Override
    public final StringBuilder toString(final StringBuilder sb) {
      return toString(sb, new HashSet<>());
    }

    @Override
    public StringBuilder toString(final StringBuilder sb, final Set<Object> seen) {
      sb.append(name);
      if (seen.add(this)) {
        sb.append("=");
        type.toString(sb, seen);
      }
      return sb;
    }

    @Override
    public final boolean isUnit() {
      return type.isUnit();
    }
  }
}
