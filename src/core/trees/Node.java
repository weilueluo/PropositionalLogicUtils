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

    String getSpaces(int depth) {
        return depth <= 0 ? "" : String.format("%" + depth * 3 + "d", 0).replace("0", " ");
    }

    public String toTreeString() {
        return toTreeStringBuilder(0).toString();
    }

    public String toBracketString() {
        return toBracketStringBuilder().toString();
    }

    public abstract Node toCNF();

    public Literal[] getLiterals() {
        Set<Literal> literals = new HashSet<>();
        addLiterals(literals);
        return literals.toArray(Literal[]::new);
    }

    abstract void addLiterals(Set<Literal> literals);

    abstract Node removeRedundantBrackets();

    abstract StringBuilder toTreeStringBuilder(int depth);

    abstract StringBuilder toBracketStringBuilder();

    abstract void eliminateArrows();

    abstract Node invertNegation();

    abstract Node pushNegations();

    abstract Node copy();  // this return a deep copy

    abstract StringBuilder toStringBuilder();

    public String toString() {
        return toStringBuilder().toString();
    }

    public static void main(String[] args) {
        var parser = new TruthTable();
        parser.evaluate("(a /\\ (~b -> a) <-> c \\/ b -> ~a /\\ (c <-> a)) \\/ ~b");

        System.out.println("Before eliminate arrow");
        System.out.println(parser.getTree());
        System.out.println(parser.generate());

        parser.getTree().eliminateArrows();
        parser.evaluate(parser.getTree());

        System.out.println("After eliminate arrow");
        System.out.println(parser.getTree());
        parser.evaluate(parser.getTree().toString());
        System.out.println(parser.generate());

        Instant start = Instant.now();
        parser.evaluate(parser.getTree().pushNegations());
        Instant end = Instant.now();

        System.out.println("After push negation");
        System.out.println(parser.getTree());
        System.out.println("After remove redundant brackets");
        parser.evaluate(parser.getTree().removeRedundantBrackets());
        System.out.println(parser.getTree());
        System.out.println(parser.generate());
        System.out.println("Push Negations Runtime: " + Duration.between(start, end).toMillis() + "ms");
    }
}
