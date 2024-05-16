package zfg;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

import org.antlr.v4.runtime.ParserRuleContext;

public sealed abstract class Symbol {
  public final ParserRuleContext ctx;
  public final String name;
  public final Type type;

  public Symbol(final ParserRuleContext ctx, final String name, final Type type) {
    assert ctx != null;
    assert name != null;
    assert type != null || this instanceof Moudle;
    assert type != Types.ERR;
    this.ctx = ctx;
    this.name = name;
    this.type = type;
  }
  public static final class Moudle extends Symbol {
    public Moudle(final ParserRuleContext ctx, final String name) {
      super(ctx, name, null);
    }
  }
  public static final class TypeDefn extends Symbol {
    public final Type.Nom type;
    public TypeDefn(final ParserRuleContext ctx, final String name, final Type.Nom type) {
      super(ctx, name, type);
      this.type = type;
    }
  }
  // public static final class Constant extends Symbol {
  //   public final Ast.ConstExpr val;
  //   public Constant(final ParserRuleContext ctx, final String name, final Ast.ConstExpr val) {
  //     super(ctx, name, val.type());
  //     assert val != null;
  //     this.val = val;
  //   }
  // }
  public static final class Variable extends Symbol {
    public final Ast.VarRef var;
    public Variable(final ParserRuleContext ctx, final String name, final Ast.VarRef var) {
      super(ctx, name, var.type);
      assert var != null;
      this.var = var;
    }
  }
  public static final class Function extends Symbol {
    public final Ast.FunRef fun;
    public Function(final ParserRuleContext ctx, final String name, final Ast.FunRef fun) {
      super(ctx, name, fun.type);
      assert fun != null;
      this.fun = fun;
    }
  }


  public static sealed abstract class Scope extends ArrayDeque<Symbol> {
    public static final class Global extends Scope {}
    public static final class Module extends Scope {}
    public static final class Method extends Scope {}
    public static final class Nested extends Scope {}
  }

  public static final class Table {
    private final HashMap<String, Deque<Symbol>> table = new HashMap<>();
    private final Deque<Scope> scopes = new ArrayDeque<>();

    public void pushGlobalScope() {
      assert scopes.isEmpty();
      scopes.push(new Scope.Global());
    }
    public void pushModuleScope() {
      assert scopes.peek() instanceof Scope.Global;
      scopes.push(new Scope.Module());
    }
    public void pushMethodScope() {
      assert scopes.peek() instanceof Scope.Module;
      scopes.push(new Scope.Method());
    }
    public void pushNestedScope() {
      assert scopes.peek() instanceof Scope.Method || scopes.peek() instanceof Scope.Nested;
      scopes.push(new Scope.Nested());
    }


    private Scope popScope() {
      // Pop the scope off the stack
      final Scope scope = scopes.pop();
      // Remove all the scope's symbols from the table
      for (final Symbol symbol : scope) {
        final String name = symbol.name;
        final Deque<Symbol> deque = table.get(name);
        assert deque != null && deque.peek() == symbol;
        if (deque.size() > 1) {
          deque.pop();
        } else {
          table.remove(name);
        }
      }
      // Return the popped scope
      return scope;
    }
    public Scope.Global popGlobalScope() {
      assert scopes.peek() instanceof Scope.Global;
      return (Scope.Global) popScope();
    }
    public Scope.Module popModuleScope() {
      assert scopes.peek() instanceof Scope.Module;
      return (Scope.Module) popScope();
    }
    public Scope.Method popMethodScope() {
      assert scopes.peek() instanceof Scope.Method;
      return (Scope.Method) popScope();
    }
    public Scope.Nested popNestedScope() {
      assert scopes.peek() instanceof Scope.Nested;
      return (Scope.Nested) popScope();
    }


    private void pushSymbol(final Symbol symbol) {
      scopes.peek().push(symbol);
      table.computeIfAbsent(symbol.name, k -> new ArrayDeque<>()).push(symbol);
    }
    public TypeDefn pushTypeDefn(
      final ParserRuleContext ctx,
      final String name,
      final Type.Nom type
    ) {
      final TypeDefn symbol = new TypeDefn(ctx, name, type);
      pushSymbol(symbol);
      return symbol;
    }
    public Function pushFunction(
      final ParserRuleContext ctx,
      final String name,
      final Ast.FunRef funRef
    ) {
      final Function symbol = new Function(ctx, name, funRef);
      pushSymbol(symbol);
      return symbol;
    }
    public Variable pushVariable(
      final ParserRuleContext ctx,
      final String name,
      final Ast.VarRef varRef
    ) {
      final Variable symbol = new Variable(ctx, name, varRef);
      pushSymbol(symbol);
      return symbol;
    }

    public Symbol getSymbol(final String name) {
      final Deque<Symbol> deque = table.get(name);
      return deque == null ? null : deque.peek();
    }
  }
}


