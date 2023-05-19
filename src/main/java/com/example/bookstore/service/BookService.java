package com.example.bookstore.service;

import com.example.bookstore.exception.BookStoreException;
import com.example.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface BookService {

    Book add(Book book) throws BookStoreException;

    void delete(String id) throws BookStoreException;

    Book get(String id) throws BookStoreException;

    Page<Book> getBooks(String author, String category, final Pageable pageable);

    Page<Book> getBooks(Specification<Book> specification, final Pageable pageable);

}
