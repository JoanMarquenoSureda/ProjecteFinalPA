/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import m03.projectefinalpa.model.classes.Horari;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import m03.projectefinalpa.model.classes.EmpleadosClass;
import m03.projectefinalpa.model.classes.ZonaTrabajo;

/**
 *
 * @author joanm
 */
public class GestioDadesEmpleados {

    // mètode que retorna les dades de l'horari i el nom més l'ubicació del restaurant o atracció, segons el nom de l'empleat cercat, ordenat els horaris de forma
    //accendent i desde el día d'avui, obviants els horaris anteriors. 
    public ObservableList<Horari> horariPerEmpleat(String dni) {
        ObservableList<Horari> horaris = FXCollections.observableArrayList();

        String sql = "SELECT horario.fecha_inicio, horario.fecha_fin,\n"
                + "  IFNULL(restaurante.nombre, atraccion.nombre) AS nombre,\n"
                + "  IFNULL(restaurante.ubicacion, atraccion.ubicacion) AS ubicacion\n"
                + "FROM empleado\n"
                + "INNER JOIN asignacion ON asignacion.idEmpleado = empleado.id\n"
                + "INNER JOIN horario ON asignacion.idHorario = horario.id AND horario.fecha_inicio >= CURRENT_DATE()\n"
                + "LEFT JOIN restaurante ON restaurante.id = horario.idRestaurante\n"
                + "LEFT JOIN atraccion ON atraccion.id = horario.idAtraccion\n"
                + "WHERE empleado.dni = ? \n"
                + "ORDER BY horario.fecha_inicio ASC;";

        Connection connection = new Connexio().connecta();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                horaris.add(new Horari(
                        resultSet.getTimestamp(1).toLocalDateTime(), //convertim el DateTime de MySQL en un LocalDateTime, 
                        resultSet.getTimestamp(2).toLocalDateTime(),
                        new ZonaTrabajo(resultSet.getString(3), resultSet.getString(4)))
                );

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return horaris;
    }

    //mètode que retorna les dades d'un empleat segons el nom de l'empleat passat per paràmetres, només retorna un objecte ja que passem per paràmetres el =. 
    public EmpleadosClass dadesEmpleat(String dni) {

        EmpleadosClass empleado = null;

        String sql = "SELECT empleado.nombre, empleado.direccion, empleado.telefono, empleado.email, empleado.foto\n"
                + "FROM empleado \n"
                + "WHERE empleado.dni = ?;";

        Connection connection = new Connexio().connecta();

        try {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, dni);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                empleado = new EmpleadosClass(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getBlob(5)
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return empleado;
    }
}
