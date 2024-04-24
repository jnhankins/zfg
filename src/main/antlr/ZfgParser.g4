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


module
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
  : imu=(LET | MUT) id=LowerId type
  ;

statement
  : mod=(LET | MUT | PUB | USE) id=LowerId type SETA (expression | block)      # DeclarationStmt
  | assignment                                                                 # AssignmentStmt
  | functionCall                                                               # FunctionCallStmt
  | RETURN expression?                                                         # FunctionReturnStmt
  | IF expression block (ELSE IF expression block)* (ELSE block)?              # IfElseStmt
  | LOOP block                                                                 # LoopStmt
  | WHILE expression block                                                     # LoopWhileStmt
  | FOR expression SEMIC expression SEMIC expression block                     # LoopForStmt
  | BREAK                                                                      # LoopBreakStmt
  | CONTINUE                                                                   # LoopContinueStmt
  ;
block
  : LBRACE (statement (SEMIC | {EOL()}?))* RBRACE
  ;

expression
  : functionCall                                             # FunctionCallExpr
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

assignment
  : lhs=identifier op=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA) rhs=expression
  ;

functionCall
  : identifier LPAREN (expression (COMMA expression)* COMMA?)? RPAREN
  ;

identifier
  : part=(UpperId | LowerId) ('.' part=(UpperId | LowerId))*
  ;
