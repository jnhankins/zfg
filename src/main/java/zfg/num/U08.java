package zfg.num;

public class U08 implements Int<U08> {
  final byte value;
  public U08(final byte value) { this.value = value; }
  public U08 pos() { return this; }
  public U08 neg() { throw new UnsupportedOperationException(); }
  public U08 not() { return new U08((byte)~(value & 0xFF)); }
  public U08 inc() { return new U08((byte)((value & 0xFF) + 1)); }
  public U08 dec() { return new U08((byte)((value & 0xFF) - 1)); }
  public U08 add(final U08 v) { return new U08((byte)((value & 0xFF) + (v.value & 0xFF))); }
  public U08 sub(final U08 v) { return new U08((byte)((value & 0xFF) - (v.value & 0xFF))); }
  public U08 mul(final U08 v) { return new U08((byte)((value & 0xFF) * (v.value & 0xFF))); }
  public U08 div(final U08 v) { return new U08((byte)((value & 0xFF) / (v.value & 0xFF))); }
  public U08 rem(final U08 v) { return new U08((byte)((value & 0xFF) % (v.value & 0xFF))); }
  public U08 mod(final U08 v) { return new U08((byte)((value & 0xFF) % (v.value & 0xFF))); }
  public U08 and(final U08 v) { return new U08((byte)((value & 0xFF) & (v.value & 0xFF))); }
  public U08 ior(final U08 v) { return new U08((byte)((value & 0xFF) | (v.value & 0xFF))); }
  public U08 xor(final U08 v) { return new U08((byte)((value & 0xFF) ^ (v.value & 0xFF))); }
  public U08 shl(final I32 v) { return new U08((byte)((value & 0xFF) << v.value)); }
  public U08 shr(final I32 v) { return new U08((byte)((value & 0xFF) >>> v.value)); }

  public String asString() { return asDecString(); }
  public String asDecString() { return Integer.toString(value, 10) + "u16"; }
  public String asBinString() { return "0b" + Integer.toBinaryString(value & 0xFF) + "u16"; }
  public String asOctString() { return "0o" + Integer.toOctalString(value & 0xFF) + "u16"; }
  public String asHexString() { return "0x" + Integer.toHexString(value & 0xFF) + "u16"; }
}
