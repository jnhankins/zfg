lexer grammar ZfgLexer;
channels { WHITESPACE_CHANNEL, COMMENTS_CHANNEL }

/*** Keywords *************************************************************************************/
// Constant variable declaration, i.e. cannot be reassigned without redeclaration, e.g. 'let x = 42'
LET: 'let' ;
// Mutable variable declaration, i.e. can be reassigned without redeclaration, e.g. 'mut x = 42'
MUT: 'mut' ;
// Function declaration, e.g. 'fun add(x: i32, y: i32): i32'
FUN: 'fun' ;
// Function return value, e.g. 'ret 2 * x'
RET: 'ret' ;

/*** Primitive Types ******************************************************************************/
VAR: 'var' ; // Inferred type, aka 'auto'
BIT: 'bit' ; // Single bit, aka 'boolean'
I08: 'i08' ; // 8-bit signed integer, aka 'byte'
I16: 'i16' ; // 16-bit signed integer, aka 'short'
I32: 'i32' ; // 32-bit signed integer, aka 'int'
I64: 'i64' ; // 64-bit signed integer, aka 'long'
F32: 'f32' ; // 32-bit floating point number, aka 'float'
F64: 'f64' ; // 64-bit floating point number, aka 'double'

/*** Separators ***********************************************************************************/
COMMA: ',' ; // Argument separator, array element separator, object field separator
POINT: '.' ; // Path separator (e.g. 'std.io.println')
COLON: ':' ; // Variable id-type separator, field id-type separator, literal id-value separator
SEMIC: ';' ; // Statement separator, type member separator

/*** Groupors *************************************************************************************/
LPAREN: '(' ; // Expression grouping, function parameters
RPAREN: ')' ;
LBRACE: '{' ; // Scoped block, function body, type body
RBRACE: '}' ;
LBRACK: '[' ; // Array literal, array access
RBRACK: ']' ;

/*** Arithmetic Operators *************************************************************************/
INC: '++' ; // Increment prefix or postfix operator, e.g. '++x' or 'x++'
DEC: '--' ; // Decrement prefix or postfix operator, e.g. '--x' or 'x--'
ADD: '+'  ; // Identity prefix or addition infix operator, e.g. '+x' or 'x + y'
SUB: '-'  ; // Negation prefix or subtraction infix operator, e.g. '-x' or 'x - y'
MUL: '*'  ; // Multiplication infix operator, e.g. 'x * y'
DIV: '/'  ; // Division infix operator, e.g. 'x / y'
REM: '%'  ; // Remainder infix operator, e.g. 'x % y'
MOD: '%%' ; // Modulo infix operator, e.g. 'x %% y'
NOT: '!'  ; // Bitwise NOT prefix operator, e.g. '!x'
AND: '&'  ; // Bitwise AND infix operator, e.g. 'x & y'
IOR: '|'  ; // Bitwise OR infix operator, e.g. 'x | y'
XOR: '^'  ; // Bitwise XOR infix operator, e.g. 'x ^ y'
SHL: '<<' ; // Shift left infix operator, e.g. 'x << y'
SHR: '>>' ; // Shift right infix operator, e.g. 'x >> y'

/*** Assignment Operators *************************************************************************/
SETA: '='   ; // Assignment infix operator, e.g. 'x = y'
ADDA: '+='  ; // Addition assignment infix operator, e.g. 'x += y'
SUBA: '-='  ; // Subtraction assignment infix operator, e.g. 'x -= y'
MULA: '*='  ; // Multiplication assignment infix operator, e.g. 'x *= y'
DIVA: '/='  ; // Division assignment infix operator, e.g. 'x /= y'
REMA: '%='  ; // Remainder assignment infix operator, e.g. 'x %= y'
MODA: '%%=' ; // Modulo assignment infix operator, e.g. 'x %%= y'
ANDA: '&='  ; // Bitwise AND assignment infix operator, e.g. 'x &= y'
IORA: '|='  ; // Bitwise OR assignment infix operator, e.g. 'x |= y'
XORA: '^='  ; // Bitwise XOR assignment infix operator, e.g. 'x ^= y'
SHLA: '<<=' ; // Shift left assignment infix operator, e.g. 'x <<= y'
SHRA: '>>=' ; // Shift right assignment infix operator, e.g. 'x >>= y'

/*** Relational Operators *************************************************************************/
LT:  '<'   ; // Less than infix operator, e.g. 'x < y'
GT:  '>'   ; // Greater than infix operator, e.g. 'x > y'
LE:  '<='  ; // Less than or equal to infix operator, e.g. 'x <= y'
GE:  '>='  ; // Greater than or equal to infix operator, e.g. 'x >= y'
EQ:  '=='  ; // Equal to infix operator, e.g. 'x == y'
NE:  '!='  ; // Not equal to infix operator, e.g. 'x != y'
EQR: '===' ; // Equal references infix operator, e.g. 'x <?> y'
NER: '!==' ; // Not equal references infix operator, e.g. 'x <!> y'
CMP: '<=>' ; // Compare infix operator, e.g. 'x <=> y'

/*** Literals *****************************************************************************/
// Boolean literal, e.g. 'true' or 'false'
BitLit: 'true' | 'false';
// Integer literal, e.g. '1234' or '0xFF'
IntLit: (NatDigits | BinIntLit | OctIntLit | DecIntLit | HexIntLit) (DigitSep? (BIT | I08 | I16 | I32 | I64))?;
// Floating point literal, e.g. '123.456' or '0xAF.23p-10'
FltLit: (DecFltLit | HexFltLit) (DigitSep? (F32 | F64))?;

/*** Identifiers **********************************************************************************/
// Upper camel case identifier, i.e. a class or module name, e.g. 'MyClass'
UpperId: [A-Z] [a-zA-Z0-9]*;
// Lower snake case identifier, i.e. a field, variable, or function name, e.g. 'my_var'
LowerId: [a-z_] [a-z0-9_]*;

/*** Whitespace and Comments **********************************************************************/
// Whitespace, i.e. spaces, tabs, and form feeds
Ws: [ \t]+ -> channel(WHITESPACE_CHANNEL);
// Newline, i.e. LF or CRLF
Nl: ([\r]? [\n])+ -> channel(WHITESPACE_CHANNEL);
// Line comment, i.e. '//' followed by any characters until the end of the line
Lc: '//' ~[\r\n]* -> channel(COMMENTS_CHANNEL);

/*** Numeric Literal Fragments ********************************************************************/
// Binary digit, i.e. 0 or 1
fragment BinDigit: [0-1];
// Octal digit, i.e. 1 through 7
fragment OctDigit: [0-7];
// Decimal digit, i.e. 0 through 9
fragment DecDigit: [0-9];
// Hexadecimal digit, i.e. 0 through 9 or A through F
fragment HexDigit: [0-9A-F];
// Digits separator, e.g. 1_000_000
fragment DigitSep: '_';
// Binary digit seuqence, e.g. 0011_1100
fragment BinDigits: BinDigit (DigitSep? BinDigit)*;
// Octal digit sequence, e.g. 0077_7700
fragment OctDigits: OctDigit (DigitSep? OctDigit)*;
// Decimal digit sequence, e.g. 0099_9900
fragment DecDigits: DecDigit (DigitSep? DecDigit)*;
// Hexadecimal digit sequence, e.g. 00FF_FF00
fragment HexDigits: HexDigit (DigitSep? HexDigit)*;
// Natural number digit sequence, i.e. no leading zeros, e.g. 1_234
fragment NatDigits: '0' | [1-9] (DigitSep? DecDigit)*;
// Decimal floating point exponent, e.g. e-10
fragment DecFltExp: 'e' [+-]? NatDigits;
// Hexadecimal floating point exponent, e.g. p-10
fragment HexFltExp: 'p' [+-]? NatDigits;
// Binary integer literal, e.g. 0b1010
fragment BinIntLit: '0b' BinDigits;
// Octal integer literal, e.g. 0o1234
fragment OctIntLit: '0o' OctDigits;
// Decimal integer literal, e.g. 1234  0d001234
fragment DecIntLit: '0d' DecDigits;
// Hexadecimal integer literal, e.g. 0xAF12
fragment HexIntLit: '0x' HexDigits;
// Decimal floating point literal, e.g. 123.456 or 1.234e-10
fragment DecFltLit:       NatDigits ('.' DecDigits? DecFltExp? | DecFltExp) | '.' DecDigits DecFltExp?;
// Hexadecimal floating point literal, e.g. 0xAF.23p-10, primarily for compacting float literals
fragment HexFltLit: '0x' (HexDigits ('.' HexDigits? HexFltExp? | HexFltExp) | '.' HexDigits HexFltExp?);
