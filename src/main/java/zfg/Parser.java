package zfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import zfg.Ast.AlgebraicExpr;
import zfg.Ast.BitwiseExpr;
import zfg.Ast.ComparisonExpr;
import zfg.Ast.ErrorExpr;
import zfg.Ast.Expr;
import zfg.Ast.FunDecl;
import zfg.Ast.LiteralExpr;
import zfg.Ast.LogicalExpr;
import zfg.Ast.ShiftExpr;
import zfg.Ast.Stmt;
import zfg.Ast.SymbolExpr;
import zfg.Ast.ThreeWayCompExpr;
import zfg.Ast.TypeDecl;
import zfg.Ast.UnaryExpr;
import zfg.Ast.VarDecl;
import zfg.Result.Err;
import zfg.Result.Val;
import zfg.Symbol.Modifier;
import zfg.antlr.ZfgContext;
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
import zfg.antlr.ZfgParser.FunctionDeclarationContext;
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
import zfg.antlr.ZfgParser.ScopeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.TupleConstructorContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.TypeDeclarationContext;
import zfg.antlr.ZfgParser.UnambiguousExpressionContext;
import zfg.antlr.ZfgParser.UnaryAssignmentContext;
import zfg.antlr.ZfgParser.UnaryExpressionContext;
import zfg.antlr.ZfgParser.VariableDeclarationContext;
import zfg.antlr.ZfgToken;
import zfg.antlr.ZfgTokenFactory;

public class Parser {


  public static Result<Ast, List<ParserError>> parse(final java.nio.file.Path path) {
    try { return parse(CharStreams.fromPath(path)); }
    catch (final Exception e) { throw new RuntimeException(e); }
  }

  public static Result<Ast, List<ParserError>> parse(final String source, final String sourceName) {
    return parse(CharStreams.fromString(source, sourceName));
  }

  public static Result<Ast, List<ParserError>> parse(final CharStream source) {
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
    final Module root = parser.parseModule(syntaxTree, source.getSourceName());
    final List<ParserError> errors = parser.errors();
    System.out.println("parsed: " + root);
    System.out.println("tree: " + root);
    System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
    return errors.isEmpty() ? new Val<>(root) : new Err<>(errors);
  }

  private Parser() {}

  private final SymbolTable symbolTable = new SymbolTable();
  private final List<ParserError> errors = new ArrayList<>();
  private List<ParserError> errors() { return errors; }
  private void err(final ParserError err) { errors.add(err); }
  private void err(final ZfgContext ctx, final String msg) { err(new ParserError(ctx, msg)); }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Module
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Module parseModule(final ModuleContext ctx, final String fqn) {
    final Stmt[] stmts = parseScope(ctx.scope);
    return new Module(fqn, stmts);
  }

  public Stmt[] parseScope(final ScopeContext ctx) {
    symbolTable.pushScope();
    final int size = ctx.stmts.size();
    final Stmt[] stmts = new Stmt[size];
    for (int i = 0; i < size; i++) parseForwardDeclaration(ctx.stmts.get(i));
    for (int i = 0; i < size; i++) stmts[i] = parseStatement(ctx.stmts.get(i));
    symbolTable.popScope();
    return stmts;
  }

  private void parseForwardDeclaration(final StatementContext ctx) {
    switch (ctx.children[0]) {
      case final TypeDeclarationContext alt -> {
        final Modifier mod = parseModififer(alt.mod);
        final String name = alt.name.getText();
        final TypeDecl decl = new TypeDecl(ctx, mod, name);
        symbolTable.declare(decl, this::err);
      }
      case final FunctionDeclarationContext alt -> {
        final Modifier mod = parseModififer(alt.mod);
        final String name = alt.name.getText();
        final FunDecl decl = new FunDecl(ctx, mod, name);
        symbolTable.declare(decl, this::err);
      }
      case final VariableDeclarationContext alt -> {
        final Modifier mod = parseModififer(alt.mod);
        final String name = alt.name.getText();
        final VarDecl decl = new VarDecl(ctx, mod, name);
        symbolTable.declare(decl, this::err);
      }
      default -> {}
    }
  }

  private Modifier parseModififer(final Token mod) {
    return switch (((ZfgToken) mod).type) {
      case ZfgLexer.PUB -> Modifier.PUB;
      case ZfgLexer.LET -> Modifier.LET;
      case ZfgLexer.MUT -> Modifier.MUT;
      default -> throw new AssertionError();
    };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Expr parseExpression(final ExpressionContext ctx) {
    return switch (ctx.children[0]) {
      case final UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      case final AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      case final BitwiseExpressionContext     alt -> parseBitwiseExpression(alt);
      case final ComparisonExpressionContext  alt -> parseComparisonExpression(alt);
      case final LogicalExpressionContext     alt -> parseLogicalExpression(alt);
      case final BinaryAssignmentContext      alt -> parseBinaryAssignment(alt);
      default -> throw new AssertionError();
    };
  }

  public Expr parseUnambiguousExpression(final UnambiguousExpressionContext ctx) {
    return switch (ctx.children[0]) {
      case final UnaryAssignmentContext         alt -> parseUnaryAssignment(alt);
      case final LiteralExpressionContext       alt -> parseLiteralExpression(alt);
      case final PathExpressionContext          alt -> parsePathExpression(alt);
      case final FunctionCallExpressionContext  alt -> parseFunctionCallExpression(alt);
      case final RecordConstructorContext       alt -> parseRecordConstructor(alt);
      case final TupleConstructorContext        alt -> parseTupleConstructor(alt);
      case final ArrayConstructorContext        alt -> parseArrayConstructor(alt);
      case final UnaryExpressionContext         alt -> parseUnaryExpression(alt);
      case final ParentheticalExpressionContext alt -> parseParentheticalExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Expr parseLiteralExpression(final LiteralExpressionContext ctx) {
    final ZfgToken lit = (ZfgToken) ctx.lit;
    final Result<? extends Inst, String> parsed = switch (lit.type) {
      case ZfgLexer.BitLit -> ParseLiteral.parseBitLit(lit.text);
      case ZfgLexer.FltLit -> ParseLiteral.parseFltLit(lit.text);
      case ZfgLexer.IntLit -> {
        final boolean hasMinusPrefix =
          ctx.parent instanceof UnaryExpressionContext parent &&
          ((ZfgToken) parent.opr).type == ZfgLexer.SUB &&
          ((ZfgToken) parent.opr).stopIndex + 1 == lit.startIndex;
        yield ParseLiteral.parseIntLit(lit.text, hasMinusPrefix);
      }
      default -> throw new AssertionError();
    };
    return switch (parsed) {
      case final Val<? extends Inst, String> val -> new LiteralExpr(ctx, val.val);
      case final Err<? extends Inst, String> err -> { err(ctx, err.err); yield new ErrorExpr(ctx); }
    };
  }

  public Expr parsePathExpression(final PathExpressionContext ctx) {
    if (ctx.symbol != null) {
      // Path Root
      final String symbolName = ((ZfgToken) ctx.symbol).text;
      switch (symbolTable.getSymbol(symbolName)) {
        case null -> {
          err(ctx, "\\'" + symbolName + "\' could not be resolved to a variable.");
          return new ErrorExpr(ctx);
        }
        case final TypeDecl decl -> {
          err(ctx, "\\'" + symbolName + "\' is a type, and type expressions are not permitted.");
          return new ErrorExpr(ctx);
        }
        case final FunDecl decl -> {
          final Expr expr = new SymbolExpr(ctx, decl);
          expr.evaluateType(this::err);
          return expr;
        }
        case final VarDecl decl -> {
          final Expr expr = new SymbolExpr(ctx, decl);
          expr.evaluateType(this::err);
          return expr;
        }
      }
    } else if (ctx.field != null) {
      // Field Access
      final Expr path = parsePathExpression(ctx.path);
      final String field = ((ZfgToken) ctx.field).text;
      throw new UnsupportedOperationException(); // TODO
    } else if (ctx.index != null) {
      // Index Access
      final Expr path  = parsePathExpression(ctx.path);
      final Expr index = parseExpression(ctx.index);
      throw new UnsupportedOperationException(); // TODO
    } else {
      throw new AssertionError();
    }
  }

  public Expr parseFunctionCallExpression(final FunctionCallExpressionContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Expr parseUnaryExpression(final UnaryExpressionContext ctx) {
    final UnaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
      case ZfgLexer.ADD -> UnaryExpr.Opr.POS;
      case ZfgLexer.SUB -> UnaryExpr.Opr.NEG;
      case ZfgLexer.NOT -> UnaryExpr.Opr.NOT;
      case ZfgLexer.LNT -> UnaryExpr.Opr.LNT;
      default -> throw new AssertionError();
    };
    final Expr rhs = parseUnambiguousExpression(ctx.rhs);
    final Expr expr = new UnaryExpr(ctx, opr, rhs);
    expr.evaluateType(this::err);
    return expr;
  }

  public Expr parseParentheticalExpression(final ParentheticalExpressionContext ctx) {
    return parseExpression(ctx.expr);
  }

  public Expr parseAlgebraicExpression(final AlgebraicExpressionContext ctx) {
    final AlgebraicExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
      case ZfgLexer.MUL -> AlgebraicExpr.Opr.MUL;
      case ZfgLexer.DIV -> AlgebraicExpr.Opr.DIV;
      case ZfgLexer.REM -> AlgebraicExpr.Opr.REM;
      case ZfgLexer.MOD -> AlgebraicExpr.Opr.MOD;
      case ZfgLexer.ADD -> AlgebraicExpr.Opr.ADD;
      case ZfgLexer.SUB -> AlgebraicExpr.Opr.SUB;
      default -> throw new AssertionError();
    };
    final Expr lhs = parseAlgebraicOperand(ctx.children[0]);
    final Expr rhs = parseAlgebraicOperand(ctx.children[2]);
    final Expr expr = new AlgebraicExpr(ctx, opr, lhs, rhs);
    expr.evaluateType(this::err);
    return expr;
  }

  public Expr parseAlgebraicOperand(final ParseTree ctx) {
    return switch (ctx) {
      case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      case AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Expr parseBitwiseExpression(final BitwiseExpressionContext ctx) {
    if (ctx.lhs != null) {
      // Shift
      final Expr lhs = parseUnambiguousExpression(ctx.lhs);
      final Expr rhs = parseUnambiguousExpression(ctx.rhs);
      final ShiftExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
        case ZfgLexer.SHL -> ShiftExpr.Opr.SHL;
        case ZfgLexer.SHR -> ShiftExpr.Opr.SHR;
        default -> throw new AssertionError();
      };
      final Expr expr = new ShiftExpr(ctx, opr, lhs, rhs);
      expr.evaluateType(this::err);
      return expr;
    } else {
      // Bitwise
      final int length = ctx.opds.size();
      final Expr[] opds = new Expr[length];
      for (int i = 0; i < length; i++) opds[i] = parseUnambiguousExpression(ctx.opds.get(i));
      final BitwiseExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
        case ZfgLexer.AND -> BitwiseExpr.Opr.AND;
        case ZfgLexer.IOR -> BitwiseExpr.Opr.IOR;
        case ZfgLexer.XOR -> BitwiseExpr.Opr.XOR;
        default -> throw new AssertionError();
      };
      final Expr expr = new BitwiseExpr(ctx, opr, opds);
      expr.evaluateType(this::err);
      return expr;
    }
  }

  public Expr parseComparisonExpression(final ComparisonExpressionContext ctx) {
    if (ctx.lhs != null) {
      // Comparison Binary
      final Expr lhs = parseComparisonOperand(ctx.lhs);
      final Expr rhs = parseComparisonOperand(ctx.rhs);
      final ThreeWayCompExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
        case ZfgLexer.TWC -> ThreeWayCompExpr.Opr.TWC;
        default -> throw new AssertionError();
      };
      final Expr expr = new ThreeWayCompExpr(ctx, opr, lhs, rhs);
      expr.evaluateType(this::err);
      return expr;
    } else {
      // Comparison Chain
      final int length = ctx.opds.size();
      final Expr[] opds = new Expr[length];
      final ComparisonExpr.Opr[] oprs = new ComparisonExpr.Opr[length];
      for (int i = 0; i < length; i++) {
        opds[i] = parseComparisonOperand(ctx.opds.get(i));
        oprs[i] = switch (((ZfgToken) ctx.oprs.get(i)).type) {
          case ZfgLexer.EQL -> ComparisonExpr.Opr.EQL;
          case ZfgLexer.NEQ -> ComparisonExpr.Opr.NEQ;
          case ZfgLexer.LTN -> ComparisonExpr.Opr.LTN;
          case ZfgLexer.LEQ -> ComparisonExpr.Opr.LEQ;
          case ZfgLexer.GTN -> ComparisonExpr.Opr.GTN;
          case ZfgLexer.GEQ -> ComparisonExpr.Opr.GEQ;
          default -> throw new AssertionError();
        };
      }
      final Expr expr = new ComparisonExpr(ctx, oprs, opds);
      expr.evaluateType(this::err);
      return expr;
    }
  }

  public Expr parseComparisonOperand(final ComparisonOperandContext ctx) {
    return switch (ctx.children[0]) {
      case final BitwiseExpressionContext     alt -> parseBitwiseExpression(alt);
      case final AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      case final UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Expr parseLogicalExpression(final LogicalExpressionContext ctx) {
    final int length = ctx.opds.size();
    final Expr[] opds = new Expr[length];
    for (int i = 0; i < length; i++) opds[i] = parseLogicalOperand(ctx.opds.get(i));
    final LogicalExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
      case ZfgLexer.LCJ -> LogicalExpr.Opr.LCJ;
      case ZfgLexer.LDJ -> LogicalExpr.Opr.LDJ;
      default -> throw new AssertionError();
    };
    final Expr expr = new LogicalExpr(ctx, opr, opds);
    expr.evaluateType(this::err);
    return expr;
  }

  public Expr parseLogicalOperand(final LogicalOperandContext ctx) {
    return switch (ctx.children[0]) {
      case final ComparisonExpressionContext  alt -> parseComparisonExpression(alt);
      case final BitwiseExpressionContext     alt -> parseBitwiseExpression(alt);
      case final AlgebraicExpressionContext   alt -> parseAlgebraicExpression(alt);
      case final UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
      default -> throw new AssertionError();
    };
  }

  public Expr parseUnaryAssignment(final UnaryAssignmentContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Expr parseBinaryAssignment(final BinaryAssignmentContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Expr parseRecordConstructor(final RecordConstructorContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Expr parseTupleConstructor(final TupleConstructorContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  public Expr parseArrayConstructor(final ArrayConstructorContext ctx) {
    throw new UnsupportedOperationException(); // TODO
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Type parseType(final TypeContext ctx) {
    return switch (ctx.children[0]) {
      case final PrimitiveTypeContext alt -> parsePrimitiveType(alt);
      case final FunctionTypeContext  alt -> parseFunctionType(alt);
      case final RecordTypeContext    alt -> parseRecordType(alt);
      case final TupleTypeContext     alt -> parseTupleType(alt);
      case final ArrayTypeContext     alt -> parseArrayType(alt);
      case final NamedTypeContext     alt -> parseNamedType(alt);
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
    final Type    type = parseType(ctx.typed);
    final int     size = ctx.size == null
      ? Type.Arr.UNKNOWN_SIZE
      : switch (ParseLiteral.parseIntLit(((ZfgToken) ctx.size).text, false)) {
        case final Err<Inst, String> err -> { err(ctx, err.err); yield Type.Arr.UNKNOWN_SIZE; }
        case final Val<Inst, String> val -> switch (val.val) {
          case final Inst.I32 i32 -> i32.value;
          default -> { err(ctx, "Array length must have type i32."); yield Type.Arr.UNKNOWN_SIZE; }
        };
      };
    return Types.arr(muta, type, size);
  }

  public Type parseNamedType(final NamedTypeContext ctx) {
    final String symbolName = ((ZfgToken) ctx.name).text;
    return switch (symbolTable.getSymbol(symbolName)) {
      case final TypeDecl decl -> decl.type();
      case null, default -> {
        err(ctx, "\'" + symbolName + "\' cannot be resolved to a type.");
        yield Types.ERR;
      }
    };
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
