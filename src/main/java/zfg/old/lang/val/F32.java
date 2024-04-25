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

public final class F32 implements Val, Parser.Flt,
    Twc.I<F32>, Ltn.I<F32>, Gtn.I<F32>, Leq.I<F32>, Geq.I<F32>, Eql.I<F32>, Neq.I<F32>,
    Neg.I<F32>, Add.I<F32>, Sub.I<F32>, Mul.I<F32>, Div.I<F32>, Rem.I<F32>, Mod.I<F32> {

  public final float value;
  protected F32(final float value) { this.value = value; }

  @Override public final I32 twc(final F32 rhs) { return Twc.f32(this, rhs); }
  @Override public final Bit ltn(final F32 rhs) { return Ltn.f32(this, rhs); }
  @Override public final Bit gtn(final F32 rhs) { return Gtn.f32(this, rhs); }
  @Override public final Bit leq(final F32 rhs) { return Leq.f32(this, rhs); }
  @Override public final Bit geq(final F32 rhs) { return Geq.f32(this, rhs); }
  @Override public final Bit eql(final F32 rhs) { return Eql.f32(this, rhs); }
  @Override public final Bit neq(final F32 rhs) { return Neq.f32(this, rhs); }
  @Override public final F32 neg() { return Neg.f32(this); }
  @Override public final F32 add(final F32 rhs) { return Add.f32(this, rhs); }
  @Override public final F32 sub(final F32 rhs) { return Sub.f32(this, rhs); }
  @Override public final F32 mul(final F32 rhs) { return Mul.f32(this, rhs); }
  @Override public final F32 div(final F32 rhs) { return Div.f32(this, rhs); }
  @Override public final F32 rem(final F32 rhs) { return Rem.f32(this, rhs); }
  @Override public final F32 mod(final F32 rhs) { return Mod.f32(this, rhs); }

  @Override public final int hashCode() { return Float.floatToIntBits(value); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof F32 && Float.floatToIntBits(value) == Float.floatToIntBits(((F32)that).value)); }
  @Override public final String toString() { return String.format("%+gf32", value); }

  public static final F32 of(final float value) { return new F32(value); }
}