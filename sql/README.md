# Base de données PostgreSQL - Category et Item

Ce projet contient les scripts SQL pour créer une base de données PostgreSQL avec deux entités reliées.

## Structure de la base de données

### Table Category
- `id` : BIGSERIAL (clé primaire)
- `code` : VARCHAR(32) (unique)
- `name` : VARCHAR(128)
- `updated_at` : TIMESTAMP (défaut: CURRENT_TIMESTAMP)

### Table Item
- `id` : BIGSERIAL (clé primaire)
- `sku` : VARCHAR(64) (unique)
- `name` : VARCHAR(128)
- `price` : NUMERIC(10,2)
- `stock` : INTEGER
- `category_id` : BIGINT (clé étrangère vers Category)
- `updated_at` : TIMESTAMP (défaut: CURRENT_TIMESTAMP)

## Installation

1. Créer une base de données PostgreSQL :
```sql
CREATE DATABASE tp_db;
```

2. Exécuter le script de création des tables :
```bash
psql -d tp_db -f sql/create_tables.sql
```

3. Exécuter le script d'insertion des données :
```bash
psql -d tp_db -f sql/insert_data.sql
```

## Données générées

- **2000 catégories** : CAT0001 à CAT2000
- **100 000 items** : répartis uniformément (50 items par catégorie)
  - Format SKU : ITM-{category_id}-{item_number}
  - Prix aléatoires entre 10.00 et 1000.00
  - Stock aléatoire entre 0 et 999

## Notes

Les scripts utilisent `generate_series()` de PostgreSQL pour générer efficacement toutes les données, ce qui rend l'insertion rapide même avec un grand volume de données.

