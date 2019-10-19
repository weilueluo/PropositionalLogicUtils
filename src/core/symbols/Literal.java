package core.symbols;

import core.exceptions.InvalidSymbolException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Literal extends Symbol {

    private final static Literal SINGLETON_TAUTOLOGY;
    private final static Literal SINGLETON_NEGATED_TAUTOLOGY;
    private final static Literal SINGLETON_CONTRADICTION;
    private final static Literal SINGLETON_NEGATED_CONTRADICTION;

    static {
        // create singletons
        SINGLETON_TAUTOLOGY = new Literal(TAUTOLOGY, false);
        SINGLETON_NEGATED_TAUTOLOGY = new Literal(TAUTOLOGY, true);
        SINGLETON_CONTRADICTION = new Literal(CONTRADICTION, false);
        SINGLETON_NEGATED_CONTRADICTION = new Literal(CONTRADICTION, true);

        // assign values
        SINGLETON_TAUTOLOGY.assign(true);
        SINGLETON_NEGATED_CONTRADICTION.assign(true);
        SINGLETON_CONTRADICTION.assign(false);
        SINGLETON_NEGATED_TAUTOLOGY.assign(false);

        // assign metadata
        SINGLETON_TAUTOLOGY.isTautology = SINGLETON_NEGATED_CONTRADICTION.isTautology = true;
        SINGLETON_TAUTOLOGY.isContradiction = SINGLETON_NEGATED_CONTRADICTION.isContradiction = false;
        SINGLETON_CONTRADICTION.isTautology = SINGLETON_NEGATED_TAUTOLOGY.isTautology = false;
        SINGLETON_CONTRADICTION.isContradiction = SINGLETON_NEGATED_TAUTOLOGY.isContradiction = true;

        // set isAssigned
        SINGLETON_TAUTOLOGY.isAssigned = SINGLETON_CONTRADICTION.isAssigned =
                SINGLETON_NEGATED_CONTRADICTION.isAssigned = SINGLETON_NEGATED_TAUTOLOGY.isAssigned = true;
    }

    private String rawLiteral, fullLiteral;
    private boolean isNegated, isAssigned, truthValue, isTautology, isContradiction;

    private Literal() {
    } // empty private constructor

    /**
     * The core literal constructor for string
     *
     * @param rawValidFormula the literal in String type, must be valid, non-tautology, non-contradiction and striped
     * @param isNegated       if this literal is negated
     */
    private Literal(String rawValidFormula, boolean isNegated) {

        this.rawLiteral = rawValidFormula;

        // full literal is literal without removing negated symbol if exists
        if (isNegated) {
            this.fullLiteral = NEG + rawValidFormula;
        } else {
            this.fullLiteral = rawValidFormula;
        }

        this.isNegated = isNegated;
        this.isAssigned = this.isTautology = this.isContradiction = false;

    }

    /**
     * This method return a Literal Object if success
     *
     * @param str
     * @return Literal Object
     * @throws InvalidSymbolException if null/empty/negation only/invalid character
     */
    @NotNull
    @Contract("null -> fail")
    public static Literal factory(String str) throws InvalidSymbolException {
        // check null
        if (str == null) {
            throw new InvalidSymbolException("Null is not a valid literal");
        }

        // remove all spaces
        str = str.replaceAll("\\s", "");

        // check empty
        if (str.isEmpty()) {
            throw new InvalidSymbolException("Given literal is empty");
        }

        // parse negation
        boolean isNegated = false;
        while (str.startsWith(NEG)) {
            isNegated = !isNegated;
            str = str.substring(NEG.length());
        }

        // str will be raw here

        // check if contains invalid character in raw literal
        if (!isAllLetters(str)) {
            throw new InvalidSymbolException(String.format("Raw literal must contains letters only, not \"%s\"", str));
        }

        // return singleton if tautology/contradiction
        if (str.equals(TAUTOLOGY)) {
            if (isNegated) {
                return SINGLETON_NEGATED_TAUTOLOGY;
            } else {
                return SINGLETON_TAUTOLOGY;
            }
        } else if (str.equals(CONTRADICTION)) {
            if (isNegated) {
                return SINGLETON_NEGATED_CONTRADICTION;
            } else {
                return SINGLETON_CONTRADICTION;
            }
        }

        return new Literal(str, isNegated);
    }

    @Contract("null -> false")
    private static boolean isAllLetters(String str) {

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

    // this will override previous value if it is not tautology or contradiction
    public void assign(boolean value) {

        if (this.isTautology || this.isContradiction) {
            throw new IllegalStateException(
                    "Assign value to " + (this.isTautology ? "tautology" : "contradiction") + " literal");
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

}