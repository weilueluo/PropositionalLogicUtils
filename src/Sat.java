import core.Parser;
import core.exceptions.InvalidFormulaException;
import core.symbols.Literal;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Sat {

    public static void main(String[] args) {

        // parse input
        if (args.length != 1) {
            throw new InvalidFormulaException("No/More than one Formula(s) given");
        }
        String user_input = args[0];
        Parser parser = new Parser();
        Map<Literal, Boolean> interpretation = new HashMap<Literal, Boolean>();

        // evaluate
        Instant start = Instant.now();
        boolean satisfiable = parser.evaluate(user_input).getTree().isSatisfiable(interpretation, true);
        Instant end = Instant.now();

        System.out.println(String.format("The formula \"%s\" is %s", parser,
                satisfiable ? "satisfiable" : "unsatisfiable"));
        if (satisfiable) {
            System.out.println("One Possible Interpretation:");
            for (Map.Entry e : interpretation.entrySet()) {
                System.out.println("|- " + ((Literal) e.getKey()).getRaw() + " -> " + e.getValue());
            }
        }
        System.out.println((String.format("Runtime: %sms", Duration.between(start, end).toMillis())));

        System.out.println(parser.getTree().toString(0));

    }
}