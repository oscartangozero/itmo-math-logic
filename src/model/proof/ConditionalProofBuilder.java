package model.proof;

import model.expression.Expression;
import model.expression.Implication;

import java.util.*;

public class ConditionalProofBuilder {
    private Map<ProofStatement, ProofStatement> generatedConditionals;

    /* Builds a proof of [h1 h2 |- H -> T] from a given [h1 H h2 |- T] */
    public static Proof build(Proof proof, int conditionHypothesisIndex) {
        return new ConditionalProofBuilder().buildConditionalProof(proof, conditionHypothesisIndex);
    }

    private Proof buildConditionalProof(Proof proof, int conditionHypothesisIndex) {
        this.generatedConditionals = new HashMap<>();
        Expression conditionHypothesis = proof.getHypotheses()[conditionHypothesisIndex].getExpression();
        ProofStatement statement = buildConditionalStatement(proof.getConsequentStatement(), conditionHypothesis);
        HypothesisStatement[] hypotheses = buildHypotheses(proof.getHypotheses(), conditionHypothesisIndex);
        return new Proof(hypotheses, statement);
    }

    private static HypothesisStatement[] buildHypotheses(HypothesisStatement[] former, int excludeIndex) {
        int hypothesesSize = former.length - 1;
        HypothesisStatement[] hypotheses = new HypothesisStatement[hypothesesSize];
        System.arraycopy(former, 0, hypotheses, 0, excludeIndex);
        System.arraycopy(former, excludeIndex + 1, hypotheses, excludeIndex, hypothesesSize - excludeIndex);
        return hypotheses;
    }

    /* Calls generateConditionalStatement, caches the result */
    private ProofStatement buildConditionalStatement(ProofStatement statement, Expression hypothesis) {
        ProofStatement conditional = generatedConditionals.get(statement);
        if (conditional != null) return conditional;
        conditional = generateConditionalStatement(statement, hypothesis);
        generatedConditionals.put(statement, conditional);
        return conditional;
    }

    /* Generates a ProofStatement of [h |- H -> X] from a given [h, H |- X] */
    private ProofStatement generateConditionalStatement(ProofStatement statement, Expression hypothesis) {
        if (statement instanceof ModusPonens) {
            return generateConditionalModusPonens((ModusPonens) statement, hypothesis);
        }
        if (statement instanceof AxiomStatement) {
            return generatePlainConditionalStatement(statement, hypothesis);
        }
        if (statement instanceof HypothesisStatement) {
            return (statement.getExpression().equals(hypothesis))
                    ? generateSelfImplicationStatement(hypothesis)
                    : generatePlainConditionalStatement(statement, hypothesis);
        }
        throw new IllegalArgumentException("Invalid ProofStatement: " + statement);
    }

    /* Generates a ProofStatement of [H -> H] */
    private static ModusPonens generateSelfImplicationStatement(Expression hypothesis) {
        Expression selfImplication = new Implication(hypothesis, hypothesis);
        return new ModusPonens(
                new ModusPonens(
                        new AxiomStatement(2, hypothesis, selfImplication, hypothesis),
                        new AxiomStatement(1, hypothesis, hypothesis)),
                new AxiomStatement(1, hypothesis, selfImplication)
        );
    }

    /* Generates a ProofStatement of [h |- H -> X] from a given hypothesis or axiom X */
    private static ModusPonens generatePlainConditionalStatement(ProofStatement statement, Expression hypothesis) {
        return new ModusPonens(
                new AxiomStatement(1, statement.getExpression(), hypothesis),
                statement);
    }

    /* Recursively converts [h, H |- X -> Y] to [h |- H -> X -> Y] and [h, H |- X] to [h |- H -> X]
     * Then generates a ProofStatement of [h |- H -> Y] from given [h |- H -> X -> Y], [h |- H -> X] */
    private ModusPonens generateConditionalModusPonens(ModusPonens statement, Expression hypothesis) {
        return new ModusPonens(
                new ModusPonens(
                        new AxiomStatement(2, hypothesis, statement.getAntecedent(), statement.getConsequent()),
                        buildConditionalStatement(statement.getAssertionStatement(), hypothesis)),
                buildConditionalStatement(statement.getConditionStatement(), hypothesis));
    }
}
