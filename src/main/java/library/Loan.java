package library;


import java.time.LocalDate;
import java.util.Objects;

/** The loan of a copy to a member. It is due 14 days after it starts. */
public class Loan {

    public static final int LOAN_DAYS = 14;

    private final Member member;
    private final Copy copy;
    private final LocalDate startDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;            // null while the loan is in progress

    public Loan(Member member, Copy copy, LocalDate startDate) {
        this.member = Objects.requireNonNull(member, "member");
        this.copy = Objects.requireNonNull(copy, "copy");
        this.startDate = Objects.requireNonNull(startDate, "startDate");
        this.dueDate = startDate.plusDays(LOAN_DAYS);
    }

    public Member getMember()        { return member; }
    public Copy getCopy()            { return copy; }
    public LocalDate getStartDate()  { return startDate; }
    public LocalDate getDueDate()    { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    /** A loan is open until the copy is returned. */
    public boolean isOpen() {
        return returnDate == null;
    }

    /** Late: still open and today is after the due date. */
    public boolean isLate(LocalDate today) {
        return isOpen() && today.isAfter(dueDate);
    }

    /** Only the library closes a loan (same package). */
    void close(LocalDate date) {
        this.returnDate = date;
    }
}
