package Model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * CUSTOMER DB class contains methods related to customer data
 */
public class CustomerDB {
    /**
     *  get all customers in database and save to observable List: allCustomers
     */
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * method returns an observable list of all customers in DB
     * @return
     */
    public static ObservableList<Customer> getAllCustomers(){
        try{
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT c.customer_ID, c.customer_name, c.address,  c.phone, c.postal_code, d.division, co.country"
                    + " FROM customers c "
                    +" INNER JOIN first_level_divisions d ON c.division_id = d.division_id"
                    +" INNER JOIN countries co ON d.country_id = co.country_id";
            ResultSet resultSet = statement.executeQuery(query);
            allCustomers.clear();
            while (resultSet.next()){
                Integer ID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone = resultSet.getString(4);
                String PostalCode = resultSet.getString(5);
                String division = resultSet.getString(6);
                String country = resultSet.getString(7);

                Customer customer = new Customer (name);
                customer.setCustomerId(ID);
                customer.setCustomerName(name);
                customer.setAddress(address);
                customer.setPhone(phone);
                customer.setPostalCode(PostalCode);
                customer.setDivision(division);
                customer.setCountry(country);

                allCustomers.add(customer);
            }
            return allCustomers;
        } catch (SQLException exception){
            System.out.println("SQL Exception " + exception.getMessage());
            return null;
        }
    }

    /**
     * query adds a new customer to the DB
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param division
     */
    public static void addCustomer(String name, String address, String postalCode, String phone, String division){
        try{
            String user = UserDB.getCurrentUserName();
            System.out.println(user);
            Statement statement = JDBC.connection.createStatement();
            Integer div_ID = firstLevelDivisionsDB.getDivisionID(division);
            String string = "INSERT INTO customers (customer_name, address, postal_code, phone, create_date, created_by, last_update, last_updated_by, division_id) "
                    +"VALUES ('" + name + "' , '" + address + "' , '" + postalCode
                    + "' , '" + phone + "' , NOW(), '" +
                    user + "' , NOW(), '" + user +"',"+ div_ID +")" ;
            statement.executeUpdate(string);
            System.out.println("Successfully added new customer");
        }catch (SQLException exception){
            System.out.println("Saving new customer SQL Exception " + exception.getMessage());
        }

    }

    /**
     * query updates an existing customer in the DB
     * @param Id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param division
     */
    public static void updateCustomer(Integer Id, String name, String address, String postalCode, String phone, String division){
        try{
            String user = User.getUsername();
            System.out.println(user);
            Statement statement = JDBC.connection.createStatement();
            Integer div_ID = firstLevelDivisionsDB.getDivisionID(division);
            String string = " UPDATE customers " +
                    " SET customer_name='" + name +"', address='" + address + "', postal_code ='" + postalCode + "', phone = '" +
                    phone + "', last_update = NOW(), last_updated_by ='" + user + "', division_id=" +div_ID +
                    " WHERE customer_id =" + Id ;
            statement.executeUpdate(string);
            System.out.println("Successfully updated customer with ID = " + Id );
        }catch (SQLException exception){
            System.out.println("Saving new customer SQL Exception " + exception.getMessage());
        }

    }

    /**
     * query deletes a customer in the DB
     * @param Id
     */
    public static void deleteCustomer(Integer Id){
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "DELETE FROM Customers" +
                    " WHERE customer_id =" + Id ;
            statement.executeUpdate(string);
            System.out.println("Successfully removed customer with ID = " + Id );
        }catch (SQLException exception){
            System.out.println("Deleting customer SQL Exception " + exception.getMessage());
        }
    }

    /**
     * method returns and observable list of all customer IDs
     * @return
     */
    public static ObservableList<Integer> getListCustomerIDs() {
        ObservableList<Integer> getAllCustomersIDs = FXCollections.observableArrayList();
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT customer_id FROM customers";
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                getAllCustomersIDs.add(id);
            } return getAllCustomersIDs;
        } catch (SQLException exception){
            System.out.println(" Getting all customers IDs SQL Exception " + exception.getMessage());
        } return null;
    }

    /**
     * method returns an observable list of all customer names
     * @return
     */
    public static ObservableList<String> getListCustomerNames() {
        ObservableList<String> getAllCustomersNames = FXCollections.observableArrayList();
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT customer_name FROM customers";
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()){
                String name = resultSet.getString(1);
                getAllCustomersNames.add(name);
            } return getAllCustomersNames;
        } catch (SQLException exception){
            System.out.println(" Getting all customers names SQL Exception " + exception.getMessage());
        } return null;
    }

    /**
     * method returns a customer name using a customerID in the DB
     * @param customerID
     * @return
     * @throws SQLException
     */
    public static String getCustomerName(Integer customerID) throws SQLException {
        String customerName = "";
        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT customer_name FROM customers WHERE customer_id =" + customerID ;
        ResultSet output = statement.executeQuery(query);
        if (output.next()){
            customerName = output.getString(1);
        }
        return customerName;
    }

    /**
     * method returns a customer ID using a customer name in the DB
     * @param customerName
     * @return
     * @throws SQLException
     */
    public static Integer getCustomerID(String customerName) throws SQLException {
        Integer customerID = 0;
        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT customer_id FROM customers WHERE customer_name = '" + customerName +"'";
        ResultSet output = statement.executeQuery(query);
        if (output.next()){
            customerID = output.getInt(1);
        }
        return customerID;
    }
}
