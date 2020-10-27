import cs132.minijava.syntaxtree.Node;

import java.util.HashMap;

import Errors.*;
import Models.Class;

public class Typechecker {
    Node root;
    HashMap<String, Class> classes;

    ClassDefinitionVisitor classDefinitionVisitor = new ClassDefinitionVisitor();

    public Typechecker(Node root) {
        this.root = root;
    }

    public void execute() throws ExistedClassDefinitionError, ParentNotExistedError {
        checkClassDefinition();
        initClasses();
        return;
    }

    private void checkClassDefinition() throws ExistedClassDefinitionError, ParentNotExistedError {
        root.accept(classDefinitionVisitor);
        classDefinitionVisitor.checkParentsExistence();
    }

    private void initClasses() {
        classDefinitionVisitor.getClasses().forEach(x -> {
            classes.put(x, new Class(x));
        });
    }
}
