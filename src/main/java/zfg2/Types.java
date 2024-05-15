package zfg2;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.Arrays;

public final class Types {
  private Types() {}


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Virtual Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final Type.Err ERR = new Type.Err();
  public static final Type.Unk UNK = new Type.Unk();

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Primitive Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Composite Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static final Type.Arr Arr(final boolean imut, final Type type) {
    return intern(new Type.Arr(imut, type));
  }
  public static final Type.Arr Arr(final boolean imut, final Type type, final int length) {
    return intern(new Type.Arr(imut, type, length));
  }
  public static final Type.Tup Tup(final boolean[] imuts, final Type[] types) {
    return intern(withoutUnitFields(new Type.Tup(imuts, types)));
  }
  public static final Type.Rec Rec(final boolean[] imuts, final String[] names, final Type[] types) {
    return intern(withoutUnitFields(new Type.Rec(imuts, names, types)));
  }
  public static final Type.Fun Fun(final Type.Rec paramsType, final Type resultType) {
    return intern(new Type.Fun(paramsType, resultType));
  }

  private static final WeakHashMap<Type, WeakReference<Type>> CACHE = new WeakHashMap<>();

  @SuppressWarnings("unchecked")
  private static <T extends Type> T intern(final T type) {
    final WeakReference<Type> ref = CACHE.get(type);
    if (ref != null) {
      final Type interned = ref.get();
      if (interned != null) return (T) interned;
    }
    CACHE.put(type, new WeakReference<>(type));
    return type;
  }

  private static final Type.Tup unitTup = new Type.Tup();
  private static final Type.Tup withoutUnitFields(final Type.Tup tup) {
    final boolean[] imuts  = tup.imuts;
    final Type[]    types = tup.types;
    final int len = imuts.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i].isUnit()) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (!types[j].isUnit()) {
            imuts[i] = imuts[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return unitTup;
        // Otherwise, return a new tuple without the unit fields
        return new Type.Tup(
          Arrays.copyOf(imuts, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return tup;
  }

  private static final Type.Rec unitRec = new Type.Rec();
  private static final Type.Rec withoutUnitFields(final Type.Rec rec) {
    final boolean[] imuts = rec.imuts;
    final String[]  names = rec.names;
    final Type[]    types = rec.types;
    final int len = imuts.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i].isUnit()) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (!types[j].isUnit()) {
            imuts[i] = imuts[j];
            names[i] = names[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return unitRec;
        // Otherwise, return a new record without the unit fields
        return new Type.Rec(
          Arrays.copyOf(imuts, i),
          Arrays.copyOf(names, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return rec;
  }
}
