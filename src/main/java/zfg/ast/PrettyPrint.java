package zfg.ast;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.runtime.tree.Trees;

public class PrettyPrint {

  public static String toPrettyTokensString(final Lexer lexer, final BufferedTokenStream tokens) {
    final Vocabulary voc = lexer.getVocabulary();
    tokens.fill();
    final StringBuilder buf = new StringBuilder();
    buf.append('[');
    for (final Token token : tokens.getTokens()) {
      if (token.getChannel() == Token.DEFAULT_CHANNEL) {
        final int type = token.getType();
        final boolean isLiteral = voc.getLiteralName(type) != null;
        if (buf.length() > 1) {
          buf.append(", ");
        }
        buf.append(voc.getSymbolicName(type));
        if (!isLiteral) {
          buf.append('(');
          buf.append(token.getText());
          buf.append(')');
        }
      }
    }
    buf.append(']');
    return buf.toString();
  }

  public static String toPrettyTreeString(final Parser parser, final Tree root) {
    final StringBuilder buf = new StringBuilder();
    toPrettyTreeString(Arrays.asList(parser.getRuleNames()), buf, root, "", true);
    return buf.toString();
  }

  private static void toPrettyTreeString(
      final List<String> ruleNames,
      final StringBuilder buf,
      final Tree node,
      final String indent,
      final boolean isLastChild) {
    if (!indent.isEmpty()) {
      buf.append(indent);
      buf.append(isLastChild ? "\u2514" : "\u251C");
    }
    buf.append(Utils.escapeWhitespace(Trees.getNodeText(node, ruleNames), false));
    final int childCount = node.getChildCount();
    if (childCount > 0) {
      final String childIndent = indent + (isLastChild ? " " : "\u2502");
      for (int i = 0; i < childCount; i++) {
        buf.append('\n');
        toPrettyTreeString(ruleNames, buf, node.getChild(i), childIndent, i == childCount - 1);
      }
    }
  }
}
