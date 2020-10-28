import java.io.InputStream;
import java.lang.System;

import cs132.minijava.MiniJavaParser;
import cs132.minijava.ParseException;
import cs132.minijava.syntaxtree.Node;

public class Typecheck {
    public static void main(String[] args) {
        System.out.println("--------------------------------");
        InputStream in = System.in;
        new MiniJavaParser(in);
        try {
            Node root = MiniJavaParser.Goal();
            Typechecker tc = new Typechecker(root);
            tc.execute();
        } catch (ParseException e) {
            System.out.println("Parse Failed.");
            return;
        } catch (Error e) {
            System.out.println(e);
            printFailure();
            return;
        }
        printSucceess();
    }

    private static void printSucceess() {
        System.out.println("Program type checked successfully");
        System.out.println("--------------------------------");
    }

    private static void printFailure() {
        System.out.println("Type error");
        System.out.println("--------------------------------");
    }
}

// gradle run <