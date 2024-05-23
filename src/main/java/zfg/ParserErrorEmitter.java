package zfg;

import zfg.antlr.ZfgContext;

@FunctionalInterface
public interface ParserErrorEmitter {
  void err(final ZfgContext ctx, final String msg);
}
