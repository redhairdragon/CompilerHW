import java.util.HashMap;

import Errors.LocalVarDefinedInArgsError;
import Errors.UndefinedTypeError;
import Helpers.Helpers;
import Models.Method;
import Models.Class;
import Models.Type;
import cs132.minijava.syntaxtree.VarDeclaration;
import cs132.minijava.visitor.DepthFirstVisitor;

public class MethodVarChecker extends DepthFirstVisitor {
    private Method m;
    HashMap<String, Class> classes;

    public MethodVarChecker(Method m, HashMap<String, Class> classes) {
        this.m = m;
        this.classes = classes;
    }

    public void visit(VarDeclaration n) {
        Type type = Helpers.type(n);
        String varname = Helpers.varname(n);

        if (m.arguments.containsKey(varname))
            throw new LocalVarDefinedInArgsError(varname, m.name, m.belongto.getName());

        if (!type.isPrimitive())
            if (!classes.containsKey(type.getName()))
                throw new UndefinedTypeError(type.getName());
        m.addVariable(type, varname);
        Helpers.debugPrint(" Class: " + m.belongto.getName() + "\tMethods:\t" + m.name + "\tLocal Var:\t" + varname
                + "\tType:\t" + type.getName());
    }
}
