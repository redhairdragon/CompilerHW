import java.util.HashMap;

import Errors.DuplicatedMethodArgumentName;
import Errors.UndefinedTypeError;
import cs132.minijava.visitor.DepthFirstVisitor;
import cs132.minijava.syntaxtree.FormalParameter;
import cs132.minijava.syntaxtree.MethodDeclaration;
import Helpers.*;
import Models.Class;

public class MethodParamVisitor extends DepthFirstVisitor {
    HashMap<String, Models.Type> args = new HashMap<String, Models.Type>();
    HashMap<Integer, String> order = new HashMap<>();
    HashMap<String, Class> classes;
    String classname;
    String methodname;

    public MethodParamVisitor(String cn, String mn, HashMap<String, Class> classes, MethodDeclaration n) {
        this.classes = classes;
        this.classname = cn;
        this.methodname = mn;
        n.accept(this);
    }

    public HashMap<String, Models.Type> getArgs() {
        return this.args;
    }

    public HashMap<Integer, String> getOrder() {
        return this.order;
    }

    @Override
    public void visit(FormalParameter n) throws DuplicatedMethodArgumentName, UndefinedTypeError {
        Models.Type type = Helpers.getType(n.f0);
        String argname = Helpers.argname(n);
        if (args.containsKey(argname))
            throw new DuplicatedMethodArgumentName(argname, methodname, classname);
        if (!type.isPrimitive() && !classes.containsKey(type.getName()))
            throw new UndefinedTypeError(type.getName());
        order.put(args.size(), argname);
        args.put(argname, type);
    }
}
