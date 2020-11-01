import java.util.HashMap;
import java.util.Vector;

import Helpers.Helpers;
import Models.Class;
import cs132.minijava.syntaxtree.ClassDeclaration;
import cs132.minijava.syntaxtree.ClassExtendsDeclaration;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.visitor.*;

public class ClassStatementVisitor extends GJVoidDepthFirst<Models.Class> {
    HashMap<String, Class> classes;

    public ClassStatementVisitor(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    @Override
    public void visit(ClassDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);
        Helpers.debugPrint("Checking " + md.size() + " Methods for Class: " + className);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(ClassExtendsDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);
        Helpers.debugPrint("Checking " + md.size() + " Methods for Extended Class: " + className);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

}
