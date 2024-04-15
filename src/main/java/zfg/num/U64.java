package zfg.num;

public class U64 implements Int<U64> {
  final long value;
  public U64(final long value) { this.value = value; }
  public U64 pos() { return this; }
  public U64 neg() { throw new UnsupportedOperationException(); }
  public U64 not() { return new U64(~value); }
  public U64 inc() { return new U64(value + 1); }
  public U64 dec() { return new U64(value - 1); }
  public U64 add(final U64 v) { return new U64(value + v.value); }
  public U64 sub(final U64 v) { return new U64(value - v.value); }
  public U64 mul(final U64 v) { return new U64(value * v.value); }
  public U64 div(final U64 v) { return new U64(Long.divideUnsigned(value, v.value)); }
  public U64 rem(final U64 v) { return new U64(Long.remainderUnsigned(value, v.value)); }
  public U64 mod(final U64 v) { return new U64(Long.remainderUnsigned(value, v.value)); }
  public U64 and(final U64 v) { return new U64(value & v.value); }
  public U64 ior(final U64 v) { return new U64(value | v.value); }
  public U64 xor(final U64 v) { return new U64(value ^ v.value); }
  public U64 shl(final I32 v) { return new U64(value << v.value); }
  public U64 shr(final I32 v) { return new U64(value >>> v.value); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Long.toUnsignedString(value, 10); }
  public String asBinString() { return "0b" + Long.toBinaryString(value) + "u64"; }
  public String asOctString() { return "0o" + Long.toOctalString(value) + "u64"; }
  public String asHexString() { return "0x" + Long.toHexString(value) + "u64"; }
}

