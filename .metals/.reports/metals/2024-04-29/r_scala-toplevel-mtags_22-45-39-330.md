error id: file://<WORKSPACE>/src/main/scala/types.scala:[56..59) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/types.scala", "import scala.collection.mutable


trait Type {
  def 
  def typeString: String
  def typeString(buf: mutable.StringBuilder)
  def typeString(buf: mutable.StringBuilder, mutable.Set[Type])
}

trait Type0 extends Type {
  def typeString(buf: mutable.StringBuilder) = buf.append(typeString)
  def typeString(buf: mutable.StringBuilder, seen: mutable.Set[Type]) = buf.append(typeString)
}

trait Type0 extends Type {
  def typeString(buf: mutable.StringBuilder) = buf.append(typeString)
  def typeString(buf: mutable.StringBuilder, seen: mutable.Set[Type]) = buf.append(typeString)
}

class UnkType extends Type,  {
  def typeString = "Unk"
}
")
file://<WORKSPACE>/src/main/scala/types.scala
file://<WORKSPACE>/src/main/scala/types.scala:6: error: expected identifier; obtained def
  def typeString: String
  ^
#### Short summary: 

expected identifier; obtained def