package zfg.lang.primitive;

public final class Parser {
  private Parser() {}

  public static final Bit parseBit(final String s) {
    return switch (s) {
      case "true" -> Bit.TRUE;
      case "false" -> Bit.FALSE;
      default -> null;
    };
  }

  public static sealed interface Int permits Bit, U08, U16, U32, U64, I08, I16, I32, I64 {}
  public static final Int parseInt(final String s, final boolean neg) {
    final int len =  s.length();

    final int radix;
    final int beg;
    if      (s.startsWith("0b")) { radix =  2; beg = 2; }
    else if (s.startsWith("0o")) { radix =  8; beg = 2; }
    else if (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix = 16; beg = 2; }
    else                         { radix = 10; beg = 0; }

    final zfg.ast.Type.Int type;
    final int end;
    if      (s.endsWith("bit")) { type = zfg.ast.Type.bit; end = len - 3; }
    else if (s.endsWith("i08")) { type = zfg.ast.Type.i08; end = len - 3; }
    else if (s.endsWith("i16")) { type = zfg.ast.Type.i16; end = len - 3; }
    else if (s.endsWith("i32")) { type = zfg.ast.Type.i32; end = len - 3; }
    else if (s.endsWith("i64")) { type = zfg.ast.Type.i64; end = len - 3; }
    else if (s.endsWith("u08")) { type = zfg.ast.Type.u08; end = len - 3; }
    else if (s.endsWith("u16")) { type = zfg.ast.Type.u16; end = len - 3; }
    else if (s.endsWith("u32")) { type = zfg.ast.Type.u32; end = len - 3; }
    else if (s.endsWith("u64")) { type = zfg.ast.Type.u64; end = len - 3; }
    else                        { type = zfg.ast.Type.i32; end = len; }

    final long v;
    final String t = s.substring(beg, end).replace("_", "");
    try { v = Long.parseUnsignedLong(t, radix); }
    catch (final NumberFormatException e) { return null; }

    // Ixx literals have ranges that extend below zero, e.g. I08 has range [-128, 127], however at
    // the time of parsing, beacuse of the lexer and parser's grammar, the minus sign is not
    // available to this function while parsing the literal.
    //
    // For non-base-10 literals, the minus sign can be encoded in the literal, e.g. "0x80" is 128
    // which will be interpreted as -128 in I08. If the minus sign is included before a non-decimal
    // literal it will be interpreted as an arithmetic negation, e.g. "-0xFF" is -(-1) = 1.
    //
    // For base-10 literals, we allow the range to be from 0 to one more then the type's actual
    // maximum value, e.g. I08 has range [-128, 127], but for parsing we allow [0, 128]. It is then
    // up to the caller of this method to check if there was a minus sign before the literal decide
    // if a NumberFormatException should be thrown. For example, "x = a - 128i08" will be parsed as
    // ... subtract I08(-128), which should then throw an exception, while "x = -128i08" will be
    // parsed as ... negate I08(-128), which is allowed and should simplify to I(-128).
    return switch (type) {
      case zfg.ast.Type.Bit x -> v >= 0 && v <=          1L ? new Bit((int) v) : null;
      case zfg.ast.Type.U08 x -> v >= 0 && v <=       0xFFL ? new U08((int) v) : null;
      case zfg.ast.Type.U16 x -> v >= 0 && v <=     0xFFFFL ? new U16((int) v) : null;
      case zfg.ast.Type.U32 x -> v >= 0 && v <= 0xFFFFFFFFL ? new U32((int) v) : null;
      case zfg.ast.Type.U64 x -> new U64(v);
      case zfg.ast.Type.I08 x -> v >= 0 && v <= (radix != 10 ?       0xFFL : neg ?       0x80L :       0x7FL) ? new I08((int)v) : null;
      case zfg.ast.Type.I16 x -> v >= 0 && v <= (radix != 10 ?     0xFFFFL : neg ?     0x8000L :     0x7FFFL) ? new I16((int)v) : null;
      case zfg.ast.Type.I32 x -> v >= 0 && v <= (radix != 10 ? 0xFFFFFFFFL : neg ? 0x80000000L : 0x7FFFFFFFL) ? new I32((int)v) : null;
      case zfg.ast.Type.I64 x -> v >= 0 || radix != 10 || (neg && v == 0x8000000000000000L)                   ? new I64(v)      : null;
      default -> throw new AssertionError();
    };
  }


  public static sealed interface Flt permits F32, F64 {}
  public static final Flt parseFlt(final String s) {
    final int len = s.length();

    @SuppressWarnings("unused")
    final int radix;
    final int beg;
    if      (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix =  2; beg = 0; }
    else                         { radix = 10; beg = 0; }

    final zfg.ast.Type.Fxx type;
    final int end;
    if      (s.endsWith("f32")) { type = zfg.ast.Type.f32; end = len - 3; }
    else if (s.endsWith("f64")) { type = zfg.ast.Type.f64; end = len - 3; }
    else                        { type = zfg.ast.Type.f64; end = len; }

    final String t = s.substring(beg, end).replace("_", "");
    try {
      return switch (type) {
        case zfg.ast.Type.F32 x -> new F32(Float.parseFloat(t));
        case zfg.ast.Type.F64 x -> new F64(Double.parseDouble(t));
        default -> throw new AssertionError();
      };
    } catch (final NumberFormatException e) {
      return null;
    }
  }
}
