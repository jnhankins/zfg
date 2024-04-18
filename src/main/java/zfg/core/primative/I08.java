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

public final class I08 implements Ixx,
    Cmp.I<I08>, Ltn.I<I08>, Gtn.I<I08>, Leq.I<I08>, Geq.I<I08>, Eql.I<I08>, Neq.I<I08>,
    Neg.I<I08>, Add.I<I08>, Sub.I<I08>, Mul.I<I08>, Div.I<I08>, Rem.I<I08>, Mod.I<I08>,
    Not.I<I08>, And.I<I08>, Xor.I<I08>, Ior.I<I08>, Shl.I<I08>, Shr.I<I08> {

  public final int value;
  public I08(final int value) { this.value = value; }

  @Override public final I32 cmp(final I08 rhs) { return Cmp.i08(this, rhs); }
  @Override public final Bit ltn(final I08 rhs) { return Ltn.i08(this, rhs); }
  @Override public final Bit gtn(final I08 rhs) { return Gtn.i08(this, rhs); }
  @Override public final Bit leq(final I08 rhs) { return Leq.i08(this, rhs); }
  @Override public final Bit geq(final I08 rhs) { return Geq.i08(this, rhs); }
  @Override public final Bit eql(final I08 rhs) { return Eql.i08(this, rhs); }
  @Override public final Bit neq(final I08 rhs) { return Neq.i08(this, rhs); }
  @Override public final I08 neg() { return Neg.i08(this); }
  @Override public final I08 add(final I08 rhs) { return Add.i08(this, rhs); }
  @Override public final I08 sub(final I08 rhs) { return Sub.i08(this, rhs); }
  @Override public final I08 mul(final I08 rhs) { return Mul.i08(this, rhs); }
  @Override public final I08 div(final I08 rhs) { return Div.i08(this, rhs); }
  @Override public final I08 rem(final I08 rhs) { return Rem.i08(this, rhs); }
  @Override public final I08 mod(final I08 rhs) { return Mod.i08(this, rhs); }
  @Override public final I08 not() { return Not.i08(this); }
  @Override public final I08 and(final I08 rhs) { return And.i08(this, rhs); }
  @Override public final I08 xor(final I08 rhs) { return Xor.i08(this, rhs); }
  @Override public final I08 ior(final I08 rhs) { return Ior.i08(this, rhs); }
  @Override public final I08 shl(final I32 rhs) { return Shl.i08(this, rhs); }
  @Override public final I08 shr(final I32 rhs) { return Shr.i08(this, rhs); }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I08 && this.value == ((I08)that).value); }
  @Override public final String toString() { return String.format("%+di08", value); }

  public static final int MIN_VALUE = 0xFFFFFF80; // -128
  public static final int MAX_VALUE = 0x0000007F; // +127
  private static final I08[] CACHE = new I08[MAX_VALUE - MIN_VALUE + 1];
  static { for (int i = 0; i < CACHE.length; i++) CACHE[i] = new I08(i + MIN_VALUE); }
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for i08 [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final I08 of(final int value) { throwIfInvalid(value); return CACHE[value - MIN_VALUE]; }
}
