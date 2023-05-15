/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import m03.projectefinalpa.model.classes.Horari;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import m03.projectefinalpa.model.classes.EmpleadosClass;
import m03.projectefinalpa.model.classes.ZonaTrabajo;

/**
 *
 * @author joanm
 */
public class GestioDadesEmpleados {

    // mètode que retorna les dades de l'horari i el nom més l'ubicació del restaurant o atracció, segons el dni de l'empleat cercat, ordenat els horaris de forma
    //accendent i desde el día d'avui, obviants els horaris anteriors. 
    public ObservableList<Horari> horariPerEmpleat(String dni) {
        ObservableList<Horari> horaris = FXCollections.observableArrayList();

       String sql = "SELECT horario.id, horario.fecha_inicio, horario.fecha_fin,\n"
                + "  IFNULL(restaurante.nombre, atraccion.nombre) AS nombre,\n"
                + "  IFNULL(restaurante.ubicacion, atraccion.ubicacion) AS ubicacion\n"
                + "FROM empleado\n"
                + "INNER JOIN asignacion ON asignacion.dniEmpleado = empleado.dni\n"
                + "INNER JOIN horario ON asignacion.idHorario = horario.id AND horario.fecha_inicio >= CURRENT_DATE()\n"
                + "LEFT JOIN restaurante ON restaurante.nombre = horario.nombreRes\n"
                + "LEFT JOIN atraccion ON atraccion.nombre = horario.nombreAtr\n"
                + "WHERE empleado.dni = ? \n"
                + "ORDER BY horario.fecha_inicio ASC;";

        Connection connection = new Connexio().connecta();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                horaris.add(new Horari(
                        resultSet.getInt(1),
                        resultSet.getTimestamp(2).toLocalDateTime(), //convertim el DateTime de MySQL en un LocalDateTime, 
                        resultSet.getTimestamp(3).toLocalDateTime(),
                        new ZonaTrabajo(resultSet.getString(4), resultSet.getString(5)))
                );

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return horaris;
    }

    //mètode que retorna les dades d'un empleat segons el dni de l'empleat passat per paràmetres
    public EmpleadosClass dadesEmpleat(String dni) {

        EmpleadosClass empleado = null;

        String sql = "SELECT empleado.dni, empleado.nombre, empleado.direccion, empleado.telefono, empleado.email, empleado.foto\n"
                + "FROM empleado \n"
                + "WHERE empleado.dni = ?;";

        Connection connection = new Connexio().connecta();

        try {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, dni);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Blob blob = resultSet.getBlob(6);
                Image image = null;

                if (blob != null) {
                    InputStream is = blob.getBinaryStream();
                    image = new Image(is);
                }
                
                empleado = new EmpleadosClass(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        image
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return empleado;
    }

    public boolean eliminarAsociacionEmpleado(Horari horario, EmpleadosClass empleado) throws SQLException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();

        String sql = "DELETE FROM asignacion WHERE idHorario =? and dniEmpleado =?";

        PreparedStatement ps = connection.prepareStatement(sql);

        try {
            ps.setInt(1, horario.getId());
            ps.setString(2, empleado.getDni());

           int filasEliminadas = ps.executeUpdate();
        if (filasEliminadas > 0) {
            ok = true;
        }

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }

        return ok;
    }
}
