package core.trees;

import core.exceptions.InvalidInsertionException;
import core.exceptions.InvalidNodeException;
import core.symbols.Literal;
import core.symbols.Negation;
import core.symbols.Symbol;

import java.util.List;
import java.util.Set;

public class NegNode extends SingletonNode {

    public NegNode(Symbol value) {
        super(value);
    }

    @Override
    public boolean isTautology() {
        return descendant.isContradiction();
    }

    @Override
    public boolean isContradiction() {
        return descendant.isTautology();
    }

    static NegNode negate(Node node) {
        NegNode neg_node = new NegNode(Negation.getInstance());
        neg_node.descendant = node;
        return neg_node;
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
    Node copy() {
        if (descendant == null) throw new InvalidNodeException("Copying a negation without descendant");
        return NegNode.negate(descendant.copy());
    }

    @Override
    Node _removeRedundantBrackets(int parent_precedence) {
        descendant = descendant._removeRedundantBrackets(getPrecedence());
        return this;
    }

    @Override
    Node _pushNegations() {
        descendant = descendant._pushNegations();
        descendant = descendant._invertNegation();
        return descendant;
    }

    @Override
    public void _eliminateArrows() {
        descendant._eliminateArrows();
    }

    @Override
    public StringBuilder toBracketStringBuilder() {
        if (descendant instanceof LitNode) {  // avoid too many brackets, so no bracket for literal
            return new StringBuilder(value.getFull())
                    .append(descendant.toBracketStringBuilder());
        } else {
            return new StringBuilder(value.getFull())
                    .append(Symbol.LBRACKET)
                    .append(descendant.toBracketStringBuilder())
                    .append(Symbol.RBRACKET);
        }
    }

    @Override
    Node _toCNF(List<Node> clauses) {
        clauses.clear();
        clauses.add(this);
        return this;
    }

    @Override
    void addLiterals(Set<Literal> literals) {
        descendant.addLiterals(literals);
    }

    @Override
    int getPrecedence() {
        return Symbol.NEGATION_PRECEDENCE;
    }

    @Override
    Node _invertNegation() {
        return descendant;  // just remove this negation node
    }

    @Override
    StringBuilder toStringBuilder() {
        return new StringBuilder().append(Symbol.NEG).append(descendant.toStringBuilder());
    }

    @Override
    public boolean isTrue() {
        return !descendant.isTrue();
    }

}
