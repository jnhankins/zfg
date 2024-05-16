package zfg.type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public sealed interface Type {

  @Override int hashCode();
  @Override boolean equals(final Object obj);
  @Override String toString();
  public StringBuilder toString(final StringBuilder sb);
  public StringBuilder toString(final StringBuilder sb, final Set<Object> seen);

  public static sealed abstract class Virtual implements Type {
    @Override public final int hashCode() { return System.identityHashCode(this); }
    @Override public final boolean equals(final Object obj) { return obj == this; }
    @Override public abstract String toString();
    @Override public final StringBuilder toString(final StringBuilder sb) { return sb.append(this); }
    @Override public final StringBuilder toString(final StringBuilder sb, final Set<Object> seen) { return sb.append(this); }
  }

  public static sealed abstract class Primitive implements Type {
    @Override public final int hashCode() { return System.identityHashCode(this); }
    @Override public final boolean equals(final Object obj) { return obj == this; }
    @Override public abstract String toString();
    @Override public final StringBuilder toString(final StringBuilder sb) { return sb.append(this); }
    @Override public final StringBuilder toString(final StringBuilder sb, final Set<Object> seen) { return sb.append(this); }
  }

  public static sealed abstract class Composite implements Type {
    @Override public abstract int hashCode();
    @Override public abstract boolean equals(final Object obj);
    @Override public final String toString() { return toString(new StringBuilder()).toString(); }
    @Override public final StringBuilder toString(final StringBuilder sb) { return toString(sb, new HashSet<>()); }
    @Override public abstract StringBuilder toString(final StringBuilder sb, final Set<Object> seen);
  }

  public static final class Err extends Virtual {
    public static final Err INSTANCE = new Err();
    private Err() {}
    @Override public String toString() { return "err"; }
  }

  public static final class Unk extends Virtual {
    public static final Unk INSTANCE = new Unk();
    private Unk() {}
    @Override public String toString() { return "unk"; }
  }

  public static final class Bit extends Primitive {
    public static final Bit INSTANCE = new Bit();
    private Bit() {}
    @Override public String toString() { return "bit"; }
  }

  public static final class U08 extends Primitive {
    public static final U08 INSTANCE = new U08();
    private U08() {}
    @Override public String toString() { return "u08"; }
  }

  public static final class U16 extends Primitive {
    public static final U16 INSTANCE = new U16();
    private U16() {}
    @Override public String toString() { return "u16"; }
  }

  public static final class U32 extends Primitive {
    public static final U32 INSTANCE = new U32();
    private U32() {}
    @Override public String toString() { return "u32"; }
  }

  public static final class U64 extends Primitive {
    public static final U64 INSTANCE = new U64();
    private U64() {}
    @Override public String toString() { return "u64"; }
  }

  public static final class I08 extends Primitive {
    public static final I08 INSTANCE = new I08();
    private I08() {}
    @Override public String toString() { return "i08"; }
  }

  public static final class I16 extends Primitive {
    public static final I16 INSTANCE = new I16();
    private I16() {}
    @Override public String toString() { return "i16"; }
  }

  public static final class I32 extends Primitive {
    public static final I32 INSTANCE = new I32();
    private I32() {}
    @Override public String toString() { return "i32"; }
  }

  public static final class I64 extends Primitive {
    public static final I64 INSTANCE = new I64();
    private I64() {}
    @Override public String toString() { return "i64"; }
  }

  public static final class F32 extends Primitive {
    public static final F32 INSTANCE = new F32();
    private F32() {}
    @Override public String toString() { return "f32"; }
  }

  public static final class F64 extends Primitive {
    public static final F64 INSTANCE = new F64();
    private F64() {}
    @Override public String toString() { return "f64"; }
  }

  public static final class Arr extends Composite {
    public static final int UNKNOWN_SIZE = -1;
    public final Type type;
    public final int  length;

    Arr(final Type type) {
      assert type != null;
      assert type != Err.INSTANCE;
      this.type = type;
      this.length = UNKNOWN_SIZE;
    }

    Arr(final Type type, final int length) {
      assert type != null;
      assert type != Err.INSTANCE;
      assert length == UNKNOWN_SIZE || length >= 0;
      this.type = type;
      this.length = length;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Arr.class,
        type,
        length
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return (this == obj || (
        obj instanceof Arr that &&
        this.type.equals(that.type) &&
        this.length == that.length
      ));
    }

    @Override
    public StringBuilder toString(final StringBuilder sb, final Set<Object> seen) {
      sb.append("Arr(");
      type.toString(sb, seen);
      if (length != UNKNOWN_SIZE) sb.append(", ").append(length);
      return sb.append(')');
    }
  }

  public static final class Tup extends Composite {
    public final boolean[] imuts;
    public final Type[]    types;

    Tup(final boolean[] imuts, final Type[] types) {
      assert imuts != null;
      assert imuts.length >= 1;
      assert types != null;
      assert types.length == imuts.length;
      assert Arrays.stream(types).noneMatch(Objects::isNull);
      assert Arrays.stream(types).noneMatch(Err.INSTANCE::equals);
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
      return (this == obj || (
        obj instanceof Tup that &&
        Arrays.equals(imuts, that.imuts) &&
        Arrays.equals(types, that.types)
      ));
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
      assert Arrays.stream(names).allMatch(zfg.Names::isLowerSnakeCase);
      assert Arrays.stream(names).distinct().count() == names.length;
      assert types != null;
      assert types.length == imuts.length;
      assert Arrays.stream(types).noneMatch(Objects::isNull);
      assert Arrays.stream(types).noneMatch(Err.INSTANCE::equals);
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
      return (this == obj || (
        obj instanceof Rec that &&
        Arrays.equals(this.imuts, that.imuts) &&
        Arrays.equals(this.names, that.names) &&
        Arrays.equals(this.types, that.types)
      ));
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
  }

  public static final class Fun extends Composite {
    public final Rec  paramsType;
    public final Type returnType;

    Fun(final Rec paramsType, final Type returnType) {
      assert paramsType != null;
      assert returnType != null;
      assert returnType != Err.INSTANCE;
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
      return (this == obj || (
        obj instanceof Fun that &&
        this.paramsType.equals(that.paramsType) &&
        this.returnType.equals(that.returnType)
      ));
    }

    @Override
    public StringBuilder toString(final StringBuilder sb, final Set<Object> seen) {
      paramsType.toString(sb, seen);
      sb.append(" ");
      returnType.toString(sb, seen);
      return sb;
    }
  }
}
