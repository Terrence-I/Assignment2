package cmpt276.assignment2.rate_cs_teaching_staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cmpt276.assignment2.rate_cs_teaching_staff.models.StaffRatings;

public interface StaffRatingRepository extends JpaRepository<StaffRatings, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndUidNot(String email, long uid);
}
