package Model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * COUNTRIES DB class contains methods related to
 * country data
 */
public class CountriesDB {

    /**
     * get an observable list of all countries in the DB
     * @return
     */
    public static ObservableList<String> getListCountries() {
        ObservableList<String> getAllCountries = FXCollections.observableArrayList();
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT Country FROM countries";
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()){
                String country = resultSet.getString(1);
                getAllCountries.add(country);
            } return getAllCountries;
        } catch (SQLException exception){
            System.out.println(" Getting all Countries SQL Exception " + exception.getMessage());
        } return null;
    }
}
