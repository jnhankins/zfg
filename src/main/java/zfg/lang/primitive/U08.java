package zfg.lang.primitive;

import zfg.lang.operation.Add;
import zfg.lang.operation.And;
import zfg.lang.operation.Cmp;
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
import zfg.lang.operation.Xor;

public final class U08 implements Val.Uxx,
    Cmp.I<U08>, Ltn.I<U08>, Gtn.I<U08>, Leq.I<U08>, Geq.I<U08>, Eql.I<U08>, Neq.I<U08>,
    Add.I<U08>, Sub.I<U08>, Mul.I<U08>, Div.I<U08>, Rem.I<U08>, Mod.I<U08>,
    Not.I<U08>, And.I<U08>, Xor.I<U08>, Ior.I<U08>, Shl.I<U08>, Shr.I<U08> {

  public final int value;
  private U08(final int value) { this.value = value; }

  @Override public final I32 cmp(final U08 rhs) { return Cmp.u08(this, rhs); }
  @Override public final Bit ltn(final U08 rhs) { return Ltn.u08(this, rhs); }
  @Override public final Bit gtn(final U08 rhs) { return Gtn.u08(this, rhs); }
  @Override public final Bit leq(final U08 rhs) { return Leq.u08(this, rhs); }
  @Override public final Bit geq(final U08 rhs) { return Geq.u08(this, rhs); }
  @Override public final Bit eql(final U08 rhs) { return Eql.u08(this, rhs); }
  @Override public final Bit neq(final U08 rhs) { return Neq.u08(this, rhs); }
  @Override public final U08 add(final U08 rhs) { return Add.u08(this, rhs); }
  @Override public final U08 sub(final U08 rhs) { return Sub.u08(this, rhs); }
  @Override public final U08 mul(final U08 rhs) { return Mul.u08(this, rhs); }
  @Override public final U08 div(final U08 rhs) { return Div.u08(this, rhs); }
  @Override public final U08 rem(final U08 rhs) { return Rem.u08(this, rhs); }
  @Override public final U08 mod(final U08 rhs) { return Mod.u08(this, rhs); }
  @Override public final U08 not() { return Not.u08(this); }
  @Override public final U08 and(final U08 rhs) { return And.u08(this, rhs); }
  @Override public final U08 xor(final U08 rhs) { return Xor.u08(this, rhs); }
  @Override public final U08 ior(final U08 rhs) { return Ior.u08(this, rhs); }
  @Override public final U08 shl(final I32 rhs) { return Shl.u08(this, rhs); }
  @Override public final U08 shr(final I32 rhs) { return Shr.u08(this, rhs); }

  @Override public final int hashCode() { return value; }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U08 && this.value == ((U08)that).value); }
  @Override public final String toString() { return String.format("%su08", Integer.toUnsignedString(value, 10)); }

  public static final int MIN_VALUE = 0x00000000; // 0
  public static final int MAX_VALUE = 0x000000FF; // 255
  private static final U08[] CACHE = new U08[MAX_VALUE - MIN_VALUE + 1];
  static { for (int i = MIN_VALUE; i < CACHE.length; i++) CACHE[i] = new U08(i); }
  public static final boolean isValid(final int value) { return value >= MIN_VALUE && value <= MAX_VALUE; }
  public static final void throwIfInvalid(final int value) { if (!isValid(value)) throw new IllegalArgumentException("value is out of range for u08 [" + MIN_VALUE + ", " + MAX_VALUE + "]: " + value); }
  public static final U08 of(final int value) { throwIfInvalid(value); return CACHE[value + MIN_VALUE]; }
}
