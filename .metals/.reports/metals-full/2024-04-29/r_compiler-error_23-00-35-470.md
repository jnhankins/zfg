file://<WORKSPACE>/src/main/java/zfg/type/Type.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/java/zfg/type/Type.java
text:
```scala
package zfg.type;

/**
 * Type
 * ├─Vrt     : Virtual (does not exist at runtime)
 * │ ├─Unk   : Unknown (e.g. type inferening has not completed yet)
 * │ └─Err   : Error   (e.g. type inferening or type checking failed)
 * ├─Pri     : Primitive (passed by value)
 * │ ├─Bit   : 1-bit unsigned integer, (boolean, true of false, 0 or 1)
 * │ ├─U08   : 8-bit unsigned integer
 * │ ├─U16   : 16-bit unsigned integer
 * │ ├─U32   : 32-bit unsigned integer
 * │ ├─U64   : 64-bit unsigned integer
 * │ ├─I08   : 8-bit signed integer
 * │ ├─I16   : 16-bit signed integer
 * │ ├─I32   : 32-bit signed integer
 * │ ├─I64   : 64-bit signed integer
 * │ ├─F32   : 32-bit floating-point number
 * │ └─F64   : 64-bit floating-point number
 * ├─Ref     : Reference (passed by reference)
 * │ ├─Com   : Composite
 * │ │ ├─Arr : Array
 * │ │ ├─Tup : Pointer
 * │ │ └─Rec : Record
 * │ └─Fun   : Function
 * ├─Fun   : Function
 *
 *
 */
public interface Type {

  publ

  /** Returns a string representation of this type. */
  public String toString();

  /** Appends the string representation of this type to the given StringBuilder and returns it. */
  public StringBuilder toString(final StringBuilder buf);
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