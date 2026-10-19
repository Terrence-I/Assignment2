package cmpt276.assignment2.rate_cs_teaching_staff.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;


import cmpt276.assignment2.rate_cs_teaching_staff.models.RoleType;
import cmpt276.assignment2.rate_cs_teaching_staff.models.StaffRatings;
import cmpt276.assignment2.rate_cs_teaching_staff.repository.StaffRatingRepository;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class StaffRatingController {
    private final StaffRatingRepository staffRatingRepository;

    public StaffRatingController(StaffRatingRepository staffRatingRepository) {
        this.staffRatingRepository = staffRatingRepository;
    }

   @GetMapping("/ratings/new")
    public String newRating(Model model) {

        model.addAttribute("rating", new StaffRatings());
        model.addAttribute("roles", RoleType.values());

        return "ratings/create";
    }

    @GetMapping("/ratings/main")
    public String showRatings(Model model) {
            model.addAttribute("ratings", staffRatingRepository.findAll());
            return "ratings/showall";
    }

    @PostMapping("/ratings/add")
    public String addRating(@Valid @ModelAttribute("rating") StaffRatings rating,BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", RoleType.values());
            return "ratings/create";
        }

        staffRatingRepository.save(rating);
        return "redirect:/ratings/main";
    }


    @GetMapping("/ratings/{id}")
    public String showRating(@PathVariable  long id, Model model) {
            StaffRatings rating = staffRatingRepository.findById(id).orElseThrow();
            model.addAttribute("rating",rating);
        
            return "ratings/details";
    }
    
    @GetMapping("/ratings/{id}/edit")
    public String editRating( @PathVariable long id, Model model) {
        StaffRatings rating = staffRatingRepository.findById(id).orElseThrow();
        model.addAttribute("rating", rating);
        model.addAttribute("roles", RoleType.values());

        return "ratings/edit";
    }

    @PostMapping("/ratings/{id}/update")
    public String updateRating(@PathVariable long id,@Valid @ModelAttribute("rating") StaffRatings rating,BindingResult bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", RoleType.values());
            return "ratings/edit";
        }

        rating.setUid(id);

        StaffRatings existing = staffRatingRepository.findById(id)
            .orElseThrow();

        existing.setName(rating.getName());
        existing.setEmail(rating.getEmail());
        existing.setRoleType(rating.getRoleType());
        existing.setClarity(rating.getClarity());
        existing.setNiceness(rating.getNiceness());
        existing.setKnowledgeableScore(rating.getKnowledgeableScore());
        existing.setComment(rating.getComment());

        staffRatingRepository.save(existing);

        return "redirect:/ratings/" + id;
    }

    @GetMapping("/ratings/{id}/delete")
    public String deletePage(@PathVariable long id,Model model) {

        model.addAttribute("rating",staffRatingRepository.findById(id).orElseThrow());

        return "ratings/delete";
    }

    @PostMapping("/ratings/{id}/delete")
    public String deleteRating(@PathVariable long id) {

        staffRatingRepository.deleteById(id);

        return "redirect:/ratings/main";
    }

    @GetMapping("/ratings/{id}/view")
    public String viewRating(@PathVariable long id, Model model) {
        StaffRatings rating = staffRatingRepository.findById(id).orElseThrow();
        model.addAttribute("rating", rating);

        return "ratings/view";
    }
}