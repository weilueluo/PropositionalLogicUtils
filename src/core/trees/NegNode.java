package core.trees;

import core.exceptions.InvalidInsertionException;
import core.exceptions.InvalidNodeException;
import core.symbols.Negation;
import core.symbols.Symbol;

import static core.symbols.Symbol.*;

public class NegNode extends SingletonNode {

    public NegNode(Symbol value) {
        super(value);
    }

    @Override
    public Node insert(LitNode node) {
        if (descendant == null) descendant = node;
        else descendant = descendant.insert(node);
        return this;
    }

    @Override
    public Node insert(BracketNode node) {
        if (descendant == null) descendant = node;
        else descendant = descendant.insert(node);
        return this;
    }

    @Override
    public Node insert(ConnNode node) {
        if (descendant == null) throw new InvalidInsertionException("Inserting Connective immediately after Negation");
        node.left = this;
        return node;
    }

    @Override
    public Node insert(NegNode node) {
        if (descendant == null) descendant = node;
        else descendant = descendant.insert(node);
        return this;
    }

    @Override
    public StringBuilder toBracketStringBuilder() {
        if (descendant instanceof LitNode) {  // avoid too many brackets, so no bracket for literal
            return new StringBuilder(value.getFull())
                    .append(descendant.toBracketStringBuilder());
        } else {
            return new StringBuilder(value.getFull())
                .append(LBRACKET)
                .append(descendant.toBracketStringBuilder())
                .append(RBRACKET);
        }
    }

    @Override
    protected void eliminateArrows() {
        descendant.eliminateArrows();
    }

    @Override
    protected Node invertNegation() {
        return descendant;  // just remove this negation node
    }

    @Override
    protected Node copy() {
        if (descendant == null) throw new InvalidNodeException("Copying a negation without descendant");
        NegNode new_node = new NegNode(Negation.getInstance());
        new_node.descendant = descendant.copy();
        return new_node;
    }

    @Override
    protected StringBuilder toStringBuilder() {
        return new StringBuilder().append(NEG).append(descendant.toStringBuilder());
    }

    @Override
    public boolean isTrue() {
        return !descendant.isTrue();
    }
}
