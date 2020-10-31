package Models;

import java.util.HashMap;

import Errors.DuplicatedMethodNameError;
import Errors.DuplicatedMemberVariableNameError;

public class Class {
    public String name;
    public Class parent;
    HashMap<String, Method> methods;
    HashMap<String, Type> variables;

    public Class(String name) {
        this.name = name;
        this.parent = null;
        this.variables = new HashMap<String, Type>();
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
}
