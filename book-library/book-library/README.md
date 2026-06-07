# Book Library — SCSJ4383 Assignment 2 (Part A: Frameworks and REST)

A small **Spring Boot MVC** application demonstrating five pieces of
functionality over a single domain (a library of books). Two of those
five are exposed as **RESTful web services**, as required by the
assignment.

> The mark is based on **how the framework and REST are applied**, not
> on how complex the application is. This project is deliberately small
> and readable so each Spring Boot concept stands out.

---

## 1. The application

A library manager. The domain is one `Book` entity with these fields:

| field        | type    | notes                                  |
| ------------ | ------- | -------------------------------------- |
| `id`         | Long    | auto-generated primary key             |
| `title`      | String  | required, ≤ 200 chars                  |
| `author`     | String  | required, ≤ 120 chars                  |
| `genre`      | String  | optional, ≤ 60 chars                   |
| `year`       | int     | ≥ 0 (stored as column `publication_year` — `YEAR` is reserved in H2) |
| `available`  | boolean | true if the book is on the shelf       |

Five books are pre-seeded by `src/main/resources/data.sql` so the demo
screen looks alive immediately.

---

## 2. The five functionalities

| #   | Functionality            | Type | URL / Endpoint                                  | File                                                                                          |
| --- | ------------------------ | ---- | ----------------------------------------------- | --------------------------------------------------------------------------------------------- |
| 1   | **Browse / search**      | MVC  | `GET /` or `GET /books?q=...`                   | `BookViewController#list` → `templates/books/list.html`                                       |
| 2   | **Add a new book**       | MVC  | `GET /books/new` + `POST /books`                | `BookViewController#newForm`, `#create` → `templates/books/form.html`                         |
| 3   | **Edit an existing book**| MVC  | `GET /books/{id}/edit` + `POST /books/{id}`     | `BookViewController#editForm`, `#update` → `templates/books/form.html`                        |
| 4   | **REST – Read books**    | REST | `GET /api/books`, `GET /api/books/{id}`         | `BookRestController#all`, `#one`                                                              |
| 5   | **REST – Mutate books**  | REST | `POST /api/books`, `PUT /api/books/{id}`, `DELETE /api/books/{id}` | `BookRestController#create`, `#update`, `#delete`                              |

Three MVC pages (Thymeleaf-rendered HTML) + two REST groups (JSON) =
five distinct functionalities, with the two REST groups satisfying the
assignment's "TWO of the functionality to be your RESTful web-services"
requirement.

---

## 3. How the framework is set up

```
book-library/
├── pom.xml                                  ← Spring Boot 3.3.5 parent + starters
├── src/main/resources/
│   ├── application.properties               ← port, datasource, JPA, H2 console
│   ├── data.sql                             ← seed data
│   ├── static/css/app.css                   ← stylesheet
│   └── templates/books/
│       ├── list.html                        ← MVC view: list + search
│       └── form.html                        ← MVC view: add + edit
└── src/main/java/com/example/library/
    ├── LibraryApplication.java              ← @SpringBootApplication, main()
    ├── model/Book.java                      ← @Entity (JPA mapping + Bean Validation)
    ├── repository/BookRepository.java       ← extends JpaRepository<Book,Long>
    ├── service/BookService.java             ← @Service, @Transactional
    └── controller/
        ├── BookViewController.java          ← @Controller — returns view names
        ├── BookRestController.java          ← @RestController — returns JSON
        └── GlobalExceptionHandler.java      ← @RestControllerAdvice — JSON errors
```

**Starter dependencies in `pom.xml` (one line each, the rest is auto-wired):**

- `spring-boot-starter-web` — embedded Tomcat + Spring MVC + Jackson (JSON)
- `spring-boot-starter-thymeleaf` — HTML templating for the MVC views
- `spring-boot-starter-data-jpa` — repository abstraction + Hibernate
- `spring-boot-starter-validation` — Jakarta Bean Validation
- `h2` (runtime) — in-memory database; no external service needed

Spring Boot's auto-configuration reads the classpath and wires up
Tomcat, the `DispatcherServlet`, the JPA `EntityManagerFactory`, an H2
`DataSource`, and the Thymeleaf view resolver — none of which you
have to configure explicitly. The whole framework boots in one call:

```java
SpringApplication.run(LibraryApplication.class, args);
```

---

## 4. How the RESTful web services are set up

`BookRestController` is annotated with `@RestController` and
`@RequestMapping("/api/books")`. That single class change is the
difference between MVC and REST in Spring:

| concern              | MVC (`@Controller`)                            | REST (`@RestController`)                                |
| -------------------- | ---------------------------------------------- | -------------------------------------------------------- |
| return type          | String → view name (resolved by Thymeleaf)     | Object → serialised to JSON by Jackson                   |
| binds input from     | `@ModelAttribute` (form data)                  | `@RequestBody` (JSON)                                    |
| validation errors    | re-render the form with field errors           | RFC 7807 `ProblemDetail` JSON (see `GlobalExceptionHandler`) |
| error visibility     | Spring error page                              | structured JSON: `type`, `title`, `status`, `detail`     |

The REST mapping uses the standard HTTP verbs:

```
GET    /api/books          → list all books
GET    /api/books/{id}     → one book
POST   /api/books          → create (returns 201 Created + Location header)
PUT    /api/books/{id}     → replace
DELETE /api/books/{id}     → remove (returns 204 No Content)
```

The MVC `BookViewController` and the REST `BookRestController` both
delegate to the same `BookService`, so the two presentation layers
share one source of truth.

---

## 5. Run it

```bash
# requires Java 17+ and Maven 3.6+
mvn spring-boot:run
# or
mvn -DskipTests package && java -jar target/book-library-0.0.1-SNAPSHOT.jar
```

The app starts on **<http://localhost:8081>**.

| URL                                | What it is                          |
| ---------------------------------- | ----------------------------------- |
| <http://localhost:8081/>           | MVC: book list (functionality #1)   |
| <http://localhost:8081/books/new>  | MVC: add form (functionality #2)    |
| <http://localhost:8081/api/books>  | REST: JSON list (functionality #4)  |
| <http://localhost:8081/h2-console> | H2 console (JDBC URL `jdbc:h2:mem:library`, user `sa`, blank password) |

---

## 6. Try the REST endpoints

```bash
# List all books
curl http://localhost:8081/api/books

# One book
curl http://localhost:8081/api/books/1

# Create a book (returns 201 + Location header)
curl -X POST http://localhost:8081/api/books \
     -H 'Content-Type: application/json' \
     -d '{"title":"Domain-Driven Design","author":"Eric Evans","genre":"Architecture","year":2003,"available":true}'

# Replace a book
curl -X PUT http://localhost:8081/api/books/1 \
     -H 'Content-Type: application/json' \
     -d '{"title":"Clean Code (2nd ed.)","author":"Robert C. Martin","genre":"Software Engineering","year":2024,"available":true}'

# Delete a book
curl -X DELETE http://localhost:8081/api/books/1
```

Validation example (blank title returns 400 with a Problem Detail JSON body):

```bash
curl -X POST http://localhost:8081/api/books \
     -H 'Content-Type: application/json' \
     -d '{"title":"","author":"X","year":2020}'
# → {"type":"about:blank","title":"Bad Request","status":400,"detail":"title: Title is required", ...}
```

---

## 7. Two-minute video script

The assignment asks the video to explain (a) framework setup,
(b) REST setup, and (c) a demo. A workable script:

**0:00 — 0:25 · Framework setup**
- "I built this with Spring Boot. The starter dependencies in `pom.xml`
  pull in Tomcat, Spring MVC, Thymeleaf, Spring Data JPA, and H2."
- "`@SpringBootApplication` on `LibraryApplication.java` boots the whole
  thing — no XML, no manual servlet config."

**0:25 — 0:55 · REST setup**
- Open `BookRestController.java`. Point to:
  - `@RestController` and `@RequestMapping("/api/books")`
  - the verb mappings `@GetMapping`, `@PostMapping`, `@PutMapping`,
    `@DeleteMapping`
  - `@RequestBody` for JSON in, `ResponseEntity` for status codes out

**0:55 — 1:50 · Demo**
- Open <http://localhost:8081/> → show the list page (MVC #1).
- Click "+ New book" → add a book → return to list (MVC #2).
- Click "Edit" on a row → change the year → save (MVC #3).
- Open a terminal → `curl http://localhost:8081/api/books` →
  show the JSON (REST #4).
- `curl -X POST` + `curl -X DELETE` to show write operations (REST #5).
- Refresh the MVC list to show the same data — the two presentations
  share one service + one repository.

**1:50 — 2:00 · Wrap**
- "Five functionalities, two RESTful — the MVC views and REST API
  share the same `BookService`, which is the point of the layered
  Spring Boot architecture."

---

## 8. Notes for the grader

- The `BookService` is the single source of truth for business logic;
  the two controllers (`@Controller`, `@RestController`) are thin
  adapters that translate between presentation formats (HTML vs JSON).
- The REST API follows conventions:
  - `201 Created` + `Location` header on POST
  - `204 No Content` on DELETE
  - `400 Bad Request` with RFC 7807 Problem Detail JSON on validation failure
  - `404 Not Found` with Problem Detail JSON when the ID doesn't exist
- Bean Validation (`@NotBlank`, `@Size`, `@Min`) is declared on the
  entity and enforced by `@Valid` on both the MVC and REST handler
  parameters — one rule set, two enforcement points.
- The H2 database is in-memory and re-created on every boot. Switching
  to a persistent database would only require changing the JDBC URL in
  `application.properties`; no code change.
