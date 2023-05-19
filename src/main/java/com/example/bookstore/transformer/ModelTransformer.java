package com.example.bookstore.transformer;

import com.example.bookstore.model.Book;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class contains functions for converting models.
 * Rest models are converted into domain models and vice versa.
 *
 * @author chetanbhatt
 */
public class ModelTransformer {

    public static Function<Book, com.example.bookstore.rest.Book> fromModelToRest = (Book b)-> {
        com.example.bookstore.rest.Book book = new com.example.bookstore.rest.Book();
        book.setId(b.getId());
        book.setName(b.getName());
        book.setCategory(com.example.bookstore.rest.Category.fromValue(b.getCategory()));
        book.setAuthor(b.getAuthor());
        return book;
    };

    public static Function<List<Book>, List<com.example.bookstore.rest.Book>> fromModelListToRestList =
            (List<Book> list)-> list.stream().map(book -> fromModelToRest.apply(book)).collect(Collectors.toList());

    public static Function<com.example.bookstore.rest.Book, Book> fromRestToModel =
            (com.example.bookstore.rest.Book b)-> new Book(b.getName(), b.getAuthor(), b.getCategory());
}



