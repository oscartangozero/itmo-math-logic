package model.proof;

import model.expression.Expression;

import java.util.List;
import java.util.Objects;

public class AxiomStatement extends ProofStatement {
    private final int axiomId;
    private final Expression expression;

    public static AxiomStatement tryMatch(Expression expression) {
        int axiomId = AxiomSet.STANDARD.findMatch(expression);
        return (axiomId != 0) ? new AxiomStatement(axiomId, expression) : null;
    }

    private AxiomStatement(int axiomId, Expression expression) {
        this.axiomId = axiomId;
        this.expression = expression;
    }

    public AxiomStatement(int axiomId, Expression... slots) {
        this.axiomId = axiomId;
        this.expression = AxiomSet.STANDARD.getExpanded(axiomId, slots);
    }

    public int getAxiomId() {
        return axiomId;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    void collectExpressions(List<Expression> expressions) {
        expressions.add(getExpression());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AxiomStatement that = (AxiomStatement) o;
        return getAxiomId() == that.getAxiomId() && getExpression().equals(that.getExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAxiomId(), getExpression());
    }

    @Override
    public String toString() {
        return "Axiom[" + getAxiomId() + "](" + getExpression() + ")";
    }
}
