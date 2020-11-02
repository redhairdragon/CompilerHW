package Errors;

public class DuplicatedLocalVariableNameError extends Error {
    public DuplicatedLocalVariableNameError(String v, String m, String c) {
        super("Duplicated local variable: " + v + " in Method: " + m + " of Class: " + c);
    }

}
