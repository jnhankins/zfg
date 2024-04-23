package zfg.lang.primitive;

import zfg.lang.operation.Add;
import zfg.lang.operation.And;
import zfg.lang.operation.Div;
import zfg.lang.operation.Eql;
import zfg.lang.operation.Geq;
import zfg.lang.operation.Gtn;
import zfg.lang.operation.Ior;
import zfg.lang.operation.Leq;
import zfg.lang.operation.Ltn;
import zfg.lang.operation.Mod;
import zfg.lang.operation.Mul;
import zfg.lang.operation.Neq;
import zfg.lang.operation.Not;
import zfg.lang.operation.Rem;
import zfg.lang.operation.Shl;
import zfg.lang.operation.Shr;
import zfg.lang.operation.Sub;
import zfg.lang.operation.Twc;
import zfg.lang.operation.Xor;

public final class U16 implements Val, Parser.Int,
    Twc.I<U16>, Ltn.I<U16>, Gtn.I<U16>, Leq.I<U16>, Geq.I<U16>, Eql.I<U16>, Neq.I<U16>,
    Add.I<U16>, Sub.I<U16>, Mul.I<U16>, Div.I<U16>, Rem.I<U16>, Mod.I<U16>,
    Not.I<U16>, And.I<U16>, Xor.I<U16>, Ior.I<U16>, Shl.I<U16>, Shr.I<U16> {

  public final int value;
  protected U16(final int value) { this.value = value; }

  @Override public final I32 twc(final U16 rhs) { return Twc.u16(this, rhs); }
  @Override public final Bit ltn(final U16 rhs) { return Ltn.u16(this, rhs); }
  @Override public final Bit gtn(final U16 rhs) { return Gtn.u16(this, rhs); }
  @Override public final Bit leq(final U16 rhs) { return Leq.u16(this, rhs); }
  @Override public final Bit geq(final U16 rhs) { return Geq.u16(this, rhs); }
  @Override public final Bit eql(final U16 rhs) { return Eql.u16(this, rhs); }
  @Override public final Bit neq(final U16 rhs) { return Neq.u16(this, rhs); }
  @Override public final U16 add(final U16 rhs) { return Add.u16(this, rhs); }
  @Override public final U16 sub(final U16 rhs) { return Sub.u16(this, rhs); }
  @Override public final U16 mul(final U16 rhs) { return Mul.u16(this, rhs); }
  @Override public final U16 div(final U16 rhs) { return Div.u16(this, rhs); }
  @Override public final U16 rem(final U16 rhs) { return Rem.u16(this, rhs); }
  @Override public final U16 mod(final U16 rhs) { return Mod.u16(this, rhs); }
  @Override public final U16 not() { return Not.u16(this); }
  @Override public final U16 and(final U16 rhs) { return And.u16(this, rhs); }
  @Override public final U16 xor(final U16 rhs) { return Xor.u16(this, rhs); }
  @Override public final U16 ior(final U16 rhs) { return Ior.u16(this, rhs); }
  @Override public final U16 shl(final I32 rhs) { return Shl.u16(this, rhs); }
  @Override public final U16 shr(final I32 rhs) { return Shr.u16(this, rhs); }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U16 && this.value == ((U16)that).value); }
  @Override public final String toString() { return String.format("%su16", Integer.toUnsignedString(value, 10)); }

  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0x0000FFFF; // 65,535
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for u16 [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final U16 of(final int value) { throwIfInvalid(value); return new U16(value); }
}
