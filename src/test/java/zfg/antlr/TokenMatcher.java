
// @formatter:off
package zfg.antlr;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class TokenMatcher {
  private int type;
  private String text = null;
  private Integer line = null;
  private Integer charPositionInLine = null;
  private Integer channel = null;
  private Integer tokenIndex = null;
  private Integer startIndex = null;
  private Integer stopIndex = null;

  // Constructors
  public static TokenMatcher token(final int type) { return new TokenMatcher(type); }
  public TokenMatcher(final int type) { this.type = type; }

  // Getters
  public TokenMatcher text(final String text) { this.text = text; return this; }
  public TokenMatcher type(final int type) { this.type = type; return this; }
  public TokenMatcher line(final int line) { this.line = line; return this; }
  public TokenMatcher charPositionInLine(final int charPositionInLine) { this.charPositionInLine = charPositionInLine; return this; }
  public TokenMatcher channel(final int channel) { this.channel = channel; return this; }
  public TokenMatcher tokenIndex(final int tokenIndex) { this.tokenIndex = tokenIndex; return this; }
  public TokenMatcher startIndex(final int startIndex) { this.startIndex = startIndex; return this; }
  public TokenMatcher stopIndex(final int stopIndex) { this.stopIndex = stopIndex; return this; }

  public static String expectedToString(final TokenMatcher matcher) {
    final StringBuilder str = new StringBuilder();
    str.append("Token(");
    str.append("type:").append(ZfgLexer.VOCABULARY.getSymbolicName(matcher.type));
    if (matcher.text != null) str.append(", text=").append(matcher.text);
    if (matcher.line != null) str.append(", line=").append(matcher.line);
    if (matcher.charPositionInLine != null) str.append(", charPositionInLine=").append(matcher.charPositionInLine);
    if (matcher.channel != null) str.append(", channel=").append(matcher.channel);
    if (matcher.tokenIndex != null) str.append(", tokenIndex=").append(matcher.tokenIndex);
    if (matcher.startIndex != null) str.append(", startIndex=").append(matcher.startIndex);
    if (matcher.stopIndex != null) str.append(", stopIndex=").append(matcher.stopIndex);
    str.append(")");
    return str.toString();
  }

  public static String actualToString(final Token actual, final TokenMatcher matcher) {
    final StringBuilder str = new StringBuilder();
    str.append("Token(");
    str.append("type:").append(ZfgLexer.VOCABULARY.getSymbolicName(actual.getType()));
    if (matcher != null) {
      if (matcher.text != null) str.append(", text=").append(actual.getText());
      if (matcher.line != null) str.append(", line=").append(actual.getLine());
      if (matcher.charPositionInLine != null) str.append(", charPositionInLine=").append(actual.getCharPositionInLine());
      if (matcher.channel != null) str.append(", channel=").append(actual.getChannel());
      if (matcher.tokenIndex != null) str.append(", tokenIndex=").append(actual.getTokenIndex());
      if (matcher.startIndex != null) str.append(", startIndex=").append(actual.getStartIndex());
      if (matcher.stopIndex != null) str.append(", stopIndex=").append(actual.getStopIndex());
    }
    str.append(")");
    return str.toString();
  }

  public static String expectedToString(final List<TokenMatcher> expected) {
    return String.format("[%s]",
      expected.stream().map(TokenMatcher::expectedToString).collect(Collectors.joining(", "))
    );
  }

  public static String actualToString(final List<Token> actual, final List<TokenMatcher> expected) {
    return String.format("[%s]", IntStream.range(0, actual.size()).mapToObj(i -> {
        final Token token = actual.get(i);
        final TokenMatcher matcher = i < expected.size() ? expected.get(i) : null;
        return actualToString(token, matcher);
      }).collect(Collectors.joining(", "))
    );
  }

  public static void assertToken(final TokenMatcher expected, final Token actual) {
    assertAll("Token mismatch: expected: " + expectedToString(expected) + " but was: " + actualToString(actual, expected),
      () -> { assertEquals(ZfgLexer.VOCABULARY.getSymbolicName(expected.type), ZfgLexer.VOCABULARY.getSymbolicName(actual.getType()), "type"); },
      () -> { if (expected.text != null) assertEquals(expected.text, actual.getText(), "text"); },
      () -> { if (expected.line != null) assertEquals(expected.line, actual.getLine(), "line"); },
      () -> { if (expected.charPositionInLine != null) assertEquals(expected.charPositionInLine, actual.getCharPositionInLine(), "charPositionInLine"); },
      () -> { if (expected.channel != null) assertEquals(expected.channel, actual.getChannel(), "channel"); },
      () -> { if (expected.tokenIndex != null) assertEquals(expected.tokenIndex, actual.getTokenIndex(), "tokenIndex"); },
      () -> { if (expected.startIndex != null) assertEquals(expected.startIndex, actual.getStartIndex(), "startIndex"); },
      () -> { if (expected.stopIndex != null) assertEquals(expected.stopIndex, actual.getStopIndex(), "stopIndex"); }
    );
  }

  public static void assertTokens(final List<TokenMatcher> expected, final List<Token> actual) {
    if (expected.size() != actual.size()) {
      fail("expected: " + expectedToString(expected) + " but was: " + actualToString(actual, expected));
    }
    assertAll(IntStream.range(0, actual.size()).mapToObj((i) -> {
      final Token token = actual.get(i);
      final TokenMatcher matcher = expected.get(i);
      return () -> assertAll("index: " + i, () -> assertToken(matcher, token));
    }));
  }

  @SuppressWarnings("unchecked")
  public static void assertTokens(final String source, final TokenMatcher... expected) {
    assertAll(source, () -> {
      final ZfgLexer lexer = new ZfgLexer(CharStreams.fromString(source));
      assertTokens(Arrays.asList(expected), (List<Token>) lexer.getAllTokens());
    });
  }
}
