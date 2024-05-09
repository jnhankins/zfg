file://<WORKSPACE>/src/main/java/zfg/type/types.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1291
uri: file://<WORKSPACE>/src/main/java/zfg/type/types.java
text:
```scala
package zfg.type;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import zfg.types.FunType;

public final class types {
	private types() {
	}

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

	public static class Trait {
		public final String[] names;
		public final FunType[] types;

		public Trait(final String[] names, final FunType[] types) {
			assert names != null;
			assert names.length > 0;
			assert Arrays.stream(names).allMatch(Objects::nonNull);
      assert Arrays.stream(names).allMatch(zfg.names::isLowerSnakeCase);
      assert Arrays.stream(names).distinct().count() == names.length;
      assert types != null;
      assert types.length == names.length;
      assert Arrays.stream(types).allMatch(Objects::nonNull);
			this.names = names;
			this.types = types;
		}
	}

	public static interface Type {
		public Kind kind();
		public String toString();
		public String toTypeString();
		public StringBuilder toTypeString(final StringBuilder buf);
		public StringBuilder toTypeString(final StringBuilder buf, final Set<Type> seen);
	}

  public static final c@@
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
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:46)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:147)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator