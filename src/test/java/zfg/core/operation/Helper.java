package zfg.core.operation;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.function.BiFunction;

import org.junit.jupiter.params.provider.Arguments;

import zfg.old.lang3.val.Bit;
import zfg.old.lang3.val.F32;
import zfg.old.lang3.val.F64;
import zfg.old.lang3.val.I08;
import zfg.old.lang3.val.I16;
import zfg.old.lang3.val.I32;
import zfg.old.lang3.val.I64;
import zfg.old.lang3.val.U08;
import zfg.old.lang3.val.U16;
import zfg.old.lang3.val.U32;
import zfg.old.lang3.val.U64;

public final class Helper {
  private Helper() {}

  public static Arguments bit(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(BitType::of).toArray(BitType[]::new)); }
  public static Arguments u08(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(U08Type::of).toArray(U08Type[]::new)); }
  public static Arguments u16(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(U16Type::of).toArray(U16Type[]::new)); }
  public static Arguments u32(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(U32Type::of).toArray(U32Type[]::new)); }
  public static Arguments u64(final long[] a) { return Arguments.of((Object[]) LongStream.of(a).mapToObj(U64Type::of).toArray(U64Type[]::new)); }
  public static Arguments i08(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(I08Type::of).toArray(I08Type[]::new)); }
  public static Arguments i16(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(I16Type::of).toArray(I16Type[]::new)); }
  public static Arguments i32(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(I32Type::of).toArray(I32Type[]::new)); }
  public static Arguments i64(final long[] a) { return Arguments.of((Object[]) LongStream.of(a).mapToObj(I64Type::of).toArray(I64Type[]::new)); }
  public static Arguments f32(final float[] a) { return Arguments.of((Object[]) IntStream.range(0, a.length).mapToObj(i -> F32Type.of(a[i])).toArray(F32Type[]::new)); }
  public static Arguments f64(final double[] a) { return Arguments.of((Object[]) DoubleStream.of(a).mapToObj(F64Type::of).toArray(F64Type[]::new)); }
  public static Stream<Arguments> bit(final int[][] a) { return Stream.of(a).map(Helper::bit); }
  public static Stream<Arguments> u08(final int[][] a) { return Stream.of(a).map(Helper::u08); }
  public static Stream<Arguments> u16(final int[][] a) { return Stream.of(a).map(Helper::u16); }
  public static Stream<Arguments> u32(final int[][] a) { return Stream.of(a).map(Helper::u32); }
  public static Stream<Arguments> u64(final long[][] a) { return Stream.of(a).map(Helper::u64); }
  public static Stream<Arguments> i08(final int[][] a) { return Stream.of(a).map(Helper::i08); }
  public static Stream<Arguments> i16(final int[][] a) { return Stream.of(a).map(Helper::i16); }
  public static Stream<Arguments> i32(final int[][] a) { return Stream.of(a).map(Helper::i32); }
  public static Stream<Arguments> i64(final long[][] a) { return Stream.of(a).map(Helper::i64); }
  public static Stream<Arguments> f32(final float[][] a) { return Stream.of(a).map(Helper::f32); }
  public static Stream<Arguments> f64(final double[][] a) { return Stream.of(a).map(Helper::f64); }

  public static Arguments u08shift(final int[] a) { return Arguments.of(U08Type.of(a[0]), I32Type.of(a[1]), U08Type.of(a[2])); }
  public static Arguments u16shift(final int[] a) { return Arguments.of(U16Type.of(a[0]), I32Type.of(a[1]), U16Type.of(a[2])); }
  public static Arguments u32shift(final int[] a) { return Arguments.of(U32Type.of(a[0]), I32Type.of(a[1]), U32Type.of(a[2])); }
  public static Arguments u64shift(final long[] a) { return Arguments.of(U64Type.of(a[0]), I32Type.of((int) a[1]), U64Type.of(a[2])); }
  public static Arguments i08shift(final int[] a) { return Arguments.of(I08Type.of(a[0]), I32Type.of(a[1]), I08Type.of(a[2])); }
  public static Arguments i16shift(final int[] a) { return Arguments.of(I16Type.of(a[0]), I32Type.of(a[1]), I16Type.of(a[2])); }
  public static Arguments i32shift(final int[] a) { return Arguments.of(I32Type.of(a[0]), I32Type.of(a[1]), I32Type.of(a[2])); }
  public static Arguments i64shift(final long[] a) { return Arguments.of(I64Type.of(a[0]), I32Type.of((int) a[1]), I64Type.of(a[2])); }
  public static Stream<Arguments> u08shift(final int[][] a) { return Stream.of(a).map(Helper::u08shift); }
  public static Stream<Arguments> u16shift(final int[][] a) { return Stream.of(a).map(Helper::u16shift); }
  public static Stream<Arguments> u32shift(final int[][] a) { return Stream.of(a).map(Helper::u32shift); }
  public static Stream<Arguments> u64shift(final long[][] a) { return Stream.of(a).map(Helper::u64shift); }
  public static Stream<Arguments> i08shift(final int[][] a) { return Stream.of(a).map(Helper::i08shift); }
  public static Stream<Arguments> i16shift(final int[][] a) { return Stream.of(a).map(Helper::i16shift); }
  public static Stream<Arguments> i32shift(final int[][] a) { return Stream.of(a).map(Helper::i32shift); }
  public static Stream<Arguments> i64shift(final long[][] a) { return Stream.of(a).map(Helper::i64shift); }

  public static Arguments bitrel(final int[] a) { return Arguments.of(BitType.of(a[0]), BitType.of(a[1]), BitType.of(a[2])); }
  public static Arguments u08rel(final int[] a) { return Arguments.of(U08Type.of(a[0]), U08Type.of(a[1]), BitType.of(a[2])); }
  public static Arguments u16rel(final int[] a) { return Arguments.of(U16Type.of(a[0]), U16Type.of(a[1]), BitType.of(a[2])); }
  public static Arguments u32rel(final int[] a) { return Arguments.of(U32Type.of(a[0]), U32Type.of(a[1]), BitType.of(a[2])); }
  public static Arguments u64rel(final long[] a) { return Arguments.of(U64Type.of(a[0]), U64Type.of(a[1]), BitType.of((int) a[2])); }
  public static Arguments i08rel(final int[] a) { return Arguments.of(I08Type.of(a[0]), I08Type.of(a[1]), BitType.of(a[2])); }
  public static Arguments i16rel(final int[] a) { return Arguments.of(I16Type.of(a[0]), I16Type.of(a[1]), BitType.of(a[2])); }
  public static Arguments i32rel(final int[] a) { return Arguments.of(I32Type.of(a[0]), I32Type.of(a[1]), BitType.of(a[2])); }
  public static Arguments i64rel(final long[] a) { return Arguments.of(I64Type.of(a[0]), I64Type.of(a[1]), BitType.of((int) a[2])); }
  public static Arguments f32rel(final float[] a) { return Arguments.of(F32Type.of(a[0]), F32Type.of(a[1]), BitType.of((int) a[2])); }
  public static Arguments f64rel(final double[] a) { return Arguments.of(F64Type.of(a[0]), F64Type.of(a[1]), BitType.of((int) a[2])); }
  public static Stream<Arguments> bitrel(final int[][] a) { return Stream.of(a).map(Helper::bitrel); }
  public static Stream<Arguments> u08rel(final int[][] a) { return Stream.of(a).map(Helper::u08rel); }
  public static Stream<Arguments> u16rel(final int[][] a) { return Stream.of(a).map(Helper::u16rel); }
  public static Stream<Arguments> u32rel(final int[][] a) { return Stream.of(a).map(Helper::u32rel); }
  public static Stream<Arguments> u64rel(final long[][] a) { return Stream.of(a).map(Helper::u64rel); }
  public static Stream<Arguments> i08rel(final int[][] a) { return Stream.of(a).map(Helper::i08rel); }
  public static Stream<Arguments> i16rel(final int[][] a) { return Stream.of(a).map(Helper::i16rel); }
  public static Stream<Arguments> i32rel(final int[][] a) { return Stream.of(a).map(Helper::i32rel); }
  public static Stream<Arguments> i64rel(final long[][] a) { return Stream.of(a).map(Helper::i64rel); }
  public static Stream<Arguments> f32rel(final float[][] a) { return Stream.of(a).map(Helper::f32rel); }
  public static Stream<Arguments> f64rel(final double[][] a) { return Stream.of(a).map(Helper::f64rel); }

  public static enum BitCase {
    x00000000(BitType.of(0x00000000)), // false
    x00000001(BitType.of(0x00000001)); // true
    public final BitType value;
    private BitCase(final BitType value) { this.value = value; }
  }

  public static enum U08Case {
    x00000000(U08Type.of(0x00000000)), //   0 - min
    x00000001(U08Type.of(0x00000001)), //   1 - one
    x0000000F(U08Type.of(0x0000000F)), //  15 - lo bits set
    x000000F0(U08Type.of(0x000000F0)), // 240 - hi bits set
    x000000FF(U08Type.of(0x000000FF)); // 255 - max
    public final U08Type value;
    private U08Case(final U08Type value) { this.value = value; }
  }

  public static enum U16Case {
    x00000000(U16Type.of(0x00000000)), //     0 - min
    x00000001(U16Type.of(0x00000001)), //     1 - one
    x000000FF(U16Type.of(0x000000FF)), //   255 - lo bits set
    x0000FF00(U16Type.of(0x0000FF00)), // 65280 - hi bits set
    x0000FFFF(U16Type.of(0x0000FFFF)); // 65535 - max
    public final U16Type value;
    private U16Case(final U16Type value) { this.value = value; }
  }

  public static enum U32Case {
    x00000000(U32Type.of(0x00000000)), //          0 - min
    x00000001(U32Type.of(0x00000001)), //          1 - one
    x0000FFFF(U32Type.of(0x0000FFFF)), //      65535 - lo bits set
    xFFFF0000(U32Type.of(0xFFFF0000)), // 4294901760 - hi bits set
    xFFFFFFFF(U32Type.of(0xFFFFFFFF)); // 4294967295 - max
    public final U32Type value;
    private U32Case(final U32Type value) { this.value = value; }
  }

  public static enum U64Case {
    x0000000000000000(U64Type.of(0x0000000000000000L)), //                    0 - min
    x0000000000000001(U64Type.of(0x0000000000000001L)), //                    1 - one
    x00000000FFFFFFFF(U64Type.of(0x00000000FFFFFFFFL)), //           4294967295 - lo bits set
    xFFFFFFFF00000000(U64Type.of(0xFFFFFFFF00000000L)), // 18446744069414584320 - hi bits set
    xFFFFFFFFFFFFFFFF(U64Type.of(0xFFFFFFFFFFFFFFFFL)); // 18446744073709551615 - max
    public final U64Type value;
    private U64Case(final U64Type value) { this.value = value; }
  }

  public static enum I08Case {
    xFFFFFF80(I08Type.of(0xFFFFFF80)), // -128 : min
    xFFFFFF81(I08Type.of(0xFFFFFF81)), // -127 : min - 1
    xFFFFFFF0(I08Type.of(0xFFFFFFF0)), //  -16 : hi bits set
    xFFFFFFFF(I08Type.of(0xFFFFFFFF)), //   -1 : minus one
    x00000000(I08Type.of(0x00000000)), //    0 : zero
    x00000001(I08Type.of(0x00000001)), //    1 : one
    x0000000F(I08Type.of(0x0000000F)), //   15 : lo bits set
    x0000007F(I08Type.of(0x0000007F)); //  127 : max
    public final I08Type value;
    private I08Case(final I08Type value) { this.value = value; }
  }

  public static enum I16Case {
    xFFFF8000(I16Type.of(0xFFFF8000)), // -32768 : min
    xFFFF8001(I16Type.of(0xFFFF8001)), // -32767 : min - 1
    xFFFFFF00(I16Type.of(0xFFFFFF00)), //   -256 : hi bits set
    xFFFFFFFF(I16Type.of(0xFFFFFFFF)), //     -1 : minus one
    x00000000(I16Type.of(0x00000000)), //      0 : zero
    x00000001(I16Type.of(0x00000001)), //      1 : one
    x000000FF(I16Type.of(0x000000FF)), //    255 : lo bits set
    x00007FFF(I16Type.of(0x00007FFF)); //  32767 : max
    public final I16Type value;
    private I16Case(final I16Type value) { this.value = value; }
  }

  public static enum I32Case {
    x80000000(I32Type.of(0x80000000)), // -2147483648 : min
    x80000001(I32Type.of(0x80000001)), // -2147483647 : min - 1
    xFFFF0000(I32Type.of(0xFFFF0000)), //      -65536 : hi bits set
    xFFFFFFFF(I32Type.of(0xFFFFFFFF)), //          -1 : minus one
    x00000000(I32Type.of(0x00000000)), //           0 : zero
    x00000001(I32Type.of(0x00000001)), //           1 : one
    x0000FFFF(I32Type.of(0x0000FFFF)), //       65535 : lo bits set
    x7FFFFFFF(I32Type.of(0x7FFFFFFF)); //  2147483647 : max
    public final I32Type value;
    private I32Case(final I32Type value) { this.value = value; }
  }

  public static enum I64Case {
    x8000000000000000(I64Type.of(0x8000000000000000L)), // -9223372036854775808 : min
    x8000000000000001(I64Type.of(0x8000000000000001L)), // -9223372036854775807 : min - 1
    xFFFFFFFF00000000(I64Type.of(0xFFFFFFFF00000000L)), //          -4294967296 : hi bits set
    xFFFFFFFFFFFFFFFF(I64Type.of(0xFFFFFFFFFFFFFFFFL)), //                   -1 : minus one
    x0000000000000000(I64Type.of(0x0000000000000000L)), //                    0 : zero
    x0000000000000001(I64Type.of(0x0000000000000001L)), //                    1 : one
    x00000000FFFFFFFF(I64Type.of(0x00000000FFFFFFFFL)), //           4294967295 : lo bits set
    x7FFFFFFFFFFFFFFF(I64Type.of(0x7FFFFFFFFFFFFFFFL)); //  9223372036854775807 : max
    public final I64Type value;
    private I64Case(final I64Type value) { this.value = value; }
  }

  public static <A extends Enum<A>> Stream<Arguments> cases(
    final Class<A> aCases,
    final Function<A, Arguments> f) {
    return Stream.of(aCases.getEnumConstants()).map(f);
  }
  public static <A extends Enum<A>, B extends Enum<B>> Stream<Arguments> cases(
    final Class<A> aCases,
    final Class<B> bCases,
    final BiFunction<A, B, Arguments> f) {
    return Stream.of(aCases.getEnumConstants()).flatMap(a ->
      Stream.of(bCases.getEnumConstants()).map(b -> f.apply(a, b))
    );
  }
}
