package com.marketplace.userservice.service;


import com.marketplace.userservice.model.Product;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.repository.ProductRepository;
import com.marketplace.userservice.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Optional<Product> getById(String id) {
        return repository.findById(id);
    }

    public List<Product> getBySellerId(String sellerId) {
        return repository.findBySellerId(sellerId);
    }

    public List<Product> getForCurrentSeller() {
        String currentSellerId = getCurrentUserId();
        if (currentSellerId == null) {
            throw new AccessDeniedException("Utilisateur non authentifié.");
        }
        return repository.findBySellerId(currentSellerId);
    }

    public Product create(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Utilisateur non authentifié.");
        }

        String usernameOrEmail = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new AccessDeniedException("Utilisateur introuvable."));

        // Renseigner automatiquement le vendeur (affichage : partie avant '@' si c'est un email)
        product.setSellerId(user.getId());
        product.setSellerName(extractDisplayName(user));

        return repository.save(product);
    }

    public Product update(String id, Product updated) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

        // Vérifie que le vendeur connecté est bien le propriétaire du produit
        String currentSellerId = getCurrentUserId();
        if (currentSellerId == null || !currentSellerId.equals(existing.getSellerId())) {
            throw new AccessDeniedException("Vous ne pouvez modifier que vos propres produits.");
        }

        // On ne permet pas de changer le propriétaire via la requête
        updated.setId(id);
        updated.setSellerId(existing.getSellerId());

        return repository.save(updated);
    }

    public void delete(String id) {
        // On laisse le contrôle d'autorisation au filtre Spring Security
        // (DELETE /products/** réservé aux rôles SELLER et ADMIN).
        // Ici on supprime simplement le produit s'il existe.
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Produit introuvable");
        }
        repository.deleteById(id);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String usernameOrEmail = authentication.getName();

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElse(null);

        return user != null ? user.getId() : null;
    }

    private String extractDisplayName(User user) {
        if (user == null) {
            return null;
        }
        String username = user.getUsername();
        if (username == null || username.isEmpty()) {
            return user.getEmail();
        }
        int atIndex = username.indexOf('@');
        if (atIndex > 0) {
            return username.substring(0, atIndex);
        }
        return username;
    }
}
