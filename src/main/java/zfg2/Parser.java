package zfg2;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AnyTypeContext;
import zfg.antlr.ZfgParser.ArrTypeContext;
import zfg.antlr.ZfgParser.ArrayTypeContext;
import zfg.antlr.ZfgParser.AssignmentStatementContext;
import zfg.antlr.ZfgParser.FunTypeContext;
import zfg.antlr.ZfgParser.FunctionDeclarationContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.InvocationStatementContext;
import zfg.antlr.ZfgParser.LiteralContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.NumericLitContext;
import zfg.antlr.ZfgParser.NumericLiteralContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.PriTypeContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.RecTypeContext;
import zfg.antlr.ZfgParser.RecordTypeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.TupTypeContext;
import zfg.antlr.ZfgParser.TupleTypeContext;
import zfg.antlr.ZfgParser.TypeDeclarationContext;
import zfg.antlr.ZfgParser.VarTypeContext;
import zfg.antlr.ZfgParser.VariableDeclarationContext;
import zfg.antlr.ZfgParser.VariableTypeContext;

public final class Parser {

  public static interface Result {
    public static final record Val(Ast value) implements Result {}
    public static final record Err(List<Error> errors) implements Result {}
  }

  public static Result parse(final Path path) {
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
    final ZfgLexer zfgLexer = new ZfgLexer(source);
    final CommonTokenStream tokens = new CommonTokenStream(zfgLexer);
    System.out.println("tokens: " + PrettyPrint.tokenStream(zfgLexer, tokens));

    // Syntax Analysis
    final ZfgParser zfgParser = new ZfgParser(tokens);
    final ModuleContext parsed = zfgParser.module();
    System.out.println("parsed: " + parsed.toStringTree(zfgParser));
    System.out.println("tree:\n" + PrettyPrint.syntaxTree(zfgParser, parsed));

    // Semantic Analysis
    final Parser parser = new Parser();
    final Ast.Module root = parser.parseModule(parsed, source.getSourceName());
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
  private static final class Error {
    final ParserRuleContext ctx; // The most speicific context for where the error occurred
    final String msg;            // The error message

    private Error(final ParserRuleContext ctx, final String msg) {
      this.ctx = ctx;
      this.msg = msg;
    }

    @Override public String toString() {
      final int line = ctx.start.getLine();
      final int column = ctx.start.getCharPositionInLine();
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
  // Symbol Table
  //////////////////////////////////////////////////////////////////////////////////////////////////



  private static final class Symbol {
    public final ParserRuleContext ctx;
    public final List<Symbol> scope;
    public final int index;
    public final boolean pub;
    public final boolean mut;
    public final String  name;
    public final Type type;

    public Symbol(
      final ParserRuleContext ctx,
      final List<Symbol> scope,
      final int index,
      final boolean pub,
      final boolean mut,
      final String name,
      final Type type
    ) {
      this.ctx = ctx;
      this.scope = scope;
      this.index = index;
      this.pub = pub;
      this.mut = mut;
      this.name = name;
      this.type = type;
    }
  }

  private final Map<String, Deque<Symbol>> symbolTable = new HashMap<>();
  private final Deque<List<Symbol>>        scopeStack  = new ArrayDeque<>();

  public void pushScope() {
    final List<Symbol> scope = new ArrayList<>();
    scopeStack.push(scope);
  }

  private List<Symbol> popScope() {
    final List<Symbol> scope = scopeStack.pop();
    final int symbolCount = scope.size();
    for (int i = 0; i < symbolCount; i++) {
      final Symbol symbol = scope.get(i);
      final Deque<Symbol> symbols = symbolTable.get(symbol.name);
      final Symbol popped = symbols.pop();
      assert popped == symbol;
      if (symbols.isEmpty()) symbolTable.remove(symbol.name);
    }
    return scope;
  }

  private void putSymbol(
    final ParserRuleContext ctx,
    final boolean pub,
    final boolean mut,
    final String name,
    final Type type
  ) {
    // Error if pub symbol is declared in a non-module-level scope
    if (pub && scopeStack.size() != 1) {
      err(ctx, "cannot declare a public symbol in a non-module-level scope");
      return;
    }

    // Get the current scope
    final List<Symbol> scope = scopeStack.peek();
    // Get the entry for the symbol's name in the symbol table
    final Deque<Symbol> entry = symbolTable.computeIfAbsent(name, k -> new ArrayDeque<>());
    // Get the extant symbol for the name in the table if it exists
    final Symbol extant = entry.peek();

    // Handle case where an extant symbol is being updated during compilation
    if (extant != null && extant.ctx == ctx) {
      assert extant.scope == scope && extant.pub == pub && extant.mut == mut;
      // Create the new symbol
      final Symbol symbol = new Symbol(ctx, scope, extant.index, pub, mut, name, type);
      // Replace the extant symbol with the new symbol in the symbol table row
      entry.pop();
      entry.push(symbol);
      // Replace the extant symbol with the new symbol in the scope
      scope.set(extant.index, symbol);
      return;
    }

    // Error if extant symbol is pub and is in the same scope
    if (extant != null && extant.scope == scope && (pub || extant.pub)) {
      err(ctx, "cannot declare a public symbol more than once in the same scope");
      return;
    }

    // Insert the new symbol
    final Symbol symbol = new Symbol(ctx, scope, scope.size(), pub, mut, name, type);
    // Add the symbol to the entry
    entry.push(symbol);
    // Add the symbol to the scope
    scope.add(symbol);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Module
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Ast.Module parseModule(final ModuleContext ctx, final String fqn) {
    final List<StatementContext> statements = ctx.scope().stmts;
    final int statementCount = statements.size();
    // 1. Push a new scope
    pushScope();
    // 2. Create forward declaration entries in the symbol table
    for (int i = 0; i < statementCount; i++) {
      switch (statements.get(i)) {
        case TypeDeclarationContext decl: {
          final boolean pub = decl.pub != null;
          final boolean mut = decl.mut != null && decl.mut.getType() == ZfgLexer.MUT;
          final String name = decl.name.getText();
          putSymbol(decl, pub, mut, name, Types.UNK);
          break;
        }
        case FunctionDeclarationContext decl: {
          final boolean pub = decl.pub != null;
          final boolean mut = decl.mut != null && decl.mut.getType() == ZfgLexer.MUT;
          final String name = decl.name.getText();
          putSymbol(decl, pub, mut, name, Types.UNK);
          break;
        }
        case VariableDeclarationContext decl: {
          final boolean pub = decl.pub != null;
          final boolean mut = decl.mut != null && decl.mut.getType() == ZfgLexer.MUT;
          final String name = decl.name.getText();
          putSymbol(decl, pub, mut, name, Types.UNK);
          break;
        }
        case AssignmentStatementContext stmt: break;
        case InvocationStatementContext stmt: break;
        default: throw new AssertionError();
      }
    }

    // 1. Parse the fully qualified name
    // N/A
    // 2. Parse the statements
    final Ast.Stmt[] stmts = new Ast.Stmt[statementCount];
    for (int i = 0; i < statementCount; i++) stmts[i] = parseStatement(statements.get(i));

    // 3. Error propagation
    for (int i = 0; i < statementCount; i++) if (stmts[i] == null) return null;
    // Construct the node
    return new Ast.Module(fqn, stmts);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Literals
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Inst parseLiteral(final LiteralContext ctx) {
    return switch (ctx) {
      case NumericLitContext lit -> parseNumericLit(lit.lit);
      default -> throw new AssertionError();
    };
  }

  private Inst parseNumericLit(final NumericLiteralContext ctx) {
    final String text = ctx.token.getText();
    final int    type = ctx.token.getType();
    switch (type) {
      case ZfgLexer.BitLit: {
        final Inst parsed = Literals.parseBitLit(text);
        if (parsed == null) err(ctx, "Invalid bit literal: \"" + text + "\"");
        return parsed;
      }
      case ZfgLexer.IntLit: {
        final boolean hasMinusPrefix =
            ctx.parent instanceof PrefixOpExprContext parent &&
            parent.opr.getType() == ZfgLexer.SUB &&
            parent.opr.getStopIndex() + 1 == ctx.getStart().getStartIndex();
        final Inst parsed = Literals.parseIntLit(text, hasMinusPrefix);
        if (parsed == null) err(ctx, "Invalid int literal: \"" + text + "\"");
        return parsed;
      }
      case ZfgLexer.FltLit: {
        final Inst parsed = Literals.parseFltLit(text);
        if (parsed == null) err(ctx, "Invalid flt literal: \"" + text + "\"");
        return parsed;
      }
      default: throw new AssertionError();
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Types
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Type parseAnyType(final AnyTypeContext ctx) {
    return switch (ctx) {
      case FunTypeContext type -> parseFunctionType(type.type);
      case VarTypeContext type -> parseVariableType(type.type);
      default -> throw new AssertionError();
    };
  }

  private Type parseVariableType(final VariableTypeContext ctx) {
    return switch (ctx) {
      case RecTypeContext type -> parseRecordType(type.type);
      case TupTypeContext type -> parseTupleType(type.type);
      case ArrTypeContext type -> parseArrayType(type.type);
      case PriTypeContext type -> parsePrimitiveType(type.type);
      default -> throw new AssertionError();
    };
  }

  private Type parseFunctionType(final FunctionTypeContext ctx) {
    final List<Token>          fieldModifiers = ctx.parameterModifiers;
    final List<Token>          fieldNames = ctx.parameterNames;
    final List<AnyTypeContext> fieldTypes = ctx.parameterTypes;
    final int fieldCount = fieldModifiers.size();
    assert fieldNames.size() == fieldCount;
    assert fieldTypes.size() == fieldCount;
    // Parse the parameter modifiers
    final boolean[] imuts = new boolean[fieldCount];
    for (int i = 0; i < fieldCount; i++) imuts[i] = parseTypeModifier(fieldModifiers.get(i));
    // Parse the parameter names
    final String[] names = new String[fieldCount];
    for (int i = 0; i < fieldCount; i++) names[i] = fieldNames.get(i).getText();
    // Parse the parameter types
    final Type[] typez = new Type[fieldCount];
    for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
    // Parse the reutrn type
    final Type returnType = parseAnyType(ctx.returnType);
    // Unique names check
    if (!allDistinct(names)) return Types.ERR;
    // Error propagation
    for (int i = 0; i < fieldCount; i++) if (typez[i] == Types.ERR) return Types.ERR;
    if (returnType == Types.ERR) return Types.ERR;
    // Construct the node
    final Type.Rec parametersType = (Type.Rec) Types.Rec(imuts, names, typez);
    return Types.Fun(parametersType, returnType);
  }

  private Type parseRecordType(final RecordTypeContext ctx) {
    final List<Token>          fieldModifiers = ctx.fieldModifiers;
    final List<Token>          fieldNames = ctx.fieldNames;
    final List<AnyTypeContext> fieldTypes = ctx.fieldTypes;
    final int fieldCount = fieldModifiers.size();
    assert fieldNames.size() == fieldCount;
    assert fieldTypes.size() == fieldCount;
    // Parse the modifiers
    final boolean[] muts = new boolean[fieldCount];
    for (int i = 0; i < fieldCount; i++) muts[i] = parseTypeModifier(fieldModifiers.get(i));
    // Parse the names
    final String[] names = new String[fieldCount];
    for (int i = 0; i < fieldCount; i++) names[i] = fieldNames.get(i).getText();
    // Parse the types
    final Type[] typez = new Type[fieldCount];
    for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
    // Unique names check
    if (!allDistinct(names)) return Types.ERR;
    // Error propagation
    for (int i = 0; i < fieldCount; i++) if (typez[i] == Types.ERR) return Types.ERR;
    // Construct the node
    return Types.Rec(muts, names, typez);
  }

  private Type parseTupleType(final TupleTypeContext ctx) {
    final List<Token>          fieldModifiers = ctx.fieldModifiers;
    final List<AnyTypeContext> fieldTypes = ctx.fieldTypes;
    final int fieldCount = fieldModifiers.size();
    assert fieldTypes.size() == fieldCount;
    // Parse the modifiers
    final boolean[] muts = new boolean[fieldCount];
    for (int i = 0; i < fieldCount; i++) muts[i] = parseTypeModifier(fieldModifiers.get(i));
    // Parse the types
    final Type[] typez = new Type[fieldCount];
    for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
    // Error propagation
    for (int i = 0; i < fieldCount; i++) if (typez[i] == Types.ERR) return Types.ERR;
    // Construct the node
    return Types.Tup(muts, typez);
  }

  private Type parseArrayType(final ArrayTypeContext ctx) {
    // Parse the modifier
    final boolean imut = switch (ctx.elementsModifier.getType()) {
      case ZfgLexer.LET -> true;
      case ZfgLexer.MUT -> false;
      default -> throw new AssertionError();
    };
    // Parse the type
    final Type type = parseAnyType(ctx.elementsType);
    // Parse the length
    final int length;
    if (ctx.elementCount == null) {
      length = -1;
    } else if (Literals.parseIntLit(ctx.elementCount.getText(), false) instanceof Inst.I32 i32) {
      length = i32.value;
    } else {
      err(ctx, "Invalid array length: " + ctx.elementCount.getText());
      length = -2;
    }
    // Error propagation
    if (type == Types.ERR || length == -2) return Types.ERR;
    // Construct the node
    return length == -1
      ? Types.Arr(imut, type)
      : Types.Arr(imut, type, length);
  }

  private Type parsePrimitiveType(final PrimitiveTypeContext ctx) {
    return switch (ctx.token.getType()) {
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

  private static boolean parseTypeModifier(final Token token) {
    return switch (token.getType()) {
      case ZfgLexer.LET -> true;
      case ZfgLexer.MUT -> false;
      default -> throw new AssertionError();
    };
  }

  private static boolean allDistinct(final String[] names) {
    final int length = names.length;
    if (length < 32) {
      for (int i = length - 1; i >= 1; i -= 1) {
        final String name = names[i];
        for (int j = i - 1; j >= 0; j -= 1) if (name.equals(names[j])) return false;
      }
      return true;
    } else {
      final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
      for (int i = 0; i < length; i++) if (!set.add(names[i])) return false;
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statements
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private Ast.Stmt parseStatement(final StatementContext ctx) {
    return switch (ctx) {
      case TypeDeclarationContext     decl -> parseTypeDeclaration(decl);
      case FunctionDeclarationContext decl -> parseFunctionDeclaration(decl);
      case VariableDeclarationContext decl -> parseVariableDeclaration(decl);
      case AssignmentStatementContext stmt -> parseAssignmentStatement(stmt);
      case InvocationStatementContext stmt -> parseInvocationStatement(stmt);
      default -> throw new AssertionError();
    };
  }
  private Ast.Stmt parseTypeDeclaration(final TypeDeclarationContext ctx) {
    // 1. Parse the 'pub' modifier
    final boolean pub = ctx.pub != null;
    // 2. Parse the 'let' or 'mut' modifier
    final boolean mut = ctx.mut != null && ctx.mut.getType() == ZfgLexer.MUT;
    // 3. Parse the name
    final String name = ctx.name.getText();
    // 4. Parse the type
    assert ctx.type != null && ctx.type.getType() == ZfgLexer.TYPE;
    // 5. Put a placeholder for the symbol in the symbol table
    putSymbol(ctx, pub, mut, name, Types.UNK);
    // 6. Parse the type definition
    final Type type = parseAnyType(ctx.rhs);
    // 7. Error propagation
    if (type == Types.ERR) return null;
    // 8. Update the symbol in the symbol table
    putSymbol(ctx, pub, mut, name, type);
  }

  private Ast.Stmt parseFunctionDeclaration(final FunctionDeclarationContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  private Ast.Stmt parseVariableDeclaration(final VariableDeclarationContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  private Ast.Stmt parseAssignmentStatement(final AssignmentStatementContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  private Ast.Stmt parseInvocationStatement(final InvocationStatementContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
}
