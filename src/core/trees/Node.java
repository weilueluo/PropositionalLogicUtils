package core.trees;

import com.sun.source.doctree.LinkTree;
import core.TruthTable;
import core.symbols.Literal;
import core.symbols.Symbol;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public abstract class Node implements NodeInsertion, TruthValue {
    Symbol value;

    @Contract(pure = true)
    protected Node() {
        this(null);
    }

    @Contract(pure = true)
    protected Node(Symbol v) {
        value = v;
    }

    String getSpaces(int depth) {
        return " ".repeat(depth * 3);
    }

    public abstract String toString(int depth);
}
