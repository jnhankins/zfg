package zfg.core.primative;

import zfg.core.operation.Add;
import zfg.core.operation.And;
import zfg.core.operation.Cmp;
import zfg.core.operation.Div;
import zfg.core.operation.Eql;
import zfg.core.operation.Geq;
import zfg.core.operation.Gtn;
import zfg.core.operation.Ior;
import zfg.core.operation.Leq;
import zfg.core.operation.Ltn;
import zfg.core.operation.Mod;
import zfg.core.operation.Mul;
import zfg.core.operation.Neg;
import zfg.core.operation.Neq;
import zfg.core.operation.Not;
import zfg.core.operation.Rem;
import zfg.core.operation.Shl;
import zfg.core.operation.Shr;
import zfg.core.operation.Sub;
import zfg.core.operation.Xor;
import zfg.core.primative.Val.Ixx;

public final class I32 implements Ixx,
    Cmp.I<I32>, Ltn.I<I32>, Gtn.I<I32>, Leq.I<I32>, Geq.I<I32>, Eql.I<I32>, Neq.I<I32>,
    Neg.I<I32>, Add.I<I32>, Sub.I<I32>, Mul.I<I32>, Div.I<I32>, Rem.I<I32>, Mod.I<I32>,
    Not.I<I32>, And.I<I32>, Xor.I<I32>, Ior.I<I32>, Shl.I<I32>, Shr.I<I32> {

  public final int value;
  public I32(final int value) { this.value = value; }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I32 && this.value == ((I32)that).value); }
  @Override public final String toString() { return String.format("%+di32", value); }

  @Override public final I32 cmp(final I32 rhs) { return Cmp.i32(this, rhs); }
  @Override public final Bit ltn(final I32 rhs) { return Ltn.i32(this, rhs); }
  @Override public final Bit gtn(final I32 rhs) { return Gtn.i32(this, rhs); }
  @Override public final Bit leq(final I32 rhs) { return Leq.i32(this, rhs); }
  @Override public final Bit geq(final I32 rhs) { return Geq.i32(this, rhs); }
  @Override public final Bit eql(final I32 rhs) { return Eql.i32(this, rhs); }
  @Override public final Bit neq(final I32 rhs) { return Neq.i32(this, rhs); }
  @Override public final I32 neg() { return Neg.i32(this); }
  @Override public final I32 add(final I32 rhs) { return Add.i32(this, rhs); }
  @Override public final I32 sub(final I32 rhs) { return Sub.i32(this, rhs); }
  @Override public final I32 mul(final I32 rhs) { return Mul.i32(this, rhs); }
  @Override public final I32 div(final I32 rhs) { return Div.i32(this, rhs); }
  @Override public final I32 rem(final I32 rhs) { return Rem.i32(this, rhs); }
  @Override public final I32 mod(final I32 rhs) { return Mod.i32(this, rhs); }
  @Override public final I32 not() { return Not.i32(this); }
  @Override public final I32 and(final I32 rhs) { return And.i32(this, rhs); }
  @Override public final I32 xor(final I32 rhs) { return Xor.i32(this, rhs); }
  @Override public final I32 ior(final I32 rhs) { return Ior.i32(this, rhs); }
  @Override public final I32 shl(final I32 rhs) { return Shl.i32(this, rhs); }
  @Override public final I32 shr(final I32 rhs) { return Shr.i32(this, rhs); }

  public static final int MIN_VALUE = 0x80000000; // -2,147,483,648
  public static final int MAX_VALUE = 0x7FFFFFFF; // +2,147,483,647
  public static final boolean isValid(final int value) { return true; }
  public static final void throwIfInvalid(final int value) { }
  public static final I32 of(final int value) { return new I32(value); }
}
