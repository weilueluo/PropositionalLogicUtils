package symbols;

import exceptions.InvalidSymbolException;

public class Variable implements Symbol {

    private String var;

    public Variable(String str) throws InvalidSymbolException {
        if (!isAllLetters(str)) {
            throw new InvalidSymbolException("\"%s\" is not a valid propositional variable".format(str));
        }
        this.var = str;
    }

    private boolean isAllLetters(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(String other) {
        return this.var.equals(other);
    }

    @Override
    public String toString() {
        return this.var;
    }
}
