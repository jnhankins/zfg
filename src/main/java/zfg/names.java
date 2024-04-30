package zfg;

public final class names {
  private names() {}

  public static final boolean isLowerSnakeCase(final String name) {
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

  public static final boolean isUpperCamelCase(final String name) {
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

}
