package tests;

import core.exceptions.InvalidSymbolException;
import core.symbols.Connective;
import core.symbols.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class ConnectiveTest {

    private Connective conn;

    @Test
    void normalFactory() {
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

    @Test
    void invalidFactory() {
        assertThrows(InvalidSymbolException.class, () -> Connective.factory(Symbol.NEG));
        assertThrows(InvalidSymbolException.class, () -> Connective.factory(Symbol.TAUTOLOGY));
        assertThrows(InvalidSymbolException.class, () -> Connective.factory(Symbol.CONTRADICTION));
        assertThrows(InvalidSymbolException.class, () -> Connective.factory("-  > <->"));
        assertThrows(InvalidSymbolException.class, () -> Connective.factory("-->"));
        assertThrows(InvalidSymbolException.class, () -> Connective.factory("//\\"));
        assertThrows(InvalidSymbolException.class, () -> Connective.factory("\\///"));
    }
}