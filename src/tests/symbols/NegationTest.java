package tests.symbols;

import core.exceptions.InvalidSymbolException;
import core.symbols.Negation;
import core.trees.NegNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NegationTest {

    @Test
    void getInstance() {
        assertNotNull(Negation.getInstance());
    }

    @Test
    void newInstanceNormalInput() {
        Negation neg = Negation.newInstance("~");
        neg = Negation.newInstance("~    ");
        neg = Negation.newInstance("     ~");
        neg = Negation.newInstance("        ~     ");
    }

    @Test
    void newInstanceInvalidInput() {
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("~~"));
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("~a"));
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("(((~)))"));
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("  ~   ~   "));
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("~~    "));
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("    ~~"));
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("~   ~"));
        assertThrows(InvalidSymbolException.class, () -> Negation.newInstance("(~a)"));
    }

    @Test
    void toNode() {
        assertNotNull(Negation.getInstance().toNode());
        assertTrue(Negation.getInstance().toNode() instanceof NegNode);
    }

    @Test
    void getRaw() {
        assertEquals(Negation.getInstance().getRaw(), "~");
        assertEquals(Negation.newInstance("~").getRaw(), "~");
        assertEquals(Negation.newInstance("~    ").getRaw(), "~");
        assertEquals(Negation.newInstance("     ~").getRaw(), "~");
        assertEquals(Negation.newInstance("  ~     ").getRaw(), "~");
    }

    @Test
    void getFull() {
        assertEquals(Negation.getInstance().getFull(), "~");
        assertEquals(Negation.newInstance("~").getFull(), "~");
        assertEquals(Negation.newInstance("~    ").getFull(), "~");
        assertEquals(Negation.newInstance("     ~").getFull(), "~");
        assertEquals(Negation.newInstance("  ~     ").getFull(), "~");
    }
}