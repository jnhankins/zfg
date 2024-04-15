package zfg.num;

public class I16 implements Int<I16> {
  final short value;
  public I16(final short value) { this.value = value; }
  public I16 pos() { return this; }
  public I16 neg() { return new I16((short)-value); }
  public I16 not() { return new I16((short)~value); }
  public I16 inc() { return new I16((short)(value + 1)); }
  public I16 dec() { return new I16((short)(value - 1)); }
  public I16 add(final I16 v) { return new I16((short)(value + v.value)); }
  public I16 sub(final I16 v) { return new I16((short)(value - v.value)); }
  public I16 mul(final I16 v) { return new I16((short)(value * v.value)); }
  public I16 div(final I16 v) { return new I16((short)(value / v.value)); }
  public I16 rem(final I16 v) { return new I16((short)(value % v.value)); }
  public I16 mod(final I16 v) { return new I16((short)(((value % v.value) + v.value) % v.value)); }
  public I16 and(final I16 v) { return new I16((short)(value & v.value)); }
  public I16 ior(final I16 v) { return new I16((short)(value | v.value)); }
  public I16 xor(final I16 v) { return new I16((short)(value ^ v.value)); }
  public I16 shl(final I32 v) { return new I16((short)(value << v.value)); }
  public I16 shr(final I32 v) { return new I16((short)(value >> v.value)); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Integer.toString(value, 10) + "i16"; }
  public String asBinString() { return "0b" + Integer.toBinaryString(value & 0xFFFF) + "i16"; }
  public String asOctString() { return "0o" + Integer.toOctalString(value & 0xFFFF) + "i16"; }
  public String asHexString() { return "0x" + Integer.toHexString(value & 0xFFFF) + "i16"; }
}
