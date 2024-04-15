package zfg.num;

public class U32 implements Int<U32> {
  final int value;
  public U32(final int value) { this.value = value; }
  public U32 pos() { return this; }
  public U32 neg() { throw new UnsupportedOperationException(); }
  public U32 not() { return new U32(~value); }
  public U32 inc() { return new U32(value + 1); }
  public U32 dec() { return new U32(value - 1); }
  public U32 add(final U32 v) { return new U32(value + v.value); }
  public U32 sub(final U32 v) { return new U32(value - v.value); }
  public U32 mul(final U32 v) { return new U32(value * v.value); }
  public U32 div(final U32 v) { return new U32((int)((value & 0xFFFFFFFFL) / (v.value & 0xFFFFFFFFL))); }
  public U32 rem(final U32 v) { return new U32((int)((value & 0xFFFFFFFFL) % (v.value & 0xFFFFFFFFL))); }
  public U32 mod(final U32 v) { return new U32((int)((value & 0xFFFFFFFFL) % (v.value & 0xFFFFFFFFL))); }
  public U32 and(final U32 v) { return new U32(value & v.value); }
  public U32 ior(final U32 v) { return new U32(value | v.value); }
  public U32 xor(final U32 v) { return new U32(value ^ v.value); }
  public U32 shl(final I32 v) { return new U32(value << v.value); }
  public U32 shr(final I32 v) { return new U32(value >>> v.value); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Integer.toUnsignedString(value, 10); }
  public String asBinString() { return "0b" + Integer.toBinaryString(value); }
  public String asOctString() { return "0o" + Integer.toOctalString(value); }
  public String asHexString() { return "0x" + Integer.toHexString(value); }
}
