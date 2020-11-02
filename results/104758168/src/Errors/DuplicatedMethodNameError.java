package Errors;

import Models.Method;
import Models.Class;

public class DuplicatedMethodNameError extends Error {
    public DuplicatedMethodNameError(Method method, Class c) {
        super("Duplicate Method: " + method.getName() + " in Class: " + c.getName());
    }

    public DuplicatedMethodNameError(String method, String c) {
        super("Duplicate Method: " + method + " in Class: " + c);
    }
}
