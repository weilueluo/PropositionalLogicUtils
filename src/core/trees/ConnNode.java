package core.trees;

import core.exceptions.InvalidInsertionException;
import core.symbols.Connective;
import core.symbols.Negation;


public class ConnNode extends BinaryNode {

    private int precedence;
    private Connective.Type type;

    public ConnNode(Connective conn) {
        super(conn);
        this.precedence = conn.getPrecedence();
        this.type = conn.getType();
    }

    private void change_type_to(Connective.Type type) {
        Connective new_conn;
        switch (type) {
            case AND:
                new_conn = Connective.getAndInstance();
                break;
            case OR:
                new_conn = Connective.getOrInstance();
                break;
            case IMPLIES:
                new_conn = Connective.getImpliesInstance();
                break;
            case IFF:
                new_conn = Connective.getIffInstance();
                break;
            default:
                throw new IllegalStateException("Unrecognised connective type");
        }
        this.value = new_conn;
        this.precedence = new_conn.getPrecedence();
        this.type = new_conn.getType();
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
    public Node insert(BracketNode node) {
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
                throw new IllegalStateException("Unrecognised Connective Type");
        }
    }

    @Override
    protected void eliminateArrows() {
        if (type == Connective.Type.IMPLIES) {
            // a -> b == ~a \/ b
            // we cannot just do left.invertNegation();
            // because it will change negation of the same literal in other part of the formula
            // e.g. a <-> b will become ((~a \/ ~b) /\ (~b \/ ~a)) instead of ((~a \/ b) /\ (~b \/ a))

            // change a to ~(a)
            NegNode neg_node = new NegNode(Negation.getInstance());
            BracketNode left_bracket_node = new BracketNode();
            left_bracket_node.head = left;
            left_bracket_node.close();
            neg_node.insert(left_bracket_node);
            left = neg_node;

            // change b to (b)
            BracketNode right_bracket_node = new BracketNode();
            right_bracket_node.head = right;
            right_bracket_node.close();
            right = right_bracket_node;

            // change /\ to \/
            change_type_to(Connective.Type.OR);
        } else if (type == Connective.Type.IFF) {
            // a <-> b == a -> b /\ b -> a

            // create (a -> b)
            BracketNode new_left = new BracketNode();
            ConnNode new_left_head = new ConnNode(Connective.getImpliesInstance());
            new_left_head.left = left;
            new_left_head.right = right;
            new_left.close();
            new_left.head = new_left_head;

            // create (b -> a)
            BracketNode new_right = new BracketNode();
            ConnNode new_right_head = new ConnNode(Connective.getImpliesInstance());
            new_right_head.left = right.copy();
            new_right_head.right = left.copy();
            new_right.close();
            new_right.head = new_right_head;

            // change assignment
            left = new_left;
            right = new_right;

            // change <-> to /\
            change_type_to(Connective.Type.AND);
        }
        left.eliminateArrows();
        right.eliminateArrows();
    }

    @Override
    protected Node invertNegation() {
        if (type == Connective.Type.IMPLIES || type == Connective.Type.IFF) {
            eliminateArrows();
        }
        if (type == Connective.Type.AND) {
            change_type_to(Connective.Type.OR);
        } else if (type == Connective.Type.OR) {
            change_type_to(Connective.Type.AND);
        } else {
            throw new IllegalStateException("Unrecognised connective type even after eliminating arrow");
        }
        left = left.invertNegation();
        right = right.invertNegation();
        return this;
    }

    @Override
    protected Node copy() {
        ConnNode new_node = new ConnNode(Connective.getInstance(type));
        new_node.left = left.copy();
        new_node.right = right.copy();
        return new_node;
    }

    @Override
    protected StringBuilder toStringBuilder() {
        return new StringBuilder()
                .append(left.toStringBuilder())
                .append(' ')
                .append(value.getFull())
                .append(' ')
                .append(right.toStringBuilder());
    }
}
