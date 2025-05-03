package com.example.touristguidetwo.model;

import java.util.List;

public class TouristAttraction {
    private int id;
    private String name;
    private String description;
    private City city;
    private List<Tags> tags;

    // Konstruktor
    public TouristAttraction(String name, String description, City city, List<Tags> tags) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.tags = tags;
    }

    public TouristAttraction() {
        this.name = "";
        this.description = "";
        this.city = City.København;
        this.tags = List.of();
    }


    // Getters og setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

