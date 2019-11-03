//package core.trees;
//
//
//import core.symbols.Connective;
//import core.symbols.Literal;
//
//final class CNFConnectiveHandler {
//
//    private static Node baseCheck(ConnNode conn_node) {
//        // a /\ b || a \/ b
//        if (conn_node.left instanceof LitNode && conn_node.right instanceof LitNode) {
//            return conn_node;
//        }
//        return null;
//    }
//
//    static Node handleAnd(ConnNode conn_node) {
//
//        Node check;
//        // literal /\ literal
//        if ((check = baseCheck(conn_node)) != null) {
//            return BracketNode.bracket(check);
//        }
//
//        Node left_ = conn_node.left instanceof BracketNode ? ((BracketNode) conn_node.left).head : conn_node.left;
//        Node right_ = conn_node.right instanceof BracketNode ? ((BracketNode) conn_node.right).head : conn_node.right;
//
//        // literal /\ (...)
//        if (left_ instanceof LitNode || right_ instanceof LitNode) {
//            Node other = left_ instanceof LitNode ? right_ : left_;
//            if (other instanceof ConnNode) {
//                // literal /\ (... /\ ...) || literal /\ (... \/ ...)
//                return BracketNode.bracket(conn_node);
//            } else {
//                throw new IllegalStateException("Right node not in CNF");
//            }
//        }
//
//        // (...) /\ (...)
//        ConnNode left = (ConnNode) left_;
//        ConnNode right = (ConnNode) right_;
//        if (left.type == right.type) {
//            if (left.type == Connective.Type.AND) {
//                // (... /\ ...) /\ (... /\ ...)
//                // => ... /\ ... /\ ... /\ ...
//                conn_node.left = left;
//                conn_node.right = right;
//                return BracketNode.bracket(conn_node);
//            } else if (left.type == Connective.Type.OR) {
//                // (... /\ ...) \/ (... /\ ...)
//                return conn_node;
//            } else {
//                throw new IllegalStateException("/\\ or \\/ not found after CNF");
//            }
//        }
//
//        // (... /\ ...) /\ (... \/ ...)
//        ConnNode distributor = left.type == Connective.Type.OR ? left : right;
//        ConnNode receiver = left.type == Connective.Type.OR ? right : left;
//        for (Literal literal : distributor.getLiterals()) {
//
//        }
//    }
//
//    static Node handleOr(ConnNode conn_node) {
//
//    }
//}
