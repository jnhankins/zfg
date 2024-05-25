parser grammar ZfgParser;
options {
  tokenVocab = ZfgLexer;
  contextSuperClass = zfg.antlr.ZfgContext;
}

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


////////////////////////////////////////////////////////////////////////////////////////////////////
// Module
////////////////////////////////////////////////////////////////////////////////////////////////////

module
  : body=scope[0] EOF
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Statements
////////////////////////////////////////////////////////////////////////////////////////////////////

// s   == 0 => module scope
// s&1 != 0 => inside function
// s&2 != 0 => inside loop
scope[int s]
  : ({$s == 0}? | {$s != 0}? LBRACE)
    stmts+=statement[$s] ( (SEMIC | {EOL()}?)
    stmts+=statement[$s] )* SEMIC?
    ({$s == 0}? | {$s != 0}? RBRACE)
  ;

statement[int s]
  : typeDeclaration[$s]
  | functionDeclaration[$s]
  | variableDeclaration[$s]
  | ifelseStatement[$s]
  | loopStatement[$s]
  | {($s&1) != 0}? returnStatement
  | {($s&2) != 0}? loopControlStatement
  | expression
  ;

typeDeclaration[int s]
  : ({$s == 0}? mod=PUB | mod=LET | mod=MUT) name=UpperId SETI rhs=type
  ;

functionDeclaration[int s]
  : ({$s == 0}? mod=PUB | mod=LET | mod=MUT) name=LowerId (SETI | COLON typed=functionType[false] SETA) rhs=functionConstructor
  ;

variableDeclaration[int s]
  : ({$s == 0}? mod=PUB | mod=LET | mod=MUT) name=LowerId (SETI | COLON typed=type SETA) rhs=expression
  ;

ifelseStatement[int s]
  : IF expression scope[$s] (ELSE IF expression scope[$s])* (ELSE scope[$s])?
  ;

loopStatement[int s]
  : LOOP scope[$s|2]
  | WHILE expression scope[$s|2]
  | FOR (functionDeclaration[$s] | variableDeclaration[$s] | expression)? SEMIC (expression)? SEMIC (expression)? scope[$s|2]
  ;

returnStatement
  : RETURN rhs=expression?
  ;

loopControlStatement
  : (BREAK | CONTINUE) (name=LowerId)?
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Expressions
////////////////////////////////////////////////////////////////////////////////////////////////////

expression
  : algebraicExpression
  | bitwiseExpression
  | comparisonExpression
  | logicalExpression
  | binaryAssignment
  | unambiguousExpression
  ;

unambiguousExpression
  : unaryAssignment
  | literalExpression
  | functionCallExpression
  | pathExpression
  | recordConstructor
  | tupleConstructor
  | arrayConstructor
  | unaryExpression
  | parentheticalExpression
  ;

literalExpression
  : lit=(BitLit | IntLit | FltLit)
  ;

pathExpression
  : symbol=LowerId
  | path=pathExpression POINT  field=(LowerId | UpperId)
  | path=pathExpression LBRACK index=expression RBRACK
  ;

functionCallExpression
  : path=pathExpression (tup=tupleConstructor | rec=recordConstructor)
  ;

unaryExpression
  : opr=(ADD | SUB | NOT | LNT) rhs=unambiguousExpression
  ;

parentheticalExpression
  : LBRACE expr=expression RBRACE
  ;

algebraicExpression
  : unambiguousExpression opr=(MUL | DIV | REM | MOD) (unambiguousExpression | algebraicExpression)
  | algebraicExpression   opr=(MUL | DIV | REM | MOD) (unambiguousExpression | algebraicExpression)
  | unambiguousExpression opr=(ADD | SUB) (unambiguousExpression | algebraicExpression)
  | algebraicExpression   opr=(ADD | SUB) (unambiguousExpression | algebraicExpression)
  ;

bitwiseExpression
  : opds+=unambiguousExpression (opr=AND opds+=unambiguousExpression)+
  | opds+=unambiguousExpression (opr=IOR opds+=unambiguousExpression)+
  | opds+=unambiguousExpression (opr=XOR opds+=unambiguousExpression)+
  | lhs=unambiguousExpression opr=(SHL | SHR) rhs=unambiguousExpression
  ;

comparisonExpression
  : opds+=comparisonOperand (oprs+=(EQL | NEQ | LTN | GTN | LEQ | GEQ) opds+=comparisonOperand)+
  | lhs=comparisonOperand opr=TWC rhs=comparisonOperand
  ;
comparisonOperand
  : bitwiseExpression
  | algebraicExpression
  | unambiguousExpression
  ;

logicalExpression
  : opds+=logicalOperand (opr=LCJ opds+=logicalOperand)+
  | opds+=logicalOperand (opr=LDJ opds+=logicalOperand)+
  ;
logicalOperand
  : comparisonExpression
  | bitwiseExpression
  | algebraicExpression
  | unambiguousExpression
  ;

unaryAssignment
  : lhs=pathExpression opr=(INC | DEC)
  | opr=(INC | DEC) rhs=pathExpression
  ;

binaryAssignment
  : lhs=pathExpression opr=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA) rhs=expression
  ;

recordConstructor
  : LPAREN (
    names+=LowerId COLON exprs+=expression (  COMMA
    names+=LowerId COLON exprs+=expression )* COMMA?)?
    RPAREN
  ;

tupleConstructor
  : LPAREN (
    exprs+=expression (  COMMA
    exprs+=expression )* COMMA?)?
    RPAREN
  ;

arrayConstructor
  : LBRACK (
    exprs+=expression (  COMMA
    exprs+=expression )* COMMA?)?
    RBRACK
  ;

functionConstructor
  : typed=functionType[true] body=scope[1]
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Types
////////////////////////////////////////////////////////////////////////////////////////////////////

type
  : primitiveType
  | functionType[false]
  | recordType
  | tupleType
  | arrayType
  | namedType
  ;

primitiveType
  : token=(BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64)
  ;

functionType[boolean requireNames]
  : (rec=recordType | {!$requireNames}? tup=tupleType) ARROW ret=type
  ;

recordType
  : LPAREN (fields+=recordField (COMMA fields+=recordField)* COMMA?)? RPAREN
  ;

recordField
  : muta=(MUT|LET)? name=LowerId COLON typed=type
  ;

tupleType
  : LPAREN (tupleField (COMMA tupleField)* COMMA?)? RPAREN
  ;

tupleField
  : muta=(MUT|LET)? name=LowerId COLON typed=type
  ;

arrayType
  : LBRACK muta=MUT? typed=type (SEMIC size=IntLit)? RBRACK
  ;

namedType
  : name=UpperId
  ;
