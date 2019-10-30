package core.trees;

import core.exceptions.InvalidInsertionException;

public class BoxNode extends BinaryNode {

    private Node head;
    private boolean closed = false;

    public BoxNode() {
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
    public Node insert(BoxNode node) {
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
    public String toString(int depth) {
        String spaces = getSpaces(depth);
        return String.format(spaces + "(%n"
                            + head.toString(depth)
                            + spaces + ")%n");
    }

    @Override
    public boolean isTrue() {
        if (head == null) {
            throw new IllegalStateException("Accessing truth value of empty bracket");
        }
        return head.isTrue();
    }
}
