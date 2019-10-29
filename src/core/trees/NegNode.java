package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Literal;
import core.symbols.Symbol;

import java.util.Map;

import static core.common.Utilities.printMap;

public class NegNode extends SingletonNode {

    public NegNode() {
        super();
    }

    public NegNode(Symbol value) {
        super(value);
    }

    @Override
    public Node insert(LitNode node) {
        if (mid == null) mid = node;
        else mid = mid.insert(node);
        return this;
    }

    @Override
    public Node insert(BoxNode node) {
        if (mid == null) mid = node;
        else mid = mid.insert(node);
        return this;
    }

    @Override
    public Node insert(ConnNode node) {
        if (mid == null) throw new InvalidInsertionException("Inserting Connective immediately after Negation");
        node.left = this;
        return node;
    }

    @Override
    public Node insert(NegNode node) {
        if (mid == null) mid = node;
        else mid = mid.insert(node);
        return this;
    }

    @Override
    public boolean isSatisfiable(Map<Literal, Boolean> interpretation, boolean truth_value) {
        System.out.println("At Negation");
        printMap(interpretation);
        System.out.println();
        if (mid == null) throw new IllegalStateException("Checking satisfiability of Negation without literal");
        else return mid.isSatisfiable(interpretation, !truth_value);
    }
}
