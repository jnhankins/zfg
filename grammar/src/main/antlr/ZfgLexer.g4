lexer grammar ZfgLexer;
channels { WHITESPACE_CHANNEL, COMMENTS_CHANNEL }

// Keywords
LET: 'let'; // Immutable variable declaration, cannot be reassinged without redecalration
MUT: 'mut'; // Mutable variable declaration, can be reassigned without redecalration
FUN: 'fun'; // Function declaration
RET: 'ret'; // Function return value

// Primitive Types
VAR: 'var'; // Inferred type, aka 'auto'
BIT: 'bit'; // Single bit, aka 'boolean'
U08: 'u08'; // 8-bit unsigned integer, aka 'char'
U16: 'u16'; // 16-bit unsigned integer, aka 'ushort'
U32: 'u32'; // 32-bit unsigned integer, aka 'uint'
U64: 'u64'; // 64-bit unsigned integer, aka 'ulong'
I08: 'i08'; // 8-bit signed integer, aka 'byte'
I16: 'i16'; // 16-bit signed integer, aka 'short'
I32: 'i32'; // 32-bit signed integer, aka 'int'
I64: 'i64'; // 64-bit signed integer, aka 'long'
F32: 'f32'; // 32-bit floating point number, aka 'float'
F64: 'f64'; // 64-bit floating point number, aka 'double'

// Separators
COMMA: ',' ; // Argument separator, array element separator, object field separator
POINT: '.' ; // Path separator (e.g. 'std.io.println')
COLON: ':' ; // Variable id-type separator, field id-type separator, literal id-value separator
SEMIC: ';' ; // Statement separator, type member separator

// Grouping Circumfix Operators
LPAREN: '(' ; // Expression grouping, function parameters
RPAREN: ')' ;
LBRACE: '{' ; // Scoped block, function body, type body
RBRACE: '}' ;
LBRACK: '[' ; // Array literal, array access
RBRACK: ']' ;

// Arithmetic Operators
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

// Assignment Operators
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

// Relational Operators
LT:  '<'   ; // Less than infix operator, e.g. 'x < y'
GT:  '>'   ; // Greater than infix operator, e.g. 'x > y'
LE:  '<='  ; // Less than or equal to infix operator, e.g. 'x <= y'
GE:  '>='  ; // Greater than or equal to infix operator, e.g. 'x >= y'
EQ:  '=='  ; // Equal to infix operator, e.g. 'x == y'
NE:  '!='  ; // Not equal to infix operator, e.g. 'x != y'
EQR: '===' ; // Equal references infix operator, e.g. 'x <?> y'
NER: '!==' ; // Not equal references infix operator, e.g. 'x <!> y'
CMP: '<=>' ; // Compare infix operator, e.g. 'x <=> y'

// Litearls
BitLit: 'true' | 'false';
IntLit: (BinIntLit | OctIntLit | DecIntLit | HexIntLit) (DigitSep* IntType)?;
FltLit: (DecFltLit | HexFltLit) (DigitSep* FltType)?;

// Identifiers
UpperId: [A-Z][a-zA-Z0-9]*;
LowerId: [a-z_][a-z0-9_]*;

// Whitespace and Comments
Ws: [ \t]+ -> channel(WHITESPACE_CHANNEL);        // Whitespace, i.e. space or tab
Nl: ([\r]? [\n])+ -> channel(WHITESPACE_CHANNEL); // Newline, i.e. LF or CRLF
Lc: '//' ~[\r\n]* -> channel(COMMENTS_CHANNEL);   // Line comment, i.e. '// ...'

// Numeric Literal Fragments
fragment BinDigit: [0-1];    // Binary digit
fragment OctDigit: [0-7];    // Octal digit
fragment DecDigit: [0-9];    // Decimal digit
fragment HexDigit: [0-9A-F]; // Hexadecimal digit
fragment DigitSep: '_';      // Digit separator (optionally used for readability)
fragment BinDigits: BinDigit (DigitSep* BinDigit)*; // Binary digit sequence
fragment OctDigits: OctDigit (DigitSep* OctDigit)*; // Octal digit sequence
fragment DecDigits: DecDigit (DigitSep* DecDigit)*; // Decimal digit sequence
fragment HexDigits: HexDigit (DigitSep* HexDigit)*; // Hexadecimal digit sequence
fragment IntType: BIT | I08 | I16 | I32 | I64; // Integer literal type suffix
fragment BinIntLit: '0b'  BinDigits; // Binary integer literal
fragment OctIntLit: '0o'  OctDigits; // Octal integer literal
fragment DecIntLit: '0d'? DecDigits; // Decimal integer literal
fragment HexIntLit: '0x'  HexDigits; // Hexadecimal integer literal
fragment FltType: F32 | F64; // Floating point literal type suffix
fragment DecFltExp: 'e' [+-]? DecDigits; // Decimal floating point exponent
fragment HexFltExp: 'p' [+-]? DecDigits; // Decimal floating point exponent
fragment DecFltLit: '0d'? (DecDigits '.' DecDigits? DecFltExp? | '.' DecDigits DecFltExp? | DecDigits DecFltExp);
fragment HexFltLit: '0x'  (HexDigits '.' HexDigits? HexFltExp? | '.' HexDigits HexFltExp? | HexDigits HexFltExp);
