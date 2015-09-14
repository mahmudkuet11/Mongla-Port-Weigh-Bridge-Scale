/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongla.port;

/**
 *
 * @author hp
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
*
* @author mohar
*/
public class DataBase {
    Connection c = null;
    Statement stmt;
    public Connection getConnection() throws SQLException, ClassNotFoundException{
        Connection con = null;
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:src\\mongla.sqlite");
        return con;
    }
}
