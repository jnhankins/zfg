parser grammar ZfgParser;
options { tokenVocab = ZfgLexer; }

@parser::members {
  final CommonTokenStream _cts = (CommonTokenStream) _input;
  int     _eolCacheKey = -1;
  boolean _eolCacheVal = false;
  boolean EOL() {
    final int i = _cts.index();
    if (i != _eolCacheKey) {
      _eolCacheKey = i;
      _eolCacheVal = EOL(i);
    }
    return _eolCacheResult;
  }
  boolean EOL(int i) {
    for (i -= 1; i >= 0; i -= 1) {
      final Token t = _cts.get(i);
      if (t.getType() == ZfgLexer.Nl) return true;
      if (t.getChannel() == Token.DEFAULT_CHANNEL) return false;
    }
    return false;
  }
}

start
  : returnBlock EOF
  ;

returnBlock
  : LBRACE (stmt=statement (SEMIC | {EOL()}?))* ret=return (SEMIC | {EOL()}?) RBRACE
  ;

statement
  : variable
  | assignment
  ;

variable
  : mod=(LET | MUT) id=LowerId (COLON type=(VAR | I08 | I16 | I32 | I64 | F32 | F64))? SETA rhs=expression
  ;

assignment
  : lhs=path op=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA) rhs=expression
  ;

return
  : RET expr=expression
  ;

expression
  : path                                                     # PathExpr
  | lit=(BitLit | IntLit | FltLit)                           # LiteralExpr
  | LPAREN expression RPAREN                                 # GroupExpr
  | lhs=expression op=(INC | DEC)                            # PostfixExpr
  | op=(INC | DEC | ADD | SUB | NOT) rhs=expression          # PrefixExpr
  | lhs=expression op=(MUL | DIV | REM | MOD) rhs=expression # InfixExpr
  | lhs=expression op=(ADD | SUB) rhs=expression             # InfixExpr
  | lhs=expression op=(SHL | SHR) rhs=expression             # InfixExpr
  | lhs=expression op=AND rhs=expression                     # InfixExpr
  | lhs=expression op=XOR rhs=expression                     # InfixExpr
  | lhs=expression op=IOR rhs=expression                     # InfixExpr
  | lhs=expression op=CMP rhs=expression                     # InfixExpr
  | lhs=expression op=(LT | GT | LE | GE) rhs=expression     # InfixExpr
  | lhs=expression op=(EQ | NE | EQR | NER) rhs=expression   # InfixExpr
  | assignment                                               # AssignExpr
  ;

path
  : part=identifier ('.' part=identifier)*
  ;

identifier
  : LowerId
  | UpperId
  ;
