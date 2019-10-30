package core;

import core.symbols.Literal;
import core.trees.Node;

import java.time.Duration;
import java.time.Instant;

public class TruthTable extends Parser {

    private Literal[] literals;
    private Node tree;
    private static final int literal_template_size = 10;
    private static final int tree_template_size = 15;
    private final static String literal_truth_value_template = "|%"+ literal_template_size + "s";
    private final static String tree_truth_value_template = "|%" + tree_template_size + "s|";

    public TruthTable() {

    }  // empty

    public static void main(String[] args) {
        TruthTable truth_table = new TruthTable();

        Instant s = Instant.now();
        truth_table.evaluate("(Chicken /\\ (~Tiger -> Snake) <-> Chicken \\/ Tiger -> ~Snake /\\ (Snake <-> Chicken)) <-> ~Tiger <-> Snake");
        Instant e = Instant.now();
        System.out.println((String.format("Evaluation Runtime: %sms", Duration.between(s, e).toMillis())));

        s = Instant.now();
        String table = truth_table.generate();
        e = Instant.now();
        System.out.println(truth_table);
        System.out.println(table);
        System.out.println((String.format("Truth Table Generation Runtime: %sms", Duration.between(s, e).toMillis())));
    }

    public String generate() {
        ensureEvaluated();
        literals = getLiterals();
        tree = getTree();
        String header = getHeader();
        return header + generate(0);
    }

    private String getHeader() {
        StringBuilder cover = new StringBuilder();
        StringBuilder titles = new StringBuilder();
        for (Literal literal : literals) {
            cover.append(String.format(literal_truth_value_template, "_".repeat(literal_template_size)));
            titles.append(String.format(literal_truth_value_template, literal.getRaw()));
        }
        cover.append(String.format(tree_truth_value_template, "_".repeat(tree_template_size)));
        titles.append(String.format(tree_truth_value_template, "Truth Value"));
        return cover.toString() + System.lineSeparator() + titles.toString() + System.lineSeparator();
    }

    private String generate(int index) {
        if (index == literals.length) {
            return getCurrentRowString();
        }
        StringBuilder sb = new StringBuilder();
        literals[index].assign(true);
        sb.append(generate(index + 1));
        literals[index].assign(false);
        sb.append(generate(index + 1));
        return sb.toString();
    }

    private String getCurrentLiteralsString() {
        StringBuilder sb = new StringBuilder();
        for (Literal literal : literals) {
            sb.append(String.format(literal_truth_value_template, literal.getTruthValue()));
        }
        return sb.toString();
    }

    private String getCurrentRowString() {
        return getCurrentLiteralsString() + String.format(tree_truth_value_template, tree.isTrue()) + System.lineSeparator();
    }
}
