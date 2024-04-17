package zfg.core.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import zfg.core.primative.I08;
import zfg.core.primative.I16;
import zfg.core.primative.I32;
import zfg.core.primative.I64;
import zfg.core.primative.U08;
import zfg.core.primative.U16;
import zfg.core.primative.U32;
import zfg.core.primative.U64;

public final class ShrTest {
  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testU08")
  void testU08(final U08 a, final I32 b, final U08 e) { assertEquals(e, Shr.u08(a, b)); }
  static Stream<Arguments> testU08() {
    return Helper.u08shift(new int[][] {
      {0x000000F0,  0, 0x000000F0},
      {0x000000F0,  1, 0x00000078},
      {0x000000F0,  4, 0x0000000F},
      {0x000000F0,  7, 0x00000001},
      {0x000000F0,  8, 0x000000F0},
      {0x000000F0,  9, 0x00000078},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testU16")
  void testU16(final U16 a, final I32 b, final U16 e) { assertEquals(e, Shr.u16(a, b)); }
  static Stream<Arguments> testU16() {
    return Helper.u16shift(new int[][] {
      {0x0000FF00,  0, 0x0000FF00},
      {0x0000FF00,  1, 0x00007F80},
      {0x0000FF00,  8, 0x000000FF},
      {0x0000FF00, 15, 0x00000001},
      {0x0000FF00, 16, 0x0000FF00},
      {0x0000FF00, 17, 0x00007F80},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testU32")
  void testU32(final U32 a, final I32 b, final U32 e) { assertEquals(e, Shr.u32(a, b)); }
  static Stream<Arguments> testU32() {
    return Helper.u32shift(new int[][] {
      {0xFFFF0000,  0, 0xFFFF0000},
      {0xFFFF0000,  1, 0x7FFF8000},
      {0xFFFF0000, 16, 0x0000FFFF},
      {0xFFFF0000, 31, 0x00000001},
      {0xFFFF0000, 32, 0xFFFF0000},
      {0xFFFF0000, 33, 0x7FFF8000},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testU64")
  void testU64(final U64 a, final I32 b, final U64 e) { assertEquals(e, Shr.u64(a, b)); }
  static Stream<Arguments> testU64() {
    return Helper.u64shift(new long[][] {
      {0xFFFFFFFF_00000000L,  0, 0xFFFFFFFF_00000000L},
      {0xFFFFFFFF_00000000L,  1, 0x7FFFFFFF_80000000L},
      {0xFFFFFFFF_00000000L, 32, 0x00000000_FFFFFFFFL},
      {0xFFFFFFFF_00000000L, 63, 0x00000000_00000001L},
      {0xFFFFFFFF_00000000L, 64, 0xFFFFFFFF_00000000L},
      {0xFFFFFFFF_00000000L, 65, 0x7FFFFFFF_80000000L},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testI08")
  void testI08(final I08 a, final I32 b, final I08 e) { assertEquals(e, Shr.i08(a, b)); }
  static Stream<Arguments> testI08() {
    return Helper.i08shift(new int[][] {
      {0xFFFFFFF0,  0, 0xFFFFFFF0},
      {0xFFFFFFF0,  1, 0xFFFFFFF8},
      {0xFFFFFFF0,  4, 0xFFFFFFFF},
      {0xFFFFFFF0,  7, 0xFFFFFFFF},
      {0xFFFFFFF0,  8, 0xFFFFFFF0},
      {0xFFFFFFF0,  9, 0xFFFFFFF8},
      {0x00000070,  0, 0x00000070},
      {0x00000070,  1, 0x00000038},
      {0x00000070,  4, 0x00000007},
      {0x00000070,  7, 0x00000000},
      {0x00000070,  8, 0x00000070},
      {0x00000070,  9, 0x00000038},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testI16")
  void testI16(final I16 a, final I32 b, final I16 e) { assertEquals(e, Shr.i16(a, b)); }
  static Stream<Arguments> testI16() {
    return Helper.i16shift(new int[][] {
      {0xFFFFFF00,  0, 0xFFFFFF00},
      {0xFFFFFF00,  1, 0xFFFFFF80},
      {0xFFFFFF00,  8, 0xFFFFFFFF},
      {0xFFFFFF00, 15, 0xFFFFFFFF},
      {0xFFFFFF00, 16, 0xFFFFFF00},
      {0xFFFFFF00, 17, 0xFFFFFF80},
      {0x00007F00,  0, 0x00007F00},
      {0x00007F00,  1, 0x00003F80},
      {0x00007F00,  8, 0x0000007F},
      {0x00007F00, 15, 0x00000000},
      {0x00007F00, 16, 0x00007F00},
      {0x00007F00, 17, 0x00003F80},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testI32")
  void testI32(final I32 a, final I32 b, final I32 e) { assertEquals(e, Shr.i32(a, b)); }
  static Stream<Arguments> testI32() {
    return Helper.i32shift(new int[][] {
      {0xFFFF0000,  0, 0xFFFF0000},
      {0xFFFF0000,  1, 0xFFFF8000},
      {0xFFFF0000, 16, 0xFFFFFFFF},
      {0xFFFF0000, 31, 0xFFFFFFFF},
      {0xFFFF0000, 32, 0xFFFF0000},
      {0xFFFF0000, 33, 0xFFFF8000},
      {0x7FFF0000,  0, 0x7FFF0000},
      {0x7FFF0000,  1, 0x3FFF8000},
      {0x7FFF0000, 16, 0x00007FFF},
      {0x7FFF0000, 31, 0x00000000},
      {0x7FFF0000, 32, 0x7FFF0000},
      {0x7FFF0000, 33, 0x3FFF8000},
    });
  }

  @ParameterizedTest(name = "{index}: {0} shr {1} = {2}") @MethodSource("testI64")
  void testI64(final I64 a, final I32 b, final I64 e) { assertEquals(e, Shr.i64(a, b)); }
  static Stream<Arguments> testI64() {
    return Helper.i64shift(new long[][] {
      {0xFFFFFFFF_00000000L,  0, 0xFFFFFFFF_00000000L},
      {0xFFFFFFFF_00000000L,  1, 0xFFFFFFFF_80000000L},
      {0xFFFFFFFF_00000000L, 32, 0xFFFFFFFF_FFFFFFFFL},
      {0xFFFFFFFF_00000000L, 63, 0xFFFFFFFF_FFFFFFFFL},
      {0xFFFFFFFF_00000000L, 64, 0xFFFFFFFF_00000000L},
      {0xFFFFFFFF_00000000L, 65, 0xFFFFFFFF_80000000L},
      {0x7FFFFFFF_00000000L,  0, 0x7FFFFFFF_00000000L},
      {0x7FFFFFFF_00000000L,  1, 0x3FFFFFFF_80000000L},
      {0x7FFFFFFF_00000000L, 32, 0x00000000_7FFFFFFFL},
      {0x7FFFFFFF_00000000L, 63, 0x00000000_00000000L},
      {0x7FFFFFFF_00000000L, 64, 0x7FFFFFFF_00000000L},
      {0x7FFFFFFF_00000000L, 65, 0x3FFFFFFF_80000000L},
    });
  }
}
