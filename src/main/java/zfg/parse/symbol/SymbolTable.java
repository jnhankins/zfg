package zfg.parse.symbol;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.core.type.Type;
import zfg.parse.Parser;
import zfg.parse.node.Node;

public final class SymbolTable {
  private final Map<String, Deque<Symbol>> table = new HashMap<>();
  private final Deque<SymbolScope>         stack = new ArrayDeque<>();

  public void pushScope() {
    stack.push(new SymbolScope());
  }

  public SymbolScope popScope() {
    final SymbolScope scope = stack.pop();
    for (final Symbol symbol : scope.symbols) {
      final Deque<Symbol> symbols = table.get(symbol.name);
      if (symbols.pop() != symbol) throw new AssertionError();
      if (symbols.isEmpty()) table.remove(symbol.name);
    }
    return scope;
  }

  public Parser.Error addSymbol(
    final DeclarationStmtContext ctx,
    final SymbolModifier         mod,
    final String                 name,
    final Type                   type,
    final Node                   node
  ) {
    // Error if pub symbol is declared in a non-module-level scope
    if (mod == SymbolModifier.Pub && stack.size() != 1) {
      return new Parser.Error(ctx, "cannot declare a public symbol in a non-module-level scope");
    }

    // Get the current scope
    final SymbolScope   scope = stack.peek();
    // Get the entry for the symbol's name in the symbol table
    final Deque<Symbol> entry = table.computeIfAbsent(name, k -> new ArrayDeque<>());
    // Get the extant symbol for the name in the table if it exists
    final Symbol extant = entry.peek();

    // Handle case where the extant symbol is being updated
    if (extant != null && extant.ctx == ctx) {
      if (extant.scope != scope || extant.mod != mod) throw new AssertionError();
      extant.type = type;
      extant.node = node;
      return null;
    }

    // Error if pub or use symbol is redeclared in the same scope
    if (extant != null && extant.scope == scope && extant.ctx != ctx && (
        mod == SymbolModifier.Pub || extant.mod == SymbolModifier.Pub ||
        mod == SymbolModifier.Use || extant.mod == SymbolModifier.Use
    )) {
      return new Parser.Error(ctx, "cannot declare a pub or use symbol more than once in the same scope");
    }

    // Insert the new symbol
    final Symbol symbol = new Symbol(ctx, mod, name, type, node, scope);
    scope.symbols.add(symbol);
    entry.push(symbol);
    return null;
  }
}
