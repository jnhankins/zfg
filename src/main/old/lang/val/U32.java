package zfg.lang.val;

import zfg.old.lang3.ops.Add;
import zfg.old.lang3.ops.And;
import zfg.old.lang3.ops.Div;
import zfg.old.lang3.ops.Eql;
import zfg.old.lang3.ops.Geq;
import zfg.old.lang3.ops.Gtn;
import zfg.old.lang3.ops.Ior;
import zfg.old.lang3.ops.Leq;
import zfg.old.lang3.ops.Ltn;
import zfg.old.lang3.ops.Mod;
import zfg.old.lang3.ops.Mul;
import zfg.old.lang3.ops.Neq;
import zfg.old.lang3.ops.Not;
import zfg.old.lang3.ops.Rem;
import zfg.old.lang3.ops.Shl;
import zfg.old.lang3.ops.Shr;
import zfg.old.lang3.ops.Sub;
import zfg.old.lang3.ops.Twc;
import zfg.old.lang3.ops.Xor;

public final class U32 implements Val, Parser.Int,
    Twc.I<U32>, Ltn.I<U32>, Gtn.I<U32>, Leq.I<U32>, Geq.I<U32>, Eql.I<U32>, Neq.I<U32>,
    Add.I<U32>, Sub.I<U32>, Mul.I<U32>, Div.I<U32>, Rem.I<U32>, Mod.I<U32>,
    Not.I<U32>, And.I<U32>, Xor.I<U32>, Ior.I<U32>, Shl.I<U32>, Shr.I<U32> {

  public final int value;
  protected U32(final int value) { this.value = value; }

  @Override public final I32 twc(final U32 rhs) { return Twc.u32(this, rhs); }
  @Override public final Bit ltn(final U32 rhs) { return Ltn.u32(this, rhs); }
  @Override public final Bit gtn(final U32 rhs) { return Gtn.u32(this, rhs); }
  @Override public final Bit leq(final U32 rhs) { return Leq.u32(this, rhs); }
  @Override public final Bit geq(final U32 rhs) { return Geq.u32(this, rhs); }
  @Override public final Bit eql(final U32 rhs) { return Eql.u32(this, rhs); }
  @Override public final Bit neq(final U32 rhs) { return Neq.u32(this, rhs); }
  @Override public final U32 add(final U32 rhs) { return Add.u32(this, rhs); }
  @Override public final U32 sub(final U32 rhs) { return Sub.u32(this, rhs); }
  @Override public final U32 mul(final U32 rhs) { return Mul.u32(this, rhs); }
  @Override public final U32 div(final U32 rhs) { return Div.u32(this, rhs); }
  @Override public final U32 rem(final U32 rhs) { return Rem.u32(this, rhs); }
  @Override public final U32 mod(final U32 rhs) { return Mod.u32(this, rhs); }
  @Override public final U32 not() { return Not.u32(this); }
  @Override public final U32 and(final U32 rhs) { return And.u32(this, rhs); }
  @Override public final U32 xor(final U32 rhs) { return Xor.u32(this, rhs); }
  @Override public final U32 ior(final U32 rhs) { return Ior.u32(this, rhs); }
  @Override public final U32 shl(final I32 rhs) { return Shl.u32(this, rhs); }
  @Override public final U32 shr(final I32 rhs) { return Shr.u32(this, rhs); }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U32 && this.value == ((U32)that).value); }
  @Override public final String toString() { return String.format("%su32", Integer.toUnsignedString(value, 10)); }

  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0xFFFFFFFF; // 4,294,967,295
  public static final U32 of(final int value) { return new U32(value); }
}
