lexer grammar ZfgLexer;
channels { WHITESPACE_CHANNEL, COMMENTS_CHANNEL }

// Declaration Keywords
LET: 'let'; // Immutable variable declaration, cannot be reassinged without redecalration
MUT: 'mut'; // Mutable variable declaration, can be reassigned without redecalration
PUB: 'pub'; // Public visibility modifier, can be accessed from other modules
USE: 'use'; // Use symbol declaration, can import from aother modudle or locally defined

// Control Flow Keywords
IF:       'if'      ; // Conditional statement
ELSE:     'else'    ; // Conditional alternative
LOOP:     'loop'    ; // Infinite loop, aka 'while(true)'
WHILE:    'while'   ; // Conditional loop
FOR:      'for'     ; // Iterative loop
BREAK:    'break'   ; // Exits nearest enclosing loop
CONTINUE: 'continue'; // Skip to the next step of the nearest enclosing loop
RETURN:   'return'  ; // Return from function

// Inferred Types
FUN: 'fun'; // Inferred function type
VAR: 'var'; // Inferred data type

// Primitive Types
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
DOUBC: '::'; // Path separator (e.g. 'std::io::println')

// Groupors (Circumfix Operators)
LPAREN: '(' ; // Expression grouping, function parameters
RPAREN: ')' ;
LBRACE: '{' ; // Scoped block, function body, type body
RBRACE: '}' ;
LBRACK: '[' ; // Array literal, array access
RBRACK: ']' ;

// Prefix and Postfix Operators
INC: '++' ; // Increment prefix or postfix operator, e.g. '++x' or 'x++'
DEC: '--' ; // Decrement prefix or postfix operator, e.g. '--x' or 'x--'

// Arithmetic Operators
ADD: '+'  ; // Identity prefix or addition infix operator, e.g. '+x' or 'x + y'
SUB: '-'  ; // Negation prefix or subtraction infix operator, e.g. '-x' or 'x - y'
MUL: '*'  ; // Multiplication infix operator, e.g. 'x * y'
DIV: '/'  ; // Division infix operator, e.g. 'x / y'
REM: '%'  ; // Remainder infix operator, e.g. 'x % y'
MOD: '%%' ; // Modulo infix operator, e.g. 'x %% y'

// Bitwise Operators
NOT: '~'  ; // Bitwise NOT prefix operator, e.g. '~x'
AND: '&'  ; // Bitwise AND infix operator, e.g. 'x & y'
IOR: '|'  ; // Bitwise OR infix operator, e.g. 'x | y'
XOR: '^'  ; // Bitwise XOR infix operator, e.g. 'x ^ y'
SHL: '<<' ; // Shift left infix operator, e.g. 'x << y'
SHR: '>>' ; // Shift right infix operator, e.g. 'x >> y'

// Relational Operators
EQL: '=='  ; // Equal to infix operator, e.g. 'x == y'
NEQ: '!='  ; // Not equal to infix operator, e.g. 'x != y'
LTN: '<'   ; // Less than infix operator, e.g. 'x < y'
GTN: '>'   ; // Greater than infix operator, e.g. 'x > y'
LEQ: '<='  ; // Less than or equal to infix operator, e.g. 'x <= y'
GEQ: '>='  ; // Greater than or equal to infix operator, e.g. 'x >= y'
TWC: '<=>' ; // Three-way compare infix operator, e.g. 'x <=> y'

// Logical Operators
LNT: '!'  ; // Logical NOT prefix operator, e.g. '!x'
LCJ: '&&' ; // Logical AND infix operator, e.g. 'x && y'
LDJ: '||' ; // Logical IOR infix operator, e.g. 'x || y'

// Assignment Operator
SETA: '='   ; // Assignment infix operator, e.g. 'x = y'

// Arithmetic Assignment Operators
ADDA: '+='  ; // Addition assignment infix operator, e.g. 'x += y'
SUBA: '-='  ; // Subtraction assignment infix operator, e.g. 'x -= y'
MULA: '*='  ; // Multiplication assignment infix operator, e.g. 'x *= y'
DIVA: '/='  ; // Division assignment infix operator, e.g. 'x /= y'
REMA: '%='  ; // Remainder assignment infix operator, e.g. 'x %= y'
MODA: '%%=' ; // Modulo assignment infix operator, e.g. 'x %%= y'

// Bitwise Assignment Operators
ANDA: '&='  ; // Bitwise AND assignment infix operator, e.g. 'x &= y'
IORA: '|='  ; // Bitwise OR assignment infix operator, e.g. 'x |= y'
XORA: '^='  ; // Bitwise XOR assignment infix operator, e.g. 'x ^= y'
SHLA: '<<=' ; // Shift left assignment infix operator, e.g. 'x <<= y'
SHRA: '>>=' ; // Shift right assignment infix operator, e.g. 'x >>= y'

// Litearls
BitLit: 'true' | 'false';
IntLit: (BinIntLit | OctIntLit | DecIntLit | HexIntLit) (DigitSep* IntType)?;
FltLit: (DecFltLit | HexFltLit) (DigitSep* FltType)?;

// Identifiers
UpperId: [A-Z][a-zA-Z0-9]*;
LowerId: [a-z_][a-z0-9_]*;

// Whitespace and Comments
WsInd: [ \t]+ -> channel(WHITESPACE_CHANNEL);        // Whitespace indentation, i.e. space or tab
WsEol: ([\r]? [\n])+ -> channel(WHITESPACE_CHANNEL); // Whitespace end-of-line, i.e. LF or CRLF
LnCom: '//' ~[\r\n]* -> channel(COMMENTS_CHANNEL);   // Line comment, i.e. '// ...'

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
