package zfg.core.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import zfg.old.lang3.ops.Neq;
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

public final class NeqTest {

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testBit")
  void testBit(final BitType a, final BitType b, final BitType e) { assertEquals(e, Neq.bit(a, b)); }
  static Stream<Arguments> testBit() {
    return Helper.bitrel(new int[][] {
      {0, 0, 0},
      {0, 1, 1},
      {1, 0, 1},
      {1, 1, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testU08")
  void testU08(final U08Type a, final U08Type b, final BitType e) { assertEquals(e, Neq.u08(a, b)); }
  static Stream<Arguments> testU08() {
    return Helper.u08rel(new int[][] {
      {0x00000000, 0x00000000, 0},
      {0x00000000, 0x0000000F, 1},
      {0x00000000, 0x000000F0, 1},
      {0x00000000, 0x000000FF, 1},

      {0x0000000F, 0x00000000, 1},
      {0x0000000F, 0x0000000F, 0},
      {0x0000000F, 0x000000F0, 1},
      {0x0000000F, 0x000000FF, 1},

      {0x000000F0, 0x00000000, 1},
      {0x000000F0, 0x0000000F, 1},
      {0x000000F0, 0x000000F0, 0},
      {0x000000F0, 0x000000FF, 1},

      {0x000000FF, 0x00000000, 1},
      {0x000000FF, 0x0000000F, 1},
      {0x000000FF, 0x000000F0, 1},
      {0x000000FF, 0x000000FF, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testU16")
  void testU16(final U16Type a, final U16Type b, final BitType e) { assertEquals(e, Neq.u16(a, b)); }
  static Stream<Arguments> testU16() {
    return Helper.u16rel(new int[][] {
      {0x00000000, 0x00000000, 0},
      {0x00000000, 0x0000000F, 1},
      {0x00000000, 0x0000F000, 1},
      {0x00000000, 0x0000FFFF, 1},

      {0x0000000F, 0x00000000, 1},
      {0x0000000F, 0x0000000F, 0},
      {0x0000000F, 0x0000F000, 1},
      {0x0000000F, 0x0000FFFF, 1},

      {0x0000F000, 0x00000000, 1},
      {0x0000F000, 0x0000000F, 1},
      {0x0000F000, 0x0000F000, 0},
      {0x0000F000, 0x0000FFFF, 1},

      {0x0000FFFF, 0x00000000, 1},
      {0x0000FFFF, 0x0000000F, 1},
      {0x0000FFFF, 0x0000F000, 1},
      {0x0000FFFF, 0x0000FFFF, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testU32")
  void testU32(final U32Type a, final U32Type b, final BitType e) { assertEquals(e, Neq.u32(a, b)); }
  static Stream<Arguments> testU32() {
    return Helper.u32rel(new int[][] {
      {0x00000000, 0x00000000, 0},
      {0x00000000, 0x0000000F, 1},
      {0x00000000, 0xF0000000, 1},
      {0x00000000, 0xFFFFFFFF, 1},

      {0x0000000F, 0x00000000, 1},
      {0x0000000F, 0x0000000F, 0},
      {0x0000000F, 0xF0000000, 1},
      {0x0000000F, 0xFFFFFFFF, 1},

      {0xF0000000, 0x00000000, 1},
      {0xF0000000, 0x0000000F, 1},
      {0xF0000000, 0xF0000000, 0},
      {0xF0000000, 0xFFFFFFFF, 1},

      {0xFFFFFFFF, 0x00000000, 1},
      {0xFFFFFFFF, 0x0000000F, 1},
      {0xFFFFFFFF, 0xF0000000, 1},
      {0xFFFFFFFF, 0xFFFFFFFF, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testU64")
  void testU64(final U64Type a, final U64Type b, final BitType e) { assertEquals(e, Neq.u64(a, b)); }
  static Stream<Arguments> testU64() {
    return Helper.u64rel(new long[][] {
      {0x00000000_00000000L, 0x00000000_00000000L, 0},
      {0x00000000_00000000L, 0x00000000_0000000FL, 1},
      {0x00000000_00000000L, 0xF0000000_00000000L, 1},
      {0x00000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 1},

      {0x00000000_0000000FL, 0x00000000_00000000L, 1},
      {0x00000000_0000000FL, 0x00000000_0000000FL, 0},
      {0x00000000_0000000FL, 0xF0000000_00000000L, 1},
      {0x00000000_0000000FL, 0xFFFFFFFF_FFFFFFFFL, 1},

      {0xF0000000_00000000L, 0x00000000_00000000L, 1},
      {0xF0000000_00000000L, 0x00000000_0000000FL, 1},
      {0xF0000000_00000000L, 0xF0000000_00000000L, 0},
      {0xF0000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 1},

      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L, 1},
      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_0000000FL, 1},
      {0xFFFFFFFF_FFFFFFFFL, 0xF0000000_00000000L, 1},
      {0xFFFFFFFF_FFFFFFFFL, 0xFFFFFFFF_FFFFFFFFL, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testI08")
  void testI08(final I08Type a, final I08Type b, final BitType e) { assertEquals(e, Neq.i08(a, b)); }
  static Stream<Arguments> testI08() {
    return Helper.i08rel(new int[][] {
      {0x00000000, 0x00000000, 0},
      {0x00000000, 0x0000000F, 1},
      {0x00000000, 0xFFFFFFF0, 1},
      {0x00000000, 0xFFFFFFFF, 1},

      {0x0000000F, 0x00000000, 1},
      {0x0000000F, 0x0000000F, 0},
      {0x0000000F, 0xFFFFFFF0, 1},
      {0x0000000F, 0xFFFFFFFF, 1},

      {0xFFFFFFF0, 0x00000000, 1},
      {0xFFFFFFF0, 0x0000000F, 1},
      {0xFFFFFFF0, 0xFFFFFFF0, 0},
      {0xFFFFFFF0, 0xFFFFFFFF, 1},

      {0xFFFFFFFF, 0x00000000, 1},
      {0xFFFFFFFF, 0x0000000F, 1},
      {0xFFFFFFFF, 0xFFFFFFF0, 1},
      {0xFFFFFFFF, 0xFFFFFFFF, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testI16")
  void testI16(final I16Type a, final I16Type b, final BitType e) { assertEquals(e, Neq.i16(a, b)); }
  static Stream<Arguments> testI16() {
    return Helper.i16rel(new int[][] {
      {0x00000000, 0x00000000, 0},
      {0x00000000, 0x0000000F, 1},
      {0x00000000, 0xFFFFF000, 1},
      {0x00000000, 0xFFFFFFFF, 1},

      {0x0000000F, 0x00000000, 1},
      {0x0000000F, 0x0000000F, 0},
      {0x0000000F, 0xFFFFF000, 1},
      {0x0000000F, 0xFFFFFFFF, 1},

      {0xFFFFF000, 0x00000000, 1},
      {0xFFFFF000, 0x0000000F, 1},
      {0xFFFFF000, 0xFFFFF000, 0},
      {0xFFFFF000, 0xFFFFFFFF, 1},

      {0xFFFFFFFF, 0x00000000, 1},
      {0xFFFFFFFF, 0x0000000F, 1},
      {0xFFFFFFFF, 0xFFFFF000, 1},
      {0xFFFFFFFF, 0xFFFFFFFF, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testI32")
  void testI32(final I32Type a, final I32Type b, final BitType e) { assertEquals(e, Neq.i32(a, b)); }
  static Stream<Arguments> testI32() {
    return Helper.i32rel(new int[][] {
      {0x00000000, 0x00000000, 0},
      {0x00000000, 0x0000000F, 1},
      {0x00000000, 0xF0000000, 1},
      {0x00000000, 0xFFFFFFFF, 1},

      {0x0000000F, 0x00000000, 1},
      {0x0000000F, 0x0000000F, 0},
      {0x0000000F, 0xF0000000, 1},
      {0x0000000F, 0xFFFFFFFF, 1},

      {0xF0000000, 0x00000000, 1},
      {0xF0000000, 0x0000000F, 1},
      {0xF0000000, 0xF0000000, 0},
      {0xF0000000, 0xFFFFFFFF, 1},

      {0xFFFFFFFF, 0x00000000, 1},
      {0xFFFFFFFF, 0x0000000F, 1},
      {0xFFFFFFFF, 0xF0000000, 1},
      {0xFFFFFFFF, 0xFFFFFFFF, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testI64")
  void testI64(final I64Type a, final I64Type b, final BitType e) { assertEquals(e, Neq.i64(a, b)); }
  static Stream<Arguments> testI64() {
    return Helper.i64rel(new long[][] {
      {0x00000000_00000000L, 0x00000000_00000000L, 0},
      {0x00000000_00000000L, 0x00000000_0000000FL, 1},
      {0x00000000_00000000L, 0xF0000000_00000000L, 1},
      {0x00000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 1},

      {0x00000000_0000000FL, 0x00000000_00000000L, 1},
      {0x00000000_0000000FL, 0x00000000_0000000FL, 0},
      {0x00000000_0000000FL, 0xF0000000_00000000L, 1},
      {0x00000000_0000000FL, 0xFFFFFFFF_FFFFFFFFL, 1},

      {0xF0000000_00000000L, 0x00000000_00000000L, 1},
      {0xF0000000_00000000L, 0x00000000_0000000FL, 1},
      {0xF0000000_00000000L, 0xF0000000_00000000L, 0},
      {0xF0000000_00000000L, 0xFFFFFFFF_FFFFFFFFL, 1},

      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_00000000L, 1},
      {0xFFFFFFFF_FFFFFFFFL, 0x00000000_0000000FL, 1},
      {0xFFFFFFFF_FFFFFFFFL, 0xF0000000_00000000L, 1},
      {0xFFFFFFFF_FFFFFFFFL, 0xFFFFFFFF_FFFFFFFFL, 0},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testF32")
  void testF32(final F32Type a, final F32Type b, final BitType e) { assertEquals(e, Neq.f32(a, b)); }
  static Stream<Arguments> testF32() {
    return Helper.f32rel(new float[][] {
      // +0.0, -0.0, +1.5, -1.5, +Inf, -Inf, NaN,
      {+0.0f, +0.0f, 0},
      {+0.0f, -0.0f, 0},
      {+0.0f, -1.5f, 1},
      {+0.0f, +1.5f, 1},
      {+0.0f, Float.POSITIVE_INFINITY, 1},
      {+0.0f, Float.NEGATIVE_INFINITY, 1},
      {+0.0f, Float.NaN, 1},

      {-0.0f, +0.0f, 0},
      {-0.0f, -0.0f, 0},
      {-0.0f, -1.5f, 1},
      {-0.0f, +1.5f, 1},
      {-0.0f, Float.POSITIVE_INFINITY, 1},
      {-0.0f, Float.NEGATIVE_INFINITY, 1},
      {-0.0f, Float.NaN, 1},

      {+1.5f, +0.0f, 1},
      {+1.5f, -0.0f, 1},
      {+1.5f, -1.5f, 1},
      {+1.5f, +1.5f, 0},
      {+1.5f, Float.POSITIVE_INFINITY, 1},
      {+1.5f, Float.NEGATIVE_INFINITY, 1},
      {+1.5f, Float.NaN, 1},

      {-1.5f, +0.0f, 1},
      {-1.5f, -0.0f, 1},
      {-1.5f, -1.5f, 0},
      {-1.5f, +1.5f, 1},
      {-1.5f, Float.POSITIVE_INFINITY, 1},
      {-1.5f, Float.NEGATIVE_INFINITY, 1},
      {-1.5f, Float.NaN, 1},

      {Float.POSITIVE_INFINITY, +0.0f, 1},
      {Float.POSITIVE_INFINITY, -0.0f, 1},
      {Float.POSITIVE_INFINITY, -1.5f, 1},
      {Float.POSITIVE_INFINITY, +1.5f, 1},
      {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0},
      {Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 1},
      {Float.POSITIVE_INFINITY, Float.NaN, 1},

      {Float.NEGATIVE_INFINITY, +0.0f, 1},
      {Float.NEGATIVE_INFINITY, -0.0f, 1},
      {Float.NEGATIVE_INFINITY, -1.5f, 1},
      {Float.NEGATIVE_INFINITY, +1.5f, 1},
      {Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 1},
      {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, 0},
      {Float.NEGATIVE_INFINITY, Float.NaN, 1},

      {Float.NaN, +0.0f, 1},
      {Float.NaN, -0.0f, 1},
      {Float.NaN, -1.5f, 1},
      {Float.NaN, +1.5f, 1},
      {Float.NaN, Float.POSITIVE_INFINITY, 1},
      {Float.NaN, Float.NEGATIVE_INFINITY, 1},
      {Float.NaN, Float.NaN, 1},
    });
  }

  @ParameterizedTest(name = "{index}: {0} neq {1} = {2}") @MethodSource("testF64")
  void testF64(final F64Type a, final F64Type b, final BitType e) { assertEquals(e, Neq.f64(a, b)); }
  static Stream<Arguments> testF64() {
    return Helper.f64rel(new double[][] {
      // +0.0, -0.0, +1.5, -1.5, +Inf, -Inf, NaN,
      {+0.0, +0.0, 0},
      {+0.0, -0.0, 0},
      {+0.0, -1.5, 1},
      {+0.0, +1.5, 1},
      {+0.0, Double.POSITIVE_INFINITY, 1},
      {+0.0, Double.NEGATIVE_INFINITY, 1},
      {+0.0, Double.NaN, 1},

      {-0.0, +0.0, 0},
      {-0.0, -0.0, 0},
      {-0.0, -1.5, 1},
      {-0.0, +1.5, 1},
      {-0.0, Double.POSITIVE_INFINITY, 1},
      {-0.0, Double.NEGATIVE_INFINITY, 1},
      {-0.0, Double.NaN, 1},

      {+1.5, +0.0, 1},
      {+1.5, -0.0, 1},
      {+1.5, -1.5, 1},
      {+1.5, +1.5, 0},
      {+1.5, Double.POSITIVE_INFINITY, 1},
      {+1.5, Double.NEGATIVE_INFINITY, 1},
      {+1.5, Double.NaN, 1},

      {-1.5, +0.0, 1},
      {-1.5, -0.0, 1},
      {-1.5, -1.5, 0},
      {-1.5, +1.5, 1},
      {-1.5, Double.POSITIVE_INFINITY, 1},
      {-1.5, Double.NEGATIVE_INFINITY, 1},
      {-1.5, Double.NaN, 1},

      {Double.POSITIVE_INFINITY, +0.0, 1},
      {Double.POSITIVE_INFINITY, -0.0, 1},
      {Double.POSITIVE_INFINITY, -1.5, 1},
      {Double.POSITIVE_INFINITY, +1.5, 1},
      {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0},
      {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1},
      {Double.POSITIVE_INFINITY, Double.NaN, 1},

      {Double.NEGATIVE_INFINITY, +0.0, 1},
      {Double.NEGATIVE_INFINITY, -0.0, 1},
      {Double.NEGATIVE_INFINITY, -1.5, 1},
      {Double.NEGATIVE_INFINITY, +1.5, 1},
      {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1},
      {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0},
      {Double.NEGATIVE_INFINITY, Double.NaN, 1},

      {Double.NaN, +0.0, 1},
      {Double.NaN, -0.0, 1},
      {Double.NaN, -1.5, 1},
      {Double.NaN, +1.5, 1},
      {Double.NaN, Double.POSITIVE_INFINITY, 1},
      {Double.NaN, Double.NEGATIVE_INFINITY, 1},
      {Double.NaN, Double.NaN, 1},
    });
  }
}
