package core.symbols;

public abstract class Symbol {

    public static String TAUTOLOGY = "T",
                         CONTRADICTION = "F",
                         NEG = "~",
                         LBRACKET = "(",
                         RBRACKET = ")",
                         IMPLIES = "->",
                         IFF = "<->",
                         AND = "/\\",
                         OR = "\\/";

    public Symbol() {
    } // empty
}