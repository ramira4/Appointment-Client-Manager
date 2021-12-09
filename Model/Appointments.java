package Model;
/**
 * Appointments class includes all required fields / columns
 * of the appointments table view.
 */

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class Appointments {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String contact;
    private Timestamp start;
    private Timestamp end;
    private int customerID;
    private int userID;

    /**
     * Constructor
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param contact
     * @param start
     * @param end
     * @param customerID
     * @param userID
     */
    public Appointments(int appointmentID, String title, String description, String location, String type, String contact, Timestamp start, Timestamp end, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.contact = contact;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;

    }
    /**
     * SETTERS & GETTERS
     */

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
