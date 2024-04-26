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

    // Creates a new scope and pushes it onto the stack
    public void pushScope() {
      final Scope scope = new Scope(new ArrayList<>());
      scopes.push(scope);
    }

    // Pops a scope off the stack, removes its symbols from the symbol table, and returns it
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
    // Returns null if successfull, otherwise return an error.
    public Parser.Error addSymbol(
      final DeclarationStmtContext ctx,
      final Modifier mod,
      final String id,
      final Type type,
      final node.Node node // <-- nullable
    ) {

      // Get the current scope
      final Scope scope = scopes.peek();

      // Get the row for the symbol's identifier
      final Deque<Entry> entries = table.computeIfAbsent(id, k -> new ArrayDeque<>());

      // Check if the symbol is already in the table
      final Entry extant = entries.peek();

      // Ensure pub symbols are declared at the root of a module
      if (mod == Modifier.Pub && scopes.size() != 1) {
        return new Parser.Error(ctx, "a public symbol can only be declared at the root of a module");
      }

      // Ensure that pub and use symbols are not redeclared in the same scope
      if (extant != null && extant.scope == scope && extant.ctx != ctx && (
          mod == Modifier.Pub || mod == Modifier.Use ||
          extant.mod == Modifier.Pub || extant.mod == Modifier.Use
      )) return new Parser.Error(ctx, String.format(
        "cannot declare symbol \"%s\" with modifier \"%s\" because it is defined elsewhere in the same scope with modifier \"%s\" on line %d column %d",
        id, ctx.mod.getText(), extant.ctx.mod.getText(), extant.ctx.getStart().getLine(), extant.ctx.getStart().getCharPositionInLine())
      );

      // Check if we're updating the extant symbol
      if (extant != null && extant.ctx == ctx) {
        // Sanity check
        if (extant.mod != mod || extant.scope != scope) throw new AssertionError();
        // Check if it's a no-op
        if (extant.type == type && extant.node == node) return null;
        // TODO: Update the symbol with the new type and/or node
        throw new UnsupportedOperationException("TODO: implement symbol updates");
      }

      // Create the symbol entry, add it to the current scope, and add it to the symbol table
      final Entry symbol = new Entry(ctx, mod, id, type, scope, node);
      scope.symbols.add(symbol);
      entries.push(symbol);
      return null;
    }

    // Returns the symbol entry for the given identifier if one exists in the symbol table,
    // otherwise returns null.
    public Entry getSymbol(final String id) {
      final Deque<Entry> entries = table.get(id);
      return entries == null ? null : entries.peek();
    }
  }
}
