package tests;

import core.exceptions.InvalidSymbolException;
import core.symbols.Literal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LiteralTest {

    private Literal literal;

    @Test
    void assign() {
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
    void truthValue() {
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
    void isContradiction() {
        literal = Literal.factory("F");
        assertTrue(literal.isContradiction());
        literal = Literal.factory("~F");
        assertFalse(literal.isContradiction());
    }

    @Test
    void isTautology() {
        literal = Literal.factory("T");
        assertTrue(literal.isTautology());
        literal = Literal.factory("~T");
        assertFalse(literal.isTautology());
    }

    @Test
    void isNegated() {
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

        // test negation with bracket
        literal = Literal.factory("~(~banana)");
        assertFalse(literal.isNegated());
        assertTrue(literal.rawEquals(Literal.factory("banana"))); // check if negation is checked
        literal = Literal.factory("(~~(~banana))");
        assertTrue(literal.rawEquals(Literal.factory("(banana)"))); // check if negation is checked
        assertTrue(literal.isNegated());
    }

    @Test
    void rawEquals() {
        literal = Literal.factory("chicken");
        assertTrue(literal.rawEquals(Literal.factory("chicken")));
        literal = Literal.factory("~chicken");
        assertTrue(literal.rawEquals(Literal.factory("chicken")));

        literal = Literal.factory("(((((((chicken)))))))");
        assertTrue(literal.rawEquals(Literal.factory("chicken")));
        literal = Literal.factory("(~(chicken))");
        assertTrue(literal.rawEquals(Literal.factory("chicken")));
        literal = Literal.factory("~~~(what)");
        assertTrue(literal.rawEquals(Literal.factory("((((what))))")));
        literal = Literal.factory("~~~~~~(~~~~(~~~(~~chicken)))");
        assertTrue(literal.rawEquals(Literal.factory("chicken")));
        literal = Literal.factory("(~(~~~~((~~~magic))))");
        assertTrue(literal.rawEquals(Literal.factory("magic")));
    }

    @Test
    void testEquals() {
        literal = Literal.factory("chicken  ");
        assertTrue(literal.equals(Literal.factory("chicken")));
        literal = Literal.factory("~chicken");
        assertTrue(literal.equals(Literal.factory("~chicken")));
        literal = Literal.factory("~~~~woof");
        assertTrue(literal.equals(Literal.factory("woof")));
        literal = Literal.factory("(((  (((((((mindless))))))  ))))           ");
        assertTrue(literal.equals(Literal.factory("   mindless   ")));
        literal = Literal.factory("~~( ~~(~~  (~~(~~zero)) ))");
        assertTrue(literal.equals(Literal.factory("zero")));
        literal = Literal.factory("~~ ~~        ~panda");
        assertTrue(literal.equals(Literal.factory("~panda")));
        literal = Literal.factory("~(((~~~(~~~(((monkey)))  ))))");
        assertTrue(literal.equals(Literal.factory("~monkey")));
        literal = Literal.factory("EndOfLife");
        assertTrue(literal.equals(Literal.factory("(~~~~EndOfLife)          ")));
        literal = Literal.factory("~(           ((~~~BorN)))");
        assertTrue(literal.equals(Literal.factory("BorN")));
    }

    @Test
    void testFailEquals() {
        literal = Literal.factory("chicken");
        assertFalse(literal.equals(Literal.factory("~chicken")));
        literal = Literal.factory("~chicken");
        assertFalse(literal.equals(Literal.factory("chicken")));
        literal = Literal.factory("~~~woof");
        assertFalse(literal.equals(Literal.factory("woof")));
        literal = Literal.factory("(((  (((((((mindless))))))  ))))           ");
        assertFalse(literal.equals(Literal.factory(" ~  mindless   ")));
        literal = Literal.factory("~~( ~~(~~  (~~(~~zero)) ))");
        assertFalse(literal.equals(Literal.factory("~zero")));
        literal = Literal.factory("~~ ~~        ~~panda");
        assertFalse(literal.equals(Literal.factory("~panda")));
        literal = Literal.factory("~~(((~~~(~~~(((monkey)))  ))))");
        assertFalse(literal.equals(Literal.factory("~monkey")));
        literal = Literal.factory("~EndOfLife");
        assertFalse(literal.equals(Literal.factory("(~~~~EndOfLife)          ")));
        literal = Literal.factory("~(           ((~~BorN)))");
        assertFalse(literal.equals(Literal.factory("BorN")));
    }

    @Test
    void testFactoryValidLiteral() {
        literal = Literal.factory("Water");
        literal = Literal.factory("~MAYDAY");
        literal = Literal.factory("~~AppleOnTheGround");
        literal = Literal.factory("~~ ~~ ~~~~   MillionBananaKingdom");
        literal = Literal.factory("~             ~ CHOPSTICK                     ");
        literal = Literal.factory("((((immunity))))");
        literal = Literal.factory("~~~(~~(~~WHY))");
        literal = Literal.factory("( ~   (~~~ (((~(( ~~((((   (  FIELD  ))))))          ))))))    ");
        literal = Literal.factory("( ~(~~ ( ~ ~ ( ~ ~ STRANGE ) ) ) ) ");
        literal = Literal.factory("(NORM)");
        literal = Literal.factory("(~TENSE)");
        literal = Literal.factory("~~~~~~((SPACe))");
    }

    @Test
    void testFactoryInvalidLiteral() {
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("be ar"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("water#r"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~3MAYDAY"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~ wss ~AppleOnTheGround"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~~ ~~ ~x~~   MillionBananaKingdom"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~             ~ CHOPSTICK       x  "));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~hEY~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("(UNDO))"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("((DO)"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("(a)()"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~~()"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("(((((())))))"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("(~)"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~~()()~~"));
    }

    @Test
    void getRaw() {
        literal = Literal.factory("hey");
        assertEquals(literal.getRaw(), "hey");
        literal = Literal.factory("~hey");
        assertEquals(literal.getRaw(), "hey");
        literal = Literal.factory("~~~hey");
        assertEquals(literal.getRaw(), "hey");
        literal = Literal.factory("(((Ah)))");
        assertEquals(literal.getRaw(), "Ah");
        literal = Literal.factory("(man)");
        assertEquals(literal.getRaw(), "man");
        literal = Literal.factory("((((justInTime))))");
        assertEquals(literal.getRaw(), "justInTime");
        literal = Literal.factory("~~~~~~longStuff");
        assertEquals(literal.getRaw(), "longStuff");
        literal = Literal.factory("~((~~~~(~(~~complex))))");
        assertEquals(literal.getRaw(), "complex");
    }

    @Test
    void getFull() {
    }

    @Test
    void getUnprocessedLiteral() {
    }
}