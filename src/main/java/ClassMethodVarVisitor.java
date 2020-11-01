import cs132.minijava.syntaxtree.ClassDeclaration;
import cs132.minijava.syntaxtree.ClassExtendsDeclaration;
import cs132.minijava.syntaxtree.MethodDeclaration;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.syntaxtree.VarDeclaration;
import cs132.minijava.visitor.*;
import Models.Class;
import Models.Method;

import java.util.HashMap;
import java.util.Vector;

import Helpers.Helpers;

public class ClassMethodVarVisitor extends GJVoidDepthFirst<Class> {
    HashMap<String, Class> classes;

    public ClassMethodVarVisitor(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    public void execute(Node root) {
        root.accept(this, null);
    }

    @Override
    public void visit(ClassDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(ClassExtendsDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(MethodDeclaration n, Class c) {
        Vector<Node> vd = Helpers.varDeclarations(n);
        Method m = c.methods.get(Helpers.methodname(n));
        MethodVarChecker mvc = new MethodVarChecker(m, classes);
        for (Node var : vd) {
            var.accept(mvc);
        }
    }
}
