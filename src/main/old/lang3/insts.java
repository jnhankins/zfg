package zfg.old.lang3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import zfg.old.lang3.types.ArrType;
import zfg.old.lang3.types.BitType;
import zfg.old.lang3.types.F32Type;
import zfg.old.lang3.types.F64Type;
import zfg.old.lang3.types.FunType;
import zfg.old.lang3.types.I08Type;
import zfg.old.lang3.types.I16Type;
import zfg.old.lang3.types.I32Type;
import zfg.old.lang3.types.I64Type;
import zfg.old.lang3.types.NameType;
import zfg.old.lang3.types.RecType;
import zfg.old.lang3.types.Type;
import zfg.old.lang3.types.U08Type;
import zfg.old.lang3.types.U16Type;
import zfg.old.lang3.types.U32Type;
import zfg.old.lang3.types.U64Type;

public final class insts {
  private insts() {}

  public static sealed interface Inst permits
      // Primitive instances (passed by value)
      BitInst, U08Inst, U16Inst, U32Inst, U64Inst, I08Inst, I16Inst, I32Inst, I64Inst, F32Inst, F64Inst,
      // Reference instances (passed by reference)
      ArrInst, // Data structure whose elements are accessed by index
      RecInst, // Data structure whose elements are accessed by name keys
      FunInst, // Function
      // Special instances
      UnitInst, // Empty record of zero size
      NameInst  // Alias for a primitive, reference, or unit type and adds methods and type bounds
  {
    public Type type();

    public default String toDebugString() {
      final StringBuilder buf = new StringBuilder();
      toDebugString(buf, new HashMap<>());
      return buf.toString();
    }
    public default void toDebugString(final StringBuilder buf, final Map<Inst, Integer> seen) {
      buf.append(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Virtual Type Instances (cannot be instantiated in memory; doesn't exist at runtime)
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Cannot have runtime instances of virtual types

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Primitive Type Instances (passed by value)
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** 1-bit unsigned integer (boolean, true or false, 0 or 1) */
  public static final class BitInst implements Inst {
    public final int value;
    private BitInst(final int value) {
      assert value == 0 || value == 1;
      this.value = value;
    }
    @Override public BitType type() { return types.bit; }
    @Override public String toString() { return "bit(" + value +")" ; }
  }

  /** 8-bit unsigned integer */
  public static final class U08Inst implements Inst {
    public final int value;
    private U08Inst(final int value) {
      assert 0 <= value && value <= 0xff;
      this.value = value;
    }
    @Override public U08Type type() { return types.u08; }
    @Override public String toString() { return "u08(" + value +")" ; }
  }

  /** 16-bit unsigned integer */
  public static final class U16Inst implements Inst {
    public final int value;
    private U16Inst(final int value) {
      assert 0 <= value && value <= 0xffff;
      this.value = value;
    }
    @Override public U16Type type() { return types.u16; }
    @Override public String toString() { return "u16(" + value +")" ; }
  }

  /** 32-bit unsigned integer */
  public static final class U32Inst implements Inst {
    public final int value;
    private U32Inst(final int value) { this.value = value; }
    @Override public U32Type type() { return types.u32; }
    @Override public String toString() { return "u32(" + Integer.toUnsignedString(value) +")" ; }
  }

  /** 64-bit unsigned integer */
  public static final class U64Inst implements Inst {
    public final long value;
    private U64Inst(final long value) { this.value = value; }
    @Override public U64Type type() { return types.u64; }
    @Override public String toString() { return "u64(" + Long.toUnsignedString(value) +")" ; }
  }

  /** 8-bit signed integer */
  public static final class I08Inst implements Inst {
    public final int value;
    private I08Inst(final int value) {
      assert -0x80 <= value && value <= 0x7f;
      this.value = value;
    }
    @Override public I08Type type() { return types.i08; }
    @Override public String toString() { return "i08(" + value +")" ; }
  }

  /** 16-bit signed integer */
  public static final class I16Inst implements Inst {
    public final int value;
    private I16Inst(final int value) {
      assert -0x8000 <= value && value <= 0x7fff;
      this.value = value;
    }
    @Override public I16Type type() { return types.i16; }
    @Override public String toString() { return "i16(" + value +")" ; }
  }

  /** 32-bit signed integer */
  public static final class I32Inst implements Inst {
    public final int value;
    private I32Inst(final int value) { this.value = value; }
    @Override public I32Type type() { return types.i32; }
    @Override public String toString() { return "i32(" + value +")" ; }
  }

  /** 64-bit signed integer */
  public static final class I64Inst implements Inst {
    public final long value;
    private I64Inst(final long value) { this.value = value; }
    @Override public I64Type type() { return types.i64; }
    @Override public String toString() { return "i64(" + value +")" ; }
  }

  /** 32-bit floating-point number */
  public static final class F32Inst implements Inst {
    public final float value;
    private F32Inst(final float value) { this.value = value; }
    @Override public F32Type type() { return types.f32; }
    @Override public String toString() { return "f32(" + value +")" ; }
  }

  /** 64-bit floating-point number */
  public static final class F64Inst implements Inst {
    public final double value;
    private F64Inst(final double value) { this.value = value; }
    @Override public F64Type type() { return types.f64; }
    @Override public String toString() { return "f64(" + value +")" ; }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Reference Type Instances (passed by reference)
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Array instance */
  public static final class ArrInst implements Inst {
    private final ArrType type;
    public final Inst[]  value;
    private ArrInst(final ArrType type, final Inst[] value) {
      assert type != null;
      assert value != null;
      assert value.length == type.length || type.length == ArrType.UNKNOWN_LENGTH;
      assert Arrays.stream(value).allMatch(Objects::nonNull);
      assert Arrays.stream(value).map(Inst::type).allMatch(type.elementType::equals);
      this.type  = type;
      this.value = value;
    }
    @Override public Type type() { return type; }
    @Override public String toString() { return toDebugString(); }
    @Override public void toDebugString(final StringBuilder buf, final Map<Inst, Integer> seen) {
      if (seen.get(this) instanceof Integer index) {
        buf.append("arr@");
        buf.append(index);
      } else {
        final int index = seen.size();
        seen.put(this, index);
        buf.append("arr#");
        buf.append(index);
        buf.append("(");
        for (int i = 0; i < value.length; i++) {
          if (i > 0) buf.append(", ");
          value[i].toDebugString(buf, seen);
        }
        buf.append(")");
      }
    }
  }

  /** Record instance */
  public static final class RecInst implements Inst {
    private final RecType type;
    public final Inst[] value;
    private RecInst(final RecType type, final Inst[] value) {
      assert type != null;
      assert value != null;
      assert value.length == type.types.length;
      assert Arrays.stream(value).allMatch(Objects::nonNull);
      assert IntStream.range(0, value.length).allMatch(i -> value[i].type().equals(type.types[i]));
      this.type  = type;
      this.value = value;
    }
    @Override public RecType type() { return type; }
    @Override public String toString() { return toDebugString(); }
    @Override public void toDebugString(final StringBuilder buf, final Map<Inst, Integer> seen) {
      if (seen.get(this) instanceof Integer index) {
        buf.append("rec@");
        buf.append(index);
      } else {
        final int index = seen.size();
        seen.put(this, index);
        buf.append("rec#");
        buf.append(index);
        buf.append("(");
        for (int i = 0; i < value.length; i++) {
          if (i > 0) buf.append(", ");
          buf.append(type.names[i]);
          buf.append(" ");
          value[i].toDebugString(buf, seen);
        }
        buf.append(")");
      }
    }
  }

  /** Function instance */
  public static final class FunInst implements Inst {
    private final FunType type;
    public final Void value; // TODO
    private FunInst(final FunType type, final Void value) { // TODO
      assert type != null;
      assert value != null;
      this.type  = type;
      this.value = value;
    }
    @Override public FunType type() { return type; }
    @Override public String toString() { return toDebugString(); }
    @Override public void toDebugString(final StringBuilder buf, final Map<Inst, Integer> seen) {
      if (seen.get(this) instanceof Integer index) {
        buf.append("fun@");
        buf.append(index);
      } else {
        final int index = seen.size();
        seen.put(this, index);
        buf.append("fun#");
        buf.append(index);
        buf.append("(TODO)"); // TODO
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Special Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Unit */
  public static final class UnitInst implements Inst {
    private UnitInst() {}
    @Override public RecType type() { return types.unt; }
    @Override public String toString() { return "unit" ; }
  }

  /** Instance of a named type */
  public static final class NameInst implements Inst {
    private final NameType type;
    private final Inst     inst;
    private NameInst(final NameType type, final Inst inst) {
      assert type != null;
      assert inst != null;
      assert type.type != types.unk;
      assert inst.type().equals(type.type);
      this.type = type;
      this.inst = inst;
    }
    @Override public Type type() { return type; }
    @Override public String toString() { return toDebugString(); }
    @Override public void toDebugString(final StringBuilder buf, final Map<Inst, Integer> seen) {
      buf.append("<");
      buf.append(type.name);
      buf.append(">");
      if (seen.get(this) instanceof Integer index) {
        buf.append("@");
        buf.append(index);
      } else {
        final int index = seen.size();
        seen.put(this, index);
        buf.append("#");
        buf.append(index);
        buf.append("(");
        inst.toDebugString(buf, seen);
        buf.append(")");
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Instance Singletons and Constructors
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Singleton Instances
  public static final UnitInst unit = new UnitInst();

  // Constructed Instances
  public static BitInst bit(final int    value) { return new BitInst(value); }
  public static U08Inst u08(final int    value) { return new U08Inst(value); }
  public static U16Inst u16(final int    value) { return new U16Inst(value); }
  public static U32Inst u32(final int    value) { return new U32Inst(value); }
  public static U64Inst u64(final long   value) { return new U64Inst(value); }
  public static I08Inst i08(final int    value) { return new I08Inst(value); }
  public static I16Inst i16(final int    value) { return new I16Inst(value); }
  public static I32Inst i32(final int    value) { return new I32Inst(value); }
  public static I64Inst i64(final long   value) { return new I64Inst(value); }
  public static F32Inst f32(final float  value) { return new F32Inst(value); }
  public static F64Inst f64(final double value) { return new F64Inst(value); }
  public static ArrInst arr(final ArrType type, final Inst[] value) {
    return new ArrInst(type, value);
  }
  public static RecInst rec(final RecType type, final Inst[] value) {
    return new RecInst(type, value);
  }
  public static FunInst fun(final FunType type, final Void value) { // TODO
    return new FunInst(type, value);
  }
  public static NameInst name(final NameType type, final Inst inst) {
    return new NameInst(type, inst);
  }
}