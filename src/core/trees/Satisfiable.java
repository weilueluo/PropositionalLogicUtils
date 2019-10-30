package core.trees;

import core.symbols.Literal;

import java.util.Map;

public interface Satisfiable {
    boolean isSatisfiable(Map<Literal, Boolean> interpretation, boolean truth_value);
}
