package zfg.core.primative;

import zfg.core.operation.And;
import zfg.core.operation.Cmp;
import zfg.core.operation.Eql;
import zfg.core.operation.Geq;
import zfg.core.operation.Gtn;
import zfg.core.operation.Ior;
import zfg.core.operation.Lcj;
import zfg.core.operation.Ldj;
import zfg.core.operation.Leq;
import zfg.core.operation.Lnt;
import zfg.core.operation.Ltn;
import zfg.core.operation.Neq;
import zfg.core.operation.Not;
import zfg.core.operation.Xor;
import zfg.core.primative.Val.Num;

public final class Bit implements Num,
    Cmp.I<Bit>, Ltn.I<Bit>, Leq.I<Bit>, Gtn.I<Bit>, Geq.I<Bit>, Eql.I<Bit>, Neq.I<Bit>,
    Not.I<Bit>, And.I<Bit>, Xor.I<Bit>, Ior.I<Bit>,
    Lnt.I<Bit>, Lcj.I<Bit>, Ldj.I<Bit> {

  public final int value;
  public Bit(final int value) { this.value = value; }

  @Override public final Bit not() { return Not.bit(this); }
  @Override public final Bit and(final Bit rhs) { return And.bit(this, rhs); }
  @Override public final Bit xor(final Bit rhs) { return Xor.bit(this, rhs); }
  @Override public final Bit ior(final Bit rhs) { return Ior.bit(this, rhs); }
  @Override public final I32 cmp(final Bit rhs) { return Cmp.bit(this, rhs); }
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
  @Override public final String toString() { return String.format("%sbit", Integer.toUnsignedString(value, 10)); }

  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0x00000001; // 1
  public static final Bit FALSE = new Bit(0);
  public static final Bit TRUE = new Bit(1);
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for bit [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final Bit of(final int value) { throwIfInvalid(value); return value == 0 ? FALSE : TRUE; }
}