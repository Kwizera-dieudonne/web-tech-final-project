package com.furniture.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.furniture.domain.Category;
import com.furniture.domain.Furniture;
import com.furniture.service.FurnitureService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/furniture")
public class FurnitureController {
    @Autowired
    private FurnitureService furnitureService;

    @PostMapping(value = "/saveFurniture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFurniture(@ModelAttribute Furniture furniture, @RequestParam("file") MultipartFile file) {
        try {
            furnitureService.createFurniture(furniture, file);
            return new ResponseEntity<>("Furniture added successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Furniture not added: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllFurniture() {
    List<Furniture> furnitureList = furnitureService.getAllFurniture();

    List<Map<String, Object>> response = furnitureList.stream()
        .map(furniture -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", furniture.getId());
            map.put("name", furniture.getName());
            map.put("price", furniture.getPrice());
           // map.put("quantity", furniture.getQuantity());
            map.put("imageUrl", furniture.getImageUrl());
            map.put("category", furniture.getCategory().getName());
            return map;
        })
        .toList();

    return response;
}

   
    @GetMapping("/{id}")
    public ResponseEntity<Furniture> getFurnitureById(@PathVariable Long id) {
        Optional<Furniture> furniture = furnitureService.getFurnitureById(id);
        return furniture.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Furniture> updateFurniture(@PathVariable Long id, @RequestBody Furniture furnitureDetails) {
        Optional<Furniture> furniture = furnitureService.getFurnitureById(id);
        if (furniture.isPresent()) {
            Furniture updatedFurniture = furniture.get();
            updatedFurniture.setName(furnitureDetails.getName());
            //updatedFurniture.setDescription(furnitureDetails.getDescription());
            updatedFurniture.setPrice(furnitureDetails.getPrice());
           // updatedFurniture.setQuantity(furnitureDetails.getQuantity());
            return ResponseEntity.ok(furnitureService.updateFurniture(updatedFurniture));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFurniture(@PathVariable Long id) {
        Optional<Furniture> furniture = furnitureService.getFurnitureById(id);
        if (furniture.isPresent()) {
            furnitureService.deleteFurniture(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }   

    /*@GetMapping("/search")
public ResponseEntity<List<Map<String, Object>>> searchFurniture(@RequestParam("query") String query) {
    List<Furniture> furnitureList = furnitureService.searchFurniture(query);

    List<Map<String, Object>> response = furnitureList.stream()
        .map(furniture -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", furniture.getId());
            map.put("name", furniture.getName());
            map.put("price", furniture.getPrice());
            map.put("quantity", furniture.getQuantity());
            map.put("imageUrl", furniture.getImageUrl());
            map.put("category", furniture.getCategory().getName());
            return map;
        })
        .toList();

    return ResponseEntity.ok(response);
}*/

@GetMapping("/by-category/{categoryId}")
public ResponseEntity<List<Furniture>> getFurnitureByCategory(@PathVariable Long categoryId) {
    Category category = new Category(); // Assuming Category is an entity with an ID field.
    category.setId(categoryId); // Set the selected category ID.
    List<Furniture> furnitureList = furnitureService.getFurnitureByCategory(category);
    return ResponseEntity.ok(furnitureList);
}

@GetMapping("/search")
public ResponseEntity<List<Furniture>> globalSearch(@RequestParam("query") String query) {
    List<Furniture> searchResults = furnitureService.globalSearch(query);
    if (searchResults.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(searchResults);
}

}

