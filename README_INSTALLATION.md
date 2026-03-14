# 🎓 Virtual Campus Hub — Guide d'Installation

## ✅ Prérequis
- Java 17+ (ou Java 25 pour les modules)
- Maven 3.8+
- MySQL 8.0+
- JavaFX 17+ (ou 25 si utilisé avec Java 25)

---

## 🗄️ Étape 1 — Configurer MySQL

### Option A : Via MySQL Workbench / phpMyAdmin
1. Ouvrir votre client MySQL
2. Importer et exécuter le fichier `database_setup.sql`

### Option B : Via le terminal
```bash
mysql -u root -p < database_setup.sql
```

### ⚠️ Modifier les identifiants si nécessaire
Dans `DatabaseConnection.java`, modifier les constantes :
```java
private static final String USER     = "root";     // ← votre user MySQL
private static final String PASSWORD = "";          // ← votre mot de passe MySQL
```

---

## 🔐 Compte de test
- **Email** : `admin@campus.fr`
- **Mot de passe** : `admin123`

> **Mode hors-ligne** : Si MySQL n'est pas disponible, l'application fonctionne en mode démo avec les mêmes identifiants.

---

## 📁 Étape 2 — Copier les fichiers

Remplacez les fichiers dans votre projet :
```
src/main/java/com/virtualcampushub/
├── Main.java                ← Remplacer
├── ViewManager.java         ← Remplacer
├── DatabaseConnection.java  ← Remplacer
├── UserDAO.java             ← Remplacer
├── Sidebar.java             ← Remplacer
├── TopBar.java              ← Remplacer
├── LoginView.java           ← Remplacer
├── RegisterView.java        ← Remplacer
├── DashboardView.java       ← Remplacer
├── CoursesView.java         ← Remplacer
├── EventsView.java          ← Remplacer
├── ProfileView.java         ← Remplacer

src/main/resources/
└── styles.css               ← Remplacer
```

---

## 🔧 Étape 3 — pom.xml recommandé

Assurez-vous d'avoir ces dépendances dans votre `pom.xml` :
```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.8</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.8</version>
    </dependency>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

---

## 🚀 Étape 4 — Lancer l'application

```bash
mvn clean javafx:run
```
ou via votre IDE (IntelliJ / Eclipse / VS Code).

---

## 🐛 Problèmes fréquents

| Erreur | Solution |
|--------|----------|
| `Communications link failure` | MySQL n'est pas démarré → Lancer MySQL |
| `Access denied for user 'root'` | Mauvais mot de passe dans `DatabaseConnection.java` |
| `Unknown database 'virtual_campus_hub'` | Exécuter `database_setup.sql` |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Ajouter mysql-connector-j dans pom.xml |
| Écran blanc / freeze au démarrage | Vérifier que JavaFX est bien configuré |

---

## 🎨 Nouveautés v2.0

- ✅ **Thème sombre moderne** avec palette cohérente
- ✅ **Mode hors-ligne** : fonctionne sans MySQL
- ✅ **Cartes animées** avec hover et transitions
- ✅ **Graphique de progression** dessiné sur Canvas
- ✅ **Sidebar** avec états actifs animés
- ✅ **Calendrier** interactif avec indicateurs d'événements
- ✅ **Cours en cartes** avec barres de progression animées
- ✅ **Profil** enrichi avec statistiques
- ✅ **Gestion d'erreurs** robuste sur toute la DB
- ✅ **Connexion automatique** et reconnexion MySQL

---

*Virtual Campus Hub v2.0 — 2026*
