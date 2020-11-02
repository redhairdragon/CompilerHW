package Errors;

public class ReturnTypeNotMatchError extends Error {
    public ReturnTypeNotMatchError(String m, String c) {
        super("In Class: " + c + " Method:" + m);
    }
}
