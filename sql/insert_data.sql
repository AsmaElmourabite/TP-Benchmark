-- Insertion de 2000 catégories (CAT0001 à CAT2000)
-- Utilisation de generate_series pour générer toutes les catégories
INSERT INTO Category (code, name)
SELECT 
    'CAT' || LPAD(serie::text, 4, '0') as code,
    'Category ' || LPAD(serie::text, 4, '0') as name
FROM generate_series(1, 2000) as serie;

-- Insertion de 100 000 items (50 items par catégorie)
-- Chaque catégorie aura 50 items avec SKU unique
-- Format SKU: ITM-{category_id}-{item_number}
INSERT INTO Item (sku, name, price, stock, category_id)
SELECT 
    'ITM-' || cat_id || '-' || LPAD(item_num::text, 2, '0') as sku,
    'Item ' || cat_id || '-' || LPAD(item_num::text, 2, '0') as name,
    ROUND((RANDOM() * 990 + 10)::numeric, 2) as price,  -- Prix entre 10.00 et 1000.00
    FLOOR(RANDOM() * 1000)::integer as stock,  -- Stock entre 0 et 999
    cat_id as category_id
FROM 
    generate_series(1, 2000) as cat_id,
    generate_series(1, 50) as item_num;
