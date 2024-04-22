package zfg.core.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import zfg.lang.operation.Neg;
import zfg.lang.primitive.F32;
import zfg.lang.primitive.F64;
import zfg.lang.primitive.I08;
import zfg.lang.primitive.I16;
import zfg.lang.primitive.I32;
import zfg.lang.primitive.I64;

public final class NegTest {

  @ParameterizedTest(name = "{index}: neg {0} = {1}") @MethodSource("testI08")
  void testI08(final I08 a, final I08 e) { assertEquals(e, Neg.i08(a)); }
  static Stream<Arguments> testI08() {
    return Helper.i08(new int[][] {
      {0xFFFFFF80, 0xFFFFFF80}, // -128 => -128
      {0xFFFFFFF0, 0x00000010}, // -127 => 127
      {0xFFFFFFFF, 0x00000001}, //   -1 => 1
      {0x00000000, 0x00000000}, //    0 => 0
      {0x00000001, 0xFFFFFFFF}, //    1 => -1
      {0x0000000F, 0xFFFFFFF1}, //   15 => -15
      {0x0000007F, 0xFFFFFF81}, //  127 => -127
    });
  }

  @ParameterizedTest(name = "{index}: neg {0} = {1}") @MethodSource("testI16")
  void testI16(final I16 a, final I16 e) { assertEquals(e, Neg.i16(a)); }
  static Stream<Arguments> testI16() {
    return Helper.i16(new int[][] {
      {0xFFFF8000, 0xFFFF8000}, // -32768 => -32768
      {0xFFFFFFF0, 0x00000010}, // -32767 => 32767
      {0xFFFFFFFF, 0x00000001}, //     -1 => 1
      {0x00000000, 0x00000000}, //      0 => 0
      {0x00000001, 0xFFFFFFFF}, //      1 => -1
      {0x0000000F, 0xFFFFFFF1}, //     15 => -15
      {0x00007FFF, 0xFFFF8001}  //  32767 => -32767
    });
  }

  @ParameterizedTest(name = "{index}: neg {0} = {1}") @MethodSource("testI32")
  void testI32(final I32 a, final I32 e) { assertEquals(e, Neg.i32(a)); }
  static Stream<Arguments> testI32() {
    return Helper.i32(new int[][] {
      {0x80000000, 0x80000000}, // -2147483648 => -2147483648
      {0xFFFFFFF0, 0x00000010}, // -2147483647 => 2147483647
      {0xFFFFFFFF, 0x00000001}, //          -1 => 1
      {0x00000000, 0x00000000}, //           0 => 0
      {0x00000001, 0xFFFFFFFF}, //           1 => -1
      {0x0000000F, 0xFFFFFFF1}, //          15 => -15
      {0x7FFFFFFF, 0x80000001}  //  2147483647 => -2147483647
    });
  }

  @ParameterizedTest(name = "{index}: neg {0} = {1}") @MethodSource("testI64")
  void testI64(final I64 a, final I64 e) { assertEquals(e, Neg.i64(a)); }
  static Stream<Arguments> testI64() {
    return Helper.i64(new long[][] {
      {0x8000000000000000L, 0x8000000000000000L}, // -9223372036854775808 => -9223372036854775808
      {0xFFFFFFFFFFFFFFF0L, 0x0000000000000010L}, // -9223372036854775807 => 9223372036854775807
      {0xFFFFFFFFFFFFFFFFL, 0x0000000000000001L}, //                   -1 => 1
      {0x0000000000000000L, 0x0000000000000000L}, //                    0 => 0
      {0x0000000000000001L, 0xFFFFFFFFFFFFFFFFL}, //                    1 => -1
      {0x000000000000000FL, 0xFFFFFFFFFFFFFFF1L}, //                   15 => -15
      {0x7FFFFFFFFFFFFFFFL, 0x8000000000000001L}  //  9223372036854775807 => -9223372036854775807
    });
  }

  @ParameterizedTest(name = "{index}: neg {0} = {1}") @MethodSource("testF32")
  void testF32(final F32 a, final F32 e) { assertEquals(e, Neg.f32(a)); }
  static Stream<Arguments> testF32() {
    return Helper.f32(new float[][] {
      {Float.NaN, Float.NaN},
      {Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY},
      {-Float.MAX_VALUE, Float.MAX_VALUE},
      {-1.5f, 1.5f},
      {-Float.MIN_NORMAL, Float.MIN_NORMAL},
      {-Float.MIN_VALUE, Float.MIN_VALUE},
      {-0.0f, 0.0f},
      {0.0f, -0.0f},
      {Float.MIN_VALUE, -Float.MIN_VALUE},
      {Float.MIN_NORMAL, -Float.MIN_NORMAL},
      {1.5f, -1.5f},
      {Float.MAX_VALUE, -Float.MAX_VALUE},
      {Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY},
    });
  }

  @ParameterizedTest(name = "{index}: neg {0} = {1}") @MethodSource("testF64")
  void testF64(final F64 a, final F64 e) { assertEquals(e, Neg.f64(a)); }
  static Stream<Arguments> testF64() {
    return Helper.f64(new double[][] {
      {Double.NaN, Double.NaN},
      {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY},
      {-Double.MAX_VALUE, Double.MAX_VALUE},
      {-1.5, 1.5},
      {-Double.MIN_NORMAL, Double.MIN_NORMAL},
      {-Double.MIN_VALUE, Double.MIN_VALUE},
      {-0.0, 0.0},
      {0.0, -0.0},
      {Double.MIN_VALUE, -Double.MIN_VALUE},
      {Double.MIN_NORMAL, -Double.MIN_NORMAL},
      {1.5, -1.5},
      {Double.MAX_VALUE, -Double.MAX_VALUE},
      {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY},
    });
  }
}
