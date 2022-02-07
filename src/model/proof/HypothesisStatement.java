package model.proof;

import model.expression.Expression;

import java.util.List;
import java.util.Objects;

public class HypothesisStatement extends ProofStatement {
    private final Expression hypothesis;

    /* Assumes expression is not null */
    HypothesisStatement(Expression hypothesis) {
        this.hypothesis = hypothesis;
    }

    public Expression getExpression() {
        return hypothesis;
    }

    @Override
    void collectExpressions(List<Expression> expressions) {
        expressions.add(getExpression());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HypothesisStatement that = (HypothesisStatement) o;
        return hypothesis.equals(that.hypothesis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hypothesis);
    }

    @Override
    public String toString() {
        return "Hypothesis(" + getExpression() + ")";
    }
}
