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
        literal = Literal.newInstance("~~~banana");
        assertTrue(literal.isNegated());

        // test negation with bracket
        literal = Literal.newInstance("~(~banana)");
        assertFalse(literal.isNegated());
        literal = Literal.newInstance("(~~(~banana))");
        assertTrue(literal.isNegated());
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
    void getUnprocessed() {
        // test true
        literal = Literal.newInstance("where");
        assertEquals(literal.getUnprocessed(), "where");
        literal = Literal.newInstance("~where");
        assertEquals(literal.getUnprocessed(), "~where");
        literal = Literal.newInstance("((((((where))))))");
        assertEquals(literal.getUnprocessed(), "((((((where))))))");
        literal = Literal.newInstance("~~~~~~((where))");
        assertEquals(literal.getUnprocessed(), "~~~~~~((where))");
        literal = Literal.newInstance("~(~(~~~(~(~(kind)))))");
        assertEquals(literal.getUnprocessed(), "~(~(~~~(~(~(kind)))))");
        literal = Literal.newInstance("(     (~~~~(~~~~Jokes))   )");
        assertEquals(literal.getUnprocessed(), "(     (~~~~(~~~~Jokes))   )");
        literal = Literal.newInstance("~~~~~~     ~united  ");
        assertEquals(literal.getUnprocessed(), "~~~~~~     ~united  ");
        literal = Literal.newInstance("~   ~~~(((~~~~a)))");
        assertEquals(literal.getUnprocessed(), "~   ~~~(((~~~~a)))");

        // test false
        literal = Literal.newInstance("~where");
        assertNotEquals(literal.getUnprocessed(), "~~where");
        literal = Literal.newInstance("which");
        assertNotEquals(literal.getUnprocessed(), "~~which");
        literal = Literal.newInstance("(((there)))");
        assertNotEquals(literal.getUnprocessed(), "((there))");
        literal = Literal.newInstance("~~~~~~((shut))");
        assertNotEquals(literal.getUnprocessed(), "~~~~~~(((shut)))");
        literal = Literal.newInstance("~(~(~~~(~(~(way)))))");
        assertNotEquals(literal.getUnprocessed(), "~(~(~~~(~(~way))))");
        literal = Literal.newInstance("(     (~~~~(~~~~jokes))   )");
        assertNotEquals(literal.getUnprocessed(), "(     (~~~~(~~~~Jokes))   )");
        literal = Literal.newInstance("~~~~~~     ~united  ");
        assertNotEquals(literal.getUnprocessed(), "~~~~~~     ~unite  ");
        literal = Literal.newInstance("~  ~~ ~~~(((~~~~a)))");
        assertNotEquals(literal.getUnprocessed(), "~ ~~  ~~~(((~~~~a)))");
    }

    @Test
    void testToString() {
        literal = Literal.newInstance("(~test)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (~test),%nFull literal: ~test,%nRaw literal: test,%nNegated: true,%n" +
                "Tautology: false,%nContradiction: false,%nAssigned raw value: null,%nTruth value: null"));

        literal = Literal.newInstance("(test)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (test),%nFull literal: test,%nRaw literal: test,%nNegated: false,%n" +
                "Tautology: false,%nContradiction: false,%nAssigned raw value: null,%nTruth value: null"));

        literal = Literal.newInstance("(T)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (T),%nFull literal: T,%nRaw literal: T,%nNegated: false,%n" +
                "Tautology: true,%nContradiction: false,%nAssigned raw value: true,%nTruth value: true"));

        literal = Literal.newInstance("(~T)");
        assertEquals(literal.toString(), String.format("Unprocessed String: (~T),%nFull literal: ~T,%nRaw literal: T,%nNegated: true,%n" +
                "Tautology: false,%nContradiction: true,%nAssigned raw value: true,%nTruth value: false"));
    }
}