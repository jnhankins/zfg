package zfg;

import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import zfg.antlr.ZfgContext;
import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.ComparisonOperandContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionConstructorContext;
import zfg.antlr.ZfgParser.FunctionDeclarationContext;
import zfg.antlr.ZfgParser.LogicalOperandContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.RecordFieldContext;
import zfg.antlr.ZfgParser.ScopeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.TypeDeclarationContext;
import zfg.antlr.ZfgParser.UnambiguousExpressionContext;
import zfg.antlr.ZfgParser.VariableDeclarationContext;
import zfg.antlr.ZfgParserBaseListener;
import zfg.antlr.ZfgTokenFactory;

public class Compiler {
  public static void parse(final java.nio.file.Path path) {
    try { parse(CharStreams.fromPath(path)); }
    catch (final Exception e) { throw new RuntimeException(e); }
  }

  public static void parse(final String source, final String sourceName) {
    parse(CharStreams.fromString(source, sourceName));
  }

  public static void parse(final CharStream source) {
    System.out.println("source: " + source.getSourceName());
    System.out.println(">" + source.toString().replaceAll("\\r?\\n", "\n>"));

    // Lexical Analysis
    final ZfgLexer tokenLexer = new ZfgLexer(source);
    tokenLexer.setTokenFactory(new ZfgTokenFactory());
    final CommonTokenStream tokenStream = new CommonTokenStream(tokenLexer);
    System.out.println("tokens: " + PrettyPrint.tokenStream(tokenLexer, tokenStream));

    // Syntax Analysis
    final ZfgParser syntaxParser = new ZfgParser(tokenStream);
    syntaxParser.addParseListener(new FirstPassListener());
    // syntaxParser.setTrace(true);
    final ModuleContext syntaxTree = syntaxParser.module();
    System.out.println("parsed: " + syntaxTree.toStringSyntaxTree(false));
    System.out.println("tree:\n" + syntaxTree.toStringSyntaxTree(true));

    // Semantic Analysis
    // ParseTreeWalker.DEFAULT.walk(new SymbolResolutionListener(), syntaxTree);

  }


  private static class FirstPassListener extends ZfgParserBaseListener {
    // Fold away useless productions to reduce parse tree height
    @Override public void exitStatement(final StatementContext ctx) { fold(ctx); }
    @Override public void exitExpression(final ExpressionContext ctx) { fold(ctx); }
    @Override public void exitUnambiguousExpression(final UnambiguousExpressionContext ctx) { fold(ctx); }
    @Override public void exitComparisonOperand(final ComparisonOperandContext ctx) { fold(ctx); }
    @Override public void exitLogicalOperand(final LogicalOperandContext ctx) { fold(ctx); }
    @Override public void exitType(final TypeContext ctx) { fold(ctx); }
    private static void fold(final ZfgContext ctx) {
      if (ctx.children == null || !(ctx.children[0] instanceof ZfgContext child)) return;
      final ZfgContext parent = ctx.getParent();
      final int index = ctx.parentIndex;
      child.setParent(parent);
      child.parentIndex = index;
      parent.children[index] = child;
    }

    // Handle symbol declarations
    @Override public void exitScope(final ScopeContext ctx) {
      if (ctx.getParent() instanceof FunctionConstructorContext fun) {
        final List<RecordFieldContext> fields = fun.typed.rec.fields;
        final int fieldCount = fields.size();
        for (int i = 0; i < fieldCount; i++) {
          final RecordFieldContext field = fields.get(i);
          declare(field, field.muta, field.name);
        }
      }
      final int childCount = ctx.childCount;
      for (int i = 0; i < childCount; i++) {
        switch (ctx.children[i]) {
          case final     TypeDeclarationContext child -> declare(child, child.mod, child.name);
          case final FunctionDeclarationContext child -> declare(child, child.mod, child.name);
          case final VariableDeclarationContext child -> declare(child, child.mod, child.name);
          default -> {}
        }
      }
    }

    private static void declare(final ZfgContext ctx, final Token mod, final Token name) {
      declare(ctx, parseModifier(mod), name.getText());
    }

    private static void declare(final ZfgContext ctx, final Modifier mod, final String name) {
      System.out.println("Declare: " + mod + " " + name);
    }
  }

  private static enum Modifier {
    PUB, LET, MUT;
    public final String lowerCaseName = name().toLowerCase();
    @Override public String toString() { return lowerCaseName; }
  }

  private static Modifier parseModifier(final Token token) {
    if (token == null) return Modifier.LET;
    return switch (token.getType()) {
      case ZfgLexer.PUB -> Modifier.PUB;
      case ZfgLexer.LET -> Modifier.LET;
      case ZfgLexer.MUT -> Modifier.MUT;
      default -> throw new AssertionError();
    };
  }

  // private static class Declaration {
  //   public final ZfgContext ctx;
  //   public final Modifier mod;
  //   public final String name;
  //   public final int pos;
  //   public Declaration(
  //     final ZfgContext ctx,
  //     final Modifier mod,
  //     final String name,
  //     final int pos
  //   ) {
  //     this.ctx = ctx;
  //     this.name = name;
  //     this.mod = mod;
  //     this.pos = pos;
  //   }
  // }

  // private static class SymbolScope extends HashMap<String, Symbol[]> {}

  // private static class ForwardDeclarationListener extends ZfgParserBaseListener {
  //   private static final Deque<SymbolScope> scopeStack = new ArrayDeque<>();
  //   @Override public void enterScope(final ScopeContext ctx) { scopeStack.push(new SymbolScope()); }
  //   @Override public void exitScope(final ScopeContext ctx) { scopeStack.pop(); }
  //   @Override public void enterTypeDeclaration(final TypeDeclarationContext ctx) { declare(ctx, ctx.name, ctx.mod); }

  // }

  // // private static class Scope {

  // //   @Override public void exitScope(final ScopeContext ctx) {
  // //     final int childCount = ctx.childCount;
  // //     final Map<String, List<Symbol>> seen = new HashMap<>();
  // //     for (int c = 0; c < childCount; c++) {
  // //       final ZfgContext child;
  // //       final Token modToken;
  // //       final Token nameToken;
  // //       switch (ctx.children[c]) {
  // //         case final TypeDeclarationContext t:
  // //           child = t;
  // //           modToken = t.mod;
  // //           nameToken = t.name;
  // //           break;
  // //         case final FunctionDeclarationContext f:
  // //           child = f;
  // //           modToken = f.mod;
  // //           nameToken = f.name;
  // //           break;
  // //         case final VariableDeclarationContext v:
  // //           child = v;
  // //           modToken = v.mod;
  // //           nameToken = v.name;
  // //           break;
  // //         default: continue;
  // //       }
  // //       final Modifier mod = switch (modToken.getType()) {
  // //         case ZfgLexer.PUB -> Modifier.PUB;
  // //         case ZfgLexer.LET -> Modifier.LET;
  // //         case ZfgLexer.MUT -> Modifier.MUT;
  // //         default -> throw new AssertionError();
  // //       };
  // //       final String name = nameToken.getText();
  // //       final List<Symbol> symbols = seen.computeIfAbsent(name, k -> new ArrayList<>());
  // //       final Symbol symbol = new Symbol(child, name, mod);
  // //       symbols.add(symbol);
  // //     }
  // //     final Map<String, Symbol[]>
  // //   }
  // // }

  // // static class SymbolResolutionListener extends ZfgParserBaseListener {

  // //   private static final class Symbol {
  // //     public final ZfgContext ctx;
  // //     public final String name;
  // //     public final Modifier mod;
  // //     public Symbol(final ZfgContext ctx, final String name, final Modifier mod) {
  // //       this.ctx = ctx;
  // //       this.name = name;
  // //       this.mod = mod;
  // //     }
  // //   }

  // //   private static enum Modifier {
  // //     PUB(ZfgLexer.PUB), LET(ZfgLexer.PUB), MUT(ZfgLexer.PUB);
  // //     private String s;
  // //     private Modifier(final int tt) { s = ZfgLexer.VOCABULARY.getLiteralName(tt); }
  // //     public static Modifier parse(final Token mod) {
  // //       return switch (mod.getType()) {
  // //         case ZfgLexer.PUB -> PUB;
  // //         case ZfgLexer.LET -> LET;
  // //         case ZfgLexer.MUT -> MUT;
  // //         default -> throw new AssertionError();
  // //       };
  // //     }
  // //   }


  // //   private final Deque<ScopeContext> scopeStack = new ArrayDeque<>();
  // //   private final Map<String, Symbol> symbols = new HashMap<>();

  // //   ////////////////////////////////////////////////////////////////////////////////////////////////
  // //   // Declare Symbols
  // //   ////////////////////////////////////////////////////////////////////////////////////////////////

  // //   @Override public void enterTypeDeclaration(final TypeDeclarationContext ctx) { declare(ctx, ctx.name, ctx.mod); }
  // //   @Override public void enterFunctionDeclaration(final FunctionDeclarationContext ctx) { declare(ctx, ctx.name, ctx.mod); }
  // //   @Override public void exitVariableDeclaration(final VariableDeclarationContext ctx) { declare(ctx, ctx.name, ctx.mod); }

  // //   public void declare(final ZfgContext ctx, final Token name, final Token mod) {
  // //     declare(ctx, name.toString(), Modifier.parse(mod));
  // //   }

  // //   public void declare(final ZfgContext ctx, final String name, final Modifier mod) {
  // //     final String key = name.toString();
  // //     if (symbols.containsKey(key)) {
  // //       final Symbol symbol = symbols.get(key);
  // //       System.err.println("Symbol '" + key + "' already declared at " + symbol.ctx.toStringSyntaxTree());
  // //       System.err.println("  redeclared at " + ctx.toStringSyntaxTree());
  // //     }
  // //     symbols.put(key, new Symbol(ctx, key, mod));
  // //   }

  // //   @Override public void exitUnaryAssignment(final UnaryAssignmentContext ctx) {
  // //     final String name = ctx.name.toString();
  // //   }

  // //   ////////////////////////////////////////////////////////////////////////////////////////////////
  // //   // Resolve Symbols
  // //   ////////////////////////////////////////////////////////////////////////////////////////////////

  // //   @Override
  // //   public void exitPathExpression(final PathExpressionContext ctx) {
  // //     System.out.println("Symbol: " + ctx.toStringSyntaxTree());
  // //   }

  // //   @Override
  // //   public void exitNamedType(final NamedTypeContext ctx) {
  // //     System.out.println("Symbol: " + ctx.toStringSyntaxTree());
  // //   }
  // // }
}
