package zfg;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser.ArrayTypeContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.LiteralContext;
import zfg.antlr.ZfgParser.LiteralExpressionContext;
import zfg.antlr.ZfgParser.NamedTypeContext;
import zfg.antlr.ZfgParser.NumericLiteralContext;
import zfg.antlr.ZfgParser.PathExpressionContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.RecordTypeContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.UnaryExpressionContext;
import zfg.antlr.ZfgParserListener;
import zfg.antlr.ZfgToken;

public class Parser2 implements ZfgParserListener {

  private Parser2() {}

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
  private <T> T err(final Error err) { errors.add(err); return null; }
  private <T> T err(final ParserRuleContext ctx, final String msg) { return err(new Error(ctx, msg)); }
  @FunctionalInterface public static interface Err {
    void err(final ParserRuleContext ctx, final String msg);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static sealed interface Expr {}

  public static class Literal {
    public final ParserRuleContext ctx;
    public final Inst inst;
    public Literal(final ParserRuleContext ctx, final Inst inst) {
      this.ctx = ctx;
      this.inst = inst;
    }
  }

  public AstLiteral parseLiteralExpression(final LiteralExpressionContext ctx) {
    final String text = ctx.lit.getText();
    switch (((ZfgToken) ctx.lit).type) {
      case ZfgLexer.BitLit:
        final Inst bitLit = Literals.parseBitLit(text);
        if (bitLit == null) { err(ctx, "Invalid bit literal: \"" + text + "\""); return null; }
        return new AstLiteral(ctx, bitLit);
      case ZfgLexer.IntLit:
        final boolean hasMinusPrefix =
            ctx.parent instanceof UnaryExpressionContext parent &&
            ((ZfgToken) parent.opr).type == ZfgLexer.SUB &&
            ((ZfgToken) parent.opr).stopIndex + 1 == ((ZfgToken) ctx.start).startIndex;
        final Inst intLit = Literals.parseIntLit(text, hasMinusPrefix);
        if (intLit == null) { err(ctx, "Invalid int literal: \"" + text + "\""); }
        return new AstLiteral(ctx, intLit);
      case ZfgLexer.FltLit:
        final Inst fltLit = Literals.parseFltLit(text);
        if (fltLit == null) { err(ctx, "Invalid flt literal: \"" + text + "\""); }
        return new AstLiteral(ctx, fltLit);
      default: throw new AssertionError();
    }
  }

  public static sealed interface PathExpr {}
  public static final class PathRoot {

  }
  public static final class FieldAccess {
    public final ParserRuleContext ctx;
    public final String field;
    public FieldAccess(final ParserRuleContext ctx, final String field) {
      this.ctx = ctx;
      this.field = field;
    }
  }
  public static final class IndexAccess {
    public final ParserRuleContext ctx;
    public final Expr index;
    public IndexAccess(final ParserRuleContext ctx, final Expr index) {
      this.ctx = ctx;
      this.index = index;
      this.field
    }
  }

  public static class PathExpr {
    public final ParserRuleContext ctx;
    public final String name;
    public PathExpr(final ParserRuleContext ctx, final String name) {
      this.ctx = ctx;
      this.name = name;
    }
  }

  public AstLiteral parseLiteral(final PathExpressionContext ctx) {
    return switch (ctx.children[0]) {
      case LiteralExpressionContext alt -> parseLiteralExpression(alt);
      case NumericLiteralContext alt -> parseNumericLiteral(alt);
      default -> throw new AssertionError();
    };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Type parseType(final TypeContext ctx) {
    return switch (ctx.children[0]) {
      case PrimitiveTypeContext alt -> parsePrimitiveType(alt);
      case FunctionTypeContext alt -> parseFunctionType(alt);
      case RecordTypeContext alt -> parseRecordType(alt);
      case TupleTypeContext alt -> parseTupleType(alt);
      case ArrayTypeContext alt -> parseArrayType(alt);
      case NamedTypeContext alt -> parseNamedType(alt);
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
    if (paramsType == null) return null;
    if (returnType == null) return null;
    return Types.Fun(paramsType, returnType);
  }

  public Type parseRecordType(final RecordTypeContext ctx) {
    final int length = ctx.types.size();
    final boolean muts[] = new boolean[length];
    final String names[] = new String[length];
    final Type[] types = new Type[length];
    for (int i = 0; i < length; i++) {
      muts[i] = ctx.muts.get(i) != null;
      names[i] = ctx.names.get(i).getText();
      types[i] = parseType(ctx.types.get(i));
    }
    if (containsDuplicates(names)) return err(ctx, "Duplicate field names in record type");
    for (int i = 0; i < length; i++) if (types[i] == null) return null;
    return Types.Rec(muts, names, types);
  }

  public Type parseTupleType(final TupleTypeContext ctx) {
    final int length = ctx.types.size();
    final boolean muts[] = new boolean[length];
    final Type[] types = new Type[length];
    for (int i = 0; i < length; i++) {
      muts[i] = ctx.muts.get(i) != null;
      types[i] = parseType(ctx.types.get(i));
    }
    for (int i = 0; i < length; i++) if (types[i] == null) return null;
    return Types.Tup(muts, types);
  }

  public Type parseArrayType(final ArrayTypeContext ctx) {
    final boolean mut = ctx.mut != null;
    final Type type = parseType(ctx.elem);
    if (ctx.length == null) {
      if (type == null) return null;
      return Types.Arr(mut, type);
    }
    final Inst parsed = Literals.parseIntLit(ctx.length.getText(), false);
    if (!(parsed instanceof Inst.I32)) return err(ctx, "Array length must be an i32 literal");
    final int length = ((Inst.I32) parsed).value;
    if (type == null) return null;
    return Types.Arr(mut, type, length);
  }

  public Type parseNamedType(final NamedTypeContext ctx) {
    final String symbolName = ((ZfgToken) ctx.name).text;
    final Type.Nom type = symbolTable.getType(symbolName);
    if (type == null) return err(ctx, "Unknown type: " + symbolName);
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
      final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
      for (int i = 0; i < length; i++) {
        if (!set.add(names[i])) {
          return true;
        }
      }
      return false;
    }
  }
}
