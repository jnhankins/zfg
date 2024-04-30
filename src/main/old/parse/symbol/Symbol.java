package zfg.old.parse.symbol;


import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.old.core.type.Type;
import zfg.old.parse.node.Node;

public final class Symbol {
  public final DeclarationStmtContext ctx;
  public final SymbolModifier         mod;
  public final String                 name;
  public final SymbolScope            scope;
  public       Type                   type;
  public       Node                   node; // nullable

  public Symbol(
    final DeclarationStmtContext ctx,
    final SymbolModifier         mod,
    final String                 name,
    final Type                   type,
    final Node                   node, // nullable
    final SymbolScope            scope
  ) {
    this.ctx  = ctx;
    this.mod  = mod;
    this.name = name;
    this.type = type;
    this.node = node;
    this.scope = scope;
  }
}

