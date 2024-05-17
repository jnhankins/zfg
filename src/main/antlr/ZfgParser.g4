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
  : statements[0] EOF
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Statements
////////////////////////////////////////////////////////////////////////////////////////////////////

statements[int depth]
  : statement[depth] ((SEMIC | {EOL()}?) statement[depth])* SEMIC?
  ;
statement[int depth]
  : declaration[depth]
  | assignment
  | functionCall
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Declarations
////////////////////////////////////////////////////////////////////////////////////////////////////

declaration[int depth]
  : typeDeclaration[depth]
  | functionDeclaration[depth]
  | variableDeclaration[depth]
  ;
typeDeclaration[int depth]
  : ({$depth == 0}? PUB | LET | MUT) UpperId COLON TYPE SETA type
  ;
functionDeclaration[int depth]
  : ({$depth == 0}? PUB | LET | MUT) LowerId COLON functionType SETA (expression | LBRACE statements[$depth+1]? RBRACE)
  ;
variableDeclaration[int depth]
  : ({$depth == 0}? PUB | LET | MUT) LowerId COLON type SETA (expression | LBRACE statements[$depth+1]? RBRACE)
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Expressions
////////////////////////////////////////////////////////////////////////////////////////////////////

expression
  : unambiguousExpression
  | algebraExpression
  | bitwiseExpression
  | compareExpression
  | logicalExpression
  ;
unambiguousExpression
  : postfixAssignment
  | prefixAssignment
  | functionCall
  | variable
  | literal
  | prefixExpression
  | LPAREN bivariateAssignment RPAREN
  | LPAREN expression RPAREN
  ;
prefixExpression
  : opr=(ADD | SUB | NOT | LNT) unambiguousExpression
  ;
algebraExpression
  : algebraExpression     opr=(MUL | DIV | REM | MOD) (algebraExpression | unambiguousExpression)
  | unambiguousExpression opr=(MUL | DIV | REM | MOD) (algebraExpression | unambiguousExpression)
  | algebraExpression     opr=(ADD | SUB) (algebraExpression | unambiguousExpression)
  | unambiguousExpression opr=(ADD | SUB) (algebraExpression | unambiguousExpression)
  ;
bitwiseExpression
  : unambiguousExpression (opr=AND unambiguousExpression)+
  | unambiguousExpression (opr=IOR unambiguousExpression)+
  | unambiguousExpression (opr=XOR unambiguousExpression)+
  | unambiguousExpression opr=(SHL | SHR) unambiguousExpression
  ;
compareExpression
  : (bitwiseExpression | algebraExpression | unambiguousExpression) (opr=(EQL | NEQ | LTN | GTN | LEQ | GEQ) (bitwiseExpression | algebraExpression | unambiguousExpression))+
  | (bitwiseExpression | algebraExpression | unambiguousExpression) opr=TWC (bitwiseExpression | algebraExpression | unambiguousExpression)
  ;
logicalExpression
  : (compareExpression | bitwiseExpression | algebraExpression | unambiguousExpression) (opr=LCJ (compareExpression | bitwiseExpression | algebraExpression | unambiguousExpression))+
  | (compareExpression | bitwiseExpression | algebraExpression | unambiguousExpression) (opr=LDJ (compareExpression | bitwiseExpression | algebraExpression | unambiguousExpression))+
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Assignments
////////////////////////////////////////////////////////////////////////////////////////////////////

assignment
  : bivariateAssignment
  | postfixAssignment
  | prefixAssignment
  ;
bivariateAssignment
  : variable opr=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA) expression
  ;
postfixAssignment
  : variable opr=(INC | DEC)
  ;
prefixAssignment
  : opr=(INC | DEC) variable
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Literals
////////////////////////////////////////////////////////////////////////////////////////////////////

literal
  : numericLiteral
  ;
numericLiteral
  : token=(BitLit | IntLit | FltLit)
  ;
recordLiteral
  : LPAREN (LowerId SEMIC expression (COMMA SEMIC expression)* COMMA?)? RPAREN
  ;
tupleLiteral
  : LPAREN (expression (COMMA expression)* COMMA?)? RPAREN
  ;
arrayLiteral
  : LBRACK (expression (COMMA expression)* COMMA?)? RBRACK
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
    (muts+=MUT | {$muts.add(null);}) keys+=LowerId COLON types+=type (  COMMA
    (muts+=MUT | {$muts.add(null);}) keys+=LowerId COLON types+=type )* COMMA?)?
    RPAREN
  ;
tupleType
  : LPAREN (
    (muts+=MUT | {$muts.add(null);}) COLON types+=type (  COMMA
    (muts+=MUT | {$muts.add(null);}) COLON types+=type )* COMMA?)?
    RPAREN
  ;
arrayType
  : LBRACK mut=MUT? typ=type (SEMIC len=IntLit)? RBRACK
  ;
namedType
  : (UpperId DOUBC)* UpperId
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Function Call
////////////////////////////////////////////////////////////////////////////////////////////////////

functionCall
  : variable (recordLiteral | tupleLiteral)
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Variable
////////////////////////////////////////////////////////////////////////////////////////////////////

variable
  : (UpperId DOUBC)* (LowerId|UpperId) (POINT (LowerId|UpperId) | LBRACK expression RBRACK)*
  ;
