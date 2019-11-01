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
        literal = Literal.newInstance("p");
        literal.assign(true);
        assertTrue(literal.getTruthValue());
        literal.assign(false);
        assertFalse(literal.getTruthValue());

        // test negated literal
        Literal other_literal = Literal.newInstance("~p");
        other_literal.assign(true);
        assertFalse(other_literal.getTruthValue());
        assertTrue(literal.getTruthValue());
        other_literal.assign(false);
        assertTrue(other_literal.getTruthValue());
        assertFalse(literal.getTruthValue());
    }

    @Test
    void truthValue() {
        literal = Literal.newInstance("literal");
        literal.assign(true);
        assertTrue(literal.getTruthValue());
        literal.assign(false);
        assertFalse(literal.getTruthValue());

        literal = Literal.newInstance("~literal");
        literal.assign(true);
        assertFalse(literal.getTruthValue());
        literal.assign(false);
        assertTrue(literal.getTruthValue());

        literal = Literal.newInstance("T");
        assertTrue(literal.getTruthValue());
        literal = Literal.newInstance("F");
        assertFalse(literal.getTruthValue());
    }

    @Test
    void isContradiction() {
        literal = Literal.newInstance("F");
        assertTrue(literal.isContradiction());
        literal = Literal.newInstance("~F");
        assertFalse(literal.isContradiction());
    }

    @Test
    void isTautology() {
        literal = Literal.newInstance("T");
        assertTrue(literal.isTautology());
        literal = Literal.newInstance("~T");
        assertFalse(literal.isTautology());
    }

    @Test
    void isNegated() {
        // test contradiction
        literal = Literal.newInstance("F");
        assertFalse(literal.isNegated());

        // test tautology
        literal = Literal.newInstance("T");
        assertFalse(literal.isNegated());

        // test negated contradiction
        literal = Literal.newInstance("~F");
        assertTrue(literal.isNegated());

        // test negated tautology
        literal = Literal.newInstance("~T");
        assertTrue(literal.isNegated());

        // test positive
        literal = Literal.newInstance("apple");
        assertFalse(literal.isNegated());

        // test negated
        literal = Literal.newInstance("~apple");
        assertTrue(literal.isNegated());

        // test more negation
        literal = Literal.newInstance("~~banana");
        assertFalse(literal.isNegated());
        assertTrue(literal.rawEquals(Literal.newInstance("banana"))); // check if negation is checked
        literal = Literal.newInstance("~~~banana");
        assertTrue(literal.rawEquals(Literal.newInstance("banana"))); // check if negation is checked
        assertTrue(literal.isNegated());

        // test negation with bracket
        literal = Literal.newInstance("~(~banana)");
        assertFalse(literal.isNegated());
        assertTrue(literal.rawEquals(Literal.newInstance("banana"))); // check if negation is checked
        literal = Literal.newInstance("(~~(~banana))");
        assertTrue(literal.rawEquals(Literal.newInstance("(banana)"))); // check if negation is checked
        assertTrue(literal.isNegated());
    }

    @Test
    void rawEquals() {
        // test equals
        literal = Literal.newInstance("chicken");
        assertTrue(literal.rawEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("~chicken");
        assertTrue(literal.rawEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("(((((((chicken)))))))");
        assertTrue(literal.rawEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("(~(chicken))");
        assertTrue(literal.rawEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("~~~(what)");
        assertTrue(literal.rawEquals(Literal.newInstance("((((what))))")));
        literal = Literal.newInstance("~~~~~~(~~~~(~~~(~~chicken)))");
        assertTrue(literal.rawEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("(~(~~~~((~~~magic))))");
        assertTrue(literal.rawEquals(Literal.newInstance("magic")));

        // test not equals
        literal = Literal.newInstance("chicken");
        assertFalse(literal.rawEquals(Literal.newInstance("chickens")));
        literal = Literal.newInstance("~chickenWings");
        assertFalse(literal.rawEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("(((((((moon)))))))");
        assertFalse(literal.rawEquals(Literal.newInstance("mom")));
        literal = Literal.newInstance("(~(chickens))");
        assertFalse(literal.rawEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("~~~(why)");
        assertFalse(literal.rawEquals(Literal.newInstance("((((whyWhy))))")));
        literal = Literal.newInstance("~~~~~~(~~~~(~~~(~~easy)))");
        assertFalse(literal.rawEquals(Literal.newInstance("easily")));
        literal = Literal.newInstance("(~(~~~~((~~~magic))))");
        assertFalse(literal.rawEquals(Literal.newInstance("magical")));
    }

    @Test
    void testEquals() {
        // test true
        literal = Literal.newInstance("chicken  ");
        assertTrue(literal.equals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("~chicken");
        assertTrue(literal.equals(Literal.newInstance("~chicken")));
        literal = Literal.newInstance("~~~~woof");
        assertTrue(literal.equals(Literal.newInstance("woof")));
        literal = Literal.newInstance("(((  (((((((mindless))))))  ))))           ");
        assertTrue(literal.equals(Literal.newInstance("   mindless   ")));
        literal = Literal.newInstance("~~( ~~(~~  (~~(~~zero)) ))");
        assertTrue(literal.equals(Literal.newInstance("zero")));
        literal = Literal.newInstance("~~ ~~        ~panda");
        assertTrue(literal.equals(Literal.newInstance("~panda")));
        literal = Literal.newInstance("~(((~~~(~~~(((monkey)))  ))))");
        assertTrue(literal.equals(Literal.newInstance("~monkey")));
        literal = Literal.newInstance("EndOfLife");
        assertTrue(literal.equals(Literal.newInstance("(~~~~EndOfLife)          ")));
        literal = Literal.newInstance("~(           ((~~~BorN)))");
        assertTrue(literal.equals(Literal.newInstance("BorN")));

        // test false
        literal = Literal.newInstance("chicken");
        assertFalse(literal.equals(Literal.newInstance("~chicken")));
        literal = Literal.newInstance("~chicken");
        assertFalse(literal.equals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("~~~woof");
        assertFalse(literal.equals(Literal.newInstance("woof")));
        literal = Literal.newInstance("(((  (((((((mindless))))))  ))))           ");
        assertFalse(literal.equals(Literal.newInstance(" ~  mindless   ")));
        literal = Literal.newInstance("~~( ~~(~~  (~~(~~zero)) ))");
        assertFalse(literal.equals(Literal.newInstance("~zero")));
        literal = Literal.newInstance("~~ ~~        ~~panda");
        assertFalse(literal.equals(Literal.newInstance("~panda")));
        literal = Literal.newInstance("~~(((~~~(~~~(((monkey)))  ))))");
        assertFalse(literal.equals(Literal.newInstance("~monkey")));
        literal = Literal.newInstance("~EndOfLife");
        assertFalse(literal.equals(Literal.newInstance("(~~~~EndOfLife)          ")));
        literal = Literal.newInstance("~(           ((~~BorN)))");
        assertFalse(literal.equals(Literal.newInstance("BorN")));
    }

    @Test
    void factory() {
        // test success
        literal = Literal.newInstance("Water");
        literal = Literal.newInstance("~MAYDAY");
        literal = Literal.newInstance("~~~((((awk))))");
        literal = Literal.newInstance("~~AppleOnTheGround");
        literal = Literal.newInstance("~~ ~~ ~~~~   MillionBananaKingdom");
        literal = Literal.newInstance("~             ~ CHOPSTICK                     ");
        literal = Literal.newInstance("((((immunity))))");
        literal = Literal.newInstance("~~~(~~(~~WHY))");
        literal = Literal.newInstance("( ~   (~~~ (((~(( ~~((((   (  FIELD  ))))))          ))))))    ");
        literal = Literal.newInstance("( ~(~~ ( ~ ~ ( ~ ~ STRANGE ) ) ) ) ");
        literal = Literal.newInstance("(NORM)");
        literal = Literal.newInstance("(~TENSE)");
        literal = Literal.newInstance("~~~~~~((SPACe))");

        // test fails
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("be ar"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("water#r"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~3MAYDAY"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~ wss ~AppleOnTheGround"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~~ ~~ ~x~~   MillionBananaKingdom"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~             ~ CHOPSTICK       x  "));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~hEY~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("(UNDO))"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("((DO)"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("(a)()"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~~()"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("(((((())))))"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("(~)"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~~()()~~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("(a ))  ))"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~~(s )~))   ~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("( (~x   )) )"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~~ x() ~~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.newInstance("~~( ) w~~"));
    }

    @Test
    void getRaw() {
        // equals test
        literal = Literal.newInstance("hey");
        assertEquals(literal.getRaw(), "hey");
        literal = Literal.newInstance("~hey");
        assertEquals(literal.getRaw(), "hey");
        literal = Literal.newInstance("~~~hey");
        assertEquals(literal.getRaw(), "hey");
        literal = Literal.newInstance("(((Ah)))");
        assertEquals(literal.getRaw(), "Ah");
        literal = Literal.newInstance("(man)");
        assertEquals(literal.getRaw(), "man");
        literal = Literal.newInstance("((((justInTime))))");
        assertEquals(literal.getRaw(), "justInTime");
        literal = Literal.newInstance("~~~~~~longStuff");
        assertEquals(literal.getRaw(), "longStuff");
        literal = Literal.newInstance("~((~~~~(~(~~complex))))");
        assertEquals(literal.getRaw(), "complex");

        // not equals test
        literal = Literal.newInstance("a");
        assertNotEquals(literal.getRaw(), "ab");
        literal = Literal.newInstance("~as");
        assertNotEquals(literal.getRaw(), "ass");
        literal = Literal.newInstance("~~~q");
        assertNotEquals(literal.getRaw(), "w");
        literal = Literal.newInstance("(((ops)))");
        assertNotEquals(literal.getRaw(), "os");
        literal = Literal.newInstance("(little)");
        assertNotEquals(literal.getRaw(), "lit");
        literal = Literal.newInstance("((((justInTime))))");
        assertNotEquals(literal.getRaw(), "justInTimes");
        literal = Literal.newInstance("~~~~~~longStuff");
        assertNotEquals(literal.getRaw(), "shortStuff");
        literal = Literal.newInstance("~((~~~~(~(~~complex))))");
        assertNotEquals(literal.getRaw(), "simple");
    }

    @Test
    void getFull() {
        // test equals
        literal = Literal.newInstance("Wa");
        assertEquals(literal.getFull(), "Wa");
        literal = Literal.newInstance("~wa");
        assertEquals(literal.getFull(), "~wa");
        literal = Literal.newInstance("(((r))  )   ");
        assertEquals(literal.getFull(), "r");
        literal = Literal.newInstance("~~~~~~a");
        assertEquals(literal.getFull(), "a");
        literal = Literal.newInstance("~~~~~(((a  )))");
        assertEquals(literal.getFull(), "~a");
        literal = Literal.newInstance("(((~~ ~a)))");
        assertEquals(literal.getFull(), "~a");
        literal = Literal.newInstance("(~(~(~a)))");
        assertEquals(literal.getFull(), "~a");
        literal = Literal.newInstance("~(~~(~~(a))  )");
        assertEquals(literal.getFull(), "~a");

        // test not equals
        literal = Literal.newInstance("wind");
        assertNotEquals(literal.getFull(), "~wind");
        literal = Literal.newInstance("~space");
        assertNotEquals(literal.getFull(), "space");
        literal = Literal.newInstance("(((regex))  )   ");
        assertNotEquals(literal.getFull(), "re");
        literal = Literal.newInstance("~~~~~~x");
        assertNotEquals(literal.getFull(), "v");
        literal = Literal.newInstance("~~~~~(((him  )))");
        assertNotEquals(literal.getFull(), "her");
        literal = Literal.newInstance("(((~~ ~a)))");
        assertNotEquals(literal.getFull(), "a");
        literal = Literal.newInstance("(~(~(~white)))");
        assertNotEquals(literal.getFull(), "~black");
        literal = Literal.newInstance("~(~~(~~(went))  )");
        assertNotEquals(literal.getFull(), "~go");
    }

    @Test
    void getTruthValue() {
        literal = Literal.newInstance("banana");
        literal.assign(true);
        assertTrue(literal.getTruthValue());

        literal = Literal.newInstance("~banana");
        literal.assign(true);
        assertFalse(literal.getTruthValue());

        literal = Literal.newInstance("banana");
        literal.assign(false);
        assertFalse(literal.getTruthValue());

        literal = Literal.newInstance("~banana");
        literal.assign(false);
        assertTrue(literal.getTruthValue());
    }

    @Test
    void isAssigned() {
        // test assigned
        literal = Literal.newInstance("fish");
        literal.assign(true);
        assertTrue(literal.isAssigned());

        literal = Literal.newInstance("~fish");
        literal.assign(true);
        assertTrue(literal.isAssigned());

        literal = Literal.newInstance("fish");
        literal.assign(false);
        assertTrue(literal.isAssigned());

        literal = Literal.newInstance("~fish");
        literal.assign(false);
        assertTrue(literal.isAssigned());

        // test not assigned
        literal = Literal.newInstance("sand");
        assertFalse(literal.isAssigned());

        literal = Literal.newInstance("~sand");
        assertFalse(literal.isAssigned());
    }

    @Test
    void fullEquals() {
        // test equals
        literal = Literal.newInstance("cv");
        assertTrue(literal.fullEquals(Literal.newInstance("cv")));
        literal = Literal.newInstance("~cv");
        assertTrue(literal.fullEquals(Literal.newInstance("~cv")));
        literal = Literal.newInstance("(((((((test)))))))");
        assertTrue(literal.fullEquals(Literal.newInstance("test")));
        literal = Literal.newInstance("(~(test))");
        assertTrue(literal.fullEquals(Literal.newInstance("~test")));
        literal = Literal.newInstance("~~~(what)");
        assertTrue(literal.fullEquals(Literal.newInstance("((((~what))))")));
        literal = Literal.newInstance("~~~~~~(~~~~(~~~(~~chicken)))");
        assertTrue(literal.fullEquals(Literal.newInstance("~chicken")));
        literal = Literal.newInstance("(~(~~~~((~~~magic))))");
        assertTrue(literal.fullEquals(Literal.newInstance("magic")));

        // test not equals
        literal = Literal.newInstance("chicken");
        assertFalse(literal.fullEquals(Literal.newInstance("~chickens")));
        literal = Literal.newInstance("~chickenWings");
        assertFalse(literal.fullEquals(Literal.newInstance("chicken")));
        literal = Literal.newInstance("(((((((moon)))))))");
        assertFalse(literal.fullEquals(Literal.newInstance("mom")));
        literal = Literal.newInstance("(~(chickens))");
        assertFalse(literal.fullEquals(Literal.newInstance("~chicken")));
        literal = Literal.newInstance("~~~(why)");
        assertFalse(literal.fullEquals(Literal.newInstance("((((whyWhy))))")));
        literal = Literal.newInstance("~~~~~~(~~~~(~~~(~~easy)))");
        assertFalse(literal.fullEquals(Literal.newInstance("easily")));
        literal = Literal.newInstance("(~(~~~~((~~~magic))))");
        assertFalse(literal.fullEquals(Literal.newInstance("magical")));
    }

    @Test
    void testToString() {
        literal = Literal.newInstance("(~test)");
        assertEquals(literal.toString(), String.format("Full literal: ~test,%nRaw literal: test,%nNegated: true,%n" +
                "Tautology: false,%nContradiction: false,%nAssigned raw value: null,%nTruth value: null"));

        literal = Literal.newInstance("(test)");
        assertEquals(literal.toString(), String.format("Full literal: test,%nRaw literal: test,%nNegated: false,%n" +
                "Tautology: false,%nContradiction: false,%nAssigned raw value: null,%nTruth value: null"));

        literal = Literal.newInstance("(T)");
        assertEquals(literal.toString(), String.format("Full literal: T,%nRaw literal: T,%nNegated: false,%n" +
                "Tautology: true,%nContradiction: false,%nAssigned raw value: true,%nTruth value: true"));

        literal = Literal.newInstance("(~T)");
        assertEquals(literal.toString(), String.format("Full literal: ~T,%nRaw literal: T,%nNegated: true,%n" +
                "Tautology: false,%nContradiction: true,%nAssigned raw value: true,%nTruth value: false"));
    }
}