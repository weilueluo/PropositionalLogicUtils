package core.trees;

import core.symbols.Symbol;


public abstract class SingletonNode extends Node {
    Node mid;

    SingletonNode(Symbol value) {
        super(value);
        mid = null;
    }

    @Override
    public StringBuilder toTreeStringBuilder(int depth) {
        return new StringBuilder(getSpaces(depth))
                .append("|- ")
                .append(value.getFull()).append(System.lineSeparator())
                .append(mid.toTreeStringBuilder(depth + 1));
    }
}
