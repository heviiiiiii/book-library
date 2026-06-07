package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Book}.
 *
 * <p>Extending {@code JpaRepository} gives us {@code save}, {@code findById},
 * {@code findAll}, {@code deleteById}, paging, sorting — for free. No
 * implementation needed; Spring synthesises one at startup.
 *
 * <p>Derived queries: the method below is named so Spring derives the JPQL
 * automatically — no SQL written by hand.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /** Find all books whose title contains the given fragment (case-insensitive). */
    List<Book> findByTitleContainingIgnoreCase(String titleFragment);
}
