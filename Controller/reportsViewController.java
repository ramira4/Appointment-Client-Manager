package Controller;

import Model.AppointmentDB;
import Model.ContactDB;
import Model.Contacts;
import Model.CustomerDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Reports view controller, allows user generate the following three reports:
 * 1. count/type of appointment in  a month
 * 2. count appointments/customer in a month
 * 3. appointments schedules per contact
 */
public class reportsViewController implements Initializable {
    @FXML
    private TextArea outputText;
    @FXML
    private Button returnToMain;

    @FXML
    private ComboBox<String> contacts;
    @FXML
    private ComboBox<String> customers;
    @FXML
    private Button appointmentsPerCustomer;
    @FXML
    private Label label2;
    @FXML
    private Label label3;

    /**
     * Get contact list for combo box of all contacts in DB, schedule will generate for
     * selected contact
     */
    final ObservableList<String> allContacts = ContactDB.getListContacts();


    /**
     * Get customer list for combo box of all customer in DB, appointment count
     * report will generate for
     * selected customer
     */
    final ObservableList<String> allCustomers = CustomerDB.getListCustomerNames();

    /**
     * count types button will generate a report of number of appointments of each type
     * per month, report is read from file and displayed in text area
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void countTypesClick(javafx.event.ActionEvent actionEvent) throws Exception {
        outputText.setText("APPOINTMENTS BY TYPE/MONTH:  ");
        try {
            PrintWriter writer = new PrintWriter("countTypes.txt");
            writer.print("");
            writer.close();
            }
             catch (FileNotFoundException exception) {
            System.err.println(exception);
        }
        try {
            outputText.setWrapText(true);
            AppointmentDB.countTypesPerMonth();
/** //////////LAMBDA EXPRESSIONS///////
 *  Lambda expression is used here to append each line read from a report '.txt' file
 *  into a text area in my FXML document. It replaced a longer method which involved using
 *  a Scanner s and doing a while loop of  s.hasNext() method of appending lines.
 */
            try(Stream<String> stream  = Files.lines(Paths.get("countTypes.txt"))){
                stream.forEach(line -> outputText.appendText(line));
            }

        } catch (FileNotFoundException exception) {
            System.err.println(exception);
        }

    }

    /**
     * Generates schedule of appointments
     * When user selects a contact from the combo box,
     * and displays report in text area
     * @param actionEvent
     * @throws Exception
     *  LAMBDA expression is used here to append each line read from a report '.txt' file
     *  into a text area in my FXML document. It replaced a longer method which involved using
     *  a Scanner s and doing a while loop of  s.hasNext() method of appending lines.
     */
    @FXML
    public void getContactSchedules(javafx.event.ActionEvent actionEvent) throws Exception{
        label3.setText("Select a contact to generate schedule:");
        String contactsValue = contacts.getValue();
        outputText.setText("APPOINTMENT SCHEDULE FOR CONTACT: " + contactsValue + "  ");
        try {
            PrintWriter writer = new PrintWriter("schedules.txt");
            writer.print("");
            writer.close();
        }
        catch (FileNotFoundException exception) {
            System.err.println(exception);
        }
        try {
            AppointmentDB.getSchedule(contactsValue);
            outputText.setWrapText(true);
            try(Stream<String> stream  = Files.lines(Paths.get("schedules.txt"))){
                stream.forEach(line -> outputText.appendText(line));
            }
        } catch (FileNotFoundException exception) {
            System.err.println(exception);
        }
    }

    /**
     * counts number of appointments per customer in a month
     * when user selects a customer name from customer combo box
     * and displays report in text area
     * @param actionEvent
     * @throws Exception
     *  LAMBDA expression is used here to append each line read from a report '.txt' file
     *  into a text area in my FXML document. It replaced a longer method which involved using
     *  a Scanner s and doing a while loop of  s.hasNext() method of appending lines.
     */
    @FXML
    public void getApptCount(javafx.event.ActionEvent actionEvent) throws Exception{
        outputText.setText("");
        String customersValue = customers.getValue();
        try {
            PrintWriter writer = new PrintWriter("appCount.txt");
            writer.print("");
            writer.close();
        }
        catch (FileNotFoundException exception) {
            System.err.println(exception);
        }
        try {
            label2.setText("Select a customer to count appointments/ month: ");
            AppointmentDB.getAppointmentCount(customersValue);
            outputText.setWrapText(true);
        try(Stream<String> stream  = Files.lines(Paths.get("appCount.txt"))){
                stream.forEach(line -> outputText.appendText(line));
            }
        } catch (FileNotFoundException exception) {
            System.err.println(exception);
        }
    }

    /**
     * handles return button,
     * back to main screen
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
     * initialize with combo boxes populated
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contacts.setItems(allContacts);
        customers.setItems(allCustomers);
    }
}
