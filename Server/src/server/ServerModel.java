package server;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import dataclass.UserStatus;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phawzy
 */
public class ServerModel {

    final String dbName = "chat";
    final String usersTable = "users";
    final String dbUsername = "root";
    final String dbPassword = "111111";
    Connection con = null;
    ServerController serverControllerObj;

    public ServerModel(ServerController controller) {
        try {
            serverControllerObj = controller;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = (Connection) DriverManager.getConnection("jdbc:mysql:"
                    + "//localhost/" + dbName + "?" + "user=" + dbUsername + "&password=" + dbPassword + "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean authenticateUserLogin(String username, String password) {

        Statement stmt;
        ResultSet rs = null;

        try {
            stmt = (Statement) con.createStatement();
            String qs = "select * from users where username='" + username + "' AND password='" + password + "'";
            rs = stmt.executeQuery(qs);
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("authenticateUserLogin exception");
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    boolean authenticateUsernameRegister(String username) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = (Statement) con.createStatement();
            String qs = "select * from  users where username='" + username + "'";
            rs = stmt.executeQuery(qs);
        } catch (SQLException ex) {
            System.out.println("authenticateUsernameRegister exception");
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (rs.next()) {
                rs.close();
                return false;
            } else {
                rs.close();
                return true;
            }
        } catch (SQLException ex) {
             System.out.println("authenticateUsernameRegister exception::2");
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }


    UserStatus[] getAllUsernamesStatus() {
        Statement stmt;
        int length = getNumOfUsers();
        System.out.println(length);
        int index = 0;
        UserStatus[] usernames = new UserStatus[length];
        try {
            stmt = (Statement) con.createStatement();
            String qs = "select username,status from  users";
            ResultSet rs = stmt.executeQuery(qs);
            try {
                while (rs.next()) {
                    System.out.println(rs.getString("username"));
                    usernames[index]=new UserStatus();
                    usernames[index].username
                            = (String)rs.getString("username");
                    usernames[index].status
                            = (String)rs.getString("status");
                    index++;
                }
                if (index == length) {
                    System.out.println("allclientsretreived");
                }
                rs.close();
                return usernames;
            } catch (SQLException ex) {
                System.out.println("the first ececption");
                Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            System.out.println("the second ececption");
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return null;
    }

    boolean addNewUser(String name, String username, String password,
            String email, String gender) {
        Statement stmt;
        try {
            stmt = (Statement) con.createStatement();
            String qs;
            qs = "insert into users (name,username,password,email,gender,status) "
                    + "VALUES('" + name + "','" + username + "','" + password + "','"
                    + email + "','" + gender + "','offline')";
            stmt.executeUpdate(qs);
            return true;
        } catch (SQLException ex) {
             System.out.println("addNewUser exception");
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    void changeStatus(String username,String status) {
        Statement stmt;
        try {
            stmt = (Statement) con.createStatement();
            String qs;
            qs = "Update users set status='" + status + "' where username='" + username + "'";
            stmt.executeUpdate(qs);
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int getNumOfUsers() {
        String qs = null;
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = (Statement) con.createStatement();
            qs = "Select count(*)from users";
            rs = stmt.executeQuery(qs);
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numOfUsers = 0;
        try {
            if (rs.next()) {
                numOfUsers = rs.getInt("COUNT(*)");
                System.out.println("total num is"+numOfUsers);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numOfUsers;
    }

}
