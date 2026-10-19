package com.marketplace.userservice.model.dto;

public class UserDto {
    private String nom;
    private String role;
    private String accessToken;

    public UserDto(String accessToken, String role, String nom) {
        this.accessToken = accessToken;
        this.role = role;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
