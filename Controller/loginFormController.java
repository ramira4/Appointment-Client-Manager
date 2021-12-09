package Controller;

import Model.*;
import helper.*;

import java.io.*;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Login screen controller,
 * verifies correct login input, logs all attempts to 'login_activity.txt',
 * sets text to system language
 */
public class loginFormController implements Initializable {
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label locationLabel;
    private ZoneId location = ZoneId.systemDefault();

    /**
     * Handles sign in button
     * Verify that username and password match the DB,
     * error is displayed on screen and calls the log attempt method
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    public void signOnAction(javafx.event.ActionEvent event) throws SQLException, IOException {
        String usernameText = username.getText();
        String passwordText = password.getText();

        if (UserDB.successfulLogin(usernameText, passwordText)){
            logAttempt(usernameText, true);
            errorLabel.setText("Successful login");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            Parent parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainScreen.fxml"));
            parent = loader.load();
            Scene scene1 = new Scene(parent);
            Stage stage1 = new Stage();
            stage1.setTitle("Appointment Manager");
            stage1.setScene(scene1);
            stage1.show();
        }else{
            logAttempt(usernameText, false);
            Locale locale = Locale.getDefault();
            ResourceBundle resourceBundle;
            resourceBundle = ResourceBundle.getBundle("languages/messages", locale);
            errorLabel.setText(resourceBundle.getString("error"));
        }

    }

    /**
     * writes successful and failed login attempts to file 'login_activity.txt'
     * @param userName
     * @param success
     */
    public static void logAttempt (String userName, boolean success){
        try(
            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter)){
            printWriter.println(userName + " " + (success ? "Successful" : "Failed to login") +
                    " "+ Instant.now().toString());
        } catch (Exception exception){
            System.out.println("Log login attempt error" + exception.getMessage());
        }

    }

    /**
     * initialized with text set to system language
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("languages/messages", locale);
        titleLabel.setText(resourceBundle.getString("title"));
        userNameLabel.setText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        loginButton.setText(resourceBundle.getString("signIn"));
        errorLabel.setText("");
        locationLabel.setText(location.toString());
    }
}
