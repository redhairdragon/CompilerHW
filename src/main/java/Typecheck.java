
import java.io.InputStream;
import java.lang.System;
import cs132.minijava.MiniJavaParser;
import cs132.minijava.ParseException;
import cs132.minijava.syntaxtree.*;
import cs132.minijava.visitor.*;

public class Typecheck {
    public static void main(String[] args)  {
        try {
            InputStream in = System.in;
            Node root= new MiniJavaParser(in).Goal();    
            // root.accept(V)
        } catch (ParseException e) {
            System.out.println("Parse Failed.");
        }
        
    
    }
}

// java Typecheck < P.java

// should print either "Program type checked successfully" or "Type error".

// No cycle subtyping A->B B->A
// No two classes having the same names