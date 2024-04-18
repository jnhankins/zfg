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
import zfg.core.operation.Neq;
import zfg.core.operation.Not;
import zfg.core.operation.Rem;
import zfg.core.operation.Shl;
import zfg.core.operation.Shr;
import zfg.core.operation.Sub;
import zfg.core.operation.Xor;

public final class U64 implements Val,
    Cmp.I<U64>, Ltn.I<U64>, Gtn.I<U64>, Leq.I<U64>, Geq.I<U64>, Eql.I<U64>, Neq.I<U64>,
    Add.I<U64>, Sub.I<U64>, Mul.I<U64>, Div.I<U64>, Rem.I<U64>, Mod.I<U64>,
    Not.I<U64>, And.I<U64>, Xor.I<U64>, Ior.I<U64>, Shl.I<U64>, Shr.I<U64> {

  public final long value;
  public U64(final long value) { this.value = value; }

  @Override public final I32 cmp(final U64 rhs) { return Cmp.u64(this, rhs); }
  @Override public final Bit ltn(final U64 rhs) { return Ltn.u64(this, rhs); }
  @Override public final Bit gtn(final U64 rhs) { return Gtn.u64(this, rhs); }
  @Override public final Bit leq(final U64 rhs) { return Leq.u64(this, rhs); }
  @Override public final Bit geq(final U64 rhs) { return Geq.u64(this, rhs); }
  @Override public final Bit eql(final U64 rhs) { return Eql.u64(this, rhs); }
  @Override public final Bit neq(final U64 rhs) { return Neq.u64(this, rhs); }
  @Override public final U64 add(final U64 rhs) { return Add.u64(this, rhs); }
  @Override public final U64 sub(final U64 rhs) { return Sub.u64(this, rhs); }
  @Override public final U64 mul(final U64 rhs) { return Mul.u64(this, rhs); }
  @Override public final U64 div(final U64 rhs) { return Div.u64(this, rhs); }
  @Override public final U64 rem(final U64 rhs) { return Rem.u64(this, rhs); }
  @Override public final U64 mod(final U64 rhs) { return Mod.u64(this, rhs); }
  @Override public final U64 not() { return Not.u64(this); }
  @Override public final U64 and(final U64 rhs) { return And.u64(this, rhs); }
  @Override public final U64 xor(final U64 rhs) { return Xor.u64(this, rhs); }
  @Override public final U64 ior(final U64 rhs) { return Ior.u64(this, rhs); }
  @Override public final U64 shl(final I32 rhs) { return Shl.u64(this, rhs); }
  @Override public final U64 shr(final I32 rhs) { return Shr.u64(this, rhs); }

  @Override public final int hashCode() { return (int)(value ^ (value >>> 32)); }
  @Override public final boolean equals(final Object that) { return this == that || (that instanceof U64 && this.value == ((U64)that).value); }
  @Override public final String toString() { return String.format("%su64", Long.toUnsignedString(value, 10)); }

  public static final long MIN_VALUE = 0x00000000_00000000L; // 0
  public static final long MAX_VALUE = 0xFFFFFFFF_FFFFFFFFL; // 18,446,744,073,709,551,615
  public static final boolean isValid(final long value) { return true; }
  public static final void throwIfInvalid(final long value) { }
  public static final U64 of(final long value) { return new U64(value); }
}
