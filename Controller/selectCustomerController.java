package Controller;

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
 * Select customer screen controller
 * When users create a new appointment they must first select a customer for
 * which to create the appointment.
 * User selects a customer from a customer table view,
 * then continues to new appointment form
 */
public class selectCustomerController implements Initializable {

    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, Integer> id;
    @FXML
    private TableColumn<Customer, String> name;
    @FXML
    private Button cancel;
    @FXML
    private Button continueButton;

    /**
     * Returns the customer selected in the tableview
     */
    private static Customer customerToUpdate;
    static Customer getCustomerToUpdate(){
        return customerToUpdate;
    }

    /**
     * handles continue button click
     * checks that a customer has been selected
     * and then opens the add appointment screen
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
            alert.setContentText("Please select a customer to create an appointment.");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.close();
            Parent parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/addAppointment.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage1 = new Stage();
            stage1.setTitle("New Appointment");
            stage1.setScene(scene);
            stage1.show();
        }
    }

    /**
     * handles  cancel button click,
     * confirms user wishes to cancel changes and returns to
     * appointment view screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    void cancelClick(javafx.event.ActionEvent actionEvent) throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want to cancel changes and return to the appointment records screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
            Parent parent = FXMLLoader.load(getClass().getResource("/View/apptView.fxml"));
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(parent));
            stage1.show();
        }
    }

    /**
     * populate customers table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customersTable.setItems(CustomerDB.getAllCustomers());
        id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
}
