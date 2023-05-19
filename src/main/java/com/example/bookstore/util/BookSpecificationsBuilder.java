package com.example.bookstore.util;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification Builder
 *
 * @author chetanbhatt
 */
public class BookSpecificationsBuilder {

    private final List<SpecSearchCriteria> params;

    public BookSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public final BookSpecificationsBuilder with(final String key, final String operation, final Object value) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            params.add(new SpecSearchCriteria(key, op, value));
        }
        return this;
    }

    public Specification<Book> build() {
        if (params.size() == 0)
            return null;

        Specification<Book> result = new BookSpecification(params.get(0));
        return Specification.where(result);
    }
}
