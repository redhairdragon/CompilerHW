package Errors;

public class VariableNotFoundError extends Error {
    public VariableNotFoundError(String v, String m, String c) {
        super("Var: " + v + " in Method: " + m + " of Class: " + c + " is not defined");
    }

}
