package core.symbols;

import core.exceptions.InvalidSymbolException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Literal extends Symbol {

    private String rawLiteral, fullLiteral, unprocessedLiteral;
    private boolean isNegated;
    private Boolean rawLiteralTruthValue, isRawTautology;

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

        if (rawLiteral.equals(Character.toString(TAUTOLOGY))) {
            this.isRawTautology = this.rawLiteralTruthValue = true;

        } else if (rawLiteral.equals(Character.toString(CONTRADICTION))) {
            this.isRawTautology = this.rawLiteralTruthValue = false;

        } else {
            this.isRawTautology = this.rawLiteralTruthValue = null;
        }
    }

    /*
        FACTORY METHODS BELOW
     */


    /**
     * This method return a Literal Object if given string is a valid representation of literal
     * This method sanitize input string before passing to actually constructor
     * Complexity is O(n)
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
            throw new InvalidSymbolException("Given Literal is null");
        }

        // parse negations and brackets
        boolean isNegated = false;

        // convert to array first instead of charAt due to extra index check in charAt, small string toCharArray
        // should be faster
        char[] chars = str.toCharArray();

        // for looping, this should later point to start of raw literal
        int startPointer = 0;

        // used to check if ends with right bracket, this should later point to end of raw literal
        int endPointer = chars.length - 1;

        // loop to ensure multiple brackets/negation sign are checked and removed
        while (startPointer < chars.length) {
            if (chars[startPointer] == ' ') {  // ignore space
                startPointer++;
            }

            // if starts with negation then negate the negation sign and increase start pointer
            else if (chars[startPointer] == NEG) {
                isNegated = !isNegated;
                startPointer += 1;
            }

            // then we check if starts with left bracket

            // if starts with left bracket, we check if ends with right bracket and increase pointer
            else if (chars[startPointer] == LBRACKET) {

                // then we check if ends with right bracket

                // skip spaces first
                while (endPointer > startPointer && chars[endPointer] == ' ') endPointer--;

                // throw exception if not encountered right bracket
                if (chars[endPointer] != RBRACKET) {
                    throw new InvalidSymbolException(String.format("Bracket is not enclosed properly for Literal " +
                            "\"%s\"", str));
                }

                // remove brackets
                startPointer += 1;
                endPointer -= 1;

            } // if (startsWithBracket)

            // if we reach here that means starts neither with negation nor right bracket
            // so we can terminate the loop
            else break;
        } // while (startPointer < chars.length)

        // remove trailing space
        while (endPointer > 0 && chars[endPointer] == ' ') endPointer--;

        // check empty
        if (endPointer < startPointer) {
            throw new InvalidSymbolException(String.format("Raw form is blank for literal \"%s\"", str));
        }

        /*
         * raw literal will be between pointers here
         */

        // get the raw literal, before checks if it is valid
        // so that we can easily print it if not valid
        String rawLiteral = String.valueOf(chars, startPointer, endPointer - startPointer + 1);

        // now we check if the raw string characters are letters
        for (int i = startPointer; i <= endPointer; i++) {
            if (!Character.isLetter(chars[i])) {
                // exception if not letter
                throw new InvalidSymbolException(String.format("Raw form of literal \"%s\" must contains letters " +
                        "only, not \"%s\"", str, rawLiteral));
            }
        }

        // everything checked, return Literal instance
        return new Literal(str, rawLiteral, isNegated);
    }

    @NotNull
    @Contract("_ -> new")
    public static Literal factory(char c) {
        if (Character.isLetter(c)) {
            String s = Character.toString(c);
            return new Literal(s, s, false);
        } else {
            throw new InvalidSymbolException(String.format("Raw literal must be letters, not \"%s\"", c));
        }
    }

    /*
        GETTER METHODS BELOW
     */

    public String getRaw() {
        return this.rawLiteral;
    }

    public String getFull() {
        return this.fullLiteral;
    }

    public String getUnprocessed() {
        return this.unprocessedLiteral;
    }

    public boolean getTruthValue() {
        if (this.rawLiteralTruthValue == null) {
            throw new IllegalStateException(String.format("Access truth value before assignment for literal: \"%s\"",
                    this.unprocessedLiteral));
        }
        return this.isNegated != this.rawLiteralTruthValue;  // same as isNegated ? !value : value;
    }

    @Nullable
    @Contract(pure = true)
    private Boolean getTruthValueOrNull() {
        if (this.rawLiteralTruthValue == null) return null;
        else return this.isNegated != this.rawLiteralTruthValue;
    }

    public void invertNegation() {
        this.isNegated = !this.isNegated;
    }

    public boolean isNegated() {
        return this.isNegated;
    }

    public boolean isTautology() {
        if (this.isRawTautology == null) return false;
        return this.isNegated ? !this.isRawTautology : this.isRawTautology;
    }

    public boolean isContradiction() {
        if (this.isRawTautology == null) return false;
        return this.isNegated ? this.isRawTautology : !this.isRawTautology;
    }

    public boolean isAssigned() {
        return this.rawLiteralTruthValue != null;
    }

    public String toString() {
        return String.format("Unprocessed String: %s,%nFull literal: %s,%nRaw literal: %s,%nNegated: %s,%n" +
                        "Tautology: %s,%nContradiction: %s,%nAssigned raw value: %s,%nTruth value: %s",
                this.unprocessedLiteral, this.fullLiteral, this.rawLiteral, this.isNegated(), this.isTautology(),
                this.isContradiction(), this.rawLiteralTruthValue, this.getTruthValueOrNull());
    }

    /*
        SETTER METHODS BELOW
     */


    // this will override previous value if it is not tautology or contradiction
    // else fails
    public void assign(boolean value) {
        if (this.isTautology() || this.isContradiction()) {
            throw new IllegalStateException(
                    "Assign value to " + (this.isTautology() ? "tautology" : "contradiction") + " literal");
        }
        this.rawLiteralTruthValue = value;
    }

    // override previous raw literal value if it is not tautology/contradiction
    // else stays the same
    public void assignOrUnchanged(boolean value) {
        if (!this.isTautology() && !this.isContradiction()) {
            this.rawLiteralTruthValue = value;
        }
    }


    /*
        COMPARATOR METHODS BELLOW
     */


    public boolean rawEquals(Literal other) {
        if (other == null) throw new InvalidSymbolException("Given other literal for rawEquals is null");
        else return this.rawLiteral.equals(other.rawLiteral);
    }

    public boolean fullEquals(Literal other) {
        if (other == null) throw new InvalidSymbolException("Given literal for rawEquals is null");
        else return this.fullLiteral.equals(other.fullLiteral);
    }

    public boolean equals(Literal other) {
        if (other == null) throw new InvalidSymbolException("Given literal for equals is null");
        else return this.rawLiteral.equals(other.rawLiteral)
                && this.isNegated == other.isNegated
                && this.getTruthValueOrNull() == other.getTruthValueOrNull();
    }

}