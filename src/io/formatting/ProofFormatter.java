package io.formatting;

import model.expression.Expression;
import model.proof.HypothesisStatement;
import model.proof.Proof;

public class ProofFormatter extends AbstractFormatter {
    public static String format(Proof target) throws FormatException {
        return format(ProofFormatter::formatProof, target);
    }

    /* Proof ::= Proposition '\n' Statement {'\n' Statement}* */
    public static void formatProof(StringBuilder builder, Proof target) throws FormatException {
        formatProposition(builder, target);
        for (Expression statement : target.getSentences()) {
            builder.append('\n');
            formatStatement(builder, statement);
        }
    }

    /* Proposition ::= {Expression {',' Expression}*}? '|-' Expression */
    private static void formatProposition(StringBuilder builder, Proof target) throws FormatException {
        String prefix = "";
        for (HypothesisStatement hypothesis : target.getHypotheses()) {
            builder.append(prefix);
            ExpressionFormatter.formatExpression(builder, hypothesis.getExpression());
            prefix = ",";
        }
        builder.append("|-");
        ExpressionFormatter.formatExpression(builder, target.getThesis());
    }

    /* Statement ::= Expression */
    private static void formatStatement(StringBuilder builder, Expression target) throws FormatException {
        ExpressionFormatter.formatExpression(builder, target);
    }
}
