package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Connective;
import org.jetbrains.annotations.NotNull;


public class ConnNode extends BinaryNode {

    private final int precedence;

    public ConnNode(Connective conn) {
        super(conn);
        this.precedence = conn.getPrecedence();
    }

    public int getPrecedence() {
        return precedence;
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
    public Node insert(@NotNull ConnNode node) {
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

}
