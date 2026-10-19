package cmpt276.assignment2.rate_cs_teaching_staff.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "staffratings")
public class StaffRatings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;

    @NotBlank(message ="Name is Required")
    @Size(max = 100, message="Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message ="Email is Required")
    @Email(message ="Enter a Valid email Address")
    @Size(max = 254)
    @Column(unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Min(1)
    @Max(10)
    private int clarity;

    @Min(1)
    @Max(10)
    private int niceness;

    @Min(1)
    @Max(10)
    private int knowledgeableScore;

    @Size(max = 1000)
    private String comment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StaffRatings(){
    }
    public StaffRatings(long uid, String name, String email, RoleType roleType,
                        int clarity, int niceness, int knowledgeableScore, String comment,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.roleType = roleType;
        this.clarity = clarity;
        this.niceness = niceness;
        this.knowledgeableScore = knowledgeableScore;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getOverallScore(){
        double score = ((clarity + knowledgeableScore + niceness) / 3.0);
        return score;
    }

    public String getRoleTitle() {
        return ProfileConstructor.create(roleType).displayTitle();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}