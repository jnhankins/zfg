package zfg.parse;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.core.type.Type;

public class symbol {

  // Symbol Modifier
  public static enum Modifier { Let, Mut, Use, Pub }

  // Symbol Table Entry
  public static record Entry(
    DeclarationStmtContext ctx, // context,      for creating useful error messages
    Modifier mod,               // modifier,     for checking if the symbol can be redeclared
    String id,                  // identifier,   for looking up the symbol by name
    Type type,                  // type,         for type checking
    Scope scope,                // scope,        for checking if the symbol can be hidden
    node.Node node              // null or node, for storing the symbol's "value"
  ) {}

  // Symbol Scope
  public static record Scope(List<Entry> symbols) {}

  // Symbol Table
  public static final class Table {
    private final Map<String, Deque<Entry>> table  = new HashMap<>();
    private final Deque<Scope>              scopes = new ArrayDeque<>();

    // Create a new scope and push it onto the stack
    public void pushScope() {
      final Scope scope = new Scope(new ArrayList<>());
      scopes.push(scope);
    }

    // Pop a scope off the stack, remove its symbols from the symbol table, and return it
    public Scope popScope() {
      final Scope scope = scopes.pop();
      for (final Entry symbol : scope.symbols) {
        final Deque<Entry> entries = table.get(symbol.id);
        entries.pop();
        if (entries.isEmpty()) table.remove(symbol.id);
      }
      return scope;
    }

    // Add a symbol to the current scope.
    // Returns null on success, otherwise returns aparser error.
    public Parser.Error addSymbol(
      final DeclarationStmtContext ctx,
      final Modifier mod,
      final String id,
      final Type type,
      final node.Node node // <-- nullable
    ) {
      // Get the row for the symbol's identifier
      final Deque<Entry> entries = table.computeIfAbsent(id, k -> new ArrayDeque<>());

      // Check if there is an existing symbol that
      //  (a) has the same identifier,
      //  (b) is in the same scope, and
      //  (c) has a modifier that disallows redeclaration.
      final Entry extant = entries.peek();
      if (extant != null &&                                           // (a)
          extant.scope == scopes.peek() &&                            // (b)
          (extant.mod == Modifier.Pub || extant.mod == Modifier.Use)  // (c)
      ) return new Parser.Error(ctx, String.format(
        "cannot redeclare symbol \"%s\" previously declared with modifier \"%s\" in the same scope on line %d column %d",
        id, extant.ctx.mod.getText(), extant.ctx.getStart().getLine(), extant.ctx.getStart().getCharPositionInLine()
      ));

      // Create the symbol entry, add it to the current scope, and add it to the symbol table
      final Scope scope = scopes.peek();
      final Entry symbol = new Entry(ctx, mod, id, type, scope, node);
      scope.symbols.add(symbol);
      entries.push(symbol);
      return null;
    }
  }
}
