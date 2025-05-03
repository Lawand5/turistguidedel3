package com.example.touristguidetwo.service;

import com.example.touristguidetwo.model.City;
import com.example.touristguidetwo.model.Tags;
import com.example.touristguidetwo.model.TouristAttraction;
import com.example.touristguidetwo.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TouristService {

    private final TouristRepository touristRepository;

    public TouristService(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }

    public List<TouristAttraction> getAllTouristAttractions() {
        return touristRepository.getAllTouristAttractions();
    }

    public TouristAttraction findTouristAttractionByName(String name) {
        return touristRepository.findTouristAttractionByName(name);
    }

    public List<TouristAttraction> findTouristAttractionByNameAndTags(String name, List<Tags> tags) {
        return touristRepository.findTouristAttractionByNameAndTags(name, tags);
    }

    public void addTouristAttraction(TouristAttraction touristAttraction) {
        touristRepository.addTouristAttraction(touristAttraction);
    }

    public void deleteTouristAttraction(String name) {
        TouristAttraction touristAttraction = findTouristAttractionByName(name);
        if (touristAttraction != null) {
            touristRepository.removeTouristAttraction(name);
        }
    }

    public void editTouristAttraction(String name, List<Tags> tags) {
        TouristAttraction touristAttraction = findTouristAttractionByName(name);
        if (touristAttraction != null) {
            touristAttraction.setTags(tags);
            touristRepository.updateTouristAttraction(touristAttraction);
        }
    }

    public void updateTouristAttraction(TouristAttraction touristAttraction) {
        TouristAttraction existingAttraction = findTouristAttractionByName(touristAttraction.getName());
        if (existingAttraction != null) {
            existingAttraction.setCity(touristAttraction.getCity());
            existingAttraction.setDescription(touristAttraction.getDescription());
            existingAttraction.setTags(touristAttraction.getTags());
            touristRepository.updateTouristAttraction(existingAttraction);
        }
    }

    public List<String> getCities() {
        return Arrays.stream(City.values())
                .map(Enum::name)
                .toList();
    }

    public List<String> getTags() {
        return Arrays.stream(Tags.values())
                .map(Enum::name)
                .toList();
    }
}