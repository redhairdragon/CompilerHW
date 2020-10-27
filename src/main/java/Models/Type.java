package Models;

public class Type {
    String name;

    public Type(String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Type other) {
        return name.equals(other.name);
    }
}
