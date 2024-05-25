// package zfg;

// import org.antlr.v4.runtime.Token;
// import java.util.Map;
// import java.util.HashMap;

// import zfg.antlr.ZfgContext;
// import zfg.antlr.ZfgParser;
// import zfg.antlr.ZfgParser.FunctionDeclarationContext;
// import zfg.antlr.ZfgParser.FunctionParameterContext;
// import zfg.antlr.ZfgParser.ScopeContext;
// import zfg.antlr.ZfgParser.TypeDeclarationContext;
// import zfg.antlr.ZfgParser.VariableDeclarationContext;
// import zfg.antlr.ZfgParserBaseListener;

// public final class SymbolResolver extends ZfgParserBaseListener {
//   private static enum Modifier { PUB, MUT, LET }


//   private static class Symbol {
//     public final ZfgContext ctx;
//     public final Modifier modifier;
//     public final String name;

//     public Symbol(final ZfgContext ctx, final Modifier modifier, final String name) {
//       this.ctx = ctx;
//       this.modifier = modifier;
//       this.name = name;
//     }
//   }

//   private final Map<String, Symbol> symbolMap = new HashMap<>();


//   private void createScope() {

//   }
//   private void finishScope() {

//   }

//   // new
//   // def
//   // use
//   // set
//   private void createSymbol(final ZfgContext ctx, final Token mod, final Token name) {
//     createSymbol(ctx, parseModifier(mod), name.getText());
//   }
//   private void createSymbol(final ZfgContext ctx, final Modifier mod, final String name) {
//     final Symbol symbol = new Symbol(ctx, mod, name);
//   }

//   private void accessSymbol(final ZfgContext ctx, final Token name) {
//     accessSymbol(ctx, name.getText());
//   }
//   private void accessSymbol(final ZfgContext ctx, final String name) {

//   }

//   private void updateSymbol(final ZfgContext ctx, final Token name) {
//     updateSymbol(ctx, name.getText());
//   }
//   private void updateSymbol(final ZfgContext ctx, final String name) {
//     final Symbol symbol = symbolMap.get(name);
//   }



//   private Modifier parseModifier(final Token mod) {
//     return mod == null ? Modifier.LET : switch (mod.getType()) {
//       case ZfgParser.LET -> Modifier.LET;
//       case ZfgParser.PUB -> Modifier.PUB;
//       case ZfgParser.MUT -> Modifier.MUT;
//       default -> throw new AssertionError();
//     };
//   }

//   class DeclarationListener extends ZfgParserBaseListener {

//     @Override
//     public void enterScope(final ScopeContext ctx) {
//       createScope();
//       if (ctx.parent instanceof final FunctionDeclarationContext funCtx) {
//         for (final FunctionParameterContext parCtx : funCtx.typed.pars) {
//           createSymbol(parCtx, parCtx.muta, parCtx.name);
//         }
//       }
//     }

//     @Override
//     public void exitScope(final ScopeContext ctx) {
//       finishScope();
//     }

//     @Override
//     public void exitTypeDeclaration(final TypeDeclarationContext ctx) {

//       // int x = 5;
//       //
//       // New Symbol
//     }

//     @Override
//     public void exitFunctionDeclaration(final FunctionDeclarationContext ctx) {
//       // New Symbol
//     }

//     @Override
//     public void exitVariableDeclaration(final VariableDeclarationContext ctx) {
//       // New Symbol
//     }

//     @Override
//     public void enterUnaryAssignment(final ZfgParser.UnaryAssignmentContext ctx) {
//       //
//     }

//     @Override
//     public void exitBinaryAssignment(final ZfgParser.BinaryAssignmentContext ctx) {
//       accessSymbol(ctx, ctx.name);
//     }

//     @Override
//     public void enterBinaryAssignment(final ZfgParser.BinaryAssignmentContext ctx) {
//       //
//     }

//     @Override
//     public void exitUnaryAssignment(final ZfgParser.UnaryAssignmentContext ctx) {
//       accessSymbol(ctx, ctx.name);
//     }
//   }
// }
