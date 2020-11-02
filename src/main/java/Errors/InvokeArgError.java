package Errors;

public class InvokeArgError extends Error {
    public InvokeArgError(String m, String c, String m_call) {
        super("Method Call: " + m_call + " Method: " + m + " Class: " + c);
    }

}
