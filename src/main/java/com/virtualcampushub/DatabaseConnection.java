package com.virtualcampushub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/virtual_campus_hub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&autoReconnect=true";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Change selon ta config MySQL

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion MySQL établie avec succès.");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver MySQL introuvable : " + e.getMessage());
                throw new SQLException("Driver MySQL manquant", e);
            } catch (SQLException e) {
                System.err.println("❌ Erreur connexion MySQL : " + e.getMessage());
                System.err.println("   → Vérifiez que MySQL est démarré sur le port 3306");
                System.err.println("   → Vérifiez les identifiants USER/PASSWORD dans DatabaseConnection.java");
                throw e;
            }
        }
        return connection;
    }

    /**
     * Initialise la base de données et crée les tables si elles n'existent pas.
     * Appelé au démarrage de l'application.
     */
    public static void initializeDatabase() {
        try {
            Connection conn = getConnection();
            createDatabaseIfNotExists(conn);
            createTables(conn);
            System.out.println("✅ Base de données initialisée.");
        } catch (SQLException e) {
            System.err.println("⚠️  Impossible d'initialiser la DB : " + e.getMessage());
            System.err.println("   → L'application fonctionnera en mode hors-ligne (données simulées).");
        }
    }

    private static void createDatabaseIfNotExists(Connection conn) throws SQLException {
        // La base est déjà sélectionnée via l'URL, on vérifie juste la connexion
        System.out.println("📂 Base de données : virtual_campus_hub");
    }

    private static void createTables(Connection conn) throws SQLException {
        String createUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(100) NOT NULL UNIQUE,
                email VARCHAR(150) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                full_name VARCHAR(200),
                avatar_color VARCHAR(20) DEFAULT '#6C63FF',
                study_hours INT DEFAULT 0,
                avg_grade DOUBLE DEFAULT 0.0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
            """;

        String createCourses = """
            CREATE TABLE IF NOT EXISTS courses (
                id INT PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(200) NOT NULL,
                teacher VARCHAR(150),
                progress INT DEFAULT 0,
                color VARCHAR(20) DEFAULT '#6C63FF',
                user_id INT,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            );
            """;

        String createEvents = """
            CREATE TABLE IF NOT EXISTS events (
                id INT PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(200) NOT NULL,
                description TEXT,
                event_date DATE,
                event_time VARCHAR(20),
                user_id INT,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            );
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createCourses);
            stmt.execute(createEvents);
            System.out.println("✅ Tables créées / vérifiées.");
        }
    }

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Connexion MySQL fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture connexion : " + e.getMessage());
        }
    }
}
