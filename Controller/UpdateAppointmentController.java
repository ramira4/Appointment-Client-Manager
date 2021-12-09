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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ValueRange;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Update appointment screen controller:
 * allows user to update existing appointment,
 */
public class UpdateAppointmentController implements Initializable {
    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private TextField appointmentID;
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
    private Label errorLabel;


    private Appointments appointmentToModify;
    final ObservableList<String> allContacts = ContactDB.getListContacts();

    /**
     * Handles the save button. Read values entered by the user
     * and feeds appropriate values to the SQL "updateAppointment" function found in
     * the Model.AppointmentDB class.
     *  Exception event is used to catch incorrect values from being entered,
     * Function also checks that appointment time is between operating hours
     * of 8am-10pm EST, no appointments should overlap, and start time comes before end time
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    void saveClick(javafx.event.ActionEvent actionEvent) throws Exception{
        try {
            Integer selectedCustomerID = Integer.parseInt(appointmentID.getText());
            String newTitle = title.getText();
            String newDescription = description.getText();
            String newLocation = location.getText();
            String newType = type.getText();
            String newContact = contacts.getValue();
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

            /**
             * check time slot is between operating hours
             */
            if(startIsBetweenOpenHours()) {
                if (endIsBewteenOpenHours()) {
                    /**
                     * check that start time is before end time
                     */
                    if((! utcStartZoned.equals(utcEndZoned) ) && utcStartZoned.isBefore(utcEndZoned)){
                        /**
                         * verify that appointment does not overlap with an existing appointment
                         */
                        if(! isOverlapping(contactID, utcStartZoned, utcEndZoned, selectedCustomerID)){
                        AppointmentDB.updateAppointment(selectedCustomerID, newTitle, newDescription, newLocation, newType,
                                utcStartZoned, utcEndZoned, selectedCustomerID, contactID);
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        Object scene = FXMLLoader.load(getClass().getResource("/View/apptView.fxml"));
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();}
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
                }
        } catch(Exception event) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter valid values for each entry.");
            alert.showAndWait();

        }
    }

    /**
     * handles cancel button,
     * confirms cancel, returns user to appointment view
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
     * check that start time is between open hours of 8am-10pmEST / 08:00-22:00EST
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
     * check that end time is between open hours of 8am-10pmEST / 08:00-22:00EST
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
     * check that time slot does not overlap with an existing appointment for the
     * selected contact
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
     *     Initialize update appointment form with all fields prefilled with existing values
     *     in the DB
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
        contacts.setItems(allContacts);
        appointmentToModify = apptViewController.getAppointmentToUpdate();
        Integer customerId =appointmentToModify.getCustomerID();
        customerID.setText(String.valueOf(customerId));
        appointmentID.setText(String.valueOf(appointmentToModify.getAppointmentID()));
        title.setText(String.valueOf(appointmentToModify.getTitle()));
        description.setText(String.valueOf(appointmentToModify.getDescription()));
        location.setText(String.valueOf(appointmentToModify.getLocation()));
        String contactName = String.valueOf(appointmentToModify.getContact());
        contacts.setValue(contactName);
        type.setText(String.valueOf(appointmentToModify.getType()));
        LocalDateTime startSet = appointmentToModify.getStart().toLocalDateTime();
        String [] getTime = startSet.toString().split("T");
        startDate.setValue(startSet.toLocalDate());
        startTime.setText(getTime[1]);
        LocalDateTime endSet = appointmentToModify.getEnd().toLocalDateTime();
        String [] getEnd = endSet.toString().split("T");
        endTime.setText(getEnd[1]);

    }
}
