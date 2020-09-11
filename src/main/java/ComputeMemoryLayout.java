import cs132.IR.sparrowv.*;
import cs132.IR.sparrowv.visitor.*;

import java.util.HashMap;

public class ComputeMemoryLayout extends DepthFirst {
    
    /************************* Memory Layout ****************************
     ...
            4(fp) is in[1]
     fp ->  0(fp) is in[0]
           -4(fp) is return address   ----
           -8(fp) is saved old fp       --
          -12(fp) is local[0]           --  Current stack frame
          -16(fp) is local[1]           --
     ...                                --  size = max(out.len) + local.len + 8 bytes
            8(sp) is out[2]             --
            4(sp) is out[1]             --
     sp ->  0(sp) is out[0]           ----

    ********************************************************************/

    
    public HashMap <String, HashMap <String, Integer>> envTable;
    private int local_count;

    public ComputeMemoryLayout() {
        envTable = new HashMap<>();
    }

    public void printEnvTable() {
        for(String func : envTable.keySet()){
            System.out.println(func + ":");
            HashMap<String , Integer> env = envTable.get(func);
            for(String id : env.keySet()){
                System.out.println("\t"+id + "\t: " + env.get(id));
            }
            System.out.println();
        }
    }

    @Override
    public void visit(FunctionDecl n) {
        String name = n.functionName.name;
        HashMap<String, Integer> env = new HashMap<>();
        envTable.put(name, env);
        local_count = 3; // every time reset it to 3
        super.visit(n);
    }

    @Override
    public void visit(Move_Id_Reg n) {
        String func_name = n.parent.parent.functionName.name;
        if(! envTable.get(func_name).containsKey(n.lhs.toString())){
            envTable.get(func_name).put(n.lhs.toString(), local_count * (-4));
            local_count ++;
        }
        super.visit(n);
    }
}
