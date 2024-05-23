package zfg;

import zfg.Ast.FunDecl;
import zfg.Ast.TypeDecl;
import zfg.Ast.VarDecl;
import zfg.antlr.ZfgContext;

public sealed interface Symbol permits TypeDecl, FunDecl, VarDecl {
  public static enum Modifier { LET, MUT, PUB }
  public ZfgContext ctx();
  public Modifier mod();
  public String name();
  public Type type();
}
