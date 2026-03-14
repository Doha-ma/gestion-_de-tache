import java.io.*;
import java.net.*;

public class LoginClientTest {
    public static void main(String[] args) {
        try {
            // Se connecter au serveur de l'application (port 8888)
            Socket socket = new Socket("localhost", 8888);
            System.out.println("Connecté au serveur de Virtual Campus Hub");

            // Envoyer un message de test
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Recevoir le message de bienvenue
            String welcomeMessage = in.readLine();
            System.out.println("Message du serveur: " + welcomeMessage);

            // Envoyer un message de test
            out.println("Test de connexion réseau - Client sur le même WiFi");
            System.out.println("Message envoyé au serveur");

            // Recevoir la réponse
            String response = in.readLine();
            System.out.println("Réponse du serveur: " + response);

            // Fermer la connexion
            socket.close();
            System.out.println("Connexion fermée");

        } catch (IOException e) {
            System.out.println("Erreur de connexion: " + e.getMessage());
            System.out.println("Assurez-vous que l'application Virtual Campus Hub est démarrée et connectée au même réseau WiFi");
        }
    }
}