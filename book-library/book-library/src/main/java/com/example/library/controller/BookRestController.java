package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * RESTful web service for {@link Book}.
 *
 * <p>This controller implements <b>the two REST functionalities</b>
 * required by the assignment:
 * <ol>
 *   <li><b>REST #1 — Read:</b>
 *       <ul>
 *         <li>{@code GET /api/books}          — list all books as JSON</li>
 *         <li>{@code GET /api/books/{id}}     — one book as JSON</li>
 *       </ul>
 *   </li>
 *   <li><b>REST #2 — Write:</b>
 *       <ul>
 *         <li>{@code POST   /api/books}       — create a book from a JSON body</li>
 *         <li>{@code PUT    /api/books/{id}}  — replace a book</li>
 *         <li>{@code DELETE /api/books/{id}}  — remove a book</li>
 *       </ul>
 *   </li>
 * </ol>
 *
 * <p>{@code @RestController} = {@code @Controller} + {@code @ResponseBody},
 * so every return value is serialised to JSON by Jackson (auto-configured
 * by Spring Boot) instead of being resolved as a view name.
 */
@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    // -------- REST Functionality #4 — Read endpoints ----------------------

    @GetMapping
    public List<Book> all() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book one(@PathVariable Long id) {
        return bookService.findById(id);
    }

    // -------- REST Functionality #5 — Write endpoints ---------------------

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        Book saved = bookService.create(book);
        // RESTful convention: 201 Created + a Location header pointing to the new resource.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getId())
            .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.update(id, book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
