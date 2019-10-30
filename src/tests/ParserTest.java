package tests;

import core.Parser;
import core.exceptions.InvalidFormulaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void ParserValidInput() {
        Parser parser = new Parser();
        parser.evaluate("a");
        parser.evaluate("~a");
        parser.evaluate("a \\/ b");
        parser.evaluate("a /\\ b");
        parser.evaluate("a->b");
        parser.evaluate("a<->b");
        parser.evaluate("(a)");
        parser.evaluate("(((((a)))))");
        parser.evaluate("a /\\ b /\\ c");
        parser.evaluate("a /\\ b \\/ c");
        parser.evaluate("a /\\ b -> c");
        parser.evaluate("a /\\ b <-> c");
        parser.evaluate("a \\/ b /\\ c");
        parser.evaluate("a \\/ b \\/ c");
        parser.evaluate("a \\/ b -> c");
        parser.evaluate("a \\/ b <-> c");
        parser.evaluate("a -> b -> c");
        parser.evaluate("a -> b /\\ c");
        parser.evaluate("a -> b \\/ c");
        parser.evaluate("a -> b <-> c");
        parser.evaluate("a <-> b /\\ c");
        parser.evaluate("a <-> b \\/ c");
        parser.evaluate("a <-> b -> c");
        parser.evaluate("a <-> b <-> c");
        parser.evaluate("~(a)");
        parser.evaluate("~(((((a)))))");
        parser.evaluate("((((~a))))");
        parser.evaluate("          a               ");
        parser.evaluate(" ( (( (a)) ) )  ");
        parser.evaluate("~a /\\ ~b");
        parser.evaluate("~~~~~~~~~a");
        parser.evaluate("~~~(~~(~~(~~(a))) ) ");
        parser.evaluate("(a /\\ b) -> c");
        parser.evaluate("(a \\/ b) -> c");
        parser.evaluate("(a <-> b) -> c");
        parser.evaluate("(a -> b) -> c");
        parser.evaluate("(a -> b) -> (c -> d)");
        parser.evaluate("(a -> b) -> (c <-> d)");
        parser.evaluate("(a -> b) -> (c \\/ d)");
        parser.evaluate("(a -> b) -> (c /\\ d)");
        parser.evaluate("(a -> b) -> (((((c /\\ d)))))");
        parser.evaluate("((((a -> b)))) -> (((((c /\\ d)))))");
        parser.evaluate("(a -> (b) -> (c) -> d)");
        parser.evaluate("(a -> (b) -> (((c) -> d)))");
        parser.evaluate("a /\\ c /\\ d /\\ e /\\ f");
        parser.evaluate("a \\/ c \\/ d \\/ e \\/ f");
        parser.evaluate("a -> c -> d -> e -> f");
        parser.evaluate("a <-> c <-> d <-> e <-> f");
        parser.evaluate("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        parser.evaluate("((((((((xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx))))))))");
        parser.evaluate("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -> c");
        parser.evaluate("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx /\\ d");
        parser.evaluate("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx <-> d");
        parser.evaluate("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx \\/ d");
        parser.evaluate("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx \\/ kkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        parser.evaluate("a/\\b->c<->d\\/e");
        parser.evaluate("(a/\\b->c)<->d\\/e");
        parser.evaluate("a/\\b->(c<->d)\\/e");
        parser.evaluate("a/\\((b->(c<->d)))\\/e");
        parser.evaluate("a/\\((b->(c<->d)))\\/((((e))))");
    }


    @Test
    void ParserInvalidInput() {
        Parser parser = new Parser();
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("   "));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate(" a b "));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a b   "));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("   a b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("->"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("~"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("<->"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("()"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("("));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate(")"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("/\\"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("\\/"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("())"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("(()"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("(())"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("~~"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("~~~~~"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("~a b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a()"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("()a"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a (/\\ b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("(a /\\/ b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("(a /\\/\\ b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("(a <--> b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a <- b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a < b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a > b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a - b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a -(> b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a <-(> b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a ->(-> b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a -> c (-> b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a -> c (x -> b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a ~-> b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a ~ b ()-> b"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("a ~ ~ (b -> b)"));
        assertThrows(InvalidFormulaException.class, () -> parser.evaluate("~~()"));
    }
}