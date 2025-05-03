package com.example.touristguidetwo.repository;

import com.example.touristguidetwo.model.City;
import com.example.touristguidetwo.model.Tags;
import com.example.touristguidetwo.model.TouristAttraction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class TouristRepository {

    private final String jdbcUrl = "jdbc:mysql://localhost:3306/touristguide";
    private final String jdbcUser = "root";
    private final String jdbcPassword = "Mustafa0108";

    public List<TouristAttraction> getAllTouristAttractions() {
        List<TouristAttraction> attractions = new ArrayList<>();
        String sql = "SELECT * FROM tourist_attraction";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TouristAttraction attraction = new TouristAttraction();
                attraction.setId(rs.getInt("id"));
                attraction.setName(rs.getString("name"));
                attraction.setDescription(rs.getString("description"));

                // Prøv at læse city
                String cityStr = rs.getString("city");
                try {
                    attraction.setCity(City.valueOf(cityStr));
                } catch (IllegalArgumentException e) {
                    System.out.println("Ugyldig city-værdi i databasen: " + cityStr);
                    continue; // Spring over denne række
                }

                // Prøv at læse tags
                String tagsString = rs.getString("tags");
                if (tagsString != null && !tagsString.isBlank()) {
                    List<Tags> tags = Arrays.stream(tagsString.split(","))
                            .map(String::trim)
                            .map(tag -> {
                                try {
                                    return Tags.valueOf(tag);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Ugyldigt tag i databasen: " + tag);
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .toList();
                    attraction.setTags(tags);
                }

                attractions.add(attraction);
                System.out.println("Tilføjede attraktion: " + attraction.getName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attractions;
    }


    public TouristAttraction findTouristAttractionByName(String name) {
        String sql = "SELECT * FROM tourist_attraction WHERE name = ?";
        TouristAttraction attraction = null;

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                attraction = new TouristAttraction();
                attraction.setId(rs.getInt("id"));
                attraction.setName(rs.getString("name"));
                attraction.setDescription(rs.getString("description"));
                attraction.setCity(City.valueOf(rs.getString("city")));

                String tagsString = rs.getString("tags");
                if (tagsString != null) {
                    List<Tags> tags = Arrays.stream(tagsString.split(","))
                            .map(String::trim)
                            .map(Tags::valueOf)
                            .toList();
                    attraction.setTags(tags);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attraction;
    }

    public void addTouristAttraction(TouristAttraction attraction) {
        String sql = "INSERT INTO tourist_attraction (name, description, city, tags) VALUES (?, ?, ?, ?)";
        String tags = String.join(",", attraction.getTags().stream().map(Tags::name).toList());

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, attraction.getName());
            stmt.setString(2, attraction.getDescription());
            stmt.setString(3, attraction.getCity().name());
            stmt.setString(4, tags);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTouristAttraction(String name) {
        String sql = "DELETE FROM tourist_attraction WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTouristAttraction(TouristAttraction attraction) {
        String sql = "UPDATE tourist_attraction SET name = ?, description = ?, city = ?, tags = ? WHERE id = ?";
        String tags = String.join(",", attraction.getTags().stream().map(Tags::name).toList());

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, attraction.getName());
            stmt.setString(2, attraction.getDescription());
            stmt.setString(3, attraction.getCity().name());
            stmt.setString(4, tags);
            stmt.setInt(5, attraction.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TouristAttraction> findTouristAttractionByNameAndTags(String name, List<Tags> tagsToMatch) {
        List<TouristAttraction> matches = new ArrayList<>();
        List<TouristAttraction> all = getAllTouristAttractions();

        for (TouristAttraction attraction : all) {
            if (attraction.getName().equalsIgnoreCase(name) && attraction.getTags().containsAll(tagsToMatch)) {
                matches.add(attraction);
            }
        }

        return matches;
    }
}