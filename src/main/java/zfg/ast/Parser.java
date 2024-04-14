package zfg.ast;

import java.nio.file.Path;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AssignExprContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.GroupExprContext;
import zfg.antlr.ZfgParser.InfixExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.PathExprContext;
import zfg.antlr.ZfgParser.PostfixExprContext;
import zfg.antlr.ZfgParser.PrefixExprContext;
import zfg.antlr.ZfgParser.StartContext;

public class Parser {

  public static Ast parse(final Path path) {
    try {
      return parse(CharStreams.fromPath(path));
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Ast parse(final String source, final String sourceName) {
    return parse(CharStreams.fromString(source, sourceName));
  }

  public static Ast parse(final CharStream source) {
    System.out.println("source: " + source.getSourceName());
    System.out.println(">" + source.toString().replaceAll("\\r?\\n", "\n>"));

    final ZfgLexer lexer = new ZfgLexer(source);
    final CommonTokenStream tokens = new CommonTokenStream(lexer);
    System.out.println("tokens: " + PrettyPrint.toPrettyTokensString(lexer, tokens));

    final ZfgParser parser = new ZfgParser(tokens);
    final StartContext start = parser.start();
    System.out.println("parsed: " + start.toStringTree(parser));
    System.out.println("tree:\n" + PrettyPrint.toPrettyTreeString(parser, start));

    return null;
  }

  public static Expr visitExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case PathExprContext sub -> visitPathExpr(sub);
      case LiteralExprContext sub -> visitLiteralExpr(sub);
      case GroupExprContext sub -> visitGroupExpr(sub);
      case PostfixExprContext sub -> visitPostfixExpr(sub);
      case PrefixExprContext sub -> visitPrefixExpr(sub);
      case InfixExprContext sub -> visitInfixExpr(sub);
      case AssignExprContext sub -> visitAssignExpr(sub);
      default -> throw newUnexpectedSubtypeException(ctx);
    };
  }

  public static Expr visitPathExpr(final PathExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }

  public static Expr visitLiteralExpr(final LiteralExprContext ctx) {
    switch (ctx.lit.getType()) {
      case ZfgParser.BitLit -> visitBitLiteralExpr(ctx);
      case ZfgParser.IntLit -> visitIntLiteralExpr(ctx);
      case ZfgParser.FltLit -> visitFltLiteralExpr(ctx);
      default -> throw new RuntimeException();
    }
  }

  public static Expr visitBitLiteralExpr(final LiteralExprContext ctx) {
    switch (ctx.lit.getText()) {
      case "true" -> throw new UnsupportedOperationException("TODO");
      case "false" -> throw new UnsupportedOperationException("TODO");
      default -> throw new RuntimeException();
    }
  }

  public static Expr visitIntLiteralExpr(final LiteralExprContext ctx) {
    final String text = ctx.lit.getText();
    throw new UnsupportedOperationException("TODO");
  }

  public static Expr visitFltLiteralExpr(final LiteralExprContext ctx) {
    final String text = ctx.lit.getText();
    throw new UnsupportedOperationException("TODO");
  }

  public static Expr visitGroupExpr(final GroupExprContext ctx) {
    return visitExpression(ctx.expression());
  }

  public static Expr visitPostfixExpr(final PostfixExprContext ctx) {
    switch (ctx.op.getType()) {
      case ZfgParser.INC -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.DEC -> throw new UnsupportedOperationException("TODO");
      default -> throw new RuntimeException();
    }
  }

  public static Expr visitPrefixExpr(final PrefixExprContext ctx) {
    switch (ctx.op.getType()) {
      case ZfgParser.INC -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.DEC -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.ADD -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.SUB -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.NOT -> throw new UnsupportedOperationException("TODO");
      default -> throw new RuntimeException();
    }
  }

  public static Expr visitInfixExpr(final InfixExprContext ctx) {
    switch (ctx.op.getType()) {
      case ZfgParser.MUL -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.DIV -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.REM -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.MOD -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.ADD -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.SUB -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.SHL -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.SHR -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.AND -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.XOR -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.IOR -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.CMP -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.LT -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.GT -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.LE -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.GE -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.EQ -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.NE -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.EQR -> throw new UnsupportedOperationException("TODO");
      case ZfgParser.NER -> throw new UnsupportedOperationException("TODO");
      default -> throw new RuntimeException();
    }
  }

  public static Expr visitAssignExpr(final AssignExprContext ctx) {
    throw new UnsupportedOperationException("TODO");
  }
}
