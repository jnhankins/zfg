package zfg.parse.nodes;

import zfg.core.type.Type;

public final class NodeExprInfixOp extends NodeExpr {
  public static enum InfixOp {
    ADD, SUB, MUL, DIV, MOD,
    AND, IOR, XOR,
    SHL, SHR,
    TWE, EQL, NEQ, LTN, GTN, LEQ, GEQ,
  }

  public final NodeExpr lhs;
  public final NodeExpr rhs;
  public final InfixOp op;

  private NodeExprInfixOp(final Type type, final NodeExpr lhs, final NodeExpr rhs, final InfixOp op) {
    super(type);
    this.lhs = lhs;
    this.rhs = rhs;
    this.op = op;
  }

  @Override public StringBuilder toSelfString(final StringBuilder buf) {
    lhs.toSelfString(buf);
    buf.append(' ').append(op).append(' ');
    rhs.toSelfString(buf);
    return buf;
  }

  @Override public StringBuilder toTreeString(final StringBuilder buf) {
    buf.append('(');
    left.toTreeString(buf);
    buf.append(' ').append(op).append(' ');
    right.toTreeString(buf);
    return buf.append(')');
  }

}
