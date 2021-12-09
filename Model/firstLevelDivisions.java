package Model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * First level divisions class
 */
public class firstLevelDivisions {
    private int divisionID;
    private String division;

    private int countryID;

    /**
     * first level divisions constructor
     * @param divisionID
     * @param division
     * @param countryID
     */
    public firstLevelDivisions(int divisionID, String division, int countryID){
        this.divisionID = divisionID;
        this.division = division;

        this.countryID = countryID;
    }

    /**
     * GETTERS AND SETTERS
     * @return
     */
    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}
