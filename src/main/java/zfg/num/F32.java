package zfg.num;

public class F32 implements Flt<F32> {
  final float value;
  public F32(final float value) { this.value = value; }
  public F32 pos() { return this; }
  public F32 neg() { return new F32(-value); }
  public F32 add(final F32 v) { return new F32(value + v.value); }
  public F32 sub(final F32 v) { return new F32(value - v.value); }
  public F32 mul(final F32 v) { return new F32(value * v.value); }
  public F32 div(final F32 v) { return new F32(value / v.value); }
  public F32 rem(final F32 v) { return new F32(value % v.value); }
  public F32 mod(final F32 v) { return new F32(((value % v.value) + v.value) % v.value); }
  public String asString() { return asDecString(); }
  public String asDecString() { return Float.toString(value) + "f32"; }
  public String asHexString() { return Float.toHexString(value) + "f32"; }
}
