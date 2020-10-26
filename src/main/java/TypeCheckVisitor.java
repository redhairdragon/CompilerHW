import java.util.HashMap;

import cs132.minijava.syntaxtree.AndExpression;
import cs132.minijava.syntaxtree.AssignmentStatement;
import cs132.minijava.syntaxtree.Expression;
import cs132.minijava.syntaxtree.IntegerLiteral;
import cs132.minijava.visitor.DepthFirstVisitor;




public class TypeCheckVisitor extends DepthFirstVisitor{
    HashMap <String,String> table;
    TypeCheckVisitor(){
        table=new HashMap<>();
    }

    @Override
    public void visit(AssignmentStatement n) {
        System.out.println(n);
        // n.fx.accept(this)
        // if type != systemo out type error
    }
    @Override
    public void visit(IntegerLiteral n) {
        System.out.println(n);

    }


}
