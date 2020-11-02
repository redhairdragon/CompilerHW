import java.util.HashMap;
import java.util.Vector;

import Errors.AssignmentTypeNotMatchingError;
import Errors.InvalidAssignmentStatementError;
import Errors.InvalidIfCondError;
import Errors.InvalidPrintError;
import Errors.InvalidWhileCondError;
import Errors.VariableNotFoundError;
import Helpers.Helpers;
import Models.Method;
import Models.Type;
import cs132.minijava.syntaxtree.ArrayAssignmentStatement;
import cs132.minijava.syntaxtree.AssignmentStatement;
import cs132.minijava.syntaxtree.Block;
import cs132.minijava.syntaxtree.IfStatement;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.syntaxtree.PrintStatement;
import cs132.minijava.syntaxtree.Statement;
import cs132.minijava.syntaxtree.WhileStatement;
import cs132.minijava.visitor.DepthFirstVisitor;
import Models.Class;

public class StatementChecker extends DepthFirstVisitor {
    Class c;
    Method method;
    ExpressionTypeResolver etr;
    HashMap<String, Class> classes;

    public StatementChecker(Class c, Method method, HashMap<String, Class> classes) {
        this.method = method;
        this.c = c;
        this.classes = classes;
        this.etr = new ExpressionTypeResolver(c, method, classes);
    }

    public void check(Node stm) {
        stm.accept(this);
    }

    @Override
    public void visit(Statement n) {
        n.f0.accept(this);
    }

    @Override
    public void visit(Block n) {
        Vector<Node> statements = n.f1.nodes;
        for (Node stm : statements) {
            stm.accept(this);
        }
    }

    @Override
    public void visit(AssignmentStatement n) {
        Type leftType = etr.visit(n.f0);
        if (leftType == null)
            throw new VariableNotFoundError(n.f0.f0.tokenImage, method.name, c.name);
        Type rightType = etr.resolve(n.f2);
        if (!Helpers.isSubType(leftType, rightType, classes))
            throw new AssignmentTypeNotMatchingError(leftType.getName(), rightType.getName());
    }

    @Override
    public void visit(ArrayAssignmentStatement n) {
        Type arrType = etr.visit(n.f0);
        Type idx = etr.resolve(n.f2);
        Type rightType = etr.resolve(n.f5);
        if (!arrType.equals(new Type("int[]")))
            throw new InvalidAssignmentStatementError("arrType");
        if (!idx.equals(new Type("int")))
            throw new InvalidAssignmentStatementError("idx");
        if (!rightType.equals(new Type("int")))
            throw new InvalidAssignmentStatementError("rightType");
    }

    @Override
    public void visit(IfStatement n) {
        Type ifcond = etr.resolve(n.f2);
        if (!ifcond.equals(new Type("boolean")))
            throw new InvalidIfCondError();
        this.visit(n.f4);
        this.visit(n.f6);
    }

    @Override
    public void visit(WhileStatement n) {
        Type ifcond = etr.resolve(n.f2);
        if (!ifcond.equals(new Type("boolean")))
            throw new InvalidWhileCondError();
        this.visit(n.f4);
    }

    @Override
    public void visit(PrintStatement n) {
        Type printContent = etr.resolve(n.f2);
        if (!printContent.equals(new Type("int")))
            throw new InvalidPrintError();
    }
}
