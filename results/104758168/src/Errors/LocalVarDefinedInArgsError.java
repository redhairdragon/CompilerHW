package Errors;

public class LocalVarDefinedInArgsError extends Error {
    public LocalVarDefinedInArgsError(String v, String m, String c) {
        super("Local variable: " + v + " has been defined in arguments in Method: " + m + " of Class: " + c);
    }

}
