package zfg.parse.nodes;

import zfg.core.type.Type;

public abstract class Node {
  public final Type type;
  public Node(final Type type) { this.type = type; }

  @Override public final String toString() { return toSelfString(); }
  public final String toSelfString() { return toSelfString(new StringBuilder()).toString(); }
  public final String toTreeString() { return toTreeString(new StringBuilder()).toString(); }
  public abstract StringBuilder toSelfString(final StringBuilder buf);
  public abstract StringBuilder toTreeString(final StringBuilder buf);
}
