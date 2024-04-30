package zfg.old.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import zfg.old.ast.Type.Val;

public sealed interface Symbol {

  public static final class LocVar implements Symbol {
    final String  name;
    final Type    type;
    final boolean immu;
    final int     offset;
    LocVar(final String name, final Type type, final boolean immu, final int offset) {
      this.name = name;
      this.type = type;
      this.immu = immu;
      this.offset = offset;
    }
    public final int offset() { return offset; }
  }

  public static final class Table {
    private final Stack<List<LocVar>>        frames = new Stack<>();
    private final Stack<Map<String, LocVar>> scopes = new Stack<>();

    public final void pushFrame() {
      frames.push(new ArrayList<>());
      scopes.push(new HashMap<>());
    }

    public final List<LocVar> popFrame() {
      scopes.pop();
      return frames.pop();
    }

    public final void pushScope() {
      scopes.push(new HashMap<>());
    }

    public final void popScope() {
      frames.pop();
    }

    public final Symbol get(final String name) {
      for (final Map<String, LocVar> scope : scopes) {
        final LocVar symbol = scope.get(name);
        if (symbol != null) return symbol;
      }
      return null;
    }

    public final Symbol put(final String name, final Type type, final boolean immu) {
      // Put occurs when we're creating a variable in the current scope.
      // The current scope could be a module or a function.

      if (name.contains(".")) throw new UnsupportedOperationException("TODO ASAP");
      final List<LocVar>        frame = frames.peek();
      final Map<String, LocVar> scope = scopes.peek();
      if (frame == null) throw new UnsupportedOperationException("TODO ASAP");

      final int offset;
      if (frame.isEmpty()) {
        offset = 0;
      } else {
        final LocVar last = frame.get(frame.size() - 1);
        offset = last.offset + last.type.fsize();
      }
      final LocVar symbol = new LocVar(name, type, immu, offset);
      frame.add(symbol);
      scope.put(name, symbol);
      return symbol;
    }
  }
}
