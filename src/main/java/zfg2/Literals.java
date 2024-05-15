package zfg2;

public final class Literals {
  private Literals() {}

  private static enum IntKind { BIT, U08, U16, U32, U64, I08, I16, I32, I64 }
  private static enum FltKind { F32, F64 }

  // Must be "true" or "false"; case-sensitive. Returns null if it could not be parsed as a Bit.
  public static final Inst.Bit parseBitLit(final String s) {
    if (s.equals("false")) return Insts.bit(0);
    if (s.equals("true")) return Insts.bit(1);
    return null;
  }

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
  public static final Inst parseIntLit(final String s, final boolean hasMinusPrefix) {
    final boolean hmp = hasMinusPrefix;
    final int len = s.length();

    final int radix;
    final int beg;
    if      (s.startsWith("0b")) { radix =  2; beg = 2; }
    else if (s.startsWith("0o")) { radix =  8; beg = 2; }
    else if (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix = 16; beg = 2; }
    else                         { radix = 10; beg = 0; }

    final IntKind kind;
    final int end;
    if      (s.endsWith("bit")) { kind = IntKind.BIT; end = len - 3; }
    else if (s.endsWith("i08")) { kind = IntKind.I08; end = len - 3; }
    else if (s.endsWith("i16")) { kind = IntKind.I16; end = len - 3; }
    else if (s.endsWith("i32")) { kind = IntKind.I32; end = len - 3; }
    else if (s.endsWith("i64")) { kind = IntKind.I64; end = len - 3; }
    else if (s.endsWith("u08")) { kind = IntKind.U08; end = len - 3; }
    else if (s.endsWith("u16")) { kind = IntKind.U16; end = len - 3; }
    else if (s.endsWith("u32")) { kind = IntKind.U32; end = len - 3; }
    else if (s.endsWith("u64")) { kind = IntKind.U64; end = len - 3; }
    else                        { kind = IntKind.I32; end = len; }

    final long v;
    final String t = s.substring(beg, end).replace("_", "");
    try { v = Long.parseUnsignedLong(t, radix); }
    catch (final NumberFormatException e) { return null; }

    return switch (kind) {
      case IntKind.BIT -> v >= 0 && v <=          1L ? Insts.bit((int) v) : null;
      case IntKind.U08 -> v >= 0 && v <=       0xFFL ? Insts.u08((int) v) : null;
      case IntKind.U16 -> v >= 0 && v <=     0xFFFFL ? Insts.u16((int) v) : null;
      case IntKind.U32 -> v >= 0 && v <= 0xFFFFFFFFL ? Insts.u32((int) v) : null;
      case IntKind.U64 ->                              Insts.u64(      v);
      case IntKind.I08 -> v >= 0 && v <= (radix != 10 ?       0xFFL : hmp ?       0x80L :       0x7FL) ? Insts.i08((int) v) : null;
      case IntKind.I16 -> v >= 0 && v <= (radix != 10 ?     0xFFFFL : hmp ?     0x8000L :     0x7FFFL) ? Insts.i16((int) v) : null;
      case IntKind.I32 -> v >= 0 && v <= (radix != 10 ? 0xFFFFFFFFL : hmp ? 0x80000000L : 0x7FFFFFFFL) ? Insts.i32((int) v) : null;
      case IntKind.I64 -> v >= 0 || radix != 10 || (hmp && v == 0x8000000000000000L)                   ? Insts.i64(      v) : null;
    };
  }

  public static final Inst parseFltLit(final String s) {
    final int len = s.length();

    @SuppressWarnings("unused")
    final int radix;
    final int beg;
    if      (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix =  2; beg = 0; }
    else                         { radix = 10; beg = 0; }

    final FltKind kind;
    final int end;
    if      (s.endsWith("f32")) { kind = FltKind.F32; end = len - 3; }
    else if (s.endsWith("f64")) { kind = FltKind.F64; end = len - 3; }
    else                        { kind = FltKind.F64; end = len; }

    final String t = s.substring(beg, end).replace("_", "");
    return switch (kind) {
      case FltKind.F32 -> {
        final float v;
        try { v = Float.parseFloat(t); }
        catch (final NumberFormatException e) { yield null; }
        yield Insts.f32(v);
      }
      case FltKind.F64 -> {
        final double v;
        try { v = Double.parseDouble(t); }
        catch (final NumberFormatException e) { yield null; }
        yield Insts.f64(v);
      }
    };
  }
}
