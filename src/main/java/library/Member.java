package library;


import java.util.Objects;

/** A member of the library, identified by a number. */
public class Member {

    private final String number;
    private final String name;

    public Member(String number, String name) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("The member number must not be empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        this.number = number;
        this.name = name;
    }

    public String getNumber() { return number; }
    public String getName()   { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member other)) return false;
        return number.equals(other.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return number + " - " + name;
    }
}

