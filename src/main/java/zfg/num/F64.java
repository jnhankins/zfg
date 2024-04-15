package zfg.num;

public class F64 implements Flt<F64> {
  final double value;
  public F64(final double value) { this.value = value; }
  public F64 pos() { return this; }
  public F64 neg() { return new F64(-value); }
  public F64 add(final F64 v) { return new F64(value + v.value); }
  public F64 sub(final F64 v) { return new F64(value - v.value); }
  public F64 mul(final F64 v) { return new F64(value * v.value); }
  public F64 div(final F64 v) { return new F64(value / v.value); }
  public F64 rem(final F64 v) { return new F64(value % v.value); }
  public F64 mod(final F64 v) { return new F64(((value % v.value) + v.value) % v.value); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Double.toString(value); }
  public String asHexString() { return Double.toHexString(value); }
}
