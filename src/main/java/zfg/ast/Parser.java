package zfg.ast;

import static zfg.antlr.ZfgLexer.BIT;
import static zfg.antlr.ZfgLexer.F32;
import static zfg.antlr.ZfgLexer.I08;
import static zfg.antlr.ZfgLexer.I16;
import static zfg.antlr.ZfgLexer.I32;
import static zfg.antlr.ZfgLexer.I64;
import static zfg.antlr.ZfgLexer.LET;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgLexer.U08;
import static zfg.antlr.ZfgLexer.U16;
import static zfg.antlr.ZfgLexer.U32;
import static zfg.antlr.ZfgLexer.U64;
import static zfg.antlr.ZfgLexer.VAR;
import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.FltLit;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import zfg.antlr.ZfgLexer;
import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.AssignmentStmtContext;
import zfg.antlr.ZfgParser.CompilationUnitContext;
import zfg.antlr.ZfgParser.DeclarationContext;
import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.FunctionCallStmtContext;
import zfg.antlr.ZfgParser.FunctionReturnStmtContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
import zfg.antlr.ZfgParser.GroupedExprContext;
import zfg.antlr.ZfgParser.IdentifierContext;
import zfg.antlr.ZfgParser.IfElseStmtContext;
import zfg.antlr.ZfgParser.InfixOpExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.LoopBreakStmtContext;
import zfg.antlr.ZfgParser.LoopContinueStmtContext;
import zfg.antlr.ZfgParser.LoopForStmtContext;
import zfg.antlr.ZfgParser.LoopStmtContext;
import zfg.antlr.ZfgParser.LoopWhileStmtContext;
import zfg.antlr.ZfgParser.NamedTypeContext;
import zfg.antlr.ZfgParser.PostfixOpExprContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.VariableExprContext;
import zfg.ast.Node.CompilationUnit;
import zfg.ast.Node.Const;
import zfg.ast.Node.VarRef;
import zfg.lang.primitive.Val;

public final class Parser {

  public static final record Error(ParserRuleContext ctx, String msg) {}

  public static Node parse(final Path path) {
    try { return parse(CharStreams.fromPath(path)); }
    catch (final Exception e) { throw new RuntimeException(e); }
  }

  public static Node parse(final String source, final String sourceName) {
    return parse(CharStreams.fromString(source, sourceName));
  }

  public static Node parse(final CharStream source) {
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
    System.out.println("parsed: " + root);
    System.out.println("tree: " + PrettyPrint.toPrettyTreeString(root));

    return root;
  }


  // List of errors encountered during parsing
  private final List<Error> errors = new ArrayList<>();

  public Parser() {}

  // Returns list of errors encountered during parsing
  public final List<Error> errors() { return errors; }

  // Reports an error and returns an error node
  public final void error(final ParserRuleContext ctx, final String msg) {
    errors.add(new Error(ctx, msg));
  }
}
