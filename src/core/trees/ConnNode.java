package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Connective;


public class ConnNode extends BinaryNode {

    private final int precedence;
    private final Connective.Type type;

    public ConnNode(Connective conn) {
        super(conn);
        this.precedence = conn.getPrecedence();
        this.type = conn.getType();
    }

    @Override
    public Node insert(LitNode node) {
        if (right == null) {
            right = node;
        } else {
            right = right.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(BoxNode node) {
        if (right == null) {
            right = node;
        } else {
            right = right.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(ConnNode node) {
       if (node.precedence >= this.precedence) {
           node.left = this;
           return node;
       } else {
           if (right == null) {
               throw new InvalidInsertionException("Inserting connective immediately after connective");
           } else {
               right = right.insert(node);
               return this;
           }
       }
    }

    @Override
    public Node insert(NegNode node) {
        if (right == null) right = node;
        else right.insert(node);
        return this;
    }


    @Override
    public boolean isTrue() {
        switch (type) {
            case AND:
                return left.isTrue() && right.isTrue();
            case OR:
                return left.isTrue() || right.isTrue();
            case IFF:
                return left.isTrue() == right.isTrue();
            case IMPLIES:
                return !left.isTrue() || right.isTrue();
            default:
                throw new IllegalStateException("Undefined Connective Type");
        }
    }
}
