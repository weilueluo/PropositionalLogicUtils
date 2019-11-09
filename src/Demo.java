import core.TruthTable;
import core.common.Pair;
import core.trees.Node;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Demo {

    public static void main(String[] args) {

        var truth_table = new TruthTable();
//        truth_table.evaluate("(a /\\ (~b -> a) <-> c \\/ b -> ~a /\\ (c <-> a)) \\/ ~b");
//          truth_table.evaluate("(~((a)) \\/ (~~c)) /\\ ((((b \\/ (~~(~~(c -> a)))))))");
        truth_table.evaluate("~~~(a -> b /\\ ~c \\/ ~b) <-> a -> c /\\ ~(c /\\ ((~a))) -> ~~~~((a)) /\\ (~(~~(~c)))");
        System.out.println("Original: " + truth_table.getTree());
        System.out.println("Original Truth Table:");
        System.out.println(truth_table.generate());
        //        truth_table.evaluate(parser.getTree()
        //                .eliminateArrows()
        //                .pushNegations()
        //                .removeRedundantBrackets());

        var start = Instant.now();
        Pair<Node, List<Node>> node_and_clauses = truth_table.getTree().toCNF();
        var end = Instant.now();
        System.out.println("CNF: " + node_and_clauses.getItem1());
        truth_table.evaluate(node_and_clauses.getItem1().toString());
        System.out.println("CNF Converted Truth Table:");
        System.out.println(truth_table.generate());
        System.out.println("CNF Clauses:");
        for (Node node : node_and_clauses.getItem2()) {
            System.out.println(node);
        }
        System.out.println("CNF Conversion Runtime: " + Duration.between(start, end).toMillis() + "ms");
    }
}