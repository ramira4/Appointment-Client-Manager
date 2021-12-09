package Controller;

import Model.AppointmentDB;
import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main screen controller
 * allows user to open appointments, customers, and reports screens
 */
public class mainScreenController implements Initializable {
    @FXML
    private Button viewAppts;

    @FXML
    private Button viewCustomers;

    @FXML
    private Button exit;

    @FXML
    private Button viewRecords;
    @FXML
    private Label upcomingAppt;

    /**
     * customer button opens customer view screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void viewCustomersClick(javafx.event.ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) viewCustomers.getScene().getWindow();
        stage.close();
        Parent parent ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/customerRecords.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage1 = new Stage();
        stage1.setTitle("Customer Records");
        stage1.setScene(scene);
        stage1.show();
    }

    /**
     * appointment button opens appointment view screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void viewAppointmentsClick(javafx.event.ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) viewAppts.getScene().getWindow();
        stage.close();
        Parent parent ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/apptView.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage1 = new Stage();
        stage1.setTitle("Appointments");
        stage1.setScene(scene);
        stage1.show();
    }

    /**
     * records button opens records view screen
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void viewRecordsClick(javafx.event.ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) viewRecords.getScene().getWindow();
        stage.close();
        Parent parent ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/reportsView.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage1 = new Stage();
        stage1.setTitle("Records");
        stage1.setScene(scene);
        stage1.show();
    }

    /**
     * exit button exits the application
     * @param actionEvent
     */
    @FXML
    void exitClicked(javafx.event.ActionEvent actionEvent) {
        System.exit(0);
    }


    /**
     * check for appointments in the next 15 mins and sets label to
     * appointment alert or displays 'no upcoming appointments'
     * if no appointments are found
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Appointments appointments = AppointmentDB.getNext15MinAppointments();
        if (appointments != null){
            upcomingAppt.setText("You have appointment with ID: "+ appointments.getAppointmentID()
                    + " of type  '" + appointments.getType() + "'  at   " + appointments.getStart().toString()
                    );
        }
    }
}
