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

expression returns [zfg.Ast.Expr parsed]
  : unambiguousExpression
  | algebraExpression
  | bitwiseExpression
  | compareExpression
  | logicalExpression
  ;
unambiguousExpression returns [zfg.Ast.Expr parsed]
  : asn=postfixAssignment                                  # PostfixAssignmentExpression
  | asn=prefixAssignment                                   # PrefixAssignmentExpression
  | fun=functionCall                                       # FunctionCallExpression
  | var=variable                                           # VariableExpression
  | lit=literal                                            # LiteralExpression
  | opr=(ADD | SUB | NOT | LNT) rhs=unambiguousExpression  # UnaryExpression
  | LPAREN asn=bivariateAssignment RPAREN                  # BivariateAssignmentExpression
  | LPAREN exp=expression RPAREN                           # ParentheticalExpression
  ;
algebraExpression returns [zfg.Ast.Expr parsed]
  : opa=algebraExpression     opr=(MUL | DIV | REM | MOD) rhs=algebraOperand
  | opu=unambiguousExpression opr=(MUL | DIV | REM | MOD) rhs=algebraOperand
  | opa=algebraExpression     opr=(ADD | SUB) rhs=algebraOperand
  | opu=unambiguousExpression opr=(ADD | SUB) rhs=algebraOperand
  ;
algebraOperand returns [zfg.Ast.Expr parsed]
  : algebraExpression
  | unambiguousExpression
  ;
bitwiseExpression returns [zfg.Ast.Expr parsed]
  : opds+=unambiguousExpression (opr=AND opds+=unambiguousExpression)+  # BitwiseChainExpression
  | opds+=unambiguousExpression (opr=IOR opds+=unambiguousExpression)+  # BitwiseChainExpression
  | opds+=unambiguousExpression (opr=XOR opds+=unambiguousExpression)+  # BitwiseChainExpression
  | lhs=unambiguousExpression opr=(SHL | SHR) rhs=unambiguousExpression # BitwiseShiftExpression
  ;
compareExpression returns [zfg.Ast.Expr parsed]
  : opds+=compareOperand (opr=(EQL | NEQ | LTN | GTN | LEQ | GEQ) opds+=compareOperand)+ # CompareChainExpression
  | lhs=compareOperand opr=TWC rhs=compareOperand                                        # CompareThreeWayExpression
  ;
compareOperand returns [zfg.Ast.Expr parsed]
  : bitwiseExpression
  | algebraExpression
  | unambiguousExpression
  ;
logicalExpression returns [zfg.Ast.Expr parsed]
  : opds+=logicalOperand (opr=LCJ opds+=logicalOperand)+
  | opds+=logicalOperand (opr=LDJ opds+=logicalOperand)+
  ;
logicalOperand returns [zfg.Ast.Expr parsed]
  : compareExpression
  | bitwiseExpression
  | algebraExpression
  | unambiguousExpression
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

literal returns [zfg.Inst parsed]
  : numericLiteral
  | recordLiteral
  | tupleLiteral
  | arrayLiteral
  ;
numericLiteral returns [zfg.Inst parsed]
  : token=(BitLit | IntLit | FltLit)
  ;
recordLiteral returns [zfg.Inst parsed]
  : LPAREN (
    names+=LowerId COLON exprs+=expression (  COMMA
    names+=LowerId COLON exprs+=expression )* COMMA?)?
    RPAREN
  ;
tupleLiteral returns [zfg.Inst parsed]
  : LPAREN (
    exprs+=expression (  COMMA
    exprs+=expression )* COMMA?)?
    RPAREN
  ;
arrayLiteral returns [zfg.Inst parsed]
  : LBRACK (
    exprs+=expression (  COMMA
    exprs+=expression )* COMMA?)?
    RBRACK
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Types
////////////////////////////////////////////////////////////////////////////////////////////////////

type returns [zfg.Type parsed]
  : primitiveType
  | functionType
  | recordType
  | tupleType
  | arrayType
  | namedType
  ;
primitiveType returns [zfg.Type parsed]
  : token=(BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64)
  ;
functionType returns [zfg.Type parsed]
  : paramsType=recordType COLON returnType=type
  ;
recordType returns [zfg.Type parsed] locals [int length = 0]
  : LPAREN (
    (muts+=MUT | {$muts.add(null);}) names+=LowerId COLON types+=type {$length += 1} (  COMMA
    (muts+=MUT | {$muts.add(null);}) names+=LowerId COLON types+=type {$length += 1} )* COMMA?)?
    RPAREN
  ;
tupleType returns [zfg.Type parsed] locals [int length = 0]
  : LPAREN (
    (muts+=MUT | {$muts.add(null);}) COLON types+=type {$length += 1} (  COMMA
    (muts+=MUT | {$muts.add(null);}) COLON types+=type {$length += 1} )* COMMA?)?
    RPAREN
  ;
arrayType returns [zfg.Type parsed]
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
  : variable (recordLiteral | tupleLiteral)
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Variable
////////////////////////////////////////////////////////////////////////////////////////////////////

variable
  : (UpperId DOUBC)* (LowerId|UpperId) (POINT (LowerId|UpperId) | LBRACK expression RBRACK)*
  ;
