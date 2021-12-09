import helper.JDBC;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Main Class
 * opens the login form and connects to the database
 */
public class main extends Application {

    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/loginForm.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Appointment Manager");
        primaryStage.setScene(scene);
        primaryStage.show();}
}
