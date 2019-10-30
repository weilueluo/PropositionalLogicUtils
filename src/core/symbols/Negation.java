package core.symbols;

import core.exceptions.InvalidSymbolException;
import core.trees.Node;
import core.trees.NegNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static core.common.Utilities.stripAllSpaces;

public class Negation extends Symbol {

    private static Negation NEGATION;

    static {
        NEGATION = new Negation('~');
    }

    private String negation;
    private String unprocessed_str;

    @Contract("null -> fail")
    private Negation(String s) {
        if (s == null) throw new InvalidSymbolException("Null is not negation");
        unprocessed_str = s;
        s = stripAllSpaces(s);
        if (s.length() != 1) throw new InvalidSymbolException(String.format("Invalid negation: \"%s\"", s));
        if (s.charAt(0) != NEG)
            throw new InvalidSymbolException(String.format("Given character is not negation: \"%s\", should be " +
                    "\"%s\"", s.charAt(0), NEG));
        negation = s;
    }

    private Negation(char c) {
        if (c != NEG) {
            throw new InvalidSymbolException(String.format("\"%s\" is not a valid negation symbol", c));
        }
        negation = unprocessed_str = String.valueOf(c);
    }

    public static Negation newInstance(@NotNull String s) {
        if (s.equals(String.valueOf(NEG))) {
            return NEGATION;
        } else {
            return new Negation(s);
        }
    }

    public static Negation newInstance(char c) {
        if (c != NEG) {
            throw new InvalidSymbolException(String.format("Given character is not negation: \"%s\", should be " +
                    "\"%s\"", c, NEG));
        }
        return NEGATION;
    }

    @Override
    public Node toNode() {
        return new NegNode(this);
    }

    @Override
    public String getRaw() {
        return negation;
    }

    @Override
    public String getFull() {
        return negation;
    }

    @Override
    public String getUnprocessed() {
        return unprocessed_str;
    }
}
