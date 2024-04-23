parser grammar ZfgParser;
options { tokenVocab = ZfgLexer; }

@parser::members {
  final CommonTokenStream _cts = (CommonTokenStream) _input;
  int _eolCache = 0;
  boolean EOL() {
    final int i = _cts.index();
    if (i == 0) return false;
    if (_eolCache == +i) return true;
    if (_eolCache == -i) return false;
    for (int j = i - 1; j >= 0; j -= 1) {
      final Token t = _cts.get(j);
      if (t.getType() == ZfgLexer.WsEol) { _eolCache = +i; return true; }
      if (t.getChannel() == Token.DEFAULT_CHANNEL) break;
    }
    _eolCache = -i;
    return false;
  }
}

// TODO: more types, e.g. arrays, structs, algebraic data types, dynamic types, etc.
// TODO: match expression


compilationUnit
  : (statement (SEMIC | {EOL()}?))*
  ;

// TODO type alias, e.g. "Node"
// TODO comptime dynamic type, e.g. "Pair(i32, f64)"
// TODO type inference, e.g. "var", "fun", "type", etc.
type
  : token=(BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64)   # PrimitiveType
  | LPAREN (functionParameter (COMMA functionParameter)* COMMA?)? RPAREN type # FunctionType
  ;
functionParameter
  : modifier=(LET | MUT) id=LowerId type
  ;

statement
  : modifier=(LET | MUT) id=LowerId type SETA expression          # DeclarationStmt
  | assignment                                                    # AssignmentStmt
  | invocation                                                    # FunctionCallStmt
  | IF expression block (ELSE IF expression block)* (ELSE block)? # IfElseStmt
  | LOOP block                                                    # LoopStmt
  | WHILE expression block                                        # LoopWhileStmt
  | FOR expression SEMIC expression SEMIC expression block        # LoopForStmt
  | BREAK                                                         # LoopBreakStmt
  | CONTINUE                                                      # LoopContinueStmt
  | RETURN expression?                                            # FunctionReturnStmt
  ;

expression
  : invocation                                               # FunctionCallExpr
  | identifier                                               # VariableExpr
  | lit=(BitLit | IntLit | FltLit)                           # LiteralExpr
  | LPAREN expression RPAREN                                 # GroupedExpr
  | lhs=expression op=(INC | DEC)                            # PostfixOpExpr
  | op=(INC | DEC | ADD | SUB | NOT) rhs=expression          # PrefixOpExpr
  | lhs=expression op=(MUL | DIV | REM | MOD) rhs=expression # InfixOpExpr
  | lhs=expression op=(ADD | SUB) rhs=expression             # InfixOpExpr
  | lhs=expression op=(SHL | SHR) rhs=expression             # InfixOpExpr
  | lhs=expression op=AND rhs=expression                     # InfixOpExpr
  | lhs=expression op=XOR rhs=expression                     # InfixOpExpr
  | lhs=expression op=IOR rhs=expression                     # InfixOpExpr
  | lhs=expression op=CMP rhs=expression                     # InfixOpExpr
  | lhs=expression op=(LTN | GTN | LEQ | GEQ) rhs=expression # InfixOpExpr
  | lhs=expression op=(EQL | NEQ)  rhs=expression            # InfixOpExpr
  | assignment                                               # AssignmentExpr
  | lhs=expression op=(LCJ | LDJ) rhs=expression             # InfixOpExpr
  ;

block
  : LBRACE (statement (SEMIC | {EOL()}?))* RBRACE
  ;

assignment
  : lhs=identifier op=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA) rhs=expression
  ;

invocation
  : identifier LPAREN (expression (COMMA expression)* COMMA?)? RPAREN
  ;

identifier
  : part=(UpperId | LowerId) ('.' part=(UpperId | LowerId))*
  ;

// x
// x, x
// x, x,

// compilationUnit
//   : nonretBlock EOF
//   ;

// nonretBlock
//   : (nonretStatement (SEMIC | {EOL()}?))*
//   ;

// returnBlock
//   : (nonretStatement (SEMIC | {EOL()}?))* returnStatement (SEMIC | {EOL()}?)
//   ;

// nonretStatement
//   : functionDeclaration
//   | variableDeclaration
//   | assignment
//   | invocation
//   ;

// returnStatement
//   : RET expression
//   ;

// variableDeclaration
//   : mod=(LET | MUT) id=LowerId (COLON type=(VAR | I08 | I16 | I32 | I64 | F32 | F64))? SETA rhs=expression
//   ;

// functionDeclaration
//   : FUN id=LowerId LPAREN RPAREN LBRACE returnBlock RBRACE // todo: parameters and return type
//   ;

// expression
//   : invocation                                               # InvocationExpr
//   | path                                                     # VariableExpr
//   | lit=(BitLit | IntLit | FltLit)                           # LiteralExpr
//   | LPAREN expression RPAREN                                 # GroupedExpr
//   | lhs=expression op=(INC | DEC)                            # PostfixOpExpr
//   | op=(INC | DEC | ADD | SUB | NOT) rhs=expression          # PrefixOpExpr
//   | lhs=expression op=(MUL | DIV | REM | MOD) rhs=expression # InfixOpExpr
//   | lhs=expression op=(ADD | SUB) rhs=expression             # InfixOpExpr
//   | lhs=expression op=(SHL | SHR) rhs=expression             # InfixOpExpr
//   | lhs=expression op=AND rhs=expression                     # InfixOpExpr
//   | lhs=expression op=XOR rhs=expression                     # InfixOpExpr
//   | lhs=expression op=IOR rhs=expression                     # InfixOpExpr
//   | lhs=expression op=CMP rhs=expression                     # InfixOpExpr
//   | lhs=expression op=(LTN | GTN | LTE | GTE) rhs=expression # InfixOpExpr
//   | lhs=expression op=(EQL | NEQ)  rhs=expression            # InfixOpExpr
//   | assignment                                               # AssignmentExpr
//   ;

// assignment
//   : lhs=path op=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA) rhs=expression
//   ;

// invocation
//   : path LPAREN RPAREN // todo: parameters
//   ;

// path
//   : part=(UpperId | LowerId) ('.' part=(UpperId | LowerId))*
//   ;

/**
 * fun main(x: i32): i32 {
 *   let y: i32 = x + 1
 *   if (x <)
 *   ret y
 * }
 */
