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
import zfg.lang.ops.Neg;
import zfg.lang.ops.Neq;
import zfg.lang.ops.Not;
import zfg.lang.ops.Rem;
import zfg.lang.ops.Shl;
import zfg.lang.ops.Shr;
import zfg.lang.ops.Sub;
import zfg.lang.ops.Twc;
import zfg.lang.ops.Xor;

public final class I64 implements Val, Parser.Int,
    Twc.I<I64>, Ltn.I<I64>, Gtn.I<I64>, Leq.I<I64>, Geq.I<I64>, Eql.I<I64>, Neq.I<I64>,
    Neg.I<I64>, Add.I<I64>, Sub.I<I64>, Mul.I<I64>, Div.I<I64>, Rem.I<I64>, Mod.I<I64>,
    Not.I<I64>, And.I<I64>, Xor.I<I64>, Ior.I<I64>, Shl.I<I64>, Shr.I<I64> {

  public final long value;
  protected I64(final long value) { this.value = value; }

  @Override public final I32 twc(final I64 rhs) { return Twc.i64(this, rhs); }
  @Override public final Bit ltn(final I64 rhs) { return Ltn.i64(this, rhs); }
  @Override public final Bit gtn(final I64 rhs) { return Gtn.i64(this, rhs); }
  @Override public final Bit leq(final I64 rhs) { return Leq.i64(this, rhs); }
  @Override public final Bit geq(final I64 rhs) { return Geq.i64(this, rhs); }
  @Override public final Bit eql(final I64 rhs) { return Eql.i64(this, rhs); }
  @Override public final Bit neq(final I64 rhs) { return Neq.i64(this, rhs); }
  @Override public final I64 neg() { return Neg.i64(this); }
  @Override public final I64 add(final I64 rhs) { return Add.i64(this, rhs); }
  @Override public final I64 sub(final I64 rhs) { return Sub.i64(this, rhs); }
  @Override public final I64 mul(final I64 rhs) { return Mul.i64(this, rhs); }
  @Override public final I64 div(final I64 rhs) { return Div.i64(this, rhs); }
  @Override public final I64 rem(final I64 rhs) { return Rem.i64(this, rhs); }
  @Override public final I64 mod(final I64 rhs) { return Mod.i64(this, rhs); }
  @Override public final I64 not() { return Not.i64(this); }
  @Override public final I64 and(final I64 rhs) { return And.i64(this, rhs); }
  @Override public final I64 xor(final I64 rhs) { return Xor.i64(this, rhs); }
  @Override public final I64 ior(final I64 rhs) { return Ior.i64(this, rhs); }
  @Override public final I64 shl(final I32 rhs) { return Shl.i64(this, rhs); }
  @Override public final I64 shr(final I32 rhs) { return Shr.i64(this, rhs); }

  @Override public final int hashCode() { return (int)(value ^ (value >>> 32)); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I64 && this.value == ((I64)that).value); }
  @Override public final String toString() { return String.format("%+di64", value); }

  public static final long MIN_VALUE = 0x80000000_00000000L; // -9,223,372,036,854,775,808
  public static final long MAX_VALUE = 0x7FFFFFFF_FFFFFFF0L; // +9,223,372,036,854,775,807
  public static final I64 of(final long value) { return new I64(value); }
}
