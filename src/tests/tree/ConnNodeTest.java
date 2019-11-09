package tests.tree;

import core.symbols.Connective;
import core.symbols.Literal;
import core.trees.ConnNode;
import core.trees.LitNode;
import core.trees.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnNodeTest {
    private LitNode true_node = new LitNode(Literal.getTautology());
    private LitNode false_node = new LitNode(Literal.getContradiction());
    private Node node;

    @Test
    void connect() {
        node = ConnNode.connect(Connective.Type.AND, true_node, false_node);
        assertEquals(node.toString(), "T /\\ F");

        node = ConnNode.connect(Connective.Type.OR, true_node, false_node);
        assertEquals(node.toString(), "T \\/ F");

        node = ConnNode.connect(Connective.Type.IFF, true_node, false_node);
        assertEquals(node.toString(), "T <-> F");

        node = ConnNode.connect(Connective.Type.IMPLIES, true_node, false_node);
        assertEquals(node.toString(), "T -> F");
    }

    @Test
    void insert() {
        node = true_node.insert(new ConnNode(Connective.getAndInstance()));
        node = node.insert(false_node);
        assertEquals(node.toString(), "T /\\ F");

        node = true_node.insert(new ConnNode(Connective.getOrInstance()));
        node = node.insert(false_node);
        assertEquals(node.toString(), "T \\/ F");

        node = true_node.insert(new ConnNode(Connective.getIffInstance()));
        node = node.insert(false_node);
        assertEquals(node.toString(), "T <-> F");

        node = true_node.insert(new ConnNode(Connective.getImpliesInstance()));
        node = node.insert(false_node);
        assertEquals(node.toString(), "T -> F");
    }

    @Test
    void isTrue() {
        // true false
        node = ConnNode.connect(Connective.Type.AND, true_node, false_node);
        assertFalse(node.isTrue());

        node = ConnNode.connect(Connective.Type.OR, true_node, false_node);
        assertTrue(node.isTrue());

        node = ConnNode.connect(Connective.Type.IMPLIES, true_node, false_node);
        assertFalse(node.isTrue());

        node = ConnNode.connect(Connective.Type.IFF, true_node, false_node);
        assertFalse(node.isTrue());

        // true true
        node = ConnNode.connect(Connective.Type.AND, true_node, true_node);
        assertTrue(node.isTrue());

        node = ConnNode.connect(Connective.Type.OR, true_node, true_node);
        assertTrue(node.isTrue());

        node = ConnNode.connect(Connective.Type.IMPLIES, true_node, true_node);
        assertTrue(node.isTrue());

        node = ConnNode.connect(Connective.Type.IFF, true_node, true_node);
        assertTrue(node.isTrue());


        // false false
        node = ConnNode.connect(Connective.Type.AND, false_node, false_node);
        assertFalse(node.isTrue());

        node = ConnNode.connect(Connective.Type.OR, false_node, false_node);
        assertFalse(node.isTrue());

        node = ConnNode.connect(Connective.Type.IMPLIES, false_node, false_node);
        assertTrue(node.isTrue());

        node = ConnNode.connect(Connective.Type.IFF, false_node, false_node);
        assertTrue(node.isTrue());
    }

    @Test
    void isTautology() {
        // true false
        node = ConnNode.connect(Connective.Type.AND, true_node, false_node);
        assertFalse(node.isTautology());

        node = ConnNode.connect(Connective.Type.OR, true_node, false_node);
        assertTrue(node.isTautology());

        node = ConnNode.connect(Connective.Type.IMPLIES, true_node, false_node);
        assertFalse(node.isTautology());

        node = ConnNode.connect(Connective.Type.IFF, true_node, false_node);
        assertFalse(node.isTautology());

        // true true
        node = ConnNode.connect(Connective.Type.AND, true_node, true_node);
        assertTrue(node.isTautology());

        node = ConnNode.connect(Connective.Type.OR, true_node, true_node);
        assertTrue(node.isTautology());

        node = ConnNode.connect(Connective.Type.IMPLIES, true_node, true_node);
        assertTrue(node.isTautology());

        node = ConnNode.connect(Connective.Type.IFF, true_node, true_node);
        assertTrue(node.isTautology());


        // false false
        node = ConnNode.connect(Connective.Type.AND, false_node, false_node);
        assertFalse(node.isTautology());

        node = ConnNode.connect(Connective.Type.OR, false_node, false_node);
        assertFalse(node.isTautology());

        node = ConnNode.connect(Connective.Type.IMPLIES, false_node, false_node);
        assertTrue(node.isTautology());

        node = ConnNode.connect(Connective.Type.IFF, false_node, false_node);
        assertTrue(node.isTautology());

        // normal node
        Node normal_node1 = new LitNode(Literal.newInstance("P"));
        Node normal_node2 = new LitNode(Literal.newInstance("Q"));

        node = ConnNode.connect(Connective.Type.AND, normal_node1, normal_node2);
        assertFalse(node.isTautology());

        node = ConnNode.connect(Connective.Type.OR, normal_node1, normal_node2);
        assertFalse(node.isTautology());

        node = ConnNode.connect(Connective.Type.IMPLIES, normal_node1, normal_node2);
        assertFalse(node.isTautology());

        node = ConnNode.connect(Connective.Type.IFF, normal_node1, normal_node2);
        assertFalse(node.isTautology());
    }

    @Test
    void isContradiction() {
        // true false
        node = ConnNode.connect(Connective.Type.AND, true_node, false_node);
        assertTrue(node.isContradiction());

        node = ConnNode.connect(Connective.Type.OR, true_node, false_node);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IMPLIES, true_node, false_node);
        assertTrue(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IFF, true_node, false_node);
        assertTrue(node.isContradiction());

        // true true
        node = ConnNode.connect(Connective.Type.AND, true_node, true_node);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.OR, true_node, true_node);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IMPLIES, true_node, true_node);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IFF, true_node, true_node);
        assertFalse(node.isContradiction());


        // false false
        node = ConnNode.connect(Connective.Type.AND, false_node, false_node);
        assertTrue(node.isContradiction());

        node = ConnNode.connect(Connective.Type.OR, false_node, false_node);
        assertTrue(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IMPLIES, false_node, false_node);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IFF, false_node, false_node);
        assertFalse(node.isContradiction());

        // normal node
        Node normal_node1 = new LitNode(Literal.newInstance("P"));
        Node normal_node2 = new LitNode(Literal.newInstance("Q"));

        node = ConnNode.connect(Connective.Type.AND, normal_node1, normal_node2);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.OR, normal_node1, normal_node2);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IMPLIES, normal_node1, normal_node2);
        assertFalse(node.isContradiction());

        node = ConnNode.connect(Connective.Type.IFF, normal_node1, normal_node2);
        assertFalse(node.isContradiction());
    }

    @Test
    void copy() {
        Node node = new ConnNode(Connective.getIffInstance());
        Node copy = node.copy();
        assertEquals(copy, node);
        assertNotSame(copy, node);

        copy = true_node.copy();
        assertEquals(copy, true_node);
        assertNotSame(copy, true_node);  // for unknown reason they maybe actually the same instance?

        copy = false_node.copy();
        assertEquals(copy, false_node);
        assertNotSame(copy, false_node);  // for unknown reason they maybe actually the same instance?
    }

    @Test
    void toStringBuilder() {
    }

    @Test
    void testEquals() {
        ConnNode node1 = ConnNode.connect(Connective.Type.IMPLIES, true_node, false_node);
        ConnNode node2 = ConnNode.connect(Connective.Type.IMPLIES, true_node.copy(), false_node.copy());
        assertEquals(node1, node2);

        ConnNode node3 = ConnNode.connect(Connective.Type.IMPLIES, false_node, true_node);
        assertNotEquals(node1, node3);
        assertNotEquals(node2, node3);
    }
}