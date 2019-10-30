package core.symbols;

import core.exceptions.InvalidNodeException;
import core.exceptions.InvalidSymbolException;
import core.trees.Node;

import static core.common.Utilities.stripAllSpaces;


public class Bracket extends Symbol {

    public static Bracket LEFT_BRACKET;
    public static Bracket RIGHT_BRACKET;

    static {
        LEFT_BRACKET = new Bracket(LBRACKET);
        RIGHT_BRACKET = new Bracket(RBRACKET);
    }

    private String bracket;
    private String unprocessed_str;

    private Bracket(char c) {
        if (c == LBRACKET || c == RBRACKET) {
            this.bracket = this.unprocessed_str = String.valueOf(c);
        } else {
            throw new InvalidSymbolException(String.format("Character is not one of \"%s\" or \"%s\"", LBRACKET,
                    RBRACKET));
        }
    }
    private Bracket(String str) {
        if (str == null) throw new InvalidSymbolException("Null is not bracket");
        this.unprocessed_str = str;

        str = stripAllSpaces(str);

        if (str.length() != 1) {
            throw new InvalidSymbolException("Invalid bracket: " + str);
        }

        char c = str.charAt(0);
        if (c != LBRACKET && c != RBRACKET) {
            throw new InvalidSymbolException(String.format("Character is not one of \"%s\" or \"%s\"", LBRACKET,
                    RBRACKET));
        }
        this.bracket = str;
    }

    public static Bracket newInstance(char c) {
        if (c == LBRACKET) return LEFT_BRACKET;
        else if (c == RBRACKET) return RIGHT_BRACKET;
        else throw new InvalidSymbolException("Character given is not a bracket");
    }

    public static Bracket newInstance(String str) {
        if (str.equals(String.valueOf(LBRACKET))) return LEFT_BRACKET;
        else if (str.equals(String.valueOf(RBRACKET))) return RIGHT_BRACKET;
        else return new Bracket(str);
    }

    @Override
    public Node toNode() {
        throw new InvalidNodeException("Bracket is just a flag for BoxNode, can't transform to Node");
    }

    @Override
    public String getRaw() {
        return bracket;
    }

    @Override
    public String getFull() {
        return bracket;
    }

    @Override
    public String getUnprocessed() {
        return unprocessed_str;
    }

}
