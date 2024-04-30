package zfg.old.lang2;

import java.util.Objects;

public final class lang {
  private lang() {}

  // public static interface Inst {
  //   public Type type();
  // }
  // public static interface Type extends Inst {
  //   public default Type type() { return this; }
  // }

  // private static final class Bit implements Type<Bit> {
  //   public Bit type() { return this; }
  // }
  // public static final Type<?> bit = new Type<Bit>() {
  //   public Bit type() { return Bit.INSTANCE; }
  // };

  // type is a inst of type and a type of type
  // inst is a inst of type and a type of inst

  // Bit is a inst of type and a type of type
  // bit is a inst of Bit  and a type of inst

  public static interface inst {
    public Type type();
  }
  public static interface Type extends inst {
    public boolean isAssignableTo(final Type That);
  };

  // Type Type
  public static final Type Type = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "Type"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this; }
  };

  // Type Primitives
  public static final Type Bit = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "Bit"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == U08 || That == U16 || That == U32 || That == U64 || That == I08 || That == I16 || That == I32 || That == I64 || That == F32 || That == F64; }
  };
  public static final Type U08 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U08"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == U16 || That == U32 || That == U64 || That == I16 || That == I32 || That == I64 || That == F32 || That == F64; }
  };
  public static final Type U16 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U16"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == U32 || That == U64 || That == I32 || That == I64 || That == F32 || That == F64; }
  };
  public static final Type U32 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U32"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == U64 || That == I64 || That == F32 || That == F64; }
  };
  public static final Type U64 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U64"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == F32 || That == F64; }
  };
  public static final Type I08 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I08"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == I16 || That == I32 || That == I64 || That == F32 || That == F64; }
  };
  public static final Type I16 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I16"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == I32 || That == I64 || That == F32 || That == F64; }
  };
  public static final Type I32 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I32"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == I64 || That == F32 || That == F64; }
  };
  public static final Type I64 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I64"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == F32 || That == F64; }
  };
  public static final Type F32 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "F32"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this || That == F64; }
  };
  public static final Type F64 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "F64"; }
    @Override public boolean isAssignableTo(final Type That) { return That == this; }
  };

  public static final inst bit = new inst() {

    public Type type() { return Bit; }
  };
  public static final inst u08 = new inst() {
    public Type type() { return U08; }
  };
  public static final inst u16 = new inst() {
    public Type type() { return U16; }
  };
  public static final inst u32 = new inst() {
    public Type type() { return U32; }
  };
  public static final inst u64 = new inst() {
    public Type type() { return U64; }
  };
  public static final inst i08 = new inst() {
    public Type type() { return I08; }
  };
  public static final inst i16 = new inst() {
    public Type type() { return I16; }
  };
  public static final inst i32 = new inst() {
    public Type type() { return I32; }
  };
  public static final inst i64 = new inst() {
    public Type type() { return I64; }
  };
  public static final inst f32 = new inst() {
    public Type type() { return F32; }
  };
  public static final inst f64 = new inst() {
    public Type type() { return F64; }
  };

  private static final class Arr implements Type {
    private static final int UNKNOWN_LENGTH = -1;
    private final Type element;
    private final int length;
    private Arr(final Type element) { this(element, UNKNOWN_LENGTH); }
    private Arr(final Type element, final int length) {
      if (!(element != null)) throw new AssertionError();
      if (!(length >= 0 || length == UNKNOWN_LENGTH)) throw new AssertionError();
      this.element = element;
      this.length = length;
    }
    @Override public Type type() {
      return Type;
    }
    @Override public String toString() {
      return length == UNKNOWN_LENGTH
        ? "Arr(" + element + ")"
        : "Arr(" + element + "," + length + ")";
    }
    @Override public int hashCode() {
      return Objects.hash(element, length);
    }
    @Override public boolean equals(final Object that) {
      return this == that || (that instanceof Arr thatArr &&
        thatArr.element.equals(element) &&
        thatArr.length == length
      );
    }
    @Override public boolean isAssignableTo(final Type That) {
      return this == That || (That instanceof Arr thatArr &&
        thatArr.element.equals(element) &&
        (thatArr.length == length || thatArr.length == UNKNOWN_LENGTH)
      );
    }
  }
  public static final Type Arr(final Type element) {
    return new Arr(element);
  }
  public static final Type Arr(final Type element, final int length) {
    return new Arr(element, length);
  }
  public static final inst arr(final Type element) {
    return new inst() {
      public Type type() { return Arr(element); }
    };
  };
  public static final inst arr(final Type element, final int length) {
    return new inst() {
      public Type type() { return Arr(element, length); }
    };
  };

  public static final class Tup implements Type {
    private final Type[] elements;
    private Tup(final Type[] elements) {
      if (!(elements != null)) throw new AssertionError();
      this.elements = elements;
    }
    @Override public Type type() {
      return Type;
    }
    @Override public String toString() {
      final StringBuilder buf = new StringBuilder("Tup(");
      for (int i = 0; i < elements.length; i++) {
        if (i > 0) buf.append(",");
        buf.append(elements[i]);
      }
      buf.append(")");
      return buf.toString();
    }
    @Override public int hashCode() {
      return Objects.hash((Object[]) elements);
    }
    @Override public boolean equals(final Object that) {
      return this == that || (that instanceof Tup thatTup &&
        java.util.Arrays.equals(thatTup.elements, elements)
      );
    }
    @Override public boolean isAssignableTo(final Type That) {
      return this == That || (That instanceof Tup thatTup &&
        thatTup.elements.length == elements.length &&
        java.util.Arrays.equals(thatTup.elements, elements)
      );
    }
  }

  // Rec: arr(rec(let mod, let name, let type)) -> ... Rec(arr.0.mod arr.0.name arr.0.type, ...)
  public static final Type Rec(final inst fields) {
    if (!(fields != null)) throw new AssertionError();
    return new Rec(fields);
  }
}
