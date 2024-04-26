package zfg.old.lang2;

/**
 *
 * primitives:
 * bit
 * u08
 * u16
 * u32
 * u64
 * i08
 * i16
 * i32
 * i64
 * f32
 * f64
 *
 * tup(arr(type))
 *
 * rec(arr(tup(bit, arr(u32), type)), type)
 *
 *
 */

import java.util.Objects;

public final class lang2 {
  private lang2() {}


  public static interface inst {
    public Type type();
  }
  public static interface Type extends inst {

  };

  // Type Type
  public static final Type Type = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "Type"; }
  };

  // Type Primitives
  public static final Type Bit = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "Bit"; }
  };
  public static final Type U08 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U08"; }
  };
  public static final Type U16 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U16"; }
  };
  public static final Type U32 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U32"; }
  };
  public static final Type U64 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "U64"; }
  };
  public static final Type I08 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I08"; }
  };
  public static final Type I16 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I16"; }
  };
  public static final Type I32 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I32"; }
  };
  public static final Type I64 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "I64"; }
  };
  public static final Type F32 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "F32"; }
  };
  public static final Type F64 = new Type() {
    @Override public Type type() { return Type; }
    @Override public String toString() { return "F64"; }
  };

  // public static final inst bit = new inst() {

  //   public Type type() { return Bit; }
  // };
  // public static final inst u08 = new inst() {
  //   public Type type() { return U08; }
  // };
  // public static final inst u16 = new inst() {
  //   public Type type() { return U16; }
  // };
  // public static final inst u32 = new inst() {
  //   public Type type() { return U32; }
  // };
  // public static final inst u64 = new inst() {
  //   public Type type() { return U64; }
  // };
  // public static final inst i08 = new inst() {
  //   public Type type() { return I08; }
  // };
  // public static final inst i16 = new inst() {
  //   public Type type() { return I16; }
  // };
  // public static final inst i32 = new inst() {
  //   public Type type() { return I32; }
  // };
  // public static final inst i64 = new inst() {
  //   public Type type() { return I64; }
  // };
  // public static final inst f32 = new inst() {
  //   public Type type() { return F32; }
  // };
  // public static final inst f64 = new inst() {
  //   public Type type() { return F64; }
  // };

  public static final class bit implements inst {
    private final int value;
    private bit(final int value) { this.value = value; }
    @Override public Type type() { return Bit; }
  }
  public static final class u08 implements inst {
    private final int value;
    private u08(final int value) { this.value = value; }
    @Override public Type type() { return U08; }
  }
  public static final class u16 implements inst {
    private final int value;
    private u16(final int value) { this.value = value; }
    @Override public Type type() { return U16; }
  }
  public static final class u32 implements inst {
    private final int value;
    private u32(final int value) { this.value = value; }
    @Override public Type type() { return U32; }
  }
  public static final class u64 implements inst {
    private final long value;
    private u64(final long value) { this.value = value; }
    @Override public Type type() { return U64; }
  }
  public static final class i08 implements inst {
    private final int value;
    @Override public Type type() { return I08; }
  }
  public static final class i16 implements inst {
    private final int value;
    private i16(final int value) { this.value = value; }
    @Override public Type type() { return I16; }
  }
  public static final class i32 implements inst {
    private final int value;
    private i32(final int value) { this.value = value; }
    @Override public Type type() { return I32; }
  }
  public static final class i64 implements inst {
    private final long value;
    private i64(final long value) { this.value = value; }
    @Override public Type type() { return I64; }
  }
  public static final class f32 implements inst {
    private final float value;
    private f32(final float value) { this.value = value; }
    @Override public Type type() { return F32; }
  }
  public static final class f64 implements inst {
    private final double value;
    private f64(final double value) { this.value = value; }
    @Override public Type type() { return F64; }
  }
  public static final class arr implements inst {
    private final inst[] value;
    private arr(final inst[] value) { this.value = value; }
    @Override public Type type() { return Type; } // TODO
  }
  public static final class rec implements inst {
    private final String[] keys;
    private final inst[]   vals;
    private rec(final String[] keys, final inst[] values) { this.value = value; }
    @Override public Type type() { return Type; } // TODO
  }
  public static final class fun implements inst {
    private final rec  params;
    private final type ret;
    private fun(final inst[] args, final inst ret) { this.args = args; this.ret = ret; }
    @Override public Type type() { return Type; } // TODO
  }

  public static final bit bit = new bit();
  public static final u08 u08 = new u08();
  public static final u16 u16 = new u16();
  public static final u32 u32 = new u32();
  public static final u64 u64 = new u64();
  public static final i08 i08 = new i08();
  public static final i16 i16 = new i16();
  public static final i32 i32 = new i32();
  public static final i64 i64 = new i64();
  public static final f32 f32 = new f32();
  public static final f64 f64 = new f64();


}
