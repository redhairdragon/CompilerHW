package Errors;

public class ExistedClassDefinitionError extends Error {
    public ExistedClassDefinitionError(String name) {
        super("Class: " + name + " is already defined.");
    }
}
