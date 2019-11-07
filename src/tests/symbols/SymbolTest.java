package tests.symbols;

import core.symbols.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTest {
    @Test
    void testConnective() {
        assertEquals(Symbol.IMPLIES, "->");
        assertEquals(Symbol.IFF, "<->");
        assertEquals(Symbol.OR, "\\/");
        assertEquals(Symbol.AND, "/\\");
    }

    @Test
    void testCharacters() {
        assertEquals(Symbol.TAUTOLOGY, 'T');
        assertEquals(Symbol.CONTRADICTION, 'F');
        assertEquals(Symbol.NEG, '~');
        assertEquals(Symbol.LBRACKET, '(');
        assertEquals(Symbol.RBRACKET, ')');
        assertEquals(Symbol.DASH, '-');
        assertEquals(Symbol.LESS_THAN, '<');
        assertEquals(Symbol.GREATER_THAN, '>');
        assertEquals(Symbol.BACKWARD_SLASH, '\\');
        assertEquals(Symbol.FORWARD_SLASH, '/');
    }
}