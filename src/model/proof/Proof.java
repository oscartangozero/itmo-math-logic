package model.proof;

import model.expression.Expression;

import java.util.*;

public class Proof {
    private final HypothesisStatement[] hypotheses;
    private final ProofStatement statement;

    Proof(HypothesisStatement[] hypotheses, ProofStatement statement) {
        this.hypotheses = hypotheses;
        this.statement = statement;
    }

    public HypothesisStatement[] getHypotheses() {
        return hypotheses;
    }

    public ProofStatement getConsequentStatement() {
        return statement;
    }

    public Expression getThesis() {
        return getConsequentStatement().getExpression();
    }

    public List<Expression> getSentences() {
        List<Expression> statements = new ArrayList<>();
        getConsequentStatement().collectExpressions(statements);
        return statements;
    }
}
