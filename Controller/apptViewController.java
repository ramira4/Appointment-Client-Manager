package Controller;

import Model.AppointmentDB;
import Model.Appointments;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Appointment Screen Controller
 * Displays tableview of appointments and allows user to view appointments by week or month
 * Buttons allow user to return to main menu, make, update or delete an appointment
 */

public class apptViewController implements Initializable {
    @FXML
    private ToggleGroup views;
    @FXML
    private Button newAppointment;
    @FXML
    private Button deleteAppointment;
    @FXML
    private Button updateAppointment;
    @FXML
    private Button returnToMain;
    @FXML
    private RadioButton monthView;
    @FXML
    private RadioButton weekView;
    @FXML
    private TableView<Appointments> appointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsIDTableColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentTitleTableColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentDescriptionTableColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentLocationTableColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentContactTableColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentTypeTableColumn;
    @FXML
    private TableColumn<Appointments, DateTimeFormatter> appointmentStartTableColumn;
    @FXML
    private TableColumn<Appointments, DateTimeFormatter> appointmentEndTableColumn;
    @FXML
    private TableColumn<Appointments, Integer> customerIDTableColumn;
    @FXML
    private TableColumn<Appointments, Integer> userIDTableColumn;


    /**
     * Return selected appointment in the tableview, send to the update screen
     */
    private static Appointments appointmentToUpdate;
    static Appointments getAppointmentToUpdate(){
        return appointmentToUpdate;
    }

    /**
     * Return appointment selected to be deleted
     */
    private static Appointments appointmentToDelete;
    static Appointments getAppointmentToDelete(){
        return appointmentToDelete;
    }

    /**
     *     when user selects month view, the table will populate using a
     *     month query in the AppointmentDB
      */

    @FXML
    void monthlySelected(javafx.event.ActionEvent actionEvent){
        appointmentsTableView.setItems(AppointmentDB.getMonthAppointments());
        appointmentsIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactTableColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartTableColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndTableColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    /**
     * When user selects week view, table will populate with data from the
     * week query in the AppointmentDB
     * @param actionEvent
     */
    @FXML
    void weeklySelected(javafx.event.ActionEvent actionEvent){
        appointmentsTableView.setItems(AppointmentDB.getWeekAppointments());
        appointmentsIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactTableColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartTableColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndTableColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    /**
     * Handles new appointment button and opens the select customer screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void newAppointmentClick(javafx.event.ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) newAppointment.getScene().getWindow();
        stage.close();
        Parent parent ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/selectCustomer.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage1 = new Stage();
        stage1.setTitle("New Appointment");
        stage1.setScene(scene);
        stage1.show();
    }

    /**
     * handles the delete button, verify that user has selected an appointment to
     * delete and confirms deletion
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void deleteAppointmentClick(javafx.event.ActionEvent actionEvent) throws Exception {
        appointmentToDelete = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (appointmentToDelete == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
        } else {
            Integer apptID = appointmentToDelete.getAppointmentID();
            String apptType = appointmentToDelete.getType();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting Appointment");
            alert.setContentText("Are you sure you want to delete appointment with ID: " + apptID +
                " and type: " + apptType +"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
            Integer deleteId = appointmentToDelete.getAppointmentID();
            AppointmentDB.deleteAppointment(deleteId);
            Stage stage = (Stage) deleteAppointment.getScene().getWindow();
            stage.close();
            Parent parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/apptView.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage1 = new Stage();
            stage1.setTitle("Appointment Records");
            stage1.setScene(scene);
            stage1.show();}
        }
    }

    /**
     * Handles update appointment button, confirms an appointment has
     * been selected to update and opens the update appointment screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void updateAppointmentClick(javafx.event.ActionEvent actionEvent) throws Exception {
        appointmentToUpdate = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (appointmentToUpdate == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) updateAppointment.getScene().getWindow();
            stage.close();
            Parent parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/updateAppointment.fxml"));
            parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage1 = new Stage();
            stage1.setTitle("Update Appointment");
            stage1.setScene(scene);
            stage1.show();
        }
    }

    /**
     * handles the return button and opens the main screen
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
     * Default month view
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthView.setSelected(true);
        appointmentsTableView.setItems(AppointmentDB.getMonthAppointments());
        appointmentsIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactTableColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartTableColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndTableColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }
}
