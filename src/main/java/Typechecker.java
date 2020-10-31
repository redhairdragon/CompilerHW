import cs132.minijava.syntaxtree.Node;

import java.util.HashMap;
import java.util.HashSet;

import Errors.*;
import Models.Class;

public class Typechecker {
    Node root;
    HashMap<String, Class> classes;
    HashMap<String, String> childToParent;

    ClassDefinitionVisitor classDefinitionVisitor = new ClassDefinitionVisitor();
    ClassMemberVisitor classMemberVisitor;
    ClassMethodVisitor classMethodVisitor;

    public Typechecker(Node root) {
        this.root = root;
    }

    public void execute() throws ExistedClassDefinitionError, ParentNotExistedError, MainFunctionNotFoundError {
        checkClassDefinition();
        initClasses();
        classMemberVisitor = new ClassMemberVisitor(classes);
        root.accept(classMemberVisitor, null);
        classMethodVisitor = new ClassMethodVisitor(classes);
        root.accept(classMethodVisitor, null);
        return;
    }

    private void checkClassDefinition()
            throws ExistedClassDefinitionError, ParentNotExistedError, MainFunctionNotFoundError {
        root.accept(classDefinitionVisitor);
        classDefinitionVisitor.checkMainExistence();
        classDefinitionVisitor.checkParentsExistence();
    }

    private void initClasses() {
        childToParent = classDefinitionVisitor.getChildToParent();
        classes = new HashMap<>();
        classDefinitionVisitor.getClasses().forEach(x -> {
            classes.put(x, new Class(x));
        });
        classes.keySet().forEach(cn -> {
            Class c = classes.get(cn);
            if (childToParent.containsKey(c.getName()))
                c.setParent(classes.get(childToParent.get(c.getName())));
        });
    }

    // Check if t1<= t2
    public boolean isSubType(String t1, String t2) {
        if (t1.equals(t2))
            return true;
        if (!childToParent.containsKey(t1))
            return false;
        if (childToParent.get(t1).equals(t2))
            return true;

        HashSet<String> visited = new HashSet<>();
        String currentParent = childToParent.get(t1);
        while (childToParent.containsKey(currentParent)) {
            if (childToParent.get(currentParent).equals(t2))
                return true;
            if (visited.contains(currentParent))
                return false;
            currentParent = childToParent.get(currentParent);
            visited.add(currentParent);
        }
        return false;
    }
}
