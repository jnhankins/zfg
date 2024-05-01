file://<WORKSPACE>/src/main/java/zfg/types.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/java/zfg/types.java
text:
```scala
package zfg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class types {
  private types() {}

  public static enum Kind {
    // Virtual Types
    UNK, ERR,
    // Primitive Data Types
    BIT, U08, U16, U32, U64, I08, I16, I32, I64, F32, F64,
    // Composite Data Types
    ARR, TUP, REC,
    // Function Type
    FUN,
    // Named Type
    NOM;
  }

  public static interface Type {
    public Kind kind();
    public String toString();
    public String toTypeString();
    public StringBuilder toTypeString(final StringBuilder buf);
    public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen);
  }

  public static final class UnkType implements Type {
    private UnkType() {}
    @Override public Kind kind() { return Kind.UNK; }
    @Override public String toString() { return "Unk"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class ErrType implements Type {
    private ErrType() {}
    @Override public Kind kind() { return Kind.ERR; }
    @Override public String toString() { return "Err"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class BitType implements Type {
    private BitType() {}
    @Override public Kind kind() { return Kind.BIT; }
    @Override public String toString() { return "Bit"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class U08Type implements Type {
    private U08Type() {}
    @Override public Kind kind() { return Kind.U08; }
    @Override public String toString() { return "U08"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class U16Type implements Type {
    private U16Type() {}
    @Override public Kind kind() { return Kind.U16; }
    @Override public String toString() { return "U16"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class U32Type implements Type {
    private U32Type() {}
    @Override public Kind kind() { return Kind.U32; }
    @Override public String toString() { return "U32"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class U64Type implements Type {
    private U64Type() {}
    @Override public Kind kind() { return Kind.U64; }
    @Override public String toString() { return "U64"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class I08Type implements Type {
    private I08Type() {}
    @Override public Kind kind() { return Kind.I08; }
    @Override public String toString() { return "I08"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class I16Type implements Type {
    private I16Type() {}
    @Override public Kind kind() { return Kind.I16; }
    @Override public String toString() { return "I16"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class I32Type implements Type {
    private I32Type() {}
    @Override public Kind kind() { return Kind.I32; }
    @Override public String toString() { return "I32"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class I64Type implements Type {
    private I64Type() {}
    @Override public Kind kind() { return Kind.I64; }
    @Override public String toString() { return "I64"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class F32Type implements Type {
    private F32Type() {}
    @Override public Kind kind() { return Kind.F32; }
    @Override public String toString() { return "F32"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class F64Type implements Type {
    private F64Type() {}
    @Override public Kind kind() { return Kind.F64; }
    @Override public String toString() { return "F64"; }
    @Override public String toTypeString() { return toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return buf.append(this); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) { return buf.append(this); }
  }
  public static final class ArrType implements Type {
    public static final int UNKNOWN_LENGTH = -1;
    public final Type elementType;
    public final int length;

    private ArrType(final Type elementType) {
      assert elementType != null;
      assert elementType != Err;
      this.elementType = elementType;
      this.length = UNKNOWN_LENGTH;
    }
    private ArrType(final Type elementType, final int length) {
      assert elementType != null;
      assert elementType != Err;
      assert length >= 0;
      this.elementType = elementType;
      this.length = length;
    }

    @Override public Kind kind() { return Kind.ARR; }
    @Override public String toString() { return toTypeString(); }
    @Override public String toTypeString() { final StringBuilder buf = new StringBuilder(); toTypeString(buf); return buf.toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { return toTypeString(buf, new HashSet<>()); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) {
      buf.append("Arr(");
      elementType.toTypeString(buf, seen);
      buf.append("Arr(");
      elementType.toTypeString(buf, seen);
      if (length != UNKNOWN_LENGTH) {
        buf.append("; ");
        buf.append(length);
      }
      ret buf.append(")");
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (obj instanceof ArrType that &&
        this.elementType.equals(that.elementType) &&
        this.length == that.length
      ));
    }
    @Override public int hashCode() {
      return Objects.hash(
        elementType,
        length
      );
    }
  }
  public static final class TupType implements Type {
    public final boolean[] muts;
    public final Type[]    types;
    private TupType() {
      this.muts = new boolean[0];
      this.types = new Type[0];
    }
    private TupType(final boolean[] muts, final Type[] types) {
      assert muts != null;
      assert muts.length >= 0;
      assert types != null;
      assert types.length == muts.length;
      assert Arrays.stream(types).allMatch(Objects::nonNull);
      assert Arrays.stream(types).allMatch(t -> t != Err);
      this.muts = muts;
      this.types = types;
    }
    @Override public Kind kind() { return Kind.TUP; }
    @Override public String toString() { return toTypeString(); }
    @Override public String toTypeString() { final StringBuilder buf = new StringBuilder(); toTypeString(buf); return buf.toString(); }
    @Override public void toTypeString(final StringBuilder buf) { toTypeString(buf, new HashSet<>()); }
    @Override public void toTypeString(final StringBuilder buf, final Set<Type> seen) {
      buf.append("Tup(");
      for (int i = 0; i < types.length; i++) {
        if (i > 0) buf.append(", ");
        buf.append(muts[i] ? "mut " : "let ");
        types[i].toTypeString(buf, seen);
      }
      buf.append(")");
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (obj instanceof TupType that &&
        Arrays.equals(this.muts, that.muts) &&
        Arrays.equals(this.types, that.types)
      ));
    }
    @Override public int hashCode() {
      return Objects.hash(
        Arrays.hashCode(muts),
        Arrays.hashCode(types)
      );
    }
  }
  public static final class RecType implements Type {
    public final boolean[] muts;
    public final String[]  names;
    public final Type[]    types;
    private RecType() {
      this.muts = new boolean[0];
      this.names = new String[0];
      this.types = new Type[0];
    }
    private RecType(final boolean[] muts, final String[] names, final Type[] types) {
      assert muts != null;
      assert muts.length >= 0;
      assert names != null;
      assert names.length == muts.length;
      assert Arrays.stream(names).allMatch(Objects::nonNull);
      assert Arrays.stream(names).allMatch(zfg.names::isLowerSnakeCase);
      assert types != null;
      assert types.length == muts.length;
      assert Arrays.stream(types).allMatch(Objects::nonNull);
      assert Arrays.stream(types).allMatch(t -> t != Err);
      this.muts = muts;
      this.names = names;
      this.types = types;
    }
    @Override public Kind kind() { return Kind.TUP; }
    @Override public String toString() { return toTypeString(); }
    @Override public String toTypeString() { final StringBuilder buf = new StringBuilder(); toTypeString(buf); return buf.toString(); }
    @Override public void toTypeString(final StringBuilder buf) { toTypeString(buf, new HashSet<>()); }
    @Override public void toTypeString(final StringBuilder buf, final Set<Type> seen) {
      buf.append("Rec(");
      for (int i = 0; i < types.length; i++) {
        if (i > 0) buf.append(", ");
        buf.append(muts[i] ? "mut " : "let ");
        buf.append(names[i]);
        buf.append(" ");
        types[i].toTypeString(buf, seen);
      }
      buf.append(")");
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (obj instanceof RecType that &&
        Arrays.equals(this.muts, that.muts) &&
        Arrays.equals(this.names, that.names) &&
        Arrays.equals(this.types, that.types)
      ));
    }
    @Override public int hashCode() {
      return Objects.hash(
        Arrays.hashCode(muts),
        Arrays.hashCode(names),
        Arrays.hashCode(types)
      );
    }
  }
  public static final class FunType implements Type {
    public final RecType paramsType;
    public final Type    resultType;
    private FunType(final RecType paramsType, final Type resultType) {
      assert paramsType != null;
      assert resultType != null;
      assert resultType != Err;
      this.paramsType = paramsType;
      this.resultType = resultType;
    }
    @Override public Kind kind() { return Kind.FUN; }
    @Override public String toString() { return toTypeString(); }
    @Override public String toTypeString() { final StringBuilder buf = new StringBuilder(); toTypeString(buf); return buf.toString(); }
    @Override public void toTypeString(final StringBuilder buf) { toTypeString(buf, new HashSet<>()); }
    @Override public void toTypeString(final StringBuilder buf, final Set<Type> seen) {
      buf.append("Fun(");
      paramsType.toTypeString(buf, seen);
      buf.append("; ");
      resultType.toTypeString(buf, seen);
      buf.append(")");
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (obj instanceof FunType that &&
        this.paramsType.equals(that.paramsType) &&
        this.resultType.equals(that.resultType)
      ));
    }
    @Override public int hashCode() {
      return Objects.hash(
        paramsType,
        resultType
      );
    }
  }
  public static final class NomType implements Type {
    public final String fqn;
    public       Type aliasedType;
    private NomType(final String fqn) {
      assert fqn != null;
      assert names.isUpperCamelCase(fqn); // TODO fqn with path components
      this.fqn = fqn;
      this.aliasedType = Unk;
    }
    private NomType(final String fqn, final Type aliasedType) {
      assert fqn != null;
      assert names.isUpperCamelCase(fqn); // TODO fqn with path components
      assert aliasedType != null;
      assert aliasedType != Err;
      assert aliasedType != Unk;
      this.fqn = fqn;
      this.aliasedType = aliasedType;
    }
    public void bind(final Type aliasedType) {
      assert aliasedType != null;
      assert aliasedType != Err;
      assert aliasedType != Unk;
      assert this.aliasedType == Unk;
      this.aliasedType = aliasedType;
    }
    @Override public Kind kind() { return Kind.NOM; }
    @Override public String toString() { return toTypeString(); }
    @Override public String toTypeString() { return toTypeString(new StringBuilder()).toString(); }
    @Override public StringBuilder toTypeString(final StringBuilder buf) { toTypeString(buf, new HashSet<>()); }
    @Override public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen) {
      buf.append("Name(");
      buf.append(fqn);
      buf.append("; ");
      if (seen.add(this)) aliasedType.toTypeString(buf, seen);
      else                buf.append("...");
      buf.append(")");
    }
    @Override public boolean equals(final Object obj) {
      return (this == obj || (obj instanceof NomType that && this.fqn.equals(that.fqn)));
    }
    @Override public int hashCode() {
      return Objects.hash(fqn);
    }
  }

  public static final ErrType Err = new ErrType();
  public static final UnkType Unk = new UnkType();
  public static final BitType Bit = new BitType();
  public static final U08Type U08 = new U08Type();
  public static final U16Type U16 = new U16Type();
  public static final U32Type U32 = new U32Type();
  public static final U64Type U64 = new U64Type();
  public static final I08Type I08 = new I08Type();
  public static final I16Type I16 = new I16Type();
  public static final I32Type I32 = new I32Type();
  public static final I64Type I64 = new I64Type();
  public static final F32Type F32 = new F32Type();
  public static final F64Type F64 = new F64Type();
  public static final RecType Unit = new RecType();

  public static final class TypeCache {
    private final Map<Type, Type> cache = new HashMap<>();

    public TypeCache() {}

    public final Type Arr(final Type elementType) {
      final Type type = new ArrType(elementType);
      if (elementType == Unit) return Unit;
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Arr(final Type elementType, final int length) {
      final Type type = new ArrType(elementType, length);
      if (elementType == Unit || length == 0) return Unit;
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Tup(final boolean[] muts, final Type[] types) {
      final Type type = withoutUnitFields(new TupType(muts, types));
      if (type == null) return Unit;
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Rec(final boolean[] muts, final String[] names, final Type[] types) {
      final Type type = withoutUnitFields(new RecType(muts, names, types));
      if (type == null) return Unit;
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Fun(final RecType paramsType, final Type resultType) {
      final Type type = new FunType(paramsType, resultType);
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Nom(final String fqn) {
      final Type type = new NomType(fqn);
      assert !cache.containsKey(type); // Can't create a named type with the same FQN more than once
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
    public final Type Nom(final String fqn, final Type aliasedType) {
      final Type type = new NomType(fqn, aliasedType);
      assert !cache.containsKey(type); // Can't create a named type with the same FQN more than once
      return cache.computeIfAbsent(type, t -> (Type)t);
    }
  }

  private static final TupType withoutUnitFields(final TupType tup) {
    final boolean[] muts  = tup.muts;
    final Type[]    types = tup.types;
    final int len = muts.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i] == Unit) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (types[j] != Unit) {
            muts[i] = muts[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return null;
        // Otherwise, return a new tuple without the unit fields
        return new TupType(
          Arrays.copyOf(muts, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return tup;
  }

  private static final RecType withoutUnitFields(final RecType rec) {
    final boolean[] muts  = rec.muts;
    final String[]  names = rec.names;
    final Type[]    types = rec.types;
    final int len = muts.length;
    if (len == 0) return null;
    // Scan through the tup for unit fields
    for (int i = 0; i < len; i++) {
      // If a unit field is found
      if (types[i] == Unit) {
        // Shift all non-unit fields to the front of the array
        for (int j = i + 1; j < len; j++) {
          if (types[j] != Unit) {
            muts[i] = muts[j];
            names[i] = names[j];
            types[i] = types[j];
            i++;
          }
        }
        // If all fields are unit fields, return the unit type
        if (i == 0) return null;
        // Otherwise, return a new record without the unit fields
        return new RecType(
          Arrays.copyOf(muts, i),
          Arrays.copyOf(names, i),
          Arrays.copyOf(types, i)
        );
      }
    }
    return rec;
  }
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:933)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:168)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:44)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:90)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:110)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator