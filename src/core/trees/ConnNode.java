package core.trees;

import core.exceptions.InvalidInsertionException;
import core.exceptions.InvalidNodeException;
import core.symbols.Connective;
import core.symbols.Literal;

import java.util.Set;


public class ConnNode extends BinaryNode {

    Connective.Type type;
    private int precedence;

    public ConnNode(Connective conn) {
        super(conn);
        this.precedence = conn.getPrecedence();
        this.type = conn.getType();
    }

    static ConnNode connect(Connective.Type conn_type, Node left, Node right) {
        ConnNode conn_node = new ConnNode(Connective.getInstance(conn_type));
        conn_node.left = left;
        conn_node.right = right;
        return conn_node;
    }

    private void changeTypeTo(Connective.Type type) {
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

    private void ensureLeftNotNull() {
        if (left == null) throw new InvalidNodeException("Left Node for a connective node should not be null");
    }

    void ensureFullNode() {
        if (left == null || right == null) {
            throw new InvalidNodeException("Connective node is incomplete");
        } else if (type == null) {
            throw new InvalidNodeException("Connective type is null");
        }
    }

    @Override
    public Node insert(LitNode node) {
        ensureLeftNotNull();
        if (right == null) {
            right = node;
        } else {
            right = right.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(BracketNode node) {
        ensureLeftNotNull();
        if (right == null) {
            right = node;
        } else {
            right = right.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(ConnNode node) {
        ensureLeftNotNull();
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
        ensureLeftNotNull();
        if (right == null) right = node;
        else right.insert(node);
        return this;
    }

    @Override
    public boolean isTrue() {
        ensureFullNode();
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

    public Node toCNF() {
        ensureFullNode();
        _eliminateArrows();  // remove -> and <->
        left = left.toCNF();
        right = right.toCNF();
        _removeRedundantBrackets();

//        if (type == Connective.Type.AND) {
//            return CNFConnectiveHandler.handleAnd(this);
//        } else if (type == Connective.Type.OR) {
//            return CNFConnectiveHandler.handleOr(this);
//        } else {
//            throw new IllegalStateException("Unrecognised Connective type");
//        }
        return null;
    }

    @Override
    void addLiterals(Set<Literal> literals) {
        left.addLiterals(literals);
        right.addLiterals(literals);
    }

    @Override
    Node _removeRedundantBrackets() {
        left = left._removeRedundantBrackets();
        right = right._removeRedundantBrackets();
        return this;
    }

    @Override
    public void _eliminateArrows() {
        ensureFullNode();
        if (type == Connective.Type.IMPLIES) {
            // a -> b == ~a \/ b
            // we cannot just do left.invertNegation();
            // because it will change negation of the same literal in other part of the formula
            // e.g. a <-> b will become ((~a \/ ~b) /\ (~b \/ ~a)) instead of ((~a \/ b) /\ (~b \/ a))

            // change a to ~(a)
            left = NegNode.negate(BracketNode.bracket(left));

            // change b to (b)
            right = BracketNode.bracket(right);

            // change /\ to \/
            changeTypeTo(Connective.Type.OR);
        } else if (type == Connective.Type.IFF) {
            // a <-> b == a -> b /\ b -> a

            // create (a -> b)
            BracketNode new_left = BracketNode.bracket(ConnNode.connect(Connective.Type.IMPLIES, left, right));

            // create (b -> a)
            BracketNode new_right = BracketNode.bracket(
                    ConnNode.connect(Connective.Type.IMPLIES, right.copy(), left.copy()));

            // change assignment
            left = new_left;
            right = new_right;

            // change <-> to /\
            changeTypeTo(Connective.Type.AND);
        }
        left._eliminateArrows();
        right._eliminateArrows();
    }

    @Override
    Node _invertNegation() {
        ensureFullNode();
        if (type == Connective.Type.IMPLIES || type == Connective.Type.IFF) {
            _eliminateArrows();
        }
        if (type == Connective.Type.AND) {
            changeTypeTo(Connective.Type.OR);
        } else if (type == Connective.Type.OR) {
            changeTypeTo(Connective.Type.AND);
        } else {
            throw new IllegalStateException("Unrecognised connective type even after eliminating arrow");
        }
        left = left._invertNegation();
        right = right._invertNegation();
        return this;
    }

    @Override
    Node _pushNegations() {
        left = left._pushNegations();
        right = right._pushNegations();
        return this;
    }

    @Override
    Node copy() {
        ensureFullNode();
        return ConnNode.connect(type, left.copy(), right.copy());
    }

    @Override
    StringBuilder toStringBuilder() {
        return new StringBuilder()
                .append(left.toStringBuilder())
                .append(' ')
                .append(value.getFull())
                .append(' ')
                .append(right.toStringBuilder());
    }
}
