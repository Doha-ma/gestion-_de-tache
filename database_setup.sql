-- Script SQL pour créer la base de données Virtual Campus Hub
-- Exécutez ce script dans votre serveur MySQL

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS virtual_campus_hub
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE virtual_campus_hub;

-- Créer la table users
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Créer un index sur l'email pour des recherches plus rapides
CREATE INDEX idx_email ON users(email);

-- Créer un index sur le username pour des recherches plus rapides
CREATE INDEX idx_username ON users(username);

-- Insérer quelques utilisateurs de test (mots de passe en clair pour les tests)
-- En production, utilisez toujours des mots de passe hashés !
INSERT INTO users (username, email, password) VALUES
('admin', 'admin@virtualcampus.com', 'admin123'),
('student1', 'student1@virtualcampus.com', 'password123'),
('professor', 'professor@virtualcampus.com', 'prof123')
ON DUPLICATE KEY UPDATE
username = VALUES(username),
email = VALUES(email);

-- Afficher la structure de la table
DESCRIBE users;

-- Afficher les données insérées
SELECT * FROM users;