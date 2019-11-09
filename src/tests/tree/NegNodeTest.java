package tests.tree;

import core.exceptions.InvalidInsertionException;
import core.symbols.Connective;
import core.symbols.Literal;
import core.symbols.Negation;
import core.trees.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NegNodeTest {
    private LitNode true_node = new LitNode(Literal.getTautology());
    private LitNode false_node = new LitNode(Literal.getContradiction());
    private Node node;

    @Test
    void isTautology() {
        node = NegNode.negate(true_node);
        assertFalse(node.isTautology());

        node = NegNode.negate(false_node);
        assertTrue(node.isTautology());
    }

    @Test
    void isContradiction() {
        node = NegNode.negate(true_node);
        assertTrue(node.isContradiction());

        node = NegNode.negate(false_node);
        assertFalse(node.isContradiction());
    }

    @Test
    void negate() {
        NegNode neg_node = NegNode.negate(new LitNode(Literal.getTautology()));
        assertEquals(neg_node.toString(), "~T");
        assertTrue(neg_node.isContradiction());

        neg_node = NegNode.negate(new LitNode(Literal.getContradiction()));
        assertEquals(neg_node.toString(), "~F");
        assertTrue(neg_node.isTautology());

        neg_node = NegNode.negate(new LitNode(Literal.newInstance("O")));
        assertEquals(neg_node.toString(), "~O");
    }

    @Test
    void insert() {
        node = new NegNode(Negation.getInstance());
        Node inserted_node = node.insert(new LitNode(Literal.newInstance("K")));
        assertEquals(inserted_node.toString(), "~K");

        node = new NegNode(Negation.getInstance());
        // inserting connective after negation
        assertThrows(InvalidInsertionException.class, () -> node.insert(new ConnNode(Connective.getAndInstance())));

        Node node1 = new NegNode(Negation.getInstance());
        inserted_node = node1.insert(new NegNode(Negation.getInstance()));
        assertEquals(inserted_node.toString(), "~~");

        node1 = new NegNode(Negation.getInstance());
        inserted_node = node1.insert(new BracketNode());
        assertEquals(inserted_node.toString(), "~(");
    }

    @Test
    void copy() {
        node = new NegNode(Negation.getInstance());
        Node copy = node.copy();
        assertEquals(copy, node);
        assertNotSame(copy, node);

        node = NegNode.negate(new LitNode(Literal.getTautology()));
        copy = node.copy();
        assertEquals(copy, node);
        assertNotSame(copy, node);
    }
}