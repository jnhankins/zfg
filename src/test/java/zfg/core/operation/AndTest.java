package zfg.core.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import zfg.old.lang3.ops.And;
import zfg.old.lang3.val.Bit;
import zfg.old.lang3.val.I08;
import zfg.old.lang3.val.I16;
import zfg.old.lang3.val.I32;
import zfg.old.lang3.val.I64;
import zfg.old.lang3.val.U08;
import zfg.old.lang3.val.U16;
import zfg.old.lang3.val.U32;
import zfg.old.lang3.val.U64;

public final class AndTest {

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testBit")
  void testBit(final BitType a, final BitType b, final BitType e) { assertEquals(e, And.bit(a, b)); }
  static Stream<Arguments> testBit() {
    return Helper.bit(new int[][] {
      {0, 0, 0},
      {0, 1, 0},
      {1, 0, 0},
      {1, 1, 1},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testU08")
  void testU08(final U08Type a, final U08Type b, final U08Type e) { assertEquals(e, And.u08(a, b)); }
  static Stream<Arguments> testU08() {
    return Helper.u08(new int[][] {
      {0x00000000, 0x00000000, 0x00000000},
      {0x00000000, 0x0000000F, 0x00000000},
      {0x00000000, 0x000000F0, 0x00000000},
      {0x00000000, 0x000000FF, 0x00000000},

      {0x0000000F, 0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F, 0x0000000F},
      {0x0000000F, 0x000000F0, 0x00000000},
      {0x0000000F, 0x000000FF, 0x0000000F},

      {0x000000F0, 0x00000000, 0x00000000},
      {0x000000F0, 0x0000000F, 0x00000000},
      {0x000000F0, 0x000000F0, 0x000000F0},
      {0x000000F0, 0x000000FF, 0x000000F0},

      {0x000000FF, 0x00000000, 0x00000000},
      {0x000000FF, 0x0000000F, 0x0000000F},
      {0x000000FF, 0x000000F0, 0x000000F0},
      {0x000000FF, 0x000000FF, 0x000000FF},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testU16")
  void testU16(final U16Type a, final U16Type b, final U16Type e) { assertEquals(e, And.u16(a, b)); }
  static Stream<Arguments> testU16() {
    return Helper.u16(new int[][] {
      {0x00000000, 0x00000000, 0x00000000},
      {0x00000000, 0x0000000F, 0x00000000},
      {0x00000000, 0x0000F000, 0x00000000},
      {0x00000000, 0x0000FFFF, 0x00000000},

      {0x0000000F, 0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F, 0x0000000F},
      {0x0000000F, 0x0000F000, 0x00000000},
      {0x0000000F, 0x0000FFFF, 0x0000000F},

      {0x0000F000, 0x00000000, 0x00000000},
      {0x0000F000, 0x0000000F, 0x00000000},
      {0x0000F000, 0x0000F000, 0x0000F000},
      {0x0000F000, 0x0000FFFF, 0x0000F000},

      {0x0000FFFF, 0x00000000, 0x00000000},
      {0x0000FFFF, 0x0000000F, 0x0000000F},
      {0x0000FFFF, 0x0000F000, 0x0000F000},
      {0x0000FFFF, 0x0000FFFF, 0x0000FFFF},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testU32")
  void testU32(final U32Type a, final U32Type b, final U32Type e) { assertEquals(e, And.u32(a, b)); }
  static Stream<Arguments> testU32() {
    return Helper.u32(new int[][] {
      {0x00000000, 0x00000000, 0x00000000},
      {0x00000000, 0x0000000F, 0x00000000},
      {0x00000000, 0xF0000000, 0x00000000},
      {0x00000000, 0xFFFFFFFF, 0x00000000},

      {0x0000000F, 0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F, 0x0000000F},
      {0x0000000F, 0xF0000000, 0x00000000},
      {0x0000000F, 0xFFFFFFFF, 0x0000000F},

      {0xF0000000, 0x00000000, 0x00000000},
      {0xF0000000, 0x0000000F, 0x00000000},
      {0xF0000000, 0xF0000000, 0xF0000000},
      {0xF0000000, 0xFFFFFFFF, 0xF0000000},

      {0xFFFFFFFF, 0x00000000, 0x00000000},
      {0xFFFFFFFF, 0x0000000F, 0x0000000F},
      {0xFFFFFFFF, 0xF0000000, 0xF0000000},
      {0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testU64")
  void testU64(final U64Type a, final U64Type b, final U64Type e) { assertEquals(e, And.u64(a, b)); }
  static Stream<Arguments> testU64() {
    return Helper.u64(new long[][] {
      {0x00000000_00000000L, 0x00000000_00000000L, 0x00000000_00000000L},
      {0x00000000_00000000L, 0x00000000_0000000FL, 0x00000000_00000000L},
      {0x00000000_00000000L, 0xF0000000_00000000L, 0x00000000_00000000L},
      {0x00000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L},

      {0x00000000_0000000FL, 0x00000000_00000000L, 0x00000000_00000000L},
      {0x00000000_0000000FL, 0x00000000_0000000FL, 0x00000000_0000000FL},
      {0x00000000_0000000FL, 0xF0000000_00000000L, 0x00000000_00000000L},
      {0x00000000_0000000FL, 0xFFFFFFFF_FFFFFFFFL, 0x00000000_0000000FL},

      {0xF0000000_00000000L, 0x00000000_00000000L, 0x00000000_00000000L},
      {0xF0000000_00000000L, 0x00000000_0000000FL, 0x00000000_00000000L},
      {0xF0000000_00000000L, 0xF0000000_00000000L, 0xF0000000_00000000L},
      {0xF0000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 0xF0000000_00000000L},

      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L, 0x00000000_00000000L},
      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_0000000FL, 0x00000000_0000000FL},
      {0xFFFFFFFF_FFFFFFFFL, 0xF0000000_00000000L, 0xF0000000_00000000L},
      {0xFFFFFFFF_FFFFFFFFL, 0xFFFFFFFF_FFFFFFFFL, 0xFFFFFFFF_FFFFFFFFL},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testI08")
  void testI08(final I08Type a, final I08Type b, final I08Type e) { assertEquals(e, And.i08(a, b)); }
  static Stream<Arguments> testI08() {
    return Helper.i08(new int[][] {
      {0x00000000, 0x00000000, 0x00000000},
      {0x00000000, 0x0000000F, 0x00000000},
      {0x00000000, 0xFFFFFFF0, 0x00000000},
      {0x00000000, 0xFFFFFFFF, 0x00000000},

      {0x0000000F, 0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F, 0x0000000F},
      {0x0000000F, 0xFFFFFFF0, 0x00000000},
      {0x0000000F, 0xFFFFFFFF, 0x0000000F},

      {0xFFFFFFF0, 0x00000000, 0x00000000},
      {0xFFFFFFF0, 0x0000000F, 0x00000000},
      {0xFFFFFFF0, 0xFFFFFFF0, 0xFFFFFFF0},
      {0xFFFFFFF0, 0xFFFFFFFF, 0xFFFFFFF0},

      {0xFFFFFFFF, 0x00000000, 0x00000000},
      {0xFFFFFFFF, 0x0000000F, 0x0000000F},
      {0xFFFFFFFF, 0xFFFFFFF0, 0xFFFFFFF0},
      {0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testI16")
  void testI16(final I16Type a, final I16Type b, final I16Type e) { assertEquals(e, And.i16(a, b)); }
  static Stream<Arguments> testI16() {
    return Helper.i16(new int[][] {
      {0x00000000, 0x00000000, 0x00000000},
      {0x00000000, 0x0000000F, 0x00000000},
      {0x00000000, 0xFFFFF000, 0x00000000},
      {0x00000000, 0xFFFFFFFF, 0x00000000},

      {0x0000000F, 0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F, 0x0000000F},
      {0x0000000F, 0xFFFFF000, 0x00000000},
      {0x0000000F, 0xFFFFFFFF, 0x0000000F},

      {0xFFFFF000, 0x00000000, 0x00000000},
      {0xFFFFF000, 0x0000000F, 0x00000000},
      {0xFFFFF000, 0xFFFFF000, 0xFFFFF000},
      {0xFFFFF000, 0xFFFFFFFF, 0xFFFFF000},

      {0xFFFFFFFF, 0x00000000, 0x00000000},
      {0xFFFFFFFF, 0x0000000F, 0x0000000F},
      {0xFFFFFFFF, 0xFFFFF000, 0xFFFFF000},
      {0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testI32")
  void testI32(final I32Type a, final I32Type b, final I32Type e) { assertEquals(e, And.i32(a, b)); }
  static Stream<Arguments> testI32() {
    return Helper.i32(new int[][] {
      {0x00000000, 0x00000000, 0x00000000},
      {0x00000000, 0x0000000F, 0x00000000},
      {0x00000000, 0xF0000000, 0x00000000},
      {0x00000000, 0xFFFFFFFF, 0x00000000},

      {0x0000000F, 0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F, 0x0000000F},
      {0x0000000F, 0xF0000000, 0x00000000},
      {0x0000000F, 0xFFFFFFFF, 0x0000000F},

      {0xF0000000, 0x00000000, 0x00000000},
      {0xF0000000, 0x0000000F, 0x00000000},
      {0xF0000000, 0xF0000000, 0xF0000000},
      {0xF0000000, 0xFFFFFFFF, 0xF0000000},

      {0xFFFFFFFF, 0x00000000, 0x00000000},
      {0xFFFFFFFF, 0x0000000F, 0x0000000F},
      {0xFFFFFFFF, 0xF0000000, 0xF0000000},
      {0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF},
    });
  }

  @ParameterizedTest(name = "{index}: {0} and {1} = {2}") @MethodSource("testI64")
  void testI64(final I64Type a, final I64Type b, final I64Type e) { assertEquals(e, And.i64(a, b)); }
  static Stream<Arguments> testI64() {
    return Helper.i64(new long[][] {
      {0x00000000_00000000L, 0x00000000_00000000L, 0x00000000_00000000L},
      {0x00000000_00000000L, 0x00000000_0000000FL, 0x00000000_00000000L},
      {0x00000000_00000000L, 0xF0000000_00000000L, 0x00000000_00000000L},
      {0x00000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L},

      {0x00000000_0000000FL, 0x00000000_00000000L, 0x00000000_00000000L},
      {0x00000000_0000000FL, 0x00000000_0000000FL, 0x00000000_0000000FL},
      {0x00000000_0000000FL, 0xF0000000_00000000L, 0x00000000_00000000L},
      {0x00000000_0000000FL, 0xFFFFFFFF_FFFFFFFFL, 0x00000000_0000000FL},

      {0xF0000000_00000000L, 0x00000000_00000000L, 0x00000000_00000000L},
      {0xF0000000_00000000L, 0x00000000_0000000FL, 0x00000000_00000000L},
      {0xF0000000_00000000L, 0xF0000000_00000000L, 0xF0000000_00000000L},
      {0xF0000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 0xF0000000_00000000L},

      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L, 0x00000000_00000000L},
      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_0000000FL, 0x00000000_0000000FL},
      {0xFFFFFFFF_FFFFFFFFL, 0xF0000000_00000000L, 0xF0000000_00000000L},
      {0xFFFFFFFF_FFFFFFFFL, 0xFFFFFFFF_FFFFFFFFL, 0xFFFFFFFF_FFFFFFFFL},
    });
  }
}
