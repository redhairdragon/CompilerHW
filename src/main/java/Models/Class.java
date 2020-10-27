package Models;

import java.util.HashMap;

public class Class {
    String name;
    HashMap<String, Method> methods;
    HashMap<String, Type> variables;

    public Class(String name) {
        this.name = name;
    }
}
