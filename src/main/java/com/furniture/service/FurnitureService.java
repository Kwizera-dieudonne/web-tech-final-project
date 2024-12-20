package com.furniture.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.furniture.domain.Category;
import com.furniture.domain.Furniture;
import com.furniture.repository.FurnitureRepository;

@Service
public class FurnitureService {


    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private FurnitureRepository furnitureRepository;

    public Furniture createFurniture(Furniture furniture, MultipartFile file) throws IOException {
        String imagePath = saveImage(file);
        furniture.setImagePath(imagePath);
        return furnitureRepository.save(furniture);
    }

    private String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path directory = Paths.get(uploadPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        Path filePath = directory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

    public List<Furniture> getAllFurniture() {
        return furnitureRepository.findAll();
    }

    public Optional<Furniture> getFurnitureById(Long id) {
        return furnitureRepository.findById(id);
    }

    public Furniture updateFurniture(Furniture furniture) {
        return furnitureRepository.save(furniture);
    }

    public void deleteFurniture(Long id) {
        furnitureRepository.deleteById(id);
    }

    public List<Furniture> getFurnitureByCategory(Category category) {
        return furnitureRepository.findByCategory(category);
    }

    public List<Furniture> searchFurniture(String query) {
        return furnitureRepository.findAll().stream()
            .filter(furniture -> furniture.getName().toLowerCase().contains(query.toLowerCase()) ||
                                 furniture.getCategory().getName().toLowerCase().contains(query.toLowerCase()))
            .toList();
    }

    public List<Furniture> globalSearch(String query) {
        return furnitureRepository.globalSearch(query.toLowerCase());
    }
}
