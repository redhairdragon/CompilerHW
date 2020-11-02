package Models;

import java.util.HashMap;

import Errors.DuplicatedLocalVariableNameError;

public class Method {
    public String name;
    public Class belongto;
    public Type returnType;
    public HashMap<String, Type> arguments;
    public HashMap<Integer, String> argorder;
    public HashMap<String, Type> variables = new HashMap<>();

    public Method(String name, Type returnType, HashMap<String, Type> arguments, HashMap<Integer, String> order,
            Class belongto) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.belongto = belongto;
        this.argorder = order;
    }

    public Method(String name, Type returnType, HashMap<String, Type> arguments, HashMap<Integer, String> order) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.argorder = order;
        this.belongto = null;
    }

    public String getName() {
        return name;
    }

    public void addVariable(Type t, String varname) throws DuplicatedLocalVariableNameError {
        if (variables.containsKey(varname))
            throw new DuplicatedLocalVariableNameError(varname, name, belongto.getName());
        variables.put(varname, t);
    }

}
