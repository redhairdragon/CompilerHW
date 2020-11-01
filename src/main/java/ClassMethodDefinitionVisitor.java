import java.util.HashMap;
import java.util.Vector;

import Errors.DuplicatedMethodArgumentName;
import Errors.UndefinedTypeError;
import Helpers.*;
import cs132.minijava.syntaxtree.ClassDeclaration;
import cs132.minijava.syntaxtree.ClassExtendsDeclaration;
import cs132.minijava.syntaxtree.MethodDeclaration;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.visitor.*;
import Models.Class;

//Load method args and check parent method type
public class ClassMethodDefinitionVisitor extends GJVoidDepthFirst<Class> {
    HashMap<String, Class> classes;

    public ClassMethodDefinitionVisitor(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    public void execute(Node root) {
        root.accept(this, null);
        ParentMethodArgsChecker pmc = new ParentMethodArgsChecker(classes);
        pmc.check(root);
    }

    @Override
    public void visit(ClassDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);
        Helpers.debugPrint("Loading " + md.size() + " Methods for Class: " + className);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(ClassExtendsDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);
        Helpers.debugPrint("Loading " + md.size() + " Methods for Extended Class: " + className);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(MethodDeclaration n, Class c) throws DuplicatedMethodArgumentName, UndefinedTypeError {
        String methodName = n.f2.f0.tokenImage;
        Models.Type returnType = Helpers.getType(n.f1);

        if (!returnType.isPrimitive())
            if (!classes.containsKey(returnType.getName()))
                throw new UndefinedTypeError(returnType.getName());

        MethodParamVisitor mpv = new MethodParamVisitor(c.getName(), methodName, classes, n);
        c.addMethod(returnType, methodName, mpv.getArgs());

        Helpers.debugPrint("Class: " + c.getName() + "\tMethod:\t" + methodName + "\tRType:\t" + returnType.getName()
                + "\nArgs:\t" + mpv.getArgs());

    }

}
