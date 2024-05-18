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
  : ({$depth == 0}? mod=PUB | mod=LET | mod=MUT) name=UpperId COLON typ=TYPE SETA rhs=type # TypeDeclaration
  | ({$depth == 0}? mod=PUB | mod=LET | mod=MUT) name=LowerId COLON typ=functionType SETA rhs=definition[$depth] # FunctionDeclaration
  | ({$depth == 0}? mod=PUB | mod=LET | mod=MUT) name=LowerId COLON typ=type SETA rhs=expression # VariableDeclaration
  | assignment # AssignmentStatement
  | functionCall # FunctionCallStatement
  ;

// TODO: Allow VariableDeclaration's to have scoped definitions
definition[int depth]
  : expression
  | scope[$depth+1]
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
  : asn=postfixAssignment # PostfixAssignmentExpression
  | asn=prefixAssignment # PrefixAssignmentExpression
  | fun=functionCall # FunctionCallExpression
  | var=variable # VariableExpression
  | lit=literal # LiteralExpression
  | opr=(ADD | SUB | NOT | LNT) rhs=unambiguousExpression  # UnaryExpression
  | LPAREN asn=bivariateAssignment RPAREN # BivariateAssignmentExpression
  | LPAREN exp=expression RPAREN # ParentheticalExpression
  ;
algebraExpression
  : lhsA=algebraExpression     opr=(MUL | DIV | REM | MOD) rhsA=algebraExpression
  | lhsA=algebraExpression     opr=(MUL | DIV | REM | MOD) rhsU=unambiguousExpression
  | lhsU=unambiguousExpression opr=(MUL | DIV | REM | MOD) rhsA=algebraExpression
  | lhsU=unambiguousExpression opr=(MUL | DIV | REM | MOD) rhsU=unambiguousExpression
  | lhsA=algebraExpression     opr=(ADD | SUB) rhsA=algebraExpression
  | lhsA=algebraExpression     opr=(ADD | SUB) rhsU=unambiguousExpression
  | lhsU=unambiguousExpression opr=(ADD | SUB) rhsA=algebraExpression
  | lhsU=unambiguousExpression opr=(ADD | SUB) rhsU=unambiguousExpression
  ;
bitwiseExpression
  : opds+=unambiguousExpression (opr=AND opds+=unambiguousExpression)+ # BitwiseNaryExpression
  | opds+=unambiguousExpression (opr=IOR opds+=unambiguousExpression)+ # BitwiseNaryExpression
  | opds+=unambiguousExpression (opr=XOR opds+=unambiguousExpression)+ # BitwiseNaryExpression
  | lhs=unambiguousExpression opr=(SHL | SHR) rhs=unambiguousExpression # BitwiseBinaryExpression
  ;
compareExpression
  : opds+=compareOperand (oprs+=(EQL | NEQ | LTN | GTN | LEQ | GEQ) opds+=compareOperand)+ # CompareChainExpression
  | lhs=compareOperand opr=TWC rhs=compareOperand # CompareBinaryExpression
  ;
compareOperand
  : bitwiseExpression
  | algebraExpression
  | unambiguousExpression
  ;
logicalExpression
  : opds+=logicalOperand (opr=LCJ opds+=logicalOperand)+
  | opds+=logicalOperand (opr=LDJ opds+=logicalOperand)+
  ;
logicalOperand
  : compareExpression
  | bitwiseExpression
  | algebraExpression
  | unambiguousExpression
  ;
// recordExpression
//   : LPAREN (
//     names+=LowerId COLON exprs+=expression (  COMMA
//     names+=LowerId COLON exprs+=expression )* COMMA?)?
//     RPAREN
//   ;
// tupleExpression
//   : LPAREN (
//     exprs+=expression (  COMMA
//     exprs+=expression )* COMMA?)?
//     RPAREN
//   ;
// arrayExpression
//   : LBRACK (
//     exprs+=expression (  COMMA
//     exprs+=expression )* COMMA?)?
//     RBRACK
//   ;

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
// TODO: StringLit, CharLit

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
    (muts+=MUT | {$muts.add(null);}) names+=LowerId COLON types+=type (  COMMA
    (muts+=MUT | {$muts.add(null);}) names+=LowerId COLON types+=type )* COMMA?)?
    RPAREN
  ;
tupleType
  : LPAREN (
    (muts+=MUT | {$muts.add(null);}) COLON types+=type (  COMMA
    (muts+=MUT | {$muts.add(null);}) COLON types+=type )* COMMA?)?
    RPAREN
  ;
arrayType
  : LBRACK
    mut=MUT? elem=type
    (SEMIC length=IntLit)?
    RBRACK
  ;
namedType
  : (UpperId DOUBC)* UpperId
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Function Call
////////////////////////////////////////////////////////////////////////////////////////////////////

functionCall
  : variable
    LPAREN (
    names+=LowerId COLON exprs+=expression (  COMMA
    names+=LowerId COLON exprs+=expression )* COMMA?)?
    RPAREN
  | variable
    LPAREN (
    exprs+=expression (  COMMA
    exprs+=expression )* COMMA?)?
    RPAREN
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Variable
////////////////////////////////////////////////////////////////////////////////////////////////////

variable
  : (UpperId DOUBC)* (LowerId|UpperId) (POINT (LowerId|UpperId) | LBRACK expression RBRACK)*
  ;
