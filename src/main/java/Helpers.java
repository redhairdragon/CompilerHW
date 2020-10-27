import cs132.minijava.syntaxtree.*;

public class Helpers {
    static String classname(MainClass n) {
        return n.f1.f0.tokenImage;
    }

    static String classname(ClassDeclaration n) {
        return n.f1.f0.tokenImage;
    }

    static String classname(ClassExtendsDeclaration n) {
        return n.f1.f0.tokenImage;
    }

    static String parentname(ClassExtendsDeclaration n) {
        return n.f3.f0.tokenImage;
    }

    static String methodname(MethodDeclaration n) {
        return n.f2.f0.tokenImage;
    }

    static void debugPrint(Object s) {
        System.out.println(s.toString());
    }
}
