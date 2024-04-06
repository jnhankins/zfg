parser grammar ZfgParser;
options { tokenVocab = ZfgLexer; }

start: (statement SEMIC?)* EOF;

statement
  : variable_declaration_statement
  | assign_expression
  | print_statement
  ;

// TODO: Remove print_statement
print_statement
  : PRINT LPAREN LowerSnakeCaseName RPAREN
  ;

variable_declaration_statement
  : (LET | MUT) LowerSnakeCaseName (COLON variable_type)? ASSIGN expression
  ;

variable_type
  : VAR | I08 | I16 | I32 | I64 | F32 | F64
  ;

expression
  // path
  : path
  // literal
  | literal
  // parenthetical circumfix
  | LPAREN expression RPAREN
  // unary postfix
  | expression (INCR | DECR)
  // unary prefix
  | (INCR | DECR | ADD | SUB | EQZ | NEQZ) expression
  // multaplicative infix
  | expression (MUL | POW | DIV | MOD | PMOD) expression
  // additive infix
  | expression (ADD | SUB) expression
  // shift infix
  | expression (SHL | SHR | SHRA ) expression
  // bitwise AND infix
  | expression BITAND expression
  // bitwise XOR infix
  | expression BITXOR expression
  // bitwise OR infix
  | expression BITOR expression
  // relational infix
  | expression (LT | GT | LE | GE) expression
  // equality infix
  | expression (EQ | NE | EQR | NER) expression
  // assignment
  | assign_expression
  ;

assign_expression
  : path
    ( ASSIGN
    | ADD_ASSIGN | SUB_ASSIGN | MUL_ASSIGN | POW_ASSIGN | DIV_ASSIGN | MOD_ASSIGN | PMOD_ASSIGN
    | SHL_ASSIGN | SHR_ASSIGN | SHRA_ASSIGN
    | BITAND_ASSIGN | BITXOR_ASSIGN | BITOR_ASSIGN)
    expression
  ;

path
  : identifier (DOT identifier)*
  ;

identifier
  : LowerSnakeCaseName
  | UpperCamelCaseName
  ;

literal
  : TRUE
  | FALSE
  | BinIntegerLiteral
  | OctIntegerLiteral
  | DecIntegerLiteral
  | HexIntegerLiteral
  | DecFloatLiteral
  | HexFloatLiteral
  ;

