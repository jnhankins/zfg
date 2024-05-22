package zfg;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;

public final class Types {
  private Types() {}


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
  
  public static final Type.Arr arr(final boolean muta, final Type type, final int size) {
    return intern(new Type.Arr(muta, type, size));
  }
  public static final Type.Tup tup(final boolean[] mutas, final Type[] types) {
    return intern(withoutUnitFields(new Type.Tup(mutas, types)));
  }
  public static final Type.Rec rec(final boolean[] mutas, final String[] names, final Type[] types) {
    return intern(withoutUnitFields(new Type.Rec(mutas, names, types)));
  }
  public static final Type.Fun fun(final Type pType, final Type rType) {
    return intern(new Type.Fun(pType, rType));
  }
  public static final Type.Nom nom(final String name, final Type type) {
    return intern(new Type.Nom(name, type));
  }

  private static final WeakHashMap<Type, WeakReference<Type>> CACHE = new WeakHashMap<>();
  private static final ReentrantLock CACHE_LOCK = new ReentrantLock();

  @SuppressWarnings("unchecked")
  private static <T extends Type> T intern(final T type) {
    // TODO: Use a caffine or something other concurrent cash with weak keys and values

    // Try to get the the type from the cache without locking
    final WeakReference<Type> cacheEntry0 = CACHE.get(type);
    if (cacheEntry0 != null) {
      final Type cachedType = cacheEntry0.get();
      if (cachedType != null) return (T) cachedType;
    }
    // If the type is not in the cache, wait for the lock, then try again
    CACHE_LOCK.lock();
    final WeakReference<Type> cacheEntry1 = CACHE.get(type);
    if (cacheEntry1 != null) {
      final Type cachedType = cacheEntry1.get();
      if (cachedType != null) {
        CACHE_LOCK.lock();
        return (T) cachedType;
      }
    }
    // If the type is still not in the cache, add it
    CACHE.put(type, new WeakReference<>(type));
    CACHE_LOCK.unlock();
    return type;
  }

  private static final Type.Tup unitTup = new Type.Tup();
  private static final Type.Tup withoutUnitFields(final Type.Tup tup) {
    final boolean[] mutas = tup.mutas;
    final Type[] types = tup.types;
    final int len = types.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i].isUnit()) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (!types[j].isUnit()) {
            mutas[i] = mutas[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return unitTup;
        // Otherwise, return a new tuple without the unit fields
        return new Type.Tup(
          Arrays.copyOf(mutas, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return tup;
  }

  private static final Type.Rec unitRec = new Type.Rec();
  private static final Type.Rec withoutUnitFields(final Type.Rec rec) {
    final boolean[] mutas = rec.mutas;
    final String[] names = rec.names;
    final Type[] types = rec.types;
    final int len = types.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i].isUnit()) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (!types[j].isUnit()) {
            mutas[i] = mutas[j];
            names[i] = names[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return unitRec;
        // Otherwise, return a new record without the unit fields
        return new Type.Rec(
          Arrays.copyOf(mutas, i),
          Arrays.copyOf(names, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return rec;
  }
}
