package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Server-rendered MVC controller — returns Thymeleaf view names (HTML).
 *
 * <p>This controller implements <b>three of the assignment's five
 * functionalities</b>:
 * <ol>
 *   <li><b>Functionality #1 — Browse:</b> GET {@code /} or {@code /books}
 *       lists all books and supports a title search.</li>
 *   <li><b>Functionality #2 — Add:</b> GET {@code /books/new} shows the
 *       form; POST {@code /books} saves it after validation.</li>
 *   <li><b>Functionality #3 — Edit:</b> GET {@code /books/{id}/edit}
 *       pre-fills the form; POST {@code /books/{id}} saves the changes.</li>
 * </ol>
 *
 * <p>{@code @Controller} (vs. {@code @RestController}) signals to Spring
 * MVC that returned Strings are view names to be resolved by Thymeleaf,
 * not response bodies.
 */
@Controller
public class BookViewController {

    private final BookService bookService;

    public BookViewController(BookService bookService) {
        this.bookService = bookService;
    }

    // -------- Functionality #1 — List + search ----------------------------

    @GetMapping({ "/", "/books" })
    public String list(@RequestParam(name = "q", required = false) String query, Model model) {
        model.addAttribute("books", bookService.search(query));
        model.addAttribute("query", query == null ? "" : query);
        return "books/list";
    }

    // -------- Functionality #2 — Add a new book ---------------------------

    @GetMapping("/books/new")
    public String newForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("mode", "create");
        return "books/form";
    }

    @PostMapping("/books")
    public String create(@Valid @ModelAttribute("book") Book book, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "create");
            return "books/form";
        }
        bookService.create(book);
        return "redirect:/books";
    }

    // -------- Functionality #3 — Edit an existing book --------------------

    @GetMapping("/books/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("mode", "edit");
        return "books/form";
    }

    @PostMapping("/books/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("book") Book book,
                         BindingResult binding,
                         Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "books/form";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    // Convenience: delete via a POST from the list page (HTML forms can't DELETE).
    @PostMapping("/books/{id}/delete")
    public String deleteFromUi(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
