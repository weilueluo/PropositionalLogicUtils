package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Connective;
import core.symbols.Literal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.common.Utilities.printMap;


public class ConnNode extends BinaryNode {

    private final int precedence;
    private final Connective.Type type;

    public ConnNode(Connective conn) {
        super(conn);
        this.type = conn.getType();
        this.precedence = conn.getPrecedence();
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
        if (node.precedence > this.precedence) {
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
    public boolean isSatisfiable(Map<Literal, Boolean> interpretation, boolean truth_value) {

        Map<Literal, Boolean> copy = new HashMap<>(interpretation);
        switch (type) {
            case OR:
                System.out.println("At OR");
                printMap(interpretation);
                System.out.println();
                if (left.isSatisfiable(copy, truth_value) || right.isSatisfiable(copy, truth_value)) {
                    interpretation.putAll(copy);
                    return true;
                } else {
                    return false;
                }
            case AND:
                System.out.println("At AND");
                printMap(interpretation);
                System.out.println();
                if (left.isSatisfiable(copy, truth_value) && right.isSatisfiable(copy, truth_value)) {
                    interpretation.putAll(copy);
                    return true;
                } else {
                    return false;
                }
            case IFF:
                System.out.println("At IFF");
                printMap(interpretation);
                System.out.println();
                if (left.isSatisfiable(copy, truth_value) == right.isSatisfiable(copy, truth_value)) {
                    interpretation.putAll(copy);
                    return true;
                } else {
                    return false;
                }
            case IMPLIES:
                System.out.println("At IMPLIES");
                printMap(interpretation);
                System.out.println();
                if (left.isSatisfiable(copy, !truth_value) || right.isSatisfiable(copy, truth_value)) {
                    interpretation.putAll(copy);
                    return true;
                } else {
                    return false;
                }
            default:
                throw new IllegalStateException("Invalid Connective type for checking satisfiability");
        }
    }
}
// ((a->(b/\c))<->(d<->(~(a->(c/\d)))))