import io.formatting.FormatException;
import io.parsing.ParseException;
import model.expression.Expression;
import io.parsing.ExpressionParser;
import io.formatting.ExpressionPrefixFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TaskA {
    public static void main(String[] args) throws IOException, ParseException, FormatException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        Expression expression = ExpressionParser.parse(input);
        String output = ExpressionPrefixFormatter.format(expression);
        System.out.println(output);
    }
}
