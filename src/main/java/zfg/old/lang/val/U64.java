package zfg.lang.val;

import zfg.lang.ops.Add;
import zfg.lang.ops.And;
import zfg.lang.ops.Div;
import zfg.lang.ops.Eql;
import zfg.lang.ops.Geq;
import zfg.lang.ops.Gtn;
import zfg.lang.ops.Ior;
import zfg.lang.ops.Leq;
import zfg.lang.ops.Ltn;
import zfg.lang.ops.Mod;
import zfg.lang.ops.Mul;
import zfg.lang.ops.Neq;
import zfg.lang.ops.Not;
import zfg.lang.ops.Rem;
import zfg.lang.ops.Shl;
import zfg.lang.ops.Shr;
import zfg.lang.ops.Sub;
import zfg.lang.ops.Twc;
import zfg.lang.ops.Xor;

public final class U64 implements Val, Parser.Int,
    Twc.I<U64>, Ltn.I<U64>, Gtn.I<U64>, Leq.I<U64>, Geq.I<U64>, Eql.I<U64>, Neq.I<U64>,
    Add.I<U64>, Sub.I<U64>, Mul.I<U64>, Div.I<U64>, Rem.I<U64>, Mod.I<U64>,
    Not.I<U64>, And.I<U64>, Xor.I<U64>, Ior.I<U64>, Shl.I<U64>, Shr.I<U64> {

  public final long value;
  protected U64(final long value) { this.value = value; }

  @Override public final I32 twc(final U64 rhs) { return Twc.u64(this, rhs); }
  @Override public final Bit ltn(final U64 rhs) { return Ltn.u64(this, rhs); }
  @Override public final Bit gtn(final U64 rhs) { return Gtn.u64(this, rhs); }
  @Override public final Bit leq(final U64 rhs) { return Leq.u64(this, rhs); }
  @Override public final Bit geq(final U64 rhs) { return Geq.u64(this, rhs); }
  @Override public final Bit eql(final U64 rhs) { return Eql.u64(this, rhs); }
  @Override public final Bit neq(final U64 rhs) { return Neq.u64(this, rhs); }
  @Override public final U64 add(final U64 rhs) { return Add.u64(this, rhs); }
  @Override public final U64 sub(final U64 rhs) { return Sub.u64(this, rhs); }
  @Override public final U64 mul(final U64 rhs) { return Mul.u64(this, rhs); }
  @Override public final U64 div(final U64 rhs) { return Div.u64(this, rhs); }
  @Override public final U64 rem(final U64 rhs) { return Rem.u64(this, rhs); }
  @Override public final U64 mod(final U64 rhs) { return Mod.u64(this, rhs); }
  @Override public final U64 not() { return Not.u64(this); }
  @Override public final U64 and(final U64 rhs) { return And.u64(this, rhs); }
  @Override public final U64 xor(final U64 rhs) { return Xor.u64(this, rhs); }
  @Override public final U64 ior(final U64 rhs) { return Ior.u64(this, rhs); }
  @Override public final U64 shl(final I32 rhs) { return Shl.u64(this, rhs); }
  @Override public final U64 shr(final I32 rhs) { return Shr.u64(this, rhs); }

  @Override public final int hashCode() { return (int)(value ^ (value >>> 32)); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U64 && this.value == ((U64)that).value); }
  @Override public final String toString() { return String.format("%su64", Long.toUnsignedString(value, 10)); }

  public static final long MIN_VALUE = 0x00000000_00000000L; // 0
  public static final long MAX_VALUE = 0xFFFFFFFF_FFFFFFFFL; // 18,446,744,073,709,551,615
  public static final U64 of(final long value) { return new U64(value); }
}
