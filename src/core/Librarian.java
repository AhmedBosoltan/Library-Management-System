package core;

/**
 * Represents a librarian user within the library management system.
 * The Librarian class extends the base User class and defines specific 
 * roles and permissions associated with library staff.
 *
 * @author Ahmed
 * @version 1.0
 */
public class Librarian extends User {
    
    /**
     * Constructs a new Librarian instance with the specified ID and name.
     *
     * @param id the unique identifier for the librarian
     * @param name the full name of the librarian
     */
    public Librarian(String id, String name) {
        super(id, name);
    }

    /**
     * Retrieves the specific system role of this user.
     *
     * @return a string representing the role, which is always "Librarian"
     */
    @Override
    public String getRole() { return "Librarian"; }
}