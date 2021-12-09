package Model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * USER DB class contains methods relates to user data
 */
public class UserDB {
    /**
     * get current user
     */
    private static String currentUserName;
    public static String getCurrentUserName() {
        return currentUserName;
    }


    private static Integer currentUserID;
    /**
     * get current user ID
     */
    public static Integer getCurrentUserID() throws SQLException {
        currentUserID = getUserId(currentUserName);
        return currentUserID;
    }

    /**
     * verify that username and password are correct and match DB
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static boolean successfulLogin(String username, String password) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT Password FROM USERS WHERE User_Name ='" +username+"'";
        ResultSet result = statement.executeQuery(query);
        if(result.next()){
            if (result.getString(1).equals(password)){
                currentUserName = username;
                return true;
            }}
        return false;

    }

    /**
     * get a user ID by using a username
     * @param usernameText
     * @return
     * @throws SQLException
     */
    private static int getUserId(String usernameText) throws SQLException {
        int userId = 0;
        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT User_ID FROM USERS WHERE User_Name ='" + usernameText + "'";
        ResultSet output = statement.executeQuery(query);
        if (output.next()){
            userId = output.getInt("User_ID");
        }
        return userId;
    }

    /**
     * method returns a list of all user IDs
     * @return
     */
    public static ObservableList<Integer> getListUserIDs() {
        ObservableList<Integer> getAllUserIDs = FXCollections.observableArrayList();
        try{
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT user_id FROM users";
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()){
                Integer userID = resultSet.getInt(1);
                getAllUserIDs.add(userID);
            } return getAllUserIDs;
        } catch (SQLException exception){
            System.out.println(" Getting all user ids SQL Exception " + exception.getMessage());
        } return null;
    }


}
