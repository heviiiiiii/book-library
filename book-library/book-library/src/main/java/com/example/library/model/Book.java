package com.example.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Domain entity for a book in the library.
 *
 * <p>{@code @Entity} tells Hibernate to map this class to a database table
 * (defaulting to {@code BOOK}). Each field becomes a column.
 *
 * <p>Validation annotations ({@code @NotBlank}, {@code @Size}, {@code @Min})
 * are enforced when the controller binds an incoming form/JSON body and the
 * handler argument is marked {@code @Valid} — see the controllers.
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 120)
    private String author;

    @Size(max = 60)
    private String genre;

    @Min(value = 0, message = "Year must be a positive number")
    @Column(name = "publication_year")
    private int year;

    private boolean available = true;

    public Book() {
        // JPA requires a no-args constructor.
    }

    public Book(String title, String author, String genre, int year, boolean available) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.available = available;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
