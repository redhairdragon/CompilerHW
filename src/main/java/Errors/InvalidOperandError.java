package Errors;

public class InvalidOperandError extends Error {
    public InvalidOperandError(String o, String m, String c) {
        super("Class: " + c + " Method: " + m + " Op: " + o);
    }
}
