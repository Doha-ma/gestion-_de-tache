-- ============================================================
--  Virtual Campus Hub - Script de configuration base de données
--  À exécuter dans MySQL Workbench ou phpMyAdmin
-- ============================================================

-- 1. Créer la base si elle n'existe pas
CREATE DATABASE IF NOT EXISTS virtual_campus_hub
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE virtual_campus_hub;

-- 2. Table Utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(100)  NOT NULL UNIQUE,
    email       VARCHAR(150)  NOT NULL UNIQUE,
    password    VARCHAR(255)  NOT NULL,           -- Hash SHA-256
    full_name   VARCHAR(200),
    avatar_color VARCHAR(20)  DEFAULT '#6C63FF',
    study_hours INT           DEFAULT 0,
    avg_grade   DOUBLE        DEFAULT 0.0,
    created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Table Cours
CREATE TABLE IF NOT EXISTS courses (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(200) NOT NULL,
    teacher     VARCHAR(150),
    progress    INT          DEFAULT 0,
    color       VARCHAR(20)  DEFAULT '#6C63FF',
    user_id     INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. Table Événements
CREATE TABLE IF NOT EXISTS events (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    event_date  DATE,
    event_time  VARCHAR(10),
    user_id     INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Utilisateur de test (mot de passe: admin123)
-- SHA-256 de "admin123" = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
INSERT IGNORE INTO users (username, email, password, full_name) VALUES (
    'admin',
    'admin@campus.fr',
    '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
    'Administrateur'
);

-- 6. Cours de démonstration
INSERT IGNORE INTO courses (title, teacher, progress, color, user_id) VALUES
    ('Algorithmique & Structures de données', 'Prof. Benali',  78, '#4facfe', 1),
    ('Développement Web Full-Stack',          'Prof. Martin',  55, '#43e97b', 1),
    ('Intelligence Artificielle',             'Prof. Dupont',  91, '#6C63FF', 1),
    ('Bases de données avancées',             'Prof. Rousseau',40, '#f093fb', 1),
    ('Réseaux et Protocoles',                 'Prof. Alami',   65, '#f7971e', 1);

-- 7. Événements de démonstration
INSERT IGNORE INTO events (title, description, event_date, event_time, user_id) VALUES
    ('Examen de Physique',     'Examen final du semestre',        '2026-03-20', '09:00', 1),
    ('Soutenance de Projet',   'Présentation du projet JavaFX',   '2026-03-25', '14:00', 1),
    ('Atelier IA',             'Workshop Intelligence Artificielle','2026-04-02','10:30', 1);

-- ============================================================
--  VÉRIFICATION
-- ============================================================
SELECT 'Base de données initialisée avec succès ✅' AS status;
SELECT * FROM users;
SELECT * FROM courses;
SELECT * FROM events;
