package com.example.bookstore.util;

/**
 * SpecSearch Criteria for searching books
 *
 * @author chetanbhatt
 */
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

    public SpecSearchCriteria(final String key, final SearchOperation operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
    public String getKey() {
        return key;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }

}