package core.symbols;

import core.exceptions.InvalidSymbolException;
import core.trees.ConnNode;
import core.trees.Node;

import java.util.Objects;

import static core.common.Utilities.stripAllSpaces;

public class Connective extends Symbol {

    public enum Type { IMPLIES, IFF, AND, OR }

    private final static Connective SINGLETON_IMPLIES;
    private final static Connective SINGLETON_IFF;
    private final static Connective SINGLETON_AND;
    private final static Connective SINGLETON_OR;
    private final String unprocessed_str;
    private final int precedence;
    private int hashcode;
    private Type type;

    static {
        SINGLETON_IFF = new Connective(IFF);
        SINGLETON_IMPLIES = new Connective(IMPLIES);
        SINGLETON_OR = new Connective(OR);
        SINGLETON_AND = new Connective(AND);
    }

    private String connective;

    private Connective(String str) throws InvalidSymbolException {

        if (str == null) {
            throw new InvalidSymbolException("Connective can't be null");
        }

        this.unprocessed_str = str;

        // remove all whitespaces
        str = str.chars().filter(c -> c != ' ').collect(StringBuilder::new, StringBuilder::appendCodePoint,
                StringBuilder::append).toString();

        if (str.isEmpty()) {
            throw new InvalidSymbolException("Connective can't be empty");
        }

        switch (str) {
            case OR:
                this.connective = OR;
                this.precedence = 2;
                type = Type.OR;
                break;
            case AND:
                this.connective = AND;
                this.precedence = 3;
                type = Type.AND;
                break;
            case IMPLIES:
                this.connective = IMPLIES;
                this.precedence = 4;
                type = Type.IMPLIES;
                break;
            case IFF:
                this.connective = IFF;
                this.precedence = 5;
                type = Type.IFF;
                break;
            default:
                throw new InvalidSymbolException(String.format("Connective \"%s\" is not recognised, use %s | %s | %s" +
                        " | %s instead", str, OR, AND, IMPLIES, IFF));
        }

        hashcode = Objects.hashCode(getFull());
    }

    public Type getType() {
        return type;
    }

    public static Connective newInstance(String str) {
        if (str == null) {
            throw new InvalidSymbolException("Connective can't be null");
        }

        // remove white spaces
        str = stripAllSpaces(str);

        if (str.isEmpty()) {
            throw new InvalidSymbolException("Connective can't be empty");
        }

        if (OR.equals(str)) return SINGLETON_OR;
        if (AND.equals(str)) return SINGLETON_AND;
        if (IMPLIES.equals(str)) return SINGLETON_IMPLIES;
        if (IFF.equals(str)) return SINGLETON_IFF;

        throw new InvalidSymbolException(String.format("Connective \"%s\" is not recognised %n use %s | %s | %s | %s"
                , str, OR, AND, IMPLIES, IFF));

    }

    public int getPrecedence() {
        return precedence;
    }

    @Override
    public String toString() {
        return this.connective;
    }

    @Override
    public Node toNode() {
        return new ConnNode(this);
    }

    @Override
    public String getRaw() {
        return connective;
    }

    @Override
    public String getFull() {
        return getRaw();
    }

    @Override
    public String getUnprocessed() {
        return this.unprocessed_str;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Connective) {
            return hashcode == other.hashCode();
        } else return false;
    }
}
