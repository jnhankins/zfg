package zfg.ast.type;

import javax.management.openmbean.ArrayType;

import zfg.core.primative.Bit;
import zfg.core.primative.F32;
import zfg.core.primative.F64;
import zfg.core.primative.I08;
import zfg.core.primative.I16;
import zfg.core.primative.I32;
import zfg.core.primative.I64;
import zfg.core.primative.U08;
import zfg.core.primative.U16;
import zfg.core.primative.U32;
import zfg.core.primative.U64;
import zfg.core.primative.Val;

public enum ValType implements Type {
  bit(Bit.class),
  u08(U08.class),
  u16(U16.class),
  u32(U32.class),
  u64(U64.class),
  i08(I08.class),
  i16(I16.class),
  i32(I32.class),
  i64(I64.class),
  f32(F32.class),
  f64(F64.class);

  public final Class<? extends Val> clazz;
  private ValType(final Class<? extends Val> clazz) {
    this.clazz = clazz;
  }
}
