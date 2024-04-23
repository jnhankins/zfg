package zfg.ast;

import zfg.antlr.ZfgParser;
import zfg.antlr.ZfgParser.AssignmentStmtContext;
import zfg.antlr.ZfgParser.DeclarationStmtContext;
import zfg.antlr.ZfgParser.FunctionCallStmtContext;
import zfg.antlr.ZfgParser.FunctionReturnStmtContext;
import zfg.antlr.ZfgParser.IfElseStmtContext;
import zfg.antlr.ZfgParser.LoopBreakStmtContext;
import zfg.antlr.ZfgParser.LoopContinueStmtContext;
import zfg.antlr.ZfgParser.LoopForStmtContext;
import zfg.antlr.ZfgParser.LoopStmtContext;
import zfg.antlr.ZfgParser.LoopWhileStmtContext;
import zfg.antlr.ZfgParser.StatementContext;

public final class ParseStmt {
  private ParseStmt() {}

  public static final Node visitStatement(final Parser parser, final StatementContext ctx) {
    return switch (ctx) {
      case DeclarationStmtContext    stmt -> visitDeclarationStmt(parser, stmt);
      case AssignmentStmtContext     stmt -> visitAssignmentStmt(parser, stmt);
      case FunctionCallStmtContext   stmt -> visitFunctionCallStmt(parser, stmt);
      case IfElseStmtContext         stmt -> visitIfElseStmt(parser, stmt);
      case LoopStmtContext           stmt -> visitLoopStmt(parser, stmt);
      case LoopWhileStmtContext      stmt -> visitLoopWhileStmt(parser, stmt);
      case LoopForStmtContext        stmt -> visitLoopForStmt(parser, stmt);
      case LoopBreakStmtContext      stmt -> visitLoopBreakStmt(parser, stmt);
      case LoopContinueStmtContext   stmt -> visitLoopContinueStmt(parser, stmt);
      case FunctionReturnStmtContext stmt -> visitFunctionReturnStmt(parser, stmt);
      default -> throw new AssertionError();
    };
  }

  public static final Node visitDeclarationStmt(final Parser parser, final DeclarationStmtContext ctx) {
    // mutable or immutable symbol
    final boolean mut = switch (ctx.modifier.getType()) {
      case ZfgParser.LET -> false;
      case ZfgParser.MUT -> true;
      default -> throw new AssertionError();
    };
    // symbol name
    final String id = ctx.id.getText();
    // symbol type
    final Type type = visitType(ctx.type()); // if tyepe
    // if symbol type is functional, permit function parameters in rhs expression or block

    // TODO RHS



    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitDeclarationStmt'");
  }

  public static final Node visitAssignmentStmt(final Parser parser, final AssignmentStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitAssignmentStmt'");
  }

  public static final Node visitFunctionCallStmt(final Parser parser, final FunctionCallStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCallStmt'");
  }

  public static final Node visitIfElseStmt(final Parser parser, final IfElseStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitIfElseStmt'");
  }

  public static final Node visitLoopStmt(final Parser parser, final LoopStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitLoopStmt'");
  }

  public static final Node visitLoopWhileStmt(final Parser parser, final LoopWhileStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitLoopWhileStmt'");
  }

  public static final Node visitLoopForStmt(final Parser parser, final LoopForStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitLoopForStmt'");
  }

  public static final Node visitLoopBreakStmt(final Parser parser, final LoopBreakStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitLoopBreakStmt'");
  }

  public static final Node visitLoopContinueStmt(final Parser parser, final LoopContinueStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitLoopContinueStmt'");
  }

  public static final Node visitFunctionReturnStmt(final Parser parser, final FunctionReturnStmtContext ctx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitFunctionReturnStmt'");
  }
}
