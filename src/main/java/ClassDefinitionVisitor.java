import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import cs132.minijava.syntaxtree.*;
import cs132.minijava.visitor.*;
import Errors.*;
import Helpers.*;

// Handles duplicate class name, inexisted parent
public class ClassDefinitionVisitor extends DepthFirstVisitor {
    HashSet<String> classes;
    HashSet<String> parents;
    HashMap<String, String> c2p; // parent to children relation

    public ClassDefinitionVisitor() {
        classes = new HashSet<>();
        parents = new HashSet<>();
        c2p = new HashMap<>();

        classes.add("boolean");
        classes.add("int");
    }

    public void execute(Node root)
            throws ExistedClassDefinitionError, ParentNotExistedError, MainFunctionNotFoundError {
        root.accept(this);
        this.checkMainExistence();
        this.checkParentsExistence();
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
        if (c2p.containsKey(className))
            throw new DoubleInheritance(className);
        else
            c2p.put(className, parentName);
    }

    public void checkParentsExistence() throws ParentNotExistedError {
        Optional<String> result = parents.stream().filter(p -> !classes.contains(p)).findFirst();
        Helpers.debugPrint("Parents: " + parents.toString());
        if (result.isPresent())
            throw new ParentNotExistedError(result.get());
    }

    public void checkMainExistence() throws MainFunctionNotFoundError {
        if (!classes.contains("Main"))
            throw new MainFunctionNotFoundError();
    }

    public HashSet<String> getClasses() {
        return classes;
    }

    public HashMap<String, String> getChildToParent() {
        return c2p;
    }
}
