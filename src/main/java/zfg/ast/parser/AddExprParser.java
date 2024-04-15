package zfg.ast.parser;

import zfg.antlr.ZfgParser.InfixExprContext;
import zfg.ast.Parser;
import zfg.ast.Parser.ParserException;
import zfg.ast.expr.Expr;
import zfg.ast.types.PriType;
import zfg.ast.types.Type;

public class AddExprParser extends ExprParser<InfixExprContext> {

  @Override
  public Expr parse(Parser parser, InfixExprContext ctx) {
    // Post-order traversal
    Expr lhs = parser.visitExpression(ctx.lhs), rhs = parser.visitExpression(ctx.rhs);

    // Type check
    final Type lhsType = lhs.type(), rhsType = rhs.type();
    throwIfNonPrimative(ctx, "add", lhsType, rhsType);
    final PriType lhsPrim = (PriType) lhsType, rhsPrim = (PriType) rhsType;
    throwIfNonNumeric(ctx, "add", lhsPrim, rhsPrim);
    throwIfMixedSignedness(ctx, "add", lhsPrim, rhsPrim);
    final PriType retType = PriType.max(lhsPrim, rhsPrim);

    // Implicit type promotion
    if (lhsPrim != retType) lhs = ImplCastExpr.of(lhs, retType);
    if (rhsPrim != retType) rhs = ImplCastExpr.of(rhs, retType);

    // Constant folding
    if (lhs instanceof ConstExpr && rhs instanceof ConstExpr) {
      return new ConstExpr(ctx, ((Num) ((ConstExpr) lhs).lit).add((Num) ((ConstExpr) rhs).lit));
    }
  }

}
