package Models;

import java.util.HashMap;

import Errors.DuplicatedLocalVariableNameError;

public class Method {
    public String name;
    public Class belongto;
    public Type returnType;
    public HashMap<String, Type> arguments;
    public HashMap<String, Type> variables = new HashMap<>();

    public Method(String name, Type returnType, HashMap<String, Type> arguments, Class belongto) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.belongto = belongto;
    }

    public Method(String name, Type returnType, HashMap<String, Type> arguments) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.belongto = null;
    }

    public String getName() {
        return name;
    }

    public void addVariable(Type t, String varname) throws DuplicatedLocalVariableNameError {
        if (variables.containsKey(varname))
            throw new DuplicatedLocalVariableNameError(varname, name, belongto.getName());
    }

}
