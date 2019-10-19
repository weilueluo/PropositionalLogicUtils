package tests;

import core.exceptions.InvalidSymbolException;
import core.symbols.Connective;
import core.symbols.Symbol;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectiveTest {

    private Connective conn;

    @Test
    public void normalFactory() {
        conn = Connective.factory(Symbol.IFF);
        assertEquals(conn.toString(), Symbol.IFF);

        conn = Connective.factory(Symbol.AND);
        assertEquals(conn.toString(), Symbol.AND);

        conn = Connective.factory(Symbol.OR);
        assertEquals(conn.toString(), Symbol.OR);

        conn = Connective.factory(Symbol.IMPLIES);
        assertEquals(conn.toString(), Symbol.IMPLIES);

        conn = Connective.factory("  -    >   ");
        assertEquals(conn.toString(), Symbol.IMPLIES);

        conn = Connective.factory("  <  -   >  ");
        assertEquals(conn.toString(), Symbol.IFF);

        conn = Connective.factory("  /    \\  ");
        assertEquals(conn.toString(), Symbol.AND);

        conn = Connective.factory("  \\      /  ");
        assertEquals(conn.toString(), Symbol.OR);
    }

    @Test(expected = InvalidSymbolException.class)
    public void invalidFactory1() {
        conn = Connective.factory(Symbol.NEG);
    }

    @Test(expected = InvalidSymbolException.class)
    public void invalidFactory2() {
        conn = Connective.factory(Symbol.TAUTOLOGY);
    }

    @Test(expected = InvalidSymbolException.class)
    public void invalidFactory3() {
        conn = Connective.factory(Symbol.CONTRADICTION);
    }

    @Test(expected = InvalidSymbolException.class)
    public void invalidFactory4() {
        conn = Connective.factory("-  > <->");
    }

    @Test(expected = InvalidSymbolException.class)
    public void invalidFactory5() {
        conn = Connective.factory("-->");
    }

    @Test(expected = InvalidSymbolException.class)
    public void invalidFactory6() {
        conn = Connective.factory("//\\");
    }

    @Test(expected = InvalidSymbolException.class)
    public void invalidFactory7() {
        conn = Connective.factory("\\///");
    }
}