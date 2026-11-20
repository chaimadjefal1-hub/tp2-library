package library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryTest{
    private static final String ISBN = "978-0-00-000001-1";
    private static final LocalDate DAY = LocalDate.of(2026, 10, 1);
    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
        library.addBook(new Book(ISBN, "Software Engineering", 2016));
        library.addCopy(ISBN, "C-001");
        library.registerMember(new Member("M-01", "First member"));
    }

    @Test
    void lendingMakesTheCopyUnavailable() {
        Loan loan = library.lend("M-01", "C-001", DAY);
        assertFalse(library.findCopy("C-001").isAvailable());
        assertEquals(DAY.plusDays(14), loan.getDueDate());
    }

    @Test
    void aCopyThatIsNotAvailableCannotBeLent() {
        library.registerMember(new Member("M-02", "Second member"));
        library.lend("M-01", "C-001", DAY);
        assertThrows(IllegalStateException.class, () -> library.lend("M-02", "C-001", DAY));
    }

    @Test
    void aMemberCannotHaveMoreThanFiveLoans() {
        for (int i = 2; i <= 6; i++) {
            library.addCopy(ISBN, "C-00" + i);
        }
        for (int i = 1; i <= 5; i++) {
            library.lend("M-01", "C-00" + i, DAY);
        }
        assertEquals(5, library.openLoansOf("M-01"));
        assertThrows(IllegalStateException.class, () -> library.lend("M-01", "C-006", DAY));
        assertTrue(library.findCopy("C-006").isAvailable());     // the refusal changed nothing
    }

    @Test
    void unknownNumbersAreRefused() {
        assertThrows(IllegalArgumentException.class, () -> library.lend("M-99", "C-001", DAY));
        assertThrows(IllegalArgumentException.class, () -> library.lend("M-01", "C-999", DAY));
        assertThrows(IllegalArgumentException.class, () -> library.addCopy("unknown", "C-002"));
        assertThrows(IllegalArgumentException.class, () -> library.returnCopy("C-999", DAY));
    }

    @Test
    void returningACopyMakesItAvailableAndClosesTheLoan() {
        Loan loan = library.lend("M-01", "C-001", DAY);
        library.returnCopy("C-001", DAY.plusDays(3));
        assertTrue(library.findCopy("C-001").isAvailable());
        assertFalse(loan.isOpen());
        assertEquals(0, library.openLoansOf("M-01"));
    }

    @Test
    void aCopyThatIsNotOnLoanCannotBeReturned() {
        assertThrows(IllegalStateException.class, () -> library.returnCopy("C-001", DAY));
    }

    @Test
    void aLoanIsLateOnlyAfterTheDueDate() {
        Loan loan = library.lend("M-01", "C-001", DAY);
        assertTrue(loan.isOpen());
        assertFalse(loan.isLate(DAY.plusDays(14)));      // the due day itself is not late
        assertTrue(loan.isLate(DAY.plusDays(15)));
        library.returnCopy("C-001", DAY.plusDays(20));
        assertFalse(loan.isLate(DAY.plusDays(30)));      // a returned loan is never late
    }

    @Test
    void booksAreEqualWhenTheirIsbnsAreEqual() {
        assertEquals(new Book(ISBN, "Same ISBN, other title", 1999),
                new Book(ISBN, "Software Engineering", 2016));
    }

    @Test
    void searchByTitleIgnoresCase() {
        library.addBook(new Book("978-0-00-000002-8", "Design Patterns", 1994));

        assertEquals(1, library.searchByTitle("SOFTWARE").size());
        assertEquals("Software Engineering",
                library.searchByTitle("engineer").get(0).getTitle());
        assertEquals(1, library.searchByTitle("patterns").size());
    }

    @Test
    void searchByTitleReturnsNothingWhenNoTitleMatches() {
        assertTrue(library.searchByTitle("cooking").isEmpty());
    }
}
