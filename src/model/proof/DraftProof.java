package model.proof;

import model.expression.Expression;
import model.expression.Implication;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DraftProof {
    private final HypothesisStatement[] hypothesesStatements;
    private final Expression thesis;
    private final Map<Expression, HypothesisStatement> hypothesesMap;
    private final Map<Expression, ProofStatement> modusPonensCandidates;
    private final LinkedHashMap<Expression, ProofStatement> consumedStatements;

    public DraftProof(List<Expression> hypotheses, Expression thesis) {
        this.hypothesesStatements = hypotheses.stream().map(HypothesisStatement::new).toArray(HypothesisStatement[]::new);
        this.hypothesesMap = new HashMap<>();
        for (int i = 0; i < hypotheses.size(); i++) hypothesesMap.put(hypotheses.get(i), hypothesesStatements[i]);
        this.modusPonensCandidates = new HashMap<>();
        this.consumedStatements = new LinkedHashMap<>();
        this.thesis = thesis;
    }

    public boolean tryAppendStatement(Expression expression) {
        if (consumedStatements.containsKey(expression)) return true;
        ProofStatement statement = annotateExpression(expression);
        if (statement == null) return false;
        appendStatement(expression, statement);
        return true;
    }

    private ProofStatement annotateExpression(Expression expression) {
        ProofStatement statement;
        statement = hypothesesMap.get(expression);              // O(1)
        if (statement != null) return statement;
        statement = tryConstructModusPonensFast(expression);    // O(1)
        if (statement != null) return statement;
        statement = AxiomStatement.tryMatch(expression);        // O(axioms number)
        if (statement != null) return statement;
        statement = tryConstructModusPonens(expression);        // O(statements number)
        return statement;
    }

    /* Has O(1) time complexity.
     * The conjecture: if a statement is derived from an implication, there is usually only one implication
     * from which it can be derived. Thus, in order to reduce the number of memory accesses and improve
     * overall performance, it makes sense to store reference only to the last such implication in a single hash map. */
    private ProofStatement tryConstructModusPonensFast(Expression expression) {
        ProofStatement conditionStatement = modusPonensCandidates.get(expression);
        if (conditionStatement == null) return null;
        Implication implication = (Implication) conditionStatement.getExpression();
        ProofStatement assertionStatement = consumedStatements.get(implication.getLeftOperand());
        if (assertionStatement == null) return null;
        return new ModusPonens(conditionStatement, assertionStatement);
    }

    /* Has O(number of statements) time complexity.
     * A straightforward approach: iterate over all expressions, check the presence of antecedent
     * for each candidate-for-modus-ponens implication. */
    private ProofStatement tryConstructModusPonens(Expression expression) {
        for (Expression statement : consumedStatements.keySet()) {
            if (statement instanceof Implication) {
                Implication implication = (Implication) statement;
                if (expression.equals(implication.getRightOperand())) {
                    ProofStatement assertion = consumedStatements.get(implication.getLeftOperand());
                    if (assertion != null) {
                        ProofStatement condition = consumedStatements.get(implication);
                        return new ModusPonens(condition, assertion);
                    }
                }
            }
        }
        return null;
    }

    /* Saves { expression : statement } pair to the map.
     * If the expression is implication `X -> Y`, also saves { `Y` : statement } to the modus-ponens-candidates map. */
    private void appendStatement(Expression expression, ProofStatement statement) {
        consumedStatements.put(expression, statement);
        if (expression instanceof Implication) {
            Expression consequent = ((Implication) expression).getRightOperand();
            modusPonensCandidates.put(consequent, statement);
        }
    }

    public Proof tryValidate() {
        ProofStatement consequentStatement = consumedStatements.get(thesis);
        return (consequentStatement == null) ? null : new Proof(hypothesesStatements, consequentStatement);
    }
}
