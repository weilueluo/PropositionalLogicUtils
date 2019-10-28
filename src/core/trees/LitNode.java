package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Literal;

import java.util.Map;

public class LitNode extends BinaryNode {

    public LitNode(Literal lit) {
        super(lit);
    }

    @Override
    public Node insert(LitNode node) {
        throw new InvalidInsertionException("Inserting Literal immediately after Literal");
    }

    @Override
    public Node insert(BoxNode node) {
        throw new InvalidInsertionException("Inserting Left bracket immediately after Literal");
    }

    @Override
    public Node insert(ConnNode node) {
        node.left = this;
        return node;
    }

    @Override
    public Node insert(NegNode node) {
        throw new InvalidInsertionException("Inserting Negation immediately after Literal");
    }

    @Override
    public boolean isSatisfiable(Map<Literal, Boolean> interpretation, boolean truth_value) {
        Literal lit = (Literal) node_value;
        if (lit.isTautology()) {
            return truth_value;
        } else if (lit.isContradiction()) {
            return !truth_value;
        }

        if (interpretation.containsKey(node_value)) {
            return interpretation.get(node_value) == truth_value;
        } else {
            interpretation.put(lit, truth_value);
            return true;
        }
    }
}
