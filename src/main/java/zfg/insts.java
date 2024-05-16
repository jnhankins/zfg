package zfg;


public final class Insts {
  private Insts() {}

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Virtual Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Cannot be instantiated

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Primitive Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final Inst.Bit bit(int    value) { return new Inst.Bit(value); }
  public static final Inst.U08 u08(int    value) { return new Inst.U08(value); }
  public static final Inst.U16 u16(int    value) { return new Inst.U16(value); }
  public static final Inst.U32 u32(int    value) { return new Inst.U32(value); }
  public static final Inst.U64 u64(long   value) { return new Inst.U64(value); }
  public static final Inst.I08 i08(int    value) { return new Inst.I08(value); }
  public static final Inst.I16 i16(int    value) { return new Inst.I16(value); }
  public static final Inst.I32 i32(int    value) { return new Inst.I32(value); }
  public static final Inst.I64 i64(long   value) { return new Inst.I64(value); }
  public static final Inst.F32 f32(float  value) { return new Inst.F32(value); }
  public static final Inst.F64 f64(double value) { return new Inst.F64(value); }
}
