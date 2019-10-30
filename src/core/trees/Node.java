package core.trees;

import core.symbols.Symbol;

public abstract class Node implements NodeInsertion, TruthValue {
    Symbol value;

    protected Node() {
        this(null);
    }

    protected Node(Symbol v) {
        value = v;
    }

    String getSpaces(int depth) {
        return " ".repeat(depth * 3);
    }

    public abstract String toString(int depth);
}
