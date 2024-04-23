package zfg.lang.val;

import zfg.lang.ops.And;
import zfg.lang.ops.Eql;
import zfg.lang.ops.Geq;
import zfg.lang.ops.Gtn;
import zfg.lang.ops.Ior;
import zfg.lang.ops.Lcj;
import zfg.lang.ops.Ldj;
import zfg.lang.ops.Leq;
import zfg.lang.ops.Lnt;
import zfg.lang.ops.Ltn;
import zfg.lang.ops.Neq;
import zfg.lang.ops.Not;
import zfg.lang.ops.Twc;
import zfg.lang.ops.Xor;

public final class Bit implements Val, Parser.Int,
    Twc.I<Bit>, Ltn.I<Bit>, Leq.I<Bit>, Gtn.I<Bit>, Geq.I<Bit>, Eql.I<Bit>, Neq.I<Bit>,
    Not.I<Bit>, And.I<Bit>, Xor.I<Bit>, Ior.I<Bit>,
    Lnt.I<Bit>, Lcj.I<Bit>, Ldj.I<Bit> {

  public final int value;
  protected Bit(final int value) { this.value = value; }

  @Override public final Bit not() { return Not.bit(this); }
  @Override public final Bit and(final Bit rhs) { return And.bit(this, rhs); }
  @Override public final Bit xor(final Bit rhs) { return Xor.bit(this, rhs); }
  @Override public final Bit ior(final Bit rhs) { return Ior.bit(this, rhs); }
  @Override public final I32 twc(final Bit rhs) { return Twc.bit(this, rhs); }
  @Override public final Bit ltn(final Bit rhs) { return Ltn.bit(this, rhs); }
  @Override public final Bit leq(final Bit rhs) { return Leq.bit(this, rhs); }
  @Override public final Bit gtn(final Bit rhs) { return Gtn.bit(this, rhs); }
  @Override public final Bit geq(final Bit rhs) { return Geq.bit(this, rhs); }
  @Override public final Bit eql(final Bit rhs) { return Eql.bit(this, rhs); }
  @Override public final Bit neq(final Bit rhs) { return Neq.bit(this, rhs); }
  @Override public final Bit lnt() { return Lnt.bit(this); }
  @Override public final Bit lcj(final Bit rhs) { return Lcj.bit(this, rhs); }
  @Override public final Bit ldj(final Bit rhs) { return Ldj.bit(this, rhs); }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof Bit && this.value == ((Bit)that).value); }
  @Override public final String toString() { return String.format("%dbit", value); }

  public static final Bit FALSE = new Bit(0);
  public static final Bit TRUE = new Bit(1);
  public static final boolean isValid(final int value) { return (value & 1) == value; }
  public static final Bit of(final int value) {
    if (!isValid(value)) throw new IllegalArgumentException(String.format("Bit.of(%#08X)", value));
    return value == 0 ? FALSE : TRUE;
  }
}
