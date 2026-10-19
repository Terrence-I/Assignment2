package cmpt276.assignment2.rate_cs_teaching_staff.models;

public class ProfessorProfile implements StaffMemberProfile {
    @Override
    public String displayTitle() {
        return "Professor";
    }
}