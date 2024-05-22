// package zfg;

// import java.util.ArrayDeque;
// import java.util.ArrayList;
// import java.util.Deque;
// import java.util.HashMap;
// import java.util.List;

// import zfg.Ast2.Modifier;
// import zfg.antlr.ZfgContext;

// public sealed interface Symbol {
//   public ZfgContext ctx();
//   public Modifier mod();
//   public String name();
//   public Type type();

//   public sealed interface Decl extends Symbol {}

//   public static final class TypeDecl implements Decl {
//     public final Ast2.TypeDecl decl;
//     public TypeDecl(final Ast2.TypeDecl decl) { this.decl = decl; }
//     @Override public ZfgContext ctx() { return decl.ctx; }
//     @Override public Modifier mod() { return decl.mod; }
//     @Override public String name() { return decl.type.name; }
//     @Override public Type type() { return decl.type; }
//   }

//   public static final class VarDecl implements Decl {
//     public final Ast2.VarDecl decl;
//     public VarDecl(final Ast2.VarDecl decl) { this.decl = decl; }
//     @Override public ZfgContext ctx() { return decl.ctx; }
//     @Override public Modifier mod() { return decl.mod; }
//     @Override public String name() { return decl.name; }
//     @Override public Type type() { return decl.type; }
//   }

//   public static final class FunDecl implements Decl {
//     public final Ast2.FunDecl decl;
//     public FunDecl(final Ast2.FunDecl decl) { this.decl = decl; }
//     @Override public ZfgContext ctx() { return decl.ctx; }
//     @Override public Modifier mod() { return decl.mod; }
//     @Override public String name() { return decl.name; }
//     @Override public Type type() { return decl.type; }
//   }


//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Scopes
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public static sealed abstract class Scope {
//     public final List<Symbol> symbols = new ArrayList<>();

//     public static final class Global extends Scope {}
//     public static final class Module extends Scope {}
//     public static final class Method extends Scope {}
//     public static final class Nested extends Scope {}
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Table
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public static final class Table {
//     final ArrayDeque<Scope> scopes = new ArrayDeque<>();
//     final HashMap<String, Deque<Symbol>> symbolTable = new HashMap<>();
//     final HashMap<ZfgContext, Symbol> ctxSymbolMap = new HashMap<>();

//     public void pushGlobalScope() {
//       assert scopes.isEmpty();
//       scopes.push(new Scope.Global());
//     }
//     public void pushModuleScope() {
//       assert scopes.peek() instanceof Scope.Global;
//       scopes.push(new Scope.Module());
//     }
//     public void pushMethodScope() {
//       assert scopes.peek() instanceof Scope.Module;
//       scopes.push(new Scope.Method());
//     }
//     public void pushNestedScope() {
//       assert scopes.peek() instanceof Scope.Method || scopes.peek() instanceof Scope.Nested;
//       scopes.push(new Scope.Nested());
//     }

//     private Scope popScope() {
//       // Pop the scope
//       final Scope scope = scopes.pop();
//       // For each symbol in the scope...
//       final List<Symbol> scopeSymbols = scope.symbols;
//       final int scopeSymbolCount = scopeSymbols.size();
//       for (int i = 0; i < scopeSymbolCount; i++) {
//         final Symbol symbol = scopeSymbols.get(i);
//         // Get the symbol's table row
//         final String symbolName = symbol.name();
//         final Deque<Symbol> symbolTableRow = symbolTable.get(symbolName);
//         assert symbolTableRow.peek() == symbol;
//         // Pop the symbol off the table row
//         if (symbolTableRow.size() > 1) {
//           symbolTableRow.pop();
//         } else {
//           symbolTable.remove(symbolName);
//         }
//         // Remove the symbol from the context-symbol map
//         assert ctxSymbolMap.get(symbol.ctx()) == symbol;
//         ctxSymbolMap.remove(symbol.ctx());
//       }
//       // Return the popped scope
//       return scope;
//     }

//     public Scope.Global popGlobalScope() {
//       assert scopes.peek() instanceof Scope.Global;
//       return (Scope.Global) popScope();
//     }
//     public Scope.Module popModuleScope() {
//       assert scopes.peek() instanceof Scope.Module;
//       return (Scope.Module) popScope();
//     }
//     public Scope.Method popMethodScope() {
//       assert scopes.peek() instanceof Scope.Method;
//       return (Scope.Method) popScope();
//     }
//     public Scope.Nested popNestedScope() {
//       assert scopes.peek() instanceof Scope.Nested;
//       return (Scope.Nested) popScope();
//     }

//     public void declare(final Decl symbol, final Parser4.Err err) {
//       assert symbol != null;
//       assert err != null;
//       assert !ctxSymbolMap.containsKey(symbol.ctx());
//       // Get the symbol's name
//       final String name = symbol.name();
//       // Get the symbol's scope
//       final Scope scope = scopes.peek();
//       // Validate that PUB symbols are only declared the global scope or a module scope
//       if (
//         symbol.mod() == Modifier.PUB &&
//         !(scope instanceof Scope.Global) &&
//         !(scope instanceof Scope.Module)
//       ) {
//         err.err(symbol.ctx(), "Public symbols must be declared in a module scope.");
//       }
//       // Get the symbol's table row
//       final Deque<Symbol> row = symbolTable.computeIfAbsent(name, k -> new ArrayDeque<>());
//       // Get the symbol, if any, that will be shadowed by the new symbol
//       final Symbol hidden = row.peek();
//       // Validate that PUB symbols are never shadowed
//       if (hidden != null && hidden.mod() == Modifier.PUB) {
//         err.err(symbol.ctx(), "Cannot shadow a public symbol.");
//       }
//       // Add the symbol to it's row (shadowing the other symbols in the row)
//       row.push(symbol);
//       // Add the symbol to the scope
//       scope.symbols.add(symbol);
//       // Add the symbol to the context-symbol map
//       ctxSymbolMap.put(symbol.ctx(), symbol);
//     }

//     public Symbol getSymbol(final ZfgContext ctx) {
//       return ctxSymbolMap.get(ctx);
//     }
//   }
// }
