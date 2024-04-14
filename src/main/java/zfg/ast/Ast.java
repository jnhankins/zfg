package zfg.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimizations:
 * 1) Constant folding and propogation
 * 2) Dead code elimination
 * 3) Common subexpression elimination
 */

public class Ast {

  static class Statments {
    private static final List<Statment> statments = new ArrayList<>();
  }

  /** Statement */
  static abstract class Statment {}

  /** Local Declaration Statement */
  static class LocVarDecStmt extends Statment {
    final String lhs;
    final Expression rhs;

    public LocVarDecStmt(final String lhs, final Expression rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }
  }

  /** Local Variable Assignment Statement */
  static class LocVarAsnStmt extends Statment {
    final String lhs;
    final Expression rhs;

    public LocVarAsnStmt(final String lhs, final Expression rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }
  }

  /** Return Statement */
  static class ReturnStmt extends Statment {
    final Expression expr;

    public ReturnStmt(Expression expr) {
      this.expr = expr;
    }
  }

  /** Expression */
  static abstract class Expression {}

  /** Path Variable Expression */
  static class PathExpr extends Expression {}

  /** Constant Expression */
  static class ConstExpr extends Expression {}

  /** Grouping Expression */
  static class GroupExpr extends Expression {}

  /** Unary Operation Expression */
  static class UnaryOpExpr<C> extends Expression {}

  /** Binary Operation Expression */
  static class BinaryOpExpr<L, R> extends Expression {}

  /** Binary Infix Operators */
  static class AssignExpr extends Expression {}
}
