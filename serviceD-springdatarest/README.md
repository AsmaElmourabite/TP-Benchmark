# Variant D - Spring Data REST Project

## Description
Projet Spring Boot 3 avec Spring Data REST + JPA/Hibernate + PostgreSQL.
Spring Data REST expose automatiquement les repositories REST sans avoir besoin de créer des controllers manuellement.

## Structure du projet

```
variantD-springdatarest/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tp/
│   │   │       ├── VariantDSpringDataRestApplication.java
│   │   │       ├── config/
│   │   │       │   ├── SpringDataRestConfig.java
│   │   │       │   └── CacheConfig.java
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

### Spring Data REST
- Base path : `/api`
- HAL activé pour la sortie JSON
- Cache HTTP désactivé

## Endpoints REST automatiques

Spring Data REST expose automatiquement les endpoints suivants :

### Categories
- `GET /api/categories` - Liste paginée (avec HAL)
- `GET /api/categories/{id}` - Détail d'une catégorie
- `POST /api/categories` - Créer une catégorie
- `PUT /api/categories/{id}` - Mettre à jour une catégorie
- `PATCH /api/categories/{id}` - Mettre à jour partiellement
- `DELETE /api/categories/{id}` - Supprimer une catégorie

### Items
- `GET /api/items` - Liste paginée (avec HAL)
- `GET /api/items/{id}` - Détail d'un item
- `POST /api/items` - Créer un item
- `PUT /api/items/{id}` - Mettre à jour un item
- `PATCH /api/items/{id}` - Mettre à jour partiellement
- `DELETE /api/items/{id}` - Supprimer un item
- `GET /api/items/search/findByCategoryId?categoryId={id}` - Recherche par catégorie

### HAL Explorer
- `GET /api` - Point d'entrée HAL avec tous les endpoints disponibles
- `GET /api/profile` - Profil des ressources disponibles

## Format HAL (Hypertext Application Language)

Les réponses JSON utilisent le format HAL qui inclut :
- `_links` : Liens vers les ressources associées
- `_embedded` : Ressources embarquées
- `page` : Informations de pagination

Exemple de réponse HAL :
```json
{
  "_embedded": {
    "categories": [
      {
        "id": 1,
        "code": "CAT0001",
        "name": "Category 0001",
        "_links": {
          "self": {
            "href": "http://localhost:8080/api/categories/1"
          },
          "category": {
            "href": "http://localhost:8080/api/categories/1"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/categories"
    },
    "profile": {
      "href": "http://localhost:8080/api/profile/categories"
    }
  },
  "page": {
    "size": 20,
    "totalElements": 2000,
    "totalPages": 100,
    "number": 0
  }
}
```

## Pagination

Spring Data REST utilise la pagination par défaut :
- `page` - Numéro de page (0-indexed, défaut: 0)
- `size` - Taille de la page (défaut: 20)
- `sort` - Tri (ex: `sort=name,asc`)

Exemple: `GET /api/categories?page=0&size=10&sort=name,asc`

## Compilation et exécution

```bash
mvn clean compile
mvn spring-boot:run
```

Ou pour créer un JAR exécutable :
```bash
mvn clean package
java -jar target/variantD-springdatarest-1.0.0.jar
```

## Dépendances principales
- Spring Boot 3.2.0
- Spring Data REST
- Spring Data JPA
- Hibernate (via Spring Data JPA)
- PostgreSQL Driver
- HikariCP (Connection Pool)
- HAL Explorer (pour explorer les endpoints)

## Fonctionnalités
- Endpoints REST automatiques basés sur les repositories
- Format HAL pour les réponses JSON
- Pagination automatique
- Recherche personnalisée avec `findByCategoryId`
- Cache HTTP désactivé
- Relations JPA exposées via HAL links

