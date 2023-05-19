package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * This is the persistence layer which interacts with the db.
 * It has all the methods for saving/fetching books from the db.
 *
 * @author chetanbhatt
 */
public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

    boolean existsById(String id);

    Book saveAndFlush(Book book);
    void deleteById(String id);
    Optional<Book> findById(String id);
    Page<Book> findByAuthor(String author, final Pageable pageable);
    Page<Book> findByCategory(String category, final Pageable pageable);
    Optional<Book> findByNameAndAuthor(String name, String author);
    Page<Book> findByAuthorAndCategory(String author, String category, final Pageable pageable);

    Page<Book> findAll(final Pageable pageable);

    Page<Book> findAll(Specification specification, Pageable pageable);

}
