package zfg;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

public final class PrettyPrint {
  private PrettyPrint() {}

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Lexical Token List
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static String tokenStream(final Lexer lexer, final BufferedTokenStream tokens) {
    final Vocabulary vocab = lexer.getVocabulary();
    final StringBuilder sb = new StringBuilder();
    sb.append('[');
    tokens.fill();
    for (final Token token : tokens.getTokens()) {
      if (token.getChannel() != Token.DEFAULT_CHANNEL) continue;
      if (sb.length() > 1) sb.append(", ");
      final int type = token.getType();
      sb.append(vocab.getSymbolicName(type));
      if (vocab.getLiteralName(type) == null) sb.append('(').append(token.getText()).append(')');
    }
    sb.append(']');
    return sb.toString();
  }
}
