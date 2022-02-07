package model.proof;

import model.expression.Expression;
import model.expression.Implication;

import java.util.List;
import java.util.Objects;

public class ModusPonens extends ProofStatement {
    private final ProofStatement condition;
    private final ProofStatement assertion;

    public ModusPonens(ProofStatement condition, ProofStatement assertion) {
        if (!(condition.getExpression() instanceof Implication)) throw new IllegalArgumentException();
        Implication conditionExpr = (Implication) condition.getExpression();
        if (!assertion.getExpression().equals(conditionExpr.getLeftOperand())) throw new IllegalArgumentException();
        this.condition = condition;
        this.assertion = assertion;
    }

    public ProofStatement getConditionStatement() {
        return condition;
    }

    public ProofStatement getAssertionStatement() {
        return assertion;
    }

    public Expression getAntecedent() {
        return assertion.getExpression();
    }

    public Expression getConsequent() {
        return ((Implication) condition.getExpression()).getRightOperand();
    }

    @Override
    public Expression getExpression() {
        return getConsequent();
    }

    @Override
    void collectExpressions(List<Expression> expressions) {
        getConditionStatement().collectExpressions(expressions);
        getAssertionStatement().collectExpressions(expressions);
        expressions.add(getExpression());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModusPonens that = (ModusPonens) o;
        return condition.equals(that.condition) && assertion.equals(that.assertion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, assertion);
    }

    @Override
    public String toString() {
        return "ModusPonens(" + getConditionStatement() + "," + getAssertionStatement() + ")";
    }
}
