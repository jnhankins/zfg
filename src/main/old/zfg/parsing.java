// package zfg;

// import zfg.antlr.ZfgParser.ExpressionContext;
// import zfg.antlr.ZfgParser.GroupedExprContext;
// import zfg.antlr.ZfgParser.InfixOpExprContext;
// import zfg.antlr.ZfgParser.LiteralExprContext;
// import zfg.antlr.ZfgParser.PrefixOpExprContext;
// import zfg.antlr.ZfgLexer;

// public final class parsing {
//   private parsing() {}

//   public static final class Parser {


//     ////////////////////////////////////////////////////////////////////////////////////////////////
//     // Expression
//     ////////////////////////////////////////////////////////////////////////////////////////////////

//     public nodes.Node parseExpression(final ExpressionContext ctx) {
//       return switch (ctx) {
//         case GroupedExprContext expr -> parseGroupedExpr(expr);
//         case LiteralExprContext expr -> parseLiteralExpr(expr);
//         case InfixOpExprContext expr -> parseInfixOpExpr(expr);
//         default -> throw new AssertionError();
//       };
//     }

//     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//     // GroupedExpr
//     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//     private nodes.Node parseGroupedExpr(final GroupedExprContext ctx) {
//       return parseExpression(ctx.expr);
//     }

//     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//     // LiteralExpr
//     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//     public nodes.Node parseLiteralExpr(final LiteralExprContext ctx) {
//       return switch (ctx.lit.getType()) {
//         case ZfgLexer.BitLit -> parseBitLit(ctx);
//         case ZfgLexer.IntLit -> parseIntLit(ctx);
//         case ZfgLexer.FltLit -> parseFltLit(ctx);
//         default -> throw new AssertionError();
//       };
//     }
//     private nodes.Node parseBitLit(final LiteralExprContext ctx) {
//       final String text = ctx.lit.getText();
//       final insts.Inst inst = literals.parseBitLit(text);
//       if (inst != null) return new nodes.Const(inst);
//       // TODO: Report error
//       return nodes.error;
//     }
//     private nodes.Node parseIntLit(final LiteralExprContext ctx) {
//       final String text = ctx.lit.getText();
//       final boolean hasMinusPrefix =
//         ctx.parent instanceof PrefixOpExprContext parent &&
//         parent.op.getType() == ZfgLexer.SUB &&
//         parent.op.getStopIndex() + 1 == ctx.getStart().getStartIndex();
//       final insts.Inst inst = literals.parseIntLit(text, hasMinusPrefix);
//       if (inst != null) return new nodes.Const(inst);
//       // TODO: Report error
//       return nodes.error;
//     }
//     private nodes.Node parseFltLit(final LiteralExprContext ctx) {
//       final String text = ctx.lit.getText();
//       final insts.Inst inst = literals.parseFltLit(text);
//       if (inst != null) return new nodes.Const(inst);
//       // TODO: Report error
//       return nodes.error;
//     }

//     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//     // InfixOpExpr
//     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//     private nodes.Node parseInfixOpExpr(final InfixOpExprContext ctx) {
//       // Post-order traversal
//       final nodes.Node lhs = parseExpression(ctx.lhs);
//       final nodes.Node rhs = parseExpression(ctx.rhs);
//       final types.Type lhsType = lhs.type();
//       final types.Type rhsType = rhs.type();

//       // Propogate errors
//       if (lhsType == types.Err || rhsType == types.Err) return nodes.error;

//       // Type checking
//       final types.Type outType = switch (ctx.op.getType()) {
//         // Arithmetic
//         case ZfgLexer.ADD, ZfgLexer.SUB, ZfgLexer.MUL, ZfgLexer.DIV, ZfgLexer.MOD -> {
//           if (!(lhsType instanceof PriType ))
//           if (!types.implArithmetic(lhsType) || !types.implArithmetic(rhsType)) yield types.err;
//           if (types.isAssignableTo(rhsType, lhsType)) yield lhsType;
//           if (types.isAssignableTo(lhsType, rhsType)) yield rhsType;
//           yield types.err;
//         }
//         // Bitwise
//         case ZfgLexer.AND, ZfgLexer.IOR, ZfgLexer.XOR -> {
//           if (!types.implBitwise(lhsType) || !types.implBitwise(rhsType)) yield types.err;
//           if (types.isAssignableTo(rhsType, lhsType)) yield lhsType;
//           if (types.isAssignableTo(lhsType, rhsType)) yield rhsType;
//           yield types.err;
//         }
//         // Shift
//         case ZfgLexer.SHL, ZfgLexer.SHR -> {
//           if (!types.implBitwise(lhsType)) yield types.err;
//           if (!types.isAssignableTo(rhsType)) yield types.err;
//           yield lhsType;
//         }
//         // Three-way comparison
//         case ZfgLexer.CMP -> {
//           if (types.isAssignableTo(rhsType, lhsType)) yield types.i08;
//           if (types.isAssignableTo(lhsType, rhsType)) yield types.i08;
//           yield types.err;
//         }
//         // Relational
//         case ZfgLexer.EQL, ZfgLexer.NEQ, ZfgLexer.LTN, ZfgLexer.GTN, ZfgLexer.LEQ, ZfgLexer.GEQ -> {
//           if (types.isAssignableTo(rhsType, lhsType)) yield types.bit;
//           if (types.isAssignableTo(lhsType, rhsType)) yield types.bit;
//           yield types.err;
//         }
//         // Logical
//         case ZfgLexer.LCJ, ZfgLexer.LDJ ->  {
//           if (lhsType != types.bit) yield types.err;
//           if (rhsType != types.bit) yield types.err;
//           yield types.bit;
//         }
//         // Exhaustive
//         default -> throw new AssertionError();
//       };
//     }

//   }
// }
