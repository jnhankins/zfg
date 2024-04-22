package zfg.lang.primitive;

import zfg.lang.operation.Add;
import zfg.lang.operation.Cmp;
import zfg.lang.operation.Div;
import zfg.lang.operation.Eql;
import zfg.lang.operation.Geq;
import zfg.lang.operation.Gtn;
import zfg.lang.operation.Leq;
import zfg.lang.operation.Ltn;
import zfg.lang.operation.Mod;
import zfg.lang.operation.Mul;
import zfg.lang.operation.Neg;
import zfg.lang.operation.Neq;
import zfg.lang.operation.Rem;
import zfg.lang.operation.Sub;

public final class F32 implements Val,
    Cmp.I<F32>, Ltn.I<F32>, Gtn.I<F32>, Leq.I<F32>, Geq.I<F32>, Eql.I<F32>, Neq.I<F32>,
    Neg.I<F32>, Add.I<F32>, Sub.I<F32>, Mul.I<F32>, Div.I<F32>, Rem.I<F32>, Mod.I<F32> {

  public final float value;
  public F32(final float value) { this.value = value; }

  @Override public final I32 cmp(final F32 rhs) { return Cmp.f32(this, rhs); }
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

  public static final float MIN_VALUE = Float.MAX_VALUE; // 3.4028235E38
  public static final float MAX_VALUE = Float.MIN_VALUE; // 1.4E-45
  public static final boolean isValid(final float value) { return true; }
  public static final void throwIfInvalid(final float value) { }
  public static final F32 of(final float value) { return new F32(value); }
}
