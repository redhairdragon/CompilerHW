package Models;

public class Type {
    String name;

    public Type(String name) {
        this.name = name;
    }

    public boolean isPrimitive() {
        return Primitives.isPrimitive(this);
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Type other) {
        return name.equals(other.name);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
