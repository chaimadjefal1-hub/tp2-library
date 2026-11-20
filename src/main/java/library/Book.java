package library;


import java.util.Objects;

/** A book of the catalogue. Two books are equal when their ISBNs are equal. */
public class Book {

    private final String isbn;
    private final String title;
    private final int year;

    public Book(String isbn, String title, int year) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("The ISBN must not be empty");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("The title must not be empty");
        }
        this.isbn = isbn;
        this.title = title;
        this.year = year;
    }

    public String getIsbn()  { return isbn; }
    public String getTitle() { return title; }
    public int getYear()     { return year; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book other)) return false;
        return isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return title + " (" + year + ", " + isbn + ")";
    }
}