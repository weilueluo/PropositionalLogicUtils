package core.trees;

import core.exceptions.InvalidInsertionException;
import core.exceptions.InvalidNodeException;
import core.symbols.Literal;

public class LitNode extends BinaryNode {

    private Literal literal;  // avoid casting

    public LitNode(Literal lit) {
        super(lit);
        literal = lit;
    }

    @Override
    public Node insert(LitNode node) {
        throw new InvalidInsertionException("Inserting Literal immediately after Literal");
    }

    @Override
    public Node insert(BracketNode node) {
        throw new InvalidInsertionException("Inserting Left bracket immediately after Literal");
    }

    @Override
    public Node insert(ConnNode node) {
        if (node == null) throw new InvalidNodeException("Connective node given is null");
        node.left = this;
        return node;
    }

    @Override
    public Node insert(NegNode node) {
        throw new InvalidInsertionException("Inserting Negation immediately after Literal");
    }

    @Override
    public StringBuilder toBracketStringBuilder() {
        return new StringBuilder(value.getFull());
    }

    @Override
    protected void eliminateArrows() {
        // no arrow-like connective in literal
    }

    @Override
    protected Node invertNegation() {
        literal.invertNegation();
        return this;
    }

    @Override
    protected Node copy() {
        return this;  // literal with the same name always refer to the same literal
    }

    @Override
    protected StringBuilder toStringBuilder() {
        return new StringBuilder(literal.getFull());
    }

    @Override
    public boolean isTrue() {
        return literal.getTruthValue();
    }
}
