package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Literal;
import core.symbols.Symbol;

import java.util.Set;

import static core.symbols.Symbol.LBRACKET;
import static core.symbols.Symbol.RBRACKET;

public class BracketNode extends Node {

    Node head;
    private boolean closed;

    public BracketNode() {
        super();
        head = null;
        closed = false;
        this.value = null;
    }

    @Override
    public Node toCNF() {
        return null;
    }

    @Override
    void addLiterals(Set<Literal> literals) {
        head.addLiterals(literals);
    }

    @Override
    int getPrecedence() {
        return Symbol.BRACKET_PRECEDENCE;
    }

    @Override
    Node _removeRedundantBrackets(int parent_precedence) {
        head = head._removeRedundantBrackets(getPrecedence());
        if (head.getPrecedence() <= parent_precedence) return head;
        else return this;

        // remove duplicate bracket
//        while (head instanceof BracketNode) {
//            head = ((BracketNode) head).head;
//        }

//        // if inner node contains bracket, negation and literal only then remove this bracket
//        // (~~(~a)) == ~~~a, (~a) == ~a etc...
//        Node look_ahead = head;
//        boolean remove_this = false;
//        while (true) {
//            if (look_ahead instanceof LitNode) {
//                remove_this = true;
//                break;
//            } else if (look_ahead instanceof NegNode) {
//                look_ahead = ((NegNode) look_ahead).descendant;
//            } else if (look_ahead instanceof BracketNode) {
//                look_ahead = ((BracketNode) look_ahead).head;
//            } else {
//                break;
//            }
//        }
    }

    static BracketNode bracket(Node node) {
        BracketNode bracket_node = new BracketNode();
        bracket_node.head = node;
        bracket_node.close();
        return bracket_node;
    }

    @Override
    public Node insert(LitNode node) {
        if (isClosed()) {
            throw new InvalidInsertionException("Inserting Literal immediately after Right bracket");
        } else {
            if (head == null) head = node;
            else head = head.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(BracketNode node) {
        if (isClosed()) {
            throw new InvalidInsertionException("Inserting Left bracket immediately after Right bracket");
        } else {
            if (head == null) head = node;
            else head = head.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(ConnNode node) {
        if (isClosed()) {
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
        if (isClosed()) {
            throw new InvalidInsertionException("Inserting Negation immediately after Right bracket");
        } else {
            if (head == null) head = node;
            else head = head.insert(node);
            return this;
        }
    }

    public void close() {
        if (isClosed()) throw new IllegalStateException("This bracket node is already closed");
        else closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public StringBuilder toTreeStringBuilder(int depth) {
        StringBuilder head_sb = head == null ? new StringBuilder() : head.toTreeStringBuilder(depth);
        String spaces = getSpaces(depth);
        return new StringBuilder(spaces)
                .append(LBRACKET).append(System.lineSeparator())
                .append(head_sb)
                .append(spaces)
                .append(RBRACKET).append(System.lineSeparator());
    }

    @Override
    public StringBuilder toBracketStringBuilder() {
        StringBuilder head_sb = head == null ? new StringBuilder() : head.toBracketStringBuilder();
        return new StringBuilder()
                .append(LBRACKET)
                .append(head_sb)
                .append(RBRACKET);
    }

    private void ensureComplete() {
        if (head == null) throw new IllegalStateException("Empty bracket: " + toString());
        else if (!isClosed()) throw new IllegalStateException("Unclosed bracket node: " + toString());
    }

    @Override
    public void _eliminateArrows() {
        ensureComplete();
        head._eliminateArrows();
    }

    @Override
    Node _invertNegation() {
        ensureComplete();
        head = head._invertNegation();
        return this;
    }

    @Override
    Node _pushNegations() {
        head = head._pushNegations();
        return this;
    }

    @Override
    Node copy() {
        ensureComplete();
        return BracketNode.bracket(head.copy());
    }

    @Override
    StringBuilder toStringBuilder() {
        StringBuilder head_sb = head == null ? new StringBuilder() : head.toStringBuilder();
        return new StringBuilder()
                .append(LBRACKET)
                .append(head_sb)
                .append(RBRACKET);
    }

    @Override
    public boolean isTrue() {
        ensureComplete();
        return head.isTrue();
    }
}
