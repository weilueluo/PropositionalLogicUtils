package core.symbols;

import core.exceptions.InvalidSymbolException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Literal extends Symbol {

    private String rawLiteral, fullLiteral, unprocessedLiteral;
    private boolean isNegated, isTautology, isContradiction;
    private Boolean truthValue;

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
            this.isTautology = !isNegated;
            this.isContradiction = isNegated;
            this.truthValue = !isNegated;

        } else if (rawLiteral.equals(Character.toString(CONTRADICTION))) {
            this.isContradiction = !isNegated;
            this.isTautology = isNegated;
            this.truthValue = isNegated;

        } else {
            this.isContradiction = this.isTautology = false;
            this.truthValue = null;
        }
    }

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

        String unprocessedStr = str;


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
                    throw new InvalidSymbolException(String.format("Literal \"%s\"\'s bracket is not enclosed properly",
                            unprocessedStr));
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
            throw new InvalidSymbolException(String.format("Literal \"%s\"\'s raw form is blank", unprocessedStr));
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
                throw new InvalidSymbolException(String.format("Given literal \"%s\"\'s raw form must contains " +
                        "letters only, not \"%s\"", unprocessedStr, rawLiteral));
            }
        }

        // everything checked, return Literal instance
        return new Literal(unprocessedStr, rawLiteral, isNegated);
    }

    public static Literal factory(char c) {
        if (Character.isLetter(c)) {
            String s = Character.toString(c);
            return new Literal(s, s, false);
        } else {
            throw new InvalidSymbolException(String.format("Raw literal must be letters, not \"%s\"", c));
        }
    }

    public String getRaw() {
        return this.rawLiteral;
    }

    public String getFull() {
        return this.fullLiteral;
    }

    public String getUnprocessed() {
        return this.unprocessedLiteral;
    }

    // this will override previous value if it is not tautology or contradiction
    public void assign(boolean value) {

        if (this.isTautology || this.isContradiction) {
            throw new IllegalStateException(
                    "Assign value to " + (this.isTautology ? "tautology" : "contradiction") + " literal");
        }

        this.truthValue = this.isNegated != value;  // same as isNegated ? !value : value;
    }

    public boolean getTruthValue() {
        if (this.truthValue == null) {
            throw new IllegalStateException(String.format("Access truth value before assignment for literal: \"%s\"",
                    this.unprocessedLiteral));
        }
        return this.truthValue;
    }

    private Boolean getTruthValueOrNull() {
        return this.truthValue;
    }

    public boolean isNegated() {
        return this.isNegated;
    }

    public boolean isTautology() {
        return this.isTautology;
    }

    public boolean isContradiction() {
        return this.isContradiction;
    }

    public boolean isAssigned() {
        return this.truthValue != null;
    }

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

    public String toString() {
        return String.format("Unprocessed String: %s,%n full literal: %s,%n raw literal: %s,%n negated: %s,%n " +
                        "tautology: %s,%n contradiction: %s,%n assigned value: %s",
                this.unprocessedLiteral, this.fullLiteral, this.rawLiteral, this.isNegated, this.isTautology,
                this.isContradiction, this.truthValue);
    }

}