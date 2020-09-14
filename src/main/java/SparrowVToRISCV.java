import cs132.IR.sparrowv.*;
import cs132.IR.sparrowv.visitor.*;
import cs132.IR.token.Identifier;

import java.util.HashMap;
import java.util.List;

public class SparrowVToRISCV extends DepthFirst {

    private int find_index(FunctionDecl fd, String id){
        List<Identifier> params = fd.formalParameters;
        for (Identifier i:params) {
            if(i.toString().equals(id)){
                return params.indexOf(i);
            }
        }
        return -1;
    }

    public HashMap <String, HashMap<String, Integer>> envTable;

    public String text_seg;
    public String ecall;
    public String data_seg;

    // Helper Functions
    public String error;
    public String alloc;

    int msg_counter;

    public String combined(){
        return ecall + "\n" +
                text_seg + "\n" +
                error + "\n" +
                alloc +"\n" +
                data_seg + "\n";
    }

    public SparrowVToRISCV(HashMap <String, HashMap<String, Integer>> envTable) {
        this.envTable = envTable;
        // initialize
        msg_counter = 0;

        text_seg =      ".text\n" +
                "\n" +
                "  jal Main                                 # Jump to main\n" +
                "  li a0, @exit                             # Code for ecall: exit\n" +
                "  ecall\n\n";


        ecall = "  .equiv @sbrk, 9\n" +
                "  .equiv @print_string, 4\n" +
                "  .equiv @print_char, 11\n" +
                "  .equiv @print_int, 1\n" +
                "  .equiv @exit 10\n" +
                "  .equiv @exit2, 17\n" +
                "\n";

        error = "# Print the error message at a0 and ends the program\n" +
                ".globl error\n" +
                "error:\n" +
                "  mv a1, a0                                # Move msg address to a1\n" +
                "  li a0, @print_string                     # Code for print_string ecall\n" +
                "  ecall                                    # Print error message in a1\n" +
                "  li a1, 10                                # Load newline character\n" +
                "  li a0, @print_char                       # Code for print_char ecall\n" +
                "  ecall                                    # Print newline\n" +
                "  li a0, @exit                             # Code for exit ecall\n" +
                "  ecall                                    # Exit with code\n" +
                "abort_17:                                  # Infinite loop\n" +
                "  j abort_17                               # Prevent fallthrough\n" +
                "\n";

        //allocates bytes on the heap, returns pointer to start in a0
        alloc = "# Allocate a0 bytes on the heap, returns pointer to start in a0\n" +
                ".globl alloc\n" +
                "alloc:\n" +
                "  mv a1, a0                                # Move requested size to a1\n" +
                "  li a0, @sbrk                             # Code for ecall: sbrk\n" +
                "  ecall                                    # Request a1 bytes\n" +
                "  jr ra                                    # Return to caller\n" +
                "\n";

        data_seg = ".data\n\n";
    }

    @Override
    public void visit(Program n) {
        for (FunctionDecl fd : n.funDecls){
            fd.accept(this);
        }
    }

    @Override
    public void visit(FunctionDecl fd) {
        String name = fd.functionName.toString();
        text_seg += ".globl " + name + "\n" +
                name + ":\n";

        // dynamically allocate out array on the stack upon calling
        int in_length = fd.formalParameters.size();


        text_seg += "  sw fp, -8(sp)                        # Store old frame pointer\n" +
                    "  mv fp, sp                            # Set new fp\n";

        int frame_size = 8 + envTable.get(name).size() * 4;

        text_seg += "  li t6, " + frame_size + "\n" +
                    "  sub sp, sp, t6                       # Allocate a new frame of size " + frame_size + " bytes\n" +
                    "  sw ra, -4(fp)                        # Store return address\n";

        fd.block.accept(this);

        text_seg += "  lw ra, -4(fp)                        # Restore ra register\n" +
                    "  lw fp, -8(fp)                        # Restore old fp\n" +
                    "  addi sp, sp, " + frame_size + "\n" +
                    "  addi sp, sp, " + (in_length * 4) + "\n" +
                    "  jr ra                                # Return\n\n";
    }

    @Override
    public void visit(Block block) {
        for (Instruction i : block.instructions){
            i.accept(this);
        }
        Identifier ret = block.return_id;
        HashMap<String, Integer> context = envTable.get(block.parent.functionName.name);
        if (context.containsKey(ret.toString())){
            text_seg += "  lw a0, " + context.get(ret.toString()) + "(fp)" + " \t\t\t\t# Load return value to a0\n";
        } else {
            int index = find_index(block.parent,ret.toString());
            assert index != -1; // otherwise, this is a undefined id
            int offset = index * 4;
            text_seg += "  lw a0, " + offset + "(fp)" + " \t\t\t\t# Load return value to a0\n";
        }
    }

    @Override
    public void visit(LabelInstr n) {
        text_seg += n.label.toString() + ":\n";
    }

    @Override
    public void visit(Move_Reg_Integer mov) {
        text_seg += "  li " + mov.lhs.toString() + ", " + mov.rhs + "\n";
    }

    @Override
    public void visit(Move_Reg_FuncName mov) {
        text_seg += "  la " + mov.lhs.toString() + ", " + mov.rhs + "\n";
    }

    @Override
    public void visit(Add add) {
        text_seg += "  add " + add.lhs.toString() + ", " + add.arg1.toString() + ", " + add.arg2.toString() +
                " \t\t\t\t# " + add.lhs.toString() + " = " + add.arg1.toString() + " + " + add.arg2.toString() + "\n";
    }

    @Override
    public void visit(Subtract sub) {
        text_seg += "  sub " + sub.lhs.toString() + ", " + sub.arg1.toString() + ", " + sub.arg2.toString() +
                " \t\t\t\t# " + sub.lhs.toString() + " = " + sub.arg1.toString() + " - " + sub.arg2.toString()  + "\n";
    }

    @Override
    public void visit(Multiply mul) {
        text_seg += "  mul " + mul.lhs.toString() + ", " + mul.arg1.toString() + ", " + mul.arg2.toString() +
                " \t\t\t\t# " + mul.lhs.toString() + " = " + mul.arg1.toString() + " - " + mul.arg2.toString()  + "\n";
    }

    @Override
    public void visit(LessThan lt) {
        text_seg += "  slt " + lt.lhs.toString() + ", " + lt.arg1.toString() + ", " + lt.arg2.toString() +
                " \t\t\t\t# " + lt.lhs.toString() + " = " + lt.arg1.toString() + " - " + lt.arg2.toString()  + "\n";
    }

    @Override
    public void visit(Load load) {
        text_seg += "  lw " + load.lhs.toString() + ", " + load.offset + "(" + load.base.toString() + ")\n";
    }

    @Override
    public void visit(Store store) {
        text_seg += "  sw " + store.rhs.toString() + ", " + store.offset + "(" + store.base.toString() + ")\n";
    }

    @Override
    public void visit(Move_Reg_Reg mov) {
        text_seg += "  mv " + mov.lhs.toString() + ", " + mov.rhs.toString() + "\n";
    }

    @Override
    public void visit(Move_Id_Reg mov) {
        String id = mov.lhs.toString();
        if (envTable.get(mov.parent.parent.functionName.name).containsKey(id)){
            text_seg += "  sw " + mov.rhs.toString() + ", " +
                    envTable.get(mov.parent.parent.functionName.name).get(id) + "(fp)" + "\n";
        } else {
            int index = find_index(mov.parent.parent,id);
            assert index != -1; // otherwise, this is a undefined id
            int offset = index * 4;
            text_seg += "  sw " + mov.rhs.toString() + ", " + offset + "(fp)" + "\n";
        }
    }

    @Override
    public void visit(Move_Reg_Id mov) {
        String id = mov.rhs.toString();
        if (envTable.get(mov.parent.parent.functionName.name).containsKey(id)){
            text_seg += "  lw " + mov.lhs.toString() + ", " +
                    envTable.get(mov.parent.parent.functionName.name).get(id) + "(fp)" + "\n";
        } else {
            int index = find_index(mov.parent.parent,id);
            assert index != -1; // otherwise, this is a undefined id
            int offset = index * 4;
            text_seg += "  lw " + mov.lhs.toString() + ", " + offset + "(fp)" + "\n";
        }
    }

    @Override
    public void visit(Alloc alloc) {
        // store the pointer at register
        text_seg += "  mv a0, " + alloc.size.toString() + " \t\t\t\t# Move requested size to a0\n" +
                    "  jal alloc" + " \t\t\t\t# Call alloc subroutine to request heap memory\n" +
                    "  mv " + alloc.lhs.toString() + ", a0" + " \t\t\t\t# Move the returned pointer to " +
                alloc.lhs.toString() + "\n";
    }

    @Override
    public void visit(Print print) {
        text_seg += "  mv a1, " + print.content.toString()   + " \t\t\t\t# Mov the content to be printed to a1\n" +
                "  li a0, @print_int" + " \t\t\t\t# Load the code for print_int to a0\n" +
                "  ecall"             + " \t\t\t\t# Print the number\n";
    }

    @Override
    public void visit(ErrorMessage error) {
        String msg = error.msg + "\n";
        data_seg += ".globl msg_" + msg_counter + "\n" +
                    "msg_"+ msg_counter + ":\n" +
                    "  .asciiz " + msg  +
                    "  .align 2\n" +
                    "\n";

        text_seg += "  la a0, msg_" + msg_counter + " \t\t\t\t# Load the address of the error message to a0\n" +
                    "  j error\n";

        msg_counter ++;
    }

    @Override
    public void visit(Goto n) {
        text_seg += "  j " + n.label.toString() + "\n";
    }

    @Override
    public void visit(IfGoto ifgoto) {
        String r = ifgoto.condition.toString();
        String l = ifgoto.label.toString();
        text_seg += "  beqz " + r + ", " + l + " \t\t\t\t# Branch jump to " + l + " if " + r + " is zero\n";
    }

    @Override
    public void visit(Call call) {
        int out_length = call.args.size();
        text_seg += "  li t6, " + (out_length * 4) + " \t\t\t\t# Dynamically allocate out array on the stack upon calling\n";
        text_seg += "  sub sp, sp, t6\n";

        for (Identifier param : call.args){
            String id = param.toString();
            assert envTable.containsKey(call.parent.parent.functionName.name);
            HashMap<String , Integer> context = envTable.get(call.parent.parent.functionName.name);
            if (context.containsKey(id)){
                text_seg += "  lw t6, " + context.get(id) + "(fp)" + " \t\t\t\t# Load value to t6\n";
            } else {
                int index = find_index(call.parent.parent, id);
                assert index != -1; // otherwise, this is a undefined id
                int offset = index * 4;
                text_seg += "  lw t6, " + offset + "(fp)" + " \t\t\t\t# Load value to t6\n";
            }
            text_seg += "  sw t6, " + (call.args.indexOf(param) * 4) + "(sp)" + " \t\t\t\t# Push argument onto the stack\n";
        }
        text_seg += "  jalr " + call.callee.toString() + "\n";
        text_seg += "  mv " + call.lhs.toString() + ", a0" + " \t\t\t\t# Move return value from a0\n";
    }
}
