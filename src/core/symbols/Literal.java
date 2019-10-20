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
        SINGLETON_TAUTOLOGY = new Literal(TAUTOLOGY, TAUTOLOGY, false);
        SINGLETON_NEGATED_TAUTOLOGY = new Literal(TAUTOLOGY, TAUTOLOGY, true);
        SINGLETON_CONTRADICTION = new Literal(CONTRADICTION, CONTRADICTION, false);
        SINGLETON_NEGATED_CONTRADICTION = new Literal(CONTRADICTION, CONTRADICTION, true);

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

    private String rawLiteral, fullLiteral, unprocessedLiteral;
    private boolean isNegated, isAssigned, truthValue, isTautology, isContradiction;

    private Literal() {
    } // empty private constructor

    /**
     * The core literal constructor for string
     *
     * @param rawLiteral the literal in String type, must be valid, non-tautology, non-contradiction and striped
     * @param isNegated  if this literal is negated
     */
    private Literal(String unprocessedLiteral, String rawLiteral, boolean isNegated) {

        this.unprocessedLiteral = unprocessedLiteral;
        this.rawLiteral = rawLiteral;

        // full literal is literal without removing negated symbol if exists
        if (isNegated) {
            this.fullLiteral = NEG + rawLiteral;
        } else {
            this.fullLiteral = rawLiteral;
        }

        this.isNegated = isNegated;
        this.isAssigned = this.isTautology = this.isContradiction = false;
    }

    /**
     * This method return a Literal Object if parsing passed
     * This method sanitize input string before passing to actually constructor
     *
     * @param str the input literal string
     * @return Literal Object
     * @throws InvalidSymbolException if null/empty/negation only/un-closed bracket/invalid character
     */
    @NotNull
    @Contract("null -> fail")
    public static Literal factory(String str) throws InvalidSymbolException {
        // check null
        if (str == null) {
            throw new InvalidSymbolException("Null is not a valid literal");
        }

        String unprocessedStr = str;

        // remove space
        str = str.strip();

        // check empty
        if (str.isEmpty()) {
            throw new InvalidSymbolException("Given literal is blank");
        }

        // now parse negations and brackets
        boolean isNegated = false;
        char[] chars = str.toCharArray();

        // for looping, this should later point to start of raw literal
        int startPointer = 0;

        // used to check if ends with right bracket, this should later point to end of raw literal
        int endPointer = chars.length - 1;

        // loop to ensure multiple brackets/negation sign are checked and removed
        while (startPointer < chars.length) {
            if (chars[startPointer] == ' ') {  // ignore space
                startPointer++;
                continue;
            }

            char[] negChars = NEG.toCharArray();
            // if starts with negation then negate the negation sign and increase start pointer
            if (charArrayStartsWith(chars, negChars, startPointer)) {
                isNegated = !isNegated;
                startPointer += negChars.length;
                continue;
            }

            // then we check if starts with left bracket
            char[] braChars = LBRACKET.toCharArray();

            // if starts with left bracket, we check if ends with right bracket and increase pointer
            if (charArrayStartsWith(chars, braChars, startPointer)) {

                // then we check if ends with right bracket
                while (endPointer > startPointer && chars[endPointer] == ' ') endPointer--; // skip spaces

                boolean endsWithRBracket = true;
                char[] rBraChars = RBRACKET.toCharArray();
                for (int i = rBraChars.length - 1; i >= 0; i--) {
                    if (endPointer - i < 0 || chars[endPointer - i] != rBraChars[i]) {
                        endsWithRBracket = false;
                        break;
                    }
                }

                // throw exception if not ends with right bracket
                if (!endsWithRBracket) {
                    throw new InvalidSymbolException(String.format("Literal \"%s\"\'s bracket is not enclosed properly",
                            unprocessedStr));
                }

                startPointer += braChars.length;
                endPointer -= rBraChars.length;
                continue;

            } // if (startsWithBracket)

            // if we reach here that means starts neither with negation nor right bracket
            // so we can terminate the loop
            break;
        } // while (startPointer < chars.length)

        // remove trailing space
        while(endPointer > 0 && chars[endPointer] == ' ') endPointer--;

        // check empty
        if (endPointer < startPointer) {
            throw new InvalidSymbolException(String.format("Literal \"%s\"\'s raw form is blank", unprocessedStr));
        }

        /*
         * raw literal will be between pointers here
         */

        String rawLiteral = String.valueOf(chars, startPointer, endPointer - startPointer + 1);

        // check if contains invalid character in raw literal
        if (!isAllLetters(rawLiteral)) {
            throw new InvalidSymbolException(String.format("Given literal \"%s\"\'s raw form must contains letters " +
                    "only, not \"%s\"", unprocessedStr, rawLiteral));
        }

        // return singleton if tautology/contradiction
        if (rawLiteral.equals(TAUTOLOGY)) {
            if (isNegated) {
                return SINGLETON_NEGATED_TAUTOLOGY;
            } else {
                return SINGLETON_TAUTOLOGY;
            }
        } else if (rawLiteral.equals(CONTRADICTION)) {
            if (isNegated) {
                return SINGLETON_NEGATED_CONTRADICTION;
            } else {
                return SINGLETON_CONTRADICTION;
            }
        }

        return new Literal(unprocessedStr, rawLiteral, isNegated);
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

    @Contract("null, _, _ -> fail; !null, null, _ -> fail")
    private static boolean charArrayStartsWith(char[] chars, char[] prefix, int startIndex) {
        if (chars == null || prefix == null) {
            throw new InvalidSymbolException("Given char array is null");
        }

        if (startIndex < 0) {
            throw new InvalidSymbolException(String.format("Given startIndex: \"%s\" is less than 0", startIndex));
        }

        if (chars.length - startIndex < prefix.length) {
            return false;
        }

        for (int i = 0; i < prefix.length; i++) {
            if (chars[startIndex + i] == ' ') {
                continue;
            }
            if (chars[startIndex + i] != prefix[i]) {
                return false;
            }
        }

        return true;
    }

    public String getRaw() {
        return this.rawLiteral;
    }

    public String getFull() {
        return this.fullLiteral;
    }

    public String getUnprocessedLiteral() {
        return this.unprocessedLiteral;
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

    private Boolean truthValueOrNull() {
        if (!this.isAssigned) return null;
        else return this.truthValue;
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
                && this.isNegated == other.isNegated
                && this.truthValueOrNull() == other.truthValueOrNull();
    }

    public String toString() {
        return String.format("Unprocessed String: %s, full literal: %s, raw literal: %s, negated: %s, " +
                        "tautology: %s, contradiction: %s, assigned: %s",
                this.unprocessedLiteral, this.fullLiteral, this.rawLiteral, this.isNegated, this.isTautology,
                this.isContradiction, this.isAssigned ? this.truthValue : "null");
    }

}