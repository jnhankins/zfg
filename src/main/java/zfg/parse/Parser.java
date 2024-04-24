package zfg.parse;

import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.parse.parse_literal.parseBitLit;
import static zfg.parse.parse_literal.parseFltLit;
import static zfg.parse.parse_literal.parseIntLit;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.AssignmentStmtContext;
import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.FunctionCallStmtContext;
import zfg.antlr.ZfgParser.FunctionReturnStmtContext;
import zfg.antlr.ZfgParser.GroupedExprContext;
import zfg.antlr.ZfgParser.IfElseStmtContext;
import zfg.antlr.ZfgParser.InfixOpExprContext;
import zfg.antlr.ZfgParser.LiteralExprContext;
import zfg.antlr.ZfgParser.LoopBreakStmtContext;
import zfg.antlr.ZfgParser.LoopContinueStmtContext;
import zfg.antlr.ZfgParser.LoopForStmtContext;
import zfg.antlr.ZfgParser.LoopStmtContext;
import zfg.antlr.ZfgParser.LoopWhileStmtContext;
import zfg.antlr.ZfgParser.ModuleContext;
import zfg.antlr.ZfgParser.PostfixOpExprContext;
import zfg.antlr.ZfgParser.PrefixOpExprContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.VariableExprContext;
import zfg.core.inst;
import zfg.core.maybe.Some;

public final class Parser {
  /** Parser error */
  public static final record Error(ParserRuleContext ctx, String msg) {
    @Override public String toString() {
      final Token start = ctx.getStart();
      final int line = start.getLine();
      final int column = start.getCharPositionInLine();
      return String.format("%d:%d: %s", line, column, msg);
    }
  }

  private final List<Error> errors;
  private final ArrayDeque<Scope> scopes;

  static interface Scope {}
  static final class ModuleScope implements Scope {}


  public Parser() {
    this.errors = new ArrayList<>();
    this.scopes = new ArrayDeque<>();
  }

  /** Get the list of errors */
  public List<Error> errors() { return errors; }

  /** Report an error */
  private void err(final ParserRuleContext ctx, final String msg) {
    errors.add(new Error(ctx, msg));
  }



  public node.Node visitModule(final ModuleContext ctx) {
    // Push a new module scope
    final Scope scope = new ModuleScope();
    scopes.push(scope);

    // Get all the statements
    final List<StatementContext> statement = ctx.statement();

    // Handle forward declarations
    for (final StatementContext stmt : statement) {
      if (stmt instanceof DeclarationStmtContext decl) {
        // TODO
      }
    }

    // Post-order traversal
    final List<node.Node> stmts = new ArrayList<>(statement.size());
    for (final StatementContext stmt : statement) {
      stmts.add(visitStatement(stmt));
    }

    // Pop the module scope
    scopes.pop();

    // Create and return the module node
    // TODO
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Statement
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public node.Node visitStatement(final StatementContext ctx) {
    return switch (ctx) {
      case DeclarationStmtContext    stmt -> visitDeclarationStmt(stmt);
      case AssignmentStmtContext     stmt -> throw new UnsupportedOperationException(); //visitAssignmentStmt(stmt);
      case FunctionCallStmtContext   stmt -> throw new UnsupportedOperationException(); //visitFunctionCallStmt(stmt);
      case FunctionReturnStmtContext stmt -> throw new UnsupportedOperationException(); //visitFunctionReturnStmt(stmt);
      case IfElseStmtContext         stmt -> throw new UnsupportedOperationException(); //visitIfElseStmt(stmt);
      case LoopStmtContext           stmt -> throw new UnsupportedOperationException(); //visitLoopStmt(stmt);
      case LoopWhileStmtContext      stmt -> throw new UnsupportedOperationException(); //visitLoopWhileStmt(stmt);
      case LoopForStmtContext        stmt -> throw new UnsupportedOperationException(); //visitLoopForStmt(stmt);
      case LoopBreakStmtContext      stmt -> throw new UnsupportedOperationException(); //visitLoopBreakStmt(stmt);
      case LoopContinueStmtContext   stmt -> throw new UnsupportedOperationException(); //visitLoopContinueStmt(stmt);
      default -> throw new AssertionError();
    };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // DeclarationStmt
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public node.Node visitDeclarationStmt(DeclarationStmtContext stmt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitDeclarationStmt'");
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expression
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public node.Node visitExpression(final ExpressionContext ctx) {
    return switch (ctx) {
      case GroupedExprContext      expr -> visitGroupedExpr(expr);
      case LiteralExprContext      expr -> visitLiteralExpr(expr);
      case VariableExprContext     expr -> throw new UnsupportedOperationException(); //visitVariableExpr(expr);
      case FunctionCallExprContext expr -> throw new UnsupportedOperationException(); //visitInvocationExpr(expr);
      case PostfixOpExprContext    expr -> throw new UnsupportedOperationException(); //visitPostfixOpExpr(expr);
      case PrefixOpExprContext     expr -> throw new UnsupportedOperationException(); //visitPrefixOpExpr(expr);
      case InfixOpExprContext      expr -> throw new UnsupportedOperationException(); //visitInfixOpExpr(expr);
      case AssignmentExprContext   expr -> throw new UnsupportedOperationException(); //visitAssignExpr(expr);
      default -> throw new AssertionError();
    };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // GroupedExpr
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public node.Node visitGroupedExpr(final GroupedExprContext ctx) {
    return visitExpression(ctx.expression());
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // LiteralExpr
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public node.Node visitLiteralExpr(final LiteralExprContext ctx) {
    return switch (ctx.lit.getType()) {
      case BitLit -> visitBitLit(ctx);
      case IntLit -> visitIntLit(ctx);
      case FltLit -> visitFltLit(ctx);
      default -> throw new AssertionError();
    };
  }

  private node.Node visitBitLit(final LiteralExprContext ctx) {
    final String text = ctx.lit.getText();
    return switch (parseBitLit(text)) {
      case Some<inst.Bit> some -> node.Const.of(some.value());
      default -> {
        err(ctx, "Invalid bit literal: \"" + text + "\"");
        yield node.Const.err;
      }
    };
  }

  private node.Node visitIntLit(final LiteralExprContext ctx) {
    final String text = ctx.lit.getText();
    final boolean hasMinusPrefix = switch (ctx.parent) {
      case PrefixOpExprContext parent ->
        parent.op.getType() == SUB &&
        parent.op.getStopIndex() + 1 == ctx.getStart().getStartIndex();
      default -> false;
    };
    return switch (parseIntLit(text, hasMinusPrefix)) {
      case Some<inst.Inst<?>> some -> node.Const.of(some.value());
      default -> {
        err(ctx, "Invalid int literal: \"" + text + "\"");
        yield node.Const.err;
      }
    };
  }

  private node.Node visitFltLit(final LiteralExprContext ctx) {
    final String text = ctx.lit.getText();
    return switch (parseFltLit(text)) {
      case Some<inst.Inst<?>> some -> node.Const.of(some.value());
      default -> {
        err(ctx, "Invalid flt literal: \"" + text + "\"");
        yield node.Const.err;
      }
    };
  }
}
