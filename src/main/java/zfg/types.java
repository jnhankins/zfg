package zfg;

public final class Types {
  private Types() {}

  public static final Type.Rec UNT = new Type.Rec();
  public static final Type.Err ERR = new Type.Err();
  public static final Type.Unk UNK = new Type.Unk();
  public static final Type.Bit BIT = new Type.Bit();
  public static final Type.U08 U08 = new Type.U08();
  public static final Type.U16 U16 = new Type.U16();
  public static final Type.U32 U32 = new Type.U32();
  public static final Type.U64 U64 = new Type.U64();
  public static final Type.I08 I08 = new Type.I08();
  public static final Type.I16 I16 = new Type.I16();
  public static final Type.I32 I32 = new Type.I32();
  public static final Type.I64 I64 = new Type.I64();
  public static final Type.F32 F32 = new Type.F32();
  public static final Type.F64 F64 = new Type.F64();

  public static final Type arr(final boolean muta, final Type type, final int size) {
    return new Type.Arr(muta, type, size);
  }

  public static final Type tup(final boolean[] mutas, final Type[] types) {
    assert mutas.length == types.length;
    return mutas.length == 0 ? UNT : new Type.Tup(mutas, types);
  }

  public static final Type rec(final boolean[] mutas, final String[] names, final Type[] types) {
    assert mutas.length == types.length && types.length == names.length;
    return mutas.length == 0 ? UNT : new Type.Rec(mutas, names, types);
  }

  public static final Type fun(final Type pType, final Type rType) {
    return new Type.Fun(pType, rType);
  }

  public static final Type nom(final String name, final Type type) {
    return new Type.Nom(name, type);
  }
}
