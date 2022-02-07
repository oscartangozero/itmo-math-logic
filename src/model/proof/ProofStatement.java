package model.proof;

import model.expression.Expression;

import java.util.List;

public abstract class ProofStatement {
    public abstract Expression getExpression();
    
    abstract void collectExpressions(List<Expression> expression);
}
