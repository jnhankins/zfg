package zfg.num;

public class I32 implements Int<I32> {
  final int value;
  public I32(final int value) { this.value = value; }
  public I32 pos() { return this; }
  public I32 neg() { return new I32(-value); }
  public I32 not() { return new I32(~value); }
  public I32 inc() { return new I32(value + 1); }
  public I32 dec() { return new I32(value - 1); }
  public I32 add(final I32 v) { return new I32(value + v.value); }
  public I32 sub(final I32 v) { return new I32(value - v.value); }
  public I32 mul(final I32 v) { return new I32(value * v.value); }
  public I32 div(final I32 v) { return new I32(value / v.value); }
  public I32 rem(final I32 v) { return new I32(value % v.value); }
  public I32 mod(final I32 v) { return new I32(((value % v.value) + v.value) % v.value); }
  public I32 and(final I32 v) { return new I32(value & v.value); }
  public I32 ior(final I32 v) { return new I32(value | v.value); }
  public I32 xor(final I32 v) { return new I32(value ^ v.value); }
  public I32 shl(final I32 v) { return new I32(value << v.value); }
  public I32 shr(final I32 v) { return new I32(value >> v.value); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Integer.toString(value, 10); }
  public String asBinString() { return "0b" + Integer.toBinaryString(value); }
  public String asOctString() { return "0o" + Integer.toOctalString(value); }
  public String asHexString() { return "0x" + Integer.toHexString(value); }
}
