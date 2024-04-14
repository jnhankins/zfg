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
  public void testSeparators() {
    assertTokens(",", token(ZfgLexer.COMMA));
    assertTokens(".", token(ZfgLexer.POINT));
    assertTokens(":", token(ZfgLexer.COLON));
    assertTokens(";", token(ZfgLexer.SEMIC));
  }

  @Test
  public void testGroupors() {
    assertTokens("(", token(ZfgLexer.LPAREN));
    assertTokens(")", token(ZfgLexer.RPAREN));
    assertTokens("{", token(ZfgLexer.LBRACE));
    assertTokens("}", token(ZfgLexer.RBRACE));
    assertTokens("[", token(ZfgLexer.LBRACK));
    assertTokens("]", token(ZfgLexer.RBRACK));
  }

  @Test
  public void testArithmeticOperators() {
    assertTokens("++", token(ZfgLexer.INC));
    assertTokens("--", token(ZfgLexer.DEC));
    assertTokens("+", token(ZfgLexer.ADD));
    assertTokens("-", token(ZfgLexer.SUB));
    assertTokens("*", token(ZfgLexer.MUL));
    assertTokens("/", token(ZfgLexer.DIV));
    assertTokens("%", token(ZfgLexer.REM));
    assertTokens("%%", token(ZfgLexer.MOD));
    assertTokens("!", token(ZfgLexer.NOT));
    assertTokens("&", token(ZfgLexer.AND));
    assertTokens("|", token(ZfgLexer.IOR));
    assertTokens("^", token(ZfgLexer.XOR));
    assertTokens("<<", token(ZfgLexer.SHL));
    assertTokens(">>", token(ZfgLexer.SHR));
  }

  @Test
  public void testAssignmentOperators() {
    assertTokens("=", token(ZfgLexer.SETA));
    assertTokens("+=", token(ZfgLexer.ADDA));
    assertTokens("-=", token(ZfgLexer.SUBA));
    assertTokens("*=", token(ZfgLexer.MULA));
    assertTokens("/=", token(ZfgLexer.DIVA));
    assertTokens("%=", token(ZfgLexer.REMA));
    assertTokens("%%=", token(ZfgLexer.MODA));
    assertTokens("&=", token(ZfgLexer.ANDA));
    assertTokens("|=", token(ZfgLexer.IORA));
    assertTokens("^=", token(ZfgLexer.XORA));
    assertTokens("<<=", token(ZfgLexer.SHLA));
    assertTokens(">>=", token(ZfgLexer.SHRA));
  }

  @Test
  public void testRelationalOperators() {
    assertTokens("<", token(ZfgLexer.LT));
    assertTokens(">", token(ZfgLexer.GT));
    assertTokens("<=", token(ZfgLexer.LE));
    assertTokens(">=", token(ZfgLexer.GE));
    assertTokens("==", token(ZfgLexer.EQ));
    assertTokens("!=", token(ZfgLexer.NE));
    assertTokens("===", token(ZfgLexer.EQR));
    assertTokens("!==", token(ZfgLexer.NER));
    assertTokens("<=>", token(ZfgLexer.CMP));
  }

  @Test
  public void testLiterals() {
    // Boolean literal
    assertTokens("true", token(ZfgLexer.BitLit).text("true"));
    assertTokens("false", token(ZfgLexer.BitLit).text("false"));
    // Natural integer literal
    assertTokens("0", token(ZfgLexer.IntLit).text("0"));
    assertTokens("12", token(ZfgLexer.IntLit).text("12"));
    // Binary literal
    assertTokens("0b0", token(ZfgLexer.IntLit).text("0b0"));
    assertTokens("0b0_0", token(ZfgLexer.IntLit).text("0b0_0"));
    assertTokens("0b10", token(ZfgLexer.IntLit).text("0b10"));
    // Octal literal
    assertTokens("0o0", token(ZfgLexer.IntLit).text("0o0"));
    assertTokens("0o0_0", token(ZfgLexer.IntLit).text("0o0_0"));
    assertTokens("0o7654321", token(ZfgLexer.IntLit).text("0o7654321"));
    // Decimal literal
    assertTokens("0d0", token(ZfgLexer.IntLit).text("0d0"));
    assertTokens("0d0_0", token(ZfgLexer.IntLit).text("0d0_0"));
    assertTokens("0d109", token(ZfgLexer.IntLit).text("0d109"));
    // Hexadecimal literal
    assertTokens("0x0", token(ZfgLexer.IntLit).text("0x0"));
    assertTokens("0x0_0", token(ZfgLexer.IntLit).text("0x0_0"));
    assertTokens("0xFEDCAB9876543210", token(ZfgLexer.IntLit).text("0xFEDCAB9876543210"));
    // Floating point literal
    assertTokens("0.", token(ZfgLexer.FltLit).text("0."));
    assertTokens(".0", token(ZfgLexer.FltLit).text(".0"));
    assertTokens("0.0", token(ZfgLexer.FltLit).text("0.0"));
    assertTokens("0e0", token(ZfgLexer.FltLit).text("0e0"));
    assertTokens("0e+0", token(ZfgLexer.FltLit).text("0e+0"));
    assertTokens("0e-0", token(ZfgLexer.FltLit).text("0e-0"));
    assertTokens("0.0e0", token(ZfgLexer.FltLit).text("0.0e0"));
    assertTokens("31.42", token(ZfgLexer.FltLit).text("31.42"));
    // Hexadecimal floating point literal
    assertTokens("0x0.", token(ZfgLexer.FltLit).text("0x0."));
    assertTokens("0x.0", token(ZfgLexer.FltLit).text("0x.0"));
    assertTokens("0x0.0", token(ZfgLexer.FltLit).text("0x0.0"));
    assertTokens("0x0p0", token(ZfgLexer.FltLit).text("0x0p0"));
    assertTokens("0x0p+0", token(ZfgLexer.FltLit).text("0x0p+0"));
    assertTokens("0x0p-0", token(ZfgLexer.FltLit).text("0x0p-0"));
    assertTokens("0x0.0p0", token(ZfgLexer.FltLit).text("0x0.0p0"));
    assertTokens("0xF1.9Ap3", token(ZfgLexer.FltLit).text("0xF1.9Ap3"));
  }

  @Test
  public void testIdentifiers() {
    // Upper camel case identifier
    assertTokens("A", token(ZfgLexer.UpperId).text("A"));
    assertTokens("Z", token(ZfgLexer.UpperId).text("Z"));
    assertTokens("A0", token(ZfgLexer.UpperId).text("A0"));
    assertTokens("Z9", token(ZfgLexer.UpperId).text("Z9"));
    assertTokens("Aa", token(ZfgLexer.UpperId).text("Aa"));
    assertTokens("Zz", token(ZfgLexer.UpperId).text("Zz"));
    assertTokens("A0a", token(ZfgLexer.UpperId).text("A0a"));
    assertTokens("Z9z", token(ZfgLexer.UpperId).text("Z9z"));
    assertTokens("FooBar", token(ZfgLexer.UpperId).text("FooBar"));
    // Lower snake case identifier
    assertTokens("a", token(ZfgLexer.LowerId).text("a"));
    assertTokens("z", token(ZfgLexer.LowerId).text("z"));
    assertTokens("a0", token(ZfgLexer.LowerId).text("a0"));
    assertTokens("z9", token(ZfgLexer.LowerId).text("z9"));
    assertTokens("a_a", token(ZfgLexer.LowerId).text("a_a"));
    assertTokens("z_z", token(ZfgLexer.LowerId).text("z_z"));
    assertTokens("a0_a", token(ZfgLexer.LowerId).text("a0_a"));
    assertTokens("z9_z", token(ZfgLexer.LowerId).text("z9_z"));
    assertTokens("_", token(ZfgLexer.LowerId).text("_"));
    assertTokens("__", token(ZfgLexer.LowerId).text("__"));
    assertTokens("_a_", token(ZfgLexer.LowerId).text("_a_"));
    assertTokens("a_", token(ZfgLexer.LowerId).text("a_"));
    assertTokens("foo_bar", token(ZfgLexer.LowerId).text("foo_bar"));
  }

  @Test
  public void testWhitespaceAndComments() {
    // Whitespace
    assertTokens(" ", token(ZfgLexer.Ws));
    assertTokens("\t", token(ZfgLexer.Ws));
    // Newline
    assertTokens("\n", token(ZfgLexer.Nl));
    assertTokens("\r\n", token(ZfgLexer.Nl));
    // Whitespace and newlines
    assertTokens(" \n\t \f\r\n\r\n",
        token(ZfgLexer.Ws), token(ZfgLexer.Nl), token(ZfgLexer.Ws), token(ZfgLexer.Nl));
    // Line comment
    assertTokens("//", token(ZfgLexer.Lc));
    assertTokens("// blah blah blah", token(ZfgLexer.Lc));
  }
}
