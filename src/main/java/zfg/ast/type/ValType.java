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
  bit(Bit.class,  1),
  u08(U08.class,  8),
  u16(U16.class, 16),
  u32(U32.class, 32),
  u64(U64.class, 64),
  i08(I08.class,  8),
  i16(I16.class, 16),
  i32(I32.class, 32),
  i64(I64.class, 64),
  f32(F32.class, 32),
  f64(F64.class, 64);

  public final Class<? extends Val> clazz;
  public final int flags;
  private ValType(final Class<? extends Val> clazz, final int flags) {
    this.clazz = clazz;
    this.flags = flags;
  }
}
