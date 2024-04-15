package zfg.ast.parser;

import zfg.antlr.ZfgParser.InfixExprContext;
import zfg.ast.Parser;
import zfg.ast.Parser.ParserException;
import zfg.ast.expr.ConstExpr;
import zfg.ast.expr.Expr;
import zfg.ast.types.Type;

public abstract class InfixExprParser<Ctx extends InfixExprContext> implements ExprParser<Ctx> {

  @Override
  public Expr parse(final Parser parser, final Ctx ctx) {
    // Post-order traversal
    Expr lhsExpr = parser.visitExpression(ctx.lhs);
    Expr rhsExpr = parser.visitExpression(ctx.rhs);
    // Input type checking
    final Type lhsType = lhsExpr.type();
    final Type rhsType = rhsExpr.type();
    if (!checkArgTypes(lhsType, rhsType)) {
      throw new ParserException(ctx, String.format(
        "Cannot apply operator \"%s\" to types: %s and %s",
        getClass().getSimpleName(), lhsType, rhsType));
    }
    // Get output type
    final Type outType = getResultType(lhsType, rhsType);
    // Implicit type promotion
    lhsExpr = promoteLhs(lhsExpr, outType);
    rhsExpr = promoteRhs(rhsExpr, outType);
    // Constant folding
    if (lhsExpr instanceof ConstExpr && rhsExpr instanceof ConstExpr) {
      final ConstExpr lhsConst = (ConstExpr) lhsExpr;
      final ConstExpr rhsConst = (ConstExpr) rhsExpr;
      return foldConstants(lhsConst, rhsConst);
    }
    // Simplify and return
    return createExpr(lhsExpr, rhsExpr);
  }

  abstract boolean checkArgTypes(final Type lhsType, final Type rhsType);
  abstract Type getResultType(final Type lhsType, final Type rhsType);
  abstract Expr promoteLhs(final Expr lhsExpr, final Type outType);
  abstract Expr promoteRhs(final Expr rhsExpr, final Type outType);
  abstract ConstExpr foldConstants(final ConstExpr lhsConst, final ConstExpr rhsConst);
  abstract Expr createExpr(final Expr lhsExpr, final Expr rhsExpr);

  // public boolean checkLhsType(final Type type) {
  //   if (!type.isPrimitive()) return false;
  //   final PriType lhsPrim = (PriType) type;
  //   return lhsPrim.isNum();
  // }
  // public boolean checkRhsType(final Type rhsType) {
  //   if (!rhsType.isPrimitive()) return false;
  //   final PriType lhsPrim = (PriType) rhsType;
  //   return rhsType.isNum();
  // }
  // public boolean checkRhsType(final Type lhsType, final Type rhsType) {
  //   return lhsType.isPrimitive() && rhsType.isPrimitive();
  // }

  // public Type returnType(final Context ctx, final Type lhsType, final Type rhsType) {
  //   throwIfNonPrimative(ctx, "add", lhsType, rhsType);
  //   final PriType lhsPrim = (PriType) lhsType;
  //   final PriType rhsPrim = (PriType) rhsType;
  //   throwIfNonNumeric(ctx, "add", lhsPrim, rhsPrim);
  //   throwIfMixedSignedness(ctx, "add", lhsPrim, rhsPrim);
  //   final PriType retType = PriType.max(lhsPrim, rhsPrim);

  // }
}
