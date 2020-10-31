package Errors;

public class DuplicatedMethodArgumentName extends Error {

    public DuplicatedMethodArgumentName(String argname, String methodname, String classname) {
        super("Arg Name: " + argname + " appears 2+ times in Method: " + methodname + " of Class: " + classname);
    }

}
