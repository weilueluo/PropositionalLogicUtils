package core.trees;

import core.symbols.Symbol;

import static core.symbols.Symbol.LBRACKET;
import static core.symbols.Symbol.RBRACKET;

public abstract class BinaryNode extends Node {

    Node left, right;

    BinaryNode() {
        super();
        left = right = null;
    }

    BinaryNode(Symbol value) {
        super(value);
        left = right = null;
    }

    @Override
    public StringBuilder toTreeStringBuilder(int depth) {
        StringBuilder left_str = left == null ? new StringBuilder() : left.toTreeStringBuilder(depth + 1);
        StringBuilder right_str = right == null ? new StringBuilder() : right.toTreeStringBuilder(depth + 1);
        return new StringBuilder(getSpaces(depth))
                        .append("|- ")
                        .append(value.getFull())
                        .append(System.lineSeparator())
                        .append(left_str)
                        .append(right_str);
    }

    @Override
    public StringBuilder toBracketStringBuilder() {
        StringBuilder left_sb = left == null ? new StringBuilder() : left.toBracketStringBuilder();
        StringBuilder right_sb = right == null ? new StringBuilder() : right.toBracketStringBuilder();
        return new StringBuilder()
                .append(LBRACKET)
                .append(left_sb)
                .append(value.getFull())
                .append(right_sb)
                .append(RBRACKET);
    }
}
