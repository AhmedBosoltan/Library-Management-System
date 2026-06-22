package core;

/**
 * An abstract representation of a user within the library management system.
 * This class serves as the base for all specific user types (such as Members and Librarians),
 * capturing shared core attributes like a unique identifier and a name.
 *
 * @author Ahmed
 * @version 1.0
 */
public abstract class User {
    private String id;
    private String name;

    /**
     * Constructs a new User instance with the specified ID and name.
     *
     * @param id the unique identifier for the user
     * @param name the full name of the user
     */
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user ID
     */
    public String getId() { return id; }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the new user ID to set
     */
    public void setId(String id) { this.id = id; }

    /**
     * Gets the full name of the user.
     *
     * @return the user's name
     */
    public String getName() { return name; }

    /**
     * Sets the full name of the user.
     *
     * @param name the new name to set
     */
    public void setName(String name) { this.name = name; }

    /**
     * Retrieves the specific system role of the user.
     * This method must be implemented by any concrete subclass to define 
     * its unique access level or role name.
     *
     * @return a string representing the role of the user
     */
    public abstract String getRole();
}