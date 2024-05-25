package zfg.antlr;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuleContextWithAltNum;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ErrorNodeImpl;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.runtime.tree.Trees;

public class ZfgContext extends RuleContextWithAltNum {

  public ZfgContext() {
    super();
  }

  public ZfgContext(final ParserRuleContext parent, final int invokingStateNumber) {
    super(parent, invokingStateNumber);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Dependents
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public ZfgContext[] dependents;
  public int dependentCount;

	public <T extends ZfgContext> T addDependent(final T t) {
    if (dependents == null) {
      dependents = new ZfgContext[4];
    } else if (dependentCount == dependents.length) {
      dependents = Arrays.copyOf(dependents, children.length << 1);
    }
    dependents[dependentCount++] = t;
    return t;
	}

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Parent
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public int parentIndex;

	@Override
	public void setParent(final RuleContext parent) {
    this.parent = parent;
	}

  @Override
  public ZfgContext getParent() {
    return (ZfgContext) this.parent;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Children
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public ParseTree[] children;
  public int childCount;

  @Override
  public <T extends ParseTree> T addAnyChild(final T t) {
    if (children == null) {
      children = new ParseTree[4];
      super.children = new ChildList();
    } else if (childCount == children.length) {
      children = Arrays.copyOf(children, children.length << 1);
    }
    if (t instanceof ZfgContext c) c.parentIndex = childCount;
    children[childCount++] = t;
    return t;
  }

  @Override
  public RuleContext addChild(final RuleContext rule) {
    return addAnyChild(rule);
  }

  @Override
  public TerminalNode addChild(final TerminalNode node) {
    node.setParent(this);
    return addAnyChild(node);
  }

  @Override
  public ErrorNode addErrorNode(final ErrorNode node) {
    node.setParent(this);
    return addAnyChild(node);
  }

  @Override
  @Deprecated
  public TerminalNode addChild(final Token token) {
    return addChild(new TerminalNodeImpl(token));
  }

  @Override
  @Deprecated
  public ErrorNode addErrorNode(final Token badToken) {
    return addErrorNode(new ErrorNodeImpl(badToken));
  }

  @Override
  public void removeLastChild() {
    if (childCount > 0) children[--childCount] = null;
  }

  @Override
  public ParseTree getChild(final int i) {
    return i >= 0 && i < childCount ? children[i] : null;
  }

  @Override
  public <T extends ParseTree> T getChild(final Class<? extends T> ctxType, int i) {
    final int childCount = this.childCount;
    final ParseTree[] children = this.children;
    if (i < 0 || i >= childCount) return null;
    for (int j = 0; j < childCount; j++)
      if (ctxType.isInstance(children[j]) && i-- == 0)
        return ctxType.cast(children[j]);
    return null;
  }

  @Override
  public TerminalNode getToken(final int ttype, int i) {
    final int childCount = this.childCount;
    final ParseTree[] children = this.children;
    if (i < 0 || i >= childCount) return null;
    for (int j = 0; j < childCount; j++)
      if (children[j] instanceof TerminalNode tnode && tnode.getSymbol().getType() == ttype && i-- == 0)
        return tnode;
    return null;
  }

  @Override
  public List<TerminalNode> getTokens(final int ttype) {
    final int childCount = this.childCount;
    final ParseTree[] children = this.children;
    int tokenCount = 0;
    for (int i = 0; i < childCount; i++)
      if (children[i] instanceof TerminalNode tnode && tnode.getSymbol().getType() == ttype)
        tokenCount++;
    if (tokenCount == 0) return Collections.emptyList();
    final TerminalNode[] tokens = new TerminalNode[tokenCount];
    for (int i = 0, j = 0; i < childCount; i++)
      if (children[i] instanceof TerminalNode tnode && tnode.getSymbol().getType() == ttype)
        tokens[j++] = tnode;
    return Arrays.asList(tokens);
  }

  @Override
  public <T extends ParserRuleContext> T getRuleContext(final Class<? extends T> ctxType, final int i) {
    return getChild(ctxType, i);
  }

  public <T extends ParserRuleContext> List<T> getRuleContexts(final Class<? extends T> ctxType) {
    final int childCount = this.childCount;
    final ParseTree[] children = this.children;
    int ruleCount = 0;
    for (int i = 0; i < childCount; i++)
      if (ctxType.isInstance(children[i]))
        ruleCount++;
    if (ruleCount == 0) return Collections.emptyList();
    @SuppressWarnings("unchecked")
    final T[] rules = (T[]) new Object[ruleCount];
    for (int i = 0, j = 0; i < childCount; i++)
      if (ctxType.isInstance(children[i]))
        rules[j++] = ctxType.cast(children[i]);
    return Arrays.asList(rules);
  }

  @Override
  public int getChildCount() {
    return childCount;
  }

  private final class ChildList extends AbstractList<ParseTree> {
    @Override public int size() { return childCount; }
    @Override public boolean isEmpty() { return childCount == 0; }
    @Override public boolean contains(Object o) { return indexOf(o) >= 0; }
    @Override public int indexOf(Object o) {
      for (int i = 0; i < childCount; i++) if (children[i] == o) return i;
      return -1;
    }
    @Override public int lastIndexOf(Object o) {
      for (int i = childCount - 1; i >= 0; i--) if (children[i] == o) return i;
      return -1;
    }
    @Override public ParseTree get(final int idx) {
      if (idx < 0 || idx >= childCount) throw new IndexOutOfBoundsException();
      return children[idx];
    }
    @Override public Object[] toArray() { return Arrays.copyOf(children, childCount); }
    @Override @SuppressWarnings("unchecked") public <T> T[] toArray(T[] a) {
      if (a.length < childCount)
        return Arrays.copyOf(children, childCount, (Class<? extends T[]>) a.getClass());
      System.arraycopy(children, 0, a, 0, childCount);
      if (a.length > childCount) a[childCount] = null;
      return a;
    }
    @Override public Iterator<ParseTree> iterator() { return listIterator(0); }
    @Override public ListIterator<ParseTree> listIterator() { return listIterator(0); }
    @Override public ListIterator<ParseTree> listIterator(final int index) {
      if (index < 0 || index >= childCount) throw new IndexOutOfBoundsException();
      return new ListIterator<ParseTree>() {
        private int cursor = index;
        @Override public boolean hasNext() { return cursor < childCount; }
        @Override public boolean hasPrevious() { return cursor > 0; }
        @Override public int nextIndex() { return cursor; }
        @Override public int previousIndex() { return cursor -1; }
        @Override public ParseTree next() {
          if (cursor >= childCount) throw new IndexOutOfBoundsException();
          return children[cursor++];
        }
        @Override public ParseTree previous() {
          if (cursor <= 0) throw new IndexOutOfBoundsException();
          return children[--cursor];
        }
        @Override public void remove() { throw new UnsupportedOperationException(); }
        @Override public void set(final ParseTree e) { throw new UnsupportedOperationException(); }
        @Override public void add(final ParseTree e) { throw new UnsupportedOperationException(); }
      };
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // To String
  //////////////////////////////////////////////////////////////////////////////////////////////////

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
    final Tree          node,
    final StringBuilder indent,
    final boolean       isLast
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
  private static final char LV  = '\u2502';
  private static final char LVR = '\u251C';
  // private static final char LVL = '\u2524';
  // private static final char LH  = '\u2501';
  private static final char LUR = '\u2514';
  // private static final char LUL = '\u2518';
  // private static final char LDR = '\u250C';
  // private static final char LDL = '\u2510';
}
