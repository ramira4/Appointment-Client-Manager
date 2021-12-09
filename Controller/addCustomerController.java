package Controller;

import Model.*;
import helper.*;
import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Label;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Add customer screen controller: allows user to save a new customer to the DB
 * and collects user input
 */
public class addCustomerController implements Initializable {
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private javafx.scene.control.TextField name;
    @FXML
    private javafx.scene.control.TextField address;
    @FXML
    private javafx.scene.control.TextField phone;
    @FXML
    private javafx.scene.control.TextField postalCode;
    @FXML
    private ComboBox<String> countries;
    @FXML
    private ComboBox<String> divisionBox;

    private ObservableList<String> allCountries = CountriesDB.getListCountries();


    /**
     * Handle the save button: get all customer information input by user and feed it
     * to the 'addCustomer' function found in the Model.CustomerDB class,
     * catch exception used to display errors
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    void saveClick(javafx.event.ActionEvent actionEvent) throws Exception{
        try {
            String newName = name.getText();
            String newAddress = address.getText();
            String newPhone = phone.getText();
            String newPostalCode = postalCode.getText();
            String division = divisionBox.getValue().toString();
            CustomerDB.addCustomer(newName, newAddress, newPostalCode, newPhone,division);
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
     * Handle cancel button: confirms that user wants to cancel changes
     * and opens the customer screen upon confirmation.
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
     * When user selects a country, this method will call the 'setDivisions'
     * method so that the division combo box is populated with divisions belonging
     * to the selected country
     * @return
     */
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        System.out.println(countries.getValue().toString());
        setDivisions(countries.getValue().toString());
        return null;
    }

    /**
     * populate division combo box for corresponding country selection
     * @param country
     */
    public void setDivisions(String country){
        final ObservableList<String> us = firstLevelDivisionsDB.getListDivisions(1);
        final ObservableList<String> canada = firstLevelDivisionsDB.getListDivisions(3);
        final ObservableList<String> uk = firstLevelDivisionsDB.getListDivisions(2);
        switch (country){
            case "U.S" :
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
     * open screen with only the country combo box pre-populated
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countries.setItems(allCountries);
    }
}
