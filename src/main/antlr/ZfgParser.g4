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
  : (statement (SEMIC | {EOL()}?))*
  ;

// TODO: named types
// TODO: type inference, e.g. "var", "fun", "type", etc.
// TODO: what about elided types, e.g. "_"? or "let x = 42"?
// TODO: decide if record types can have private fields. maybe only if its assigned to a named type?
// TODO: algebric types: enum, union, type && type, type || type, etc...
type
  : primitiveType=(BIT | U08 | U16 | U32 | U64 | I08 | I16 | I32 | I64 | F32 | F64) # PrimitiveType
  | LBRACK mod=(LET | MUT)? elementType=type (SEMIC length=IntLit)? RBRACK          # ArrayType
  | recordType_=recordType                                                          # RecordType_
  | paramsType=recordType returnType=type                                           # FunctionType
  ;
recordType
  : LPAREN (recordField (COMMA recordField)* COMMA?)? RPAREN
  ;
recordField
  : mod=(LET | MUT)? id=LowerId fieldType=type
  ;
// recordInstance
//   : LPAREN (         xpression (COMMA         expression)* COMMA?)? RPAREN
//   | LPAREN (LowerId expression (COMMA LowerId expression)* COMMA?)? RPAREN
//   ;

// TODO: should we separate out different types of decl statements?
//       mod=(LET | MUT | PUB | USE) id=LowerId symbolType=functionType SETA expr=expression # BlockFunctionDecl
//       mod=(LET | MUT | PUB | USE) id=LowerId symbolType=functionType SETA expr=expression # InlineFunctionDecl
//       mod=(LET | MUT | PUB | USE) id=LowerId symbolType=dataType SETA expr=expression # InlineFunctionDecl
//       mod=(LET | MUT | PUB | USE) id=UperId  symbolType=typeType SETA expr=expression # InlineTypeDecl
// TODO: for loop init header should allow multiple comma delimited seclaration or expressions
// TODO: for loop incr header should allow multiple comma delimited expressions
// TODO: for-each loops? probably requires some standard iterator interface
// TODO: labeled for loops, with labeled 'break' and 'continue'
// TODO: labeled blocks? do we allow breaking a block?
// TODO: labeled functions and lableled returns? how would that work with lambdas?
// TODO: destructuring assignment statements
// TODO: match statments
// TODO: defer statments
// TODO: nit: do we really need separate 'loop', 'while', and 'for' keywords?
statement
  : mod=(LET | MUT | PUB | USE) id=LowerId symbolType=type SETA (expr=expression | blk=block) # DeclarationStmt
  | assign=assignment                                                           # AssignmentStmt
  | funCall=functionCall                                                        # FunctionCallStmt
  | RETURN expr=expression?                                                     # FunctionReturnStmt
  | IF expression block (ELSE IF expression block)* (ELSE block)?               # IfElseStmt
  | LOOP                                                            blk=block   # LoopStmt
  | WHILE                     cond=expression                       blk=block   # LoopWhileStmt
  | FOR init=expression SEMIC cond=expression SEMIC incr=expression blk=block   # LoopForStmt
  | BREAK                                                                       # LoopBreakStmt
  | CONTINUE                                                                    # LoopContinueStmt
  ;
block
  : LBRACE (statement (SEMIC | {EOL()}?))* RBRACE
  ;

// TODO: array instantiation
// TODO: record instantiation
expression
  : funCall=functionCall                                     # FunctionCallExpr
  | id=identifier                                            # VariableExpr
  | lit=(BitLit | IntLit | FltLit)                           # LiteralExpr
  | LPAREN inner=expression RPAREN                           # GroupedExpr
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
  | assign=assignment                                        # AssignmentExpr
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
