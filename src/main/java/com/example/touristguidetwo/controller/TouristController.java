package com.example.touristguidetwo.controller;

import com.example.touristguidetwo.model.Tags;
import com.example.touristguidetwo.model.TouristAttraction;
import com.example.touristguidetwo.service.TouristService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attractions")
public class TouristController {

    private final TouristService touristService;

    public TouristController(TouristService touristService){

        this.touristService = touristService;
    }

    @GetMapping("")
    public String getAllTouristAttractions(Model model) {
        List<TouristAttraction> touristAttractions = touristService.getAllTouristAttractions();
        model.addAttribute("touristAttractions", touristAttractions);
        return "attractionList";
    }

    @GetMapping("/{name}")
    public String getTouristAttractionByName(@PathVariable String name, Model model) {
        TouristAttraction touristAttraction = touristService.findTouristAttractionByName(name);
            model.addAttribute("touristAttraction", touristAttraction);
            return "touristAttractionDetail";
    }

@GetMapping("/{name}/tags")
public String getTouristAttractionByNameAndTags(@PathVariable String name, Model model) {
    TouristAttraction touristAttraction = touristService.findTouristAttractionByName(name);
    model.addAttribute("touristAttraction", touristAttraction);
    return "tags";
}

    @PostMapping("/add")
    public String addTouristAttraction(@ModelAttribute TouristAttraction touristAttraction) {
        touristService.addTouristAttraction(touristAttraction);
        return "redirect:/attractions/";
    }


    @GetMapping("/{name}/edit")
    public String editTouristAttraction(@PathVariable String name, Model model) {
        TouristAttraction touristAttraction = touristService.findTouristAttractionByName(name);
        if (touristAttraction != null) {
            model.addAttribute("touristAttraction", touristAttraction);
            model.addAttribute("cities", touristService.getCities());
            model.addAttribute("tags", touristService.getTags());
            return "editAttraction";
        }
        model.addAttribute("error", "Turristattraktion blev ikke fundet.");
        return "redirect:/attractions";
    }

    @PostMapping("/edit")
    public String updateTouristAttraction(@ModelAttribute TouristAttraction touristAttraction) {
        if (touristAttraction.getName() == null) {
            return "redirect:/attractions";
        }

        TouristAttraction existingAttraction = touristService.findTouristAttractionByName(touristAttraction.getName());
        if (existingAttraction != null) {
            existingAttraction.setCity(touristAttraction.getCity());
            existingAttraction.setDescription(touristAttraction.getDescription());
            existingAttraction.setTags(touristAttraction.getTags());
            touristService.updateTouristAttraction(existingAttraction);
            return "redirect:/attractions";
        } else {
            return "redirect:/attractions";
        }
    }

    @PostMapping("/delete/{name}")
    public String deleteTouristAttraction(@PathVariable String name) {
        TouristAttraction touristAttraction = touristService.findTouristAttractionByName(name);
        if (touristAttraction != null){
            touristService.deleteTouristAttraction(name);
        }
            return "redirect:/attractions";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<String> cities = touristService.getCities();
        List<String> tags = Arrays.stream(Tags.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        model.addAttribute("cities", cities);
        model.addAttribute("tags", tags);
        model.addAttribute("touristAttraction", new TouristAttraction());

        return "addAttraction";
    }

    @PostMapping("/save")
    public String saveTouristAttraction(@ModelAttribute TouristAttraction touristAttraction) {
        List<Tags> selectedTags = touristAttraction.getTags().stream()
                .map(tag -> Tags.valueOf(tag.toString()))
                .collect(Collectors.toList());

        touristAttraction.setTags(selectedTags);
        touristService.addTouristAttraction(touristAttraction);

        return "redirect:/attractions";
    }
}


