package Models;

import java.util.HashMap;

public class Method {
    public String name;
    public Class belongto;
    public Type returnType;
    public HashMap<String, Type> arguments;

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

}
