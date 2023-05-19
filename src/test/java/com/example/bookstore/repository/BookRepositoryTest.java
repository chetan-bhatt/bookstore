package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import com.example.bookstore.rest.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    private Pageable pageable;

    @Before
    public void setup(){
        pageable = PageRequest.of(0, 2);

        book = new Book("Book1", "Author", Category.TECHNICAL);
        Book savedBook = bookRepository.saveAndFlush(book);
        assertNotNull(savedBook);
        assertEquals(book.getId(), savedBook.getId());
    }

    @Test
    public void testSave(){
        Book book = new Book("Book2", "Author", Category.TECHNICAL);
        Book savedBook = bookRepository.saveAndFlush(book);
        assertNotNull(savedBook);
        assertEquals(book.getId(), savedBook.getId());
    }

    @Test
    public void testDeleteById(){
        Book book = new Book("Book2", "Author", Category.TECHNICAL);
        Book savedBook = bookRepository.saveAndFlush(book);
        assertNotNull(savedBook);
        assertEquals(book.getId(), savedBook.getId());
        bookRepository.deleteById(book.getId());
        assertFalse(bookRepository.existsById(book.getId()));
    }

    @Test
    public void testExists(){
        boolean exists = bookRepository.existsById(book.getId());
        assertTrue(exists);
    }

    @Test
    public void testFindById(){
        Optional<Book> returnedBook = bookRepository.findById(book.getId());
        assertTrue(returnedBook.isPresent());
    }

    @Test
    public void testFindByAuthor(){
        Page<Book> bookPage = bookRepository.findByAuthor(book.getAuthor(), pageable);
        assertNotNull(bookPage);
        assertEquals(1, bookPage.getTotalElements());
    }

    @Test
    public void testFindByCategory(){
        Page<Book> bookPage = bookRepository.findByCategory(book.getCategory(), pageable);
        assertNotNull(bookPage);
        assertEquals(1, bookPage.getTotalElements());
    }

    @Test
    public void testFindByNameAndAuthor(){
        Optional<Book> returnedBook = bookRepository.findByNameAndAuthor(book.getName(), book.getAuthor());
        assertTrue(returnedBook.isPresent());
    }

    @Test
    public void testFindAll(){
        Book book = new Book("Book2", "Author", Category.TECHNICAL);
        Book savedBook = bookRepository.saveAndFlush(book);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        assertNotNull(bookPage);
        assertEquals(2, bookPage.getTotalElements());
    }

    @After
    public void tearDown(){
        if(bookRepository.existsById(book.getId())){
            bookRepository.deleteById(book.getId());
        }
    }
}
