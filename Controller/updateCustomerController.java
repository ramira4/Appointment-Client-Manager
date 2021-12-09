package Controller;

import Model.CountriesDB;
import Model.Customer;
import Model.firstLevelDivisionsDB;
import Controller.*;
import Model.CustomerDB;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * update customer screen controller:
 * allows user to update customer information,
 * text fields are populated with existing data
 *
 */
public class updateCustomerController implements Initializable {
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    @FXML
    private TextField postalCode;

    @FXML
    private ComboBox<String> countries;
    @FXML
    private ComboBox<String> divisionBox;

    /**
     * customer to update is selected in the customerRecords view screen
     */
    private Customer selectedCustomer;

    /***
     * Handles the save button,
     * get all values in the form and call the updateCustomer method
     * in the CustomerDB
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    void saveClick(javafx.event.ActionEvent actionEvent) throws Exception{
        try {
            selectedCustomer = customerRecordsController.getCustomerToUpdate();
            Integer sameID = selectedCustomer.getCustomerId();
            String newName = name.getText();
            String newAddress = address.getText();
            String newPhone = phone.getText();
            String newPostalCode = postalCode.getText();
            String division = divisionBox.getValue().toString();
            CustomerDB.updateCustomer(sameID, newName, newAddress, newPostalCode, newPhone,division);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View/customerRecords.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        } catch(Exception event) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter valid values for each entry.");
            alert.showAndWait();
        }
    }

    /**
     * Create a list of all countries in DB to populate country combo box
     */
    private ObservableList<String> allCountries = CountriesDB.getListCountries();

    /**
     * handles country selection and calls the set divisions
     * methods, which will set the divisions combo box with
     * the corresponding division per country selected
     * @return
     */
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        System.out.println(countries.getValue().toString());
        setDivisions(countries.getValue().toString());
        return null;
    }

    /**
     * Set items in the division combo box based off of which
     * country is selected
     * @param country
     */
    public void setDivisions(String country){
        final ObservableList<String> us = firstLevelDivisionsDB.getListDivisions(1);
        final ObservableList<String> canada = firstLevelDivisionsDB.getListDivisions(3);
        final ObservableList<String> uk = firstLevelDivisionsDB.getListDivisions(2);
        switch (country){
            case "U.S":
                divisionBox.setItems(us);
                break;
            case "Canada":
                divisionBox.setItems(canada);
                break;
            case  "UK":
                divisionBox.setItems(uk);
                break;
        }

    }

    /**
     * handles cancel button,
     * confirms cancel, returns to customer records screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    void cancelClick(javafx.event.ActionEvent actionEvent) throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want to cancel changes and return to the customer records screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
            Parent parent = FXMLLoader.load(getClass().getResource("/View/customerRecords.fxml"));
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(parent));
            stage1.show();
        }
    }

    /**
     * initialize screen with form populated with
     * all existing customer data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedCustomer = customerRecordsController.getCustomerToUpdate();
        id.setText(Integer.valueOf(selectedCustomer.getCustomerId()).toString());
        name.setText(String.valueOf(selectedCustomer.getCustomerName()));
        address.setText(String.valueOf(selectedCustomer.getAddress()));
        phone.setText(String.valueOf(selectedCustomer.getPhone()));
        postalCode.setText(String.valueOf(selectedCustomer.getPostalCode()));
        countries.setItems(allCountries);
        String countrySelected =(String.valueOf(selectedCustomer.getCountry()));
        countries.setValue(countrySelected);
        setDivisions(countrySelected);
        String divisionSelected = (String.valueOf(selectedCustomer.getDivision()));
        divisionBox.setValue(divisionSelected);
    }
}
