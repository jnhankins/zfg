package zfg.antlr;

import java.io.Serializable;

import org.abego.treelayout.internal.util.java.lang.string.StringUtil;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.WritableToken;
import org.antlr.v4.runtime.misc.Interval;

public final class ZfgToken implements WritableToken, Serializable {
	public int type       = 0;
	public int line       = 0;
	public int column     = -1;
	public int channel    = DEFAULT_CHANNEL;
	public int tokenIndex = -1;
	public int startIndex = 0;
	public int stopIndex  = 0;
	public String text             = null;
	public TokenSource tokenSource = null;
  public CharStream inputStream  = null;

	public ZfgToken(final int type) {
		this.type = type;
	}

	public ZfgToken(final int type, final String text) {
		this.type = type;
		this.text = text;
	}

	public ZfgToken(
    final int type,
		final int line,
    final int column,
    final int channel,
    final int tokenIndex,
    final int startIndex,
    final int stopIndex,
    final String text,
    final TokenSource tokenSource,
    final CharStream inputStream
  ) {
    this.type        = type;
    this.line        = line;
    this.column      = column;
    this.channel     = channel;
    this.tokenIndex  = tokenIndex;
    this.startIndex  = startIndex;
    this.stopIndex   = stopIndex;
    this.text        = text;
    this.tokenSource = tokenSource;
    this.inputStream = inputStream;
	}

	public ZfgToken(final Token token) {
    switch (token) {
      case ZfgToken tok -> {
        type        = tok.type;
        line        = tok.line;
        column      = tok.column;
        channel     = tok.channel;
        tokenIndex  = tok.tokenIndex;
        startIndex  = tok.startIndex;
        stopIndex   = tok.stopIndex;
        text        = tok.text;
        tokenSource = tok.tokenSource;
        inputStream = tok.inputStream;
      }
      case Token tok -> {
        type        = tok.getType();
        line        = tok.getLine();
        column   = tok.getCharPositionInLine();
        channel     = tok.getChannel();
        tokenIndex  = tok.getTokenIndex();
        startIndex  = tok.getStartIndex();
        stopIndex   = tok.getStopIndex();
        text        = tok.getText();
        tokenSource = tok.getTokenSource();
        inputStream = tok.getInputStream();
      }
    }
	}

  @Override public String getText() {
    if (text != null) return text;
    if (inputStream == null) return null;
    final int size = inputStream.size();
    return text = startIndex < size && stopIndex < size
      ? inputStream.getText(Interval.of(startIndex, stopIndex))
      : "<EOF>";
  }

  @Override public int getType() { return type;  }
  @Override public int getLine() { return line;}
  @Override public int getCharPositionInLine() { return column; }
  @Override public int getChannel() { return channel; }
  @Override public int getTokenIndex() { return tokenIndex; }
  @Override public int getStartIndex() { return startIndex; }
  @Override public int getStopIndex() { return stopIndex; }
  @Override public TokenSource getTokenSource() { return tokenSource; }
  @Override public CharStream getInputStream() { return inputStream; }
  @Override public void setText(final String text) { this.text = text; }
  @Override public void setType(final int type) { this.type = type; }
  @Override public void setLine(final int line) { this.line = line; }
  @Override public void setCharPositionInLine(final int column) { this.column = column; }
  @Override public void setChannel(int channel) { this.channel = channel; }
  @Override public void setTokenIndex(int tokenIndex) { this.tokenIndex = tokenIndex; }
  public void setStartIndex(final int startIndex) { this.startIndex = startIndex; }
  public void setStopIndex(final int stopIndex) { this.stopIndex = stopIndex; }

  @Override
  public String toString() {
    return toString(ZfgLexer.VOCABULARY);
  }
  public String toString(final Recognizer<?, ?> recognizer) {
    return toString(recognizer == null ? ZfgLexer.VOCABULARY : recognizer.getVocabulary());
  }
	public String toString(final Vocabulary vocabulary) {
    return new StringBuilder()
      .append("[@")
      .append(tokenIndex)
      .append(',')
      .append(startIndex)
      .append(':')
      .append(stopIndex)
      .append("=")
      .append(getText() instanceof String txt ? StringUtil.quote(txt) : "<no text>")
      .append(",<")
      .append(vocabulary == null ? type : vocabulary.getDisplayName(type))
      .append('>')
      .append(channel > 0 ? ",channel=" + channel : "")
      .append(',')
      .append(line)
      .append(':')
      .append(column)
      .append(']')
      .toString();
	}

}
