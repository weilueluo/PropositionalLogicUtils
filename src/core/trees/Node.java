package core.trees;

import core.TruthTable;
import core.symbols.Literal;
import core.symbols.Symbol;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


public abstract class Node implements NodeInsertion, TruthValue {
    Symbol value;

    protected Node() {
        this(null);
    }

    protected Node(Symbol v) {
        value = v;
    }

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

    /**
     * @return a string of this node with explicit brackets
     */
    public String toBracketString() {
        return toBracketStringBuilder().toString();
    }

    public abstract Node toCNF();

    /**
     * @return all unique literals of this tree
     */
    public Literal[] getLiterals() {
        Set<Literal> literals = new HashSet<>();
        addLiterals(literals);
        return literals.toArray(Literal[]::new);
    }

    abstract void addLiterals(Set<Literal> literals);

    /**
     * // TODO
     * @return a node after removing duplicate bracket and bracket for literal, not removing precedence bracket yet.
     */
    public Node removeRedundantBrackets() {
        Node copy = this.copy();
        copy = copy._removeRedundantBrackets();
        return copy;
    }

    // internal method which may alter the existing tree
    abstract Node _removeRedundantBrackets();

    // internal method which return a string builder instead of string for efficiency
    abstract StringBuilder toTreeStringBuilder(int depth);

    // internal method which return a string builder instead of string for efficiency
    abstract StringBuilder toBracketStringBuilder();

    /**
     * Replace all -> and <-> in this tree by Equivalent Replacement Rule
     * a <-> b === a -> b /\ b -> a
     * a -> b === ~a \/ b
     */
    public Node eliminateArrows() {
        this._eliminateArrows();
        return this;
    }

    // internal method which does not return 'this' reference
    abstract void _eliminateArrows();

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
     * @return a node after pushing existing negations into the literal
     */
    public Node pushNegations() {
        Node copy = this.copy();
        copy = copy._pushNegations();
        return copy;
    }

    // internal method which may alter the existing tree
    abstract Node _pushNegations();

    /**
     * @return a deep copy of this node, everything except Literal are new instances
     */
    abstract Node copy();  // this return a deep copy

    abstract StringBuilder toStringBuilder();

    /**
     * @return the formula string of this node
     */
    public String toString() {
        return toStringBuilder().toString();
    }

    public static void main(String[] args) {
        var parser = new TruthTable();
//        parser.evaluate("(a /\\ (~b -> a) <-> c \\/ b -> ~a /\\ (c <-> a)) \\/ ~b");
        parser.evaluate("a /\\ b /\\ c");
        System.out.println("Before");
        System.out.println(parser.getTree());
        System.out.println(parser.generate());

        var start = Instant.now();
        parser.evaluate(parser.getTree()
                .eliminateArrows()
                .pushNegations()
                .removeRedundantBrackets());
        var end = Instant.now();

        System.out.println("After");
        System.out.println(parser.getTree());
        System.out.println(parser.generate());
        System.out.println(parser.getTree().toTreeString());

        System.out.println("Runtime: " + Duration.between(start, end).toMillis() + "ms");


//        System.out.println("Before eliminate arrow");
//        System.out.println(parser.getTree());
//        System.out.println(parser.generate());
//
//        parser.getTree()._eliminateArrows();
//        parser.evaluate(parser.getTree());
//
//        System.out.println("After eliminate arrow");
//        System.out.println(parser.getTree());
//        parser.evaluate(parser.getTree().toString());
//        System.out.println(parser.generate());
//
//        parser.evaluate(parser.getTree().pushNegations());
//
//        System.out.println("After push negation");
//        System.out.println(parser.getTree());
//        System.out.println("After remove redundant brackets");
//        parser.evaluate(parser.getTree().removeRedundantBrackets());
//        System.out.println(parser.getTree());
//        System.out.println(parser.generate());
    }
}
