package com.example.bookstore.model;

import com.example.bookstore.rest.Category;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;
import java.util.UUID;

/**
 * This is the Entity which is to be persisted in the db.
 * @author chetanbhatt
 */
@Getter
@NonNull
@Entity
@Table(indexes = { @Index(name = "author_index", columnList = "author"),
                    @Index(name = "name_index", columnList = "name"),
                },
        uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "author" }) }
)
public final class Book {
    @Id
    private String id;

    private String name;

    private String author;

    private String category;

    public Book(){}

    public Book(String name, String author, Category category) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.author = author;
        this.category = category.getValue();
    }
}