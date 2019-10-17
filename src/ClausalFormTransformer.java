import exceptions.InvalidFormulaException;
import symbols.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ClausalFormTransformer {

    private final String original_formula = null;

    private final String transformed_formula = null;

    public ClausalFormTransformer(String formula) throws InvalidFormulaException {

        if (formula == null) {
            throw new InvalidFormulaException("%s is not a valid formula".format(formula));
        }

        String[] tokens = formula.split(" +");

        List<Symbol> symbols = new ArrayList<>();
        // TODO
    }
}