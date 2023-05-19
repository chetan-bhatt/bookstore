package com.example.bookstore.controller;

import com.example.bookstore.api.BooksApi;
import com.example.bookstore.exception.BookStoreException;
import com.example.bookstore.exception.ErrorCode;
import com.example.bookstore.util.BookSpecificationsBuilder;
import com.example.bookstore.util.SearchOperation;
import com.example.bookstore.rest.AddBookRequest;
import com.example.bookstore.rest.Book;
import com.example.bookstore.rest.Category;
import com.example.bookstore.rest.PagedBookResponse;
import com.example.bookstore.service.BookService;
import com.example.bookstore.transformer.ModelTransformer;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This class represents the Rest Controller for the bookstore application.
 * @author chetanbhatt
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookController implements BooksApi {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addBook(@ApiParam(value = "Request Body for adding a book" ,required=true )  @Valid @RequestBody AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setName(addBookRequest.getName());
        book.setAuthor(addBookRequest.getAuthor());
        book.setCategory(Category.fromValue(addBookRequest.getCategory().getValue()));
        bookService.add(ModelTransformer.fromRestToModel.apply(book)).getId();
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteBook(@ApiParam(value = "The id of the book to delete",required=true) @PathVariable("id") String id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBook(@ApiParam(value = "The id of the book to retrieve",required=true) @PathVariable("id") String id) {
        com.example.bookstore.model.Book book = bookService.get(id);
        return ResponseEntity.ok(ModelTransformer.fromModelToRest.apply(book));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedBookResponse> getBooks(@ApiParam(value = "Get the books by the author") @Valid @RequestParam(value = "author", required = false) String author,@ApiParam(value = "Get the books by category") @Valid @RequestParam(value = "category", required = false) String category,@ApiParam(value = "Get the books by the author", defaultValue = "0") @Valid @RequestParam(value = "page", required = false, defaultValue="0") Integer page,@ApiParam(value = "Get the books by the author", defaultValue = "50") @Valid @RequestParam(value = "size", required = false, defaultValue="50") Integer size) {
        Page<com.example.bookstore.model.Book> bookPage = bookService.getBooks(author, category, PageRequest.of(page, size));
        return ResponseEntity.ok(convertToPagedBookResponse(bookPage));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedBookResponse> searchBook(@NotNull @ApiParam(value = "Search books by providing query string. Only Equality operator(:) is supported as of now. Example of a query string - name:abc", required = true) @Valid @RequestParam(value = "query", required = true) String query,@ApiParam(value = "Get the books by the author", defaultValue = "0") @Valid @RequestParam(value = "page", required = false, defaultValue="0") Integer page,@ApiParam(value = "Get the books by the author", defaultValue = "50") @Valid @RequestParam(value = "size", required = false, defaultValue="50") Integer size) {
        BookSpecificationsBuilder builder = new BookSpecificationsBuilder();
        if(!query.contains(SearchOperation.SIMPLE_OPERATION_SET)){
            throw new BookStoreException(ErrorCode.VALIDATION_ERROR, null, "Invalid query string");
        }
        String[] searchParams = query.trim().split(SearchOperation.SIMPLE_OPERATION_SET);
        builder.with(searchParams[0], SearchOperation.SIMPLE_OPERATION_SET, searchParams[1]);
        Page<com.example.bookstore.model.Book> bookPage = bookService.getBooks(builder.build(), PageRequest.of(page, size));
        return ResponseEntity.ok(convertToPagedBookResponse(bookPage));
    }

    private PagedBookResponse convertToPagedBookResponse(final Page<com.example.bookstore.model.Book> bookPage) {
        PagedBookResponse response = new PagedBookResponse();
        response.setBooks(ModelTransformer.fromModelListToRestList.apply(bookPage.getContent()));
        response.setCurrentPage(bookPage.getNumber());
        response.setTotalPages(bookPage.getTotalPages());
        response.setTotalItems(bookPage.getTotalElements());
        return response;
    }
}
