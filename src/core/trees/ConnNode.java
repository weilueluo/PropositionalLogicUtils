package core.trees;

import core.exceptions.InvalidInsertionException;
import core.exceptions.InvalidNodeException;
import core.exceptions.InvalidSymbolException;
import core.symbols.Connective;
import core.symbols.Literal;

import java.util.*;

import static core.symbols.Connective.Type.AND;
import static core.symbols.Connective.Type.OR;


public class ConnNode extends BinaryNode {

    Connective.Type type;
    private int precedence;

    public ConnNode(Connective conn) {
        super(conn);
        this.precedence = conn.getPrecedence();
        this.type = conn.getType();
    }

    public static ConnNode connect(Connective.Type conn_type, Node left, Node right) {
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

    private void ensureFullNode() {
        if (left == null || right == null) {
            throw new InvalidNodeException("Connective node is incomplete");
        } else if (type == null) {
            throw new InvalidNodeException("Connective type is null");
        }
    }

    @Override
    public Node insert(LitNode node) {
        if (left == null) {
            left = node;
        } else if (right == null) {
            right = node;
        } else {
            right = right.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(BracketNode node) {
        if (left == null) {
            left = node;
        } else if (right == null) {
            right = node;
        } else {
            right = right.insert(node);
        }
        return this;
    }

    @Override
    public Node insert(ConnNode node) {
        if (left  == null) {
            throw new InvalidInsertionException("Inserting connective immediately before connective");
        } else if (node.getPrecedence() >= this.getPrecedence()) {
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
        if (left == null) throw new InvalidInsertionException("Inserting Negation immediately before connective");
        else if (right == null) right = node;
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

    @Override
    public boolean isTautology() {
        switch(type) {
            case OR:
                return left.isTautology() || right.isTautology();
            case IMPLIES:
                return left.isContradiction() || right.isTautology();
            case IFF:
                return left.isTautology() && right.isTautology() || left.isContradiction() && right.isContradiction();
            case AND:
                return left.isTautology() && right.isTautology();
            default:
                throw new InvalidSymbolException("Unrecognised connective type");
        }
    }

    @Override
    public boolean isContradiction() {
        switch(type) {
            case OR:
                return left.isContradiction() && right.isContradiction();
            case IMPLIES:
                return left.isTautology() && right.isContradiction();
            case IFF:
                return left.isTautology() && right.isContradiction() || left.isContradiction() && right.isTautology();
            case AND:
                return left.isContradiction() || right.isContradiction();
            default:
                throw new InvalidSymbolException("Unrecognised connective type");
        }
    }

    @Override
    public Node copy() {
        return ConnNode.connect(type,
                left == null ? null : left.copy(),
                right == null ? null : right.copy());
    }

    @Override
    Node _removeRedundantBrackets(int parent_precedence) {
        left = left._removeRedundantBrackets(getPrecedence());
        right = right._removeRedundantBrackets(getPrecedence());
        return this;
    }

    @Override
    Node _pushNegations() {
        left = left._pushNegations();
        right = right._pushNegations();
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
            changeTypeTo(OR);
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
    Node _toCNF(List<Node> clauses) {
        // remove redundant bracket should already be called
        // so at most one bracket outside and we remove them first, just keep that it mind
        left = left instanceof BracketNode ? ((BracketNode) left).head : left;
        right = right instanceof BracketNode ? ((BracketNode) right).head : right;

        // we dont care about whats inside previously
        // as long as clauses contains clauses of this node when return
        clauses.clear();

        // both side are literal or ~literal
        // a /\ b || a \/ b || ~a \/ b || ~a /\ b || a /\ ~b || a \/ ~b || ~a \/ ~b || ~a /\ ~b
        if ((left instanceof NegNode || left instanceof LitNode) &&
                (right instanceof NegNode || right instanceof LitNode)) {

            // add clauses
            if (type == AND) {
                clauses.add(left);
                clauses.add(right);
            } else {  // type == OR
                clauses.add(this);
            }

            return this;
        }

        // at least one side is connective
        List<Node> left_clauses = new ArrayList<>();
        List<Node> right_clauses = new ArrayList<>();
        left = left._toCNF(left_clauses);
        right = right._toCNF(right_clauses);

        if (type == AND) {
            // ... /\ ...
            clauses.addAll(left_clauses);
            clauses.addAll(right_clauses);
            return _handleCNF_AND();
        } else {
            // ... \/ ...
            // distributivity law needed
            return handleCNF_OR(clauses, left_clauses, right_clauses);
        }
    }

    @Override
    void addLiterals(Set<Literal> literals) {
        left.addLiterals(literals);
        right.addLiterals(literals);
    }

    @Override
    int getPrecedence() {
        return precedence;
    }

    @Override
    Node _invertNegation() {
        ensureFullNode();
        if (type == Connective.Type.IMPLIES || type == Connective.Type.IFF) {
            _eliminateArrows();
        }
        if (type == Connective.Type.AND) {
            changeTypeTo(OR);
        } else if (type == OR) {
            changeTypeTo(Connective.Type.AND);
        } else {
            throw new IllegalStateException("Unrecognised connective type even after eliminating arrow");
        }
        left = left._invertNegation();
        right = right._invertNegation();
        return this;
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ConnNode)) return false;

        ConnNode node = (ConnNode) other;
        if (node.type != type) return false;

        return Objects.equals(left, node.left) && Objects.equals(right, node.right);
    }

    private Node _handleCNF_AND() {
        // left and right should not be literal at the same time
        // neither left nor right is bracket node
        // negation node must follow by literal node

        // check if there is literal on either side
        Boolean left_is_literal = null;
        if (left instanceof LitNode || left instanceof NegNode) {
            left_is_literal = true;
        } else if (right instanceof LitNode || right instanceof NegNode) {
            left_is_literal = false;
        }

        if (left_is_literal != null) {
            // one side is literal
            // other side must be connective
            ConnNode conn_node = left_is_literal ? (ConnNode) right : (ConnNode) left;
            if (conn_node.type == OR) {
                // p /\ (q \/ r) or (q \/ r) /\ p
                // bracket the OR node and return
                if (left_is_literal) {
                    right = BracketNode.bracket(right);
                } else {
                    left = BracketNode.bracket(left);
                }

                return this;
            } else {
                // p /\ q /\ r
                // already in cnf
                return this;
            }
        }

        // both side are connective
        ConnNode left_conn_node = (ConnNode) left;
        ConnNode right_conn_node = (ConnNode) right;
        if (left_conn_node.type == AND && right_conn_node.type == AND) {
            // a /\ b /\ c /\ d
            return this;
        }

        if (left_conn_node.type == OR && right_conn_node.type == OR) {
            // (a \/ b) /\ (c \/ d)
            left = BracketNode.bracket(left);
            right = BracketNode.bracket(right);
            return this;
        }

        // one side is /\ other is \/
        boolean left_is_and = left_conn_node.type == AND;
        if (left_is_and) {
            // a /\ b /\ (c \/ d)
            right = BracketNode.bracket(right);
        } else {
            // (a \/ b) /\ c /\ d
            left = BracketNode.bracket(left);
        }
        return this;
    }

    private Node handleCNF_OR(List<Node> clauses, List<Node> left_clauses, List<Node> right_clauses) {
        // left and right should not be literal at the same time
        // neither left nor right is bracket node
        // negation node must follow by literal node

        // check if there is literal on either side
        Boolean left_is_literal = null;
        if (left instanceof LitNode || left instanceof NegNode) {
            left_is_literal = true;
        } else if (right instanceof LitNode || right instanceof NegNode) {
            left_is_literal = false;
        }

        if (left_is_literal != null) {
            // one side is literal
            // other side must be connective
            ConnNode conn_node = left_is_literal ? (ConnNode) right : (ConnNode) left;
            if (conn_node.type == OR) {
                // p \/ q \/ r
                // already in cnf
                clauses.addAll(left_clauses);
                clauses.addAll(right_clauses);
                return this;
            } else {
                // p \/ (q /\ r) || (q /\ r) \/ p
                // distribute
                if (left_is_literal) {
                    return _cnf_distribute(clauses, left_clauses, right_clauses);
                } else {
                    return _cnf_distribute(clauses, right_clauses, left_clauses);
                }
            }  // if (conn_node.type == OR)
        }

        // both are connective
        ConnNode left_conn_node = (ConnNode) left;
        ConnNode right_conn_node = (ConnNode) right;
        if (left_conn_node.type == OR && right_conn_node.type == OR) {
            // p \/ q \/ r \/ t, already in cnf
            clauses.addAll(left_clauses);
            clauses.addAll(right_clauses);
            return this;
        }

        if (left_conn_node.type == AND && right_conn_node.type == AND) {
            // (p /\ q) \/ (r /\ t)
            // for each node in left side, distribute to right side
            // and link the results from distribution by AND
            // => (p \/ r) /\ (p \/ t) /\ (q \/ r) /\ (q \/ t)
            List<Node> cnf_distributed_nodes = new ArrayList<>();
            for (Node clause : left_clauses) {
                List<Node> one_clause_list = new ArrayList<>();
                one_clause_list.add(clause);
                Node cnf_distributed_node = _cnf_distribute(clauses, one_clause_list, right_clauses);
                cnf_distributed_nodes.add(cnf_distributed_node);
            }
            return _AND_link(cnf_distributed_nodes);
        }

        // must be one /\ and one \/
        // /\ clauses distribute to \/ clauses
        Node cnf_node;
        if (left_conn_node.type == AND) {
            cnf_node = _cnf_distribute(clauses, left_clauses, right_clauses);
        } else {
            cnf_node = _cnf_distribute(clauses, right_clauses, left_clauses);
        }
        return cnf_node;
    }

    private Node _cnf_distribute(List<Node> clauses, List<Node> distributor, List<Node> receiver) {
        List<Node> last_received_nodes = receiver;
        for (Node distribute_node : distributor) {
            List<Node> curr_received_nodes = new ArrayList<>();
            for (Node receive_node : last_received_nodes) {
                curr_received_nodes.add(ConnNode.connect(OR, receive_node, distribute_node));
            }
            last_received_nodes = curr_received_nodes;
        }

        // now all receive nodes are updated to have distribute nodes
        // update clauses
        clauses.addAll(last_received_nodes);
        // link them by AND
        return _AND_link(last_received_nodes);
    }

    private Node _AND_link(List<Node> nodes) {
        BinaryNode curr_insert_node = null;
        BinaryNode head = null;
        for (Iterator<Node> node_iterator = nodes.iterator(); node_iterator.hasNext(); ) {

            // bracket the OR node to link by AND, because AND has lower precedence
            Node curr_node = BracketNode.bracket(node_iterator.next());

            if (curr_insert_node == null) {
                // first time, insert left
                curr_insert_node = new ConnNode(Connective.getAndInstance());
                curr_insert_node.left = curr_node;
                head = curr_insert_node;  // keep track of head

            } else if (!node_iterator.hasNext()) {
                // last time, insert right
                curr_insert_node.right = curr_node;

            } else {
                // middle insertion, create new node and insert left
                curr_insert_node.right = new ConnNode(Connective.getAndInstance());
                curr_insert_node = (ConnNode) curr_insert_node.right;
                curr_insert_node.left = curr_node;
            }
        }
        return head;
    }

}
