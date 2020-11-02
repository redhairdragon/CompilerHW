package Errors;

public class MethodNotFoundError extends Error {
    public MethodNotFoundError(String method, String c) {
        super("Method: " + method + " not found in Class: " + c);
    }
}
