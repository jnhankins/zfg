package zfg.parse.symbol;


import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.parse.node.Node;
import zfg.core.type.Type;

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

