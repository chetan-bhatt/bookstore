package com.example.bookstore.controller;

import com.example.bookstore.advice.CustomExceptionHandler;
import com.example.bookstore.exception.BookStoreException;
import com.example.bookstore.model.Book;
import com.example.bookstore.rest.AddBookRequest;
import com.example.bookstore.rest.Category;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static com.example.bookstore.exception.ErrorCode.BOOK_ALREADY_EXIST;
import static com.example.bookstore.exception.ErrorCode.BOOK_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext applicationContext;

    private BookService bookService = Mockito.mock(BookService.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        final BookController bookController = new BookController(bookService);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new CustomExceptionHandler()).build();
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book("Book", "Author", Category.TECHNICAL);
        when(bookService.get(book.getId())).thenReturn(book);
        mockMvc.perform(get("/api/v1/books/" + book.getId()))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        Book book = new Book("Book", "Author", Category.TECHNICAL);
        when(bookService.get(book.getId())).thenThrow(new BookStoreException(BOOK_NOT_FOUND, Map.of("id", book.getId()), String.format("Book with id %s does not exist", book.getId())));
        mockMvc.perform(get("/api/v1/books/" + book.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddBook() throws Exception {
        AddBookRequest bookRequest = new AddBookRequest();
        bookRequest.setName("Book1");
        bookRequest.setAuthor("Author1");
        bookRequest.setCategory(Category.TECHNICAL);

        when(bookService.add(any())).thenReturn(new Book("Book1", "Author1", Category.TECHNICAL));
        mockMvc.perform(post("/api/v1/books/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddBookAlreadyExist() throws Exception {
        AddBookRequest bookRequest = new AddBookRequest();
        bookRequest.setName("Book1");
        bookRequest.setAuthor("Author1");
        bookRequest.setCategory(Category.TECHNICAL);
        when(bookService.add(any())).thenThrow(new BookStoreException(BOOK_ALREADY_EXIST, Map.of("name", bookRequest.getName(), "author", bookRequest.getAuthor()),
                String.format("Book already exists with name %s and author %s", bookRequest.getName(), bookRequest.getAuthor())));

        mockMvc.perform(post("/api/v1/books/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testDeleteBookNotFound() throws Exception {
        Book book = new Book("Book", "Author", Category.TECHNICAL);
        Mockito.doThrow(new BookStoreException(BOOK_NOT_FOUND, Map.of("id", book.getId()), String.format("Book with id %s does not exist", book.getId())))
                        .when(bookService).delete(book.getId());
        mockMvc.perform(delete("/api/v1/books/" + book.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBook() throws Exception {
        Book book = new Book("Book", "Author", Category.TECHNICAL);
        Mockito.doNothing().when(bookService).delete(book.getId());
        mockMvc.perform(delete("/api/v1/books/" + book.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBooks() throws Exception {
        Book book = new Book("Book1", "Author", Category.TECHNICAL);
        Book book2 = new Book("Book2", "Author", Category.TECHNICAL);
        PageRequest pageable = PageRequest.of(0, 50);
        when(bookService.getBooks(null, null, pageable)).thenReturn(new PageImpl<>(List.of(book, book2)));
        mockMvc.perform(get("/api/v1/books/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBooksByAuthor() throws Exception {
        Book book = new Book("Book1", "Author", Category.TECHNICAL);
        Book book2 = new Book("Book2", "Author", Category.TECHNICAL);
        PageRequest pageable = PageRequest.of(0, 50);
        when(bookService.getBooks("Author", null, pageable)).thenReturn(new PageImpl<>(List.of(book, book2)));
        mockMvc.perform(get("/api/v1/books/")
                        .param("author", "Author"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBooksByCategory() throws Exception {
        Book book = new Book("Book1", "Author", Category.TECHNICAL);
        Book book2 = new Book("Book2", "Author", Category.TECHNICAL);
        PageRequest pageable = PageRequest.of(0, 50);
        when(bookService.getBooks(null, "Technical", pageable)).thenReturn(new PageImpl<>(List.of(book, book2)));
        mockMvc.perform(get("/api/v1/books/")
                        .param("category", "Technical"))
                .andExpect(status().isOk());
    }

}
