package io.parsing;

import model.expression.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractParser {
    static boolean isCharInRange(char c, char start, char end) {
        return start <= c && c <= end;
    }

    static boolean isWhitespace(char c) {
        return (c == ' ' | c == '\t' | c == '\r');
    }

    static void skipToNext(StringView buffer) {
        while (!buffer.isEmpty() && isWhitespace(buffer.getChar())) buffer.shiftStartPosition(1);
    }

    static boolean isBufferExhausted(StringView buffer) {
        skipToNext(buffer);
        return buffer.isEmpty();
    }

    static boolean tryConsumeNext(char target, StringView buffer) {
        skipToNext(buffer);
        if (!buffer.startsWith(target)) return false;
        buffer.shiftStartPosition(1);
        return true;
    }

    static boolean tryConsumeNext(String target, StringView buffer) {
        skipToNext(buffer);
        if (!buffer.startsWith(target)) return false;
        buffer.shiftStartPosition(target.length());
        return true;
    }

    static void consumeNext(char target, StringView buffer) throws ParseException {
        if (!tryConsumeNext(target, buffer)) throw ParseException.invalidCharacter(buffer, target);

    }

    static void consumeNext(String target, StringView buffer) throws ParseException {
        if (!tryConsumeNext(target, buffer)) throw ParseException.invalidCharacters(buffer, target);
    }

    static void expectHasNextChar(StringView buffer) throws ParseException {
        if (isBufferExhausted(buffer)) throw ParseException.unexpectedTermination(buffer);
    }

    static char expectNextChar(StringView buffer) throws ParseException {
        expectHasNextChar(buffer);
        return buffer.getChar();
    }

    static void expectNoNextChar(StringView buffer) throws ParseException {
        if (!isBufferExhausted(buffer)) throw ParseException.unexpectedTermination(buffer);
    }

    @FunctionalInterface
    interface ParseFunction<R> {
        R apply(StringView data, Map<String, Variable> variablesCache) throws ParseException;
    }

    static <R> R parseExhaustively(ParseFunction<R> parser, String text,
                                   Map<String, Variable> variablesCache) throws ParseException {
        StringView data = new StringView(text);
        R result = parser.apply(data, variablesCache);
        expectNoNextChar(data);
        return result;
    }

    static <R> List<R> parseSequenceGreedily(ParseFunction<R> parser, StringView buffer,
                                             Map<String, Variable> variablesCache,
                                             char terminalDelimiter) throws ParseException {
        List<R> sequence = new ArrayList<>();
        do sequence.add(parser.apply(buffer, variablesCache));
        while (tryConsumeNext(terminalDelimiter, buffer));
        return sequence;
    }
}
