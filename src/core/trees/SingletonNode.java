package core.trees;

import core.exceptions.InvalidNodeException;
import core.symbols.Symbol;


public abstract class SingletonNode extends Node {
    Node descendant;

    SingletonNode(Symbol value) {
        super(value);
        if (value == null) {
            throw new InvalidNodeException("Constructing Singleton node with null symbol");
        }
        descendant = null;
    }

    @Override
    public StringBuilder toTreeStringBuilder(int depth) {
        StringBuilder descendant_sb =
                descendant == null ?
                    new StringBuilder() :
                        descendant.toTreeStringBuilder(depth + 1);
        return new StringBuilder(getSpaces(depth))
                .append("|- ")
                .append(value.getFull()).append(System.lineSeparator())
                .append(descendant_sb);
    }
}
