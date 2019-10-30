package core;

import core.symbols.Literal;
import core.trees.Node;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class TruthTable extends Parser {

    private List<Literal> literals;
    private Node tree;
    private final static String literalTruthValueTemplate = "|%10s";
    private final static String TreeTruthValueTemplate = "|%15s|";

    public TruthTable() {

    }  // empty

    public static void main(String[] args) {
        TruthTable tb = new TruthTable();
        Instant s = Instant.now();
        tb.evaluate("(Chicken /\\ (~Tiger -> Cat) <-> Chicken \\/ Tiger -> ~Cat /\\ (Snake <-> Chicken)) \\/ ~Tiger");
        Instant e = Instant.now();
        System.out.println((String.format("Runtime: %sms", Duration.between(s, e).toMillis())));
        System.out.println(tb.generate());
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
            cover.append(String.format(literalTruthValueTemplate, '_')
                    .replaceAll("[^_]", "_"));
            titles.append(String.format(literalTruthValueTemplate, literal.getRaw()));
        }
        cover.append(String.format(TreeTruthValueTemplate, '_')
                .replaceAll("[^_]", "_"));
        titles.append(String.format(TreeTruthValueTemplate, "Truth Value"));
        return cover.toString() + System.lineSeparator() + titles.toString() + System.lineSeparator();
    }

    private String generate(int index) {
        if (index == literals.size()) {
            return getCurrentRowString();
        }
        StringBuilder sb = new StringBuilder();
        literals.get(index).assign(true);
        sb.append(generate(index + 1));
        literals.get(index).assign(false);
        sb.append(generate(index + 1));
        return sb.toString();
    }

    private String getCurrentLiteralsString() {
        StringBuilder sb = new StringBuilder();
        for (Literal literal : literals) {
            sb.append(String.format(literalTruthValueTemplate, literal.getTruthValue()));
        }
        return sb.toString();
    }

    private String getCurrentRowString() {
        return getCurrentLiteralsString() + String.format(TreeTruthValueTemplate, tree.isTrue()) + System.lineSeparator();
    }
}
