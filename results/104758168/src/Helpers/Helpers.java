package Helpers;

import java.util.HashMap;
import java.util.Vector;

import Models.Primitives;
import Models.Class;
import cs132.minijava.syntaxtree.*;
import cs132.minijava.syntaxtree.Node;

public class Helpers {
    public static String classname(MainClass n) {
        return n.f1.f0.tokenImage;
    }

    public static String classname(ClassDeclaration n) {
        return n.f1.f0.tokenImage;
    }

    public static String classname(ClassExtendsDeclaration n) {
        return n.f1.f0.tokenImage;
    }

    public static String parentname(ClassExtendsDeclaration n) {
        return n.f3.f0.tokenImage;
    }

    public static String methodname(MethodDeclaration n) {
        return n.f2.f0.tokenImage;
    }

    public static void debugPrint(Object s) {
        // System.out.println(s.toString());
    }

    public static Vector<Node> variableDeclarations(ClassDeclaration n) {
        return n.f3.nodes;
    }

    public static Vector<Node> variableDeclarations(ClassExtendsDeclaration n) {
        return n.f5.nodes;
    }

    public static Vector<Node> methodDeclarations(ClassDeclaration n) {
        return n.f4.nodes;
    }

    public static Vector<Node> methodDeclarations(ClassExtendsDeclaration n) {
        return n.f6.nodes;
    }

    public static Vector<Node> varDeclarations(MainClass n) {
        return n.f14.nodes;
    }

    public static Vector<Node> varDeclarations(MethodDeclaration n) {
        return n.f7.nodes;
    }

    public static Vector<Node> statements(MethodDeclaration n) {
        return n.f8.nodes;
    }

    public static Vector<Node> statements(MainClass n) {
        return n.f15.nodes;
    }

    public static Models.Type type(VarDeclaration n) {
        return getType(n.f0);
    }

    public static String varname(VarDeclaration n) {
        return n.f1.f0.tokenImage;
    }

    public static Models.Type getType(Type n) {
        int typecode = n.f0.which;
        Models.Type type;
        if (Primitives.isPrimitive(typecode)) {
            type = Primitives.getCodeName(typecode);
        } else { // Not a primative type
            Identifier nonPrimitiveNode = (Identifier) n.f0.choice;
            type = new Models.Type(nonPrimitiveNode.f0.tokenImage);
        }
        return type;
    }

    public static String argname(FormalParameter n) {
        return n.f1.f0.tokenImage;
    }

    // return t1<=t2
    public static boolean isSubType(Models.Type t1, Models.Type t2, HashMap<String, Class> classes) {
        if (t1.isPrimitive()) {
            if (t2.isPrimitive())
                return t1.equals(t2);
            return false;
        } else {
            if (t2.isPrimitive())
                return false;
            Class c = classes.get(t1.getName());
            while (c != null) {
                if (!c.getName().equals(t2.getName()))
                    c = c.parent;
                else
                    return true;
            }
            return false;
        }
    }

    public static String getIdName(Identifier n) {
        return n.f0.tokenImage;
    }
}
