// package zfg.ast.parser;

// import java.util.function.BiFunction;
// import java.util.function.Predicate;

// import org.antlr.v4.runtime.ParserRuleContext;

// import zfg.antlr.ZfgParser.InfixExprContext;
// import zfg.ast.Parser;
// import zfg.ast.Parser.ParserException;
// import zfg.ast.expr.AddExpr;
// import zfg.ast.expr.ConstExpr;
// import zfg.ast.expr.Expr;
// import zfg.ast.expr.ImplCastExpr;
// import zfg.ast.types.PriType;
// import zfg.ast.types.Type;
// import zfg.num.Lit;
// import zfg.num.Num;

// @FunctionalInterface
// public interface ExprParser<Ctx extends ParserRuleContext> {
//   public Expr parse(final Parser parser, final Ctx ctx);

//   @FunctionalInterface public static interface TriFunction<T, U, V, R> { R apply(T t, U u, V v); }

//   public static <Ctx extends InfixExprContext> ExprParser<Ctx> forInfixExpr(
//     final String name,
//     final BiFunction<Type, Type, Boolean> checkArgTypes,
//     final BiFunction<Type, Type, Type> getResultType,
//     final BiFunction<Expr, Type, Expr> promoteLhs,
//     final BiFunction<Expr, Type, Expr> promoteRhs,
//     final BiFunction<ConstExpr, ConstExpr, ConstExpr> foldConstants,
//     final TriFunction<Expr, Expr, Type, Expr> createExpr
//   ) {
//     return (final Parser parser, final Ctx ctx) -> {
//       // Post-order traversal
//       Expr lhsExpr = parser.visitExpression(ctx.lhs);
//       Expr rhsExpr = parser.visitExpression(ctx.rhs);
//       // Input type checking
//       final Type lhsType = lhsExpr.type();
//       final Type rhsType = rhsExpr.type();
//       if (!checkArgTypes.apply(lhsType, rhsType)) {
//         throw new ParserException(ctx, String.format(
//           "Cannot apply operator \"%s\" to types: %s and %s", name, lhsType, rhsType
//         ));
//       }
//       // Get output type
//       final Type outType = getResultType.apply(lhsType, rhsType);
//       // Implicit type promotion
//       lhsExpr = promoteLhs.apply(lhsExpr, outType);
//       rhsExpr = promoteRhs.apply(rhsExpr, outType);
//       // Constant folding
//       if (lhsExpr instanceof ConstExpr && rhsExpr instanceof ConstExpr) {
//         return foldConstants.apply((ConstExpr) lhsExpr, (ConstExpr) rhsExpr);
//       }
//       // Simplify and return
//       return createExpr.apply(lhsExpr, rhsExpr, outType);
//     };
//   }

//   @SuppressWarnings({ "rawtypes", "unchecked" })
//   public static final ExprParser<InfixExprContext> AddExprParser = forInfixExpr(
//     "add",
//     (final Type lhsType, final Type rhsType) -> {
//       if (!lhsType.isPrimitive() || !rhsType.isPrimitive()) return false;
//       final PriType lhsPrim = (PriType) lhsType;
//       final PriType rhsPrim = (PriType) rhsType;
//       return (
//         lhsPrim.isNum() && rhsPrim.isNum() &&
//         !(lhsPrim.isUnsignedInt() && rhsPrim.isSignedInt()) &&
//         !(lhsPrim.isSignedInt() && rhsPrim.isUnsignedInt())
//       );
//     },
//     (final Type lhsType, final Type rhsType) -> {
//       final PriType lhsPrim = (PriType) lhsType;
//       final PriType rhsPrim = (PriType) rhsType;
//       return PriType.max(lhsPrim, rhsPrim);
//     },
//     (final Expr lhsExpr, final Type outType) -> maybeImplCast(lhsExpr, outType),
//     (final Expr rhsExpr, final Type outType) -> maybeImplCast(rhsExpr, outType),
//     (final ConstExpr lhsExpr, final ConstExpr rhsExpr) -> {
//       final Num lhsVal = (Num) lhsExpr.value();
//       final Num rhsVal = (Num) rhsExpr.value();
//       return new ConstExpr(lhsExpr.type(), lhsVal.add(rhsVal));
//     },
//     (final Expr lhsExpr, final Expr rhsExpr, final Type outType) -> {
//       new AddExpr(lhsExpr, rhsExpr, outType);
//     }
//   );

//   public static Expr maybeImplCast(final Expr expr, final Type type) {
//     if (expr.type().equals(type)) {
//       return expr;
//     } else if (expr instanceof ConstExpr) {
//       final ConstExpr constExpr = (ConstExpr) expr;
//       // TODO
//       // final Lit value = switch (constExpr.value()) {
//       //   case Bit bit -> null,
//       //   case

//       // }
//       return new ConstExpr(type, constExpr.value());
//     } else if (expr instanceof ImplCastExpr) {
//       final ImplCastExpr castExpr = (ImplCastExpr) expr;
//       return new ImplCastExpr(castExpr.expr(), type);
//     }
//     // TODO: Maybe push the cast down the tree to avoid redundant casts
//     return new ImplCastExpr(expr, type);
//   }
// }
