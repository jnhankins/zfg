package zfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AlgebraicExpressionContext;
import zfg.antlr.ZfgParser.ArrayConstructorContext;
import zfg.antlr.ZfgParser.ArrayTypeContext;
import zfg.antlr.ZfgParser.BinaryAssignmentContext;
import zfg.antlr.ZfgParser.BitwiseExpressionContext;
import zfg.antlr.ZfgParser.ComparisonExpressionContext;
import zfg.antlr.ZfgParser.ComparisonOperandContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallExpressionContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.LiteralExpressionContext;
import zfg.antlr.ZfgParser.LogicalExpressionContext;
import zfg.antlr.ZfgParser.LogicalOperandContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.NamedTypeContext;
import zfg.antlr.ZfgParser.ParentheticalExpressionContext;
import zfg.antlr.ZfgParser.PathExpressionContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.RecordConstructorContext;
import zfg.antlr.ZfgParser.RecordTypeContext;
import zfg.antlr.ZfgParser.TupleConstructorContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.UnambiguousExpressionContext;
import zfg.antlr.ZfgParser.UnaryAssignmentContext;
import zfg.antlr.ZfgParser.UnaryExpressionContext;
import zfg.antlr.ZfgToken;
import zfg.antlr.ZfgTokenFactory;

public class Parser {

  public static interface Result {
    public static final record Val(Ast value) implements Result {}
    public static final record Err(List<Error> errors) implements Result {}
  }

  public static Result parse(final java.nio.file.Path path) {
    try { return parse(CharStreams.fromPath(path)); }
    catch (final Exception e) { throw new RuntimeException(e); }
  }

  public static Result parse(final String source, final String sourceName) {
    return parse(CharStreams.fromString(source, sourceName));
  }

  public static Result parse(final CharStream source) {
    System.out.println("source: " + source.getSourceName());
    System.out.println(">" + source.toString().replaceAll("\\r?\\n", "\n>"));

    // Lexical Analysis
    final ZfgLexer tokenLexer = new ZfgLexer(source);
    tokenLexer.setTokenFactory(new ZfgTokenFactory());
    final CommonTokenStream tokenStream = new CommonTokenStream(tokenLexer);
    System.out.println("tokens: " + PrettyPrint.tokenStream(tokenLexer, tokenStream));

    // Syntax Analysis
    final ZfgParser syntaxParser = new ZfgParser(tokenStream);
    final ModuleContext syntaxTree = syntaxParser.module();
    System.out.println("parsed: " + syntaxTree.toStringSyntaxTree(false));
    System.out.println("tree:\n" + syntaxTree.toStringSyntaxTree(true));

    // Semantic Analysis
    final Parser parser = new Parser();
    final Ast.Module root = parser.parseModule(syntaxTree, source.getSourceName());
    final List<Error> errors = parser.errors();
    System.out.println("parsed: " + root);
    System.out.println("tree: " + root);
    System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
    return errors.isEmpty() ? new Result.Val(root) : new Result.Err(errors);
  }

  private Parser() {}

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Errors
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Parser error */
  public static final class Error {
    final ParserRuleContext ctx; // The most speicific context for where the error occurred
    final String msg; // The error message

    public Error(final ParserRuleContext ctx, final String msg) {
      this.ctx = ctx;
      this.msg = msg;
    }

    @Override public String toString() {
      final int line = ((ZfgToken) ctx.start).line;
      final int column = ((ZfgToken) ctx.start).column;
      return String.format("%d:%d: %s", line, column, msg);
    }
  }

  /** List of all reported errors */
  private final List<Error> errors = new ArrayList<>();

  /** Get the list of errors */
  public List<Error> errors() { return errors; }

  /** Report an error */
  private void err(final Error err) { errors.add(err); }
  private void err(final ParserRuleContext ctx, final String msg) { err(new Error(ctx, msg)); }
  @FunctionalInterface public static interface EmitErr {
    void emit(final ParserRuleContext ctx, final String msg);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////


  public Ast.Expr parseExpression(final ExpressionContext ctx) {
    return switch (ctx.children[0]) {
      case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      case AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      case BitwiseExpressionContext     alt -> parseBitwiseExpression(alt);
      case ComparisonExpressionContext  alt -> parseComparisonExpression(alt);
      case LogicalExpressionContext     alt -> parseLogicalExpression(alt);
      case BinaryAssignmentContext      alt -> parseBinaryAssignment(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr parseUnambiguousExpression(final UnambiguousExpressionContext ctx) {
    return switch (ctx.children[0]) {
      case UnaryAssignmentContext         alt -> parseUnaryAssignment(alt);
      case LiteralExpressionContext       alt -> parseLiteralExpression(alt);
      case PathExpressionContext          alt -> parsePathExpression(alt);
      case FunctionCallExpressionContext  alt -> parseFunctionCallExpression(alt);
      case RecordConstructorContext       alt -> parseRecordConstructor(alt);
      case TupleConstructorContext        alt -> parseTupleConstructor(alt);
      case ArrayConstructorContext        alt -> parseArrayConstructor(alt);
      case UnaryExpressionContext         alt -> parseUnaryExpression(alt);
      case ParentheticalExpressionContext alt -> parseParentheticalExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr parseLiteralExpression(final LiteralExpressionContext ctx) {
    final ZfgToken lit = (ZfgToken) ctx.lit;
    final Literals.Result<? extends Inst> parsed = switch (lit.type) {
      case ZfgLexer.BitLit -> Literals.parseBitLit(lit.text);
      case ZfgLexer.FltLit -> Literals.parseFltLit(lit.text);
      case ZfgLexer.IntLit -> {
        final boolean hasMinusPrefix =
          ctx.parent instanceof UnaryExpressionContext parent &&
          ((ZfgToken) parent.opr).type == ZfgLexer.SUB &&
          ((ZfgToken) parent.opr).stopIndex + 1 == lit.startIndex;
        yield Literals.parseIntLit(lit.text, hasMinusPrefix);
      }
      default -> throw new AssertionError();
    };
    return switch (parsed) {
      case Literals.Val<? extends Inst> val -> new Ast.Literal(ctx, val.value);
      case Literals.Err<? extends Inst> err -> {
        err(ctx, err.error);
        yield new Ast.Error(ctx);
      }
    };
  }

  public Ast.Expr parsePathExpression(final PathExpressionContext ctx) {
    if (ctx.symbol != null) {
      // Path Root
      final String symbolName = ((ZfgToken) ctx.symbol).text;
      final Symbol symbol = symbolTable.getSymbol(symbolName);
      throw new UnsupportedOperationException(); // TODO
    } else if (ctx.field != null) {
      // Field Access
      final Ast.Expr path  = parsePathExpression(ctx.path);
      final String   field = ((ZfgToken) ctx.field).text;
      throw new UnsupportedOperationException(); // TODO
    } else if (ctx.index != null) {
      // Index Access
      final Ast.Expr path  = parsePathExpression(ctx.path);
      final Ast.Expr index = parseExpression(ctx.index);
      throw new UnsupportedOperationException(); // TODO
    } else {
      throw new AssertionError();
    }
  }

  public Ast.Expr parseFunctionCallExpression(final FunctionCallExpressionContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Ast.Expr parseUnaryExpression(final UnaryExpressionContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Ast.Expr parseParentheticalExpression(final ParentheticalExpressionContext ctx) {
    return parseExpression(ctx.expr);
  }

  public Ast.Expr parseAlgebraicExpression(final AlgebraicExpressionContext ctx) {
    final Ast.AlgebraicExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
      case ZfgLexer.MUL -> Ast.AlgebraicExpr.Opr.MUL;
      case ZfgLexer.DIV -> Ast.AlgebraicExpr.Opr.DIV;
      case ZfgLexer.REM -> Ast.AlgebraicExpr.Opr.REM;
      case ZfgLexer.MOD -> Ast.AlgebraicExpr.Opr.MOD;
      case ZfgLexer.ADD -> Ast.AlgebraicExpr.Opr.ADD;
      case ZfgLexer.SUB -> Ast.AlgebraicExpr.Opr.SUB;
      default -> throw new AssertionError();
    };
    final Ast.Expr lhs = parseAlgebraicOperand(ctx.children[0]);
    final Ast.Expr rhs = parseAlgebraicOperand(ctx.children[2]);
    final Ast.Expr expr = new Ast.AlgebraicExpr(ctx, opr, lhs, rhs);
    expr.evaluateType(this::err);
    return expr;
  }

  public Ast.Expr parseAlgebraicOperand(final ParseTree ctx) {
    return switch (ctx) {
      case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      case AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr parseBitwiseExpression(final BitwiseExpressionContext ctx) {
    if (ctx.lhs != null) {
      // Shift
      final Ast.Expr lhs = parseUnambiguousExpression(ctx.lhs);
      final Ast.Expr rhs = parseUnambiguousExpression(ctx.rhs);
      final Ast.ShiftExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
        case ZfgLexer.SHL -> Ast.ShiftExpr.Opr.SHL;
        case ZfgLexer.SHR -> Ast.ShiftExpr.Opr.SHR;
        default -> throw new AssertionError();
      };
      final Ast.Expr expr = new Ast.ShiftExpr(ctx, opr, lhs, rhs);
      expr.evaluateType(this::err);
      return expr;
    } else {
      // Bitwise
      final int length = ctx.opds.size();
      final Ast.Expr[] opds = new Ast.Expr[length];
      for (int i = 0; i < length; i++) opds[i] = parseUnambiguousExpression(ctx.opds.get(i));
      final Ast.BitwiseExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
        case ZfgLexer.AND -> Ast.BitwiseExpr.Opr.AND;
        case ZfgLexer.IOR -> Ast.BitwiseExpr.Opr.IOR;
        case ZfgLexer.XOR -> Ast.BitwiseExpr.Opr.XOR;
        default -> throw new AssertionError();
      };
      final Ast.Expr expr = new Ast.BitwiseExpr(ctx, opr, opds);
      expr.evaluateType(this::err);
      return expr;
    }
  }

  public Ast.Expr parseComparisonExpression(final ComparisonExpressionContext ctx) {
    if (ctx.lhs != null) {
      // Comparison Binary
      final Ast.Expr lhs = parseComparisonOperand(ctx.lhs);
      final Ast.Expr rhs = parseComparisonOperand(ctx.rhs);
      final Ast.ThreeWayCmpExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
        case ZfgLexer.TWC -> Ast.ThreeWayCmpExpr.Opr.TWC;
        default -> throw new AssertionError();
      };
      final Ast.Expr expr = new Ast.ThreeWayCmpExpr(ctx, opr, lhs, rhs);
      expr.evaluateType(this::err);
      return expr;
    } else {
      // Comparison Chain
      final int length = ctx.opds.size();
      final Ast.Expr[] opds = new Ast.Expr[length];
      final Ast.ComparisonExpr.Opr[] oprs = new Ast.ComparisonExpr.Opr[length];
      for (int i = 0; i < length; i++) {
        opds[i] = parseComparisonOperand(ctx.opds.get(i));
        oprs[i] = switch (((ZfgToken) ctx.oprs.get(i)).type) {
          case ZfgLexer.EQL -> Ast.ComparisonExpr.Opr.EQL;
          case ZfgLexer.NEQ -> Ast.ComparisonExpr.Opr.NEQ;
          case ZfgLexer.LTN -> Ast.ComparisonExpr.Opr.LTN;
          case ZfgLexer.LEQ -> Ast.ComparisonExpr.Opr.LEQ;
          case ZfgLexer.GTN -> Ast.ComparisonExpr.Opr.GTN;
          case ZfgLexer.GEQ -> Ast.ComparisonExpr.Opr.GEQ;
          default -> throw new AssertionError();
        };
      }
      final Ast.Expr expr = new Ast.ComparisonExpr(ctx, oprs, opds);
      expr.evaluateType(this::err);
      return expr;
    }
  }

  public Ast.Expr parseComparisonOperand(final ComparisonOperandContext ctx) {
    return switch (ctx.children[0]) {
      case BitwiseExpressionContext     alt -> parseBitwiseExpression(alt);
      case AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr parseLogicalExpression(final LogicalExpressionContext ctx) {
    final int length = ctx.opds.size();
    final Ast.Expr[] opds = new Ast.Expr[length];
    for (int i = 0; i < length; i++) opds[i] = parseLogicalOperand(ctx.opds.get(i));
    final Ast.LogicalExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
      case ZfgLexer.LCJ -> Ast.LogicalExpr.Opr.LCJ;
      case ZfgLexer.LDJ -> Ast.LogicalExpr.Opr.LDJ;
      default -> throw new AssertionError();
    };
    final Ast.Expr expr = new Ast.LogicalExpr(ctx, opr, opds);
    expr.evaluateType(this::err);
    return expr;
  }

  public Ast.Expr parseLogicalOperand(final LogicalOperandContext ctx) {
    return switch (ctx.children[0]) {
      case ComparisonExpressionContext  alt -> parseComparisonExpression(alt);
      case BitwiseExpressionContext     alt -> parseBitwiseExpression(alt);
      case AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Ast.Expr parseUnaryAssignment(final UnaryAssignmentContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Ast.Expr parseBinaryAssignment(final BinaryAssignmentContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Ast.Expr parseRecordConstructor(final RecordConstructorContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Ast.Expr parseTupleConstructor(final TupleConstructorContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Ast.Expr parseArrayConstructor(final ArrayConstructorContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Type parseType(final TypeContext ctx) {
    return switch (ctx.children[0]) {
      case PrimitiveTypeContext alt -> parsePrimitiveType(alt);
      case FunctionTypeContext  alt -> parseFunctionType(alt);
      case RecordTypeContext    alt -> parseRecordType(alt);
      case TupleTypeContext     alt -> parseTupleType(alt);
      case ArrayTypeContext     alt -> parseArrayType(alt);
      case NamedTypeContext     alt -> parseNamedType(alt);
      default -> throw new AssertionError();
    };
  }

  public Type parsePrimitiveType(final PrimitiveTypeContext ctx) {
    return switch (((ZfgToken) ctx.token).type) {
      case ZfgLexer.BIT -> Types.BIT;
      case ZfgLexer.U08 -> Types.U08;
      case ZfgLexer.U16 -> Types.U16;
      case ZfgLexer.U32 -> Types.U32;
      case ZfgLexer.U64 -> Types.U64;
      case ZfgLexer.I08 -> Types.I08;
      case ZfgLexer.I16 -> Types.I16;
      case ZfgLexer.I32 -> Types.I32;
      case ZfgLexer.I64 -> Types.I64;
      case ZfgLexer.F32 -> Types.F32;
      case ZfgLexer.F64 -> Types.F64;
      default -> throw new AssertionError();
    };
  }

  public Type parseFunctionType(final FunctionTypeContext ctx) {
    final Type paramsType = parseRecordType(ctx.paramsType);
    final Type returnType = parseType(ctx.returnType);
    return Types.fun(paramsType, returnType);
  }

  public Type parseRecordType(final RecordTypeContext ctx) {
    final int       length = ctx.types.size();
    final boolean[] mutas  = new boolean[length];
    final String[]  names  = new String[length];
    final Type[]    types  = new Type[length];
    for (int i = 0; i < length; i++) {
      mutas[i] = ctx.mutas.get(i) != null;
      names[i] = ctx.names.get(i).getText();
      types[i] = parseType(ctx.types.get(i));
    }
    if (containsDuplicates(names)) err(ctx, "Duplicate field names in record type");
    return Types.rec(mutas, names, types);
  }

  public Type parseTupleType(final TupleTypeContext ctx) {
    final int       length = ctx.types.size();
    final boolean[] mutas  = new boolean[length];
    final Type[]    types  = new Type[length];
    for (int i = 0; i < length; i++) {
      mutas[i] = ctx.mutas.get(i) != null;
      types[i] = parseType(ctx.types.get(i));
    }
    return Types.tup(mutas, types);
  }

  public Type parseArrayType(final ArrayTypeContext ctx) {
    final boolean muta = ctx.muta != null;
    final Type    type = parseType(ctx.elem);
    final int     size = switch (ctx.size) {
      case null -> Type.Arr.UNKNOWN_SIZE;
      case Token token -> switch (Literals.parseIntLit(((ZfgToken) token).text, false)) {
        case Literals.Err<Inst> err -> {
          err(ctx, err.error);
          yield Type.Arr.UNKNOWN_SIZE;
        }
        case Literals.Val<Inst> val -> switch (val.value) {
          case Inst.I32 i32 -> i32.value;
          default -> {
            err(ctx, "Array length must have type i32.");
            yield Type.Arr.UNKNOWN_SIZE;
          }
        };
      };
    };
    return Types.arr(muta, type, size);
  }

  public Type parseNamedType(final NamedTypeContext ctx) {
    final String symbolName = ((ZfgToken) ctx.name).text;
    final Type.Nom type = symbolTable.getType(symbolName);
    if (type == null) {
      err(ctx, "Unknown type \'" + symbolName + '\'');
      return Types.ERR;
    }
    return type;
  }

  private static boolean containsDuplicates(final String[] names) {
    final int length = names.length;
    if (length < 32) {
      for (int i = length - 1; i >= 1; i -= 1) {
        final String name = names[i];
        for (int j = i - 1; j >= 0; j -= 1) {
          if (name.equals(names[j])) {
            return true;
          }
        }
      }
      return false;
    } else {
      final HashSet<String> set = new HashSet<>(length, 0.5f);
      for (int i = 0; i < length; i++) {
        if (!set.add(names[i])) {
          return true;
        }
      }
      return false;
    }
  }
}
