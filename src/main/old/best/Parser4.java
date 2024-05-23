// package zfg;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// import org.antlr.v4.runtime.CharStream;
// import org.antlr.v4.runtime.CharStreams;
// import org.antlr.v4.runtime.CommonTokenStream;
// import org.antlr.v4.runtime.ParserRuleContext;
// import org.antlr.v4.runtime.Token;

// import zfg.Ast2.Expr;
// import zfg.antlr.ZfgLexer;
// import zfg.antlr.ZfgParser;
// import zfg.antlr.ZfgParser.AlgebraExpressionContext;
// import zfg.antlr.ZfgParser.ArrayTypeContext;
// import zfg.antlr.ZfgParser.AssignmentContext;
// import zfg.antlr.ZfgParser.AssignmentStatementContext;
// import zfg.antlr.ZfgParser.BitwiseBinaryExpressionContext;
// import zfg.antlr.ZfgParser.BitwiseExpressionContext;
// import zfg.antlr.ZfgParser.BitwiseNaryExpressionContext;
// import zfg.antlr.ZfgParser.BivariateAssignmentContext;
// import zfg.antlr.ZfgParser.BivariateAssignmentExpressionContext;
// import zfg.antlr.ZfgParser.CompareBinaryExpressionContext;
// import zfg.antlr.ZfgParser.CompareChainExpressionContext;
// import zfg.antlr.ZfgParser.CompareExpressionContext;
// import zfg.antlr.ZfgParser.CompareOperandContext;
// import zfg.antlr.ZfgParser.ExpressionContext;
// import zfg.antlr.ZfgParser.FunctionCallContext;
// import zfg.antlr.ZfgParser.FunctionCallExpressionContext;
// import zfg.antlr.ZfgParser.FunctionCallStatementContext;
// import zfg.antlr.ZfgParser.FunctionDeclarationContext;
// import zfg.antlr.ZfgParser.FunctionTypeContext;
// import zfg.antlr.ZfgParser.LiteralContext;
// import zfg.antlr.ZfgParser.LiteralExpressionContext;
// import zfg.antlr.ZfgParser.LogicalExpressionContext;
// import zfg.antlr.ZfgParser.LogicalOperandContext;
// import zfg.antlr.ZfgParser.ModuleContext;
// import zfg.antlr.ZfgParser.NamedTypeContext;
// import zfg.antlr.ZfgParser.NumericLiteralContext;
// import zfg.antlr.ZfgParser.ParentheticalExpressionContext;
// import zfg.antlr.ZfgParser.PostfixAssignmentContext;
// import zfg.antlr.ZfgParser.PostfixAssignmentExpressionContext;
// import zfg.antlr.ZfgParser.PrefixAssignmentContext;
// import zfg.antlr.ZfgParser.PrefixAssignmentExpressionContext;
// import zfg.antlr.ZfgParser.PrimitiveTypeContext;
// import zfg.antlr.ZfgParser.RecordTypeContext;
// import zfg.antlr.ZfgParser.ScopeContext;
// import zfg.antlr.ZfgParser.StatementContext;
// import zfg.antlr.ZfgParser.TupleTypeContext;
// import zfg.antlr.ZfgParser.TypeContext;
// import zfg.antlr.ZfgParser.TypeDeclarationContext;
// import zfg.antlr.ZfgParser.UnambiguousExpressionContext;
// import zfg.antlr.ZfgParser.UnaryExpressionContext;
// import zfg.antlr.ZfgParser.VariableContext;
// import zfg.antlr.ZfgParser.VariableDeclarationContext;
// import zfg.antlr.ZfgParser.VariableExpressionContext;
// import zfg.antlr.ZfgToken;
// import zfg.antlr.ZfgTokenFactory;

// public class Parser4 {

//   public static interface Result {
//     public static final record Val(Ast2 value) implements Result {}
//     public static final record Err(List<Error> errors) implements Result {}
//   }

//   public static Result parse(final java.nio.file.Path path) {
//     try { return parse(CharStreams.fromPath(path)); }
//     catch (final Exception e) { throw new RuntimeException(e); }
//   }

//   public static Result parse(final String source, final String sourceName) {
//     return parse(CharStreams.fromString(source, sourceName));
//   }

//   public static Result parse(final CharStream source) {
//     System.out.println("source: " + source.getSourceName());
//     System.out.println(">" + source.toString().replaceAll("\\r?\\n", "\n>"));

//     // Lexical Analysis
//     final ZfgLexer tokenLexer = new ZfgLexer(source);
//     tokenLexer.setTokenFactory(new ZfgTokenFactory());
//     final CommonTokenStream tokenStream = new CommonTokenStream(tokenLexer);
//     System.out.println("tokens: " + PrettyPrint.tokenStream(tokenLexer, tokenStream));

//     // Syntax Analysis
//     final ZfgParser syntaxParser = new ZfgParser(tokenStream);
//     final ModuleContext syntaxTree = syntaxParser.module();
//     System.out.println("parsed: " + syntaxTree.toStringSyntaxTree(false));
//     System.out.println("tree:\n" + syntaxTree.toStringSyntaxTree(true));

//     // Semantic Analysis
//     final Parser4 parser = new Parser4();
//     final Ast2.Module root = parser.parseModule(syntaxTree, source.getSourceName());
//     final List<Error> errors = parser.errors();
//     System.out.println("parsed: " + root);
//     System.out.println("tree: " + root);
//     System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
//     return errors.isEmpty() ? new Result.Val(root) : new Result.Err(errors);
//   }

//   private Parser4() {}

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Errors
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   /** Parser error */
//   public static final class Error {
//     final ParserRuleContext ctx; // The most speicific context for where the error occurred
//     final String msg; // The error message

//     public Error(final ParserRuleContext ctx, final String msg) {
//       this.ctx = ctx;
//       this.msg = msg;
//     }

//     @Override public String toString() {
//       final int line = ((ZfgToken) ctx.start).line;
//       final int column = ((ZfgToken) ctx.start).column;
//       return String.format("%d:%d: %s", line, column, msg);
//     }
//   }

//   /** List of all reported errors */
//   private final List<Error> errors = new ArrayList<>();

//   /** Get the list of errors */
//   public List<Error> errors() { return errors; }

//   /** Report an error */
//   private void err(final Error err) { errors.add(err); }
//   private void err(final ParserRuleContext ctx, final String msg) { err(new Error(ctx, msg)); }
//   @FunctionalInterface public static interface Err {
//     void err(final ParserRuleContext ctx, final String msg);
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Symbol Table
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private final Symbol.Table symbolTable = new Symbol.Table();

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Module
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public Ast2.Module parseModule(final ModuleContext ctx, final String fqn) {
//     // Create the module scope
//     symbolTable.pushModuleScope();
//     // Parse the module scope
//     final Ast2.Stmt[] stmts = parseScope(ctx.body);
//     // Pop the module scope
//     symbolTable.popModuleScope();
//     // Error handling
//     if (stmts == null) return null;
//     // Construct and return the module
//     return new Ast2.Module(ctx, fqn, stmts);
//   }


//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Scope & Statements
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public Ast2.Stmt[] parseScope(final ScopeContext ctx) {
//     final List<StatementContext> ctxStmts = ctx.stmts;
//     final int statementCount = ctxStmts.size();
//     // Create forward declarations
//     createForwardDeclarations(ctx);
//     // Post-order traversal
//     final Ast2.Stmt[] stmts = new Ast2.Stmt[statementCount];
//     for (int i = 0; i < statementCount; i++) stmts[i] = parseStatement(ctxStmts.get(i));
//     // Error propogation
//     for (int i = 0; i < statementCount; i++) if (stmts[i] == null) return null;
//     // Construct the node
//     return stmts;
//   }

//   private void createForwardDeclarations(final ScopeContext ctx) {
//     // Note: Continue parsing even if there are errors so that we can report multiple errors at once
//     final List<StatementContext> ctxStmts = ctx.stmts;
//     final int statementCount = ctxStmts.size();
//     for (int i = 0; i < statementCount; i++) {
//       switch (ctxStmts.get(i)) {
//         case TypeDeclarationContext alt -> {
//           final Ast2.Modifier mod = parseSymbolModifier(alt.mod);
//           final String name = alt.name.getText();
//           final Type.Nom type = Types.Nom(name);
//           final Ast2.TypeDecl decl = new Ast2.TypeDecl(alt, mod, type);
//           symbolTable.declare(new Symbol.TypeDecl(decl), this::err);
//         }
//         case FunctionDeclarationContext alt -> {
//           final Ast2.Modifier mod = parseSymbolModifier(alt.mod);
//           final String name = alt.name.getText();
//           final Ast2.FunDecl decl = new Ast2.FunDecl(alt, mod, name, Types.UNK);
//           symbolTable.declare(new Symbol.FunDecl(decl), this::err);
//         }
//         case VariableDeclarationContext alt -> {
//           final Ast2.Modifier mod = parseSymbolModifier(alt.mod);
//           final String name = alt.name.getText();
//           final Ast2.VarDecl decl = new Ast2.VarDecl(alt, mod, name, Types.UNK);
//           symbolTable.declare(new Symbol.VarDecl(decl), this::err);
//         }
//         default -> { /* do nothing */ }
//       }
//     }
//   }

//   public Ast2.Stmt parseStatement(final StatementContext ctx) {
//     return switch (ctx) {
//       case TypeDeclarationContext decl -> parseTypeDeclaration(decl);
//       case FunctionDeclarationContext decl -> parseFunctionDeclaration(decl);
//       case VariableDeclarationContext decl -> parseVariableDeclaration(decl);
//       case AssignmentStatementContext stmt -> parseAssignmentStatement(stmt);
//       case FunctionCallStatementContext stmt -> parseFunctionCallStatement(stmt);
//       default -> throw new AssertionError();
//     };
//   }

//   public Ast2.Stmt parseTypeDeclaration(final TypeDeclarationContext ctx) {
//     // Fetch the AST node from the symbol table
//     final Ast2.TypeDecl decl = ((Symbol.TypeDecl) symbolTable.getSymbol(ctx)).decl;

//     // Parse and bind the type
//     final Type type = parseType(ctx.rhs);
//     if (type != Types.ERR) decl.bindType(type);

//     // Error propogation
//     if (type == Types.ERR) return null;

//     // Return the AST node
//     return decl;
//   }

//   public Ast2.Stmt parseFunctionDeclaration(final FunctionDeclarationContext ctx) {
//     // Fetch the AST node from the symbol table
//     final Ast2.FunDecl decl = ((Symbol.FunDecl) symbolTable.getSymbol(ctx)).decl;

//     // Parse and bind the type
//     final Type type = parseFunctionType(ctx.typ);
//     if (type != Types.ERR) decl.bindType(type);

//     // Create a new scope for the function and declare the function parameters
//     symbolTable.pushMethodScope();
//     if (type instanceof Type.Fun funType && funType.paramsType instanceof Type.Rec params) {
//       final int length = params.types.length;
//       for (int i = 0; i < length; i++) {
//         final Ast2.Modifier paramMod = params.muts[i] ? Ast2.Modifier.MUT : Ast2.Modifier.LET;
//         final String paramName = params.names[i];
//         final Type paramType = params.types[i];
//         final Ast2.VarDecl paramDecl = new Ast2.VarDecl(ctx, paramMod, paramName, paramType);
//         symbolTable.declare(new Symbol.VarDecl(paramDecl), this::err);
//       }
//     }

//     // Parse and bind the body
//     final Ast2.Stmt[] body = switch (ctx.rhs.children.get(0)) {
//       case ExpressionContext bod -> {
//         final Ast2.Expr expr = parseExpression(bod);
//         if (expr == null) yield null;
//         final Ast2.ReturnStmt stmt = new Ast2.ReturnStmt(bod, decl, expr);
//         decl.addReturnStmt(stmt);
//         yield new Ast2.Stmt[] { stmt };
//       }
//       case ScopeContext bod -> parseScope(bod);
//       default -> throw new AssertionError();
//     };
//     if (body != null) decl.bindStmts(body);

//     // Pop the scope
//     symbolTable.popMethodScope();

//     // TODO: Type checking

//     // Error propogation
//     if (type == Types.ERR) return null;
//     if (body == null) return null;

//     // Return the AST node
//     return decl;
//   }

//   public Ast2.Stmt parseVariableDeclaration(final VariableDeclarationContext ctx) {
//     // Fetch the AST node from the symbol table
//     final Ast2.VarDecl decl = ((Symbol.VarDecl) symbolTable.getSymbol(ctx)).decl;

//     // Parse and bind the type
//     final Type type = parseType(ctx.typ);
//     if (type != Types.ERR) decl.bindType(type);

//     // Parse and bind the expression
//     // TODO: Allow VariableDeclaration's to have scoped definitions
//     // TODO: Allow VariableDeclaration's to be forward declared without an immediate assignment?
//     //       Maybe not. Need a reason to do this. Maybe like:
//     //       let x: i32; let y: i32; if something { x = 1; y = 2; } else { x = 3; y = 4; }
//     final Expr expr = parseExpression(ctx.rhs);
//     if (expr != null) decl.bindExpr(expr);

//     // Error propogation
//     if (type == Types.ERR) return null;
//     if (expr == null) return null;

//     // Return the AST node
//     return decl;
//   }

//   private Ast2.Modifier parseSymbolModifier(final Token token) {
//     return switch (((ZfgToken) token).type) {
//       case ZfgLexer.PUB -> Ast2.Modifier.PUB;
//       case ZfgLexer.LET -> Ast2.Modifier.LET;
//       case ZfgLexer.MUT -> Ast2.Modifier.MUT;
//       default -> throw new AssertionError();
//     };
//   }

//   public Ast2.Stmt parseAssignmentStatement(final AssignmentStatementContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseAssignmentStatement'");
//   }

//   public Ast2.Stmt parseFunctionCallStatement(final FunctionCallStatementContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseFunctionCallStatement'");
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Expressions
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public Ast2.Expr parseExpression(final ExpressionContext ctx) {
//     return switch (ctx.children.get(0)) {
//       case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
//       case AlgebraExpressionContext alt -> parseAlgebraExpression(alt);
//       case BitwiseExpressionContext alt -> parseBitwiseExpression(alt);
//       case CompareExpressionContext alt -> parseCompareExpression(alt);
//       case LogicalExpressionContext alt -> parseLogicalExpression(alt);
//       default -> throw new AssertionError();
//     };
//   }

//   public Ast2.Expr parseUnambiguousExpression(final UnambiguousExpressionContext ctx) {
//     return switch (ctx) {
//       case PostfixAssignmentExpressionContext alt -> parsePostfixAssignmentExpression(alt);
//       case PrefixAssignmentExpressionContext alt -> parsePrefixAssignmentExpression(alt);
//       case FunctionCallExpressionContext alt -> parseFunctionCallExpression(alt);
//       case VariableExpressionContext alt -> parseVariableExpression(alt);
//       case LiteralExpressionContext alt -> parseLiteralExpression(alt);
//       case UnaryExpressionContext alt -> parseUnaryExpression(alt);
//       case BivariateAssignmentExpressionContext alt -> parseBivariateAssignmentExpression(alt);
//       case ParentheticalExpressionContext alt -> parseExpression(alt.exp);
//       default -> throw new AssertionError();
//     };
//   }

//   public Ast2.Expr parsePostfixAssignmentExpression(final PostfixAssignmentExpressionContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parsePostfixAssignmentExpression'");
//   }

//   public Ast2.Expr parsePrefixAssignmentExpression(final PrefixAssignmentExpressionContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parsePrefixAssignmentExpression'");
//   }

//   public Ast2.Expr parseFunctionCallExpression(final FunctionCallExpressionContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseFunctionCallExpression'");
//   }

//   public Ast2.Expr parseVariableExpression(final VariableExpressionContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseVariableExpression'");
//   }

//   public Ast2.Expr parseLiteralExpression(final LiteralExpressionContext ctx) {
//     // Parse the literal
//     final Inst inst = parseLiteral(ctx.lit);
//     // Handle errors
//     if (inst == null) return new Ast2.ErrorExpr(ctx);
//     // Construct and return the expression
//     return new Ast2.ConstExpr(ctx, inst);
//   }

//   public Ast2.Expr parseUnaryExpression(final UnaryExpressionContext ctx) {
//     // Parse the operand
//     final Ast2.Expr opd = parseUnambiguousExpression(ctx.rhs);
//     // Parse the operator
//     final Ast2.UnaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
//       case ZfgLexer.ADD -> Ast2.UnaryExpr.Opr.POS;
//       case ZfgLexer.SUB -> Ast2.UnaryExpr.Opr.NEG;
//       case ZfgLexer.NOT -> Ast2.UnaryExpr.Opr.NOT;
//       case ZfgLexer.LNT -> Ast2.UnaryExpr.Opr.LNT;
//       default -> throw new AssertionError();
//     };
//     throw new UnsupportedOperationException("TODO"); // TODO
//   }

//   public Ast2.Expr parseBivariateAssignmentExpression(final BivariateAssignmentExpressionContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseBivariateAssignmentExpression'");
//   }

//   public Ast2.Expr parseAlgebraExpression(final AlgebraExpressionContext ctx) {
//     // Parse the operands
//     final Ast2.Expr lhs = ctx.lhsA != null
//       ? parseAlgebraExpression(ctx.lhsA)
//       : parseUnambiguousExpression(ctx.lhsU);
//     final Ast2.Expr rhs = ctx.rhsA != null
//       ? parseAlgebraExpression(ctx.rhsA)
//       : parseUnambiguousExpression(ctx.rhsU);
//     // Parse the operator
//     final Ast2.BinaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
//       case ZfgLexer.ADD -> Ast2.BinaryExpr.Opr.ADD;
//       case ZfgLexer.SUB -> Ast2.BinaryExpr.Opr.SUB;
//       case ZfgLexer.MUL -> Ast2.BinaryExpr.Opr.MUL;
//       case ZfgLexer.DIV -> Ast2.BinaryExpr.Opr.DIV;
//       case ZfgLexer.REM -> Ast2.BinaryExpr.Opr.REM;
//       case ZfgLexer.MOD -> Ast2.BinaryExpr.Opr.MOD;
//       default -> throw new AssertionError();
//     };
//     throw new UnsupportedOperationException("TODO"); // TODO
//   }

//   public Ast2.Expr parseBitwiseExpression(final BitwiseExpressionContext ctx) {
//     return switch (ctx) {
//       case BitwiseNaryExpressionContext alt -> parseBitwiseNaryExpression(alt);
//       case BitwiseBinaryExpressionContext alt -> parseBitwiseBinaryExpression(alt);
//       default -> throw new AssertionError();
//     };
//   }

//   public Ast2.Expr parseBitwiseNaryExpression(final BitwiseNaryExpressionContext ctx) {
//     // Parse the operands
//     final int length = ctx.opds.size();
//     final Ast2.Expr[] opds = new Ast2.Expr[length];
//     for (int i = 0; i < length; i++) opds[i] = parseUnambiguousExpression(ctx.opds.get(i));
//     // Parse the operator
//     final Ast2.NaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
//       case ZfgLexer.AND -> Ast2.NaryExpr.Opr.AND;
//       case ZfgLexer.IOR -> Ast2.NaryExpr.Opr.IOR;
//       case ZfgLexer.XOR -> Ast2.NaryExpr.Opr.XOR;
//       default -> throw new AssertionError();
//     };
//     throw new UnsupportedOperationException("TODO"); // TODO
//   }

//   public Ast2.Expr parseBitwiseBinaryExpression(final BitwiseBinaryExpressionContext ctx) {
//     // Parse the operands
//     final Ast2.Expr lhs = parseUnambiguousExpression(ctx.lhs);
//     final Ast2.Expr rhs = parseUnambiguousExpression(ctx.rhs);
//     // Parse the operator
//     final Ast2.BinaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
//       case ZfgLexer.SHL -> Ast2.BinaryExpr.Opr.SHL;
//       case ZfgLexer.SHR -> Ast2.BinaryExpr.Opr.SHR;
//       case ZfgLexer.MOD -> Ast2.BinaryExpr.Opr.MOD;
//       case ZfgLexer.REM -> Ast2.BinaryExpr.Opr.REM;
//       default -> throw new AssertionError();
//     };
//     throw new UnsupportedOperationException("TODO"); // TODO
//   }

//   public Ast2.Expr parseCompareExpression(final CompareExpressionContext ctx) {
//     return switch (ctx) {
//       case CompareChainExpressionContext alt -> parseCompareChainExpression(alt);
//       case CompareBinaryExpressionContext alt -> parseCompareBinaryExpression(alt);
//       default -> throw new AssertionError();
//     };
//   }

//   public Ast2.Expr parseCompareChainExpression(final CompareChainExpressionContext ctx) {
//     // Parse the operands
//     final int length = ctx.opds.size();
//     final Ast2.Expr[] opds = new Ast2.Expr[length];
//     for (int i = 0; i < length; i++) opds[i] = parseCompareOperand(ctx.opds.get(i));
//     // Parse the operators
//     final Ast2.CompareExpr.Opr[] oprs = new Ast2.CompareExpr.Opr[length - 1];
//     for (int i = 0; i < length - 1; i++) oprs[i] = switch (((ZfgToken) ctx.oprs.get(i)).type) {
//       case ZfgLexer.EQL -> Ast2.CompareExpr.Opr.EQL;
//       case ZfgLexer.NEQ -> Ast2.CompareExpr.Opr.NEQ;
//       case ZfgLexer.LTN -> Ast2.CompareExpr.Opr.LTN;
//       case ZfgLexer.LEQ -> Ast2.CompareExpr.Opr.LEQ;
//       case ZfgLexer.GTN -> Ast2.CompareExpr.Opr.GTN;
//       case ZfgLexer.GEQ -> Ast2.CompareExpr.Opr.GEQ;
//       default -> throw new AssertionError();
//     };
//     throw new UnsupportedOperationException("TODO"); // TODO
//   }

//   public Ast2.Expr parseCompareBinaryExpression(final CompareBinaryExpressionContext ctx) {
//     // Parse the operands
//     final Ast2.Expr lhs = parseCompareOperand(ctx.lhs);
//     final Ast2.Expr rhs = parseCompareOperand(ctx.rhs);
//     // Parse the operator
//     final Ast2.BinaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
//       case ZfgLexer.TWC -> Ast2.BinaryExpr.Opr.TWC;
//       default -> throw new AssertionError();
//     };
//     throw new UnsupportedOperationException("TODO"); // TODO
//   }

//   public Ast2.Expr parseCompareOperand(final CompareOperandContext ctx) {
//     return switch (ctx.children.get(0)) {
//       case BitwiseExpressionContext alt -> parseBitwiseExpression(alt);
//       case AlgebraExpressionContext alt -> parseAlgebraExpression(alt);
//       case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
//       default -> throw new AssertionError();
//     };
//   }

//   public Ast2.Expr parseLogicalExpression(final LogicalExpressionContext ctx) {
//     // Parse the operands
//     final int length = ctx.opds.size();
//     final Ast2.Expr[] opds = new Ast2.Expr[length];
//     for (int i = 0; i < length; i++) opds[i] = parseLogicalOperand(ctx.opds.get(i));
//     // Parse the operator
//     final Ast2.NaryExpr.Opr opr = switch (((ZfgToken) ctx.opr).type) {
//       case ZfgLexer.LCJ -> Ast2.NaryExpr.Opr.LCJ;
//       case ZfgLexer.LDJ -> Ast2.NaryExpr.Opr.LDJ;
//       default -> throw new AssertionError();
//     };
//     throw new UnsupportedOperationException("TODO"); // TODO
//   }

//   public Ast2.Expr parseLogicalOperand(final LogicalOperandContext ctx) {
//     return switch (ctx.children.get(0)) {
//       case CompareExpressionContext alt -> parseCompareExpression(alt);
//       case BitwiseExpressionContext alt -> parseBitwiseExpression(alt);
//       case AlgebraExpressionContext alt -> parseAlgebraExpression(alt);
//       case UnambiguousExpressionContext alt -> parseUnambiguousExpression(alt);
//       default -> throw new AssertionError();
//     };
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Assignments
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public void parseAssignment(final AssignmentContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseAssignment'");
//   }

//   public void parseBivariateAssignment(final BivariateAssignmentContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseBivariateAssignment'");
//   }

//   public void parsePostfixAssignment(final PostfixAssignmentContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parsePostfixAssignment'");
//   }

//   public void parsePrefixAssignment(final PrefixAssignmentContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parsePrefixAssignment'");
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Literals
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public Inst parseLiteral(final LiteralContext ctx) {
//     return switch (ctx.children.get(0)) {
//       case NumericLiteralContext alt -> parseNumericLiteral(alt);
//       default -> throw new AssertionError();
//     };
//   }

//   public Inst parseNumericLiteral(final NumericLiteralContext ctx) {
//     final String text = ctx.token.getText();
//     switch (((ZfgToken) ctx.token).type) {
//       case ZfgLexer.BitLit:
//         final Inst bitLit = Literals.parseBitLit(text);
//         if (bitLit == null) err(ctx, "Invalid bit literal: \"" + text + "\"");
//         return bitLit;
//       case ZfgLexer.IntLit:
//         final boolean hasMinusPrefix =
//             ctx.parent instanceof UnaryExpressionContext parent &&
//             ((ZfgToken) parent.opr).type == ZfgLexer.SUB &&
//             ((ZfgToken) parent.opr).stopIndex + 1 == ((ZfgToken) ctx.start).startIndex;
//         final Inst intLit = Literals.parseIntLit(text, hasMinusPrefix);
//         if (intLit == null) err(ctx, "Invalid int literal: \"" + text + "\"");
//         return intLit;
//       case ZfgLexer.FltLit:
//         final Inst fltLit = Literals.parseFltLit(text);
//         if (fltLit == null) err(ctx, "Invalid flt literal: \"" + text + "\"");
//         return fltLit;
//       default: throw new AssertionError();
//     }
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Types
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   public Type parseType(final TypeContext ctx) {
//     return switch (ctx.children.get(0)) {
//       case PrimitiveTypeContext alt -> parsePrimitiveType(alt);
//       case FunctionTypeContext alt -> parseFunctionType(alt);
//       case RecordTypeContext alt -> parseRecordType(alt);
//       case TupleTypeContext alt -> parseTupleType(alt);
//       case ArrayTypeContext alt -> parseArrayType(alt);
//       case NamedTypeContext alt -> parseNamedType(alt);
//       default -> throw new AssertionError();
//     };
//   }

//   public Type parsePrimitiveType(final PrimitiveTypeContext ctx) {
//     return switch (((ZfgToken) ctx.token).type) {
//       case ZfgLexer.BIT -> Types.BIT;
//       case ZfgLexer.I08 -> Types.I08;
//       case ZfgLexer.I16 -> Types.I16;
//       case ZfgLexer.I32 -> Types.I32;
//       case ZfgLexer.I64 -> Types.I64;
//       case ZfgLexer.U08 -> Types.U08;
//       case ZfgLexer.U16 -> Types.U16;
//       case ZfgLexer.U32 -> Types.U32;
//       case ZfgLexer.U64 -> Types.U64;
//       case ZfgLexer.F32 -> Types.F32;
//       case ZfgLexer.F64 -> Types.F64;
//       default -> throw new AssertionError();
//     };
//   }

//   public Type parseFunctionType(final FunctionTypeContext ctx) {
//     // Post-order traversal
//     final Type paramsType = parseRecordType(ctx.paramsType);
//     final Type returnType = parseType(ctx.returnType);
//     // Error checking
//     if (paramsType == Types.ERR || returnType == Types.ERR) return Types.ERR;
//     // Type inference
//     return Types.Fun(paramsType, returnType);
//   }

//   public Type parseRecordType(final RecordTypeContext ctx) {
//     // Post-order traversal
//     final int length = ctx.types.size();
//     final boolean[] muts = new boolean[length];
//     for (int i = 0; i < muts.length; i++) muts[i] = ctx.muts.get(i) == null;
//     final String[] names = new String[length];
//     for (int i = 0; i < names.length; i++) names[i] = ctx.names.get(i).getText();
//     final Type[] types = new Type[length];
//     for (int i = 0; i < types.length; i++) types[i] = parseType(ctx.types.get(i));
//     // Error checking
//     if (hasDuplicates(names)) { err(ctx, "Record field names must be unique"); return Types.ERR; }
//     for (int i = 0; i < types.length; i++) if (types[i] == Types.ERR) return Types.ERR;
//     // Type inference
//     return Types.Rec(muts, names, types);
//   }

//   public Type parseTupleType(final TupleTypeContext ctx) {
//     // Post-order traversal
//     final int length = ctx.types.size();
//     final boolean[] muts = new boolean[length];
//     for (int i = 0; i < muts.length; i++) muts[i] = ctx.muts.get(i) == null;
//     final Type[] types = new Type[length];
//     for (int i = 0; i < types.length; i++) types[i] = parseType(ctx.types.get(i));
//     // Error checking
//     for (int i = 0; i < types.length; i++) if (types[i] == Types.ERR) return Types.ERR;
//     // Type inference
//     return Types.Tup(muts, types);
//   }

//   public Type parseArrayType(final ArrayTypeContext ctx) {
//     // Post-order traversal
//     final boolean mut = ctx.mut != null;
//     final Type type = parseType(ctx.elem);
//     final int length = switch (ctx.length) {
//       case null -> Type.Arr.UNKNOWN_LENGTH;
//       case Token t when Literals.parseIntLit(t.getText(), false) instanceof Inst.I32 i -> i.value;
//       default -> -2;
//     };
//     // Error checking
//     if (length == -2) { err(ctx, "Array length must be an i32 literal"); return Types.ERR; }
//     if (type == Types.ERR) return Types.ERR;
//     // Type inference
//     return length == -1 ? Types.Arr(mut, type) : Types.Arr(mut, type, length);
//   }

//   public Type parseNamedType(final NamedTypeContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseNamedType'");
//   }

//   private static boolean hasDuplicates(final String[] names) {
//     final int length = names.length;
//     if (length < 32) {
//       for (int i = length - 1; i >= 1; i -= 1) {
//         final String name = names[i];
//         for (int j = i - 1; j >= 0; j -= 1) {
//           if (name.equals(names[j])) {
//             return true;
//           }
//         }
//       }
//       return false;
//     } else {
//       final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
//       for (int i = 0; i < length; i++) {
//         if (!set.add(names[i])) {
//           return true;
//         }
//       }
//       return false;
//     }
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Function Call
//   //////////////////////////////////////////////////////////////////////////////////////////////////


//   public void parseFunctionCall(final FunctionCallContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseFunctionCall'");
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Variable
//   //////////////////////////////////////////////////////////////////////////////////////////////////


//   public void parseVariable(final VariableContext ctx) {
//     throw new UnsupportedOperationException("Unimplemented method 'parseVariable'");
//   }
// }
