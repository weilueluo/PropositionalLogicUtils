package symbols;

import exceptions.InvalidSymbolException;
import org.jetbrains.annotations.Contract;

public class Literal extends Symbol {

    private String rawLiteral, fullLiteral;

    private boolean isTautology, isContradiction, isNegated, isAssigned, truthValue;

    /**
     * The literal constructor for string
     * <p>
     *
     * @param str the literal in String type
     * @throws InvalidSymbolException
     * if null or empty string or string containing not only letters
     */
    @Contract("null -> fail")
    public Literal(String str) throws InvalidSymbolException {

        // check null
        if (str == null) {
            throw new InvalidSymbolException("Null is not a valid literal");
        }

        // remove space
        str = str.strip();

        // check empty
        if (str.equals("")) {
            throw new InvalidSymbolException("Given literal is empty");
        }

        // check if starts with negation symbol
        this.isNegated = str.substring(0, NEG.length()).equals(NEG);

        // check if only contains negation symbol
        if (this.isNegated && str.length() == NEG.length()) {
            throw new InvalidSymbolException(String.format("Given literal contains only Negation Symbol: \"%s\"", NEG));
        }

        // full literal is literal without removing negated symbol if exists
        this.fullLiteral = str;

        // remove negated symbol to get raw literal
        if (this.isNegated) {
            str = str.substring(NEG.length());
        }
        this.rawLiteral = str;

        // raw literal should be letters only
        if (!isAllLetters(this.rawLiteral)) {
            throw new InvalidSymbolException(String.format("Raw literal should be letters only, not \"%s\"",
                    this.rawLiteral));
        }

        // set if tautology or contradiction
        if (this.rawLiteral.equals(TRUE)) {
            this.isTautology = !isNegated;  // tautology but negated?
            this.isContradiction = isNegated;
        } else if (this.rawLiteral.equals(FALSE)) {
            this.isTautology = isNegated;  // contradiction but negated?
            this.isContradiction = !isNegated;
        } else {
            this.isTautology = this.isContradiction = false;
        }

        // set value if it is tautology or contradiction
        if (this.isTautology || this.isContradiction) {
            this.isAssigned = true;
            this.truthValue = this.isTautology;
        }

    }

    // this will override previous value if it is not tautology or contradiction
    public void assign(boolean value) {

        if (this.isTautology || this.isContradiction) {
            throw new IllegalStateException("Assign value to " + (this.isTautology ? "tautology" : "contradiction") + " literal");
        }

        this.truthValue = this.isNegated != value;
        this.isAssigned = true;
    }

    public boolean truthValue() {
        if (!this.isAssigned) {
            throw new IllegalStateException(String.format("Access truth value before assignment for literal: \"%s\"",
                    this.fullLiteral));
        }

        return this.truthValue;
    }

    public boolean isContradiction() {
        return this.isContradiction;
    }

    public boolean isTautology() {
        return this.isTautology;
    }

    public boolean isNegated() {
        return this.isNegated;
    }

    public boolean rawEquals(Literal other) {
        return this.rawLiteral.equals(other.rawLiteral);
    }

    public boolean equals(Literal other) {
        return this.rawLiteral.equals(other.rawLiteral)
                && this.isNegated == other.isNegated;
    }

    public String toString() {
        return String.format("Full literal: %s, raw literal: %s, negated: %s, " +
                        "tautology: %s, contradiction: %s, assigned: %s",
                this.fullLiteral, this.rawLiteral, this.isNegated, this.isTautology,
                this.isContradiction, this.isAssigned ? this.truthValue : "null");
    }

    @Contract("null -> false")
    private boolean isAllLetters(String str) {

        if (str == null) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

}