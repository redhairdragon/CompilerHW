package Models;

import java.util.HashMap;

public class Primitives {
    static final HashMap<Integer, String> coding = new HashMap<Integer, String>() {
        {
            put(0, "int[]");
            put(1, "boolean");
            put(2, "int");
        }
    };

    public static boolean isPrimitive(Type type) {
        return coding.containsValue(type.getName());
    }

    public static boolean isPrimitive(int code) {
        return coding.containsKey(code);
    }

    public static Type getCodeName(int code) {
        return new Type(coding.get(code));
    }

}
