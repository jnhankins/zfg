error id: file://<WORKSPACE>/src/main/scala/types.scala:[192..197) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/types.scala", "import scala.collection.mutable


trait Type {
  def typeString: String
  def typeString(buf: mutable.StringBuilder)
  def typeString(buf: mutable.StringBuilder, mutable.Set[Type])
}

trait 

class UnkType extends Type,  {
  def typeString = "Unk"
}
")
file://<WORKSPACE>/src/main/scala/types.scala
file://<WORKSPACE>/src/main/scala/types.scala:12: error: expected identifier; obtained class
class UnkType extends Type,  {
^
#### Short summary: 

expected identifier; obtained class