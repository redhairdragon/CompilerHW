import java.util.HashMap;
import java.util.Vector;

import cs132.minijava.syntaxtree.ClassDeclaration;
import cs132.minijava.syntaxtree.ClassExtendsDeclaration;
import cs132.minijava.syntaxtree.MainClass;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.syntaxtree.VarDeclaration;
import cs132.minijava.visitor.GJVoidDepthFirst;
import Models.Class;
import Errors.*;
import Helpers.*;

public class ClassMemberVisitor extends GJVoidDepthFirst<Class> {
    HashMap<String, Class> classes;

    public ClassMemberVisitor(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    public void execute(Node root) {
        root.accept(this, null);
    }

    @Override
    public void visit(VarDeclaration n, Class c) throws DuplicatedMemberVariableNameError {
        String varname = Helpers.varname(n);
        Models.Type type = Helpers.getType(n.f0);

        if (!type.isPrimitive())
            if (!classes.containsKey(type.getName()))
                throw new UndefinedTypeError(type.getName());

        c.addVariable(type, varname);

        Helpers.debugPrint(" Class: " + c.getName() + "\tVar:\t" + varname + "\tType:\t" + type.getName());
    }

    @Override
    public void visit(ClassDeclaration n, Class c) throws Error {
        String className = Helpers.classname(n);
        Vector<Node> vd = Helpers.variableDeclarations(n);

        Helpers.debugPrint("Loading " + n.f3.size() + " Member Variables for Class: " + className);

        for (Node var : vd) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(ClassExtendsDeclaration n, Class c) throws Error {
        String className = Helpers.classname(n);
        Vector<Node> vd = Helpers.variableDeclarations(n);
        Helpers.debugPrint("Loading " + n.f5.size() + " Member Variables for Extended Class: " + className);

        for (Node var : vd) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(MainClass n, Class c) throws Error {
        String className = Helpers.classname(n);
        Vector<Node> vd = Helpers.variableDeclarations(n);

        Helpers.debugPrint("Loading " + n.f14.size() + " Member Variables for Class: " + className);
        classes.get(className).addVariable(new Models.Type("String[]"), Helpers.getIdName(n.f11));

        for (Node var : vd) {
            var.accept(this, classes.get(className));
        }
    }

}
