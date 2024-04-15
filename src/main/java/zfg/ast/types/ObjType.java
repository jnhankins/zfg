package zfg.ast.types;

public class ObjType implements Type {
  public boolean isPrimitive() { return false; }
  public boolean isObject() { return true; }
}
