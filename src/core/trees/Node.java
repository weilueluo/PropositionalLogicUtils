package core.trees;

import core.TruthTable;
import core.exceptions.InvalidNodeException;
import core.exceptions.InvalidSymbolException;
import core.symbols.Symbol;

import java.time.Duration;
import java.time.Instant;


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

    public abstract StringBuilder toTreeStringBuilder(int depth);

    public abstract StringBuilder toBracketStringBuilder();

    protected abstract void eliminateArrows();

    protected abstract Node invertNegation();

    protected abstract Node copy();  // this return a deep copy

    protected abstract StringBuilder toStringBuilder();

    public String toString() {
        return toStringBuilder().toString();
    }

    public static void main(String[] args) {
        var parser = new TruthTable();
        parser.evaluate("(a /\\ (~b -> a) <-> c \\/ b -> ~a /\\ (c <-> a)) \\/ ~b");

        System.out.println("Before eliminate arrow");
        System.out.println(parser.getTree());
        System.out.println(parser.generate());

        Instant start = Instant.now();
        parser.getTree().eliminateArrows();
        Instant end = Instant.now();

        System.out.println("After eliminate arrow");
        System.out.println(parser.getTree());
        parser.evaluate(parser.getTree().toString());
        System.out.println(parser.generate());
        System.out.println("Arrow Elimination Runtime: " + Duration.between(start, end).toMillis() + "ms");
    }
}
