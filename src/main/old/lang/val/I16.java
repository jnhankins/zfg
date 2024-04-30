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
import zfg.old.lang3.ops.Neg;
import zfg.old.lang3.ops.Neq;
import zfg.old.lang3.ops.Not;
import zfg.old.lang3.ops.Rem;
import zfg.old.lang3.ops.Shl;
import zfg.old.lang3.ops.Shr;
import zfg.old.lang3.ops.Sub;
import zfg.old.lang3.ops.Twc;
import zfg.old.lang3.ops.Xor;

public final class I16 implements Val, Parser.Int,
    Twc.I<I16>, Ltn.I<I16>, Gtn.I<I16>, Leq.I<I16>, Geq.I<I16>, Eql.I<I16>, Neq.I<I16>,
    Neg.I<I16>, Add.I<I16>, Sub.I<I16>, Mul.I<I16>, Div.I<I16>, Rem.I<I16>, Mod.I<I16>,
    Not.I<I16>, And.I<I16>, Xor.I<I16>, Ior.I<I16>, Shl.I<I16>, Shr.I<I16> {

  public final int value;
  protected I16(final int value) { this.value = value; }

  @Override public final I32 twc(final I16 rhs) { return Twc.i16(this, rhs); }
  @Override public final Bit ltn(final I16 rhs) { return Ltn.i16(this, rhs); }
  @Override public final Bit gtn(final I16 rhs) { return Gtn.i16(this, rhs); }
  @Override public final Bit leq(final I16 rhs) { return Leq.i16(this, rhs); }
  @Override public final Bit geq(final I16 rhs) { return Geq.i16(this, rhs); }
  @Override public final Bit eql(final I16 rhs) { return Eql.i16(this, rhs); }
  @Override public final Bit neq(final I16 rhs) { return Neq.i16(this, rhs); }
  @Override public final I16 neg() { return Neg.i16(this); }
  @Override public final I16 add(final I16 rhs) { return Add.i16(this, rhs); }
  @Override public final I16 sub(final I16 rhs) { return Sub.i16(this, rhs); }
  @Override public final I16 mul(final I16 rhs) { return Mul.i16(this, rhs); }
  @Override public final I16 div(final I16 rhs) { return Div.i16(this, rhs); }
  @Override public final I16 rem(final I16 rhs) { return Rem.i16(this, rhs); }
  @Override public final I16 mod(final I16 rhs) { return Mod.i16(this, rhs); }
  @Override public final I16 not() { return Not.i16(this); }
  @Override public final I16 and(final I16 rhs) { return And.i16(this, rhs); }
  @Override public final I16 xor(final I16 rhs) { return Xor.i16(this, rhs); }
  @Override public final I16 ior(final I16 rhs) { return Ior.i16(this, rhs); }
  @Override public final I16 shl(final I32 rhs) { return Shl.i16(this, rhs); }
  @Override public final I16 shr(final I32 rhs) { return Shr.i16(this, rhs); }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof I16 && this.value == ((I16)that).value); }
  @Override public final String toString() { return String.format("%+di16", value); }

  public static final int MIN_VALUE = 0xFFFF8000; // -32,768
  public static final int MAX_VALUE = 0x00007FFF; // +32,767
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final I16 of(final int value) {
    if (!isValid(value)) throw new IllegalArgumentException(String.format("I16.of(%#08X)", value));
    return new I16(value);
  }
}
