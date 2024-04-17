package zfg.core.operation;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import zfg.core.primative.Bit;
import zfg.core.primative.F32;
import zfg.core.primative.F64;
import zfg.core.primative.I08;
import zfg.core.primative.I16;
import zfg.core.primative.I32;
import zfg.core.primative.I64;
import zfg.core.primative.U08;
import zfg.core.primative.U16;
import zfg.core.primative.U32;
import zfg.core.primative.U64;

public class Helper {
  public static Arguments bit(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(Bit::of).toArray(Bit[]::new)); }
  public static Arguments u08(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(U08::of).toArray(U08[]::new)); }
  public static Arguments u16(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(U16::of).toArray(U16[]::new)); }
  public static Arguments u32(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(U32::of).toArray(U32[]::new)); }
  public static Arguments u64(final long[] a) { return Arguments.of((Object[]) LongStream.of(a).mapToObj(U64::of).toArray(U64[]::new)); }
  public static Arguments i08(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(I08::of).toArray(I08[]::new)); }
  public static Arguments i16(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(I16::of).toArray(I16[]::new)); }
  public static Arguments i32(final int[] a) { return Arguments.of((Object[]) IntStream.of(a).mapToObj(I32::of).toArray(I32[]::new)); }
  public static Arguments i64(final long[] a) { return Arguments.of((Object[]) LongStream.of(a).mapToObj(I64::of).toArray(I64[]::new)); }
  public static Arguments f32(final float[] a) { return Arguments.of((Object[]) IntStream.range(0, a.length).mapToObj(i -> F32.of(a[i])).toArray(F32[]::new)); }
  public static Arguments f64(final double[] a) { return Arguments.of((Object[]) DoubleStream.of(a).mapToObj(F64::of).toArray(F64[]::new)); }
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

  public static Arguments u08shift(final int[] a) { return Arguments.of(U08.of(a[0]), I32.of(a[1]), U08.of(a[2])); }
  public static Arguments u16shift(final int[] a) { return Arguments.of(U16.of(a[0]), I32.of(a[1]), U16.of(a[2])); }
  public static Arguments u32shift(final int[] a) { return Arguments.of(U32.of(a[0]), I32.of(a[1]), U32.of(a[2])); }
  public static Arguments u64shift(final long[] a) { return Arguments.of(U64.of(a[0]), I32.of((int) a[1]), U64.of(a[2])); }
  public static Arguments i08shift(final int[] a) { return Arguments.of(I08.of(a[0]), I32.of(a[1]), I08.of(a[2])); }
  public static Arguments i16shift(final int[] a) { return Arguments.of(I16.of(a[0]), I32.of(a[1]), I16.of(a[2])); }
  public static Arguments i32shift(final int[] a) { return Arguments.of(I32.of(a[0]), I32.of(a[1]), I32.of(a[2])); }
  public static Arguments i64shift(final long[] a) { return Arguments.of(I64.of(a[0]), I32.of((int) a[1]), I64.of(a[2])); }
  public static Stream<Arguments> u08shift(final int[][] a) { return Stream.of(a).map(Helper::u08shift); }
  public static Stream<Arguments> u16shift(final int[][] a) { return Stream.of(a).map(Helper::u16shift); }
  public static Stream<Arguments> u32shift(final int[][] a) { return Stream.of(a).map(Helper::u32shift); }
  public static Stream<Arguments> u64shift(final long[][] a) { return Stream.of(a).map(Helper::u64shift); }
  public static Stream<Arguments> i08shift(final int[][] a) { return Stream.of(a).map(Helper::i08shift); }
  public static Stream<Arguments> i16shift(final int[][] a) { return Stream.of(a).map(Helper::i16shift); }
  public static Stream<Arguments> i32shift(final int[][] a) { return Stream.of(a).map(Helper::i32shift); }
  public static Stream<Arguments> i64shift(final long[][] a) { return Stream.of(a).map(Helper::i64shift); }
}
