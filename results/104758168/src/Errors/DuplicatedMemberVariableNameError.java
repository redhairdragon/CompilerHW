package Errors;

public class DuplicatedMemberVariableNameError extends Error {
    public DuplicatedMemberVariableNameError(String var, String className) {
        super("Variable " + var + " appears 2+ times in Class: " + className);
    }

}
