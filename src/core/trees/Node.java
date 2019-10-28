package core.trees;

import core.symbols.Symbol;
import org.jetbrains.annotations.Contract;

public abstract class Node implements NodeInsertion {
    public Symbol value;

    @Contract(pure = true)
    protected Node() {
        this(null);
    }

    @Contract(pure = true)
    protected Node(Symbol v) {
        value = v;
    }

    protected String getSpaces(int depth) {
        return " ".repeat(depth * 3);
    }

    public abstract String toString(int depth);
}
