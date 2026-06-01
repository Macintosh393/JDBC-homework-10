# Flyway Migrations & Client CRUD Service (Homework 10)

An advanced Java database application expanding on the raw JDBC core. It replaces manual database provisioning with automated database migrations using **Flyway**, implements standard CRUD operations for `client` entities with parameter validation, and manages transactions safely without ORM engines.

---

## 🛠️ Technologies Used
- **Java SE 11+**: Primary programming language and platform.
- **Gradle**: Build automation and dependency management tool.
- **H2 Database Engine (2.2.224)**: Embedded, file-based SQL database for lightweight persistent storage.
- **Flyway Migrations (9.22.3)**: Version control database engine that tracks schema changes and automates table migration seamlessly.
- **JDBC (Java Database Connectivity)**: Low-level API used with parameter validation, robust exception wrappers, and generated auto-increment keys mapping.

---

## 📂 Project Structure

```
JDBC-homework-10/
│
├── src/main/resources/db/migration/ # Flyway Migration Scripts
│   ├── V1__init_db.sql              # Automated migration: Schema provisioning
│   └── V2__populate_db.sql          # Automated migration: Seeds initial mock records
│
├── sql/                             # Analytical Queries (Retained for query support)
│   ├── find_longest_project.sql
│   ├── find_max_projects_client.sql
│   ├── find_max_salary_worker.sql
│   ├── find_youngest_eldest_workers.sql
│   └── print_project_prices.sql
│
├── src/main/java/org/example/       # Java Source Directory
│   ├── Database.java                # Singleton that instantiates H2 and automatically runs Flyway
│   ├── Client.java                  # Model class representing a Client entity
│   ├── ClientService.java           # CRUD API service with validation logic and test assertions
│   ├── DatabaseQueryService.java    # Analytical query execution engine
│   └── [DTOs]                       # Data Transfer Objects for query mapping
│
├── build.gradle                     # Gradle configuration with Flyway dependencies and task runner
└── .gitignore                       # Keeps H2 cache files (`testdb.*`) out of repository
```

---

## 🚀 How to Use / Execution Instructions

### 1. Execute Client CRUD Service Tests & Automated Migrations
To trigger the automated migrations (V1 & V2) via Flyway, execute CRUD operations, and verify that name validations raise appropriate exceptions:
```bash
./gradlew runClientService
```

*What this command does:*
1. Instantiates the `Database` singleton which starts the H2 database.
2. Configures Flyway and executes `V1__init_db.sql` and `V2__populate_db.sql` automatically.
3. Inserts, retrieves, updates, and deletes test clients via JDBC.
4. Asserts and validates input rules (e.g. throwing `IllegalArgumentException` for names with length < 2 or > 1000 characters).

### 2. Execute Analytical Queries
To execute the complex DTO queries defined in the `sql/` directory:
```bash
./gradlew runQuery
```

---

## ⚙️ Configuration
- The database is stored locally in the root directory under `./testdb` (file `testdb.mv.db`).
- Default credentials: User `sa`, Password ``.
