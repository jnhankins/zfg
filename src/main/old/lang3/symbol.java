package zfg.old.lang3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zfg.antlr.ZfgParser.DeclarationStmtContext;

public final class symbol {
  private symbol() {}

  public static enum Kind { Inst, Type; }

  // Symbol table entry for a symbol
  public static final class Entry {
    public final DeclarationStmtContext ctx;   // Source code context

    public final String                 name;  // Symbol name
    public final Scope                  scope; // Lexical scope

    private Entry(
      final DeclarationStmtContext ctx,
      final String                 name,
      final Scope                  scope
    ) {
      assert ctx != null;
      assert name != null;
      assert scope != null;
      this.ctx = ctx;
      this.name = name;
      this.scope = scope;
    }
  }

  // Lexical scope
  public static final class Scope {
    final List<Entry> symbols = new ArrayList<>();
  }

  // Symbol table
  public static final class Table {
    private final Map<String, Deque<Entry>> table = new HashMap<>();
    private final Deque<Scope>              stack = new ArrayDeque<>();

    public Entry get(final String name) {
      final Deque<Entry> entries = table.get(name);
      return entries == null ? null : entries.peek();
    }

    public void pushScope() {
      stack.push(new Scope());
    }

    public Scope popScope() {
      final Scope scope = stack.pop();
      final List<Entry> symbols = scope.symbols;
      final int size = symbols.size();
      for (int i = 0; i < size; i++) {
        final Entry entry = symbols.get(i);
        final Deque<Entry> entries = table.get(entry.name);
        final Entry extant = entries.pop();
        assert extant == entry;
        if (entries.isEmpty()) table.remove(entry.name);
      }
      return scope;
    }
  }
}
