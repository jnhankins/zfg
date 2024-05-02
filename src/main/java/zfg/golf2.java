package zfg;

import java.util.HashMap;

public class golf2 {

  public static void main(String[] args) {
    HashMap<Integer, Integer> table = new HashMap<>();
    table.get(null);
    Math.mod

    // a mod b = a - b * floor(a / b)

    // :: a mod b = (a % b + b) % b
    // 0: [a, b] dup_x1
    // 1: [b, a, b] dup_x1
    // 2: [b, b, a, b] irem
    // 3: [b, b, (a % b)] iadd
    // 4: [b, (a % b + b)] swap
    // 5: [(a % b + b), b] irem
    // 6: [(a % b + b) % b]

    // a mod b = (a % b) < 0 ? (a % b) + b : (a % b)

    // :: a mod b = (a % b) + ((a % b) < 0 ? b : 0)
    // [a, b] dup_x1
    // [b, a, b] irem
    // [b, (a % b)] dup_x1
    // [(a % b), b, (a % b)] bipush 31
    // [(a % b), b, (a % b), 31] ishr
    // [(a % b), b, ((a % b) >> 31)] iand
    // [(a % b), (((a % b) >> 31) & b)] iadd

    // alternative
    // [a, b] dup
    // [a, b, b] bipush 31
    // [a, b, b, 31] ishr
    // [a, b, (b >> 31)] iand



    // for (int i = -15; i < 15; i++) {
    //   int r = i % 7;
    //   int m = ((i % 7) + 7) % 7;
    //   Math.floorMod(a, b);
    //   System.out.printf("%08X ; %08X ; %d ; %d ; %d\n", r, m, i, r, m);
    // }
  }

  public static int mod1(int a, int b) {
    return (a % b + b) % b;
  }

  public static int mod2(int a, int b) {
    int m = a % b;
    if (m < 0) m += b;
    return m;
  }
}
