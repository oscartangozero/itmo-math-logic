package io.formatting;

public class FormatException extends Exception {
    private FormatException(String message) {
        super(message);
    }

    public static FormatException invalidObject(Object o) {
        return new FormatException("Unsupported object: " + objectDescription(o));
    }

    private static String objectDescription(Object o) {
        return o.getClass().getSimpleName();
    }
}
