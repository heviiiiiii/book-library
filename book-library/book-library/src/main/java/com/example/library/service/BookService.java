package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service layer — orchestrates business logic between controllers and the
 * repository. Keeping controllers thin and pushing logic here makes both
 * the MVC and REST entry points reuse the same code path.
 */
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Book> search(String titleFragment) {
        if (titleFragment == null || titleFragment.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCase(titleFragment);
    }

    public Book create(Book book) {
        book.setId(null); // never trust an incoming id on create
        return bookRepository.save(book);
    }

    public Book update(Long id, Book incoming) {
        Book existing = findById(id);
        existing.setTitle(incoming.getTitle());
        existing.setAuthor(incoming.getAuthor());
        existing.setGenre(incoming.getGenre());
        existing.setYear(incoming.getYear());
        existing.setAvailable(incoming.isAvailable());
        return existing; // managed entity — flushed on transaction commit
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }
}
