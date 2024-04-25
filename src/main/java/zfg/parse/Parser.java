package zfg.parse;

import static zfg.antlr.ZfgLexer.BIT;
import static zfg.antlr.ZfgLexer.BitLit;
import static zfg.antlr.ZfgLexer.F32;
import static zfg.antlr.ZfgLexer.F64;
import static zfg.antlr.ZfgLexer.FltLit;
import static zfg.antlr.ZfgLexer.I08;
import static zfg.antlr.ZfgLexer.I16;
import static zfg.antlr.ZfgLexer.I32;
import static zfg.antlr.ZfgLexer.I64;
import static zfg.antlr.ZfgLexer.IntLit;
import static zfg.antlr.ZfgLexer.LET;
import static zfg.antlr.ZfgLexer.MUT;
import static zfg.antlr.ZfgLexer.PUB;
import static zfg.antlr.ZfgLexer.SUB;
import static zfg.antlr.ZfgLexer.U08;
import static zfg.antlr.ZfgLexer.U16;
import static zfg.antlr.ZfgLexer.U32;
import static zfg.antlr.ZfgLexer.U64;
import static zfg.antlr.ZfgLexer.USE;
import static zfg.parse.parse_literal.parseBitLit;
import static zfg.parse.parse_literal.parseFltLit;
import static zfg.parse.parse_literal.parseIntLit;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import zfg.antlr.ZfgParser.AssignmentExprContext;
import zfg.antlr.ZfgParser.AssignmentStmtContext;
import zfg.antlr.ZfgParser.BlockContext;
import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.antlr.ZfgParser.ExpressionContext;
import zfg.antlr.ZfgParser.FunctionCallExprContext;
import zfg.antlr.ZfgParser.FunctionCallStmtContext;
import zfg.antlr.ZfgParser.FunctionParameterContext;
import zfg.antlr.ZfgParser.FunctionReturnStmtContext;
import zfg.antlr.ZfgParser.FunctionTypeContext;
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
import zfg.antlr.ZfgParser.PrimitiveTypeContext;
import zfg.antlr.ZfgParser.StatementContext;
import zfg.antlr.ZfgParser.TypeContext;
import zfg.antlr.ZfgParser.VariableExprContext;
import zfg.core.inst;
import zfg.core.maybe.Some;
import zfg.core.type;
import zfg.core.type.Type;

public final class Parser {

  public Parser() {}

  //// Errors //////////////////////////////////////////////////////////////////////////////////////

  /** Parser error */
  public static final record Error(
    ParserRuleContext ctx, // The context where the error occurred
    String msg             // The error message
  ) {
    @Override public String toString() {
      final int line = ctx.start.getLine();
      final int column = ctx.start.getCharPositionInLine();
      return String.format("%d:%d: %s", line, column, msg);
    }
  }

  /** List of errors */
  private final List<Error> errors = new ArrayList<>();

  /** Get the list of errors */
  public List<Error> errors() { return errors; }

  /** Report an error */
  private void err(final Error err) { errors.add(err); }
  private void err(final ParserRuleContext ctx, final String msg) { err(new Error(ctx, msg)); }

  //// Symbol Table /////////////////////////////////////////////////////////////////////

  final symbol.Table symbolTable = new symbol.Table();

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Module
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public node.Node visitModule(final ModuleContext ctx) {
    // Push a new module scope
    symbolTable.pushScope();

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
    final symbol.Scope scope = symbolTable.popScope();

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

  public node.Node visitDeclarationStmt(final DeclarationStmtContext ctx) {
    // Get the symbol modifier
    final symbol.Modifier mod = switch (ctx.mod.getType()) {
      case LET -> symbol.Modifier.Let;
      case MUT -> symbol.Modifier.Mut;
      case USE -> symbol.Modifier.Use;
      case PUB -> symbol.Modifier.Pub;
      default -> throw new AssertionError();
    };
    // Get the symbol name
    final String id = ctx.id.getText();
    // Visit the symbol type
    final type.Type type = visitType(ctx.type());

    

    // Add the symbol to the symbol table
    final Error err = symbolTable.addSymbol(ctx, mod, id, type, null);
    if (err != null) {
      err(err);
    }
    // If it's a function type, create a scope and add the argument symbols

    // TODO ...

    // Visit the symbol's definition (expression or block)
    final ExpressionContext expr = ctx.expression();
    final BlockContext block = ctx.block();
    if ((expr == null) == (block == null)) throw new AssertionError();
    if (expr != null) {
      final node.Node child = visitExpression(expr);
    } else {
      // If it's a function type, add the argument symbols
      final node.Node child = visitBlock(block);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Type
  //////////////////////////////////////////////////////////////////////////////////////////////////

  public type.Type visitType(final TypeContext ctx) {
    return switch (ctx) {
      case PrimitiveTypeContext  type -> visitPrimitiveType(type);
      case FunctionTypeContext   type -> visitFunctionType(type);
      default -> throw new AssertionError();
    };
  }

  public type.Type visitPrimitiveType(final PrimitiveTypeContext ctx) {
    return switch (ctx.token.getType()) {
      case BIT -> type.bit;
      case U08 -> type.u08;
      case U16 -> type.u16;
      case U32 -> type.u32;
      case U64 -> type.u64;
      case I08 -> type.i08;
      case I16 -> type.i16;
      case I32 -> type.i32;
      case I64 -> type.i64;
      case F32 -> type.f32;
      case F64 -> type.f64;
      default -> throw new AssertionError();
    };
  }

  public type.Type visitFunctionType(final FunctionTypeContext ctx) {
    // Get the argument types
    final List<FunctionParameterContext> params = ctx.functionParameter();
    final List<node.Type> args = new ArrayList<>(params.size());
    for (final TypeContext argType : argTypes) {
      args.add(visitType(argType));
    }
    // Get the return type
    final type.Type returnType = visitType(ctx.type());
    // Create and return the function type
    return type.Type.fun(returnType, args);
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
      case Some<inst.Bit> some -> node.Const.of(some.value);
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
      case Some<inst.Inst<?>> some -> node.Const.of(some.value);
      default -> {
        err(ctx, "Invalid int literal: \"" + text + "\"");
        yield node.Const.err;
      }
    };
  }

  private node.Node visitFltLit(final LiteralExprContext ctx) {
    final String text = ctx.lit.getText();
    return switch (parseFltLit(text)) {
      case Some<inst.Inst<?>> some -> node.Const.of(some.value);
      default -> {
        err(ctx, "Invalid flt literal: \"" + text + "\"");
        yield node.Const.err;
      }
    };
  }
}
