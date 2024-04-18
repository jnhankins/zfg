package zfg.ast.type;

public class ObjType implements Type {
  public boolean isPrimitive() { return false; }
  public boolean isObject() { return true; }
}
