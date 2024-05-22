package zfg;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public sealed interface Type {
  Kind kind();
  boolean isUnit();
  boolean hasError();
  boolean hasUnknown();
  String toString();
  String toString(final boolean pretty);
  void appendTo(final StringBuilder sb);
  void appendTo(final StringBuilder sb, final boolean pretty);
  boolean isAssignableTo(final Type targetType);
  boolean isAssignableTo(final Type targetType, final Cycles cyclic);

  public static enum Kind {
    ERR, UNK, BIT, U08, U16, U32, U64, I08, I16, I32, I64, F32, F64, ARR, TUP, REC, FUN, NOM;
  }

  public static final class Err implements Type {
    private static final String NAME = "err";
    Err() {}
    @Override public Kind kind() { return Kind.ERR; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return true; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { throw new UnsupportedOperationException(); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { throw new UnsupportedOperationException(); }
  }

  public static final class Unk implements Type {
    private static final String NAME = "Unk";
    Unk() {}
    @Override public Kind kind() { return Kind.UNK; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return true; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { throw new UnsupportedOperationException(); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { throw new UnsupportedOperationException(); }
  }

  public static final class Bit implements Type {
    private static final String NAME = "bit";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.BIT);
    Bit() {}
    @Override public Kind kind() { return Kind.BIT; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class U08 implements Type {
    private static final String NAME = "u08";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(
      Kind.U08, Kind.U16, Kind.U32, Kind.U64, Kind.I16, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    U08() {}
    @Override public Kind kind() { return Kind.U08; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class U16 implements Type {
    private static final String NAME = "u16";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(
      Kind.U16, Kind.U32, Kind.U64, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    U16() {}
    @Override public Kind kind() { return Kind.U16; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class U32 implements Type {
    private static final String NAME = "u32";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.U32, Kind.U64, Kind.I64, Kind.F64);
    U32() {}
    @Override public Kind kind() { return Kind.U32; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class U64 implements Type {
    private static final String NAME = "u64";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.U64);
    U64() {}
    @Override public Kind kind() { return Kind.U64; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class I08 implements Type {
    private static final String NAME = "i08";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I08, Kind.I16, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    I08() {}
    @Override public Kind kind() { return Kind.I08; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class I16 implements Type {
    private static final String NAME = "i16";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I16, Kind.I32, Kind.I64, Kind.F32, Kind.F64);
    I16() {}
    @Override public Kind kind() { return Kind.I16; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class I32 implements Type {
    private static final String NAME = "i32";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I32, Kind.I64, Kind.F64);
    I32() {}
    @Override public Kind kind() { return Kind.I32; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class I64 implements Type {
    private static final String NAME = "i64";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.I64);
    I64() {}
    @Override public Kind kind() { return Kind.I64; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class F32 implements Type {
    private static final String NAME = "f32";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.F32, Kind.F64);
    F32() {}
    @Override public Kind kind() { return Kind.F32; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class F64 implements Type {
    private static final String NAME = "f64";
    private static final EnumSet<Kind> ASSIGNABLE = EnumSet.of(Kind.F64);
    F64() {}
    @Override public Kind kind() { return Kind.F64; }
    @Override public boolean isUnit() { return false; }
    @Override public boolean hasError() { return false; }
    @Override public boolean hasUnknown() { return false; }
    @Override public String toString() { return NAME; }
    @Override public String toString(final boolean pretty) { return NAME; }
    @Override public void appendTo(final StringBuilder sb) { sb.append(NAME); }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) { sb.append(NAME); }
    @Override public boolean isAssignableTo(final Type t) { return ASSIGNABLE.contains(dealias(t).kind()); }
    @Override public boolean isAssignableTo(final Type t, final Cycles c) { return isAssignableTo(t); }
  }

  public static final class Arr implements Type {
    public static final int UNKNOWN_SIZE = -1;
    public final boolean muta;
    public final Type    type;
    public final int     size;
    Arr(final boolean muta, final Type type, final int size) {
      assert type != null;
      assert size == UNKNOWN_SIZE || size >= 0;
      this.muta = muta;
      this.type = type;
      this.size = size;
    }
    @Override public Kind kind() {
      return Kind.ARR;
    }
    @Override public boolean isUnit() {
      return false;
    }
    @Override public boolean hasError() {
      return type.hasError();
    }
    @Override public boolean hasUnknown() {
      return type.hasUnknown();
    }
    @Override public int hashCode() {
      return Objects.hash(Arr.class, muta, type, size);
    }
    @Override public boolean equals(final Object o) {
      return this == o || (o instanceof final Arr t && muta == t.muta && type == t.type && size == t.size);
    }
    @Override public String toString() {
      return toString(false);
    }
    @Override public String toString(final boolean pretty) {
      final StringBuilder sb = new StringBuilder(); appendTo(sb, pretty); return sb.toString();
    }
    @Override public void appendTo(final StringBuilder sb) {
      appendTo(sb, false);
    }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) {
      Type.appendTo(this, sb, pretty);
    }
    @Override public boolean isAssignableTo(final Type target) { return isAssignableTo(target, null); }
    @Override public boolean isAssignableTo(final Type target, final Cycles cycles) {
      return switch (dealias(target)) {
        case final Arr tgt -> {
          if (tgt.size != UNKNOWN_SIZE && size != tgt.size) yield false;
          if (tgt.muta && !muta) yield false;
          if (!type.isAssignableTo(tgt.type, cycles)) yield false;
          yield true;
        }
        case final Tup tgt -> {
          if (size != tgt.size) yield false;
          for (int i = 0; i < tgt.size; i++) if (tgt.mutas[i] && !muta) yield false;
          for (int i = 0; i < tgt.size; i++) if (!type.isAssignableTo(tgt.types[i], cycles)) yield false;
          yield true;
        }
        case final Rec tgt -> {
          if (size != tgt.size) yield false;
          for (int i = 0; i < tgt.size; i++) if (tgt.mutas[i] && !muta) yield false;
          for (int i = 0; i < tgt.size; i++) if (!type.isAssignableTo(tgt.types[i], cycles)) yield false;
          yield true;
        }
        case null, default -> false;
      };
    }
  }

  public static final class Tup implements Type {
    public final boolean[] mutas;
    public final Type[]    types;
    public final int       size;
    Tup() {
      this.mutas = new boolean[0];
      this.types = new Type[0];
      this.size = 0;
    }
    Tup(final boolean[] mutas, final Type[] types) {
      assert mutas != null;
      assert types != null;
      assert types.length == mutas.length;
      assert Arrays.stream(types).allMatch(Objects::nonNull);
      this.mutas = mutas;
      this.types = types;
      this.size = types.length;
    }
    @Override public Kind kind() {
      return Kind.TUP;
    }
    @Override public boolean isUnit() {
      return mutas.length == 0;
    }
    @Override public boolean hasError() {
      for (int i = 0; i < types.length; i++) if (types[i].hasError()) return true;
      return false;
    }
    @Override public boolean hasUnknown() {
      for (int i = 0; i < types.length; i++) if (types[i].hasUnknown()) return true;
      return false;
    }
    @Override public int hashCode() {
      return Objects.hash(Tup.class, Arrays.hashCode(mutas), Arrays.hashCode(types));
    }
    @Override public boolean equals(final Object o) {
      return this == o || (o instanceof final Tup t && Arrays.equals(mutas, t.mutas) && Arrays.equals(types, t.types));
    }
    @Override public String toString() {
      return toString(false);
    }
    @Override public String toString(final boolean pretty) {
      final StringBuilder sb = new StringBuilder(); appendTo(sb, pretty); return sb.toString();
    }
    @Override public void appendTo(final StringBuilder sb) {
      appendTo(sb, false);
    }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) {
      Type.appendTo(this, sb, pretty);
    }
    @Override public boolean isAssignableTo(final Type target) { return isAssignableTo(target, null); }
    @Override public boolean isAssignableTo(final Type target, final Cycles cycles) {
      return switch (dealias(target)) {
        case final Arr tgt -> {
          if (tgt.size != Arr.UNKNOWN_SIZE && size != tgt.size) yield false;
          for (int i = 0; i < size; i++) if (tgt.muta && !mutas[i]) yield false;
          for (int i = 0; i < size; i++) if (!types[i].isAssignableTo(tgt.type, cycles)) yield false;
          yield true;
        }
        case final Tup tgt -> {
          if (size != tgt.size) yield false;
          for (int i = 0; i < size; i++) if (tgt.mutas[i] && !mutas[i]) yield false;
          for (int i = 0; i < size; i++) if (!types[i].isAssignableTo(tgt.types[i], cycles)) yield false;
          yield true;
        }
        case final Rec rec -> {
          if (size != rec.size) yield false;
          for (int i = 0; i < size; i++) if (rec.mutas[i] && !mutas[i]) yield false;
          for (int i = 0; i < size; i++) if (!types[i].isAssignableTo(rec.types[i], cycles)) yield false;
          yield true;
        }
        case null, default -> false;
      };
    }
  }

  public static final class Rec implements Type {
    public final boolean[] mutas;
    public final String[]  names;
    public final Type[]    types;
    public final int       size;
    Rec() {
      this.mutas = new boolean[0];
      this.names = new String[0];
      this.types = new Type[0];
      this.size = 0;
    }
    Rec(final boolean[] mutas, final String[] names, final Type[] types) {
      assert mutas != null;
      assert names != null;
      assert names.length == mutas.length;
      assert Arrays.stream(names).allMatch(Objects::nonNull);
      assert Arrays.stream(names).allMatch(Names::isLowerSnakeCase);
      assert types != null;
      assert types.length == mutas.length;
      assert Arrays.stream(types).allMatch(Objects::nonNull);
      this.mutas = mutas;
      this.names = names;
      this.types = types;
      this.size = types.length;
    }
    @Override public Kind kind() {
      return Kind.REC;
    }
    @Override public boolean isUnit() {
      return mutas.length == 0;
    }
    @Override public boolean hasError() {
      for (int i = 0; i < types.length; i++) if (types[i].hasError()) return true;
      return false;
    }
    @Override public boolean hasUnknown() {
      for (int i = 0; i < types.length; i++) if (types[i].hasUnknown()) return true;
      return false;
    }
    @Override public int hashCode() {
      return Objects.hash(Rec.class, Arrays.hashCode(mutas), Arrays.hashCode(names), Arrays.hashCode(types));
    }
    @Override public boolean equals(final Object o) {
      return this == o || (o instanceof final Rec t && Arrays.equals(mutas, t.mutas) && Arrays.equals(names, t.names) && Arrays.equals(types, t.types));
    }
    @Override public String toString() {
      return toString(false);
    }
    @Override public String toString(final boolean pretty) {
      final StringBuilder sb = new StringBuilder(); appendTo(sb, pretty); return sb.toString();
    }
    @Override public void appendTo(final StringBuilder sb) {
      appendTo(sb, false);
    }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) {
      Type.appendTo(this, sb, pretty);
    }
    @Override public boolean isAssignableTo(final Type target) { return isAssignableTo(target, null); }
    @Override public boolean isAssignableTo(final Type target, final Cycles cycles) {
      return switch (dealias(target)) {
        case final Arr tgt -> {
          if (tgt.size != Arr.UNKNOWN_SIZE && size != tgt.size) yield false;
          for (int i = 0; i < size; i++) if (tgt.muta && !mutas[i]) yield false;
          for (int i = 0; i < size; i++) if (!types[i].isAssignableTo(tgt.type, cycles)) yield false;
          yield true;
        }
        case final Tup tgt -> {
          if (size != tgt.size) yield false;
          for (int i = 0; i < size; i++) if (tgt.mutas[i] && !mutas[i]) yield false;
          for (int i = 0; i < size; i++) if (!types[i].isAssignableTo(tgt.types[i], cycles)) yield false;
          yield true;
        }
        case final Rec tgt -> {
          if (size != tgt.size) yield false;
          // Try to match by keys...
          byKey: do {
            final int[] map = createKeyIndexMap(names, tgt.names);
            if (map == null) break byKey;
            for (int i = 0; i < size; i++) if (tgt.mutas[map[i]] && !mutas[i]) break byKey;
            for (int i = 0; i < size; i++) if (!types[i].isAssignableTo(tgt.types[map[i]], cycles)) break byKey;
            yield true;
          } while (false);
          // Try to match by order...
          byIdx: do {
            for (int i = 0; i < size; i++) if (tgt.mutas[i] && !mutas[i]) break byIdx;
            for (int i = 0; i < size; i++) if (!types[i].isAssignableTo(tgt.types[i], cycles)) break byIdx;
            yield true;
          } while (false);
          yield false;
        }
        case null, default -> false;
      };
    }
  }

  public static final class Fun implements Type {
    public final Type pType;
    public final Type rType;
    Fun(final Type pType, final Type rType) {
      assert pType != null;
      assert pType instanceof Err || pType instanceof Unk || pType instanceof Rec || pType instanceof Tup;
      assert rType != null;
      this.pType = pType;
      this.rType = rType;
    }
    @Override public boolean isUnit() {
      return false;
    }
    @Override public Kind kind() {
      return Kind.FUN;
    }
    @Override public boolean hasError() {
      return pType.hasError() || rType.hasError();
    }
    @Override public boolean hasUnknown() {
      return pType.hasUnknown() || rType.hasUnknown();
    }
    @Override public int hashCode() {
      return Objects.hash(Rec.class, pType, rType);
    }
    @Override public boolean equals(final Object o) {
      return this == o || (o instanceof final Fun t && pType == t.pType && rType == t.rType);
    }
    @Override public String toString() {
      return toString(false);
    }
    @Override public String toString(final boolean pretty) {
      final StringBuilder sb = new StringBuilder(); appendTo(sb, pretty); return sb.toString();
    }
    @Override public void appendTo(final StringBuilder sb) {
      appendTo(sb, false);
    }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) {
      Type.appendTo(this, sb, pretty);
    }
    @Override public boolean isAssignableTo(final Type target) { return isAssignableTo(target, null); }
    @Override public boolean isAssignableTo(final Type target, final Cycles cycles) {
      return switch (dealias(target)) {
        case final Fun tgt -> pType.isAssignableTo(tgt.pType, cycles) && rType.isAssignableTo(tgt.rType, cycles);
        default -> false;
      };
    }
  }

  public static final class Nom implements Type {
    public final String name;
    public Type type;
    Nom(final String name, final Type type) {
      assert name != null;
      assert Names.isUpperCamelCase(name);
      assert type != null;
      this.name = name;
      this.type = type;
    }
    @Override public Kind kind() {
      return Kind.NOM;
    }
    @Override public boolean isUnit() {
      return type.isUnit();
    }
    @Override public boolean hasError() {
      return type.hasError();
    }
    @Override public boolean hasUnknown() {
      return type.hasUnknown();
    }
    @Override public int hashCode() {
      return Objects.hash(Nom.class, name);
    }
    @Override public boolean equals(final Object o) {
      return this == o || (o instanceof final Nom t && name.equals(t.name));
    }
    @Override public String toString() {
      return toString(false);
    }
    @Override public String toString(final boolean pretty) {
      final StringBuilder sb = new StringBuilder(); appendTo(sb, pretty); return sb.toString();
    }
    @Override public void appendTo(final StringBuilder sb) {
      appendTo(sb, false);
    }
    @Override public void appendTo(final StringBuilder sb, final boolean pretty) {
      Type.appendTo(this, sb, pretty);
    }
    @Override public boolean isAssignableTo(final Type target) { return isAssignableTo(target, null); }
    @Override public boolean isAssignableTo(final Type target, final Cycles _cycles) {
      final Type src = dealias(this);
      final Type tgt = dealias(target);
      if (src == tgt) return true;
      if (src == null || tgt == null) return false;
      final Cycles cycles = _cycles == null ? new Cycles() : _cycles;
      if (cycles.isCycling(this, target)) return true;
      return src.isAssignableTo(tgt, cycles);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Assignability Helpers
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Returns the dealiased type, or null if a cycle is detected.
  private static Type dealias(Type type) {
    // Most likey, the type is not a type alias...
    if (type instanceof final Nom nom0) {
      type = nom0.type;
    } else {
      return type;
    }
    // No problem, surely the aliased type is not also an alias...
    if (type instanceof final Nom nom1) {
      if (nom1 == nom0) return null;
      type = nom1.type;
    } else {
      return type;
    }
    // Ok... two aliased types in a row... lets make an array cache for checking for cycles...
    final Nom[] nomArr = new Nom[32];
    nomArr[0] = nom0;
    nomArr[1] = nom1;
    for (int i = 2; i < nomArr.length; i++) {
      if (type instanceof final Nom nom) {
        for (int j = 0; j < i; j++) if (nom == nomArr[j]) return null;
        nomArr[i] = nom;
        type = nom.type;
        continue;
      }
      return type;
    }
    // Wow... 32 noms in a row? Swap out the array cache for a hash set...
    final HashSet<Nom> nomSet = new HashSet<>(64, 0.5f);
    nomSet.addAll(Arrays.asList(nomArr));
    while (type instanceof final Nom nom) {
      if (!nomSet.add(nom)) return null;
      type = nom.type;
      continue;
    }
    return type;
  }

  // Returns a map from source key index to target key index, or null if the keys do not match.
  private static int[] createKeyIndexMap(final String[] src, final String[] tgt) {
    final int size = src.length;
    if (size != tgt.length) return null;
    final int[] map = new int[size];
    if (size < 32) {
      loop: for (int i = 0; i < size; i++) {
        final String name = src[i];
        for (int j = 0; j < size; j++) if (name.equals(tgt[j])) { map[i] = j; continue loop; }
        return null;
      }
    } else {
      final Map<String, Integer> tgtKeyIdx = new HashMap<>(size, 0.5f);
      for (int i = 0; i < size; i++) tgtKeyIdx.put(tgt[i], i);
      for (int i = 0; i < size; i++) {
        final String name = src[i];
        final Integer j = tgtKeyIdx.get(name);
        if (j == null) return null;
        map[i] = j;
      }
    }
    return map;
  }

  public static final class Cycles {
    private static final record TypePair(Type src, Type tgt) {}
    private final Set<TypePair> cache = new HashSet<>();
    public boolean isCycling(final Type src, final Type tgt) {
      return !cache.add(new TypePair(src, tgt));
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // String Helpers
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private static void appendTo(
    final Type type,
    final StringBuilder sb,
    final boolean pretty
  ) {
    final Set<Object> seen = new HashSet<>();
    final int indent = pretty ? 0 : -1;
    appendTo(type, sb, seen, indent);
  }

  private static void appendTo(
    final Type type,
    final StringBuilder sb,
    final Set<Object> seen,
    final int indent
  ) {
    switch (type) {
      case final Err t -> sb.append("err");
      case final Unk t -> sb.append("unk");
      case final Bit t -> sb.append("bit");
      case final U08 t -> sb.append("u08");
      case final U16 t -> sb.append("u16");
      case final U32 t -> sb.append("u32");
      case final U64 t -> sb.append("u64");
      case final I08 t -> sb.append("i08");
      case final I16 t -> sb.append("i16");
      case final I32 t -> sb.append("i32");
      case final I64 t -> sb.append("i64");
      case final F32 t -> sb.append("f32");
      case final F64 t -> sb.append("f64");
      case final Arr t -> {
        final int nextIndent = indent >= 0 ? indent + 1 : -1;
        // [let <type>; <length>]
        sb.append('[');
        sb.append(t.muta ? "mut " : "let ");
        appendTo(t.type, sb, seen, nextIndent);
        if (t.size != Arr.UNKNOWN_SIZE) sb.append("; ").append(t.size);
        sb.append(']');
      }
      case final Tup t -> {
        final boolean[] mutas = t.mutas;
        final Type[]    types = t.types;
        if (indent >= 0) {
          // (\n
          // <indent+1>let <type>,\n
          // <indent+1>mut <type>,\n
          // <indent>)
          final int nextIndent = indent + 1;
          sb.append('(');
          for (int i = 0; i < types.length; i++) {
            appendIndent(sb, nextIndent);
            sb.append(mutas[i] ? "mut " : "let ");
            appendTo(types[i], sb, seen, nextIndent);
            sb.append(',');
          }
          appendIndent(sb, indent);
          sb.append(')');
        } else {
          // (let <type>, mut <type>, ...)
          sb.append('(');
          for (int i = 0; i < types.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(mutas[i] ? "mut " : "let ");
            appendTo(types[i], sb, seen, -1);
          }
          sb.append(')');
        }
      }
      case final Rec t -> {
        final boolean[] mutas = t.mutas;
        final String[]  names = t.names;
        final Type[]    types = t.types;
        if (indent >= 0) {
          // (\n
          // <indent+1>let <name>: <type>,\n
          // <indent+1>mut <name>: <type>,\n
          // <indent>)
          final int nextIndent = indent + 1;
          sb.append('(');
          for (int i = 0; i < types.length; i++) {
            appendIndent(sb, nextIndent);
            sb.append(mutas[i] ? "mut " : "let ");
            sb.append(names[i]).append(": ");
            appendTo(types[i], sb, seen, nextIndent);
            sb.append(',');
          }
          appendIndent(sb, indent);
          sb.append(')');
        } else {
          // (let <name>: <type>, mut <name>: <type>, ...)
          sb.append('(');
          for (int i = 0; i < types.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(mutas[i] ? "mut " : "let ");
            sb.append(names[i]).append(": ");
            appendTo(types[i], sb, seen, -1);
          }
          sb.append(')');
        }
      }
      case final Fun t -> {
        // <paramsType>: <returnType>
        final int nextIndent = indent >= 0 ? indent + 1 : -1;
        appendTo(t.pType, sb, seen, nextIndent);
        sb.append(": ");
        appendTo(t.rType, sb, seen, nextIndent);
      }
      case final Nom t -> {
        // \<<name>\><type>
        sb.append('<').append(t.name).append('>');
        if (seen.add(t.type)) appendTo(t.type, sb, seen, indent);
      }
    }
  }

  private static void appendIndent(final StringBuilder sb, final int indent) {
    sb.ensureCapacity(sb.length() + indent + 1);
    sb.append('\n');
    for (int i = 0; i < indent; i++) sb.append(' ');
  }
}
