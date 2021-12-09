package Model;

/**
 * User class
 */
public class User {

    private static String username;

    /**
     * Constructor
     */
    public User() {

        username = null;
    }

    /**
     * GETTER AND SETTER
     * @param username
     */
    public User( String username) {

        this.username = username;

    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }


}
