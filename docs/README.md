# Inventory and Supplier Management System

## Team Members

- **Ofir Vaknin** – 323920769
- **Kirill Marmalevski** – 342662319
- **Tal Roth** - 325700607
- **Ofri Gal** - 322313396

---

## Introduction

Welcome to the unified **Inventory & Supplier Management System**!  
This Java-based system provides tools to manage:

- 📍 Inventory (branches, products, items, reports)
- 📦 Supplier Contracts & Orders

This project is a unified Inventory and Supplier Management System designed to support real-time operations across branches, product tracking, supplier agreements, and order handling — all through a user-friendly Command Line Interface (CLI).

---

## Prerequisites

- **Java Version**: The system was compiled with **Java 21**, so it must be run with **Java 21 or higher**.
- **Database**: Uses **SQLite** (local file `Data.db`, created automatically on first run).
- **No external frameworks**: Only standard Java libraries and JDBC.

---

## Running the System

1. Open a terminal in the `release` folder.
2. Run the following command:

```bash
java -jar adss2025_v01.jar
```

## 🚀 Running the System

### 🟡 Option 1: Run Inventory System

Menu:

```
1. Manage Branches
2. Manage Products
3. Manage Items
4. Reports
0. Exit
```

Each sub-menu includes:

**🔹 Branches Menu:**

- Add / Remove / Rename Branch
- Change Branch Address
- List All Branches

**🔹 Products Menu:**

- Add / Remove / Update Product
- View All Products
- Update Minimal Quantities

**🔹 Items Menu:**

- Add / Remove / Purchase / Update Item
- View All Items

**🔹 Reports Menu:**

- Deficiency Report
- Sales Report
- Defected Items Report
- Expired Items Report

---

### 🟢 Option 2: Run Supplier System

Menu:

```
1. Add Supplier
2. Add Agreement
3. Change Agreement
4. Remove Agreement
5. Get Supplied Items
6. Get Cataloged Items
7. Create Order
8. Get Order Details
9. Change Order
10. Cancel Order
11. Get Order History
12. Load Data
13. Exit
```

## 📚 Glossary

- **Branch** – A physical storage location.
- **Product** – A generic category of goods.
- **Item** – A specific purchasable instance of a product.
- **Report** – Summaries of inventory or order activity.
- **Supplier** – A company supplying products through contracts.
- **Agreement** – A contractual relationship including pricing and items.
- **Order** – A scheduled or created delivery request.

---

## 🛠️ Libraries & Tools Used

- Java 21
- SQLite (with JDBC driver)
- No external libraries

---

## ❗ Important Rules

- ❌ Cannot add items to a non-existing product or branch.
- ✅ Minimal quantity must be non-negative.
- ⏳ Items must have a **future expiration date**.
- ✅ All user inputs are validated.
- 🔐 Database enforces **foreign key constraints** and **ON DELETE CASCADE** to maintain integrity.
