package core;

/**
 * Represents a library member within the library management system.
 * The Member class extends the base User class and includes additional 
 * personal information such as contact details.
 *
 * @author Ahmed
 * @version 1.0
 */
public class Member extends User {
    private String contactInfo;

    /**
     * Constructs a new Member instance with the specified ID, name, and contact information.
     *
     * @param id the unique identifier for the member
     * @param name the full name of the member
     * @param contactInfo the contact details of the member (e.g., email or phone number)
     */
    public Member(String id, String name, String contactInfo) {
        super(id, name);
        this.contactInfo = contactInfo;
    }

    /**
     * Gets the contact information of the member.
     *
     * @return the member's contact details
     */
    public String getContactInfo() { return contactInfo; }

    /**
     * Sets or updates the contact information of the member.
     *
     * @param contactInfo the new contact details to set
     */
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    /**
     * Retrieves the specific system role of this user.
     *
     * @return a string representing the role, which is always "Member"
     */
    @Override
    public String getRole() { return "Member"; }
}