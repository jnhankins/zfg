package zfg;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public final class golf {
  private golf() {}

  private static final FileOutputStream stdout = new FileOutputStream(FileDescriptor.out);
  private static final byte[] buf = new byte[1024];
  private static int pos = 0;

  private static final long[] bitset = new long[67108864];
  private static long nset = 0;
  private static final void setBit(final int i32) { // [0 1 2 3 ... 63]
    final int hi = i32 >>> 6;
    final int lo = i32 & 0x3F;
    final long wrd = bitset[hi];
    final long bit = 1L << lo;
    if ((wrd & bit) == 0) {
      bitset[hi] = wrd | bit;
      nset++;
    }
  }
  private static final void printSetBitRanges() {
    boolean inRange = false;
    int beg = 0;
    for (int hi = 0; hi < bitset.length; hi++) {
      final long wrd = bitset[hi];
      for (int lo = 0; lo < 64; lo++) {
        final long bit = 1L << lo;
        if ((wrd & bit) != 0) {
          if (!inRange) {
            beg = (hi << 6) + lo;;
            inRange = true;
          }
        } else {
          if (inRange) {
            final int end = ((hi << 6) + lo - 1);
            ensureCapacity(100);
            append(beg);
            if (beg != end) {
              append("-");
              append(end);
              append(":");
              append(Long.toString((end & 0xFFFFFFFFL) - (beg & 0xFFFFFFFFL) + 1L));
            }
            append("\n");
            inRange = false;
          }
        }
      }
    }
    if (inRange) {
      final int end = -1;
      ensureCapacity(100);
      append(beg);
      if (beg != end) {
        append("-");
        append(end);
        append(":");
        append(Long.toString((end & 0xFFFFFFFFL) - (beg & 0xFFFFFFFFL) + 1L));
      }
      append("\n");
    }
  }

  private static final void flush() {
    try { stdout.write(buf, 0, pos); }
    catch (IOException e) { throw new RuntimeException(e); }
    pos = 0;
  }
  private static final void ensureCapacity(final int len) {
    if (buf.length - pos < len) flush();
  }

  @SuppressWarnings("deprecation")
  private static final void append(final String s) {
    final int len = s.length();
    s.getBytes(0, len, buf, pos);
    pos += len;
  }

  private static final void append(int x) {
    final int len = 8;//((32 - Integer.numberOfLeadingZeros(x)) + 3)/4;
    pos += len;
    for (int i = 0, j = pos -1; i < len; i++, j--) {
      int c = x & 0xF;
      x >>>= 4;
      buf[j] = (byte)(c < 10 ? '0' + c : 'A' + c - 10);
    }
  }
  private static final void appendB(int x) {
    final int y = x >>> 4;
    buf[pos++] = (byte)(y < 10 ? '0' + y : 'A' + y - 10);
    x &= 0xF;
    buf[pos++] = (byte)(x < 10 ? '0' + x : 'A' + x - 10);
  }

  public static void main(String[] args) {
    append("helloo world\n");

    // for (int i = -1; i <= 5; i++) {
    //   // iconst
    //   setBit(i);
    //   // // iconst ineg
    //   // setBit(-i);

    //   for (int j = -1; j <= 5; j++) {
    //     //////// NO GAIN
    //     // // iconst iconst ishl
    //     // setBit(i << j);
    //     // // iconst iconst ishr
    //     // setBit(i >> j);
    //     // // iconst iconst ishru
    //     // setBit(i >>> j);
    //     // // iconst iconst iand
    //     // setBit(i & j);
    //     // // iconst iconst ior
    //     // setBit(i | j);
    //     // // iconst iconst ixor
    //     // setBit(i ^ j);
    //     // // iconst iconst iadd
    //     // setBit(i + j);
    //     // // iconst iconst isub
    //     // setBit(i - j);

    //     //////// NO GAIN
    //     // // iconst neg iconst ishl
    //     // setBit((-i) << j);
    //     // // iconst neg iconst ishr
    //     // setBit((-i) >> j);
    //     // // iconst neg iconst ishru
    //     // setBit((-i) >>> j);
    //     // // iconst neg iconst iand
    //     // setBit((-i) & j);
    //     // // iconst neg iconst ior
    //     // setBit((-i) | j);
    //     // // iconst neg iconst ixor
    //     // setBit((-i) ^ j);
    //     // // iconst neg iconst iadd
    //     // setBit((-i) + j);
    //     // // iconst neg iconst isub
    //     // setBit((-i) - j);

    //     //////// NO GAIN
    //     // // iconst iconst ineg ishl
    //     // setBit(i << (-j));
    //     // // iconst iconst ineg ishr
    //     // setBit(i >> (-j));
    //     // // iconst iconst ineg ishru
    //     // setBit(i >>> (-j));
    //     // // iconst iconst ineg iand
    //     // setBit(i & (-j));
    //     // // iconst iconst ineg ior
    //     // setBit(i | (-j));
    //     // // iconst iconst ineg ixor
    //     // setBit(i ^ (-j));
    //     // // iconst iconst ineg iadd
    //     // setBit(i + (-j));
    //     // // iconst iconst ineg isub
    //     // setBit(i - (-j));

    //     //////// NO GAIN
    //     // // iconst iconst ishl ineg
    //     // setBit(-(i << j));
    //     // // iconst iconst ishr ineg
    //     // setBit(-(i >> j));
    //     // // iconst iconst ishru ineg
    //     // setBit(-(i >>> j));
    //     // // iconst iconst iand ineg
    //     // setBit(-(i & j));
    //     // // iconst iconst ior ineg
    //     // setBit(-(i | j));
    //     // // iconst iconst ixor ineg
    //     // setBit(-(i ^ j));
    //     // // iconst iconst iadd ineg
    //     // setBit(-(i + j));
    //     // // iconst iconst isub ineg
    //     // setBit(-(i - j));
    //   }

    //   for (int j = 0xFFFFFF80; j <= 0x0000007F; j++) {
    //     // iconst bipush n ishl
    //     setBit(i << j);
    //     // // iconst bipush n ishr
    //     // setBit(i >> j);
    //     // // iconst bipush n ishru
    //     // setBit(i >>> j);
    //     // // iconst bipush n iand
    //     // setBit(i & j);
    //     // // iconst bipush n ior
    //     // setBit(i | j);
    //     // // iconst bipush n ixor
    //     // setBit(i ^ j);
    //     // // iconst bipush n iadd
    //     // setBit(i + j);
    //     // // iconst bipush n isub
    //     // setBit(i - j);
    //   }

    // }
    // for (int i = 0xFFFFFF80; i <= 0x0000007F; i++) {
    //   // // bipush b
    //   // setBit(i);
    //   // // bipush b neg
    //   // setBit(-i);

    //   for (int j = -1; j <= 5; j++) {
    //     // // bipush b iconst ishl
    //     // setBit(i << j);
    //     // // bipush b iconst ishr
    //     // setBit(i >> j);
    //     // // bipush b iconst ishru
    //     // setBit(i >>> j);
    //     // // bipush b iconst ior
    //     // setBit(j | i);
    //     // // bipush b iconst iand
    //     // setBit(j & i);
    //     // // bipush b iconst ixor
    //     // setBit(j ^ i);
    //     // // bipush b iconst imul
    //     // setBit(j * i);
    //   }


    //   for (int j = 0xFFFFFF80; j <= 0x0000007F; j++) {
    //     // // bipush b bipush b ishl
    //     // setBit(i << j);
    //   }
    // }

    // for (int i = 0xFFFF8000; i <= 0x00007FFF; i++) {
    //   // sipush b b
    //   setBit(i);
    //   // // sipush b b neg
    //   // setBit(-i);
    //   // // bipush b b i2s
    //   // setBit(i & 0xFFFF);
    // }


    // iconst
    for (int i = -1; i <= 5; i++)
      setBit(i);
    // bipush
    for (int i = 0xFFFFFF80; i <= 0x0000007F; i++)
      setBit(i);
    // sipush
    for (int i = 0xFFFF8000; i <= 0x00007FFF; i++)
      setBit(i);
    // ishl
    for (int i = 0; i <= 63; i++)
      setBit(1 << i);
    // iushr
    for (int i = 0; i <= 63; i++)
      setBit(-1 >>> i);
    // iushr
    for (int i = 0; i <= 63; i++)
      setBit(-(-1 >>> i));

    printSetBitRanges();

    append(Long.toString(nset));
    append("\n");

    // for (int i32 = 0; i32 <= Short.MAX_VALUE; i32++) {
    //   ensureCapacity(64);
    //   append(i32);
    //   append(" = ");
    //   if      (iconst(i32)) {}
    //   else if (bipush(i32)) {}
    //   else if (sipush(i32)) {}
    //   else {
    //     throw new AssertionError();
    //   }
    //   append("\n");
    // }
    flush();
  }

  // private static final boolean iconst(final int i32) {
  //   if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) {
  //     if (i32 == -1) {
  //       append("iconst_m1");
  //     } else {
  //       append("iconst_");
  //       append('0' + i32);
  //     }
  //     return true;
  //   }
  //   return false;
  // }
  // private static final boolean bipush(final int i32) {
  //   if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) {
  //     append("bipush ");
  //     appendB(i32);
  //     return true;
  //   }
  //   return false;
  // }
  // private static final boolean sipush(final int i32) {
  //   if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) {
  //     append("sipush ");
  //     appendB(i32 >>> 8);
  //     appendB(i32 & 0xFF);
  //     return true;
  //   }
  //   return false;
  // }
  // private static final boolean shft(final int i32) {

  //   // iconst_n iconst_n ishl       //  c1 << b = i32
  //   // iconst_n ineg iconst_n ishl  // -c1 << b = i32
  //   // iconst_n iconst_n ineg ishl  //
  //   // iconst_n iconst_n ishl ineg

  //   // bipush   n        iconst_n        ishl neg

  //   // iconst_n iconst_n islshl   ineg
  //   // bipush   n        iconst_n ishl

  //   int L = Integer.numberOfLeadingZeros(i32);
  //   int R = Integer.numberOfTrailingZeros(i32);
  //   if (L < R)
  //   sipush hi lo bipush 16 ishl sipush hi lo ior
  // }
}
