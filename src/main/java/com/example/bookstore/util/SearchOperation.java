package com.example.bookstore.util;

/**
 * Search operations for searching books.
 * Currently, only Equality operation is supported.
 *
 * @author chetanbhatt
 */
public enum SearchOperation {
    EQUALITY;
    public static final String SIMPLE_OPERATION_SET = ":";
    public static SearchOperation getSimpleOperation(final char input) {
        switch (input) {
            case ':':
                return EQUALITY;
            default:
                return null;
        }
    }
}