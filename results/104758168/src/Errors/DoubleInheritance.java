package Errors;

public class DoubleInheritance extends Error {
    public DoubleInheritance(String name) {
        super("Class: " + name + " Inherits from two classes.(Should not appear)");
    }
}
