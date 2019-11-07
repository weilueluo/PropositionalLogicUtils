package tests.tree;

import core.symbols.Connective;
import core.symbols.Literal;
import core.symbols.Negation;
import core.trees.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BracketNodeTest {

    private Node node;

    @Test
    void isTautology() {
        node = new BracketNode().insert(new LitNode(Literal.getTautology()));
        assertTrue(node.isTautology());

        node = new BracketNode().insert(new LitNode(Literal.getContradiction()));
        assertFalse(node.isTautology());

        node = new BracketNode().insert(new LitNode(Literal.newInstance("P")));
        assertFalse(node.isTautology());
    }

    @Test
    void isContradiction() {
        node = new BracketNode().insert(new LitNode(Literal.getContradiction()));
        assertTrue(node.isContradiction());

        node = new BracketNode().insert(new LitNode(Literal.getTautology()));
        assertFalse(node.isContradiction());

        node = new BracketNode().insert(new LitNode(Literal.newInstance("P")));
        assertFalse(node.isContradiction());
    }

    @Test
    void bracketAndGetHead() {
        LitNode litnode = new LitNode(Literal.getTautology());
        BracketNode node = BracketNode.bracket(litnode);
        assertEquals(litnode, node.getHead());

        ConnNode conn_node = new ConnNode(Connective.getAndInstance());
        node = BracketNode.bracket(conn_node);
        assertEquals(conn_node, node.getHead());

        NegNode neg_node = new NegNode(Negation.getInstance());
        node = BracketNode.bracket(neg_node);
        assertEquals(neg_node, node.getHead());
    }

    @Test
    void insert() {
        BracketNode node = new BracketNode();
        LitNode litnode = new LitNode(Literal.getTautology());
        assertEquals(litnode, ((BracketNode) node.insert(litnode)).getHead());

        node = new BracketNode();
        ConnNode conn_node = new ConnNode(Connective.getAndInstance());
        assertEquals(conn_node, ((BracketNode) node.insert(conn_node)).getHead());

        node = new BracketNode();
        NegNode neg_node = new NegNode(Negation.getInstance());
        assertEquals(neg_node, ((BracketNode) node.insert(neg_node)).getHead());
    }

    @Test
    void closeValid() {
        BracketNode node = new BracketNode();
        Node inserted = node.insert(new LitNode(Literal.getTautology()));
        ((BracketNode) inserted).close();
        assertTrue(((BracketNode) inserted).isClosed());
    }

    @Test
    void closeInvalid() {
        BracketNode node = new BracketNode();
        assertThrows(IllegalStateException.class, node::close);  // close empty bracket
        node.insert(new ConnNode(Connective.getOrInstance()));
        node.close();
        assertThrows(IllegalStateException.class, node::close);  // close closed bracket
    }

    @Test
    void toTreeStringBuilder() {
    }

    @Test
    void toBracketStringBuilder() {
    }

    @Test
    void copy() {
        BracketNode node = BracketNode.bracket(new LitNode(Literal.getContradiction()));
        Node copy = node.copy();
        assertEquals(node, copy);
        assertNotSame(node, copy);

        node = BracketNode.bracket(new ConnNode(Connective.getAndInstance()));
        copy = node.copy();
        assertEquals(node, copy);
        assertNotSame(node, copy);

        node = BracketNode.bracket(new NegNode(Negation.getInstance()));
        copy = node.copy();
        assertEquals(node, copy);
        assertNotSame(node, copy);
    }

    @Test
    void toStringBuilder() {
    }

    @Test
    void isTrue() {
        BracketNode node = BracketNode.bracket(new LitNode(Literal.getTautology()));
        assertTrue(node.isTrue());

        node = BracketNode.bracket(new LitNode(Literal.getContradiction()));
        assertFalse(node.isTrue());
    }
}