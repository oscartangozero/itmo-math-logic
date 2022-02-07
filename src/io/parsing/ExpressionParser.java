package io.parsing;

import model.expression.*;

import java.util.HashMap;
import java.util.Map;

public class ExpressionParser extends AbstractParser {
    public static Expression tryParse(String text) {
        try {
            return parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Expression parse(String text) throws ParseException {
        return parse(text, new HashMap<>());
    }

    public static Expression parse(String text, Map<String, Variable> variablesCache) throws ParseException {
        return parseExhaustively(ExpressionParser::parseExpression, text, variablesCache);
    }

    /* Expression ::= Disjunction | Disjunction '->' Expression */
    protected static Expression parseExpression(StringView data, Map<String, Variable> variables) throws ParseException {
        Expression leftOperand = parseDisjunction(data, variables);
        if (tryConsumeNext("->", data)) {
            leftOperand = new Implication(leftOperand, parseExpression(data, variables));
        }
        return leftOperand;
    }

    /* Disjunction ::= Conjunction | Disjunction '|' Conjunction */
    private static Expression parseDisjunction(StringView data, Map<String, Variable> variables) throws ParseException {
        Expression leftOperand = parseConjunction(data, variables);
        while (tryConsumeNext('|', data)) {
            leftOperand = new Disjunction(leftOperand, parseConjunction(data, variables));
        }
        return leftOperand;
    }

    /* Conjunction ::= Negation | Conjunction '&' Negation */
    private static Expression parseConjunction(StringView data, Map<String, Variable> variables) throws ParseException {
        Expression leftOperand = parseNegation(data, variables);
        while (tryConsumeNext('&', data)) {
            leftOperand = new Conjunction(leftOperand, parseNegation(data, variables));
        }
        return leftOperand;
    }

    /* Negation ::= '!' Negation | Variable | '(' Expression ')' */
    private static Expression parseNegation(StringView data, Map<String, Variable> variables) throws ParseException {
        Expression result;
        switch (expectNextChar(data)) {
            case '!':
                data.shiftStartPosition(1);
                return new Negation(parseNegation(data, variables));
            case '(':
                data.shiftStartPosition(1);
                result = parseExpression(data, variables);
                consumeNext(')', data);
                return result;
            default:
                return parseVariable(data, variables);
        }
    }

    /* Variable ::= ('A'...'Z'){'A'...'Z'|'0'...'9'|'''}  */
    private static Variable parseVariable(StringView data, Map<String, Variable> variables) throws ParseException {
        if (!isCapLetter(data.getChar())) throw ParseException.invalidCharacters(data, "variable name");
        int nameLength = 0, dataLength = data.length();
        while (nameLength < dataLength && isIDChar(data.charAt(nameLength))) nameLength++;
        String name = data.removePrefixString(nameLength);
        return constructVariableObject(name, variables);
    }

    private static Variable constructVariableObject(String name, Map<String, Variable> variables) {
        Variable variable = variables.get(name);
        if (variable == null) {
            variable = new Variable(name);
            variables.put(name, variable);
        }
        return variable;
    }

    private static boolean isIDChar(char c) {
        return isCapLetter(c) || isDigit(c) || c == '\'';
    }

    private static boolean isDigit(char c) {
        return isCharInRange(c, '0', '9');
    }

    private static boolean isCapLetter(char c) {
        return isCharInRange(c, 'A', 'Z');
    }
}
