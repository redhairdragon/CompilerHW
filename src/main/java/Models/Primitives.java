package Models;

import java.util.Arrays;
import java.util.HashSet;

public class Primitives {
    static final HashSet<Type> primitives = new HashSet<>(
            Arrays.asList(new Type("boolean"), new Type("int"), new Type("int[]")));

    public static boolean isPrimitive(Type type) {
        return primitives.contains(type);
    }
}
