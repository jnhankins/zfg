package zfg;

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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Syntax Tree
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static String syntaxTree(final Parser parser, final Tree root) {
    final List<String> rn = Arrays.asList(parser.getRuleNames());
    final StringBuilder sb = new StringBuilder();
    syntaxTree(rn, sb, root, "", true);
    return sb.toString();
  }

  private static void syntaxTree(
      final List<String>  rn,
      final StringBuilder sb,
      final Tree    node,
      final String  indent,
      final boolean isLast
  ) {
    if (!indent.isEmpty()) sb.append(indent).append(isLast ? "\u2514" : "\u251C");
    sb.append(Utils.escapeWhitespace(Trees.getNodeText(node, rn), false));
    final int childCount = node.getChildCount();
    if (childCount > 0) {
      final String childIndent = indent + (isLast ? " " : "\u2502");
      for (int i = 0; i < childCount; i++) {
        sb.append('\n');
        syntaxTree(rn, sb, node.getChild(i), childIndent, i == childCount - 1);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Semantic Tree
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static String semanticTree(final Ast node) {
    final StringBuilder sb = new StringBuilder();
    semanticTree(sb, node, "", true);
    return sb.toString();
  }

  private static void semanticTree(
      final StringBuilder sb,
      final Ast     node,
      final String  indent,
      final boolean isLast
  ) {
    // TODO
    node.toString(sb);
  }
}
