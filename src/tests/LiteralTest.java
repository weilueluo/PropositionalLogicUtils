package tests;

import core.exceptions.InvalidSymbolException;
import core.symbols.Literal;
import org.junit.Test;

import static org.junit.Assert.*;

public class LiteralTest {

    private Literal literal;

    @Test
    public void assign() {
        // test not negated literal
        literal = Literal.factory("p");
        literal.assign(true);
        assertTrue(literal.truthValue());
        literal.assign(false);
        assertFalse(literal.truthValue());

        // test negated literal
        literal = Literal.factory("~p");
        literal.assign(true);
        assertFalse(literal.truthValue());
        literal.assign(false);
        assertTrue(literal.truthValue());
    }

    @Test
    public void truthValue() {
        literal = Literal.factory("literal");
        literal.assign(true);
        assertTrue(literal.truthValue());
        literal.assign(false);
        assertFalse(literal.truthValue());

        literal = Literal.factory("~literal");
        literal.assign(true);
        assertFalse(literal.truthValue());
        literal.assign(false);
        assertTrue(literal.truthValue());

        literal = Literal.factory("T");
        assertTrue(literal.truthValue());
        literal = Literal.factory("F");
        assertFalse(literal.truthValue());
    }

    @Test
    public void isContradiction() {
        literal = Literal.factory("F");
        assertTrue(literal.isContradiction());
        literal = Literal.factory("~F");
        assertFalse(literal.isContradiction());
    }

    @Test
    public void isTautology() {
        literal = Literal.factory("T");
        assertTrue(literal.isTautology());
        literal = Literal.factory("~T");
        assertFalse(literal.isTautology());
    }

    @Test
    public void isNegated() {

        // test contradiction
        literal = Literal.factory("F");
        assertFalse(literal.isNegated());

        // test tautology
        literal = Literal.factory("T");
        assertFalse(literal.isNegated());

        // test negated contradiction
        literal = Literal.factory("~F");
        assertTrue(literal.isNegated());

        // test negated tautology
        literal = Literal.factory("~T");
        assertTrue(literal.isNegated());

        // test positive
        literal = Literal.factory("apple");
        assertFalse(literal.isNegated());

        // test negated
        literal = Literal.factory("~apple");
        assertTrue(literal.isNegated());

        // test more negation
        literal = Literal.factory("~~banana");
        assertFalse(literal.isNegated());
        assertTrue(literal.rawEquals(Literal.factory("banana"))); // check if negation is checked
        literal = Literal.factory("~~~banana");
        assertTrue(literal.rawEquals(Literal.factory("banana"))); // check if negation is checked
        assertTrue(literal.isNegated());

    }

    @Test
    public void rawEquals() {
        literal = Literal.factory("chicken");
        assertTrue(literal.rawEquals(Literal.factory("chicken")));
        literal = Literal.factory("~chicken");
        assertTrue(literal.rawEquals(Literal.factory("chicken")));

    }

    @Test
    public void testEquals() {
        literal = Literal.factory("chicken");
        assertTrue(literal.equals(Literal.factory("chicken")));
        literal = Literal.factory("~chicken");
        assertTrue(literal.equals(Literal.factory("~chicken")));
    }

    @Test
    public void testSingleton() {
        literal = Literal.factory("T");
        assertSame(literal, Literal.factory("T"));

        literal = Literal.factory("~T");
        assertSame(literal, Literal.factory("~T"));

        literal = Literal.factory("F");
        assertSame(literal, Literal.factory("F"));

        literal = Literal.factory("~F");
        assertSame(literal, Literal.factory("~F"));
    }

    @Test
    public void testFactoryValidLiteral() {
        literal = Literal.factory("Water");
        literal = Literal.factory("~MAYDAY");
        literal = Literal.factory("~~AppleOnTheGround");
        literal = Literal.factory("~~ ~~ ~~~~   MillionBananaKingdom");
        literal = Literal.factory("~             ~ CHOPSTICK                     ");
    }

    @Test(expected = InvalidSymbolException.class)
    public void testFactoryInvalidLiteral() {
        literal = Literal.factory("Wate#r");
    }

    @Test(expected = InvalidSymbolException.class)
    public void testFactoryInvalidLiteral1() {
        literal = Literal.factory("~3MAYDAY");
    }

    @Test(expected = InvalidSymbolException.class)
    public void testFactoryInvalidLiteral2() {
        literal = Literal.factory("~ wss ~AppleOnTheGround");
    }

    @Test(expected = InvalidSymbolException.class)
    public void testFactoryInvalidLiteral3() {
        literal = Literal.factory("~~ ~~ ~x~~   MillionBananaKingdom");
    }

    @Test(expected = InvalidSymbolException.class)
    public void testFactoryInvalidLiteral4() {
        literal = Literal.factory("~             ~ CHOPSTICK       x  ");
    }




}