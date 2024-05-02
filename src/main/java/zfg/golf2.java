package zfg;

public class golf2 {
  static int H = 0;

  public static void main(String[] args) {
    long t1 = 0;
    long t2 = 0;
    long t3 = 0;
    System.out.println();
    t3 = test3(10000);
    t1 = test1(10000);
    t2 = test2(10000);
    System.out.println(t1 +"\n"+ t2 +"\n"+ t3);
    t3 = test3(20000);
    t1 = test1(20000);
    t2 = test2(20000);
    System.out.println(t1 +"\n"+ t2 +"\n"+ t3);
    t3 = test3(30000);
    t1 = test1(30000);
    t2 = test2(30000);
    System.out.println(t1 +"\n"+ t2 +"\n"+ t3);
    t3 = test3(40000);
    t1 = test1(40000);
    t2 = test2(40000);
    System.out.println(t1 +"\n"+ t2 +"\n"+ t3);
    System.out.println(H);
  }

  public static long test1(int Q) {
    int h1 = 0;
    long T1 = 0;
    long t0 = System.nanoTime();
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        h1 ^= mod1(a, b);
      }
    }
    long t1 = System.nanoTime();
    T1 += t1 - t0;
    H ^= h1;
    return T1;
  }

  public static long test2(int Q) {
    int h1 = 0;
    long T1 = 0;
    long t0 = System.nanoTime();
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        h1 ^= mod2(a, b);
      }
    }
    long t1 = System.nanoTime();
    T1 += t1 - t0;
    H ^= h1;
    return T1;
  }

  public static long test3(int Q) {
    int h1 = 0;
    long T1 = 0;
    long t0 = System.nanoTime();
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        h1 ^= mod3(a, b);
      }
    }
    long t1 = System.nanoTime();
    T1 += t1 - t0;
    H ^= h1;
    return T1;
  }

  public static void test132(int Q) {
    int h1 = 0;
    int h2 = 0;
    int h3 = 0;
    long T1 = 0;
    long T2 = 0;
    long T3 = 0;
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        long t0 = System.nanoTime();
        h1 ^= mod1(a, b);
        long t1 = System.nanoTime();
        h3 ^= mod3(a, b);
        long t2 = System.nanoTime();
        h2 ^= mod2(a, b);
        long t3 = System.nanoTime();
        T1 += t1 - t0;
        T2 += t2 - t1;
        T3 += t3 - t2;
      }
    }
    System.out.println(h1);
    System.out.println(h2);
    System.out.println(h3);
    System.out.println(T1);
    System.out.println(T3);
    System.out.println(T2);
  }

  public static void test213(int Q) {
    int h1 = 0;
    int h2 = 0;
    int h3 = 0;
    long T1 = 0;
    long T2 = 0;
    long T3 = 0;
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        long t0 = System.nanoTime();
        h2 ^= mod2(a, b);
        long t1 = System.nanoTime();
        h1 ^= mod1(a, b);
        long t2 = System.nanoTime();
        h3 ^= mod3(a, b);
        long t3 = System.nanoTime();
        T1 += t1 - t0;
        T2 += t2 - t1;
        T3 += t3 - t2;
      }
    }
    System.out.println(h1);
    System.out.println(h2);
    System.out.println(h3);
    System.out.println(T2);
    System.out.println(T1);
    System.out.println(T3);
  }

  public static void test231(int Q) {
    int h1 = 0;
    int h2 = 0;
    int h3 = 0;
    long T1 = 0;
    long T2 = 0;
    long T3 = 0;
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        long t0 = System.nanoTime();
        h2 ^= mod2(a, b);
        long t1 = System.nanoTime();
        h3 ^= mod3(a, b);
        long t2 = System.nanoTime();
        h1 ^= mod1(a, b);
        long t3 = System.nanoTime();
        T1 += t1 - t0;
        T2 += t2 - t1;
        T3 += t3 - t2;
      }
    }
    System.out.println(h1);
    System.out.println(h2);
    System.out.println(h3);
    System.out.println(T2);
    System.out.println(T3);
    System.out.println(T1);
  }

  public static void test312(int Q) {
    int h1 = 0;
    int h2 = 0;
    int h3 = 0;
    long T1 = 0;
    long T2 = 0;
    long T3 = 0;
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        long t0 = System.nanoTime();
        h3 ^= mod3(a, b);
        long t1 = System.nanoTime();
        h1 ^= mod1(a, b);
        long t2 = System.nanoTime();
        h2 ^= mod2(a, b);
        long t3 = System.nanoTime();
        T1 += t1 - t0;
        T2 += t2 - t1;
        T3 += t3 - t2;
      }
    }
    System.out.println(h1);
    System.out.println(h2);
    System.out.println(h3);
    System.out.println(T3);
    System.out.println(T1);
    System.out.println(T2);
  }

  public static void test321(int Q) {
    int h1 = 0;
    int h2 = 0;
    int h3 = 0;
    long T1 = 0;
    long T2 = 0;
    long T3 = 0;
    for (int a = -Q; a <= Q; a++) {
      if (a == 0) continue;
      for (int b = -Q; b <= Q; b++) {
        if (b == 0) continue;
        long t0 = System.nanoTime();
        h3 ^= mod3(a, b);
        long t1 = System.nanoTime();
        h2 ^= mod2(a, b);
        long t2 = System.nanoTime();
        h1 ^= mod1(a, b);
        long t3 = System.nanoTime();
        T1 += t1 - t0;
        T2 += t2 - t1;
        T3 += t3 - t2;
      }
    }
    System.out.println(h1);
    System.out.println(h2);
    System.out.println(h3);
    System.out.println(T3);
    System.out.println(T2);
    System.out.println(T1);
  }

  public static int mod1(int a, int b) {
    return (a % b + b) % b;
  }

  // public static int mod2(int a, int b) {
  //   int r = a % b;
  //   if ((a ^ b) < 0 && r != 0) r += b;
  //   return r;
  // }

  public static int mod2(int a, int b) {
    int r = a % b;
    if (((a ^ b) & (r | -r)) < 0) r += b;
    return r;
  }

  public static int mod3(int a, int b) {
    int r = a % b;
    r += b & (((r|-r) & (r ^ b)) >> 31);
    return r;
  }
}
