package zfg.num;

import java.util.Optional;

public interface Flt<T extends Flt<T>> extends Num<T> {
  public String asDecString();
  public String asHexString();

  public static enum Type { F32, F64 };

  @SuppressWarnings("rawtypes")
  public static Optional<Flt> parseFlt(final String s) {
    final int len = s.length();

    @SuppressWarnings("unused")
    final int radix;
    final int beg;
    if      (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix =  2; beg = 0; }
    else                         { radix = 10; beg = 0; }

    final Type type;
    final int end;
    if      (s.endsWith("f32")) { type = Type.F32; end = len - 3; }
    else if (s.endsWith("f64")) { type = Type.F64; end = len - 3; }
    else                        { type = Type.F64; end = len; }

    final String t = s.substring(beg, end).replace("_", "");
    try {
      switch (type) {
        case F32: return Optional.of(new F32(Float.parseFloat(t)));
        case F64: return Optional.of(new F64(Double.parseDouble(t)));
        default: throw new AssertionError();
      }
    } catch (final NumberFormatException e) {
      return Optional.empty();
    }
  }
}
