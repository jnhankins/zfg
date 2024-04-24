package zfg.lang.val;

import zfg.lang.ops.Add;
import zfg.lang.ops.Div;
import zfg.lang.ops.Eql;
import zfg.lang.ops.Geq;
import zfg.lang.ops.Gtn;
import zfg.lang.ops.Leq;
import zfg.lang.ops.Ltn;
import zfg.lang.ops.Mod;
import zfg.lang.ops.Mul;
import zfg.lang.ops.Neg;
import zfg.lang.ops.Neq;
import zfg.lang.ops.Rem;
import zfg.lang.ops.Sub;
import zfg.lang.ops.Twc;

public final class F64 implements Val, Parser.Flt,
    Twc.I<F64>, Ltn.I<F64>, Gtn.I<F64>, Leq.I<F64>, Geq.I<F64>, Eql.I<F64>, Neq.I<F64>,
    Neg.I<F64>, Add.I<F64>, Sub.I<F64>, Mul.I<F64>, Div.I<F64>, Rem.I<F64>, Mod.I<F64> {

  public final double value;
  protected F64(final double value) { this.value = value; }

  @Override public final I32 twc(final F64 rhs) { return Twc.f64(this, rhs); }
  @Override public final Bit ltn(final F64 rhs) { return Ltn.f64(this, rhs); }
  @Override public final Bit gtn(final F64 rhs) { return Gtn.f64(this, rhs); }
  @Override public final Bit leq(final F64 rhs) { return Leq.f64(this, rhs); }
  @Override public final Bit geq(final F64 rhs) { return Geq.f64(this, rhs); }
  @Override public final Bit eql(final F64 rhs) { return Eql.f64(this, rhs); }
  @Override public final Bit neq(final F64 rhs) { return Neq.f64(this, rhs); }
  @Override public final F64 neg() { return Neg.f64(this); }
  @Override public final F64 add(final F64 rhs) { return Add.f64(this, rhs); }
  @Override public final F64 sub(final F64 rhs) { return Sub.f64(this, rhs); }
  @Override public final F64 mul(final F64 rhs) { return Mul.f64(this, rhs); }
  @Override public final F64 div(final F64 rhs) { return Div.f64(this, rhs); }
  @Override public final F64 rem(final F64 rhs) { return Rem.f64(this, rhs); }
  @Override public final F64 mod(final F64 rhs) { return Mod.f64(this, rhs); }

  @Override public final int hashCode() { final long h = Double.doubleToLongBits(value); return (int)(h ^ (h >>> 32)); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof F64 && Double.doubleToLongBits(value) == Double.doubleToLongBits(((F64)that).value)); }
  @Override public final String toString() { return String.format("%+gf64", value); }

  public static final F64 of(final double value) { return new F64(value); }
}
