package core.trees;

import core.TruthTable;
import core.common.Pair;
import core.symbols.Literal;
import core.symbols.Symbol;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static core.symbols.Connective.Type.AND;


public abstract class Node implements NodeInsertion, TruthValue {
    Symbol value;

    Node() {
        this(null);
    }

    Node(Symbol v) {
        value = v;
    }

    /**
     * @return a new tree after removing redundant brackets of this tree.
     */
    public Node removeRedundantBrackets() {
        Node copy = this.copy();
        copy = copy._removeRedundantBrackets(Symbol.INITIAL_PRECEDENCE);
        return copy;
    }

    /**
     * @return a new tree after pushing existing tree's negations into the literal
     */
    public Node pushNegations() {
        Node copy = this.copy();
        copy = copy._pushNegations();
        return copy;
    }

    /**
     * Replace all -> and <-> in this tree by Equivalent Replacement Rule
     * a <-> b === a -> b /\ b -> a
     * a -> b === ~a \/ b
     */
    public Node eliminateArrows() {
        this._eliminateArrows();
        return this;
    }

    public abstract boolean isTautology();

    public abstract boolean isContradiction();

    /**
     * @return a deep copy of this node, everything except Literal are new instances
     */
    public abstract Node copy();  // this return a deep copy

    // internal method which may alter the existing tree
    abstract Node _removeRedundantBrackets(int parent_precedent);

    // internal method which may alter the existing tree
    abstract Node _pushNegations();

    // internal method which does not return 'this' reference
    abstract void _eliminateArrows();

    /**
     * @param depth current depth of the tree
     * @return a string with depth * 3 spaces
     */
    String getSpaces(int depth) {
        return depth <= 0 ? "" : String.format("%" + depth * 3 + "s", " ");
    }

    /**
     * @return a tree like string of this node for debug
     */
    public String toTreeString() {
        return toTreeStringBuilder(0).toString();
    }

    // internal method which return a string builder instead of string for efficiency
    abstract StringBuilder toTreeStringBuilder(int depth);

    /**
     * @return a string of this node with explicit brackets
     */
    public String toBracketString() {
        return toBracketStringBuilder().toString();
    }

    // internal method which return a string builder instead of string for efficiency
    abstract StringBuilder toBracketStringBuilder();


    /**
     * @return A pair<Node: [new tree in CNF form], List<Node>: [list of clauses]>
     */
    public Pair<Node, List<Node>> toCNF() {
        List<Node> clauses = new ArrayList<>();
        Node cnf_node = this.copy();

        cnf_node._eliminateArrows();
        cnf_node = cnf_node._pushNegations();
        cnf_node = cnf_node._removeRedundantBrackets(Symbol.INITIAL_PRECEDENCE);
        cnf_node = cnf_node._toCNF(clauses);

        return new Pair<>(cnf_node, clauses);
    }

    // Internal method requires input node MUST be in cnf form
    private static Node __simplify_CNF_node(Node cnf_node) {
        // remove external brackets
        if (cnf_node instanceof BracketNode) {
            //  (...)
            return __simplify_CNF_node(((BracketNode) cnf_node).head);
        }

        ConnNode cnf_conn_node = (ConnNode) cnf_node;
        if (cnf_conn_node.type == AND) {
            // ... /\ ... : left and right are in cnf form
            cnf_conn_node.left = __simplify_CNF_node(cnf_conn_node.left);
            cnf_conn_node.right = __simplify_CNF_node(cnf_conn_node.right);
            if (cnf_conn_node.isContradiction()) {
                return new LitNode(Literal.getContradiction());
            } else if (cnf_conn_node.isTautology()) {
                return new LitNode(Literal.getTautology());
            }
            // TODO remove duplicate literal and check tautology and contradiction as different method?
            return cnf_conn_node;

        }
        // TODO
        return null;
    }

    // internal method
    // MUST only call after eliminateArrows and pushNegations and removeRedundantBrackets
    // if class cast exception is thrown
    // maybe logical error in removeArrows or pushNegations or removeRedundantBrackets
    abstract Node _toCNF(List<Node> clauses);

    /**
     * @return all unique literals of this tree
     */
    public Set<Literal> getLiterals() {
        Set<Literal> literals = new HashSet<>();
        addLiterals(literals);
        return literals;
    }

    abstract void addLiterals(Set<Literal> literals);

    abstract int getPrecedence();

    /**
     * @return a node after negations of this node is inverted
     */
    public Node invertNegation() {
        Node copy = this.copy();
        copy = copy._invertNegation();
        return copy;
    }

    // internal method which may alter the existing tree
    abstract Node _invertNegation();

    /**
     * @return the formula string of this node
     */
    public String toString() {
        return toStringBuilder().toString();
    }

    abstract StringBuilder toStringBuilder();

    @Override
    public abstract boolean equals(Object other);  // structure-ly equals

}
