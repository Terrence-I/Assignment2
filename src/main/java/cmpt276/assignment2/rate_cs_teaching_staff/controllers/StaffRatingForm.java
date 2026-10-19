package cmpt276.assignment2.rate_cs_teaching_staff.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import cmpt276.assignment2.rate_cs_teaching_staff.models.RoleType;
import cmpt276.assignment2.rate_cs_teaching_staff.models.StaffRatings;

public class StaffRatingForm {
    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name must be 100 characters or fewer.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid email address.")
    @Size(max = 254, message = "Email must be 254 characters or fewer.")
    private String email;

    @NotNull(message = "Choose a staff role.")
    private RoleType roleType;

    @Min(value = 1, message = "Clarity must be between 1 and 10.")
    @Max(value = 10, message = "Clarity must be between 1 and 10.")
    private int clarity;

    @Min(value = 1, message = "Niceness must be between 1 and 10.")
    @Max(value = 10, message = "Niceness must be between 1 and 10.")
    private int niceness;

    @Min(value = 1, message = "Knowledge score must be between 1 and 10.")
    @Max(value = 10, message = "Knowledge score must be between 1 and 10.")
    private int knowledgeableScore;

    @Size(max = 1000, message = "Comment must be 1000 characters or fewer.")
    private String comment;

    public static StaffRatingForm from(StaffRatings rating) {
        StaffRatingForm form = new StaffRatingForm();
        form.setName(rating.getName());
        form.setEmail(rating.getEmail());
        form.setRoleType(rating.getRoleType());
        form.setClarity(rating.getClarity());
        form.setNiceness(rating.getNiceness());
        form.setKnowledgeableScore(rating.getKnowledgeableScore());
        form.setComment(rating.getComment());
        return form;
    }

    public void applyTo(StaffRatings rating) {
        rating.setName(name);
        rating.setEmail(email);
        rating.setRoleType(roleType);
        rating.setClarity(clarity);
        rating.setNiceness(niceness);
        rating.setKnowledgeableScore(knowledgeableScore);
        rating.setComment(comment);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public int getClarity() {
        return clarity;
    }

    public void setClarity(int clarity) {
        this.clarity = clarity;
    }

    public int getNiceness() {
        return niceness;
    }

    public void setNiceness(int niceness) {
        this.niceness = niceness;
    }

    public int getKnowledgeableScore() {
        return knowledgeableScore;
    }

    public void setKnowledgeableScore(int knowledgeableScore) {
        this.knowledgeableScore = knowledgeableScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
