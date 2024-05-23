package zfg;

import zfg.antlr.ZfgContext;
import zfg.antlr.ZfgToken;

public final class ParserError {
  public final ZfgContext ctx;
  public final String msg;

  public ParserError(final ZfgContext ctx, final String msg) {
    this.ctx = ctx;
    this.msg = msg;
  }

  @Override
  public String toString() {
    final int line = ((ZfgToken) ctx.start).line;
    final int column = ((ZfgToken) ctx.start).column;
    return String.format("%d:%d: %s", line, column, msg);
  }
}
