package cmpt276.assignment2.rate_cs_teaching_staff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import cmpt276.assignment2.rate_cs_teaching_staff.models.ProfileConstructor;
import cmpt276.assignment2.rate_cs_teaching_staff.models.RoleType;
import cmpt276.assignment2.rate_cs_teaching_staff.models.StaffMemberProfile;
import cmpt276.assignment2.rate_cs_teaching_staff.models.StaffRatings;
import cmpt276.assignment2.rate_cs_teaching_staff.repository.StaffRatingRepository;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:ratings-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class RateCsTeachingStaffApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StaffRatingRepository staffRatingRepository;

	@BeforeEach
	void clearRatings() {
		staffRatingRepository.deleteAll();
	}

	@Test
	void indexRendersRatingListAndModel() throws Exception {
		staffRatingRepository.save(rating("Ada Lovelace", "ada@example.com"));

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("ratings/showall"))
				.andExpect(model().attributeExists("ratings"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Ada Lovelace")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Teaching Assistant")));
	}

	@Test
	void detailAndEditPagesRenderExistingRating() throws Exception {
		StaffRatings rating = staffRatingRepository.save(rating("Ada Lovelace", "ada@example.com"));

		mockMvc.perform(get("/ratings/{id}", rating.getUid()))
				.andExpect(status().isOk())
				.andExpect(view().name("ratings/details"))
				.andExpect(model().attributeExists("rating"));

		mockMvc.perform(get("/ratings/{id}/edit", rating.getUid()))
				.andExpect(status().isOk())
				.andExpect(view().name("ratings/edit"))
				.andExpect(model().attributeExists("rating", "roles", "ratingId"));

		mockMvc.perform(get("/ratings/{id}/delete", rating.getUid()))
				.andExpect(status().isOk())
				.andExpect(view().name("ratings/delete"));
	}

	@Test
	void createPersistsRatingAndRedirectsToIndex() throws Exception {
		mockMvc.perform(post("/ratings/add")
						.param("name", "Grace Hopper")
						.param("email", "grace@example.com")
						.param("roleType", "PROF")
						.param("clarity", "8")
						.param("niceness", "9")
						.param("knowledgeableScore", "10")
						.param("comment", "Excellent lectures"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/ratings/main"));

		StaffRatings saved = staffRatingRepository.findAll().get(0);
		assertEquals("Grace Hopper", saved.getName());
		assertEquals(RoleType.PROF, saved.getRoleType());
		assertEquals(10, saved.getKnowledgeableScore());
		assertNotNull(saved.getCreatedAt());
		assertNotNull(saved.getUpdatedAt());
	}

	@Test
	void rejectsInvalidEmailAndKeepsFormValues() throws Exception {
		mockMvc.perform(post("/ratings/add")
						.param("name", "Ada Lovelace")
						.param("email", "not-an-email")
						.param("roleType", "TA")
						.param("clarity", "8")
						.param("niceness", "9")
						.param("knowledgeableScore", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("ratings/create"))
				.andExpect(model().attributeHasFieldErrors("rating", "email"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("not-an-email")));

		assertEquals(0, staffRatingRepository.count());
	}

	@Test
	void rejectsMissingName() throws Exception {
		mockMvc.perform(post("/ratings/add")
						.param("email", "ada@example.com")
						.param("roleType", "TA")
						.param("clarity", "8")
						.param("niceness", "9")
						.param("knowledgeableScore", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("ratings/create"))
				.andExpect(model().attributeHasFieldErrors("rating", "name"));

		assertEquals(0, staffRatingRepository.count());
	}

	@Test
	void rejectsOutOfRangeScore() throws Exception {
		mockMvc.perform(post("/ratings/add")
						.param("name", "Ada Lovelace")
						.param("email", "ada@example.com")
						.param("roleType", "TA")
						.param("clarity", "11")
						.param("niceness", "9")
						.param("knowledgeableScore", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("ratings/create"))
				.andExpect(model().attributeHasFieldErrors("rating", "clarity"));

		assertEquals(0, staffRatingRepository.count());
	}

	@Test
	void rejectsDuplicateEmailWithoutSaving() throws Exception {
		staffRatingRepository.save(rating("First Staff Member", "same@example.com"));

		mockMvc.perform(post("/ratings/add")
						.param("name", "Second Staff Member")
						.param("email", "same@example.com")
						.param("roleType", "TA")
						.param("clarity", "8")
						.param("niceness", "9")
						.param("knowledgeableScore", "10"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("rating", "email"));

		assertEquals(1, staffRatingRepository.count());
	}

	@Test
	void updatePreservesCreationTimeAndUpdatesFields() throws Exception {
		StaffRatings existing = staffRatingRepository.save(rating("Old Name", "old@example.com"));
		existing = staffRatingRepository.findById(existing.getUid()).orElseThrow();
		LocalDateTime createdAt = existing.getCreatedAt();

		mockMvc.perform(post("/ratings/{id}/update", existing.getUid())
						.param("uid", "9999")
						.param("createdAt", "2000-01-01T00:00:00")
						.param("name", "New Name")
						.param("email", "new@example.com")
						.param("roleType", "PROF")
						.param("clarity", "7")
						.param("niceness", "8")
						.param("knowledgeableScore", "9")
						.param("comment", "Updated"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/ratings/" + existing.getUid()));

		StaffRatings updated = staffRatingRepository.findById(existing.getUid()).orElseThrow();
		assertEquals("New Name", updated.getName());
		assertEquals(existing.getUid(), updated.getUid());
		assertEquals(createdAt, updated.getCreatedAt());
		assertEquals("Updated", updated.getComment());
		assertTrue(!updated.getUpdatedAt().isBefore(createdAt));
		assertEquals(1, staffRatingRepository.count());
	}

	@Test
	void deleteRemovesRating() throws Exception {
		StaffRatings rating = staffRatingRepository.save(rating("Ada Lovelace", "ada@example.com"));

		mockMvc.perform(post("/ratings/{id}/delete", rating.getUid()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/ratings/main"));

		assertTrue(staffRatingRepository.findById(rating.getUid()).isEmpty());
	}

	@Test
	void roleProfilesUsePolymorphicTitles() {
		StaffMemberProfile ta = ProfileConstructor.create(RoleType.TA);
		StaffMemberProfile professor = ProfileConstructor.create(RoleType.PROF);

		assertEquals("Teaching Assistant", ta.displayTitle());
		assertEquals("Professor", professor.displayTitle());
	}

	private StaffRatings rating(String name, String email) {
		StaffRatings rating = new StaffRatings();
		rating.setName(name);
		rating.setEmail(email);
		rating.setRoleType(RoleType.TA);
		rating.setClarity(8);
		rating.setNiceness(9);
		rating.setKnowledgeableScore(10);
		rating.setComment("Helpful");
		return rating;
	}
}
