package tests.symbols;

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
        conn = Connective.newInstance(Symbol.IFF);
        assertEquals(conn.toString(), Symbol.IFF);

        conn = Connective.newInstance(Symbol.AND);
        assertEquals(conn.toString(), Symbol.AND);

        conn = Connective.newInstance(Symbol.OR);
        assertEquals(conn.toString(), Symbol.OR);

        conn = Connective.newInstance(Symbol.IMPLIES);
        assertEquals(conn.toString(), Symbol.IMPLIES);

        conn = Connective.newInstance("  -    >   ");
        assertEquals(conn.toString(), Symbol.IMPLIES);

        conn = Connective.newInstance("  <  -   >  ");
        assertEquals(conn.toString(), Symbol.IFF);

        conn = Connective.newInstance("  /    \\  ");
        assertEquals(conn.toString(), Symbol.AND);

        conn = Connective.newInstance("  \\      /  ");
        assertEquals(conn.toString(), Symbol.OR);
    }

    @Test
    void invalidFactory() {
        assertThrows(InvalidSymbolException.class, () -> Connective.newInstance("~"));
        assertThrows(InvalidSymbolException.class, () -> Connective.newInstance("T"));
        assertThrows(InvalidSymbolException.class, () -> Connective.newInstance("F"));
        assertThrows(InvalidSymbolException.class, () -> Connective.newInstance("-  > <->"));
        assertThrows(InvalidSymbolException.class, () -> Connective.newInstance("-->"));
        assertThrows(InvalidSymbolException.class, () -> Connective.newInstance("//\\"));
        assertThrows(InvalidSymbolException.class, () -> Connective.newInstance("\\///"));
    }
}