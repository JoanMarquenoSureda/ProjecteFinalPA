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

    private final String URL = "jdbc:mysql://127.0.0.1:3307/bd_portaventura";// nom bd
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private String user = "Administrador";
    private String password = "Administrador";

    public Connexio(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public Connexio() {
    }

    public Connection connecta() {

        Connection connexio = null;
        try {
            // Carreguem el driver
            Class.forName(DRIVER);
            connexio = DriverManager.getConnection(URL, user, password);
        } catch (SQLException | ClassNotFoundException throwables) {
            System.out.println(throwables.getLocalizedMessage());
        }
        return connexio;
    }
}
