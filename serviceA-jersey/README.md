# Variant A - Jersey JAX-RS Project

## Description
Projet Java 17 avec Maven utilisant Jersey (JAX-RS) + JPA/Hibernate + PostgreSQL.

## Structure du projet

```
variantA-jersey/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tp/
│   │   │       ├── config/
│   │   │       │   └── JerseyApplication.java
│   │   │       ├── dao/
│   │   │       │   ├── CategoryDAO.java
│   │   │       │   └── ItemDAO.java
│   │   │       ├── entity/
│   │   │       │   ├── Category.java
│   │   │       │   └── Item.java
│   │   │       ├── resource/
│   │   │       │   ├── CategoryResource.java
│   │   │       │   └── ItemResource.java
│   │   │       └── util/
│   │   │           └── EntityManagerUtil.java
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── persistence.xml
│   │       └── application.properties
```

## Configuration

### Base de données
Modifiez les paramètres dans `src/main/resources/application.properties` et `src/main/resources/META-INF/persistence.xml` :
- URL de la base de données
- Nom d'utilisateur
- Mot de passe

### HikariCP
Configuration du pool de connexions dans `persistence.xml` :
- minimumIdle: 10
- maximumPoolSize: 20

## Endpoints REST

### Categories
- `GET /api/categories` - Liste paginée (query params: page, size)
- `GET /api/categories/{id}` - Détail d'une catégorie
- `POST /api/categories` - Créer une catégorie
- `PUT /api/categories/{id}` - Mettre à jour une catégorie
- `DELETE /api/categories/{id}` - Supprimer une catégorie
- `GET /api/categories/{id}/items` - Liste des items d'une catégorie (paginated)

### Items
- `GET /api/items` - Liste paginée (query params: page, size, categoryId)
- `GET /api/items/{id}` - Détail d'un item
- `POST /api/items` - Créer un item
- `PUT /api/items/{id}` - Mettre à jour un item
- `DELETE /api/items/{id}` - Supprimer un item

## Compilation et exécution

```bash
mvn clean compile
mvn package
```

## Dépendances principales
- Jersey 3.1.3 (JAX-RS)
- Hibernate 6.2.13.Final (JPA)
- PostgreSQL Driver 42.6.0
- HikariCP 5.0.1 (Connection Pool)
- Jackson 2.15.2 (JSON)

