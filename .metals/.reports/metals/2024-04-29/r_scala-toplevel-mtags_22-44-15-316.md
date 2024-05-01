error id: file://<WORKSPACE>/src/main/scala/types.scala:[218..219) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/types.scala", "import scala.collection.mutable


trait Type {
  def typeString: String
  def typeString(buf: mutable.StringBuilder)
  def typeString(buf: mutable.StringBuilder, mutable.Set[Type])
}

trait Type0 extends Type {
  def 
}

class UnkType extends Type,  {
  def typeString = "Unk"
}
")
file://<WORKSPACE>/src/main/scala/types.scala
file://<WORKSPACE>/src/main/scala/types.scala:12: error: expected identifier; obtained rbrace
}
^
#### Short summary: 

expected identifier; obtained rbrace