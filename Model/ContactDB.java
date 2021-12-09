package Model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Contact DB class contains methods related to
 * contact data
 */
public class ContactDB {

    /**
     * get contact using
     * @param contactName
     * @return contact ID
     */
    public static Integer getContactID(String contactName){
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT contact_id FROM contacts WHERE contact_name = '" + contactName +"'";
            ResultSet resultSet = statement.executeQuery(string);
            System.out.println("Successfully retrieved contact ID.");
            while (resultSet.next()){
                Integer ID = resultSet.getInt(1);
                return ID;
            }
        } catch (SQLException exception){
            System.out.println(" Getting contact ID SQl Exception " + exception.getMessage());
        }return null;
    }

    /**
     *
     * @return an observable list of all contact names in the DB
     */
    public static ObservableList<String> getListContacts() {
        ObservableList<String> getAllContacts = FXCollections.observableArrayList();
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT contact_name FROM contacts";
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()){
                String name = resultSet.getString(1);
                getAllContacts.add(name);
            } return getAllContacts;
        } catch (SQLException exception){
            System.out.println(" Getting all contacts SQL Exception " + exception.getMessage());
        } return null;
    }

    /**
     * method gets a contact name using a contact id
     * @param id
     * @return
     * @throws SQLException
     */
    public static String getContactName(Integer id) throws SQLException {
        String  contactName = "";
        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT contact_name FROM contacts WHERE contact_id =" + id ;
        ResultSet output = statement.executeQuery(query);
        if (output.next()){
            contactName = output.getString(1);
        }
        return contactName;
    }
}
