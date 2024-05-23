package zfg;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;

import zfg.Symbol.Modifier;
import zfg.antlr.ZfgContext;

public final class SymbolTable {
  private static final class SymbolScope extends ArrayList<Symbol> {}
  private final ArrayDeque<SymbolScope> scopes = new ArrayDeque<>();
  private final HashMap<String, Deque<Symbol>> symbolTable = new HashMap<>();
  private final HashMap<ZfgContext, Symbol> ctxSymbolMap = new HashMap<>();

  public void pushScope() {
    scopes.push(new SymbolScope());
  }

  public void popScope() {
    // Pop the scope
    final SymbolScope scope = scopes.pop();
    // For each symbol in the scope...
    final int scopeSize = scope.size();
    for (int i = 0; i < scopeSize; i++) {
      final Symbol symbol = scope.get(i);
      // Get the symbol's table row
      final String symbolName = symbol.name();
      final Deque<Symbol> symbolTableRow = symbolTable.get(symbolName);
      assert symbolTableRow.peek() == symbol;
      // Pop the symbol off the table row
      if (symbolTableRow.size() > 1) {
        symbolTableRow.pop();
      } else {
        symbolTable.remove(symbolName);
      }
      // Remove the symbol from the context-symbol map
      assert ctxSymbolMap.get(symbol.ctx()) == symbol;
      ctxSymbolMap.remove(symbol.ctx());
    }
  }

  public void declare(final Symbol symbol, final ParserErrorEmitter emit) {
    assert symbol != null;
    assert emit != null;
    assert !ctxSymbolMap.containsKey(symbol.ctx());
    // Get the symbol's name
    final String name = symbol.name();
    // Get the symbol's scope
    final SymbolScope scope = scopes.peek();
    // Validate that PUB symbols are only declared the global scope or a module scope
    if (symbol.mod() == Modifier.PUB && scopes.size() > 2) {
      emit.err(symbol.ctx(), "Public symbols must be declared in a module scope.");
    }
    // Get the symbol's table row
    final Deque<Symbol> row = symbolTable.computeIfAbsent(name, k -> new ArrayDeque<>());
    // Get the symbol, if any, that will be shadowed by the new symbol
    final Symbol hidden = row.peek();
    // Validate that PUB symbols are never shadowed
    if (hidden != null && hidden.mod() == Modifier.PUB) {
      emit.err(symbol.ctx(), "Cannot shadow a public symbol.");
    }
    // Add the symbol to it's row (shadowing the other symbols in the row)
    row.push(symbol);
    // Add the symbol to the scope
    scope.add(symbol);
    // Add the symbol to the context-symbol map
    ctxSymbolMap.put(symbol.ctx(), symbol);
  }

  public Symbol getSymbol(final String name) {
    final Deque<Symbol> row = symbolTable.get(name);
    return row == null ? null : row.peek();
  }

  public Symbol getSymbol(final ZfgContext ctx) {
    return ctxSymbolMap.get(ctx);
  }
}
