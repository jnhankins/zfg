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

scope[int depth]
  : ({$depth == 0}? | {$depth > 0}? LBRACE)
    stmts+=statement[depth] ( (SEMIC | {EOL()}?)
    stmts+=statement[depth] )* SEMIC?
    ({$depth == 0}? | {$depth > 0}? RBRACE)
  ;

statement[int depth]
  : typeDeclaration[$depth]
  | functionDeclaration[$depth]
  | variableDeclaration[$depth]
  | expression
  ;

typeDeclaration[int depth]
  : ({$depth == 0}? mod=PUB | mod=LET | mod=MUT) name=UpperId COLON typed=TYPE SETA rhs=type
  ;

functionDeclaration[int depth]
  : ({$depth == 0}? mod=PUB | mod=LET | mod=MUT) name=LowerId COLON typed=functionType SETA (rhs=expression | block=scope[$depth+1])
  ;

variableDeclaration[int depth]
  : ({$depth == 0}? mod=PUB | mod=LET | mod=MUT) name=LowerId COLON typed=type SETA (rhs=expression | block=scope[$depth+1])
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Expressions
////////////////////////////////////////////////////////////////////////////////////////////////////

expression
  : unambiguousExpression
  | algebraicExpression
  | bitwiseExpression
  | comparisonExpression
  | logicalExpression
  | binaryAssignment
  ;

unambiguousExpression
  : unaryAssignment
  | literalExpression
  | pathExpression
  | functionCallExpression
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

////////////////////////////////////////////////////////////////////////////////////////////////////
// Types
////////////////////////////////////////////////////////////////////////////////////////////////////

type
  : primitiveType
  | functionType
  | recordType
  | tupleType
  | arrayType
  | namedType
  ;

primitiveType
  : token=(BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64)
  ;

functionType
  : paramsType=recordType COLON returnType=type
  ;

recordType
  : LPAREN (
    (mutas+=MUT | {$mutas.add(null);}) names+=LowerId COLON types+=type (  COMMA
    (mutas+=MUT | {$mutas.add(null);}) names+=LowerId COLON types+=type )* COMMA?)?
    RPAREN
  ;

tupleType
  : LPAREN (
    (mutas+=MUT | {$mutas.add(null);}) COLON types+=type (  COMMA
    (mutas+=MUT | {$mutas.add(null);}) COLON types+=type )* COMMA?)?
    RPAREN
  ;

arrayType
  : LBRACK
    muta=MUT? typed=type
    (SEMIC size=IntLit)?
    RBRACK
  ;

namedType
  : name=UpperId
  ;
