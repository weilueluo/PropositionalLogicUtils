package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Connective;
import core.symbols.Negation;
import core.symbols.Symbol;

import static core.symbols.Symbol.*;

public class NegNode extends SingletonNode {

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
    public Node insert(BracketNode node) {
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
    public StringBuilder toBracketStringBuilder() {
        if (mid instanceof LitNode) {
            return new StringBuilder(value.getFull())
                    .append(mid.toBracketStringBuilder());
        } else {
            return new StringBuilder(value.getFull())
                .append(LBRACKET)
                .append(mid.toBracketStringBuilder())
                .append(RBRACKET);
        }
    }

    @Override
    protected void eliminateArrows() {
        mid.eliminateArrows();
    }

    @Override
    protected Node invertNegation() {
        return mid;  // just remove this negation node
    }

    @Override
    protected Node copy() {
        NegNode new_node = new NegNode(Negation.getInstance());
        if (mid != null) {
            new_node.mid = mid.copy();
        }
        return new_node;
    }

    @Override
    protected StringBuilder toStringBuilder() {
        return new StringBuilder().append(NEG).append(mid.toStringBuilder());
    }

    @Override
    public boolean isTrue() {
        return !mid.isTrue();
    }
}
