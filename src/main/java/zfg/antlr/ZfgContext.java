package zfg.antlr;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContextWithAltNum;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.runtime.tree.Trees;

public class ZfgContext extends RuleContextWithAltNum {

  public ZfgContext() {
    super();
  }

	public ZfgContext(final ParserRuleContext parent, final int invokingStateNumber) {
		super(parent, invokingStateNumber);
	}

  public final String toStringSyntaxTree() {
    return toStringSyntaxTree(this, false);
  }

  public final String toStringSyntaxTree(final boolean pretty) {
    return toStringSyntaxTree(this, pretty);
  }

  public final String toStringSemanticTree() {
    return toStringSemanticTree(this, false);
  }

  public final String toStringSemanticTree(final boolean pretty) {
    return toStringSemanticTree(this, pretty);
  }

  private static final String toStringSyntaxTree(final ZfgContext root, final boolean pretty) {
    if (pretty) {
      final StringBuilder sb = new StringBuilder();
      toStringSyntaxTree(sb, root, new StringBuilder(), true);
      return sb.toString();
    } else {
      return Trees.toStringTree(root, ruleNames);
    }
  }

  private static final void toStringSyntaxTree(
    final StringBuilder sb,
    final Tree node,
    final StringBuilder indent,
    final boolean isLast
  ) {
    final int indentLength = indent.length();
    // Indent
    if (indentLength > 0) sb.append(indent).append(isLast ? LUR : LVR);
    // Print self
    sb.append(Utils.escapeWhitespace(Trees.getNodeText(node, ruleNames), false));
    // Print children
    final int childCount = node.getChildCount();
    if (childCount > 0) {
      indent.append(isLast ? ' ' : LV);
      for (int c = 0; c < childCount; c++) {
        sb.append('\n');
        toStringSyntaxTree(sb, node.getChild(c), indent, c == childCount - 1);
      }
      indent.deleteCharAt(indentLength);
    }
  }

  private static final String toStringSemanticTree(final ZfgContext root, final boolean pretty) {
    return "TODO"; // TODO
  }

  private static final List<String> ruleNames = Arrays.asList(ZfgParser.ruleNames);
  private static final char LV  = '\u2502'; // │
  private static final char LVR = '\u251C'; // ├
  // private static final char LVL = '\u2524'; // ┤
  // private static final char LH  = '\u2501'; // ─
  private static final char LUR = '\u2514'; // └
  // private static final char LUL = '\u2518'; // ┘
  // private static final char LDR = '\u250C'; // ┌
  // private static final char LDL = '\u2510'; // ┐
}
