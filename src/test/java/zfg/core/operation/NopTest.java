package zfg.core.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import zfg.old.lang3.ops.Nop;
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

public final class NopTest {

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testBit")
  void testBit(final BitType a, final BitType e) { assertEquals(e, Nop.bit(a)); }
  static Stream<Arguments> testBit() {
    return Helper.bit(new int[][] {
      {0, 0},
      {1, 1},
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testU08")
  void testU08(final U08Type a, final U08Type e) { assertEquals(e, Nop.u08(a)); }
  static Stream<Arguments> testU08() {
    return Helper.u08(new int[][] {
      {0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F},
      {0x000000F0, 0x000000F0},
      {0x000000FF, 0x000000FF}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testU16")
  void testU16(final U16Type a, final U16Type e) { assertEquals(e, Nop.u16(a)); }
  static Stream<Arguments> testU16() {
    return Helper.u16(new int[][] {
      {0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F},
      {0x0000F000, 0x0000F000},
      {0x0000FFFF, 0x0000FFFF}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testU32")
  void testU32(final U32Type a, final U32Type e) { assertEquals(e, Nop.u32(a)); }
  static Stream<Arguments> testU32() {
    return Helper.u32(new int[][] {
      {0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F},
      {0xF0000000, 0xF0000000},
      {0xFFFFFFFF, 0xFFFFFFFF}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testU64")
  void testU64(final U64Type a, final U64Type e) { assertEquals(e, Nop.u64(a)); }
  static Stream<Arguments> testU64() {
    return Helper.u64(new long[][] {
      {0x0000000000000000L, 0x0000000000000000L},
      {0x000000000000000FL, 0x000000000000000FL},
      {0xF000000000000000L, 0xF000000000000000L},
      {0xFFFFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testI08")
  void testI08(final I08Type a, final I08Type e) { assertEquals(e, Nop.i08(a)); }
  static Stream<Arguments> testI08() {
    return Helper.i08(new int[][] {
      {0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F},
      {0xFFFFFFF0, 0xFFFFFFF0},
      {0xFFFFFFFF, 0xFFFFFFFF}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testI16")
  void testI16(final I16Type a, final I16Type e) { assertEquals(e, Nop.i16(a)); }
  static Stream<Arguments> testI16() {
    return Helper.i16(new int[][] {
      {0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F},
      {0xFFFFF000, 0xFFFFF000},
      {0xFFFFFFFF, 0xFFFFFFFF}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testI32")
  void testI32(final I32Type a, final I32Type e) { assertEquals(e, Nop.i32(a)); }
  static Stream<Arguments> testI32() {
    return Helper.i32(new int[][] {
      {0x00000000, 0x00000000},
      {0x0000000F, 0x0000000F},
      {0xF0000000, 0xF0000000},
      {0xFFFFFFFF, 0xFFFFFFFF}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testI64")
  void testI64(final I64Type a, final I64Type e) { assertEquals(e, Nop.i64(a)); }
  static Stream<Arguments> testI64() {
    return Helper.i64(new long[][] {
      {0x0000000000000000L, 0x0000000000000000L},
      {0x000000000000000FL, 0x000000000000000FL},
      {0xF000000000000000L, 0xF000000000000000L},
      {0xFFFFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL}
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testF32")
  void testF32(final F32Type a, final F32Type e) { assertEquals(e, Nop.f32(a)); }
  static Stream<Arguments> testF32() {
    return Helper.f32(new float[][] {
      {0.0f, 0.0f},
      {1.1f, 1.1f},
      {-0.0f, -0.0f}, // negative zero
      {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY},
      {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY},
      {Float.NaN, Float.NaN},
    });
  }

  @ParameterizedTest(name = "{index}: nop {0} = {1}") @MethodSource("testF64")
  void testF64(final F64Type a, final F64Type e) { assertEquals(e, Nop.f64(a)); }
  static Stream<Arguments> testF64() {
    return Helper.f64(new double[][] {
      {0.0, 0.0},
      {1.1, 1.1},
      {-0.0, -0.0}, // negative zero
      {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
      {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
      {Double.NaN, Double.NaN},
    });
  }
}
