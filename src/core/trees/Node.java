package core.trees;

import core.symbols.Symbol;

public abstract class Node implements NodeInsertion, Satisfiable {
    public Symbol node_value;

    protected Node() {
        this(null);
    }

    protected Node(Symbol v) {
        node_value = v;
    }

    protected String getSpaces(int depth) {
        return " ".repeat(depth * 3);
    }

    public abstract String toString(int depth);
}
