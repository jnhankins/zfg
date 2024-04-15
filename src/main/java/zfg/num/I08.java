package zfg.num;

public class I08 implements Int<I08> {
  final byte value;
  public I08(final byte value) { this.value = value; }
  public I08 pos() { return this; }
  public I08 neg() { return new I08((byte)-value); }
  public I08 not() { return new I08((byte)~value); }
  public I08 inc() { return new I08((byte)(value + 1)); }
  public I08 dec() { return new I08((byte)(value - 1)); }
  public I08 add(final I08 v) { return new I08((byte)(value + v.value)); }
  public I08 sub(final I08 v) { return new I08((byte)(value - v.value)); }
  public I08 mul(final I08 v) { return new I08((byte)(value * v.value)); }
  public I08 div(final I08 v) { return new I08((byte)(value / v.value)); }
  public I08 rem(final I08 v) { return new I08((byte)(value % v.value)); }
  public I08 mod(final I08 v) { return new I08((byte)(((value % v.value) + v.value) % v.value)); }
  public I08 and(final I08 v) { return new I08((byte)(value & v.value)); }
  public I08 ior(final I08 v) { return new I08((byte)(value | v.value)); }
  public I08 xor(final I08 v) { return new I08((byte)(value ^ v.value)); }
  public I08 shl(final I32 v) { return new I08((byte)(value << v.value)); }
  public I08 shr(final I32 v) { return new I08((byte)(value >> v.value)); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Integer.toString(value, 10) + "i08"; }
  public String asBinString() { return "0b" + Integer.toBinaryString(value & 0xFF) + "i08"; }
  public String asOctString() { return "0o" + Integer.toOctalString(value & 0xFF) + "i08"; }
  public String asHexString() { return "0x" + Integer.toHexString(value & 0xFF) + "i08"; }
}
