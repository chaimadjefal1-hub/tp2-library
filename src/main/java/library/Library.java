package library;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** The library: books, copies, members and loans. */
public class Library {

    public static final int MAX_LOANS = 5;

    private final Map<String, Book> books = new LinkedHashMap<>();       // key: ISBN
    private final Map<String, Copy> copies = new LinkedHashMap<>();      // key: inventory number
    private final Map<String, Member> members = new LinkedHashMap<>();   // key: member number
    private final List<Loan> loans = new ArrayList<>();

    public void addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Book already registered: " + book.getIsbn());
        }
        books.put(book.getIsbn(), book);
    }

    public void addCopy(String isbn, String inventoryNumber) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Unknown ISBN: " + isbn);
        }
        if (copies.containsKey(inventoryNumber)) {
            throw new IllegalArgumentException("Copy already registered: " + inventoryNumber);
        }
        copies.put(inventoryNumber, new Copy(inventoryNumber, book));
    }
    
   
    public void registerMember(Member member) {
        if (members.containsKey(member.getNumber())) {
            throw new IllegalArgumentException("Member already registered: " + member.getNumber());
        }
        members.put(member.getNumber(), member);
    }

    public Copy findCopy(String inventoryNumber) {
        Copy copy = copies.get(inventoryNumber);
        if (copy == null) {
            throw new IllegalArgumentException("Unknown copy: " + inventoryNumber);
        }
        return copy;
    }

    public Member findMember(String memberNumber) {
        Member member = members.get(memberNumber);
        if (member == null) {
            throw new IllegalArgumentException("Unknown member: " + memberNumber);
        }
        return member;
    }

    public int openLoansOf(String memberNumber) {
        int count = 0;
        for (Loan loan : loans) {
            if (loan.isOpen() && loan.getMember().getNumber().equals(memberNumber)) {
                count++;
            }
        }
        return count;
    }

    public Loan lend(String memberNumber, String inventoryNumber, LocalDate today) {
        Member member = findMember(memberNumber);          // unknown -> IllegalArgumentException
        Copy copy = findCopy(inventoryNumber);
        if (!copy.isAvailable()) {
            throw new IllegalStateException("Copy not available: " + inventoryNumber);
        }
        if (openLoansOf(memberNumber) >= MAX_LOANS) {
            throw new IllegalStateException("Member " + memberNumber + " already has "
                    + MAX_LOANS + " loans");
        }
        Loan loan = new Loan(member, copy, today);
        loans.add(loan);
        copy.setAvailable(false);
        return loan;
    }

    public Loan returnCopy(String inventoryNumber, LocalDate today) {
        Copy copy = findCopy(inventoryNumber);
        for (Loan loan : loans) {
            if (loan.isOpen() && loan.getCopy().equals(copy)) {
                loan.close(today);
                copy.setAvailable(true);
                return loan;
            }
        }
        throw new IllegalStateException("Copy is not on loan: " + inventoryNumber);
    }

    /** Books whose title contains the keyword, ignoring case. */
    public List<Book> searchByTitle(String keyword) {
        String wanted = keyword.toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(wanted)) {
                result.add(book);
            }
        }
        return result;
    }
}

