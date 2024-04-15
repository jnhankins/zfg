package zfg.ast.types;

public enum PriType implements Type {
  bit,
  u08,
  u16,
  u32,
  u64,
  i08,
  i16,
  i32,
  i64,
  f32,
  f64;

  public boolean isPrimitive() { return true; }
  public boolean isObject() { return false; }

  public boolean isNum() {
    return switch (this) { case u08, u16, u32, u64, i08, i16, i32, i64, f32, f64 -> true; default -> false; };
  }
  public boolean isInt() {
    return switch (this) { case u08, u16, u32, u64, i08, i16, i32, i64 -> true; default -> false; };
  }
  public boolean isUnsignedInt() {
    return switch (this) { case u08, u16, u32, u64 -> true; default -> false; };
  }
  public boolean isSignedInt() {
    return switch (this) { case i08, i16, i32, i64 -> true; default -> false; };
  }
  public boolean isFloat() {
    return switch (this) { case f32, f64 -> true; default -> false; };
  }

  public static PriType max(final PriType a, final PriType b) {
		return a.ordinal() >= b.ordinal() ? a : b;
	}
}
