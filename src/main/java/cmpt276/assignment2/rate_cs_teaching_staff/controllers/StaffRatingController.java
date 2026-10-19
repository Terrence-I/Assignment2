package cmpt276.assignment2.rate_cs_teaching_staff.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import cmpt276.assignment2.rate_cs_teaching_staff.models.RoleType;
import cmpt276.assignment2.rate_cs_teaching_staff.models.StaffRatings;
import cmpt276.assignment2.rate_cs_teaching_staff.repository.StaffRatingRepository;

@Controller
public class StaffRatingController {
    private final StaffRatingRepository staffRatingRepository;

    public StaffRatingController(StaffRatingRepository staffRatingRepository) {
        this.staffRatingRepository = staffRatingRepository;
    }

    @GetMapping("/ratings/new")
    public String newRating(Model model) {
        model.addAttribute("rating", new StaffRatingForm());
        model.addAttribute("roles", RoleType.values());
        return "ratings/create";
    }

    @GetMapping({"/", "/ratings/main"})
    public String showRatings(Model model) {
        model.addAttribute("ratings", staffRatingRepository.findAll());
        return "ratings/showall";
    }

    @PostMapping("/ratings/add")
    public String addRating(@Valid @ModelAttribute("rating") StaffRatingForm form,
                            BindingResult bindingResult, Model model) {
        if (form.getEmail() != null && staffRatingRepository.existsByEmail(form.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "This email has already been used for a rating.");
        }
        if (bindingResult.hasErrors()) {
            return showCreateForm(model);
        }

        StaffRatings rating = new StaffRatings();
        form.applyTo(rating);
        staffRatingRepository.save(rating);
        return "redirect:/ratings/main";
    }

    @GetMapping("/ratings/{id}")
    public String showRating(@PathVariable long id, Model model) {
        model.addAttribute("rating", findRating(id));
        return "ratings/details";
    }

    @GetMapping("/ratings/{id}/edit")
    public String editRating(@PathVariable long id, Model model) {
        model.addAttribute("rating", StaffRatingForm.from(findRating(id)));
        model.addAttribute("ratingId", id);
        model.addAttribute("roles", RoleType.values());
        return "ratings/edit";
    }

    @PostMapping("/ratings/{id}/update")
    public String updateRating(@PathVariable long id,
                               @Valid @ModelAttribute("rating") StaffRatingForm form,
                               BindingResult bindingResult, Model model) {
        StaffRatings existing = findRating(id);
        if (form.getEmail() != null && !form.getEmail().equals(existing.getEmail())
                && staffRatingRepository.existsByEmailAndUidNot(form.getEmail(), id)) {
            bindingResult.rejectValue("email", "duplicate", "This email has already been used for a rating.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("ratingId", id);
            model.addAttribute("roles", RoleType.values());
            return "ratings/edit";
        }

        form.applyTo(existing);
        staffRatingRepository.save(existing);
        return "redirect:/ratings/" + id;
    }

    @GetMapping("/ratings/{id}/delete")
    public String deletePage(@PathVariable long id, Model model) {
        model.addAttribute("rating", findRating(id));
        return "ratings/delete";
    }

    @PostMapping("/ratings/{id}/delete")
    public String deleteRating(@PathVariable long id) {
        StaffRatings rating = findRating(id);
        staffRatingRepository.delete(rating);
        return "redirect:/ratings/main";
    }

    private String showCreateForm(Model model) {
        model.addAttribute("roles", RoleType.values());
        return "ratings/create";
    }

    private StaffRatings findRating(long id) {
        return staffRatingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating not found"));
    }
}