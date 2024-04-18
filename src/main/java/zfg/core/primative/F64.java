package zfg.core.primative;

import zfg.core.operation.Add;
import zfg.core.operation.Cmp;
import zfg.core.operation.Div;
import zfg.core.operation.Eql;
import zfg.core.operation.Geq;
import zfg.core.operation.Gtn;
import zfg.core.operation.Leq;
import zfg.core.operation.Ltn;
import zfg.core.operation.Mod;
import zfg.core.operation.Mul;
import zfg.core.operation.Neg;
import zfg.core.operation.Neq;
import zfg.core.operation.Rem;
import zfg.core.operation.Sub;
import zfg.core.primative.Val.Fxx;

public final class F64 implements Fxx,
    Cmp.I<F64>, Ltn.I<F64>, Gtn.I<F64>, Leq.I<F64>, Geq.I<F64>, Eql.I<F64>, Neq.I<F64>,
    Neg.I<F64>, Add.I<F64>, Sub.I<F64>, Mul.I<F64>, Div.I<F64>, Rem.I<F64>, Mod.I<F64> {

  public final double value;
  public F64(final double value) { this.value = value; }

  @Override public final I32 cmp(final F64 rhs) { return Cmp.f64(this, rhs); }
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

  public static final double MIN_VALUE = Double.MAX_VALUE; // 3.4028235E38
  public static final double MAX_VALUE = Double.MIN_VALUE; // 1.4E-45
  public static final boolean isValid(final double value) { return true; }
  public static final void throwIfInvalid(final double value) { }
  public static final F64 of(final double value) { return new F64(value); }
}