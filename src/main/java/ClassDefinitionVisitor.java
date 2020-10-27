import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import cs132.minijava.syntaxtree.*;
import cs132.minijava.visitor.*;
import Errors.*;

// Handles duplicate class name, inexisted parent
public class ClassDefinitionVisitor extends DepthFirstVisitor {
    HashSet<String> classes;
    HashSet<String> parents;
    HashMap<String, HashSet<String>> p2c; // parent to children relation

    public ClassDefinitionVisitor() {
        classes = new HashSet<>();
        parents = new HashSet<>();
        p2c = new HashMap<>();

        classes.add("boolean");
        classes.add("int");
    }

    @Override
    public void visit(MainClass n) throws ExistedClassDefinitionError {
        String className = Helpers.classname(n);
        Helpers.debugPrint("Loading MainClass: " + className);
        if (classes.contains(className))
            throw new ExistedClassDefinitionError(className);
        classes.add(className);
    }

    @Override
    public void visit(ClassDeclaration n) throws ExistedClassDefinitionError {
        String className = Helpers.classname(n);
        Helpers.debugPrint("Loading Class: " + className);
        if (classes.contains(className))
            throw new ExistedClassDefinitionError(className);
        classes.add(className);
    }

    @Override
    public void visit(ClassExtendsDeclaration n) throws ExistedClassDefinitionError {
        String className = Helpers.classname(n);
        String parentName = Helpers.parentname(n);
        Helpers.debugPrint("Loading Class: " + className + " Extending: " + parentName);

        if (classes.contains(className))
            throw new ExistedClassDefinitionError(className);
        classes.add(className);
        parents.add(parentName);

        // Add to inheritance tree
        if (p2c.containsKey(parentName))
            p2c.get(parentName).add(className);
        else
            p2c.put(parentName, new HashSet<>(Arrays.asList(className)));
    }

    public void checkParentsExistence() throws ParentNotExistedError {
        Optional<String> result = parents.stream().filter(p -> !classes.contains(p)).findFirst();
        Helpers.debugPrint(parents.toString());
        if (result.isPresent())
            throw new ParentNotExistedError(result.get());
    }

    // Check if t1<= t2
    public boolean isSubType(String t1, String t2) {
        if (t1.equals(t2))
            return true;
        if (!p2c.containsKey(t2))
            return false;
        if (p2c.get(t2).contains(t1))
            return true;
        // Another DFS required here
        return false;
    }

    public HashSet<String> getClasses() {
        return classes;
    }
}
