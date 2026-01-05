# Variant C - Spring Boot MVC Project

## Description
Projet Spring Boot 3 avec JPA/Hibernate + PostgreSQL utilisant @RestController et Spring Data JPA.

## Structure du projet

```
variantC-springmvc/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tp/
│   │   │       ├── VariantCSpringMvcApplication.java
│   │   │       ├── controller/
│   │   │       │   ├── CategoryController.java
│   │   │       │   └── ItemController.java
│   │   │       ├── entity/
│   │   │       │   ├── Category.java
│   │   │       │   └── Item.java
│   │   │       └── repository/
│   │   │           ├── CategoryRepository.java
│   │   │           └── ItemRepository.java
│   │   └── resources/
│   │       └── application.properties
```

## Configuration

### Base de données
Modifiez les paramètres dans `src/main/resources/application.properties` :
- `spring.datasource.url` - URL de la base de données
- `spring.datasource.username` - Nom d'utilisateur
- `spring.datasource.password` - Mot de passe

### HikariCP
Configuration du pool de connexions dans `application.properties` :
- `spring.datasource.hikari.minimum-idle=10`
- `spring.datasource.hikari.maximum-pool-size=20`

## Endpoints REST

### Categories
- `GET /categories` - Liste paginée (query params: page, size, sort)
- `GET /categories/{id}` - Détail d'une catégorie
- `POST /categories` - Créer une catégorie
- `PUT /categories/{id}` - Mettre à jour une catégorie
- `DELETE /categories/{id}` - Supprimer une catégorie
- `GET /categories/{id}/items` - Liste des items d'une catégorie (paginated)

### Items
- `GET /items` - Liste paginée (query params: page, size, sort, categoryId)
- `GET /items/{id}` - Détail d'un item
- `POST /items` - Créer un item
- `PUT /items/{id}` - Mettre à jour un item
- `DELETE /items/{id}` - Supprimer un item

## Pagination

Spring Boot utilise `Pageable` pour la pagination :
- `page` - Numéro de page (0-indexed)
- `size` - Taille de la page (défaut: 20)
- `sort` - Tri (ex: `sort=name,asc`)

Exemple: `GET /categories?page=0&size=10&sort=name,asc`

## Compilation et exécution

```bash
mvn clean compile
mvn spring-boot:run
```

Ou pour créer un JAR exécutable :
```bash
mvn clean package
java -jar target/variantC-springmvc-1.0.0.jar
```

## Dépendances principales
- Spring Boot 3.2.0
- Spring Data JPA
- Hibernate (via Spring Data JPA)
- PostgreSQL Driver
- HikariCP (Connection Pool)
- Jackson (sérialisation JSON automatique)

## Fonctionnalités
- Pagination automatique avec `Pageable`
- Sérialisation JSON via Jackson (configuré automatiquement)
- Validation avec Spring Boot Validation
- Gestion des erreurs HTTP appropriées
- Relations JPA OneToMany/ManyToOne

