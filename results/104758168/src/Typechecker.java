import cs132.minijava.syntaxtree.Node;

import java.util.HashMap;
import Errors.*;
import Models.Class;

public class Typechecker {
    Node root;
    HashMap<String, Class> classes;
    HashMap<String, String> childToParent;

    ClassDefinitionVisitor classDefinitionVisitor;
    ClassMemberVisitor classMemberVisitor;
    ClassMethodDefinitionVisitor classMethodDefVisitor;
    ClassMethodVarVisitor classMethodVarVisitor;
    ClassStatementVisitor classStatementVisitor;

    public Typechecker(Node root) {
        this.root = root;
    }

    public void execute() throws ExistedClassDefinitionError, ParentNotExistedError, MainFunctionNotFoundError {
        classDefinitionVisitor = new ClassDefinitionVisitor();
        classDefinitionVisitor.execute(root);
        initClasses();
        classMemberVisitor = new ClassMemberVisitor(classes);
        classMemberVisitor.execute(root);
        classMethodDefVisitor = new ClassMethodDefinitionVisitor(classes);
        classMethodDefVisitor.execute(root);
        classMethodVarVisitor = new ClassMethodVarVisitor(classes);
        classMethodVarVisitor.execute(root);
        classStatementVisitor = new ClassStatementVisitor(classes);
        classStatementVisitor.execute(root);
        return;
    }

    private void initClasses() {
        childToParent = classDefinitionVisitor.getChildToParent();
        classes = new HashMap<>();
        classDefinitionVisitor.getClasses().forEach(x -> {
            classes.put(x, new Class(x));
        });
        classes.get(classDefinitionVisitor.main).isMain = true;

        classes.keySet().forEach(cn -> {
            Class c = classes.get(cn);
            if (childToParent.containsKey(c.getName()))
                c.setParent(classes.get(childToParent.get(c.getName())));
        });
    }
}
