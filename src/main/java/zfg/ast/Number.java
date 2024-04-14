package zfg.ast;

public interface Number {
  public static interface Int extends Number {}
  public static class Bit implements Int {
    public final boolean value;
    public Bit(final boolean value) { this.value = value; }
  }
  public static class I08 implements Int {
    public final byte value;
    public I08(final byte value) { this.value = value; }
  }
  public static class I16 implements Int {
    public final short value;
    public I16(final short value) { this.value = value; }
  }
  public static class I32 implements Int {
    public final int value;
    public I32(final int value) { this.value = value; }
  }
  public static class I64 implements Int {
    public final long value;
    public I64(final long value) { this.value = value; }
  }

  public static interface Flt extends Number {}
  public static class F32 implements Flt {
    public final float value;
    public F32(final float value) { this.value = value; }
  }
  public static class F64 implements Flt {
    public final double value;
    public F64(final double value) { this.value = value; }
  }

  public static Bit parseBit(final String s) {
    return new Bit(switch (s) {
      case "true" -> true;
      case "false" -> false;
      default -> throw new RuntimeException();
    });
  }

  public static Int parseInt(final String s) {
    final int nbits;
    final int length;
    if (s.endsWith("bit")) {
      nbits = 1;
      length = s.length() - 3;
    } else if (s.endsWith("i08")) {
      nbits = 8;
      length = s.length() - 3;
    } else if (s.endsWith("i16")) {
      nbits = 16;
      length = s.length() - 3;
    } else if (s.endsWith("i32")) {
      nbits = 32;
      length = s.length() - 3;
    } else if (s.endsWith("i64")) {
      nbits = 64;
      length = s.length() - 3;
    } else {
      nbits = 32;
      length = s.length();
    }

    final int radix;
    int index;
    if (s.startsWith("0b")) {
      radix = 2;
      index = 2;
    } else if (s.startsWith("0o")) {
      radix = 8;
      index = 2;
    } else if (s.startsWith("0d")) {
      radix = 10;
      index = 2;
    } else if (s.startsWith("0x")) {
      radix = 16;
      index = 2;
    } else {
      radix = 10;
      index = 0;
    }


    long test = -9223372036854775808L;
    long b = test + -9223372036854775808L;


  }

  public static Flt parseFlt(final String s) {
    final int bits
  }
}
