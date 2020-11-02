import java.util.HashMap;
import java.util.Vector;

import Errors.ReturnTypeNotMatchError;
import Helpers.Helpers;
import Models.Class;
import Models.Method;
import Models.Type;
import cs132.minijava.syntaxtree.ClassDeclaration;
import cs132.minijava.syntaxtree.ClassExtendsDeclaration;
import cs132.minijava.syntaxtree.MainClass;
import cs132.minijava.syntaxtree.MethodDeclaration;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.visitor.*;

public class ClassStatementVisitor extends GJVoidDepthFirst<Models.Class> {
    HashMap<String, Class> classes;

    public ClassStatementVisitor(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    public void execute(Node root) {
        root.accept(this, null);
    }

    @Override
    public void visit(MainClass n, Class c) {
        String className = Helpers.classname(n);
        Helpers.debugPrint("Checking Statements for Main Class: " + className);
        c = classes.get(className);
        Vector<Node> statements = Helpers.statements(n);

        StatementChecker sc = new StatementChecker(c, c.methods.get("main"), classes);
        for (Node stm : statements) {
            sc.check(stm);
        }
    }

    @Override
    public void visit(ClassDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);
        Helpers.debugPrint("Checking Statements for Class: " + className);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(ClassExtendsDeclaration n, Class c) {
        String className = Helpers.classname(n);
        Vector<Node> md = Helpers.methodDeclarations(n);
        Helpers.debugPrint("Checking Statements for Extended Class: " + className);

        for (Node var : md) {
            var.accept(this, classes.get(className));
        }
    }

    @Override
    public void visit(MethodDeclaration n, Class c) {

        Method m = c.methods.get(Helpers.methodname(n));
        Helpers.debugPrint("Checking Statements for Method: " + m.name + " Class: " + c.getName());

        Vector<Node> statements = Helpers.statements(n);
        StatementChecker sc = new StatementChecker(c, m, classes);
        for (Node stm : statements) {
            sc.check(stm);
        }

        ExpressionTypeResolver etr = new ExpressionTypeResolver(c, m, classes);
        Type returnType = m.returnType;
        Type actualReturnType = etr.resolve(n.f10);
        if (!Helpers.isSubType(actualReturnType, returnType, classes))
            throw new ReturnTypeNotMatchError(m.name, c.name);

    }

}
