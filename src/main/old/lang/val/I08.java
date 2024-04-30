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

public final class I08 implements Val, Parser.Int,
    Twc.I<I08>, Ltn.I<I08>, Gtn.I<I08>, Leq.I<I08>, Geq.I<I08>, Eql.I<I08>, Neq.I<I08>,
    Neg.I<I08>, Add.I<I08>, Sub.I<I08>, Mul.I<I08>, Div.I<I08>, Rem.I<I08>, Mod.I<I08>,
    Not.I<I08>, And.I<I08>, Xor.I<I08>, Ior.I<I08>, Shl.I<I08>, Shr.I<I08> {

  public final int value;
  protected I08(final int value) { this.value = value; }

  @Override public final I32 twc(final I08 rhs) { return Twc.i08(this, rhs); }
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
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final I08 of(final int value) {
    if (!isValid(value)) throw new IllegalArgumentException(String.format("I08.of(%#08X)", value));
    final I08 cached = CACHE[value - MIN_VALUE];
    return cached != null ? cached : (CACHE[value - MIN_VALUE] = new I08(value));
  }
  private static final I08[] CACHE = new I08[256];
}
