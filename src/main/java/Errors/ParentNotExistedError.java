package Errors;

public class ParentNotExistedError extends Error {
    public ParentNotExistedError(String name) {
        super("Class: " + name + " doesn't exist. But it is used as a parent");
    }
}
