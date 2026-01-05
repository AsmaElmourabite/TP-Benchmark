import mysql.connector
from faker import Faker
from tqdm import tqdm
import random

# --- CONFIGURATION DE LA CONNEXION ---
db = mysql.connector.connect(
    host="localhost",
    user="root",          # utilisateur par défaut XAMPP
    password="",          # mot de passe vide par défaut
    database="db_ecommerce"
)

cursor = db.cursor()
fake = Faker('fr_FR')

# --- PARAMÈTRES ---
NUM_CATEGORIES = 2000
NUM_ITEMS = 100000  # 100k produits au total
ITEMS_PER_CATEGORY = NUM_ITEMS // NUM_CATEGORIES  # ≈ 50 produits par catégorie

# --- CRÉATION DES CATÉGORIES ---
print("Insertion des catégories...")
categories = []
for i in tqdm(range(NUM_CATEGORIES)):
    code = f"C{i+1:04d}"
    name = fake.word().capitalize() + " " + fake.word().capitalize()
    categories.append((code, name))

cursor.executemany("INSERT INTO category (code, name) VALUES (%s, %s)", categories)
db.commit()

# --- RÉCUPÉRATION DES IDS DES CATÉGORIES ---
cursor.execute("SELECT id FROM category")
category_ids = [row[0] for row in cursor.fetchall()]

# --- CRÉATION DES PRODUITS ---
print("\nInsertion des produits...")
items = []
for category_id in tqdm(category_ids):
    for index in range(ITEMS_PER_CATEGORY):
        # SKU unique garanti
        sku = f"SKU{category_id}_{index}"
        name = fake.word().capitalize() + " " + fake.word().capitalize()
        price = round(random.uniform(5.0, 500.0), 2)
        stock = random.randint(1, 500)
        items.append((sku, name, price, stock, category_id))

        # Insertion par lots pour éviter la surcharge mémoire
        if len(items) >= 1000:
            cursor.executemany(
                "INSERT INTO item (sku, name, price, stock, category_id) VALUES (%s, %s, %s, %s, %s)",
                items
            )
            db.commit()
            items = []

# Insertion du dernier lot restant
if items:
    cursor.executemany(
        "INSERT INTO item (sku, name, price, stock, category_id) VALUES (%s, %s, %s, %s, %s)",
        items
    )
    db.commit()

print("\n✅ Insertion terminée avec succès !")

cursor.close()
db.close()
