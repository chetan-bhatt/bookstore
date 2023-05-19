package com.example.bookstore.service.impl;

import com.example.bookstore.exception.BookStoreException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.rest.Category;
import com.example.bookstore.service.BookService;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private BookRepository bookRepository = Mockito.mock(BookRepository.class);

    private BookService bookService = new BookServiceImpl(bookRepository);

    @Test
    public void testAddBook(){
        String name = "Learn DSA";
        String author = "Chetan Bhatt";
        Book book = new Book(name, author, Category.TECHNICAL);
        when(bookRepository.findByNameAndAuthor(name, author)).thenReturn(Optional.ofNullable(null));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book savedBook = bookService.add(book);
        assertNotNull(savedBook);
        assertEquals(book.getId(), savedBook.getId());
    }

    @Test(expected = BookStoreException.class)
    public void testAddBookAlreadyExist(){
        String name = "Learn DSA";
        String author = "Chetan Bhatt";
        Book book = new Book(name, author, Category.TECHNICAL);
        when(bookRepository.findByNameAndAuthor(name, author)).thenReturn(Optional.ofNullable(book));
        bookService.add(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullBook(){
        bookService.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBookWithNameNull(){
        String author = "Chetan Bhatt";
        Book book = new Book(null, author, Category.TECHNICAL);
        bookService.add(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBookWithAuthorNull(){
        String author = "Chetan Bhatt";
        Book book = new Book("Book", null, Category.TECHNICAL);
        bookService.add(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteBookIdIsNull(){
        bookService.delete(null);
    }

    @Test
    public void testDeleteBookId(){
        String id = "id";
        when(bookRepository.existsById(id)).thenReturn(true);
        bookService.delete(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test(expected = BookStoreException.class)
    public void testDeleteBookIdAlreadyExist(){
        String id = "id";
        when(bookRepository.existsById(id)).thenReturn(false);
        bookService.delete(id);
    }

    @Test
    public void testGetBookById(){
        Book book = new Book("Name", "Author", Category.TECHNICAL);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        Book returnedBook = bookService.get(book.getId());
        assertNotNull(returnedBook);
        assertEquals(book.getId(), returnedBook.getId());
    }

    @Test(expected = BookStoreException.class)
    public void testGetBookByIdNotFound(){
        when(bookRepository.findById("id")).thenReturn(Optional.ofNullable(null));
        bookService.get("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBookByNullId(){
        bookService.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBooksWithNullPageable(){
        bookService.getBooks("Author", Category.TECHNICAL.getValue(), null);
    }

    @Test
    public void testGetBooksWithAuthorAndCategory(){
        List<Book> books = List.of(
                                    new Book("Book1", "Author1", Category.TECHNICAL),
                                    new Book("Book2", "Author1", Category.TECHNICAL)
                                    );
        Page<Book> page = new PageImpl<>(books);
        PageRequest pageable = PageRequest.of(0, 3);
        when(bookRepository.findByAuthorAndCategory("Author1", Category.TECHNICAL.getValue(), pageable)).thenReturn(page);
        Page<Book> returnedPage = bookService.getBooks("Author1", Category.TECHNICAL.getValue(), pageable);
        assertNotNull(returnedPage);
        assertEquals(2, returnedPage.getTotalElements());
    }
}
