package zfg.ast;

import static zfg.antlr.ZfgLexer.ADD;
import static zfg.antlr.ZfgLexer.AND;
import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.CMP;
import static zfg.antlr.ZfgLexer.DEC;
import static zfg.antlr.ZfgLexer.DIV;
import static zfg.antlr.ZfgLexer.EQL;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.GEQ;
import static zfg.antlr.ZfgLexer.GTN;
import static zfg.antlr.ZfgLexer.INC;
import static zfg.antlr.ZfgLexer.IOR;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.LCJ;
import static zfg.antlr.ZfgLexer.LDJ;
import static zfg.antlr.ZfgLexer.LEQ;
import static zfg.antlr.ZfgLexer.LNT;
import static zfg.antlr.ZfgLexer.LTN;
import static zfg.antlr.ZfgLexer.MOD;
import static zfg.antlr.ZfgLexer.MUL;
import static zfg.antlr.ZfgLexer.NEQ;
import static zfg.antlr.ZfgLexer.NOT;
import static zfg.antlr.ZfgLexer.REM;
import static zfg.antlr.ZfgLexer.SHL;
import static zfg.antlr.ZfgLexer.SHR;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgLexer.XOR;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.GroupedExprContext;
import zfg.antlr.ZfgParser.InfixOpExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.PostfixOpExprContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.VariableExprContext;
import zfg.ast.Node.Leaf;
import zfg.lang.primitive.Val;

public final class ParseExpr {
  private ParseExpr() {}

  public static final Node visitExpression(final Parser parser, final ExpressionContext ctx) {
    return switch (ctx) {
      case FunctionCallExprContext expr -> visitFunctionCallExpr(parser, expr);
      case VariableExprContext     expr -> visitVariableExpr(parser, expr);
      case LiteralExprContext      expr -> visitLiteralExpr(parser, expr);
      case GroupedExprContext      expr -> visitGroupedExpr(parser, expr);
      case PostfixOpExprContext    expr -> visitPostfixOpExpr(parser, expr);
      case PrefixOpExprContext     expr -> visitPrefixOpExpr(parser, expr);
      case InfixOpExprContext      expr -> visitInfixOpExpr(parser, expr);
      case AssignmentExprContext   expr -> visitAssignExpr(parser, expr);
      default -> throw new AssertionError();
    };
  }

  public static final Node visitLiteralExpr(final Parser parser, final LiteralExprContext ctx) {
    final String str = ctx.lit.getText();
    return switch (ctx.lit.getType()) {
      case BitLit -> switch (zfg.lang.primitive.Parser.parseBit(str)) {
        case null -> {
          parser.error(ctx, "Invalid bit literal: \"" + str + "\"");
          yield new Leaf.Error(Node.Const.class, str);
        }
        case Val val -> new Node.Const(Type.bit, val);
      };
      case IntLit -> {
        final boolean hasNegativeSignPrefix = switch (ctx.parent) {
          case PrefixOpExprContext parent ->
            parent.op.getType() == SUB &&
            parent.op.getStopIndex() + 1 == ctx.getStart().getStartIndex();
          default -> false;
        };
        yield switch (zfg.lang.primitive.Parser.parseInt(str, hasNegativeSignPrefix)) {
          case null -> {
            parser.error(ctx, "Invalid int literal: \"" + str + "\"");
            yield new Leaf.Error(Node.Const.class, str);
          }
          case Val val -> new Node.Const(Type.of(val), val);
        };
      }
      case FltLit -> switch (zfg.lang.primitive.Parser.parseFlt(str)) {
        case null -> {
          parser.error(ctx, "Invalid flt literal: \"" + str + "\"");
          yield new Leaf.Error(Node.Const.class, str);
        }
        case Val val -> new Node.Const(Type.of(val), val);
      };
      default -> throw new AssertionError();
    };
  }

  public static final Node visitVariableExpr(final Parser parser, final VariableExprContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitVariableExpr'");
  }

  public static final Node visitFunctionCallExpr(final Parser parser, final FunctionCallExprContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCallExpr'");
  }

  public static final Node visitGroupedExpr(final Parser parser, final GroupedExprContext ctx) {
    return visitExpression(parser, ctx.expression());
  }

  public static final Node visitPrefixOpExpr(final Parser parser, final PrefixOpExprContext ctx) {
    // Post-order traversal
    return switch (ctx.op.getType()) {
      case ADD -> newUnaryPrefixExpr(parser, ctx, Node.Ident::new, "arithmetic identity",           // +a: identity
        t -> t instanceof Type.Num ? t : Type.err);
      case SUB -> newUnaryPrefixExpr(parser, ctx, Node.Neg::new, "arithmetic negation",             // -a: negation
        t -> t instanceof Type.Ixx || t instanceof Type.Fxx ? t : Type.err);
      case NOT -> newUnaryPrefixExpr(parser, ctx, Node.Not::new, "bitwise complement",              // ~a: bitwise not
        t -> t instanceof Type.Ixx || t instanceof Type.Uxx ? t : Type.err);
      case LNT -> newUnaryPrefixExpr(parser, ctx, Node.Lnt::new, "logical not",                     // !a: logical not
        t -> t == Type.bit ? t : Type.err);
      case INC -> // TODO Auto-generated method stub                                                // ++a: pre-increment
        throw new UnsupportedOperationException("Unimplemented: ++a");
      case DEC -> // TODO Auto-generated method stub                                                // --a: pre-decrement
        throw new UnsupportedOperationException("Unimplemented: --a");
      default -> throw new AssertionError();
    };
  }

  public static final Node visitPostfixOpExpr(final Parser parser, final PostfixOpExprContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitPostfixOpExpr'");
    // return switch (ctx.op.getType()) {
    //   case INC -> // TODO Auto-generated method stub                                                // a++: post-increment
    //     throw new UnsupportedOperationException("Unimplemented: a--");
    //   case DEC -> // TODO Auto-generated method stub                                                // a--: post-decrement
    //     throw new UnsupportedOperationException("Unimplemented: a--");
    //   default -> throw new AssertionError();
    // };
  }

  public static final Node visitInfixOpExpr(final Parser parser, final InfixOpExprContext ctx) {
    return switch (ctx.op.getType()) {
      // Arithemetic
      case ADD -> newArithmeticInfixExpr(parser, ctx, Node.Add::new, "arithmatic addition");        // a + b : addition
      case SUB -> newArithmeticInfixExpr(parser, ctx, Node.Sub::new, "arithmatic subtraction");     // a - b : subtraction
      case MUL -> newArithmeticInfixExpr(parser, ctx, Node.Mul::new, "arithmatic multiplcation");   // a * b : multiplication
      case DIV -> newArithmeticInfixExpr(parser, ctx, Node.Div::new, "arithmatic division");        // a / b : division
      case REM -> newArithmeticInfixExpr(parser, ctx, Node.Rem::new, "arithmatic remainder");       // a % b : remainder
      case MOD -> newArithmeticInfixExpr(parser, ctx, Node.Mod::new, "arithmatic modulo");          // a %% b: modulus
      // Bitwise Logical
      case AND -> newBitwiseInfixExpr(parser, ctx, Node.And::new, "bitwise and");                   // a & b : bitwise and
      case IOR -> newBitwiseInfixExpr(parser, ctx, Node.Ior::new, "bitwise or");                    // a | b : bitwise inclusive or
      case XOR -> newBitwiseInfixExpr(parser, ctx, Node.Xor::new, "bitwise xor");                   // a ^ b : bitwise exclusive or
      // Bitwise Shift
      case SHL -> newShiftInfixExpr(parser, ctx, Node.Shl::new, "bitwise shift left");              // a << b: bitwise shift left
      case SHR -> newShiftInfixExpr(parser, ctx, Node.Shr::new, "bitwise shift right");             // a >> b: bitwise shift right
      // Relational
      case CMP -> newRelationalInfixExpr(parser, ctx, Node.Twc::new, "three-way comparison");       // a <=> b: three-way comparison
      case LTN -> newRelationalInfixExpr(parser, ctx, Node.Ltn::new, "less than");                  // a < b : less than
      case GTN -> newRelationalInfixExpr(parser, ctx, Node.Gtn::new, "greater than");               // a > b : greater than
      case LEQ -> newRelationalInfixExpr(parser, ctx, Node.Geq::new, "greater than or equal");      // a <= b: less than or equal
      case GEQ -> newRelationalInfixExpr(parser, ctx, Node.Leq::new, "less than or equal");         // a >= b: greater than or equal
      case EQL -> newRelationalInfixExpr(parser, ctx, Node.Eql::new, "equal");                      // a == b: equal
      case NEQ -> newRelationalInfixExpr(parser, ctx, Node.Neq::new, "not equal");                  // a != b: not equal
      // Logical Short-circuit
      case LCJ -> newLogicalInfixExpr(parser, ctx, Node.Lcj::new, "logical conjunction");           // a && b: short-cuirting logical conjunction
      case LDJ -> newLogicalInfixExpr(parser, ctx, Node.Ldj::new, "logical disjunction");           // a || b: short-cuirting logical disjunction
      default -> throw new AssertionError();
    };
  }

  public static final Node visitAssignExpr(final Parser parser, final AssignmentExprContext expr) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitAssignmentExpr'");
  }

  private static final Node newUnaryPrefixExpr(
    final Parser parser,
    final PrefixOpExprContext ctx,
    final UnaryNodeFactory nodeFactory,
    final String opName,
    final UnaryTypeCheck typeCheck
  ) {
    return newUnaryExpr(parser, ctx, ctx.rhs, ctx.op, typeCheck, nodeFactory, opName);
  }

  @SuppressWarnings("unused")
  private static final Node newUnaryPostfixExpr(
    final Parser parser,
    final PostfixOpExprContext ctx,
    final UnaryTypeCheck typeCheck,
    final UnaryNodeFactory nodeFactory,
    final String opName
  ) {
    return newUnaryExpr(parser, ctx, ctx.lhs, ctx.op, typeCheck, nodeFactory, opName);
  }


  private static final Node newArithmeticInfixExpr(
    final Parser parser,
    final InfixOpExprContext ctx,
    final BinaryNodeFactory nodeFactory,
    final String opName
  ) {
    return newBinaryExpr(
      parser,
      ctx,
      BinaryTypeCheck::arithmetic,
      ImplicitCast::widen,
      ImplicitCast::widen,
      nodeFactory,
      opName
    );
  }

  private static final Node newRelationalInfixExpr(
    final Parser parser,
    final InfixOpExprContext ctx,
    final BinaryNodeFactory nodeFactory,
    final String opName
  ) {
    return newBinaryExpr(
      parser,
      ctx,
      BinaryTypeCheck::relational,
      ImplicitCast::widen,
      ImplicitCast::widen,
      nodeFactory,
      opName
    );
  }

  private static final Node newBitwiseInfixExpr(
    final Parser parser,
    final InfixOpExprContext ctx,
    final BinaryNodeFactory nodeFactory,
    final String opName
  ) {
    return newBinaryExpr(
      parser,
      ctx,
      BinaryTypeCheck::bitwise,
      ImplicitCast::widen,
      ImplicitCast::widen,
      nodeFactory,
      opName
    );
  }

  private static final Node newShiftInfixExpr(
    final Parser parser,
    final InfixOpExprContext ctx,
    final BinaryNodeFactory nodeFactory,
    final String opName
  ) {
    return newBinaryExpr(
      parser,
      ctx,
      BinaryTypeCheck::shift,
      ImplicitCast::noop,
      ImplicitCast::shiftRhs,
      nodeFactory,
      opName
    );
  }

  private static final Node newLogicalInfixExpr(
    final Parser parser,
    final InfixOpExprContext ctx,
    final BinaryNodeFactory nodeFactory,
    final String opName
  ) {
    return newBinaryExpr(
      parser,
      ctx,
      BinaryTypeCheck::logical,
      ImplicitCast::noop,
      ImplicitCast::noop,
      nodeFactory,
      opName
    );
  }

  private static final Node newUnaryExpr(
    final Parser parser,
    final ParserRuleContext ctx,
    final ExpressionContext childCtx,
    final Token opToken,
    final UnaryTypeCheck typeCheck,
    final UnaryNodeFactory nodeFactory,
    final String opName
  ) {
    // Post-order traversal
    final Node childExpr = visitExpression(parser, childCtx);
    final Type childType = childExpr.type();
    // Error propagation
    if (childType == Type.err) return nodeFactory.apply(Type.err, childExpr);
    // Type checking
    final Type outType = typeCheck.apply(childType);
    if (outType == Type.err) {
      parser.error(ctx, String.format(
        "Invalid operand type for %s (%s) operator: %s",
        opName, opToken.getText(), childType
      ));
    }
    // Create node
    return nodeFactory.apply(outType, childExpr);
  }

  private static final Node newBinaryExpr(
    final Parser parser,
    final InfixOpExprContext ctx,
    final BinaryTypeCheck typeCheck,
    final ImplicitCast implicitCastingLhs,
    final ImplicitCast implicitCastingRhs,
    final BinaryNodeFactory nodeFactory,
    final String opName
  ) {
    // Post-order traversal
    final Node lhsExpr = visitExpression(parser, ctx.lhs);
    final Type lhsType = lhsExpr.type();
    final Node rhsExpr = visitExpression(parser, ctx.rhs);
    final Type rhsType = rhsExpr.type();
    // Error propagation
    if (lhsType == Type.err || rhsType == Type.err) {
      return nodeFactory.apply(Type.err, lhsExpr, rhsExpr);
    }
    // Type checking
    final Type outType = typeCheck.apply(lhsType, rhsType);
    if (outType == Type.err) {
      parser.error(ctx, String.format(
        "Invalid operand types for %s (%s) operator: %s and %s",
        opName, ctx.op.getText(), lhsType, rhsType
      ));
      return nodeFactory.apply(outType, lhsExpr, rhsExpr);
    }
    // Implicit type conversion
    final Node lhs = implicitCastingLhs.apply(lhsExpr, outType);
    final Node rhs = implicitCastingRhs.apply(rhsExpr, outType);
    // Create node
    return nodeFactory.apply(outType, lhs, rhs);
  }

  @FunctionalInterface
  private static interface UnaryNodeFactory  {
    Node apply(Type type, Node child);
  }

  @FunctionalInterface
  private static interface UnaryTypeCheck  {
    Type apply(Type child);
  }

  @FunctionalInterface
  private static interface BinaryNodeFactory {
    Node apply(Type type, Node lhs, Node rhs);
  }

  @FunctionalInterface
  private static interface BinaryTypeCheck {
    Type apply(Type lhs, Type rhs);

    static Type arithmetic(final Type lhs, final Type rhs) {
      // Both input types must be numeric (i.e. bit, ixx, uxx or fxx)
      if (!(lhs instanceof Type.Num)) return Type.err;
      if (!(rhs instanceof Type.Num)) return Type.err;
      // The input types cannot both be bit
      if (lhs == Type.bit && rhs == Type.bit) return Type.err;
      // If one input type is uxx and the other is ixx, then the ixx must be wider than the uxx
      if (lhs instanceof Type.Uxx && rhs instanceof Type.Ixx) return lhs.nbits() < rhs.nbits() ? rhs : Type.err;
      if (lhs instanceof Type.Ixx && rhs instanceof Type.Uxx) return lhs.nbits() > rhs.nbits() ? lhs : Type.err;
      // Otherwise the output type is the wider of the two input types:
      // bit < (u08, i08) < (u16, i16) < (u32, i32) < (u64, i64) < f32 < f64
      return lhs.order() >= rhs.order() ? lhs : rhs;
    }

    static Type relational(final Type lhs, final Type rhs) {
      // Both input types must be numeric (i.e. bit, ixx, uxx or fxx)
      if (!(lhs instanceof Type.Num)) return Type.err;
      if (!(rhs instanceof Type.Num)) return Type.err;
      // If one input type is uxx and the other is ixx, then the ixx must be wider than the uxx
      if (lhs instanceof Type.Uxx && rhs instanceof Type.Ixx) return lhs.nbits() < rhs.nbits() ? rhs : Type.err;
      if (lhs instanceof Type.Ixx && rhs instanceof Type.Uxx) return lhs.nbits() > rhs.nbits() ? lhs : Type.err;
      // Otherwise the output type is the wider of the two input types:
      // bit < (u08, i08) < (u16, i16) < (u32, i32) < (u64, i64) < f32 < f64
      return lhs.order() >= rhs.order() ? lhs : rhs;
    }

    static Type bitwise(final Type lhs, final Type rhs) {
      // Both input types must be signed or unsigned integers (i.e. ixx or uxx)
      if (!(lhs instanceof Type.Ixx || lhs instanceof Type.Uxx)) return Type.err;
      if (!(rhs instanceof Type.Ixx || rhs instanceof Type.Uxx)) return Type.err;
      // If one input type is uxx and the other is ixx, then the ixx must be wider than the uxx
      if (lhs instanceof Type.Uxx && rhs instanceof Type.Ixx) return lhs.nbits() < rhs.nbits() ? rhs : Type.err;
      if (lhs instanceof Type.Ixx && rhs instanceof Type.Uxx) return lhs.nbits() > rhs.nbits() ? lhs : Type.err;
      // Otherwise the output type is the wider of the two input types:
      // (u08, i08) < (u16, i16) < (u32, i32) < (u64, i64)
      return lhs.order() >= rhs.order() ? lhs : rhs;
    }

    static Type shift(final Type lhs, final Type rhs) {
      // Both input types must be signed or unsigned integers (i.e. ixx or uxx)
      // Note: The right-hand side will be truncated to log2(width(rhs)) bits (i.e. 3-6 bits)
      if (!(lhs instanceof Type.Ixx || lhs instanceof Type.Uxx)) return Type.err;
      if (!(rhs instanceof Type.Ixx || rhs instanceof Type.Uxx)) return Type.err;
      // The output type is the same as the left-hand side
      return lhs;
    }

    static Type logical(final Type lhs, final Type rhs) {
      // Both input types must be bit
      if (lhs != Type.bit) return Type.err;
      if (rhs != Type.bit) return Type.err;
      // The output type is bit
      return Type.bit;
    }
  }

  @FunctionalInterface
  private static interface ImplicitCast {
    Node apply(final Node node, final Type outType);

    static Node noop(final Node node, final Type out) {
      return node;
    }

    static Node widen(final Node node, final Type out) {
      return node.type().order() < out.order() ? new Node.ImplCast(out, node) : node;
    }

    static Node shiftRhs(final Node node, final Type ignored) {
      return new Node.ImplCast(Type.i32, node);
    }
  }
}
