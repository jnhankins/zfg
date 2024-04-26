package zfg;

import zfg.core.type;
import zfg.core.type.Type;

public final class type {
  private type() {}


  public static interface Type {

  }
  public static interface Inst<T extends Type> {
    public T type();
  }

  public class Bit {
    static class Type implements type.Type
  }
}
