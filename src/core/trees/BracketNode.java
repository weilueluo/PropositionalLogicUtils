package core.trees;

import core.exceptions.InvalidInsertionException;

import static core.symbols.Symbol.LBRACKET;
import static core.symbols.Symbol.RBRACKET;

public class BracketNode extends BinaryNode {

    Node head;
    private boolean closed;

    public BracketNode() {
        super();
        head = null;
        closed = false;
        this.value = null;
    }

    @Override
    public Node insert(LitNode node) {
        if (closed) {
            throw new InvalidInsertionException("Inserting Literal immediately after Right bracket");
        } else {
            if (head == null) head = node;
            else head = head.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(BracketNode node) {
        if (closed) {
            throw new InvalidInsertionException("Inserting Left bracket immediately after Right bracket");
        } else {
            if (head == null) head = node;
            else head = head.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(ConnNode node) {
        if (closed) {
            node.left = this;
            return node;
        } else {
            if (head == null) head = node;
            else head = head.insert(node);
            return this;
        }
    }

    @Override
    public Node insert(NegNode node) {
        if (closed) {
            throw new InvalidInsertionException("Inserting Negation immediately after Right bracket");
        } else {
            if (head == null) head = node;
            else head = head.insert(node);
            return this;
        }
    }

    public void close() {
        if (closed) throw new IllegalStateException("This Box node is already closed");
        else closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public StringBuilder toTreeStringBuilder(int depth) {
        String spaces = getSpaces(depth);
        return new StringBuilder(spaces)
                .append(LBRACKET).append(System.lineSeparator())
                .append(head.toTreeStringBuilder(depth))
                .append(spaces)
                .append(RBRACKET).append(System.lineSeparator());
    }

    @Override
    public StringBuilder toBracketStringBuilder() {
        StringBuilder left_sb = left == null ? new StringBuilder() : left.toBracketStringBuilder();
        StringBuilder right_sb = right == null ? new StringBuilder() : right.toBracketStringBuilder();
        return new StringBuilder()
                .append(LBRACKET)
                .append(left_sb)
                .append(head.toBracketString())
                .append(right_sb)
                .append(RBRACKET);
    }

    @Override
    protected void eliminateArrows() {
        head.eliminateArrows();
    }

    @Override
    protected Node invertNegation() {
        head = head.invertNegation();
        return this;
    }

    @Override
    protected Node copy() {
        return head.copy();
    }

    @Override
    protected StringBuilder toStringBuilder() {
        return new StringBuilder()
                .append(LBRACKET)
                .append(head.toStringBuilder())
                .append(RBRACKET);
    }

    @Override
    public boolean isTrue() {
        if (head == null) {
            throw new IllegalStateException("Accessing truth value of empty bracket");
        }
        return head.isTrue();
    }
}