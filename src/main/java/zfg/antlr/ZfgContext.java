package zfg.antlr;

import org.antlr.v4.runtime.ParserRuleContext;

public class ZfgContext extends ParserRuleContext {
  public zfg.Type typed = zfg.Types.UNK;

  public ZfgContext() {
    super();
  }

	public ZfgContext(final ParserRuleContext parent, final int invokingStateNumber) {
		super(parent, invokingStateNumber);
	}
}
