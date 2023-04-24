/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

/**
 *
 * @author joanm
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexio {

    private final String URL = "jdbc:mysql://127.0.0.1:3306/bd_portaventura";//nom bd
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String USER = "Administrador";
    private final String PASSWD = "Administrador";   
   

    public Connection connecta() {
        Connection connexio = null;
        try {
            //Carreguem el driver          
            Class.forName(DRIVER); 
            connexio = DriverManager.getConnection(URL, USER, PASSWD); 
        } catch (SQLException | ClassNotFoundException throwables) {
            System.out.println(throwables.getLocalizedMessage());
        }    
        return connexio;
    }
}
