package Errors;

public class NoOverloadingError extends Error {
    public NoOverloadingError(String m, String c) {
        super("Method: " + m + " in Class: " + c + "violates Overloading Rule.");
    }

}
