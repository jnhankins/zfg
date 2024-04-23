package zfg.core.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import zfg.lang.ops.Shl;
import zfg.lang.val.I08;
import zfg.lang.val.I16;
import zfg.lang.val.I32;
import zfg.lang.val.I64;
import zfg.lang.val.U08;
import zfg.lang.val.U16;
import zfg.lang.val.U32;
import zfg.lang.val.U64;

public final class ShlTest {

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testU08")
  void testU08(final U08 a, final I32 b, final U08 e) { assertEquals(e, Shl.u08(a, b)); }
  static Stream<Arguments> testU08() {
    return Helper.u08shift(new int[][] {
      {0x0000000F,  0, 0x0000000F},
      {0x0000000F,  1, 0x0000001E},
      {0x0000000F,  4, 0x000000F0},
      {0x0000000F,  7, 0x00000080},
      {0x0000000F,  8, 0x0000000F},
      {0x0000000F,  9, 0x0000001E},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testU16")
  void testU16(final U16 a, final I32 b, final U16 e) { assertEquals(e, Shl.u16(a, b)); }
  static Stream<Arguments> testU16() {
    return Helper.u16shift(new int[][] {
      {0x000000FF,  0, 0x000000FF},
      {0x000000FF,  1, 0x000001FE},
      {0x000000FF,  8, 0x0000FF00},
      {0x000000FF, 15, 0x00008000},
      {0x000000FF, 16, 0x000000FF},
      {0x000000FF, 17, 0x000001FE},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testU32")
  void testU32(final U32 a, final I32 b, final U32 e) { assertEquals(e, Shl.u32(a, b)); }
  static Stream<Arguments> testU32() {
    return Helper.u32shift(new int[][] {
      {0x0000FFFF,  0, 0x0000FFFF},
      {0x0000FFFF,  1, 0x0001FFFE},
      {0x0000FFFF, 16, 0xFFFF0000},
      {0x0000FFFF, 31, 0x80000000},
      {0x0000FFFF, 32, 0x0000FFFF},
      {0x0000FFFF, 33, 0x0001FFFE},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testU64")
  void testU64(final U64 a, final I32 b, final U64 e) { assertEquals(e, Shl.u64(a, b)); }
  static Stream<Arguments> testU64() {
    return Helper.u64shift(new long[][] {
      {0x00000000_FFFFFFFFL,  0, 0x00000000_FFFFFFFFL},
      {0x00000000_FFFFFFFFL,  1, 0x00000001_FFFFFFFEL},
      {0x00000000_FFFFFFFFL, 32, 0xFFFFFFFF_00000000L},
      {0x00000000_FFFFFFFFL, 63, 0x80000000_00000000L},
      {0x00000000_FFFFFFFFL, 64, 0x00000000_FFFFFFFFL},
      {0x00000000_FFFFFFFFL, 65, 0x00000001_FFFFFFFEL},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testI08")
  void testI08(final I08 a, final I32 b, final I08 e) { assertEquals(e, Shl.i08(a, b)); }
  static Stream<Arguments> testI08() {
    return Helper.i08shift(new int[][] {
      {0x0000000F,  0, 0x0000000F},
      {0x0000000F,  1, 0x0000001E},
      {0x0000000F,  4, 0xFFFFFFF0},
      {0x0000000F,  7, 0xFFFFFF80},
      {0x0000000F,  8, 0x0000000F},
      {0x0000000F,  9, 0x0000001E},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testI16")
  void testI16(final I16 a, final I32 b, final I16 e) { assertEquals(e, Shl.i16(a, b)); }
  static Stream<Arguments> testI16() {
    return Helper.i16shift(new int[][] {
      {0x000000FF,  0, 0x000000FF},
      {0x000000FF,  1, 0x000001FE},
      {0x000000FF,  8, 0xFFFFFF00},
      {0x000000FF, 15, 0xFFFF8000},
      {0x000000FF, 16, 0x000000FF},
      {0x000000FF, 17, 0x000001FE},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testI32")
  void testI32(final I32 a, final I32 b, final I32 e) { assertEquals(e, Shl.i32(a, b)); }
  static Stream<Arguments> testI32() {
    return Helper.i32shift(new int[][] {
      {0x0000FFFF,  0, 0x0000FFFF},
      {0x0000FFFF,  1, 0x0001FFFE},
      {0x0000FFFF, 16, 0xFFFF0000},
      {0x0000FFFF, 31, 0x80000000},
      {0x0000FFFF, 32, 0x0000FFFF},
      {0x0000FFFF, 33, 0x0001FFFE},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shl {1} = {2}") @MethodSource("testI64")
  void testI64(final I64 a, final I32 b, final I64 e) { assertEquals(e, Shl.i64(a, b)); }
  static Stream<Arguments> testI64() {
    return Helper.i64shift(new long[][] {
      {0x00000000_FFFFFFFFL,  0, 0x00000000_FFFFFFFFL},
      {0x00000000_FFFFFFFFL,  1, 0x00000001_FFFFFFFEL},
      {0x00000000_FFFFFFFFL, 32, 0xFFFFFFFF_00000000L},
      {0x00000000_FFFFFFFFL, 63, 0x80000000_00000000L},
      {0x00000000_FFFFFFFFL, 64, 0x00000000_FFFFFFFFL},
      {0x00000000_FFFFFFFFL, 65, 0x00000001_FFFFFFFEL},
    });
  }
}
