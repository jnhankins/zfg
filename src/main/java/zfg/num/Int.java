package zfg.num;

import java.util.Optional;

public interface Int<T extends Int<T>> extends Num<T> {
  public T pos();
  public T neg();
  public T not();

  public T inc();
  public T dec();

  public T and(T v);
  public T ior(T v);
  public T xor(T v);
  public T shl(I32 v);
  public T shr(I32 v);

  public String asBinString();
  public String asOctString();
  public String asDecString();
  public String asHexString();

  public static enum Type { BIT, I08, I16, I32, I64, U08, U16, U32, U64 };

  @SuppressWarnings("rawtypes")
  public static Optional<Int> parseInt(final String s) {
    final int len =  s.length();

    final int radix;
    final int beg;
    if      (s.startsWith("0b")) { radix =  2; beg = 2; }
    else if (s.startsWith("0o")) { radix =  8; beg = 2; }
    else if (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix = 16; beg = 2; }
    else                         { radix = 10; beg = 0; }

    final Type type;
    final int end;
    if      (s.endsWith("bit")) { type = Type.BIT; end = len - 3; }
    else if (s.endsWith("i08")) { type = Type.I08; end = len - 3; }
    else if (s.endsWith("i16")) { type = Type.I16; end = len - 3; }
    else if (s.endsWith("i32")) { type = Type.I32; end = len - 3; }
    else if (s.endsWith("i64")) { type = Type.I64; end = len - 3; }
    else if (s.endsWith("u08")) { type = Type.U08; end = len - 3; }
    else if (s.endsWith("u16")) { type = Type.U16; end = len - 3; }
    else if (s.endsWith("u32")) { type = Type.U32; end = len - 3; }
    else if (s.endsWith("u64")) { type = Type.U64; end = len - 3; }
    else                        { type = Type.I64; end = len; }

    final long v;
    final String t = s.substring(beg, end).replace("_", "");
    try { v = Long.parseUnsignedLong(t, radix); }
    catch (final NumberFormatException e) { return Optional.empty(); }

    switch (type) {
      case BIT:
        if (v != 0L && v != 1L) return Optional.empty();
        return Optional.of(new Bit(v == 1L));
      case I08:
        if (v < 0L || v > (radix == 10 ? 0x7FL : 0xFFL)) return Optional.empty();
        return Optional.of(new I08((byte)v));
      case I16:
        if (v < 0L || v > (radix == 10 ? 0x7FFFL : 0xFFFFL)) return Optional.empty();
        return Optional.of(new I16((short)v));
      case I32:
        if (v < 0L || v > (radix == 10 ? 0x7FFFFFFFL : 0xFFFFFFFFL)) return Optional.empty();
        return Optional.of(new I32((int)v));
      case I64:
        if (radix == 10 && v > 0x7FFFFFFFFFFFFFFFL) return Optional.empty();
        return Optional.of(new I64(v));
      case U08:
        if (v < 0L || v > 0xFFL) return Optional.empty();
        return Optional.of(new U08((byte)v));
      case U16:
        if (v < 0L || v > 0xFFFFL) return Optional.empty();
        return Optional.of(new U16((short)v));
      case U32:
        if (v < 0L || v > 0xFFFFFFFFL) return Optional.empty();
        return Optional.of(new U32((int)v));
      case U64:
        return Optional.of(new U64(v));
      default: throw new AssertionError();
    }
  }
}
