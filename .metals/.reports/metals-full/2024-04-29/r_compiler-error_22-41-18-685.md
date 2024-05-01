file://<WORKSPACE>/src/main/scala/types.scala
### dotty.tools.dotc.core.CyclicReference: Cyclic reference involving method typeString

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/scala/types.scala
text:
```scala


trait Type {
  def typeString: String
  def typeString()
}

class UnkType extends Type {
  def typeString = "Unk"
}

```



#### Error stacktrace:

```

```
#### Short summary: 

dotty.tools.dotc.core.CyclicReference: Cyclic reference involving method typeString