package zfg;

import zfg.antlr.ZfgParser.ExpressionContext;

public final class Parser {

  public ModuleNode parseModule(final ModuleContext ctx) {
    ctx.get
    final List<FunctionNode> functions = ctx.functions.stream()
      .map(this::parseFunction)
      .collect(Collectors.toList());
    return new ModuleNode(name, functions);
  }

  public nodes.Node parseExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case GroupedExprContext expr -> parseGroupedExpr(expr);
      case LiteralExprContext expr -> parseLiteralExpr(expr);
      case InfixOpExprContext expr -> parseInfixOpExpr(expr);
      default -> throw new AssertionError();
    };
  }

  public static final class ModuleNode {
    public final String name;
    public final List<FunctionNode> functions;

    public ModuleNode(final String name, final List<FunctionNode> functions) {
      this.name = name;
      this.functions = functions;
    }
  }
}
