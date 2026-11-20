package library;


import java.util.Objects;

/** A physical copy of a book, identified by its inventory number. */
public class Copy {

    private final String inventoryNumber;
    private final Book book;
    private boolean available = true;

    public Copy(String inventoryNumber, Book book) {
        if (inventoryNumber == null || inventoryNumber.isBlank()) {
            throw new IllegalArgumentException("The inventory number must not be empty");
        }
        this.inventoryNumber = inventoryNumber;
        this.book = Objects.requireNonNull(book, "book");
    }

    public String getInventoryNumber() { return inventoryNumber; }
    public Book getBook()              { return book; }
    public boolean isAvailable()       { return available; }

    /** Only the library changes the availability (same package). */
    void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Copy other)) return false;
        return inventoryNumber.equals(other.inventoryNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryNumber);
    }

    @Override
    public String toString() {
        return inventoryNumber + " - " + book.getTitle();
    }
}
