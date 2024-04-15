package zfg.num;

import java.util.Optional;

public class Bit implements Int<Bit> {
  final boolean value;
  public Bit(final boolean value) { this.value = value; }

  public Bit pos() { return this; }
  public Bit neg() { throw new UnsupportedOperationException(); }
  public Bit not() { return new Bit((~(value ? 0 : 1)) == 1 ? true : false); }
  public Bit inc() { return new Bit((((value ? 0 : 1) + 1)) == 1 ? true : false); }
  public Bit dec() { return new Bit((((value ? 0 : 1) - 1)) == 1 ? true : false); }
  public Bit add(final Bit v) { return new Bit((((value ? 0 : 1) + (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit sub(final Bit v) { return new Bit((((value ? 0 : 1) - (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit mul(final Bit v) { return new Bit((((value ? 0 : 1) * (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit div(final Bit v) { return new Bit((((value ? 0 : 1) / (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit rem(final Bit v) { return new Bit((((value ? 0 : 1) % (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit mod(final Bit v) { return new Bit((((value ? 0 : 1) % (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit and(final Bit v) { return new Bit((((value ? 0 : 1) & (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit ior(final Bit v) { return new Bit((((value ? 0 : 1) | (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit xor(final Bit v) { return new Bit((((value ? 0 : 1) ^ (v.value ? 0 : 1))) == 1 ? true : false); }
  public Bit shl(final I32 v) { return new Bit((((value ? 0 : 1) << v.value)) == 1 ? true : false); }
  public Bit shr(final I32 v) { return new Bit((((value ? 0 : 1) >>> v.value)) == 1 ? true : false); }

  public String asString() { return asDecString(); }
  public String asDecString() { return Integer.toString(value? 1 : 0, 10) + "u16"; }
  public String asBinString() { return "0b" + Integer.toBinaryString(value ? 1 : 0) + "u16"; }
  public String asOctString() { return "0o" + Integer.toOctalString(value ? 1 : 0) + "u16"; }
  public String asHexString() { return "0x" + Integer.toHexString(value ? 1 : 0) + "u16"; }

  public static Optional<Bit> parseBit(final String s) {
    return switch (s) {
      case "true" -> Optional.of(new Bit(true));
      case "false" -> Optional.of(new Bit(false));
      default -> Optional.empty();
    };
  }
}
