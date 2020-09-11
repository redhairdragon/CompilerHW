import cs132.IR.ParseException;
import cs132.IR.SparrowParser;
import cs132.IR.registers.*;
import cs132.IR.sparrowv.Program;
import cs132.IR.visitor.SparrowVConstructor;

public class SV2V {

    public static void main (String[] args) {
        Registers.SetRiscVregs();
        try {
            cs132.IR.syntaxtree.Node root = new SparrowParser(System.in).Program();
            SparrowVConstructor svc = new SparrowVConstructor();
            root.accept(svc);
            Program p = svc.getProgram();

            ComputeMemoryLayout cml = new ComputeMemoryLayout();
            cml.visit(p);
            SparrowVToRISCV sv2v = new SparrowVToRISCV(cml.envTable);
            sv2v.visit(p);
            System.out.println(sv2v.combined());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
