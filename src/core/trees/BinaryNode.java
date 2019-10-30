package core.trees;

import core.symbols.Symbol;

public abstract class BinaryNode extends Node {

    protected Node left, right;

    public BinaryNode() {
        super();
        left = right = null;
    }

    public BinaryNode(Symbol value) {
        super(value);
        left = right = null;
    }

    public String toString(int depth) {
//        String spaces = getSpaces(depth);
//        String left_str = left == null ? "" : left.toString(depth + 1);
//        String right_str = right == null ? "" : right.toString(depth + 1);
        return "(" + (left == null ? "" : left.toString(depth)) + node_value.getFull() + (right == null ? "" : right.toString(depth)) + ")";
    }
}
