package com.virtualcampushub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Enregistre un nouvel utilisateur dans la base de données
     * @param username Le nom d'utilisateur
     * @param email L'adresse email
     * @param password Le mot de passe (doit être hashé avant l'appel)
     * @return true si l'enregistrement est réussi, false sinon
     */
    public static boolean registerUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password); // Le mot de passe devrait être hashé

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie les informations de connexion d'un utilisateur
     * @param email L'adresse email
     * @param password Le mot de passe
     * @return User object si la connexion est réussie, null sinon
     */
    public static User loginUser(String email, String password) {
        String sql = "SELECT id, username, email FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String userEmail = rs.getString("email");

                    return new User(id, username, userEmail);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion de l'utilisateur: " + e.getMessage());
        }

        return null; // Connexion échouée
    }

    /**
     * Vérifie si un email existe déjà dans la base de données
     * @param email L'adresse email à vérifier
     * @return true si l'email existe, false sinon
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email: " + e.getMessage());
        }

        return false;
    }

    /**
     * Vérifie si un nom d'utilisateur existe déjà dans la base de données
     * @param username Le nom d'utilisateur à vérifier
     * @return true si le nom d'utilisateur existe, false sinon
     */
    public static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du nom d'utilisateur: " + e.getMessage());
        }

        return false;
    }

    /**
     * Classe interne pour représenter un utilisateur
     */
    public static class User {
        private final int id;
        private final String username;
        private final String email;

        public User(int id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}