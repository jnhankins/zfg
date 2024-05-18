package zfg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public sealed interface Type {
  @Override public int hashCode();
  @Override public boolean equals(final Object obj);
  @Override public String toString();
  public default String toString(final boolean pretty) { return toString(); }
  public default void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(this); }
  public default boolean isUnit() { return false; }
  public default boolean isVirtual() { return this instanceof Virtual; }
  public default boolean isPrimitive() { return this instanceof Primitive; }
  public default boolean isComposite() { return this instanceof Composite; }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Virtual Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed abstract class Virtual implements Type {
    @Override public abstract String toString();
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
    @Override public abstract String toString();
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
    @Override public final String toString() { return toString(false); }
    @Override public final String toString(final boolean pretty) {
      final StringBuilder sb = new StringBuilder();
      appendTo(sb, pretty);
      return sb.toString();
    }
    @Override public final void appendTo(final StringBuilder sb, final boolean pretty) {
      Type.appendTo(this, sb, pretty);
    }
  }

  public static final class Arr extends Composite {
    public static final int UNKNOWN_LENGTH = -1;
    public final boolean mut;
    public final Type type;
    public final int length;

    Arr(final boolean imut, final Type type) {
      assert type != null;
      assert !type.isVirtual();
      this.mut = imut;
      this.type = type;
      this.length = UNKNOWN_LENGTH;
    }

    Arr(final boolean imut, final Type type, final int length) {
      assert type != null;
      assert !type.isVirtual();
      assert length >= 0;
      this.mut = imut;
      this.type = type;
      this.length = length;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Arr.class,
        mut,
        type,
        length
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Arr that &&
        this.mut == that.mut &&
        this.type.equals(that.type) &&
        this.length == that.length
      );
    }

    @Override
    public final boolean isUnit() {
      return length == 0;
    }
  }

  public static final class Tup extends Composite {
    public final boolean[] muts;
    public final Type[] types;

    Tup() {
      muts = new boolean[0];
      types = new Type[0];
    }

    Tup(final boolean[] muts, final Type[] types) {
      assert muts != null;
      assert muts.length >= 1;
      assert types != null;
      assert types.length == muts.length;
      assert Arrays.stream(types).noneMatch(Objects::isNull);
      assert Arrays.stream(types).noneMatch(t -> t.isVirtual());
      this.muts = muts;
      this.types = types;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Tup.class,
        Arrays.hashCode(muts),
        Arrays.hashCode(types)
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Tup that &&
        Arrays.equals(muts, that.muts) &&
        Arrays.equals(types, that.types)
      );
    }

    @Override
    public final boolean isUnit() {
      return muts.length == 0;
    }
  }

  public static final class Rec extends Composite {
    public final boolean[] muts;
    public final String[] names;
    public final Type[] types;

    Rec() {
      muts = new boolean[0];
      names = new String[0];
      types = new Type[0];
    }

    Rec(final boolean[] muts, final String[] names, final Type[] types) {
      assert muts != null;
      assert muts.length >= 1;
      assert names != null;
      assert names.length == muts.length;
      assert Arrays.stream(names).allMatch(Names::isLowerSnakeCase);
      assert Arrays.stream(names).distinct().count() == names.length;
      assert types != null;
      assert types.length == muts.length;
      assert Arrays.stream(types).noneMatch(Objects::isNull);
      assert Arrays.stream(types).noneMatch(t -> t.isVirtual());
      this.muts = muts;
      this.names = names;
      this.types = types;
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        Rec.class,
        Arrays.hashCode(muts),
        Arrays.hashCode(names),
        Arrays.hashCode(types)
      );
    }

    @Override
    public boolean equals(final Object obj) {
      return this == obj || (
        obj instanceof Rec that &&
        Arrays.equals(muts, that.muts) &&
        Arrays.equals(names, that.names) &&
        Arrays.equals(types, that.types)
      );
    }

    @Override
    public final boolean isUnit() {
      return muts.length == 0;
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
    public final boolean isUnit() {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Nominal (Alias) Type
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final class Nom implements Type {
    public final String name;
    public Type type;

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
      return toString(false);
    }

    @Override
    public final String toString(final boolean pretty) {
      final StringBuilder sb = new StringBuilder();
      appendTo(sb, pretty);
      return sb.toString();
    }

    @Override
    public final void appendTo(final StringBuilder sb, final boolean pretty) {
      Type.appendTo(this, sb, pretty);
    }

    @Override
    public final boolean isUnit() {
      return type.isUnit();
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Helpers
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private static void appendTo(
    final Type type,
    final StringBuilder sb,
    final boolean pretty
  ) {
    appendTo(type, sb, new HashSet<>(), pretty ? 0 : -1);
  }

  private static void appendTo(
    final Type type,
    final StringBuilder sb,
    final Set<Object> seen,
    final int indent
  ) {
    switch (type) {
      case Virtual   t -> sb.append(t);
      case Primitive t -> sb.append(t);
      case Arr t -> {
        // [let <type> ; <length>]
        sb.append('[');
        sb.append(t.mut ? "mut " : "let ");
        appendTo(t.type, sb, seen, indent <= 0 ? -1 : indent+1);
        if (t.length != Arr.UNKNOWN_LENGTH) {
          sb.append("; ");
          sb.append(t.length);
        }
        sb.append(']');
      }
      case Tup t -> {
        if (indent < 0) {
          // (let <type>, mut <type>, ...)
          sb.append('(');
          for (int i = 0; i < t.types.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(t.muts[i] ? "mut " : "let ");
            appendTo(t.types[i], sb, seen, -1);
          }
          sb.append(')');
        } else {
          // (\n
          // <indent+1> let <type>,\n
          // <indent+1> mut <type>,\n
          // <indent>)\n
          sb.append('(');
          for (int i = 0; i < t.types.length; i++) {
            appendIndent(sb, indent+1);
            sb.append(t.muts[i] ? "mut " : "let ");
            appendTo(t.types[i], sb, seen, indent+1);
            sb.append(',');
          }
          appendIndent(sb, indent);
        }
      }
      case Rec t -> {
        if (indent < 0) {
          // (let <name>: <type>, mut <name>: <type>, ...)
          sb.append('(');
          for (int i = 0; i < t.types.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(t.muts[i] ? "mut " : "let ");
            sb.append(t.names[i]).append(": ");
            appendTo(t.types[i], sb, seen, -1);
          }
          sb.append(')');
        } else {
          // (\n
          // <indent+1> let <name> <type>,\n
          // <indent+1> mut <name> <type>,\n
          // <indent>)\n
          sb.append('(');
          for (int i = 0; i < t.types.length; i++) {
            appendIndent(sb, indent+1);
            sb.append(t.muts[i] ? "mut " : "let ");
            sb.append(t.names[i]).append(": ");
            appendTo(t.types[i], sb, seen, indent+1);
            sb.append(',');
          }
          appendIndent(sb, indent);
        }
      }
      case Fun t -> {
        // <paramsType>: <returnType>
        appendTo(t.paramsType, sb, seen, indent <= 0 ? -1 : indent+1);
        sb.append(": ");
        appendTo(t.returnType, sb, seen, indent <= 0 ? -1 : indent+1);
      }
      case Nom n -> {
        // <name>=<type>
        sb.append(n.name);
        if (seen.add(n)) {
          sb.append('=');
          appendTo(n.type, sb, seen, indent <= 0 ? -1 : indent+1);
        }
      }
    }
  }

  private static void appendIndent(final StringBuilder sb, final int indent) {
    sb.ensureCapacity(sb.length() + indent + 1);
    sb.append('\n');
    for (int i = 0; i < indent; i++) sb.append(' ');
  }
}
