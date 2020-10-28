/*
Example
 */

public interface DatabaseTable {
    String DATABASE = "database.db";

    interface UserTable {
        String USER_TABLE = "User";

        String COLUMN_USER_ID = "UserID";
        String COLUMN_NICKNAME = "Nickname";
        String COLUMN_PASSWORT = "Passwort";
        String COLUMN_ISADMIN = "isAdmin";
        String[] userAllColumns = new String[]{
                COLUMN_USER_ID, COLUMN_NICKNAME, COLUMN_PASSWORT, COLUMN_ISADMIN
        };
    }

}
