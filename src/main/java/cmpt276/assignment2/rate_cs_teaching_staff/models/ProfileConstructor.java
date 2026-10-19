package cmpt276.assignment2.rate_cs_teaching_staff.models;

public class ProfileConstructor {
    private ProfileConstructor() {
    }

    public static StaffMemberProfile create(RoleType role) {
        if (role == null) {
            return new DEFProfile();
        }
        return switch (role) {
            case TA -> new TAProfile();
            case PROF -> new ProfessorProfile();
            case INSTRUCTOR -> () -> "Instructor";
            case STAFF -> () -> "Staff";
        };
    }
}