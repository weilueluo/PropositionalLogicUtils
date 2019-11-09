package tests.tree;

import core.exceptions.InvalidInsertionException;
import core.symbols.Connective;
import core.symbols.Literal;
import core.symbols.Negation;
import core.trees.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LitNodeTest {


    @Test
    void insert() {
        LitNode node = new LitNode(Literal.newInstance("P"));
        Node inserted_node = node.insert(new ConnNode(Connective.getIffInstance()));
        assertEquals(inserted_node.toString(), "P <-> ");

        LitNode node1 = new LitNode(Literal.newInstance("P"));
        // inserting bracket after literal
        assertThrows(InvalidInsertionException.class, () -> node1.insert(new BracketNode()));

        LitNode node2 = new LitNode(Literal.newInstance("P"));
        // inserting literal after literal
        assertThrows(InvalidInsertionException.class, () -> node2.insert(new LitNode(Literal.newInstance("Q"))));

        LitNode node3 = new LitNode(Literal.newInstance("P"));
        // inserting negation after literal
        assertThrows(InvalidInsertionException.class, () -> node3.insert(new NegNode(Negation.getInstance())));
    }

    @Test
    void isTautology() {
        LitNode true_node = new LitNode(Literal.getTautology());
        assertTrue(true_node.isTautology());

        LitNode false_node = new LitNode(Literal.getContradiction());
        assertFalse(false_node.isTautology());

        LitNode normal_node = new LitNode(Literal.newInstance("W"));
        assertFalse(normal_node.isTautology());
    }

    @Test
    void isContradiction() {
        LitNode true_node = new LitNode(Literal.getTautology());
        assertFalse(true_node.isContradiction());

        LitNode false_node = new LitNode(Literal.getContradiction());
        assertTrue(false_node.isContradiction());

        LitNode normal_node = new LitNode(Literal.newInstance("W"));
        assertFalse(normal_node.isContradiction());
    }

    @Test
    void copyAndEqualTest() {
        LitNode true_node = new LitNode(Literal.getTautology());
        Node copy = true_node.copy();
        assertEquals(copy, true_node);
        assertNotSame(copy, true_node);

        LitNode false_node = new LitNode(Literal.getContradiction());
        copy = false_node.copy();
        assertEquals(copy, false_node);
        assertNotSame(copy, false_node);

        LitNode normal_node = new LitNode(Literal.newInstance("S"));
        copy = normal_node.copy();
        assertEquals(copy, normal_node);
        assertNotSame(copy, normal_node);
    }
}