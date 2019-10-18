package symbols;

import exceptions.InvalidSymbolException;

public class Connective extends Symbol {

    private String connective;

    public Connective(String str) throws InvalidSymbolException {
        if (str == null) {
            throw new InvalidSymbolException("\"%s\" is not a valid connective".format(str));
        }

        str = str.strip();

        if (!isValidConnective(str)) {
            throw new InvalidSymbolException("%s is not a valid connective".format(str));
        }
    }

    private boolean isValidConnective(String str) {
        return str.equals(IMPLIES) || str.equals(AND)
                || str.equals(OR) || str.equals(IFF);
    }

    @Override
    public String toString() {
        return this.connective.toString();
    }
}
