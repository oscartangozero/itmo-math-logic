package model.proof;

import io.parsing.ExpressionParser;
import model.expression.Expression;

import java.util.Arrays;
import java.util.Iterator;

public class AxiomSet implements Iterable<Expression> {
    public static final AxiomSet STANDARD;

    static {
        String[] axioms = {
                "A->B->A",
                "(A->B)->(A->B->C)->(A->C)",
                "A->B->A&B",
                "A&B->A", "A&B->B",
                "A->A|B", "B->A|B",
                "(A->C)->(B->C)->(A|B->C)",
                "(A->B)->(A->!B)->!A",
                "!!A->A"};
        Expression[] axiomExpressions = Arrays.stream(axioms).map(ExpressionParser::tryParse).toArray(Expression[]::new);
        if (Arrays.asList(axiomExpressions).contains(null))
            throw new IllegalStateException("Failed to construct standard axiom set");
        STANDARD = new AxiomSet(axiomExpressions);
    }

    private final Expression[] axioms;

    public AxiomSet(Expression... axioms) {
        this.axioms = axioms;
    }

    public int size() {
        return axioms.length;
    }

    public Expression get(int index) {
        return axioms[index - 1];
    }

    public Expression getExpanded(int index, Expression... slots) {
        return get(index).expand(new AxiomVariableSlots(slots));
    }

    public int findMatch(Expression expr) {
        for (int i = 1; i <= size(); i++) {
            if (get(i).mapsTo(expr)) return i;
        }
        return 0;
    }

    @Override
    public Iterator<Expression> iterator() {
        return Arrays.stream(axioms).iterator();
    }
}
