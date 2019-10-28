package core.symbols;

import core.trees.Node;

public abstract class Symbol {

    public final static String IMPLIES = "->";
    public final static String IFF = "<->";
    public final static String AND = "/\\";
    public final static String OR = "\\/";

    public final static char TAUTOLOGY = 'T';
    public final static char CONTRADICTION = 'F';
    public final static char NEG = '~';
    public final static char LBRACKET = '(';
    public final static char RBRACKET = ')';
    public final static char DASH = '-';
    public final static char LESS_THAN = '<';
    public final static char GREATER_THAN = '>';
    public final static char BACKWARD_SLASH = '\\';
    public final static char FORWARD_SLASH = '/';

    public Symbol() {
    } // empty

    public abstract Node toNode();

    public abstract String getRaw();

    public abstract String getFull();

    public abstract String getUnprocessed();

    public abstract int hashCode();

    public abstract boolean equals(Object other);
}