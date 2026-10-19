package com.marketplace.userservice.controller;

import com.marketplace.userservice.model.Product;
import com.marketplace.userservice.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/seller/{sellerId}")
    public List<Product> getBySellerId(@PathVariable String sellerId) {
        return service.getBySellerId(sellerId);
    }

    @GetMapping("/seller/current")
    public List<Product> getByCurrentSeller() {
        return service.getForCurrentSeller();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.create(product);
    }

    @PostMapping("/with-image")
    public Product createWithImage(@RequestPart("product") Product product,
                                   @RequestPart("image") MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        product.setImageUrl("/uploads/" + fileName);

        return service.create(product);
    }

    @PutMapping("/{id}/with-image")
    public Product updateWithImage(@PathVariable String id,
                                   @RequestPart("product") Product product,
                                   @RequestPart("image") MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        product.setImageUrl("/uploads/" + fileName);

        return service.update(id, product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
