# Virtual Campus Hub

Une application JavaFX moderne pour la gestion d'un campus virtuel avec authentification MySQL et communication réseau.

## Fonctionnalités

- 🔐 **Authentification sécurisée** avec base de données MySQL
- 👤 **Inscription d'utilisateurs** avec validation en temps réel
- 💬 **Messagerie instantanée** avec communication TCP
- 📊 **Tableau de bord** avec vue d'ensemble
- 📚 **Gestion des cours** et événements
- 👥 **Profils utilisateurs**
- 🎨 **Interface moderne** avec couleurs claires et animations

## Prérequis

- **Java 25** ou supérieur
- **MySQL 8.0** ou supérieur
- **Maven 3.6** ou supérieur
- **Windows/Linux/MacOS**

## Installation

### 1. Cloner le projet
```bash
git clone <repository-url>
cd virtual-campus-hub
```

### 2. Configurer la base de données MySQL

#### Option A: Utilisation du script fourni
```bash
# Se connecter à MySQL
mysql -u root -p

# Exécuter le script de configuration
source database_setup.sql
```

#### Option B: Configuration manuelle
```sql
-- Créer la base de données
CREATE DATABASE virtual_campus_hub;
USE virtual_campus_hub;

-- Créer la table utilisateurs
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Créer un utilisateur MySQL
CREATE USER 'vch_user'@'localhost' IDENTIFIED BY 'vch_password';
GRANT ALL PRIVILEGES ON virtual_campus_hub.* TO 'vch_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurer la connexion à la base de données

Modifiez le fichier `DatabaseConnection.java` avec vos paramètres :

```java
private static final String URL = "jdbc:mysql://localhost:3306/virtual_campus_hub";
private static final String USER = "vch_user";
private static final String PASSWORD = "vch_password";
```

### 4. Compiler et exécuter

```bash
# Compiler le projet
mvn clean compile

# Exécuter l'application
mvn exec:java -Dexec.mainClass="com.virtualcampushub.Main"
```

## Utilisation

### Première connexion
1. Lancez l'application
2. Cliquez sur "Créer un compte" pour vous inscrire
3. Remplissez le formulaire d'inscription
4. Connectez-vous avec vos identifiants

### Fonctionnalités principales

- **Dashboard**: Vue d'ensemble de votre espace étudiant
- **Cours**: Gestion et visualisation des cours
- **Événements**: Calendrier des événements du campus
- **Chat**: Messagerie instantanée avec autres utilisateurs sur le même réseau
- **Profil**: Gestion de votre profil utilisateur

## Architecture

### Classes principales

- `Main.java` - Point d'entrée de l'application
- `ViewManager.java` - Gestionnaire de vues et navigation
- `LoginView.java` - Interface de connexion
- `RegisterView.java` - Interface d'inscription
- `ChatView.java` - Interface de messagerie
- `DatabaseConnection.java` - Gestionnaire de connexion MySQL
- `UserDAO.java` - Couche d'accès aux données utilisateur

### Communication réseau

- **Port 8888**: Communication de connexion/déconnexion
- **Port 8889**: Messagerie instantanée
- Support du réseau local (192.168.x.x, 10.x.x.x)

## Sécurité

⚠️ **Important**: Cette application utilise des mots de passe en clair pour les tests. En production :

1. Implémentez le hachage des mots de passe (bcrypt, Argon2)
2. Utilisez des connexions SSL/TLS pour MySQL
3. Validez et échappez toutes les entrées utilisateur
4. Implémentez une gestion des sessions sécurisée

## Dépannage

### Erreur de connexion MySQL
- Vérifiez que MySQL est démarré
- Vérifiez les identifiants dans `DatabaseConnection.java`
- Assurez-vous que l'utilisateur a les permissions nécessaires

### Problèmes réseau
- Vérifiez que les ports 8888 et 8889 ne sont pas utilisés
- Assurez-vous que le firewall autorise les connexions locales

### Erreurs de compilation
- Vérifiez que Java 25 est installé
- Vérifiez que Maven peut télécharger les dépendances

## Technologies utilisées

- **Java 25** - Langage de programmation
- **JavaFX 25** - Framework d'interface utilisateur
- **MySQL Connector/J 8.0.33** - Driver JDBC MySQL
- **Maven** - Gestionnaire de dépendances et build
- **CSS** - Stylisation de l'interface

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.

## Contribution

Les contributions sont les bienvenues ! Veuillez :

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## Support

Pour obtenir de l'aide :
- Ouvrez une issue sur GitHub
- Consultez la documentation
- Vérifiez les logs de l'application pour les erreurs