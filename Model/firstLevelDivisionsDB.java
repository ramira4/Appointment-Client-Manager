package Model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * first level division class contains methods related to division data
 */
public class firstLevelDivisionsDB {

    /**
     * Get the ID of a division using a division name in the DB
     * @param divisionName
     * @return
     */
    public static Integer getDivisionID(String divisionName){
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT division_id, division, country_id FROM first_level_divisions WHERE division = '" + divisionName +"'";
            ResultSet resultSet = statement.executeQuery(string);
            System.out.println("Successfully retrieved division ID.");
            while (resultSet.next()){
                Integer ID = resultSet.getInt(1);
                String division_name = resultSet.getString(2);
                Integer country_id = resultSet.getInt(3);
                firstLevelDivisions division = new firstLevelDivisions(ID, division_name, country_id);
                division.setDivisionID(ID);
                division.setDivision(divisionName);
                division.setCountryID(country_id);
                return ID;
            }
        } catch (SQLException exception){
            System.out.println(" Getting Div ID SQl Exception " + exception.getMessage());
        }return null;
    }

    /**
     * method returns an observable list of all divisions with a countryID in the Db
     * @param country
     * @return
     */
    public static ObservableList<String> getListDivisions(Integer country){
        ObservableList<String> getAllDivisions = FXCollections.observableArrayList();
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT division FROM first_level_divisions WHERE country_ID =" + country;
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()){
                String division = resultSet.getString(1);
                getAllDivisions.add(division);
            } return getAllDivisions;
        } catch (SQLException exception){
            System.out.println("Getting divisions SQL Exception " + exception.getMessage());
        } return null;
    }

}
