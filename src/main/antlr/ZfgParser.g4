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

module
  : body=scope? EOF
  ;

scope
  : statement ((SEMIC | {EOL()}) statement)* SEMIC?
  ;

statement
  : modifier=(LET | MUT | PUB) name=LowerId type=anyType SETA rhs=definition   # DefinitionStatement
  | child=assignment                                                           # AssignmentStatement
  | child=functionCall                                                         # FunctionCallStatement
  ;

definition
  : LBRACE body=scope? RBRACE                                                  # ScopedDefinition
  | body=expression                                                            # InlineDefinition
  ;

assignment
  : lhs=path
    op=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA)
    rhs=expression
  ;

 // TODO: named parameters
functionCall
  : path LPAREN (arguments=expression (COMMA arguments=expression)* COMMA?)? RPAREN
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Expressions
////////////////////////////////////////////////////////////////////////////////////////////////////

expression
  : expr=unambigExpression                                                     # UnambigExpr
  | expr=algebraExpression                                                     # AlgebraExpr
  | expr=bitwiseExpression                                                     # BitwiseExpr
  | expr=compareExpression                                                     # CompareExpr
  | expr=logicalExpression                                                     # LogicalExpr
  ;

unambigExpression
  : lhs=path op=(INC | DEC)                                                    # PostfixExpr
  | op=(INC | DEC) rhs=path                                                    # PrefixExpr
  | child=functionCall                                                         # FunctionCallExpr
  | child=path                                                                 # VariablePathExpr
  | child=literal                                                              # LiteralExpr
  | op=(ADD | SUB | NOT | LNT) rhs=unambigExpression                           # PrefixExpr
  | LPAREN expr=expression RPAREN                                              # PrecedenceExpr
  | LPAREN expr=assignment RPAREN                                              # AssignmentExpr
  ;

algebraExpression
  : lhs=algebraExpression op=(MUL | DIV | REM | MOD) rhs=algebraExpression     # AlgebraInfixExprAA
  | lhs=algebraExpression op=(MUL | DIV | REM | MOD) rhs=unambigExpression     # AlgebraInfixExprAU
  | lhs=unambigExpression op=(MUL | DIV | REM | MOD) rhs=algebraExpression     # AlgebraInfixExprUA
  | lhs=unambigExpression op=(MUL | DIV | REM | MOD) rhs=unambigExpression     # AlgebraInfixExprUU
  | lhs=algebraExpression op=(ADD | SUB) rhs=algebraExpression                 # AlgebraInfixExprAA
  | lhs=algebraExpression op=(ADD | SUB) rhs=unambigExpression                 # AlgebraInfixExprAU
  | lhs=unambigExpression op=(ADD | SUB) rhs=algebraExpression                 # AlgebraInfixExprUA
  | lhs=unambigExpression op=(ADD | SUB) rhs=unambigExpression                 # AlgebraInfixExprUU
  ;

bitwiseExpression
  : unambigExpression (op=AND unambigExpression)+                              # BitwiseChianExpr
  | unambigExpression (op=IOR unambigExpression)+                              # BitwiseChianExpr
  | unambigExpression (op=XOR unambigExpression)+                              # BitwiseChianExpr
  | lhs=unambigExpression op=(SHL | SHR) rhs=unambigExpression                 # BitwiseInfixExpr
  ;

compareExpression
  : compareOperand (op=(EQL | NEQ | LTN | GTN | LEQ | GEQ) compareOperand)+    # CompareChianExpr
  | lhs=compareOperand op=TWC rhs=compareOperand                               # CompareInfixExpr
  ;
compareOperand
  : expr=bitwiseExpression                                                     # BitwiseCompareOpd
  | expr=algebraExpression                                                     # AlgebraCompareOpd
  | expr=unambigExpression                                                     # UnambigCompareOpd
  ;

logicalExpression
  : logicalOperand (op=LCJ logicalOperand)+                                    # LogicalChianExpr
  | logicalOperand (op=LDJ logicalOperand)+                                    # LogicalChianExpr
  ;
logicalOperand
  : expr=compareExpression                                                     # CompareLogicalOpd
  | expr=bitwiseExpression                                                     # BitwiseLogicalOpd
  | expr=algebraExpression                                                     # AlgebraLogicalOpd
  | expr=unambigExpression                                                     # UnambigLogicalOpd
  ;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Literals
////////////////////////////////////////////////////////////////////////////////////////////////////

// TODO
// - arrayLiteral
// - tupleLiteral
// - recordLiteral
// - functionLiteral ?
// - characterLiteral ?
// - stringLiteral ?
literal
  : numericLiteral                                                             # NumericLit
  ;

numericLiteral
  : token=(BitLit | IntLit | FltLit)
  ;


////////////////////////////////////////////////////////////////////////////////////////////////////
// Types
////////////////////////////////////////////////////////////////////////////////////////////////////

// TODO: named type
anyType
  : type=functionType                                                          # FunType
  | type=recordType                                                            # RecType
  | type=tupleType                                                             # TupType
  | type=arrayType                                                             # ArrType
  | type=primitiveType                                                         # PriType
  ;

functionType:
  LPAREN (
  parameterModifiers=(LET | MUT) parameterNames=LowerId parameterTypes=anyType  (COMMA
  parameterModifiers=(LET | MUT) parameterNames=LowerId parameterTypes=anyType)* COMMA?)?
  RPAREN
  returnType=anyType;

recordType:
  LPAREN (
  fieldModifiers=(LET | MUT) fieldNames=LowerId fieldTypes=anyType  (COMMA
  fieldModifiers=(LET | MUT) fieldNames=LowerId fieldTypes=anyType)* COMMA?)?
  RPAREN;

tupleType:
  LPAREN (
  fieldModifiers=(LET | MUT) fieldTypes=anyType  (COMMA
  fieldModifiers=(LET | MUT) fieldTypes=anyType)* COMMA?)?
  RPAREN;

arrayType:
  LBRACK
  elementsModifier=(LET | MUT) elementsType=anyType
  (SEMIC elementCount=IntLit)?
  RBRACK;

primitiveType:
  token=(BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64);

inferredType:
  token=(FUN | VAR);

////////////////////////////////////////////////////////////////////////////////////////////////////
// Paths
////////////////////////////////////////////////////////////////////////////////////////////////////

path
  : (modulePath=UpperId DOUBC)* root=LowerId (fieldPath=accessor)*
  ;

accessor
  : POINT name=LowerId                                                         # NamedAccessor
  | POINT index=IntLit                                                         # IndexAccessor
  | LBRACK index=expression RBRACK                                             # ArrayAccessor
  ;



// // TODO: named types
// // TODO: type inference, e.g. "var", "fun", "type", etc.
// // TODO: what about elided types, e.g. "_"? or "let x = 42"?
// // TODO: decide if record types can have private fields. maybe only if its assigned to a named type?
// // TODO: algebric types: enum, union, type && type, type || type, etc...
// type
//   : primitiveType=(BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64) # PrimitiveType
//   | LBRACK mod=(LET | MUT)? elementType=type (SEMIC length=IntLit)? RBRACK          # ArrayType
//   | recordType_=recordType                                                          # RecordType_
//   | paramsType=recordType returnType=type                                           # FunctionType
//   ;
// recordType
//   : LPAREN (recordField (COMMA recordField)* COMMA?)? RPAREN
//   ;
// recordField
//   : mod=(LET | MUT)? id=LowerId fieldType=type
//   ;
// // recordInstance
// //   : LPAREN (         xpression (COMMA         expression)* COMMA?)? RPAREN
// //   | LPAREN (LowerId expression (COMMA LowerId expression)* COMMA?)? RPAREN
// //   ;

// // TODO: should we separate out different types of decl statements?
// //       mod=(LET | MUT | PUB | USE) id=LowerId symbolType=funType SETA expr=expression # BlockFunctionDecl
// //       mod=(LET | MUT | PUB | USE) id=LowerId symbolType=funType SETA expr=expression # InlineFunctionDecl
// //       mod=(LET | MUT | PUB | USE) id=LowerId symbolType=datType SETA expr=expression # InlineFunctionDecl
// //       mod=(LET | MUT | PUB | USE) id=UperId  symbolType=typType SETA expr=expression # InlineTypeDecl
// // TODO: for loop init header should allow multiple comma delimited seclaration or expressions
// // TODO: for loop incr header should allow multiple comma delimited expressions
// // TODO: for-each loops? probably requires some standard iterator interface
// // TODO: labeled for loops, with labeled 'break' and 'continue'
// // TODO: labeled blocks? do we allow breaking a block?
// // TODO: labeled functions and lableled returns? how would that work with lambdas?
// // TODO: destructuring assignment statements
// // TODO: match statments
// // TODO: defer statments
// // TODO: nit: do we really need separate 'loop', 'while', and 'for' keywords?
// statement
//   : mod=(LET | MUT | PUB | USE) id=LowerId symbolType=type SETA (expr=expression | blk=block) # DeclarationStmt
//   | assign=assignment                                                           # AssignmentStmt
//   | funCall=functionCall                                                        # FunctionCallStmt
//   | RETURN expr=expression?                                                     # FunctionReturnStmt
//   | IF expression block (ELSE IF expression block)* (ELSE block)?               # IfElseStmt
//   | LOOP                                                            blk=block   # LoopStmt
//   | WHILE                     cond=expression                       blk=block   # LoopWhileStmt
//   | FOR init=expression SEMIC cond=expression SEMIC incr=expression blk=block   # LoopForStmt
//   | BREAK                                                                       # LoopBreakStmt
//   | CONTINUE                                                                    # LoopContinueStmt
//   ;
// block
//   : LBRACE (statement (SEMIC | {EOL()}?))* RBRACE
//   ;

// // TODO: array instantiation
// // TODO: record instantiation
// expression
//   : funCall=functionCall                                     # FunctionCallExpr
//   | id=identifier                                            # VariableExpr
//   | lit=(BitLit | IntLit | FltLit)                           # LiteralExpr
//   | LPAREN expr=expression RPAREN                            # GroupedExpr
//   | lhs=expression op=(INC | DEC)                            # PostfixOpExpr
//   | op=(INC | DEC | ADD | SUB | NOT) rhs=expression          # PrefixOpExpr
//   | lhs=expression op=(MUL | DIV | REM | MOD) rhs=expression # InfixOpExpr
//   | lhs=expression op=(ADD | SUB) rhs=expression             # InfixOpExpr
//   | lhs=expression op=(SHL | SHR) rhs=expression             # InfixOpExpr
//   | lhs=expression op=AND rhs=expression                     # InfixOpExpr
//   | lhs=expression op=XOR rhs=expression                     # InfixOpExpr
//   | lhs=expression op=IOR rhs=expression                     # InfixOpExpr
//   | lhs=expression op=CMP rhs=expression                     # InfixOpExpr
//   | lhs=expression op=(LTN | GTN | LEQ | GEQ) rhs=expression # InfixOpExpr
//   | lhs=expression op=(EQL | NEQ)  rhs=expression            # InfixOpExpr
//   | assign=assignment                                        # AssignmentExpr
//   | lhs=expression op=(LCJ | LDJ) rhs=expression             # InfixOpExpr
//   ;

// assignment
//   : lhs=identifier op=(SETA | ADDA | SUBA | MULA | DIVA | REMA | MODA | ANDA | IORA | XORA | SHLA | SHRA) rhs=expression
//   ;

// functionCall
//   : identifier LPAREN (expression (COMMA expression)* COMMA?)? RPAREN
//   ;

// identifier
//   : part=(UpperId | LowerId) ('.' part=(UpperId | LowerId))*
//   ;
