package tests;

import exceptions.InvalidSymbolException;
import symbols.Literal;

import static org.junit.Assert.*;

public class LiteralTest {

    @org.junit.Test
    public void assign() {
        Literal literal;

        // test not negated literal
        try {
            literal = new Literal("p");
        } catch (InvalidSymbolException e) {
            fail(e.getMessage());
            return;
        }
        literal.assign(true);
        assert(literal.truthValue());
        literal.assign(false);
        assert(!literal.truthValue());

        // test negated literal
        try {
            literal = new Literal("~p");
        } catch (InvalidSymbolException e) {
            fail(e.getMessage());
            return;
        }
        literal.assign(true);
        assert(!literal.truthValue());
        literal.assign(false);
        assert(literal.truthValue());
    }

    @org.junit.Test
    public void truthValue() {
    }

    @org.junit.Test
    public void isContradiction() {
    }

    @org.junit.Test
    public void isTautology() {
    }

    @org.junit.Test
    public void isNegated() {
    }

    @org.junit.Test
    public void rawEquals() {
    }

    @org.junit.Test
    public void testEquals() {
    }
}