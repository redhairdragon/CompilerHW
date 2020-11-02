import cs132.minijava.syntaxtree.Expression;
import cs132.minijava.syntaxtree.NodeOptional;
import cs132.minijava.visitor.DepthFirstVisitor;

import java.util.HashMap;
import java.util.Vector;

import Models.*;
import Models.Class;

public class ExpressionListVisitor extends DepthFirstVisitor {
    Class c;
    Method method;
    HashMap<String, Class> classes;
    Vector<Type> types = new Vector<>();
    ExpressionTypeResolver etr;

    public ExpressionListVisitor(Class c, Method method, HashMap<String, Class> classes) {
        this.method = method;
        this.c = c;
        this.classes = classes;
        this.etr = new ExpressionTypeResolver(c, method, classes);
    }

    @Override
    public void visit(Expression e) {
        types.add(etr.resolve(e));
    }

    public Vector<Type> types(NodeOptional n) {
        n.accept(this);
        return types;
    }
}
