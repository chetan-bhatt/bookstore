package com.example.bookstore.service.impl;

import com.example.bookstore.exception.BookStoreException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.example.bookstore.exception.ErrorCode.BOOK_ALREADY_EXIST;
import static com.example.bookstore.exception.ErrorCode.BOOK_NOT_FOUND;

/**
 *
 * Service class implementation that holds the service methods.
 *
 * @author chetanbhatt
 */
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Adds the book in DB.
     * @param book
     * @return Book
     * @throws BookStoreException
     */
    @Override
    public Book add(Book book) throws BookStoreException {
        validate(book);
        Optional<Book> bookInDb = bookRepository.findByNameAndAuthor(book.getName(), book.getAuthor());
        if(bookInDb.isPresent()){
            throw new BookStoreException(BOOK_ALREADY_EXIST, Map.of("name", book.getName(), "author", book.getAuthor()),
                    String.format("Book already exists with name %s and author %s", book.getName(), book.getAuthor()));
        }
        log.info("Saving book in db, id = {}", book.getId());
        return bookRepository.save(book);
    }

    /**
     * Deletes the book specified by id.
     *
     * @param id
     * @throws BookStoreException
     */
    @Override
    public void delete(String id) throws BookStoreException {
        if(id == null){
            throw new IllegalArgumentException("Invalid argument, id is null");
        }
        if(!bookRepository.existsById(id)){
            throw new BookStoreException(BOOK_NOT_FOUND, Map.of("id", id), String.format("Book with id %s does not exist", id));
        }
        log.info("Deleting book, id = {}", id);
        bookRepository.deleteById(id);
    }

    /**
     * Gets the book specified by id.
     *
     * @param id
     * @return Book
     * @throws BookStoreException
     */
    @Override
    public Book get(String id) throws BookStoreException {
        if(id == null || id.isEmpty()){
            throw new IllegalArgumentException("Invalid argument, id is null");
        }
        log.info("Fetching book, id = {}", id);
        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty()){
            throw new BookStoreException(BOOK_NOT_FOUND, Map.of("id", id), String.format("Book with id %s does not exist", id));
        }
        return book.get();
    }

    /**
     * Gets the Page of books as specified by the parameters
     *
     * @param author
     * @param category
     * @param pageable
     * @return Page of books
     */
    @Override
    public Page<Book> getBooks(String author, String category, Pageable pageable) {
        if(pageable == null){
            throw new IllegalArgumentException("Pageable must not be null");
        }
        log.info("Page = {}, size = {}", pageable.getPageNumber(), pageable.getPageSize());
        if(author != null && category != null){
            log.info("Fetching books by author {} and category {}", author, category);
            return bookRepository.findByAuthorAndCategory(author, category, pageable);
        }
        if(author != null){
            log.info("Fetching books by author {}", author);
            return bookRepository.findByAuthor(author, pageable);
        }
        if(category != null){
            log.info("Fetching books by category {}", category);
            return bookRepository.findByCategory(category, pageable);
        }
        log.info("Fetching all books");
        return bookRepository.findAll(pageable);
    }

    /**
     * Searches for books as specified by search criteria defined in Specification
     *
     * @param specification
     * @param pageable
     * @return Page of books
     */
    @Override
    public Page<Book> getBooks(Specification<Book> specification, Pageable pageable) {
        if(pageable == null){
            throw new IllegalArgumentException("Pageable must not be null");
        }
        log.info("Fetching books by specification, page = {}, size = {}", pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findAll(specification, pageable);
    }

    private void validate(Book book){
        if(book == null ||
                book.getId() == null || book.getId().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().isEmpty() ||
                book.getName() == null || book.getName().isEmpty() ||
                book.getCategory() == null || book.getCategory().isEmpty()){
            throw new IllegalArgumentException("Book is null or one/more of its properties are null or empty");
        }
    }
}
