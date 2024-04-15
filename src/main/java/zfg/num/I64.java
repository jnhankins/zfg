package zfg.num;

public class I64 implements Int<I64> {
  final long value;
  public I64(final long value) { this.value = value; }
  public I64 pos() { return this; }
  public I64 neg() { return new I64(-value); }
  public I64 not() { return new I64(~value); }
  public I64 inc() { return new I64(value + 1); }
  public I64 dec() { return new I64(value - 1); }
  public I64 add(final I64 v) { return new I64(value + v.value); }
  public I64 sub(final I64 v) { return new I64(value - v.value); }
  public I64 mul(final I64 v) { return new I64(value * v.value); }
  public I64 div(final I64 v) { return new I64(value / v.value); }
  public I64 rem(final I64 v) { return new I64(value % v.value); }
  public I64 mod(final I64 v) { return new I64(((value % v.value) + v.value) % v.value); }
  public I64 and(final I64 v) { return new I64(value & v.value); }
  public I64 ior(final I64 v) { return new I64(value | v.value); }
  public I64 xor(final I64 v) { return new I64(value ^ v.value); }
  public I64 shl(final I32 v) { return new I64(value << v.value); }
  public I64 shr(final I32 v) { return new I64(value >> v.value); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Long.toString(value, 10); }
  public String asBinString() { return "0b" + Long.toBinaryString(value) + "i64"; }
  public String asOctString() { return "0o" + Long.toOctalString(value) + "i64"; }
  public String asHexString() { return "0x" + Long.toHexString(value) + "i64"; }
}
