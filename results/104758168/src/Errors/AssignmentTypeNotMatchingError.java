package Errors;

public class AssignmentTypeNotMatchingError extends Error {
    public AssignmentTypeNotMatchingError(String l, String r) {
        super("LeftType: " + l + " RightType: " + r);
    }
}
