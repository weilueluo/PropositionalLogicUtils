package core;

import core.symbols.Literal;
import core.trees.Node;

import java.time.Duration;
import java.time.Instant;

public class TruthTable extends Parser {

    private static final int literal_template_size = 10;
    private static final int tree_template_size = 15;
    private final static String literal_truth_value_template = "|%" + literal_template_size + "s";
    private final static String tree_truth_value_template = "|%" + tree_template_size + "s|";
    private Literal[] literals;
    private Node tree;
    private StringBuilder curr_truth_table_builder;

    public TruthTable() {

    }  // empty

    public static void main(String[] args) {
        TruthTable truth_table = new TruthTable();

        Instant s = Instant.now();
        truth_table.evaluate("(Chicken /\\ (~F -> Snake) <-> Chicken \\/ Tiger -> T /\\ (Snake <-> Chicken)) <-> " +
                "~Tiger <-> Snake");
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
        curr_truth_table_builder = new StringBuilder();
        generate(0);
        return getHeader().append(curr_truth_table_builder).toString();
    }

    private StringBuilder getHeader() {
        StringBuilder cover = new StringBuilder();
        StringBuilder titles = new StringBuilder();
        for (Literal literal : literals) {
            cover.append(String.format(literal_truth_value_template, "_".repeat(literal_template_size)));
            titles.append(String.format(literal_truth_value_template, literal.getRaw()));
        }
        cover.append(String.format(tree_truth_value_template, "_".repeat(tree_template_size)));
        titles.append(String.format(tree_truth_value_template, "Truth Value"));
        return cover.append(System.lineSeparator()).append(titles).append(System.lineSeparator());
    }

    private void generate(int index) {
        if (index == literals.length) {
            curr_truth_table_builder.append(getCurrentRowString());
            return;
        }
        Literal curr_literal = literals[index];
        if (curr_literal.isContradiction() || curr_literal.isTautology()) {
            generate(index + 1);
        } else {
            curr_literal.assign(true);
            generate(index + 1);
            curr_literal.assign(false);
            generate(index + 1);
        }
    }

    private StringBuilder getCurrentLiteralsString() {
        StringBuilder sb = new StringBuilder();
        for (Literal literal : literals) {
            sb.append(String.format(literal_truth_value_template, literal.getTruthValue()));
        }
        return sb;
    }

    private StringBuilder getCurrentRowString() {
        return getCurrentLiteralsString()
                .append(String.format(tree_truth_value_template, tree.isTrue()))
                .append(System.lineSeparator());
    }
}
