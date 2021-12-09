package Controller;

import Model.AppointmentDB;
import Model.Customer;

import Model.CustomerDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Customer Records Screen Controller Class
 * includes tableview of customers and allows user to
 * create, update and delete customers
 */
public class customerRecordsController implements Initializable {

    @FXML
    private Button returnToMain;
    @FXML
    private Button newCustomer;
    @FXML
    private Button updateCustomer;
    @FXML
    private Button deleteCustomer;
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, Integer> id;
    @FXML
    private TableColumn<Customer, String> name;
    @FXML
    private TableColumn<Customer, String> phone;
    @FXML
    private TableColumn<Customer, String> address;
    @FXML
    private TableColumn<Customer, String> country;
    @FXML
    private TableColumn<Customer, String> division;
    @FXML
    private TableColumn<Customer, String> postalCode;

    /**
     * Returns customer selected by the user in the tableview
     */
    private static Customer customerToUpdate;
    static Customer getCustomerToUpdate(){
        return customerToUpdate;
    }

    /**
     * Handles the update button,
     * Confirms a customer has been selected to update and opens the
     * update customer screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void updateCustomerClick(javafx.event.ActionEvent actionEvent) throws Exception {
        customerToUpdate = customersTable.getSelectionModel().getSelectedItem();
        if (customerToUpdate == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select a customer to update");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) updateCustomer.getScene().getWindow();
            stage.close();
            Parent parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/updateCustomer.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage1 = new Stage();
            stage1.setTitle("Update Customer");
            stage1.setScene(scene);
            stage1.show();
        }
    }

    /**
     * Handles the return button and takes user back to the main screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void returnClick(javafx.event.ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) returnToMain.getScene().getWindow();
        stage.close();
        Parent parent ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainScreen.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage1 = new Stage();
        stage1.setTitle("Appointment Manager");
        stage1.setScene(scene);
        stage1.show();
    }

    /**
     * handles the new customer button:
     * opens the add customer screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void newCustomerClick(javafx.event.ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) newCustomer.getScene().getWindow();
        stage.close();
        Parent parent ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/addCustomer.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage1 = new Stage();
        stage1.setTitle("Add Customer");
        stage1.setScene(scene);
        stage1.show();
    }

    /**
     * handles the delete customer button,
     * confirms that a customer has been selected to delete
     * then confirms action, and that the customer has no active appointments
     * then reloads screen after deletion
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void deleteCustomerClick(javafx.event.ActionEvent actionEvent) throws Exception {
        customerToUpdate = customersTable.getSelectionModel().getSelectedItem();
        if (customerToUpdate == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select a customer to delete");
            alert.showAndWait();
        } else {
            Integer deleteId = customerToUpdate.getCustomerId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting Appointment");
            alert.setContentText("Are you sure you want to delete the customer with id: " + deleteId +"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                AppointmentDB.getAllCustomersAppointments(deleteId).clear();
                if (AppointmentDB.getAllCustomersAppointments(deleteId).size() == 0 ){
                    CustomerDB.deleteCustomer(deleteId);
                    Stage stage = (Stage) deleteCustomer.getScene().getWindow();
                    stage.close();
                    Parent parent;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/customerRecords.fxml"));
                    parent = loader.load();
                    Scene scene = new Scene(parent);
                    Stage stage1 = new Stage();
                    stage1.setTitle("Customer Records");
                    stage1.setScene(scene);
                    stage1.show();} else {
                            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                            alert1.setTitle("Deleting Appointment Error");
                            alert1.setContentText("Customer with id: " + deleteId +" has active appointments and may not be deleted.");
                            Optional<ButtonType> result1 = alert1.showAndWait();
                        }
        }}
    }

    /**
     * populate the customers table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customersTable.setItems(CustomerDB.getAllCustomers());
        id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        division.setCellValueFactory(new PropertyValueFactory<>("division"));

    }
}
