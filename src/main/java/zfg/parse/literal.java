package zfg.parse;

import zfg.core.inst;
import zfg.core.type;

public final class literal {

  // Must be "true" or "false"; case-sensitive. Returns null if it could not be parsed as a Bit.
  public static final inst.Bit parseBitLit(final String s) {
    if (s.equals("false")) return type.Bit.of(0);
    if (s.equals("true")) return type.Bit.of(1);
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
  public static final inst.Inst<?> parseIntLit(final String s, final boolean hasMinusPrefix) {
    final boolean hmp = hasMinusPrefix;
    final int len = s.length();

    final int radix;
    final int beg;
    if      (s.startsWith("0b")) { radix =  2; beg = 2; }
    else if (s.startsWith("0o")) { radix =  8; beg = 2; }
    else if (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix = 16; beg = 2; }
    else                         { radix = 10; beg = 0; }

    final type.Kind kind;
    final int end;
    if      (s.endsWith("bit")) { kind = type.Kind.BIT; end = len - 3; }
    else if (s.endsWith("i08")) { kind = type.Kind.I08; end = len - 3; }
    else if (s.endsWith("i16")) { kind = type.Kind.I16; end = len - 3; }
    else if (s.endsWith("i32")) { kind = type.Kind.I32; end = len - 3; }
    else if (s.endsWith("i64")) { kind = type.Kind.I64; end = len - 3; }
    else if (s.endsWith("u08")) { kind = type.Kind.U08; end = len - 3; }
    else if (s.endsWith("u16")) { kind = type.Kind.U16; end = len - 3; }
    else if (s.endsWith("u32")) { kind = type.Kind.U32; end = len - 3; }
    else if (s.endsWith("u64")) { kind = type.Kind.U64; end = len - 3; }
    else                        { kind = type.Kind.I32; end = len; }

    final long v;
    final String t = s.substring(beg, end).replace("_", "");
    try { v = Long.parseUnsignedLong(t, radix); }
    catch (final NumberFormatException e) { return null; }

    switch (kind) {
      case type.Kind.BIT: return v >= 0 && v <=          1L ? type.Bit.ofUnchecked((int) v) : null;
      case type.Kind.U08: return v >= 0 && v <=       0xFFL ? type.U08.ofUnchecked((int) v) : null;
      case type.Kind.U16: return v >= 0 && v <=     0xFFFFL ? type.U16.ofUnchecked((int) v) : null;
      case type.Kind.U32: return v >= 0 && v <= 0xFFFFFFFFL ? type.U32.ofUnchecked((int) v) : null;
      case type.Kind.U64: return                              type.U64.ofUnchecked(      v);
      case type.Kind.I08: return v >= 0 && v <= (radix != 10 ?       0xFFL : hmp ?       0x80L :       0x7FL) ? type.I08.ofUnchecked((int) v) : null;
      case type.Kind.I16: return v >= 0 && v <= (radix != 10 ?     0xFFFFL : hmp ?     0x8000L :     0x7FFFL) ? type.I16.ofUnchecked((int) v) : null;
      case type.Kind.I32: return v >= 0 && v <= (radix != 10 ? 0xFFFFFFFFL : hmp ? 0x80000000L : 0x7FFFFFFFL) ? type.I32.ofUnchecked((int) v) : null;
      case type.Kind.I64: return v >= 0 || radix != 10 || (hmp && v == 0x8000000000000000L)                   ? type.I64.ofUnchecked(      v) : null;
      default: throw new AssertionError();
    }
  }

  public static final inst.Inst<?> parseFltLit(final String s) {
    final int len = s.length();

    @SuppressWarnings("unused")
    final int radix;
    final int beg;
    if      (s.startsWith("0d")) { radix = 10; beg = 2; }
    else if (s.startsWith("0x")) { radix =  2; beg = 0; }
    else                         { radix = 10; beg = 0; }

    final type.Kind kind;
    final int end;
    if      (s.endsWith("f32")) { kind = type.Kind.F32; end = len - 3; }
    else if (s.endsWith("f64")) { kind = type.Kind.F64; end = len - 3; }
    else                        { kind = type.Kind.F64; end = len; }

    final String t = s.substring(beg, end).replace("_", "");
    switch (kind) {
      case type.Kind.F32: {
        final float v;
        try { v = Float.parseFloat(t); }
        catch (final NumberFormatException e) { return null; }
        return type.F32.of(v);
      }
      case type.Kind.F64: {
        final double v;
        try { v = Double.parseDouble(t); }
        catch (final NumberFormatException e) { return null; }
        return type.F64.of(v);
      }
      default: throw new AssertionError();
    }
  }

  // module
  private literal() {}
}
