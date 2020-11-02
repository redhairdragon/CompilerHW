import Models.Method;

import java.util.HashMap;
import java.util.Vector;

import Errors.InvalidArrayAllocationTypeError;
import Errors.InvalidArrayLookupError;
import Errors.InvalidCmpOperandError;
import Errors.InvalidMinusOperandError;
import Errors.InvalidOperandError;
import Errors.InvalidPlusOperandError;
import Errors.InvalidTimesOperandError;
import Errors.InvokeArgError;
import Errors.LengthCalledOnNonArrayError;
import Errors.UndefinedTypeError;
import Errors.VariableNotFoundError;
import Helpers.Helpers;
import Models.Class;
import Models.Type;
import cs132.minijava.syntaxtree.*;
import cs132.minijava.visitor.GJNoArguDepthFirst;

public class ExpressionTypeResolver extends GJNoArguDepthFirst<Type> {
    Class c;
    Method method;
    HashMap<String, Class> classes;

    public ExpressionTypeResolver(Class c, Method method, HashMap<String, Class> classes) {
        this.classes = classes;
        this.method = method;
        this.c = c;
    }

    public Type resolve(Expression e) {
        return e.f0.choice.accept(this);
    }

    @Override
    public Type visit(AllocationExpression e) {
        String allocatingType = Helpers.getIdName(e.f1);
        if (classes.containsKey(allocatingType))
            return new Type(allocatingType);
        throw new UndefinedTypeError(allocatingType);
    }

    @Override
    public Type visit(NotExpression e) {
        if (resolve(e.f1).equals(new Type("boolean")))
            return new Type("boolean");
        throw new InvalidOperandError("Not", method.name, c.name);
    }

    @Override
    public Type visit(BracketExpression e) {
        return resolve(e.f1);
    }

    @Override
    public Type visit(ThisExpression e) {
        return new Type(c.name);
    }

    @Override
    public Type visit(ArrayAllocationExpression e) {
        Type arrlen = this.resolve(e.f3);
        if (arrlen.equals(new Type("int")))
            return new Type("int[]");
        throw new InvalidArrayAllocationTypeError();
    }

    @Override
    public Type visit(MessageSend e) {

        Type callerType = e.f0.accept(this);
        Method callingMethod = classes.get(callerType.getName()).getMethod(Helpers.getIdName(e.f2));

        Helpers.debugPrint("Resolving Function Invoke: " + callingMethod.name);

        HashMap<String, Type> args = callingMethod.arguments;
        ExpressionListVisitor elv = new ExpressionListVisitor(c, method, classes);
        Vector<Type> passedTypes = elv.types(e.f4);

        if (passedTypes.size() != callingMethod.argorder.size())
            throw new InvokeArgError(method.name, c.name, callingMethod.name);
        for (int i = 0; i < callingMethod.argorder.size(); i++) {
            if (!Helpers.isSubType(passedTypes.get(i), args.get(callingMethod.argorder.get(i)), classes))
                throw new InvokeArgError(method.name, c.name, callingMethod.name);
        }
        return callingMethod.returnType;
    }

    @Override
    public Type visit(AndExpression e) {
        Helpers.debugPrint("Resolving And");

        PrimaryExpression l = e.f0;
        PrimaryExpression r = e.f2;
        Type leftType = l.accept(this);
        Type rightType = r.accept(this);

        Type target = new Type("boolean");
        if (leftType.equals(target) && rightType.equals(target))
            return target;
        throw new InvalidOperandError("AndExpression", method.name, c.name);
    }

    @Override
    public Type visit(TrueLiteral e) {
        Helpers.debugPrint("Resolving TrueLiteral ");
        return new Type("boolean");
    }

    @Override
    public Type visit(FalseLiteral e) {
        Helpers.debugPrint("Resolving FalseLiteral ");
        return new Type("boolean");
    }

    @Override
    public Type visit(IntegerLiteral e) {
        Helpers.debugPrint("Resolving IntegerLiteral: " + e);
        return new Type("int");
    }

    @Override
    public Type visit(Identifier e) {
        Helpers.debugPrint("Resolving Identifier: " + Helpers.getIdName(e));

        Type t = checkScopes(e.f0.tokenImage);
        if (t != null)
            return t;
        throw new VariableNotFoundError(e.f0.tokenImage, method.name, c.name);
    }

    @Override
    public Type visit(CompareExpression e) {
        Helpers.debugPrint("Resolving CompareExpression: " + e);
        Type leftType = e.f0.accept(this);
        Type rightType = e.f2.accept(this);
        Type target = new Type("int");
        if (!leftType.equals(target) || !rightType.equals(target))
            throw new InvalidCmpOperandError();
        return new Type("boolean");
    }

    @Override
    public Type visit(PlusExpression e) {
        Helpers.debugPrint("Resolving PlusExpression: " + e);
        Type leftType = e.f0.accept(this);
        Type rightType = e.f2.accept(this);
        Type target = new Type("int");
        if (!leftType.equals(target) || !rightType.equals(target))
            throw new InvalidPlusOperandError();
        return new Type("int");
    }

    @Override
    public Type visit(MinusExpression e) {
        Helpers.debugPrint("Resolving MinusExpression: " + e);
        Type leftType = e.f0.accept(this);
        Type rightType = e.f2.accept(this);
        Type target = new Type("int");
        if (!leftType.equals(target) || !rightType.equals(target))
            throw new InvalidMinusOperandError();
        return new Type("int");
    }

    @Override
    public Type visit(TimesExpression e) {
        Helpers.debugPrint("Resolving TimesExpression: " + e);
        Type leftType = e.f0.accept(this);
        Type rightType = e.f2.accept(this);
        Type target = new Type("int");
        if (!leftType.equals(target) || !rightType.equals(target))
            throw new InvalidTimesOperandError();
        return new Type("int");
    }

    @Override
    public Type visit(ArrayLookup e) {
        Helpers.debugPrint("Resolving ArrayLookup: " + e);
        Type arrType = e.f0.accept(this);
        Type arrTypeTarget = new Type("int[]");
        if (!arrType.equals(arrTypeTarget))
            throw new InvalidArrayLookupError();
        Type indexType = e.f2.accept(this);
        Type indexTypeTarget = new Type("int");
        if (!indexType.equals(indexTypeTarget))
            throw new InvalidArrayLookupError();
        return new Type("int");
    }

    @Override
    public Type visit(ArrayLength e) {
        Helpers.debugPrint("Resolving ArrayLength: " + e);
        Type arrType = e.f0.accept(this);
        Type arrTypeTarget = new Type("int[]");
        if (arrType.equals(arrTypeTarget))
            return new Type("int");
        throw new LengthCalledOnNonArrayError();
    }

    @Override
    public Type visit(PrimaryExpression e) {
        return e.f0.choice.accept(this);
    }

    Type checkScopes(String var) {
        if (method.variables.containsKey(var))
            return method.variables.get(var);
        if (method.arguments.containsKey(var))
            return method.arguments.get(var);
        if (c.variables.containsKey(var))
            return c.variables.get(var);
        Class parent = c.parent;
        while (parent != null) {
            if (parent.variables.containsKey(var))
                return parent.variables.get(var);
            parent = parent.parent;
        }
        return null;
    }
}
