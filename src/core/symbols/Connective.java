package core.symbols;

import core.exceptions.InvalidSymbolException;

public class Connective extends Symbol {

    private final static Connective SINGLETON_IMPLIES;
    private final static Connective SINGLETON_IFF;
    private final static Connective SINGLETON_AND;
    private final static Connective SINGLETON_OR;

    static {
        SINGLETON_IMPLIES = new Connective(IMPLIES);
        SINGLETON_IFF = new Connective(IFF);
        SINGLETON_AND = new Connective(AND);
        SINGLETON_OR = new Connective(OR);
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

        if (OR.equals(str)) this.connective = OR;
        else if (AND.equals(str)) this.connective = AND;
        else if (IMPLIES.equals(str)) this.connective = IMPLIES;
        else if (IFF.equals(str)) this.connective = IFF;
        else
            throw new InvalidSymbolException(String.format("Connective \"%s\" is not recognised, use %s | %s | %s | " +
                    "%s instead", str, OR, AND, IMPLIES, IFF));

    }

    public static Connective factory(String str) {
        if (str == null) {
            throw new InvalidSymbolException("Connective can't be null");
        }

        // remove white spaces
        str = str.chars().filter(c -> c != ' ').collect(StringBuilder::new, StringBuilder::appendCodePoint,
                StringBuilder::append).toString();

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

    public static Connective factory(char c) {
        return factory(Character.toString(c));
    }

    @Override
    public String toString() {
        return this.connective;
    }
}
