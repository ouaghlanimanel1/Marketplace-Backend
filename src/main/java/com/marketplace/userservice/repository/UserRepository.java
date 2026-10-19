package com.marketplace.userservice.repository;
import com.marketplace.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Rechercher un utilisateur par son nom d'utilisateur
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByEmail(String email);

    // Vérifier l'existence d'un utilisateur par son nom d'utilisateur
    boolean existsByUsername(String username);
}

