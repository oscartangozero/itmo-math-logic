package io.parsing;

import model.expression.Expression;
import model.expression.Variable;
import model.proof.Proof;
import model.proof.DraftProof;

import java.util.*;

public class ProofParser extends AbstractParser {
    public static Proof parse(String text) throws ParseException {
        return parse(text, new HashMap<>());
    }

    public static Proof parse(String text, Map<String, Variable> variablesCache) throws ParseException {
        return parseExhaustively(ProofParser::parseProof, text, variablesCache);
    }

    public static DraftProof parseProposition(String text) throws ParseException {
        return parseProposition(text, new HashMap<>());
    }

    public static DraftProof parseProposition(String text, Map<String, Variable> variablesCache) throws ParseException {
        return parseExhaustively(ProofParser::parseProposition, text, variablesCache);
    }

    public static Expression parseStatement(String text) throws ParseException {
        return ExpressionParser.parse(text);
    }

    public static Expression parseStatement(String text, Map<String, Variable> variablesCache) throws ParseException {
        return ExpressionParser.parse(text, variablesCache);
    }

    /* Proof ::= Proposition '\n' Statements */
    private static Proof parseProof(StringView data, Map<String, Variable> variables) throws ParseException {
        DraftProof draftProof = parseProposition(data, variables);
        consumeNext('\n', data);
        List<Expression> statements = parseStatements(data, variables);
        for (Expression statement : statements) {
            draftProof.tryAppendStatement(statement);
        }
        return draftProof.tryValidate();
    }

    /* Proposition ::= Context '|-' Expression */
    private static DraftProof parseProposition(StringView data, Map<String, Variable> variables) throws ParseException {
        String delimiter = "|-";
        int delimiterIndex = data.indexOf(delimiter);
        if (delimiterIndex == -1) throw ParseException.unexpectedTermination(data, delimiter);
        StringView contextData = data.removePrefix(delimiterIndex);
        data.shiftStartPosition(delimiter.length());
        List<Expression> context = parseContext(contextData, variables);
        Expression thesis = ExpressionParser.parseExpression(data, variables);
        return new DraftProof(context, thesis);
    }

    /* Context ::= {Hypothesis {',' Hypothesis}*}? */
    private static List<Expression> parseContext(StringView data, Map<String, Variable> variables) throws ParseException {
        if (isBufferExhausted(data)) return Collections.emptyList();
        return parseSequenceGreedily(ExpressionParser::parseExpression, data, variables, ',');
    }

    /* Statements ::= Statement {'\n' Statement}* */
    private static List<Expression> parseStatements(StringView data, Map<String, Variable> variables) throws ParseException {
        return parseSequenceGreedily(ExpressionParser::parseExpression, data, variables, '\n');
    }
}
