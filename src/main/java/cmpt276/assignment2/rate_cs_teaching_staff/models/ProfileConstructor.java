package cmpt276.assignment2.rate_cs_teaching_staff.models;

public class ProfileConstructor {
   public static StaffMemberProfile create(RoleType role){
        return switch(role){
            case TA -> new TAProfile();
            case PROF  -> new ProfessorProfile();
            default -> new DEFProfile(); 
        };
   }
}