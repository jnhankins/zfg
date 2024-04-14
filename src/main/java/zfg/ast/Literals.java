package zfg.ast;

public interface Literals {

  public static abstract class IntLit {

  }

  public static abstract class FltLit {

  }

  public static class BitLit {
    final boolean value;

    public BitLit(final boolean value) {
      this.value = value;
    }
  }

  public static class I08t {
    final byte value;

    public I08Lit(final byte value) {
      this.value = value;
    }
  }

  public static class I32Lit {
    final int value;

    public I32Lit(final int value) {
      this.value = value;
    }
  }

  public static class I64Lit {
    final long value;

    public I64Lit(final long value) {
      this.value = value;
    }
  }

  public static class F32Lit {
    final float value;

    public F32Lit(final float value) {
      this.value = value;
    }
  }

  public static class F64Lit {
    final double value;

    public F64Lit(final double value) {
      this.value = value;
    }
  }

  /**
   * @return unsigned long
   */
  public static long parseIntLit(final String s) {
    final int len = s.length(); // length of input string
    int radix; // radix
    int index; // index of current character
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

    // skip leading zeros
    char c = s.charAt(index++);
    while (c == '0') {
      if (index == len) return 0;
      c = s.charAt(index++);
    }

    long value = 0;
    do {
      long digit = c - (c <= '9' ? '0' : 'A');

      if (c >= '0' && c <= '9') {
        digit = c - '0';
      } else if (c >= 'a' && c <= 'z') {
        digit = c - 'a' + 10;
      } else if (c >= 'A' && c <= 'Z') {
        digit = c - 'A' + 10;
      } else {
        throw new NumberFormatException("Invalid digit: " + c);
      }
      if (digit >= radix) {
        throw new NumberFormatException("Digit not in radix: " + c);
      }
      value = value * radix + digit;
    } while (index < len && (c = s.charAt(index++)) != '_');


  }

}
