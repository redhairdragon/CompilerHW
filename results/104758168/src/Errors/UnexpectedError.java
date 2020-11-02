package Errors;

public class UnexpectedError extends Error {
    public UnexpectedError(String name) {
        super("Unexpected Error: " + name);
    }

}
