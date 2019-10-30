package core.symbols;

public abstract class Symbol {

    public final static String IMPLIES = "->",
                                IFF = "<->",
                                AND = "/\\",
                                OR = "\\/";

    public final static char TAUTOLOGY = 'T',
                            CONTRADICTION = 'F',
                            NEG = '~',
                            LBRACKET = '(',
                            RBRACKET = ')';

    public Symbol() {
    } // empty
}