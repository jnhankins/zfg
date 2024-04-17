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

public final class NotTest {
  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testU08")
  void testU08(final U08 a, final U08 e) { assertEquals(e, Not.u08(a)); }
  static Stream<Arguments> testU08() {
    return Helper.u08(new int[][] {
      {0x00000000, 0x000000FF},
      {0x0000000F, 0x000000F0},
      {0x000000F0, 0x0000000F},
      {0x000000FF, 0x00000000}
    });
  }

  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testU16")
  void testU16(final U16 a, final U16 e) { assertEquals(e, Not.u16(a)); }
  static Stream<Arguments> testU16() {
    return Helper.u16(new int[][] {
      {0x00000000, 0x0000FFFF},
      {0x0000000F, 0x0000FFF0},
      {0x0000F000, 0x00000FFF},
      {0x0000FFFF, 0x00000000}
    });
  }

  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testU32")
  void testU32(final U32 a, final U32 e) { assertEquals(e, Not.u32(a)); }
  static Stream<Arguments> testU32() {
    return Helper.u32(new int[][] {
      {0x00000000, 0xFFFFFFFF},
      {0x0000000F, 0xFFFFFFF0},
      {0xF0000000, 0x0FFFFFFF},
      {0xFFFFFFFF, 0x00000000}
    });
  }

  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testU64")
  void testU64(final U64 a, final U64 e) { assertEquals(e, Not.u64(a)); }
  static Stream<Arguments> testU64() {
    return Helper.u64(new long[][] {
      {0x00000000_00000000L, 0xFFFFFFFF_FFFFFFFFL},
      {0x00000000_0000000FL, 0xFFFFFFFF_FFFFFFF0L},
      {0xF0000000_00000000L, 0x0FFFFFFF_FFFFFFFFL},
      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L}
    });
  }

  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testI08")
  void testI08(final I08 a, final I08 e) { assertEquals(e, Not.i08(a)); }
  static Stream<Arguments> testI08() {
    return Helper.i08(new int[][] {
      {0x00000000, 0xFFFFFFFF},
      {0x0000000F, 0xFFFFFFF0},
      {0xFFFFFFF0, 0x0000000F},
      {0xFFFFFFFF, 0x00000000}
    });
  }

  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testI16")
  void testI16(final I16 a, final I16 e) { assertEquals(e, Not.i16(a)); }
  static Stream<Arguments> testI16() {
    return Helper.i16(new int[][] {
      {0x00000000, 0xFFFFFFFF},
      {0x0000000F, 0xFFFFFFF0},
      {0xFFFFF000, 0x00000FFF},
      {0xFFFFFFFF, 0x00000000}
    });
  }

  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testI32")
  void testI32(final I32 a, final I32 e) { assertEquals(e, Not.i32(a)); }
  static Stream<Arguments> testI32() {
    return Helper.i32(new int[][] {
      {0x00000000, 0xFFFFFFFF},
      {0x0000000F, 0xFFFFFFF0},
      {0xF0000000, 0x0FFFFFFF},
      {0xFFFFFFFF, 0x00000000}
    });
  }

  @ParameterizedTest(name = "{index}: not {0} = {1}") @MethodSource("testI64")
  void testI64(final I64 a, final I64 e) { assertEquals(e, Not.i64(a)); }
  static Stream<Arguments> testI64() {
    return Helper.i64(new long[][] {
      {0x00000000_00000000L, 0xFFFFFFFF_FFFFFFFFL},
      {0x00000000_0000000FL, 0xFFFFFFFF_FFFFFFF0L},
      {0xF0000000_00000000L, 0x0FFFFFFF_FFFFFFFFL},
      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L}
    });
  }
}
