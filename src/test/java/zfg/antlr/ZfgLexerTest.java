package zfg.antlr;

import static zfg.antlr.TokenMatcher.assertTokens;
import static zfg.antlr.TokenMatcher.token;

import org.junit.jupiter.api.Test;

public class ZfgLexerTest {

  @Test
  public void testKeywords() {
    assertTokens("let", token(ZfgLexer.LET));
    assertTokens("mut", token(ZfgLexer.MUT));
    assertTokens("fun", token(ZfgLexer.FUN));
  }

  @Test
  public void testPrimitives() {
    assertTokens("var", token(ZfgLexer.VAR));
    assertTokens("bit", token(ZfgLexer.BIT));
    assertTokens("i08", token(ZfgLexer.I08));
    assertTokens("i16", token(ZfgLexer.I16));
    assertTokens("i32", token(ZfgLexer.I32));
    assertTokens("i64", token(ZfgLexer.I64));
    assertTokens("f32", token(ZfgLexer.F32));
    assertTokens("f64", token(ZfgLexer.F64));
  }

  @Test
  public void testLogicalLiterals() {
    assertTokens("true", token(ZfgLexer.TRUE));
    assertTokens("false", token(ZfgLexer.FALSE));
  }

  @Test
  public void testNumericLiterals() {
    // Natural integer literal
    assertTokens("0", token(ZfgLexer.DecIntegerLiteral).text("0"));
    assertTokens("12", token(ZfgLexer.DecIntegerLiteral).text("12"));
    // Binary literal
    assertTokens("0b0", token(ZfgLexer.BinIntegerLiteral).text("0b0"));
    assertTokens("0b0_0", token(ZfgLexer.BinIntegerLiteral).text("0b0_0"));
    assertTokens("0b10", token(ZfgLexer.BinIntegerLiteral).text("0b10"));
    // Octal literal
    assertTokens("0o0", token(ZfgLexer.OctIntegerLiteral).text("0o0"));
    assertTokens("0o0_0", token(ZfgLexer.OctIntegerLiteral).text("0o0_0"));
    assertTokens("0o7654321", token(ZfgLexer.OctIntegerLiteral).text("0o7654321"));
    // Decimal literal
    assertTokens("0d0", token(ZfgLexer.DecIntegerLiteral).text("0d0"));
    assertTokens("0d0_0", token(ZfgLexer.DecIntegerLiteral).text("0d0_0"));
    assertTokens("0d109", token(ZfgLexer.DecIntegerLiteral).text("0d109"));
    // Hexadecimal literal
    assertTokens("0x0", token(ZfgLexer.HexIntegerLiteral).text("0x0"));
    assertTokens("0x0_0", token(ZfgLexer.HexIntegerLiteral).text("0x0_0"));
    assertTokens("0xFEDCAB9876543210", token(ZfgLexer.HexIntegerLiteral).text("0xFEDCAB9876543210"));
    // Floating point literal
    assertTokens("0.", token(ZfgLexer.DecFloatLiteral).text("0."));
    assertTokens(".0", token(ZfgLexer.DecFloatLiteral).text(".0"));
    assertTokens("0.0", token(ZfgLexer.DecFloatLiteral).text("0.0"));
    assertTokens("0e0", token(ZfgLexer.DecFloatLiteral).text("0e0"));
    assertTokens("0e+0", token(ZfgLexer.DecFloatLiteral).text("0e+0"));
    assertTokens("0e-0", token(ZfgLexer.DecFloatLiteral).text("0e-0"));
    assertTokens("0.0e0", token(ZfgLexer.DecFloatLiteral).text("0.0e0"));
    assertTokens("31.42", token(ZfgLexer.DecFloatLiteral).text("31.42"));
    // Hexadecimal floating point literal
    assertTokens("0x0.", token(ZfgLexer.HexFloatLiteral).text("0x0."));
    assertTokens("0x.0", token(ZfgLexer.HexFloatLiteral).text("0x.0"));
    assertTokens("0x0.0", token(ZfgLexer.HexFloatLiteral).text("0x0.0"));
    assertTokens("0x0p0", token(ZfgLexer.HexFloatLiteral).text("0x0p0"));
    assertTokens("0x0p+0", token(ZfgLexer.HexFloatLiteral).text("0x0p+0"));
    assertTokens("0x0p-0", token(ZfgLexer.HexFloatLiteral).text("0x0p-0"));
    assertTokens("0x0.0p0", token(ZfgLexer.HexFloatLiteral).text("0x0.0p0"));
    assertTokens("0xF1.9Ap3", token(ZfgLexer.HexFloatLiteral).text("0xF1.9Ap3"));
  }

  @Test
  public void testSeparators() {
    assertTokens(",", token(ZfgLexer.COMMA));
    assertTokens(".", token(ZfgLexer.DOT));
    assertTokens(":", token(ZfgLexer.COLON));
    assertTokens(";", token(ZfgLexer.SEMIC));
    assertTokens("(", token(ZfgLexer.LPAREN));
    assertTokens(")", token(ZfgLexer.RPAREN));
    assertTokens("{", token(ZfgLexer.LBRACE));
    assertTokens("}", token(ZfgLexer.RBRACE));
    assertTokens("[", token(ZfgLexer.LBRACK));
    assertTokens("]", token(ZfgLexer.RBRACK));
  }

  @Test
  public void testArithmeticOperators() {
    assertTokens("++", token(ZfgLexer.INCR));
    assertTokens("--", token(ZfgLexer.DECR));
    assertTokens("~", token(ZfgLexer.BITINV));
    assertTokens("!", token(ZfgLexer.EQZ));
    assertTokens("!!", token(ZfgLexer.NEQZ));
    assertTokens("+", token(ZfgLexer.ADD));
    assertTokens("-", token(ZfgLexer.SUB));
    assertTokens("*", token(ZfgLexer.MUL));
    assertTokens("**", token(ZfgLexer.POW));
    assertTokens("/", token(ZfgLexer.DIV));
    assertTokens("%", token(ZfgLexer.MOD));
    assertTokens("%%", token(ZfgLexer.PMOD));
    assertTokens("&", token(ZfgLexer.BITAND));
    assertTokens("^", token(ZfgLexer.BITXOR));
    assertTokens("|", token(ZfgLexer.BITOR));
    assertTokens("<<", token(ZfgLexer.SHL));
    assertTokens(">>", token(ZfgLexer.SHR));
    assertTokens(">>>", token(ZfgLexer.SHRA));
  }

  @Test
  public void testAssignmentOperators() {
    assertTokens("=", token(ZfgLexer.ASSIGN));
    assertTokens("+=", token(ZfgLexer.ADD_ASSIGN));
    assertTokens("-=", token(ZfgLexer.SUB_ASSIGN));
    assertTokens("*=", token(ZfgLexer.MUL_ASSIGN));
    assertTokens("**=", token(ZfgLexer.POW_ASSIGN));
    assertTokens("/=", token(ZfgLexer.DIV_ASSIGN));
    assertTokens("%=", token(ZfgLexer.MOD_ASSIGN));
    assertTokens("%%=", token(ZfgLexer.PMOD_ASSIGN));
    assertTokens("&=", token(ZfgLexer.BITAND_ASSIGN));
    assertTokens("^=", token(ZfgLexer.BITXOR_ASSIGN));
    assertTokens("|=", token(ZfgLexer.BITOR_ASSIGN));
    assertTokens("<<=", token(ZfgLexer.SHL_ASSIGN));
    assertTokens(">>=", token(ZfgLexer.SHR_ASSIGN));
    assertTokens(">>>=", token(ZfgLexer.SHRA_ASSIGN));
  }

  @Test
  public void testRelationalOperators() {
    assertTokens("<", token(ZfgLexer.LT));
    assertTokens(">", token(ZfgLexer.GT));
    assertTokens("<=", token(ZfgLexer.LE));
    assertTokens(">=", token(ZfgLexer.GE));
    assertTokens("==", token(ZfgLexer.EQ));
    assertTokens("!=", token(ZfgLexer.NE));
    assertTokens("<?>", token(ZfgLexer.EQR));
    assertTokens("<!>", token(ZfgLexer.NER));
  }

  @Test
  public void testWhitespace() {
    // Whitespace
    assertTokens(" ", token(ZfgLexer.WS));
    assertTokens("\t", token(ZfgLexer.WS));
    assertTokens("\f", token(ZfgLexer.WS));
    // Newline
    assertTokens("\n", token(ZfgLexer.NL));
    assertTokens("\r\n", token(ZfgLexer.NL));
    // Whitespace and newlines
    assertTokens(" \n\t \f\r\n\r\n",
        token(ZfgLexer.WS), token(ZfgLexer.NL), token(ZfgLexer.WS), token(ZfgLexer.NL));
  }

  @Test
  public void testIdentifiers() {
    // Upper camel case identifier
    assertTokens("A", token(ZfgLexer.UpperCamelCaseName).text("A"));
    assertTokens("Z", token(ZfgLexer.UpperCamelCaseName).text("Z"));
    assertTokens("A0", token(ZfgLexer.UpperCamelCaseName).text("A0"));
    assertTokens("Z9", token(ZfgLexer.UpperCamelCaseName).text("Z9"));
    assertTokens("Aa", token(ZfgLexer.UpperCamelCaseName).text("Aa"));
    assertTokens("Zz", token(ZfgLexer.UpperCamelCaseName).text("Zz"));
    assertTokens("A0a", token(ZfgLexer.UpperCamelCaseName).text("A0a"));
    assertTokens("Z9z", token(ZfgLexer.UpperCamelCaseName).text("Z9z"));
    assertTokens("FooBar", token(ZfgLexer.UpperCamelCaseName).text("FooBar"));
    // Lower snake case identifier
    assertTokens("a", token(ZfgLexer.LowerSnakeCaseName).text("a"));
    assertTokens("z", token(ZfgLexer.LowerSnakeCaseName).text("z"));
    assertTokens("a0", token(ZfgLexer.LowerSnakeCaseName).text("a0"));
    assertTokens("z9", token(ZfgLexer.LowerSnakeCaseName).text("z9"));
    assertTokens("a_a", token(ZfgLexer.LowerSnakeCaseName).text("a_a"));
    assertTokens("z_z", token(ZfgLexer.LowerSnakeCaseName).text("z_z"));
    assertTokens("a0_a", token(ZfgLexer.LowerSnakeCaseName).text("a0_a"));
    assertTokens("z9_z", token(ZfgLexer.LowerSnakeCaseName).text("z9_z"));
    assertTokens("_", token(ZfgLexer.LowerSnakeCaseName).text("_"));
    assertTokens("__", token(ZfgLexer.LowerSnakeCaseName).text("__"));
    assertTokens("_a_", token(ZfgLexer.LowerSnakeCaseName).text("_a_"));
    assertTokens("a_", token(ZfgLexer.LowerSnakeCaseName).text("a_"));
    assertTokens("foo_bar", token(ZfgLexer.LowerSnakeCaseName).text("foo_bar"));
  }
}
