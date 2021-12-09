package Controller;

import Model.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ValueRange;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Add appointment screen controller
 */
public class AddAppointmentController implements Initializable {
    @FXML
    private CheckBox testingPurposes;
    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private TextField location;
    @FXML
    private ComboBox<String> contacts;
    @FXML
    private TextField type;
    @FXML
    private DatePicker startDate;
    @FXML
    private TextField startTime;

    @FXML
    private TextField endTime;
    @FXML
    private TextField customerID;
    @FXML
    private TextField customerName;

    @FXML
    private Label errorLabel;

    /**
     * Selected customer coming from "select customer screen"
     * user must select a customer to create an appointment
     */
    private Customer selectedCustomer;


    /**
     * allContacts list is used to populate combobox
     * from which user may select a contact for the appointment
     */
    final ObservableList<String> allContacts = ContactDB.getListContacts();


    /**
     * Handles the save button. Read values entered by the user
     * and feeds appropriate values to the SQL "addAppointment" function found in
     * the Model.AppointmentDB class.
     * Exception event is used to catch incorrect values from being entered,
     * Function also checks that appointment time is between operating hours
     * of 8am-10pm EST, no appointments should overlap, and start time comes before end time
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    void saveClick(javafx.event.ActionEvent actionEvent) throws Exception{
        try {
            selectedCustomer = selectCustomerController.getCustomerToUpdate();
            Integer selectedCustomerID = selectCustomerController.getCustomerToUpdate().getCustomerId();
            String newTitle = title.getText();
            String newDescription = description.getText();
            String newLocation = location.getText();
            String newType = type.getText();
            String newContact = contacts.getValue().toString();
            Integer contactID = ContactDB.getContactID(newContact);
            LocalDate startDate1 = startDate.getValue();
            String startTime1 = startTime.getText();
            LocalDateTime newStart = AppointmentDB.getFormat(startDate1, startTime1);
            ZonedDateTime newStartZoned = newStart.atZone(ZoneId.systemDefault());
            ZonedDateTime utcStartZoned = newStartZoned.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDate endDate1 = startDate.getValue();
            String endTime1 = endTime.getText();
            LocalDateTime newEnd = AppointmentDB.getFormat(endDate1, endTime1);
            ZonedDateTime newEndZoned = newEnd.atZone((ZoneId.systemDefault()));
            ZonedDateTime utcEndZoned = newEndZoned.withZoneSameInstant(ZoneId.of("UTC"));
// time selected between open hours of 8am-10pmEST
            if(startIsBetweenOpenHours() || testingPurposes.isSelected()) {
                if (endIsBewteenOpenHours() || testingPurposes.isSelected()) {
     // Start time must be before end time
                    if((! utcStartZoned.equals(utcEndZoned) ) && utcStartZoned.isBefore(utcEndZoned)){
                // appointments should not overlap for a a particular contact
                        if(! isOverlapping(contactID, utcStartZoned, utcEndZoned, -1)){
                   // if no errors then appointment should be saved, and appointments screen
                   // is initiated
                            AppointmentDB.addAppointment(newTitle, newDescription, newLocation, newType,
                                    utcStartZoned, utcEndZoned, selectedCustomerID, contactID);
                            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            Object scene = FXMLLoader.load(getClass().getResource("/View/apptView.fxml"));
                            stage.setScene(new Scene((Parent) scene));
                            stage.show();}
          // catch errors and display appropriate error messages
                        else{
                            System.out.println("Overlapping appointments");
                            errorLabel.setWrapText(true);
                            errorLabel.setText("Time selection conflicts with an existing appointment.");
                        }
                    }else {
                        System.out.println("end time must be after start time");
                        errorLabel.setWrapText(true);
                        errorLabel.setText("End time must be after start time.");
                    }}
                else{System.out.println("end time out of bounds"); errorLabel.setWrapText(true);
                    errorLabel.setText("End time is not between open hours of 8am-10pm EST");}
            }else {System.out.println("start time is out of bounds"); errorLabel.setWrapText(true);
                errorLabel.setText("Start time is not between open hours of 8am-10pm EST");
                } } catch(Exception event) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter valid values for each entry.");
            alert.showAndWait();
        }
    }


    /**
     * Handle cancel button,
     * Ask user to confirm they are canceling and changes to DB
     * and returns to appointments screen upon confirmation.
     *
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
     * Check that start time provided by user does not overlap an existing
     * appointment for a specific contact, and checks against EST time zone
     * @return
     */
    private boolean startIsBetweenOpenHours(){
        ValueRange open = ValueRange.of(8,21);
        LocalDate startDate1 = startDate.getValue();
        String startTime1 = startTime.getText();
        LocalDateTime newStart = AppointmentDB.getFormat(startDate1, startTime1);
        ZonedDateTime newStartZoned = newStart.atZone(ZoneId.systemDefault());
        ZonedDateTime ESTStartZoned = newStartZoned.withZoneSameInstant(ZoneId.of("America/New_York"));
        Integer startHour = ESTStartZoned.getHour();
        return (open.isValidValue(startHour));
    }

    /**
     * Check that end time provided is within operating hours
     * of 8am-10pm EST / between hours 08:00 and 22:00 EST, and checks system
     * default time against EST time zone
     * @return
     */
    private boolean endIsBewteenOpenHours(){
        ValueRange open = ValueRange.of(8,21);
        LocalDate endDate1 = startDate.getValue();
        String endTime1 = endTime.getText();
        LocalDateTime newEnd = AppointmentDB.getFormat(endDate1, endTime1);
        ZonedDateTime newEndZoned = newEnd.atZone((ZoneId.systemDefault()));
        ZonedDateTime ESTEndZoned = newEndZoned.withZoneSameInstant(ZoneId.of("America/New_York"));
        Integer endHour = ESTEndZoned.getHour();
        return (open.isValidValue(endHour));
    }

    /**
     * Check that start and end time provided by user does not overlap an existing
     * appointment for a specific contact
     * @param contactId
     * @param tryStart
     * @param tryEnd
     * @param appointmentID
     * @return
     */
    private boolean isOverlapping(Integer contactId, ZonedDateTime tryStart, ZonedDateTime tryEnd, Integer appointmentID){
        if(AppointmentDB.getAllOverlappingCustomersAppointments(contactId, tryStart, tryEnd, appointmentID).size() > 0){
            return true;
        }return false;
    }

    /**
     * Initialize screen with customer information pre filled in the form,
     * combo box for contacts is populated, error label is blank until an error occurs
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contacts.setItems(allContacts);
        selectedCustomer = selectCustomerController.getCustomerToUpdate();
        customerID.setText(Integer.valueOf(selectedCustomer.getCustomerId()).toString());
        customerName.setText(String.valueOf(selectedCustomer.getCustomerName()));
        errorLabel.setText("");

    }
}
