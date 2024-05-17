package zfg.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

public final class ZfgTokenFactory implements org.antlr.v4.runtime.TokenFactory<ZfgToken> {

	@Override
	public ZfgToken create(
    final Pair<TokenSource, CharStream> source,
    final int type,
    final String text,
    final int channel,
    final int startIndex,
    final int stopIndex,
		final int line,
    final int charPositionInLine
  ) {
    return new ZfgToken(
      type,
      line,
      charPositionInLine,
      channel,
      -1,
      startIndex,
      stopIndex,
      text,
      source.a,
      source.b
    );
	}

	@Override
	public ZfgToken create(final int type, final String text) {
    return new ZfgToken(type, text);
	}

}
