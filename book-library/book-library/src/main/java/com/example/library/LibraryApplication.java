package com.example.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Book Library application.
 *
 * <p>{@code @SpringBootApplication} is a meta-annotation that combines:
 * <ul>
 *   <li>{@code @Configuration} — this class can define Spring beans.</li>
 *   <li>{@code @EnableAutoConfiguration} — Spring Boot wires up Tomcat,
 *       Spring MVC, Thymeleaf, Spring Data JPA, the H2 DataSource, etc.
 *       based on the dependencies on the classpath.</li>
 *   <li>{@code @ComponentScan} — picks up @Controller / @Service /
 *       @Repository in this package and its sub-packages.</li>
 * </ul>
 */
@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
