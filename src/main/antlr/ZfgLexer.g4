lexer grammar ZfgLexer;

channels { WHITESPACE_CHANNEL, COMMENTS_CHANNEL }

// TODO: remove PRINT
PRINT: 'print';

/*** Keywords *************************************************************************************/
// Constant variable declaration, i.e. cannot be reassigned without redeclaration, e.g. 'let x = 42'
LET: 'let';
// Mutable variable declaration, i.e. can be reassigned without redeclaration, e.g. 'mut x = 42'
MUT: 'mut';
// Function declaration, e.g. 'fun add(x: i32, y: i32): i32'
FUN: 'fun';

/*** Primitive Types ******************************************************************************/
VAR: 'var'; // Inferred type, aka 'auto'
BIT: 'bit'; // Single bit, aka 'boolean'
I08: 'i08'; // 8-bit signed integer, aka 'byte'
I16: 'i16'; // 16-bit signed integer, aka 'short'
I32: 'i32'; // 32-bit signed integer, aka 'int'
I64: 'i64'; // 64-bit signed integer, aka 'long'
F32: 'f32'; // 32-bit floating point number, aka 'float'
F64: 'f64'; // 64-bit floating point number, aka 'double'

/*** Logical Literals *****************************************************************************/
// Logical boolean false literal, e.g. '(1 == 2) == false'
FALSE: 'false';
// Logical boolean true literal, e.g. '(1 == 1) == true'
TRUE: 'true';

/*** Numeric Literals *****************************************************************************/
// Binary integer literal, e.g. 0b1010
BinIntegerLiteral: '0b' BinDigits;
// Octal integer literal, e.g. 0o1234
OctIntegerLiteral: '0o' OctDigits;
// Decimal integer literal, e.g. 1234  0d001234
DecIntegerLiteral: '0d' DecDigits | NatDigits;
// Hexadecimal integer literal, e.g. 0xAF12
HexIntegerLiteral: '0x' HexDigits;
// Decimal floating point literal, e.g. 123.456 or 1.234e-10
DecFloatLiteral:       NatDigits ('.' DecDigits? DecFltExp? | DecFltExp) | '.' DecDigits DecFltExp?;
// Hexadecimal floating point literal, e.g. 0xAF.23p-10, primarily for compacting float literals
HexFloatLiteral: '0x' (HexDigits ('.' HexDigits? HexFltExp? | HexFltExp) | '.' HexDigits HexFltExp?);

/*** Separators ***********************************************************************************/
COMMA  : ','; // Argument separator, array element separator, object field separator
DOT    : '.'; // Path separator (e.g. 'std.io.println')
COLON  : ':'; // Variable id-type separator, field id-type separator, literal id-value separator
SEMIC   : ';'; // Statement separator, type member separator
LPAREN : '('; // Expression grouping, function parameters
RPAREN : ')';
LBRACE : '{'; // Scoped block, function body, type body
RBRACE : '}';
LBRACK : '['; // Array literal, array access
RBRACK : ']';

/*** Arithmetic Operators *************************************************************************/
INCR   : '++' ; // Increment prefix or postfix operator, e.g. '++x' or 'x++'
DECR   : '--' ; // Decrement prefix or postfix operator, e.g. '--x' or 'x--'
BITINV : '~'  ; // Bitwise complement/inversion prefix operator, '~x'
EQZ    : '!'  ; // Logical NOT prefix operator, e.g. '!x', equivalent to 'x == false' or 'x == 0'
NEQZ   : '!!' ; // Logical NOT NOT prefix operator, e.g. '!!x', equivalent to 'x != false' or 'x != 0'
ADD    : '+'  ; // Identity prefix operator, e.g. '+x', or addition infix operator, e.g. 'x + y'
SUB    : '-'  ; // Negation prefix operator, e.g. '-x', or subtraction infix operator, e.g. 'x - y'
MUL    : '*'  ; // Multiplication infix operator, e.g. 'x * y'
POW    : '**' ; // Exponentiation infix operator, e.g. 'x ** y'
DIV    : '/'  ; // Division infix operator, e.g. 'x / y'
MOD    : '%'  ; // Logical modulus infix operator, e.g. 'x % y'
PMOD   : '%%' ; // Positive modulus infix operator, e.g. 'x +% y'
BITAND : '&'  ; // Bitwise AND infix operator, e.g. 'x & y'
BITXOR : '^'  ; // Bitwise XOR infix operator, e.g. 'x ^ y'
BITOR  : '|'  ; // Bitwise OR infix operator, e.g. 'x | y'
SHL    : '<<' ; // Shift left infix operator, e.g. 'x << y'
SHR    : '>>' ; // Shift right infix operator, e.g. 'x >> y'
SHRA   : '>>>'; // Shift right arithmetic infix operator, e.g. 'x >>> y'

/*** Assignment Operators *************************************************************************/
ASSIGN        : '='   ; // Assignment infix operator, e.g. 'x = y'
ADD_ASSIGN    : '+='  ; // Addition assignment infix operator, e.g. 'x += y'
SUB_ASSIGN    : '-='  ; // Subtraction assignment infix operator, e.g. 'x -= y'
MUL_ASSIGN    : '*='  ; // Multiplication assignment infix operator, e.g. 'x *= y'
POW_ASSIGN    : '**=' ; // Exponentiation assignment infix operator, e.g. 'x **= y'
DIV_ASSIGN    : '/='  ; // Division assignment infix operator, e.g. 'x /= y'
MOD_ASSIGN    : '%='  ; // Logical modulus assignment infix operator, e.g. 'x %= y'
PMOD_ASSIGN   : '%%=' ; // Positive modulus assignment infix operator, e.g. 'x %%= y'
BITAND_ASSIGN : '&='  ; // Bitwise AND assignment infix operator, e.g. 'x &= y'
BITXOR_ASSIGN : '^='  ; // Bitwise XOR assignment infix operator, e.g. 'x ^= y'
BITOR_ASSIGN  : '|='  ; // Bitwise OR assignment infix operator, e.g. 'x |= y'
SHL_ASSIGN    : '<<=' ; // Shift left assignment infix operator, e.g. 'x <<= y'
SHR_ASSIGN    : '>>=' ; // Shift right assignment infix operator, e.g. 'x >>= y'
SHRA_ASSIGN   : '>>>='; // Shift right arithmetic assignment infix operator, e.g. 'x >>>= y'

/*** Relational Operators *************************************************************************/
LT  : '<'   ; // Less than infix operator, e.g. 'x < y'
GT  : '>'   ; // Greater than infix operator, e.g. 'x > y'
LE  : '<='  ; // Less than or equal to infix operator, e.g. 'x <= y'
GE  : '>='  ; // Greater than or equal to infix operator, e.g. 'x >= y'
EQ  : '=='  ; // Equal to infix operator, e.g. 'x == y'
NE  : '!='  ; // Not equal to infix operator, e.g. 'x != y'
EQR : '<?>' ; // Equal references infix operator, e.g. 'x <?> y'
NER : '<!>' ; // Not equal references infix operator, e.g. 'x <!> y'

/*** Whitespace and Comments **********************************************************************/
// Whitespace, i.e. spaces, tabs, and form feeds
WS: [ \t]+ -> channel(WHITESPACE_CHANNEL);
// Newline, i.e. LF or CRLF
NL: ('\r'? '\n')+ -> channel(WHITESPACE_CHANNEL);
// Single line comment, i.e. '//' followed by any characters until the end of the line
LineComment: '//' ~[\r\n]* -> channel(COMMENTS_CHANNEL);

/*** Identifiers **********************************************************************************/
// Upper camel case identifier, i.e. a class or module name, e.g. 'MyClass'
UpperCamelCaseName: [A-Z] [a-zA-Z0-9]*;
// Lower camel case identifier, i.e. a field, variable, or function name, e.g. 'my_var'
LowerSnakeCaseName: [a-z_] [a-z0-9_]*;

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
