package Errors;

public class UndefinedTypeError extends Error {
    public UndefinedTypeError(String name) {
        super("Type: " + name + " is undefined");
    }

}
