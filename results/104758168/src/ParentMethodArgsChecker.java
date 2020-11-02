import java.util.HashMap;
import java.util.Vector;

import Helpers.Helpers;
import cs132.minijava.syntaxtree.ClassDeclaration;
import cs132.minijava.syntaxtree.ClassExtendsDeclaration;
import cs132.minijava.syntaxtree.MethodDeclaration;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.visitor.*;
import Models.Class;
import Models.Method;
import Models.Type;
import Errors.*;

//Check for overloading which is not allowed
public class ParentMethodArgsChecker extends GJVoidDepthFirst<Class> {
    HashMap<String, Class> classes;

    public ParentMethodArgsChecker(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    public void check(Node root) {
        root.accept(this, null);
    }

    @Override
    public void visit(ClassDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);

        Helpers.debugPrint("ParentMethodArgsChecking " + md.size() + " Methods for Extended Class: " + className);

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

    @Override
    public void visit(MethodDeclaration n, Class c) throws NoOverloadingError {
        String methodName = n.f2.f0.tokenImage;
        Models.Type returnType = Helpers.getType(n.f1);
        HashMap<String, Type> arguments = c.methods.get(methodName).arguments;

        Class currentClass = c.parent;
        while (currentClass != null) {
            if (c.parent.methods.containsKey(methodName)) {
                Method parentMethod = c.parent.methods.get(methodName);
                boolean rtChecked = parentMethod.returnType.equals(returnType);
                boolean argsChecked = parentMethod.arguments.toString().equals(arguments.toString());
                if (!rtChecked || !argsChecked)
                    throw new NoOverloadingError(methodName, c.name);
                return;
            } else {
                currentClass = currentClass.parent;
            }
        }
    }

}
