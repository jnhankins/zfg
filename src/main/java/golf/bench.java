package golf;



public final class bench {
  private bench() {}

  public final static void main(final String[] args) {
    System.out.println("start");
    for (int i = 1; i <= 256; i *= 2) test(i);
    System.out.println();
    for (int i = 1; i <= 256; i *= 2) test(i);
    System.out.println();
    for (int i = 1; i <= 256; i *= 2) test(i);
  }

  public static final void test(final int maxCnt) {
    System.out.println("\ntest:" + maxCnt);
    final java.util.Random rng = new java.util.Random(7);
    final int maxIter = 3*10000;
    final int maxLen = 10;

    long timeA = 0;
    long timeB = 0;
    long timeC = 0;
    long nuniq = 0;

    for (int i = 0; i < maxIter; i++) {
      final int cnt = maxCnt;
      final boolean isu = cnt < 2 || rng.nextBoolean();
      final String[] names = new String[cnt];
      for (int j = 0; j < cnt; j++) {
        final int len = maxLen;//rng.nextInt(1, maxLen+1);
        final char[] buf = new char[len];
        for (int k = 0; k < len; k++) buf[k] = (char) ('a' + rng.nextInt(0, 25));
        names[j] = new String(buf);
      }
      if (!isu) {
        final int x = rng.nextInt(0, cnt);
        final int y = rng.nextInt(0, cnt-1);
        final int z = y < x ? y : y+1;
        final char[] buf = names[x].toCharArray();
        names[z] = new String(buf);
      }

      final long tA = System.nanoTime();
      final boolean resA = uniqueA(names);
      timeA += System.nanoTime() - tA;
      if (resA) nuniq += 1;

      final long tB = System.nanoTime();
      final boolean resB = uniqueB(names);
      timeB += System.nanoTime() - tB;
      if (resB) nuniq += 1;

      final long tC = System.nanoTime();
      final boolean resC = uniqueC(names);
      timeC += System.nanoTime() - tC;
      if (resC) nuniq += 1;

      if (resA != resB || resA != resC) {
        System.out.println("error");
        System.out.println(java.util.Arrays.toString(names));
        System.out.println(resA);
        System.out.println(resB);
        System.out.println(resC);
        System.exit(0);
      }
    }

    System.out.println(nuniq);
    System.out.println(timeA <= timeB ? timeA <= timeC ? "A" : "C" : timeB <= timeC ? "B" : "C");
    System.out.println((Math.min(timeA, timeB) - timeC) / (double) timeC);
  }

  public static boolean uniqueA(final String[] names) {
    for (int i = 0; i < names.length; i++) {
      for (int j = i + 1; j < names.length; j++) {
        if (names[i].equals(names[j])) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean uniqueB(final String[] names) {
    final java.util.HashSet<String> set = new java.util.HashSet<>();
    for (int i = 0; i < names.length; i++) {
      if (!set.add(names[i])) {
        return false;
      }
    }
    return true;
  }

  public static boolean uniqueC(final String[] names) {
    final int length = names.length;
    if (length < 32) {
      for (int i = length - 1; i >= 1; i -= 1) {
        final String name = names[i];
        for (int j = i - 1; j >= 0; j -= 1) if (name.equals(names[j])) return false;
      }
      return true;
    } else {
      final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
      for (int i = 0; i < length; i++) if (!set.add(names[i])) return false;
      return true;
    }
  }
}
