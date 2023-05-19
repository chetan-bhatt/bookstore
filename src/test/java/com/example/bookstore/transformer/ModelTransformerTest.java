package com.example.bookstore.transformer;

import com.example.bookstore.rest.Book;
import com.example.bookstore.rest.Category;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModelTransformerTest {

    @Test
    public void testFromRestModelToDomain(){
        com.example.bookstore.rest.Book book = new Book();
        book.setName("book1");
        book.setCategory(Category.TECHNICAL);
        book.setAuthor("Author");

        com.example.bookstore.model.Book domainBook = ModelTransformer.fromRestToModel.apply(book);
        assertNotNull(domainBook);
        assertEquals(book.getAuthor(), domainBook.getAuthor());
        assertEquals(book.getName(), domainBook.getName());
        assertEquals(book.getCategory().getValue(), domainBook.getCategory());
    }

    @Test
    public void testFromDomainModelToRest(){
        com.example.bookstore.model.Book domainBook = new com.example.bookstore.model.Book("Book1", "Author", Category.HUMOUR);
        Book book = ModelTransformer.fromModelToRest.apply(domainBook);
        assertNotNull(domainBook);
        assertEquals(domainBook.getId(), book.getId());
        assertEquals(domainBook.getAuthor(), book.getAuthor());
        assertEquals(domainBook.getName(), book.getName());
        assertEquals(domainBook.getCategory(), book.getCategory().getValue());
    }

    @Test
    public void testfromModelListToRestList(){
        List<com.example.bookstore.model.Book> domainBookList = List.of(new com.example.bookstore.model.Book("Book1", "Author", Category.HUMOUR));
        List<Book> bookList = ModelTransformer.fromModelListToRestList.apply(domainBookList);
        assertNotNull(bookList);
        assertEquals(domainBookList.size(), bookList.size());
    }
}
