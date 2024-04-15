package zfg.num;

public class U16 implements Int<U16> {
  final short value;
  public U16(final short value) { this.value = value; }
  public U16 pos() { return this; }
  public U16 neg() { throw new UnsupportedOperationException(); }
  public U16 not() { return new U16((short)~(value & 0xFFFF)); }
  public U16 inc() { return new U16((short)((value & 0xFFFF) + 1)); }
  public U16 dec() { return new U16((short)((value & 0xFFFF) - 1)); }
  public U16 add(final U16 v) { return new U16((short)((value & 0xFFFF) + (v.value & 0xFFFF))); }
  public U16 sub(final U16 v) { return new U16((short)((value & 0xFFFF) - (v.value & 0xFFFF))); }
  public U16 mul(final U16 v) { return new U16((short)((value & 0xFFFF) * (v.value & 0xFFFF))); }
  public U16 div(final U16 v) { return new U16((short)((value & 0xFFFF) / (v.value & 0xFFFF))); }
  public U16 rem(final U16 v) { return new U16((short)((value & 0xFFFF) % (v.value & 0xFFFF))); }
  public U16 mod(final U16 v) { return new U16((short)((value & 0xFFFF) % (v.value & 0xFFFF))); }
  public U16 and(final U16 v) { return new U16((short)((value & 0xFFFF) & (v.value & 0xFFFF))); }
  public U16 ior(final U16 v) { return new U16((short)((value & 0xFFFF) | (v.value & 0xFFFF))); }
  public U16 xor(final U16 v) { return new U16((short)((value & 0xFFFF) ^ (v.value & 0xFFFF))); }
  public U16 shl(final I32 v) { return new U16((short)((value & 0xFFFF) << v.value)); }
  public U16 shr(final I32 v) { return new U16((short)((value & 0xFFFF) >>> v.value)); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Integer.toString(value, 10) + "i16"; }
  public String asBinString() { return "0b" + Integer.toBinaryString(value & 0xFFFF) + "i16"; }
  public String asOctString() { return "0o" + Integer.toOctalString(value & 0xFFFF) + "i16"; }
  public String asHexString() { return "0x" + Integer.toHexString(value & 0xFFFF) + "i16"; }
}
