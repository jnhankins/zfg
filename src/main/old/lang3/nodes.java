package zfg.old.lang3;

import zfg.old.lang3.insts.Inst;
import zfg.old.lang3.types.Type;

public final class nodes {
  private nodes() {}

  public static interface Node {
    public Type type();
  }

  public static final class Const implements Node {
    public static final Const err = new Const(types.err, null);

    public final Type type;
    public final Inst value;

    public Const(final Inst value) {
      this(value.type(), value);
    }

    private Const(final Type type, final Inst value) {
      assert type != null && type != types.unk;
      assert (value != null && value.type() == type) || (value == null && type == types.err);
      this.type = type;
      this.value = value;
    }

    @Override public Type type() { return type; }
  }
}
