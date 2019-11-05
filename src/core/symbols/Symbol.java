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

    public final static int LITERAL_PRECEDENCE = 0;
    public final static int BRACKET_PRECEDENCE = 1;
    public final static int NEGATION_PRECEDENCE = 2;
    public final static int AND_PRECEDENCE = 3;
    public final static int OR_PRECEDENCE = 4;
    public final static int IMPLIES_PRECEDENCE = 5;
    public final static int IFF_PRECEDENCE = 6;
    public final static int INITIAL_PRECEDENCE = 7;

    public Symbol() {
    } // empty

    public abstract Node toNode();

    public abstract String getRaw();

    public abstract String getFull();
}