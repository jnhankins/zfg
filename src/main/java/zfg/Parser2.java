package zfg;

import static zfg.Literals.parseIntLit;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AlgebraExpressionContext;
import zfg.antlr.ZfgParser.ArrayLiteralContext;
import zfg.antlr.ZfgParser.ArrayTypeContext;
import zfg.antlr.ZfgParser.AssignmentContext;
import zfg.antlr.ZfgParser.BitwiseExpressionContext;
import zfg.antlr.ZfgParser.BivariateAssignmentContext;
import zfg.antlr.ZfgParser.CompareExpressionContext;
import zfg.antlr.ZfgParser.DeclarationContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallContext;
import zfg.antlr.ZfgParser.FunctionDeclarationContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.LiteralContext;
import zfg.antlr.ZfgParser.LogicalExpressionContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.NamedTypeContext;
import zfg.antlr.ZfgParser.NumericLiteralContext;
import zfg.antlr.ZfgParser.PostfixAssignmentContext;
import zfg.antlr.ZfgParser.PrefixAssignmentContext;
import zfg.antlr.ZfgParser.PrefixExpressionContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.RecordLiteralContext;
import zfg.antlr.ZfgParser.RecordTypeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.StatementsContext;
import zfg.antlr.ZfgParser.TupleLiteralContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.TypeDeclarationContext;
import zfg.antlr.ZfgParser.UnambiguousExpressionContext;
import zfg.antlr.ZfgParser.VariableContext;
import zfg.antlr.ZfgParser.VariableDeclarationContext;
import zfg.antlr.ZfgParserListener;
import zfg.antlr.ZfgToken;
import zfg.antlr.ZfgTokenFactory;

public class Parser2 implements ZfgParserListener {

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
    final ParseTree syntaxTree = syntaxParser.module();
    System.out.println("parsed: " + syntaxTree.toStringTree(syntaxParser));
    System.out.println("tree:\n" + PrettyPrint.syntaxTree(syntaxParser, syntaxTree));

    // Semantic Analysis
    final ParseTreeWalker walker = new ParseTreeWalker();
    final Parser2 listener = new Parser2();
    walker.walk(listener, syntaxTree);

    //   // Semantic Analysis
    // final Parser2 parser = new Parser2();
    // final Ast.Module root = parser.parseModule(parsed, source.getSourceName());
    // final List<Error> errors = parser.errors();
    // System.out.println("parsed: " + root);
    // System.out.println("tree: " + root);
    // System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
    // return errors.isEmpty() ? new Result.Val(root) : new Result.Err(errors);
    return null;
  }

  private Parser2() {}

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Errors
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /** Parser error */
  private static final class Error {
    final ParserRuleContext ctx; // The most speicific context for where the error occurred
    final String msg;            // The error message

    private Error(final ParserRuleContext ctx, final String msg) {
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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Module
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitModule(final ModuleContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitModule'");
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statements
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitStatements(final StatementsContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitStatements'");
  }

  public void visitStatement(final StatementContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitStatement'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Declarations
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitDeclaration(final DeclarationContext ctx) {}

  public void visitTypeDeclaration(final TypeDeclarationContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitTypeDeclaration'");
  }

  public void visitFunctionDeclaration(final FunctionDeclarationContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionDeclaration'");
  }

  public void visitVariableDeclaration(final VariableDeclarationContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitVariableDeclaration'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitExpression(final ExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitExpression'");
  }

  public void visitUnambiguousExpression(final UnambiguousExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitUnambiguousExpression'");
  }

  public void visitPrefixExpression(final PrefixExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitPrefixExpression'");
  }

  public void visitAlgebraExpression(final AlgebraExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitAlgebraExpression'");
  }

  public void visitBitwiseExpression(final BitwiseExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitBitwiseExpression'");
  }

  public void visitCompareExpression(final CompareExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitCompareExpression'");
  }

  public void visitLogicalExpression(final LogicalExpressionContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitLogicalExpression'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Assignments
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public void visitAssignment(final AssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitAssignment'");
  }

  public void visitBivariateAssignment(final BivariateAssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitBivariateAssignment'");
  }

  public void visitPostfixAssignment(final PostfixAssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitPostfixAssignment'");
  }

  public void visitPrefixAssignment(final PrefixAssignmentContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitPrefixAssignment'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Literals
  //////////////////////////////////////////////////////////////////////////////////////////////////


  public Inst visitLiteral(final LiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitLiteral'");
  }

  public Inst visitNumericLiteral(final NumericLiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitNumericLiteral'");
  }

  public Inst visitRecordLiteral(final RecordLiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitRecordLiteral'");
  }

  public Inst visitTupleLiteral(final TupleLiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitTupleLiteral'");
  }

  public Inst visitArrayLiteral(final ArrayLiteralContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitArrayLiteral'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public Type visitType(final TypeContext ctx) {
    return ctx.typed = switch (ctx.children == null ? null : ctx.children.get(0)) {
      case PrimitiveTypeContext alt -> visitPrimitiveType(alt);
      case FunctionTypeContext alt -> visitFunctionType(alt);
      case RecordTypeContext alt -> visitRecordType(alt);
      case TupleTypeContext alt -> visitTupleType(alt);
      case ArrayTypeContext alt -> visitArrayType(alt);
      case NamedTypeContext alt -> visitNamedType(alt);
      default -> throw new AssertionError();
    };
  }

  public Type visitPrimitiveType(final PrimitiveTypeContext ctx) {
    return ctx.typed = switch (((ZfgToken) ctx.token).type) {
      case ZfgLexer.BIT -> Types.BIT;
      case ZfgLexer.I08 -> Types.I08;
      case ZfgLexer.I16 -> Types.I16;
      case ZfgLexer.I32 -> Types.I32;
      case ZfgLexer.I64 -> Types.I64;
      case ZfgLexer.U08 -> Types.U08;
      case ZfgLexer.U16 -> Types.U16;
      case ZfgLexer.U32 -> Types.U32;
      case ZfgLexer.U64 -> Types.U64;
      case ZfgLexer.F32 -> Types.F32;
      case ZfgLexer.F64 -> Types.F64;
      default -> throw new AssertionError();
    };
  }

  public Type visitFunctionType(final FunctionTypeContext ctx) {
    // Post-order traversal
    final Type paramsType = visitRecordType(ctx.paramsType);
    final Type returnType = visitType(ctx.returnType);
    // Error checking
    if (paramsType == Types.ERR || returnType == Types.ERR) return ctx.typed = Types.ERR;
    // Type inference
    return ctx.typed = Types.Fun(paramsType, returnType);
  }

  public Type visitRecordType(final RecordTypeContext ctx) {
    boolean error = false;
    final List<Token>       ctxMuts = ctx.muts;
    final List<Token>       ctxKeys = ctx.keys;
    final List<TypeContext> ctxTypes = ctx.types;
    final int       size  = ctxTypes.size();
    final boolean[] muts  = new boolean[size];
    final String[]  keys  = new String[size];
    final Type[]    types = new Type[size];
    // Post-order traversal
    for (int i = 0; i < size; i++) {
      muts[i] = ctxMuts.get(i) != null;
      keys[i] = ctxKeys.get(i).getText();
      if ((types[i] = visitType(ctxTypes.get(i))) == Types.ERR) error = true;
    }
    // Error checking
    if (hasDuplicates(keys)) { err(ctx, "Record field names must be unique"); error = true; }
    if (error) return ctx.typed = Types.ERR;
    // Type inference
    return ctx.typed = Types.Rec(muts, keys, types);
  }

  public Type visitTupleType(final TupleTypeContext ctx) {
    boolean error = false;
    final List<Token>       ctxMuts = ctx.muts;
    final List<TypeContext> ctxTypes = ctx.types;
    final int       size  = ctxTypes.size();
    final boolean[] muts  = new boolean[size];
    final Type[]    types = new Type[size];
    // Post-order traversal
    for (int i = 0; i < size; i++) {
      muts[i] = ctxMuts.get(i) != null;
      if ((types[i] = visitType(ctxTypes.get(i))) == Types.ERR) error = true;
    }
    // Error checking
    if (error) return ctx.typed = Types.ERR;
    // Type inference
    return ctx.typed = Types.Tup(muts, types);
  }

  public Type visitArrayType(final ArrayTypeContext ctx) {
    boolean error = false;
    // Post-order traversal
    final boolean mut    = ctx.mut != null;
    final Type    type   = visitType(ctx.typ);
    final int     length = switch (ctx.len) {
      case null -> -1;
      case Token len when parseIntLit(len.getText(), false) instanceof Inst.I32 i32 -> i32.value;
      default   -> -2;
    };
    // Error checking
    if (type == Types.ERR) error = true;
    if (length < -1) { err(ctx, "Invalid array length: " + ctx.len.getText()); error = true; }
    if (error) return ctx.typed = Types.ERR;
    // Type inference
    return ctx.typed = length == -1 ? Types.Arr(mut, type) : Types.Arr(mut, type, length);
  }

  public Type visitNamedType(final NamedTypeContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitNamedType'");
  }

  private static boolean hasDuplicates(final String[] names) {
    final int length = names.length;
    if (length < 32) {
      for (int i = length - 1; i >= 1; i -= 1) {
        final String name = names[i];
        for (int j = i - 1; j >= 0; j -= 1) if (name.equals(names[j])) return true;
      }
      return false;
    } else {
      final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
      for (int i = 0; i < length; i++) if (!set.add(names[i])) return true;
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Function Call
  //////////////////////////////////////////////////////////////////////////////////////////////////


  public void visitFunctionCall(final FunctionCallContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Variable
  //////////////////////////////////////////////////////////////////////////////////////////////////


  public void visitVariable(final VariableContext ctx) {
    throw new UnsupportedOperationException("Unimplemented method 'visitVariable'");
  }
}
