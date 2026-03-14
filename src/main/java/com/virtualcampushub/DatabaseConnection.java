package com.virtualcampushub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/virtual_campus_hub";
    private static final String USER = "root"; // Changez selon votre configuration
    private static final String PASSWORD = ""; // Changez selon votre configuration

    private static Connection connection = null;

    /**
     * Établit et retourne la connexion à la base de données MySQL
     * @return Connection à la base de données
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Charger le driver MySQL (optionnel depuis JDBC 4.0)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Établir la connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la base de données établie avec succès.");

                // Configuration de la connexion
                connection.setAutoCommit(true);

            } catch (ClassNotFoundException e) {
                System.err.println("Driver MySQL non trouvé: " + e.getMessage());
                throw new SQLException("Driver MySQL non trouvé", e);
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }

    /**
     * Teste la connexion à la base de données
     * @return true si la connexion est réussie, false sinon
     */
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            System.err.println("Test de connexion échoué: " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie si la connexion est active
     * @return true si la connexion est active, false sinon
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}