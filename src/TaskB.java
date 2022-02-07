import io.parsing.ParseException;
import model.expression.*;
import io.parsing.ExpressionParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TaskB {
    public static void main(String[] args) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        String output = targetFunc(input);
        System.out.println(output);
    }

    private static String targetFunc(String input) throws ParseException {
        Map<String, Variable> variablesCache = new HashMap<>();
        Expression expression = ExpressionParser.parse(input, variablesCache);
        IterableVariableValues values = new IterableVariableValues(variablesCache.keySet());
        int trueCases = 0;
        int falseCases = 0;
        do {
            if (expression.evaluate(values)) trueCases++;
            else falseCases++;
        } while (values.increment());
        if (trueCases == 0) return "Unsatisfiable";
        if (falseCases == 0) return "Valid";
        return "Satisfiable and invalid, " + trueCases + " true and " + falseCases + " false cases";
    }
}
