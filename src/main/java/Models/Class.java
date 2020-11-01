package Models;

import java.util.HashMap;

import Errors.DuplicatedMethodNameError;
import Errors.MethodNotFoundError;
import Errors.DuplicatedMemberVariableNameError;

public class Class {
    public String name;
    public Class parent;
    public HashMap<String, Method> methods = new HashMap<>();
    public HashMap<String, Type> variables = new HashMap<String, Type>();

    public Class(String name) {
        this.name = name;
        this.parent = null;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void addVariable(Type t, String varname) throws DuplicatedMemberVariableNameError {
        if (variables.containsKey(varname))
            throw new DuplicatedMemberVariableNameError(varname, this.name);
        variables.put(varname, t);
    }

    public String getName() {
        return name;
    }

    public void setParent(Class parent) {
        this.parent = parent;
    }

    public void addMethod(Type returnType, String methodName, HashMap<String, Models.Type> args)
            throws DuplicatedMethodNameError {
        if (methods.containsKey(methodName))
            throw new DuplicatedMethodNameError(methodName, name);
        Method method = new Method(methodName, returnType, args, this);
        methods.put(method.getName(), method);
    }

    public Type returnType(String methodName) throws MethodNotFoundError {
        Method m = getMethod(methodName);
        return m.returnType;
    }

    public HashMap<String, Type> args(String methodName) throws MethodNotFoundError {
        Method m = getMethod(methodName);
        return m.arguments;
    }

    // recursively look up method
    private Method getMethod(String methodName) throws MethodNotFoundError {
        Class currentClass = this;
        while (currentClass != null) {
            if (currentClass.methods.containsKey(methodName))
                return currentClass.methods.get(methodName);
            currentClass = currentClass.parent;
        }
        throw new MethodNotFoundError(methodName, name);
    }
}
