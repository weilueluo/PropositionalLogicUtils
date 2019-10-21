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
        assertTrue(literal.getTruthValue());
        literal.assign(false);
        assertFalse(literal.getTruthValue());

        // test negated literal
        literal = Literal.factory("~p");
        literal.assign(true);
        assertFalse(literal.getTruthValue());
        literal.assign(false);
        assertTrue(literal.getTruthValue());
    }

    @Test
    void truthValue() {
        literal = Literal.factory("literal");
        literal.assign(true);
        assertTrue(literal.getTruthValue());
        literal.assign(false);
        assertFalse(literal.getTruthValue());

        literal = Literal.factory("~literal");
        literal.assign(true);
        assertFalse(literal.getTruthValue());
        literal.assign(false);
        assertTrue(literal.getTruthValue());

        literal = Literal.factory("T");
        assertTrue(literal.getTruthValue());
        literal = Literal.factory("F");
        assertFalse(literal.getTruthValue());
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
        // test equals
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

        // test not equals
        literal = Literal.factory("chicken");
        assertFalse(literal.rawEquals(Literal.factory("chickens")));
        literal = Literal.factory("~chickenWings");
        assertFalse(literal.rawEquals(Literal.factory("chicken")));
        literal = Literal.factory("(((((((moon)))))))");
        assertFalse(literal.rawEquals(Literal.factory("mom")));
        literal = Literal.factory("(~(chickens))");
        assertFalse(literal.rawEquals(Literal.factory("chicken")));
        literal = Literal.factory("~~~(why)");
        assertFalse(literal.rawEquals(Literal.factory("((((whyWhy))))")));
        literal = Literal.factory("~~~~~~(~~~~(~~~(~~easy)))");
        assertFalse(literal.rawEquals(Literal.factory("easily")));
        literal = Literal.factory("(~(~~~~((~~~magic))))");
        assertFalse(literal.rawEquals(Literal.factory("magical")));
    }

    @Test
    void testEquals() {
        // test true
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

        // test false
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
    void factory() {
        // test success
        literal = Literal.factory("Water");
        literal = Literal.factory("~MAYDAY");
        literal = Literal.factory("~~~((((awk))))");
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

        // test fails
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
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("(a ))  ))"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~~(s )~))   ~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("( (~x   )) )"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~~ x() ~~"));
        assertThrows(InvalidSymbolException.class, () -> Literal.factory("~~( ) w~~"));
    }

    @Test
    void getRaw() {
        // equals test
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

        // not equals test
        literal = Literal.factory("a");
        assertNotEquals(literal.getRaw(), "ab");
        literal = Literal.factory("~as");
        assertNotEquals(literal.getRaw(), "ass");
        literal = Literal.factory("~~~q");
        assertNotEquals(literal.getRaw(), "w");
        literal = Literal.factory("(((ops)))");
        assertNotEquals(literal.getRaw(), "os");
        literal = Literal.factory("(little)");
        assertNotEquals(literal.getRaw(), "lit");
        literal = Literal.factory("((((justInTime))))");
        assertNotEquals(literal.getRaw(), "justInTimes");
        literal = Literal.factory("~~~~~~longStuff");
        assertNotEquals(literal.getRaw(), "shortStuff");
        literal = Literal.factory("~((~~~~(~(~~complex))))");
        assertNotEquals(literal.getRaw(), "simple");
    }

    @Test
    void getFull() {
        // test equals
        literal = Literal.factory("Wa");
        assertEquals(literal.getFull(), "Wa");
        literal = Literal.factory("~wa");
        assertEquals(literal.getFull(), "~wa");
        literal = Literal.factory("(((r))  )   ");
        assertEquals(literal.getFull(), "r");
        literal = Literal.factory("~~~~~~a");
        assertEquals(literal.getFull(), "a");
        literal = Literal.factory("~~~~~(((a  )))");
        assertEquals(literal.getFull(), "~a");
        literal = Literal.factory("(((~~ ~a)))");
        assertEquals(literal.getFull(), "~a");
        literal = Literal.factory("(~(~(~a)))");
        assertEquals(literal.getFull(), "~a");
        literal = Literal.factory("~(~~(~~(a))  )");
        assertEquals(literal.getFull(), "~a");

        // test not equals
        literal = Literal.factory("wind");
        assertNotEquals(literal.getFull(), "~wind");
        literal = Literal.factory("~space");
        assertNotEquals(literal.getFull(), "space");
        literal = Literal.factory("(((regex))  )   ");
        assertNotEquals(literal.getFull(), "re");
        literal = Literal.factory("~~~~~~x");
        assertNotEquals(literal.getFull(), "v");
        literal = Literal.factory("~~~~~(((him  )))");
        assertNotEquals(literal.getFull(), "her");
        literal = Literal.factory("(((~~ ~a)))");
        assertNotEquals(literal.getFull(), "a");
        literal = Literal.factory("(~(~(~white)))");
        assertNotEquals(literal.getFull(), "~black");
        literal = Literal.factory("~(~~(~~(went))  )");
        assertNotEquals(literal.getFull(), "~go");
    }

    @Test
    void getUnprocessed() {
        // test true
        literal = Literal.factory("where");
        assertEquals(literal.getUnprocessed(), "where");
        literal = Literal.factory("~where");
        assertEquals(literal.getUnprocessed(), "~where");
        literal = Literal.factory("((((((where))))))");
        assertEquals(literal.getUnprocessed(), "((((((where))))))");
        literal = Literal.factory("~~~~~~((where))");
        assertEquals(literal.getUnprocessed(), "~~~~~~((where))");
        literal = Literal.factory("~(~(~~~(~(~(kind)))))");
        assertEquals(literal.getUnprocessed(), "~(~(~~~(~(~(kind)))))");
        literal = Literal.factory("(     (~~~~(~~~~Jokes))   )");
        assertEquals(literal.getUnprocessed(), "(     (~~~~(~~~~Jokes))   )");
        literal = Literal.factory("~~~~~~     ~united  ");
        assertEquals(literal.getUnprocessed(), "~~~~~~     ~united  ");
        literal = Literal.factory("~   ~~~(((~~~~a)))");
        assertEquals(literal.getUnprocessed(), "~   ~~~(((~~~~a)))");

        // test false
        literal = Literal.factory("~where");
        assertNotEquals(literal.getUnprocessed(), "~~where");
        literal = Literal.factory("which");
        assertNotEquals(literal.getUnprocessed(), "~~which");
        literal = Literal.factory("(((there)))");
        assertNotEquals(literal.getUnprocessed(), "((there))");
        literal = Literal.factory("~~~~~~((shut))");
        assertNotEquals(literal.getUnprocessed(), "~~~~~~(((shut)))");
        literal = Literal.factory("~(~(~~~(~(~(way)))))");
        assertNotEquals(literal.getUnprocessed(), "~(~(~~~(~(~way))))");
        literal = Literal.factory("(     (~~~~(~~~~jokes))   )");
        assertNotEquals(literal.getUnprocessed(), "(     (~~~~(~~~~Jokes))   )");
        literal = Literal.factory("~~~~~~     ~united  ");
        assertNotEquals(literal.getUnprocessed(), "~~~~~~     ~unite  ");
        literal = Literal.factory("~  ~~ ~~~(((~~~~a)))");
        assertNotEquals(literal.getUnprocessed(), "~ ~~  ~~~(((~~~~a)))");
    }

    @Test
    void getTruthValue() {
        literal = Literal.factory("banana");
        literal.assign(true);
        assertTrue(literal.getTruthValue());

        literal = Literal.factory("~banana");
        literal.assign(true);
        assertFalse(literal.getTruthValue());

        literal = Literal.factory("banana");
        literal.assign(false);
        assertFalse(literal.getTruthValue());

        literal = Literal.factory("~banana");
        literal.assign(false);
        assertTrue(literal.getTruthValue());
    }

    @Test
    void isAssigned() {
        // test assigned
        literal = Literal.factory("fish");
        literal.assign(true);
        assertTrue(literal.isAssigned());

        literal = Literal.factory("~fish");
        literal.assign(true);
        assertTrue(literal.isAssigned());

        literal = Literal.factory("fish");
        literal.assign(false);
        assertTrue(literal.isAssigned());

        literal = Literal.factory("~fish");
        literal.assign(false);
        assertTrue(literal.isAssigned());

        // test not assigned
        literal = Literal.factory("sand");
        assertFalse(literal.isAssigned());

        literal = Literal.factory("~sand");
        assertFalse(literal.isAssigned());
    }

    @Test
    void fullEquals() {
        // test equals
        literal = Literal.factory("cv");
        assertTrue(literal.fullEquals(Literal.factory("cv")));
        literal = Literal.factory("~cv");
        assertTrue(literal.fullEquals(Literal.factory("~cv")));
        literal = Literal.factory("(((((((test)))))))");
        assertTrue(literal.fullEquals(Literal.factory("test")));
        literal = Literal.factory("(~(test))");
        assertTrue(literal.fullEquals(Literal.factory("~test")));
        literal = Literal.factory("~~~(what)");
        assertTrue(literal.fullEquals(Literal.factory("((((~what))))")));
        literal = Literal.factory("~~~~~~(~~~~(~~~(~~chicken)))");
        assertTrue(literal.fullEquals(Literal.factory("~chicken")));
        literal = Literal.factory("(~(~~~~((~~~magic))))");
        assertTrue(literal.fullEquals(Literal.factory("magic")));

        // test not equals
        literal = Literal.factory("chicken");
        assertFalse(literal.fullEquals(Literal.factory("~chickens")));
        literal = Literal.factory("~chickenWings");
        assertFalse(literal.fullEquals(Literal.factory("chicken")));
        literal = Literal.factory("(((((((moon)))))))");
        assertFalse(literal.fullEquals(Literal.factory("mom")));
        literal = Literal.factory("(~(chickens))");
        assertFalse(literal.fullEquals(Literal.factory("~chicken")));
        literal = Literal.factory("~~~(why)");
        assertFalse(literal.fullEquals(Literal.factory("((((whyWhy))))")));
        literal = Literal.factory("~~~~~~(~~~~(~~~(~~easy)))");
        assertFalse(literal.fullEquals(Literal.factory("easily")));
        literal = Literal.factory("(~(~~~~((~~~magic))))");
        assertFalse(literal.fullEquals(Literal.factory("magical")));
    }

    @Test
    void testToString() {
        literal = Literal.factory("(~test)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (~test),%nFull literal: ~test,%nRaw literal: test,%nNegated: true,%n" +
                "Tautology: false,%nContradiction: false,%nAssigned raw value: null,%nTruth value: null"));

        literal = Literal.factory("(test)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (test),%nFull literal: test,%nRaw literal: test,%nNegated: false,%n" +
                "Tautology: false,%nContradiction: false,%nAssigned raw value: null,%nTruth value: null"));

        literal = Literal.factory("(T)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (T),%nFull literal: T,%nRaw literal: T,%nNegated: false,%n" +
                "Tautology: true,%nContradiction: false,%nAssigned raw value: true,%nTruth value: true"));

        literal = Literal.factory("(~T)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (~T),%nFull literal: ~T,%nRaw literal: T,%nNegated: true,%n" +
                "Tautology: false,%nContradiction: true,%nAssigned raw value: true,%nTruth value: false"));
    }
}