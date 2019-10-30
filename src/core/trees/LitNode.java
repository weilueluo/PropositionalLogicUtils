package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Literal;
import org.jetbrains.annotations.NotNull;

public class LitNode extends BinaryNode {

    private Literal literal;  // avoid casting

    public LitNode(Literal lit) {
        super(lit);
        literal = lit;
    }

    protected LitNode() {
        super();
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
    public Node insert(@NotNull ConnNode node) {
        node.left = this;
        return node;
    }

    @Override
    public Node insert(NegNode node) {
        throw new InvalidInsertionException("Inserting Negation immediately after Literal");
    }

    @Override
    public boolean isTrue() {
        return literal.getTruthValue();
    }
}
