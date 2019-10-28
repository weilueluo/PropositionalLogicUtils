package core.trees;

import core.symbols.Symbol;

public abstract class SingletonNode extends Node {
    protected Node mid;

    public SingletonNode() {
        super();
        mid = null;
    }

    public SingletonNode(Symbol value) {
        super(value);
        mid = null;
    }

    @Override
    public String toString(int depth) {
        String spaces = getSpaces(depth);
        return String.format(spaces + "|- %s%n%s", value.getFull(), mid.toString(depth + 1));
    }
}
