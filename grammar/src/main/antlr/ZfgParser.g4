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

module
  : body=statementBlock? EOF
  ;

statementBlock
  : statements=statement ((SEMIC | {EOL()}) statements=statement)* (SEMIC | {EOL()})?
  ;

statement
  : modifier=(LET | MUT | PUB) name=LowerId type=functionType EQL body=functionBody # FunctionDeclaration
  | modifier=(LET | MUT | PUB) name=LowerId type=anyType EQL body=expression        # VariableDeclaration
  | child=assignment                                                                # AssignmentStatement
  | child=functionCall                                                              # FunctionCallStatement
  ;

functionBody
  : LBRACE body=statementBlock? RBRACE # FunctionBlock
  | body=expression                    # FunctionExpression
  ;

assignment
  : lhs=path
    op=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA)
    rhs=expression
  ;

functionCall
  : path LPAREN (arguments=expression (COMMA arguments=expression)* COMMA?)? RPAREN
  ; // TODO: named parameters

////////////////////////////////////////////////////////////////////////////////////////////////////
// Expressions
////////////////////////////////////////////////////////////////////////////////////////////////////

expression
  : unambigExpression
  | algebraExpression
  | bitwiseExpression
  | compareExpression
  | logicalExpression
  ;

unambigExpression
  : lhs=path op=(INC | DEC)                                                      # SuffixExpr
  | op=(INC | DEC) rhs=path                                                      # PrefixExpr
  | child=functionCall                                                           # FunctionCallExpr
  | child=path                                                                   # VariablePathExpr
  | literal=(BitLit | IntLit | FltLit)                                           # LiteralValueExpr
  | op=(ADD | SUB | NOT | LNT) rhs=unambigExpression                             # PrefixExpr
  | LPAREN expr=assignment RPAREN                                                # AssignmentExpr
  | LPAREN expr=expression RPAREN                                                # PrecedenceExpr
  ;

algebraExpression
  : lhs=algebraOperand op=(MUL | DIV | REM | MOD) rhs=algebraOperand             # AlgebraInfixExpr
  | lhs=algebraOperand op=(ADD | SUB) rhs=algebraOperand                         # AlgebraInfixExpr
  ;
algebraOperand
  : expr=algebraExpression
  | expr=unambigExpression
  ;

bitwiseExpression
  : opd=bitwiseOperand (op=AND opd=bitwiseOperand)+                              # BitwiseChianExpr
  | opd=bitwiseOperand (op=IOR opd=bitwiseOperand)+                              # BitwiseChianExpr
  | opd=bitwiseOperand (op=XOR opd=bitwiseOperand)+                              # BitwiseChianExpr
  | lhs=bitwiseOperand op=(SHL | SHR | CMP) rhs=bitwiseOperand                   # BitwiseInfixExpr
  ;
bitwiseOperand
  : expr=unambigExpression
  ;

compareExpression
  : lhs=compareOperand op=(EQL | NEQ | LTN | GTN | LEQ | GEQ) rhs=compareOperand
  ;
compareOperand
  : expr=bitwiseExpression
  | expr=algebraExpression
  | expr=unambigExpression
  ;

logicalExpression
  : opd=logicalOperand (op=LCJ opd=logicalOperand)+
  | opd=logicalOperand (op=LDJ opd=logicalOperand)+
  ;
logicalOperand
  : expr=compareExpression
  | expr=bitwiseExpression
  | expr=algebraExpression
  | expr=unambigExpression
  ;


////////////////////////////////////////////////////////////////////////////////////////////////////
// Types
////////////////////////////////////////////////////////////////////////////////////////////////////

// TODO: type path
anyType
  : primitiveType
  | arrayType
  | tupleType
  | recordType
  | functionType
  ;

primitiveType:
  BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64;

arrayType:
  LBRACK
  elementsModifier=(LET | MUT) elementsType=anyType
  (SEMIC staticLength=IntLit)?
  RBRACK;

tupleType:
  LPAREN (
  fieldModifiers=(LET | MUT) fieldTypes=anyType (COMMA
  fieldModifiers=(LET | MUT) fieldTypes=anyType)* COMMA?)?
  RPAREN;

recordType:
  LPAREN (
  fieldModifiers=(LET | MUT) fieldNames=LowerId fieldTypes=anyType (COMMA
  fieldModifiers=(LET | MUT) fieldNames=LowerId fieldTypes=anyType)* COMMA?)?
  RPAREN;

functionType:
  LPAREN (
  parameterModifiers=(LET | MUT) parameterNames=LowerId parameterTypes=anyType (COMMA
  parameterModifiers=(LET | MUT) parameterNames=LowerId parameterTypes=anyType)* COMMA?)?
  RPAREN
  returnType=anyType;


////////////////////////////////////////////////////////////////////////////////////////////////////
// Paths
////////////////////////////////////////////////////////////////////////////////////////////////////

path
  : (modulePath=UpperId DOUBC)* root=LowerId (fieldPath=accessor)*
  ;

accessor
  : POINT name=LowerId             # NamedAccessor
  | POINT index=IntLit             # IndexAccessor
  | LBRACK index=expression RBRACK # ArrayAccessor
  ;
