package tests;

import exceptions.InvalidSymbolException;
import org.junit.Test;
import symbols.Literal;

import static org.junit.Assert.*;

public class LiteralTest {

    private Literal literal;

    @org.junit.Test
    public void assign() {
        // test not negated literal
        literal = new Literal("p");
        literal.assign(true);
        assertTrue(literal.truthValue());
        literal.assign(false);
        assertFalse(literal.truthValue());

        // test negated literal
        literal = new Literal("~p");
        literal.assign(true);
        assertFalse(literal.truthValue());
        literal.assign(false);
        assertTrue(literal.truthValue());
    }

    @Test
    public void truthValue() {
        literal = new Literal("literal");
        literal.assign(true);
        assertTrue(literal.truthValue());
        literal.assign(false);
        assertFalse(literal.truthValue());

        literal = new Literal("~literal");
        literal.assign(true);
        assertFalse(literal.truthValue());
        literal.assign(false);
        assertTrue(literal.truthValue());

        literal = new Literal("T");
        assertTrue(literal.truthValue());
        literal = new Literal("F");
        assertFalse(literal.truthValue());
    }

    @org.junit.Test
    public void isContradiction() {
        literal = new Literal("F");
        assertTrue(literal.isContradiction());
        literal = new Literal("~F");
        assertFalse(literal.isContradiction());
    }

    @org.junit.Test
    public void isTautology() {
        literal = new Literal("T");
        assertTrue(literal.isTautology());
        literal = new Literal("~T");
        assertFalse(literal.isTautology());
    }

    @org.junit.Test
    public void isNegated() {

        // test contradiction
        literal = new Literal("F");
        assertFalse(literal.isNegated());

        // test tautology
        literal = new Literal("T");
        assertFalse(literal.isNegated());

        // test positive
        literal = new Literal("apple");
        assertFalse(literal.isNegated());

        // test negated
        literal = new Literal("~apple");
        assertTrue(literal.isNegated());

    }

    @org.junit.Test
    public void rawEquals() {
        literal = new Literal("chicken");
        assertTrue(literal.rawEquals(new Literal("chicken")));
    }

    @org.junit.Test
    public void testEquals() {
    }
}