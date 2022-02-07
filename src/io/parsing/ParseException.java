package io.parsing;

public class ParseException extends Exception {
    private static final int CONTEXT_SIZE = 10;

    private ParseException(String message) {
        super(message);
    }

    public static ParseException invalidCharacter(StringView data, char expected) {
        return new ParseException("Invalid character, expected " + expected + ", got: " + extractContext(data));
    }

    public static ParseException invalidCharacters(StringView data, String expected) {
        return new ParseException("Invalid characters, expected " + expected + ", got: " + extractContext(data));
    }

    public static ParseException unexpectedTermination(StringView data) {
        return new ParseException("Unexpected termination: " + extractTerminationContext(data));
    }

    public static ParseException unexpectedTermination(StringView data, String expected) {
        return new ParseException(
                "Unexpected termination, expected " + expected + ", got: " + extractTerminationContext(data));
    }

    private static String extractContext(StringView data) {
        if (CONTEXT_SIZE < data.length()) return data.subString(0, CONTEXT_SIZE) + "...";
        else return data.subString(0, data.length());
    }

    private static String extractTerminationContext(StringView data) {
        if (CONTEXT_SIZE < data.length()) {
            data.shiftStartPosition(data.length() - CONTEXT_SIZE);
            return "..." + data.subString(0, CONTEXT_SIZE);
        } else {
            return data.subString(0, data.length());
        }
    }
}
