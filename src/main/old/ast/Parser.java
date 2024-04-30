package zfg.old.ast;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.CompilationUnitContext;

public final class Parser {
  public static final record Error(ParserRuleContext ctx, String msg) {
    @Override
    public String toString() {
      return ctx.getStart().getLine() + ":" + ctx.getStart().getCharPositionInLine() + ": " + msg;
    }
  }

  public static interface Result {
    public static final record Val(Node value) implements Result {}
    public static final record Err(List<Error> errors) implements Result {}
  }

  public static Result parse(final Path path) {
    try { return parse(CharStreams.fromPath(path)); }
    catch (final Exception e) { throw new RuntimeException(e); }
  }

  public static Result parse(final String source, final String sourceName) {
    return parse(CharStreams.fromString(source, sourceName));
  }

  public static Result parse(final CharStream source) {
    System.out.println("source: " + source.getSourceName());
    System.out.println(">" + source.toString().replaceAll("\\r?\\n", "\n>"));

    // Lexical Analysis
    final ZfgLexer zfgLexer = new ZfgLexer(source);
    final CommonTokenStream tokens = new CommonTokenStream(zfgLexer);
    System.out.println("tokens: " + PrettyPrint.toPrettyTokensString(zfgLexer, tokens));

    // Syntax Analysis
    final ZfgParser zfgParser = new ZfgParser(tokens);
    final CompilationUnitContext parsed = zfgParser.compilationUnit();
    System.out.println("parsed: " + parsed.toStringTree(zfgParser));
    System.out.println("tree:\n" + PrettyPrint.toPrettyTreeString(zfgParser, parsed));

    // Semantic Analysis
    final Parser parser = new Parser();
    final Node root = parser.visitCompilationUnit(parsed);
    final List<Error> errors = parser.errors();
    System.out.println("parsed: " + root);
    System.out.println("tree: " + PrettyPrint.toPrettyTreeString(root));
    System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
    return errors.isEmpty() ? new Result.Val(root) : new Result.Err(errors);
  }

  private final List<Error> errors = new ArrayList<>();

  public Parser() {}

  // Returns list of errors encountered during parsing
  public final List<Error> errors() { return errors; }

  // Reports an error and returns an error node
  public final void errors(final ParserRuleContext ctx, final String msg) {
    errors.add(new Error(ctx, msg));
  }
}
