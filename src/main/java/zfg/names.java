package zfg;

public final class Names {
  private Names() {}

  public static final boolean isLowerSnakeCase(final CharSequence name) {
    // [a-z_][a-z0-9_]*
    if (name == null) return false;
    final int len = name.length();
    if (len == 0) return false;
    final char f = name.charAt(0);
    if (f != '_' && (f < 'a' || 'z' < f)) return false;
    for (int i = 0; i < len; i++) {
      final char c = name.charAt(i);
      if ('a' <= c && c <= 'z') continue;
      if ('0' <= c && c <= '9') continue;
      if (c == '_') continue;
      return false;
    }
    return true;
  }

  public static final boolean isUpperCamelCase(final CharSequence name) {
    // [A-Z][a-zA-Z0-9]*
    if (name == null) return false;
    final int len = name.length();
    if (len == 0) return false;
    final char f = name.charAt(0);
    if (f < 'A' || 'Z' < f) return false;
    if (name.charAt(0) < 'A' || name.charAt(0) > 'Z') return false;
    for (int i = 1; i < len; i++) {
      final char c = name.charAt(i);
      if ('a' <= c && c <= 'z') continue;
      if ('A' <= c && c <= 'Z') continue;
      if ('0' <= c && c <= '9') continue;
      return false;
    }
    return true;
  }

  public static final boolean isModulePath(final String name) {
    final int len = name.length();
    int pos = 0;
    while (pos < len) {
      final int beg = pos;
      final int end = name.indexOf("::", pos);
      if (end == -1 || beg == end) return false;
      if (!isUpperCamelCase(new Substring(name, beg, end))) return false;
      pos = end + 2;
    }
    return true;
  }

  private static final class Substring implements CharSequence {
    private final CharSequence s;
    private final int beg;
    private final int end;
    Substring(final CharSequence s, final int beg, final int end) {
      this.s = s;
      this.beg = beg;
      this.end = end;
    }
    @Override public int length() {
      return end - beg;
    }
    @Override public char charAt(final int index) {
      return s.charAt(beg + index);
    }
    @Override public CharSequence subSequence(final int start, final int stop) {
      return new Substring(s, beg + start, beg + stop);
    }
    @Override public String toString() {
      return s.subSequence(beg, end).toString();
    }
  }

}
