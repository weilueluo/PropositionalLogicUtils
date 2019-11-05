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


public abstract class Node implements NodeInsertion, TruthValue {
    Symbol value;

    protected Node() {
        this(null);
    }

    protected Node(Symbol v) {
        value = v;
    }

    public static void main(String[] args) {
        var parser = new TruthTable();
        parser.evaluate("(a /\\ (~b -> a) <-> c \\/ b -> ~a /\\ (c <-> a)) \\/ ~b");
        //        parser.evaluate("(~((a)) \\/ (~~c)) /\\ ((((b \\/ (~~(~~(c -> a)))))))");
//                parser.evaluate("(e /\\ c \\/ a /\\ d)");
        System.out.println("Original: " + parser.getTree());
        System.out.println(parser.generate());
//        System.out.println(parser.generate());
//
//
//        parser.evaluate(parser.getTree()
//                .eliminateArrows()
//                .pushNegations()
//                .removeRedundantBrackets());
//
//        System.out.println("After");
//        System.out.println(parser.getTree());
//        System.out.println(parser.generate());

        var start = Instant.now();
        Pair<Node, List<Node>> node_and_clauses = parser.getTree().toCNF();
        var end = Instant.now();
        System.out.println("CNF: " + node_and_clauses.getItem1());
        parser.evaluate(node_and_clauses.getItem1().toString());
        System.out.println(parser.generate());
        System.out.println("Clauses:");
        for (Node node : node_and_clauses.getItem2()) {
            System.out.println(node);
        }
        System.out.println("Runtime: " + Duration.between(start, end).toMillis() + "ms");

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

    /**
     * @return a deep copy of this node, everything except Literal are new instances
     */
    abstract Node copy();  // this return a deep copy

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
     * @return A pair<Node: new tree in CNF form, List<Node>: list of clauses>
     */
    public Pair<Node, List<Node>> toCNF() {
        List<Node> clauses = new ArrayList<>();
        Node cnf_node = this.copy();

        cnf_node.eliminateArrows();
        cnf_node = cnf_node._pushNegations();
        cnf_node = cnf_node._removeRedundantBrackets(Symbol.INITIAL_PRECEDENCE);
        cnf_node = cnf_node._toCNF(clauses)._removeRedundantBrackets(Symbol.INITIAL_PRECEDENCE);
        return new Pair<>(cnf_node, clauses);
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

}
