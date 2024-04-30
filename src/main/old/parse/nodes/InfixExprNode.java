package zfg.old.parse.nodes;

import zfg.old.core.type.Type;

public class InfixOpExprNode {
  public static enum Op {
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    AND,
    OR,
    XOR,
    SHL,
    SHR,
    EQ,
    NE,
    LT,
    GT,
    LE,
    GE,
  }

  public final ExprNode lhs;
  public final ExprNode rhs;
  public final Op op;
  public final Type type;
}
