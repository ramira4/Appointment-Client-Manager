package Model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Appointment database class,
 * includes methods pertaining to data about appointments
 */
public class AppointmentDB {

    /**
     *  get all appointments in database and save to observable List: allAppointments
     */

    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

    /**
     * method returns an observable list of all the appointments in the database and
     * creates new appointment objects
     * @return
     */
    public static ObservableList<Appointments> getAllAppointments() {
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT appointment_id, title, description, location, type, start, end," +
                    " customer_ID, user_ID, contact_id " +
                    "FROM appointments";
            ResultSet resultSet = statement.executeQuery(query);
            allAppointments.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                String location = resultSet.getString(4);
                String type = resultSet.getString(5);
                Timestamp start = resultSet.getTimestamp(6);
                Timestamp end = resultSet.getTimestamp(7);
                int customerID = resultSet.getInt(8);
                int userID = resultSet.getInt(9);
                int contactID = resultSet.getInt(10);
                String contactName = ContactDB.getContactName(contactID);

                Appointments appointments = new Appointments(id, title, description, location, type, contactName, start, end, customerID, userID);
                appointments.setAppointmentID(id);
                appointments.setTitle(title);
                appointments.setDescription(description);
                appointments.setLocation(location);
                appointments.setType(type);
                appointments.setContact(contactName);
                appointments.setStart(start);
                appointments.setEnd(end);
                appointments.setCustomerID(customerID);
                appointments.setUserID(userID);

                allAppointments.add(appointments);
            }
            return allAppointments;
        } catch (SQLException exception) {
            System.out.println("Getting all appointments SQL Exception " + exception.getMessage());
            return null;
        }
    }

    /**
     * Query inserts a new appointment into the database
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param contactID
     */
    public static void addAppointment(String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end, Integer customerID, Integer contactID) {
        try {
            String userName = UserDB.getCurrentUserName();
            Integer userID = UserDB.getCurrentUserID();
            LocalDateTime start1 = start.toLocalDateTime();
            LocalDateTime end1 = end.toLocalDateTime();
            Statement statement = JDBC.connection.createStatement();
            String string = "INSERT INTO appointments (title, description, location, type, start, end," +
                    " create_date, created_by, last_update, last_updated_by, " +
                    "customer_id, user_id, contact_id) "
                    + "VALUES ('" + title + "' , '" + description + "' , '" + location
                    + "' , '" + type + "' , '" + start1 + "' , '" + end1 + "' , NOW(), '" + userName
                    + "' , NOW(), '" + userName + "', " + customerID + ", " + userID + "," + contactID + ")";
            statement.executeUpdate(string);
            System.out.println("Successfully added new appointment");
        } catch (SQLException exception) {
            System.out.println("Saving new appointment SQL Exception " + exception.getMessage());
        }

    }

    /**
     * Query updates an existing appointment in the database
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param contactID
     */
    public static void updateAppointment(Integer id, String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end, Integer customerID, Integer contactID) {
        try {
            String userName = UserDB.getCurrentUserName();
            Integer userID = UserDB.getCurrentUserID();
            LocalDateTime start1 = start.toLocalDateTime();
            LocalDateTime end1 = end.toLocalDateTime();
            Statement statement = JDBC.connection.createStatement();
            String string = " UPDATE appointments " +
                    " SET title ='" + title + "', description='" + description + "', location ='" + location + "', type = '" +
                    type + "', start='" + start1 + "', end ='" + end1 + "', last_update = NOW(), last_updated_by ='" + userName +
                    "', user_id = " + userID + ", contact_id = " + contactID +
                    " WHERE appointment_id =" + id;
            statement.executeUpdate(string);
            System.out.println("Successfully updated appointment with ID = " + id);
        } catch (SQLException exception) {
            System.out.println("Updating appointment SQL Exception " + exception.getMessage());
        }

    }

    /**
     * query deletes an appointment from database
     * @param Id
     */
    public static void deleteAppointment(Integer Id) {
        try {
            Statement statement = JDBC.connection.createStatement();
            String string = "DELETE FROM appointments" +
                    " WHERE appointment_id =" + Id;
            statement.executeUpdate(string);
            System.out.println("Successfully removed appointment with ID = " + Id);
        } catch (SQLException exception) {
            System.out.println("Deleting appointment SQL Exception " + exception.getMessage());
        }
    }

    /**
     * formats date and time data provided by user
     * into SQL acceptable format
     * @param date
     * @param time
     * @return
     */
    public static LocalDateTime getFormat(LocalDate date, String time) {
        String string = date + " " + time + ":00.0";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime dateTime = LocalDateTime.parse(string, formatter);
        System.out.println(dateTime);
        return dateTime;
    }

    /**
     * query returns any appointments starting in the next 15 minutes
      * @return
     */
    public static Appointments getNext15MinAppointments() {
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        LocalDateTime utcNow = now.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

        LocalDateTime utcNowPlus15 = utcNow.plusMinutes(15);
        Appointments appointments = null;
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT appointment_id, title, description, location, type, start, end," +
                    " customer_ID, user_ID, contact_id " +
                    " FROM appointments"+
                    " WHERE start BETWEEN '" + utcNow + "' AND '"+ utcNowPlus15 + "'";
//            System.out.println(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                String location = resultSet.getString(4);
                String type = resultSet.getString(5);
                Timestamp start = resultSet.getTimestamp(6);
                Timestamp end = resultSet.getTimestamp(7);
                int customerID = resultSet.getInt(8);
                int userID = resultSet.getInt(9);
                int contactID = resultSet.getInt(10);
                String contactName = ContactDB.getContactName(contactID);

                appointments = new Appointments(id, title, description, location, type, contactName, start, end, customerID, userID);
                appointments.setAppointmentID(id);
                appointments.setTitle(title);
                appointments.setDescription(description);
                appointments.setLocation(location);
                appointments.setType(type);
                appointments.setContact(contactName);
                appointments.setStart(start);
                appointments.setEnd(end);
                appointments.setCustomerID(customerID);
                appointments.setUserID(userID);

            }
            return appointments;
        } catch (SQLException exception) {
            System.out.println("Getting appointments in the next 15 mins SQL Exception " + exception.getMessage());
            return null;
        }
    }

    /**
     * get a month of appointments in database and save to observable List: monthAppointments
     */
    private static ObservableList<Appointments> monthAppointments = FXCollections.observableArrayList();

    /**
     * method returns a list of all appointments coming up in one months time
     * @return
     */
    public static ObservableList<Appointments> getMonthAppointments() {
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        LocalDateTime utcNow = now.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime utcNowPlus1 = utcNow.plusMonths(1);
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT appointment_id, title, description, location, type, start, end," +
                    " customer_ID, user_ID, contact_id " +
                    "FROM appointments "+
                    " WHERE start BETWEEN '" + utcNow + "' AND '"+ utcNowPlus1 + "'";
            ResultSet resultSet = statement.executeQuery(query);
            monthAppointments.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                String location = resultSet.getString(4);
                String type = resultSet.getString(5);
                Timestamp start = resultSet.getTimestamp(6);
                Timestamp end = resultSet.getTimestamp(7);
                int customerID = resultSet.getInt(8);
                int userID = resultSet.getInt(9);
                int contactID = resultSet.getInt(10);
                String contactName = ContactDB.getContactName(contactID);

                Appointments appointments = new Appointments(id, title, description, location, type, contactName, start, end, customerID, userID);
                appointments.setAppointmentID(id);
                appointments.setTitle(title);
                appointments.setDescription(description);
                appointments.setLocation(location);
                appointments.setType(type);
                appointments.setContact(contactName);
                appointments.setStart(start);
                appointments.setEnd(end);
                appointments.setCustomerID(customerID);
                appointments.setUserID(userID);

                monthAppointments.add(appointments);
            }
            return monthAppointments;
        } catch (SQLException exception) {
            System.out.println("Getting all month appointments SQL Exception " + exception.getMessage());
            return null;
        }
    }

    /**
     * get all in the upcoming week appointments in database and save to observable List: weekAppointments
     */
    private static ObservableList<Appointments> weekAppointments = FXCollections.observableArrayList();

    /**
     * method returns an observable list of all appointments coming u in the next seven days
     * @return
     */
    public static ObservableList<Appointments> getWeekAppointments() {
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        LocalDateTime utcNow = now.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime utcNowPlus7 = utcNow.plusDays(7);
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT appointment_id, title, description, location, type, start, end," +
                    " customer_ID, user_ID, contact_id " +
                    "FROM appointments" +
                    " WHERE start BETWEEN '" + utcNow + "' AND '"+ utcNowPlus7 + "'";
            ResultSet resultSet = statement.executeQuery(query);
            weekAppointments.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                String location = resultSet.getString(4);
                String type = resultSet.getString(5);
                Timestamp start = resultSet.getTimestamp(6);
                Timestamp end = resultSet.getTimestamp(7);
                int customerID = resultSet.getInt(8);
                int userID = resultSet.getInt(9);
                int contactID = resultSet.getInt(10);
                String contactName = ContactDB.getContactName(contactID);

                Appointments appointments = new Appointments(id, title, description, location, type, contactName, start, end, customerID, userID);
                appointments.setAppointmentID(id);
                appointments.setTitle(title);
                appointments.setDescription(description);
                appointments.setLocation(location);
                appointments.setType(type);
                appointments.setContact(contactName);
                appointments.setStart(start);
                appointments.setEnd(end);
                appointments.setCustomerID(customerID);
                appointments.setUserID(userID);

                weekAppointments.add(appointments);
            }
            return weekAppointments;
        } catch (SQLException exception) {
            System.out.println("Getting all week appointments SQL Exception " + exception.getMessage());
            return null;
        }
    }

    /**
     * get all appointments for specific customer in database and save to observable List: allCustomersAppointments
     */
    private static ObservableList<Appointments> allCustomersAppointments = FXCollections.observableArrayList();

    /**
     * method returns an observable list of all appointments belonging to a customer
     * @param customerIDin
     * @return
     */
    public static ObservableList<Appointments> getAllCustomersAppointments(Integer customerIDin) {
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT appointment_id, title, description, location, type, start, end," +
                    " customer_ID, user_ID, contact_id " +
                    "FROM appointments" +
                    " WHERE customer_id =" + customerIDin;
            ResultSet resultSet = statement.executeQuery(query);
            allCustomersAppointments.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                String location = resultSet.getString(4);
                String type = resultSet.getString(5);
                Timestamp start = resultSet.getTimestamp(6);
                Timestamp end = resultSet.getTimestamp(7);
                int customerID = resultSet.getInt(8);
                int userID = resultSet.getInt(9);
                int contactID = resultSet.getInt(10);
                String contactName = ContactDB.getContactName(contactID);

                Appointments appointments = new Appointments(id, title, description, location, type, contactName, start, end, customerID, userID);
                appointments.setAppointmentID(id);
                appointments.setTitle(title);
                appointments.setDescription(description);
                appointments.setLocation(location);
                appointments.setType(type);
                appointments.setContact(contactName);
                appointments.setStart(start);
                appointments.setEnd(end);
                appointments.setCustomerID(customerID);
                appointments.setUserID(userID);

                allCustomersAppointments.add(appointments);
            }
            return allCustomersAppointments;
        } catch (SQLException exception) {
            System.out.println("Getting all customers appointments SQL Exception " + exception.getMessage());
            return null;
        }
    }

    /**
     * query returns how many appointments of each type exist in the upcoming month,
     * the writeTypesReport method is called to store info in a txt file
     * @throws Exception
     */
    public static void countTypesPerMonth() throws Exception {

        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        LocalDateTime utcNow = now.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime utcNowPlusMonth = utcNow.plusMonths(1);
        try {
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT type, count(*) AS 'count'" +
                    " FROM appointments" +
                    " WHERE start BETWEEN '" + utcNow + "' AND '" + utcNowPlusMonth +"'" +
                    " GROUP BY type";
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()) {
                String typeAppointment = resultSet.getString(1);
                Integer countType = resultSet.getInt(2);
                writeTypesReport(typeAppointment, countType);
//                System.out.println(typeAppointment);
//                System.out.println(countType);
            }
            System.out.println("Successfully retrieved types report");

        } catch (SQLException Exception) {
            System.out.println("Types report SQL Exception " + Exception.getMessage());
        }
    }

    /**
     * format and write the types report to file 'countTypes.txt'
     * @param type
     * @param count
     */
    public static void writeTypesReport(String type, Integer count){
        String resultStatement;
        resultStatement = " || There exists " + count + " appointment/s of the type '" +
                type + "' ||";
        try(
        FileWriter fileWriter = new FileWriter("countTypes.txt", true);
        PrintWriter printWriter = new PrintWriter(fileWriter)){
            printWriter.println(resultStatement);
        }catch (Exception exception){
            System.out.println("Writing types report error" + exception.getMessage());
        }

    }

    /**
     * query returns all appointments for a contact
     * and calls writeSchedules method to write report to a .txt file
     * @param contact
     */
    public static void getSchedule(String contact){
        Integer id = ContactDB.getContactID(contact);
        try {
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT appointment_id, title, type, description, start, end, customer_id " +
                    " FROM appointments" +
                    " WHERE contact_id =" + id;
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()) {
                Integer appointmentID= resultSet.getInt(1);
                String title = resultSet.getString(2);
                String type = resultSet.getString(3);
                String description = resultSet.getString(4);
                String start = resultSet.getTimestamp(5).toString();
                String end = resultSet.getTimestamp(6).toString();
                Integer customerID = resultSet.getInt(7);
                writeSchedules(appointmentID, title, type, description,start, end, customerID);

            }
            System.out.println("Successfully retrieved schedules report");

        } catch (SQLException Exception) {
            System.out.println("Schedules report SQL Exception " + Exception.getMessage());
        }
    }

    /**
     * method formats and write a schedule report to 'schedules.txt'
     * @param appointmentID
     * @param title
     * @param type
     * @param description
     * @param start
     * @param end
     * @param customerID
     */
    public static void writeSchedules(Integer appointmentID, String title, String type, String description,
                    String start, String end, Integer customerID){
        String resultStatement;
        resultStatement = "|| APPOINTMENT ID : " +appointmentID + " TITLE: " + title
                + " TYPE: " + type + " DESCRIPTION: " + description + " START: "
                +start + " END: " + end + " CUSTOMER ID: " + customerID + "   || ";
        try(
                FileWriter fileWriter = new FileWriter("schedules.txt", true);
                PrintWriter printWriter = new PrintWriter(fileWriter)){
            printWriter.println(resultStatement);
        }catch (Exception exception){
            System.out.println("Writing schedules report error" + exception.getMessage());
        }
    }

    /**
     * query returns the count of appointments for a customer in the upcoming month
     * and method calls the writeAppointmentCount method to store report in a .txt file
     * @param customerName
     * @throws SQLException
     */
    public static void getAppointmentCount(String customerName) throws  SQLException{
        Integer customerID = CustomerDB.getCustomerID(customerName);
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        LocalDateTime utcNow = now.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime utcNowPlusMonth = utcNow.plusMonths(1);
        try {
            Statement statement = JDBC.connection.createStatement();
            String string = "SELECT appointment_id, count(*) as 'count' " +
                    " FROM appointments" +
                    " WHERE customer_id =" + customerID +
                    " AND start BETWEEN '" + utcNow + "' AND '" + utcNowPlusMonth +"'" ;
//            System.out.println(string);
            ResultSet resultSet = statement.executeQuery(string);
            while (resultSet.next()) {
                Integer count = resultSet.getInt(2);
                writeAppointmentCount(customerName, count, utcNowPlusMonth);

            }
            System.out.println("Successfully retrieved Appointment Count report");

        } catch (SQLException Exception) {
            System.out.println("Appointment Count report SQL Exception " + Exception.getMessage());
        }
    }

    /**
     * writes and formats appointment count report to 'appCount.txt'
     * @param CustomerName
     * @param count
     * @param monthOut
     */
    public static void writeAppointmentCount(String CustomerName, Integer count, LocalDateTime monthOut){
        //ZonedDateTime monthOut1= monthOut.atZone(ZoneId.systemDefault());
        String resultStatement;
        resultStatement = CustomerName + " has " + count + " appointment/s coming up between now and one month's time (" +
                  monthOut +").";
        try(
                FileWriter fileWriter = new FileWriter("appCount.txt", true);
                PrintWriter printWriter = new PrintWriter(fileWriter)){
            printWriter.println(resultStatement);
        }catch (Exception exception){
            System.out.println("Writing Appointment Count report error" + exception.getMessage());
        }
    }

    /**
     * get all  overlapping appointments
      */
    private static ObservableList<Appointments> allOverlappingCustomersAppointments = FXCollections.observableArrayList();

    /**
     * query will return appointments if any existing time slots overlap with the specified time slot for a
     * new appointment
     * @param contactId
     * @param tryStart
     * @param tryEnd
     * @param appointmentID
     * @return
     */
    public static ObservableList<Appointments> getAllOverlappingCustomersAppointments
            (Integer contactId, ZonedDateTime tryStart, ZonedDateTime tryEnd, Integer appointmentID) {
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT appointment_id, title, description, location, type, start, end," +
                    " customer_ID, user_ID, contact_id " +
                    " FROM appointments" +
                    " WHERE ( ( start BETWEEN '"
                    + tryStart +"' AND '" + tryEnd + "') OR ( end BETWEEN '" +
                    tryStart + "' AND '" + tryEnd + "') OR (start < '" + tryStart +
                    "' AND '" + tryEnd + "' < end) ) AND ( appointment_id != "
                    + appointmentID +")";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println(query);
            allOverlappingCustomersAppointments.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                String location = resultSet.getString(4);
                String type = resultSet.getString(5);
                Timestamp start = resultSet.getTimestamp(6);
                Timestamp end = resultSet.getTimestamp(7);
                int customerID = resultSet.getInt(8);
                int userID = resultSet.getInt(9);
                int contactID = resultSet.getInt(10);
                String contactName = ContactDB.getContactName(contactID);

                Appointments appointments = new Appointments(id, title, description, location, type, contactName, start, end, customerID, userID);
                appointments.setAppointmentID(id);
                appointments.setTitle(title);
                appointments.setDescription(description);
                appointments.setLocation(location);
                appointments.setType(type);
                appointments.setContact(contactName);
                appointments.setStart(start);
                appointments.setEnd(end);
                appointments.setCustomerID(customerID);
                appointments.setUserID(userID);

                allOverlappingCustomersAppointments.add(appointments);
            }
            return allOverlappingCustomersAppointments;
        } catch (SQLException exception) {
            System.out.println("Getting all overlapping appointments SQL Exception " + exception.getMessage());
            return null;
        }
    }


}



