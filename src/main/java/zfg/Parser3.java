// package zfg;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// import org.antlr.v4.runtime.CharStream;
// import org.antlr.v4.runtime.CharStreams;
// import org.antlr.v4.runtime.CommonTokenStream;
// import org.antlr.v4.runtime.ParserRuleContext;
// import org.antlr.v4.runtime.Token;

// import zfg.Ast.FunRef;
// import zfg.Ast.VarRef;
// import zfg.Ast.VarRef.Site;
// import zfg.Path.Segment.Index;
// import zfg.Path.Segment.Named;
// import zfg.antlr.ZfgLexer;
// import zfg.antlr.ZfgParser;
// import zfg.antlr.ZfgParser.AnyTypeContext;
// import zfg.antlr.ZfgParser.ArrTypeContext;
// import zfg.antlr.ZfgParser.ArrayTypeContext;
// import zfg.antlr.ZfgParser.AssignmentStatementContext;
// import zfg.antlr.ZfgParser.ExpressionContext;
// import zfg.antlr.ZfgParser.FunTypeContext;
// import zfg.antlr.ZfgParser.FunctionDeclarationContext;
// import zfg.antlr.ZfgParser.FunctionTypeContext;
// import zfg.antlr.ZfgParser.IndexPathSegmentContext;
// import zfg.antlr.ZfgParser.InvocationStatementContext;
// import zfg.antlr.ZfgParser.LiteralContext;
// import zfg.antlr.ZfgParser.ModuleContext;
// import zfg.antlr.ZfgParser.NamedPathSegmentContext;
// import zfg.antlr.ZfgParser.NomTypeContext;
// import zfg.antlr.ZfgParser.NumericLitContext;
// import zfg.antlr.ZfgParser.NumericLiteralContext;
// import zfg.antlr.ZfgParser.PathContext;
// import zfg.antlr.ZfgParser.PrefixOpExprContext;
// import zfg.antlr.ZfgParser.PriTypeContext;
// import zfg.antlr.ZfgParser.PrimitiveTypeContext;
// import zfg.antlr.ZfgParser.RecTypeContext;
// import zfg.antlr.ZfgParser.RecordTypeContext;
// import zfg.antlr.ZfgParser.StatementContext;
// import zfg.antlr.ZfgParser.TupTypeContext;
// import zfg.antlr.ZfgParser.TupleTypeContext;
// import zfg.antlr.ZfgParser.TypeDeclarationContext;
// import zfg.antlr.ZfgParser.VarTypeContext;
// import zfg.antlr.ZfgParser.VariableDeclarationContext;
// import zfg.antlr.ZfgParser.VariableTypeContext;

// public final class Parser3 {

//   public static interface Result {
//     public static final record Val(Ast value) implements Result {}
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
//     final ZfgLexer zfgLexer = new ZfgLexer(source);
//     final CommonTokenStream tokens = new CommonTokenStream(zfgLexer);
//     System.out.println("tokens: " + PrettyPrint.tokenStream(zfgLexer, tokens));

//     // Syntax Analysis
//     final ZfgParser zfgParser = new ZfgParser(tokens);
//     final ModuleContext parsed = zfgParser.module();
//     System.out.println("parsed: " + parsed.toStringTree(zfgParser));
//     System.out.println("tree:\n" + PrettyPrint.syntaxTree(zfgParser, parsed));

//     // Semantic Analysis
//     final Parser3 parser = new Parser3();
//     final Ast.Module root = parser.parseModule(parsed, source.getSourceName());
//     final List<Error> errors = parser.errors();
//     System.out.println("parsed: " + root);
//     System.out.println("tree: " + root);
//     System.out.println("errors: " + Arrays.toString(errors.toArray(Error[]::new)));
//     return errors.isEmpty() ? new Result.Val(root) : new Result.Err(errors);
//   }

//   private Parser3() {}

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Errors
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   /** Parser error */
//   private static final class Error {
//     final ParserRuleContext ctx; // The most speicific context for where the error occurred
//     final String msg;            // The error message

//     private Error(final ParserRuleContext ctx, final String msg) {
//       this.ctx = ctx;
//       this.msg = msg;
//     }

//     @Override public String toString() {
//       final int line = ctx.start.getLine();
//       final int column = ctx.start.getPosInLine();
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

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Symbol Table
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private final Symbol2.Table symbolTable = new Symbol2.Table();

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Module
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private Ast.Module parseModule(final ModuleContext ctx, final String fqn) {
//     final List<StatementContext> statements = ctx.scope().stmts;
//     final int statementCount = statements.size();

//     // Create the module scope
//     symbolTable.pushModuleScope();

//     // Push forward-declarations to the symbol table
//     for (int i = 0; i < statementCount; i++) {
//       switch (statements.get(i)) {
//         case TypeDeclarationContext decl -> {
//           final String name = decl.name.getText();
//           symbolTable.pushTypeDefn(decl, name, new Type.Nom(name));
//         }
//         case FunctionDeclarationContext decl -> {
//           final String name = decl.name.getText();
//           symbolTable.pushFunction(decl, name, new FunRef(Types.UnkFun, name, fqn));
//         }
//         case VariableDeclarationContext decl -> {
//           final String name = decl.name.getText();
//           symbolTable.pushVariable(decl, name, new VarRef(Types.UNK, Site.Static, name, fqn));
//         }
//         default -> {}
//       }
//     }

//     // Parse each statement
//     final Ast.Stmt[] stmts = new Ast.Stmt[statementCount];
//     for (int i = 0; i < statementCount; i++) stmts[i] = parseStatement(statements.get(i));

//     // Pop the module scope
//     final Symbol2.Scope.Module scope = symbolTable.popModuleScope();

//     // Error propagation
//     for (int i = 0; i < statementCount; i++) if (stmts[i] == null) return null;

//     // Construct the node
//     return new Ast.Module(fqn, stmts);
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Expressions
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private Ast.Expr parseExpression(final ExpressionContext ctx) {
//     throw new UnsupportedOperationException("TODO");
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Paths
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private Path parsePath(final PathContext ctx) {
//     try {
//     // Parse the module path
//     final int modulePathLength = ctx.modulePath.size();
//     final String[] modulePath = new String[modulePathLength];
//     for (int i = 0; i < modulePathLength; i++) modulePath[i] = ctx.modulePath.get(i).getText();
//     // Parse the symbol name
//     final String symbolName = ctx.symbol.getText();
//     // Parse the field path
//     final int fieldPathLength = ctx.fieldPath.size();
//     final Path.Segment[] fieldPath = new Path.Segment[fieldPathLength];
//     for (int i = 0; i < fieldPathLength; i++) {
//       fieldPath[i] = switch (ctx.fieldPath.get(i)) {
//         case NamedPathSegmentContext seg -> {
//           yield new Path.Segment.Named(seg.name.getText());
//         }
//         case IndexPathSegmentContext seg -> {
//           final Ast.Expr index = parseExpression(seg.index);
//           yield index == null ? null : new Path.Segment.Index(index);
//         }
//         default -> throw new AssertionError();
//       };
//     }
//     // Error propagation
//     for (int i = 0; i < fieldPathLength; i++) if (fieldPath[i] == null) return null;
//     // Construct the path
//     return new Path(modulePath, symbolName, fieldPath);
//     } catch (final Exception e) {
//       System.out.println("ctx: " + ctx);
//       System.out.println("ctx: " + ctx.getText());
//       System.out.println("ctx: " + Arrays.toString(ctx.modulePath.toArray()));
//       System.out.println("ctx: " + ctx.symbol);
//       System.out.println("ctx: " + Arrays.toString(ctx.fieldPath.toArray()));
//       throw e;
//     }
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Literals
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private Inst parseLiteral(final LiteralContext ctx) {
//     return switch (ctx) {
//       case NumericLitContext lit -> parseNumericLit(lit.lit);
//       default -> throw new AssertionError();
//     };
//   }

//   private Inst parseNumericLit(final NumericLiteralContext ctx) {
//     final String text = ctx.token.getText();
//     final int    type = ctx.token.getType();
//     switch (type) {
//       case ZfgLexer.BitLit: {
//         final Inst parsed = Literals.parseBitLit(text);
//         if (parsed == null) err(ctx, "Invalid bit literal: \"" + text + "\"");
//         return parsed;
//       }
//       case ZfgLexer.IntLit: {
//         final boolean hasMinusPrefix =
//             ctx.parent instanceof PrefixOpExprContext parent &&
//             parent.opr.getType() == ZfgLexer.SUB &&
//             parent.opr.getStopIndex() + 1 == ctx.getStart().getStartIndex();
//         final Inst parsed = Literals.parseIntLit(text, hasMinusPrefix);
//         if (parsed == null) err(ctx, "Invalid int literal: \"" + text + "\"");
//         return parsed;
//       }
//       case ZfgLexer.FltLit: {
//         final Inst parsed = Literals.parseFltLit(text);
//         if (parsed == null) err(ctx, "Invalid flt literal: \"" + text + "\"");
//         return parsed;
//       }
//       default: throw new AssertionError();
//     }
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Types
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private Type parseAnyType(final AnyTypeContext ctx) {
//     return switch (ctx) {
//       case FunTypeContext type -> parseFunctionType(type.type);
//       case VarTypeContext type -> parseVariableType(type.type);
//       default -> throw new AssertionError();
//     };
//   }

//   private Type parseVariableType(final VariableTypeContext ctx) {
//     return switch (ctx) {
//       case RecTypeContext type -> parseRecordType(type.type);
//       case TupTypeContext type -> parseTupleType(type.type);
//       case ArrTypeContext type -> parseArrayType(type.type);
//       case PriTypeContext type -> parsePrimitiveType(type.type);
//       case NomTypeContext type -> parseNomType(type);
//       default -> throw new AssertionError();
//     };
//   }

//   private Type parseFunctionType(final FunctionTypeContext ctx) {
//     final List<Token>          fieldModifiers = ctx.parameterModifiers;
//     final List<Token>          fieldNames = ctx.parameterNames;
//     final List<AnyTypeContext> fieldTypes = ctx.parameterTypes;
//     final int fieldCount = fieldModifiers.size();
//     assert fieldNames.size() == fieldCount;
//     assert fieldTypes.size() == fieldCount;
//     // Parse the parameter modifiers
//     final boolean[] imuts = new boolean[fieldCount];
//     for (int i = 0; i < fieldCount; i++) imuts[i] = parseTypeModifier(fieldModifiers.get(i));
//     // Parse the parameter names
//     final String[] names = new String[fieldCount];
//     for (int i = 0; i < fieldCount; i++) names[i] = fieldNames.get(i).getText();
//     // Parse the parameter types
//     final Type[] typez = new Type[fieldCount];
//     for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
//     // Parse the reutrn type
//     final Type returnType = parseAnyType(ctx.returnType);
//     // Unique names check
//     if (!allDistinct(names)) return Types.ERR;
//     // Error propagation
//     for (int i = 0; i < fieldCount; i++) if (typez[i] == Types.ERR) return Types.ERR;
//     if (returnType == Types.ERR) return Types.ERR;
//     // Construct the node
//     final Type.Rec parametersType = (Type.Rec) Types.Rec(imuts, names, typez);
//     return Types.Fun(parametersType, returnType);
//   }

//   private Type parseRecordType(final RecordTypeContext ctx) {
//     final List<Token>          fieldModifiers = ctx.fieldModifiers;
//     final List<Token>          fieldNames = ctx.fieldNames;
//     final List<AnyTypeContext> fieldTypes = ctx.fieldTypes;
//     final int fieldCount = fieldModifiers.size();
//     assert fieldNames.size() == fieldCount;
//     assert fieldTypes.size() == fieldCount;
//     // Parse the modifiers
//     final boolean[] muts = new boolean[fieldCount];
//     for (int i = 0; i < fieldCount; i++) muts[i] = parseTypeModifier(fieldModifiers.get(i));
//     // Parse the names
//     final String[] names = new String[fieldCount];
//     for (int i = 0; i < fieldCount; i++) names[i] = fieldNames.get(i).getText();
//     // Parse the types
//     final Type[] typez = new Type[fieldCount];
//     for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
//     // Unique names check
//     if (!allDistinct(names)) return Types.ERR;
//     // Error propagation
//     for (int i = 0; i < fieldCount; i++) if (typez[i] == Types.ERR) return Types.ERR;
//     // Construct the node
//     return Types.Rec(muts, names, typez);
//   }

//   private Type parseTupleType(final TupleTypeContext ctx) {
//     final List<Token>          fieldModifiers = ctx.fieldModifiers;
//     final List<AnyTypeContext> fieldTypes = ctx.fieldTypes;
//     final int fieldCount = fieldModifiers.size();
//     assert fieldTypes.size() == fieldCount;
//     // Parse the modifiers
//     final boolean[] muts = new boolean[fieldCount];
//     for (int i = 0; i < fieldCount; i++) muts[i] = parseTypeModifier(fieldModifiers.get(i));
//     // Parse the types
//     final Type[] typez = new Type[fieldCount];
//     for (int i = 0; i < fieldCount; i++) typez[i] = parseAnyType(fieldTypes.get(i));
//     // Error propagation
//     for (int i = 0; i < fieldCount; i++) if (typez[i] == Types.ERR) return Types.ERR;
//     // Construct the node
//     return Types.Tup(muts, typez);
//   }

//   private Type parseArrayType(final ArrayTypeContext ctx) {
//     // Parse the modifier
//     final boolean imut = switch (ctx.elementsModifier.getType()) {
//       case ZfgLexer.LET -> true;
//       case ZfgLexer.MUT -> false;
//       default -> throw new AssertionError();
//     };
//     // Parse the type
//     final Type type = parseAnyType(ctx.elementsType);
//     // Parse the length
//     final int length;
//     if (ctx.elementCount == null) {
//       length = -1;
//     } else if (Literals.parseIntLit(ctx.elementCount.getText(), false) instanceof Inst.I32 i32) {
//       length = i32.value;
//     } else {
//       err(ctx, "Invalid array length: " + ctx.elementCount.getText());
//       length = -2;
//     }
//     // Error propagation
//     if (type == Types.ERR || length == -2) return Types.ERR;
//     // Construct the node
//     return length == -1
//       ? Types.Arr(imut, type)
//       : Types.Arr(imut, type, length);
//   }

//   private Type parsePrimitiveType(final PrimitiveTypeContext ctx) {
//     return switch (ctx.token.getType()) {
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

//   private Type parseNomType(final NomTypeContext ctx) {
//     // Parse the path
//     final Path path = parsePath(ctx.type);
//     // Error propagation
//     if (path == null) return Types.ERR;
//     // Lookup the symbol
//     final String symbolName = path.qualifiedName();
//     final Symbol2 symbol = symbolTable.getSymbol(symbolName);
//     if (symbol == null) {
//       err(ctx, "Undefined type: " + path.qualifiedName());
//       return Types.ERR;
//     }
//     if (!(symbol instanceof Symbol2.TypeDefn)) {
//       err(ctx, "Not a type: " + path.qualifiedName());
//       return Types.ERR;
//     }
//     // Follow the field path
//     Type type = symbol.type;
//     for (int p = 0; p < path.fieldPath.length; p++) {
//       // Get the aliased type
//       while (type instanceof Type.Nom nom) type = nom.type;
//       // Get the next field path segment
//       switch (path.fieldPath[p]) {
//         case Named named -> {
//           final String key = named.named;
//           switch (type) {
//             case Type.Rec rec -> {
//               for (int idx = 0; idx < rec.names.length; idx++) {
//                 if (rec.names[idx].equals(key)) {
//                   type = rec.types[idx];
//                   continue;
//                 }
//               }
//             }
//             case Type.Fun fun -> {
//               // TODO: This overlaps the keyword "return"
//               if (key.equals("return")) {
//                 type = fun.returnType;
//                 continue;
//               }
//               if (fun.paramsType instanceof Type.Rec rec) {
//                 for (int idx = 0; idx < rec.names.length; idx++) {
//                   if (rec.names[idx].equals(key)) {
//                     type = rec.types[idx];
//                     continue;
//                   }
//                 }
//               }
//             }
//             default -> {}
//           }
//         }
//         case Index index -> {
//           final Ast.Expr expr = index.index;
//           // TODO: Attempt to evaluate the index expression
//           if (expr instanceof Ast.ConstExpr constExpr && constExpr.val instanceof Inst.I32 i32) {
//             final int idx = i32.value;
//             switch (type) {
//               case Type.Arr arr -> {
//                 if (idx >= 0 && (idx < arr.length || arr.length == -1)) {
//                   type = arr.type;
//                   continue;
//                 }
//               }
//               case Type.Tup tup -> {
//                 if (idx >= 0 && idx < tup.types.length) {
//                   type = tup.types[idx];
//                   continue;
//                 }
//               }
//               case Type.Rec rec -> {
//                 if (idx >= 0 && idx < rec.types.length) {
//                   type = rec.types[idx];
//                   continue;
//                 }
//               }
//               case Type.Fun fun -> {
//                 if (idx == -1) {
//                   type = fun.returnType;
//                   continue;
//                 }
//                 if (fun.paramsType instanceof Type.Rec rec && idx >= 0 && idx < rec.types.length) {
//                   type = rec.types[idx];
//                   continue;
//                 }
//               }
//               default -> {}
//             }
//           }
//         }
//       }
//       err(ctx, "Cannot resolve type: " + ctx.type.getText());
//       return Types.ERR;
//     }

//     if (type == Types.ERR || type == Types.UNK || type == Types.UnkFun) {
//       err(ctx, "Cannot resolve type: " + ctx.type.getText());
//       return Types.ERR;
//     }

//     return type;
//   }

//   private static boolean parseTypeModifier(final Token token) {
//     return switch (token.getType()) {
//       case ZfgLexer.LET -> true;
//       case ZfgLexer.MUT -> false;
//       default -> throw new AssertionError();
//     };
//   }

//   private static boolean allDistinct(final String[] names) {
//     final int length = names.length;
//     if (length < 32) {
//       for (int i = length - 1; i >= 1; i -= 1) {
//         final String name = names[i];
//         for (int j = i - 1; j >= 0; j -= 1) if (name.equals(names[j])) return false;
//       }
//       return true;
//     } else {
//       final java.util.HashSet<String> set = new java.util.HashSet<>(length, 0.5f);
//       for (int i = 0; i < length; i++) if (!set.add(names[i])) return false;
//       return true;
//     }
//   }

//   //////////////////////////////////////////////////////////////////////////////////////////////////
//   // Statements
//   //////////////////////////////////////////////////////////////////////////////////////////////////

//   private Ast.Stmt parseStatement(final StatementContext ctx) {
//     return switch (ctx) {
//       case TypeDeclarationContext     decl -> parseTypeDeclaration(decl);
//       case FunctionDeclarationContext decl -> parseFunctionDeclaration(decl);
//       case VariableDeclarationContext decl -> parseVariableDeclaration(decl);
//       case AssignmentStatementContext stmt -> parseAssignmentStatement(stmt);
//       case InvocationStatementContext stmt -> parseInvocationStatement(stmt);
//       default -> throw new AssertionError();
//     };
//   }
//   private Ast.TypeDecl parseTypeDeclaration(final TypeDeclarationContext ctx) {
//     // Parse the name
//     final String name = ctx.name.getText();
//     // Parse the rhs
//     final Type rhs = parseAnyType(ctx.rhs);
//     // Error propagation
//     if (rhs == Types.ERR) return null;
//     // Get the symbol
//     final Symbol2.TypeDefn symbol = (Symbol2.TypeDefn) symbolTable.getSymbol(name);
//     assert symbol instanceof Symbol2.TypeDefn;
//     // Get the nominal type
//     final Type.Nom type = symbol.type;
//     // Bind the definition to the nominal type
//     type.bind(rhs);
//     // Construct the node
//     return new Ast.TypeDecl(type);
//   }

//   private Ast.Stmt parseFunctionDeclaration(final FunctionDeclarationContext ctx) {
//     throw new UnsupportedOperationException("TODO");
//   }

//   private Ast.Stmt parseVariableDeclaration(final VariableDeclarationContext ctx) {
//     throw new UnsupportedOperationException("TODO");
//   }

//   private Ast.Stmt parseAssignmentStatement(final AssignmentStatementContext ctx) {
//     throw new UnsupportedOperationException("TODO");
//   }

//   private Ast.Stmt parseInvocationStatement(final InvocationStatementContext ctx) {
//     throw new UnsupportedOperationException("TODO");
//   }
// }
