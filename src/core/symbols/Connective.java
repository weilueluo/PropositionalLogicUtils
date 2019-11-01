package core.symbols;

import core.exceptions.InvalidSymbolException;
import core.trees.ConnNode;
import core.trees.Node;

import static core.common.Utilities.stripAllSpaces;

public class Connective extends Symbol {

    public enum Type { IFF, AND, OR, IMPLIES }

    private final static Connective SINGLETON_IMPLIES;
    private final static Connective SINGLETON_IFF;
    private final static Connective SINGLETON_AND;
    private final static Connective SINGLETON_OR;
    private final int precedence;
    private final Type type;

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

        // remove all whitespaces
        str = str.chars().filter(c -> c != ' ').collect(StringBuilder::new, StringBuilder::appendCodePoint,
                StringBuilder::append).toString();

        if (str.isEmpty()) {
            throw new InvalidSymbolException("Connective can't be empty");
        }

        switch (str) {
            case OR:
                this.type = Type.OR;
                this.connective = OR;
                this.precedence = 2;
                break;
            case AND:
                this.type = Type.AND;
                this.connective = AND;
                this.precedence = 3;
                break;
            case IMPLIES:
                this.type = Type.IMPLIES;
                this.connective = IMPLIES;
                this.precedence = 4;
                break;
            case IFF:
                this.type = Type.IFF;
                this.connective = IFF;
                this.precedence = 5;
                break;
            default:
                throw new InvalidSymbolException(String.format("Connective \"%s\" is not recognised, use %s | %s | %s" +
                        " | %s instead", str, OR, AND, IMPLIES, IFF));
        }

    }

    public static Connective getImpliesInstance() {
        return SINGLETON_IMPLIES;
    }

    public static Connective getIffInstance() {
        return SINGLETON_IFF;
    }

    public static Connective getAndInstance() {
        return SINGLETON_AND;
    }

    public static Connective getOrInstance() {
        return SINGLETON_OR;
    }

    public static Connective getInstance(Type type) {
        switch (type) {
            case OR:
                return SINGLETON_OR;
            case AND:
                return SINGLETON_AND;
            case IMPLIES:
                return SINGLETON_IMPLIES;
            case IFF:
                return SINGLETON_IFF;
            default:
                throw new IllegalStateException("Unrecognised connective type");
        }
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

    public Type getType() {
        return type;
    }
}
