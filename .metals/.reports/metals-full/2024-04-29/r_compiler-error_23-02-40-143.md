file://<WORKSPACE>/src/main/java/zfg/type/types.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 380
uri: file://<WORKSPACE>/src/main/java/zfg/type/types.java
text:
```scala
package zfg.type;

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

  public static class Interfac@@e {
    
  }

  public static interface Type {
    public Kind kind();
    public String toString();
    public String toTypeString();
    public StringBuilder toTypeString(final StringBuilder buf);
    public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen);
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
	scala.meta.internal.pc.HoverProvider$.hover(HoverProvider.scala:34)
	scala.meta.internal.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:368)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator