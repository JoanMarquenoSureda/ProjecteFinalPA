/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import m03.projectefinalpa.model.classes.Horari;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import m03.projectefinalpa.model.classes.EmpleadosClass;
import m03.projectefinalpa.model.classes.ZonaTrabajo;


/**
 *
 * @author joanm
 */
public class GestioDadesEmpleados {

    public ObservableList<Horari> horariPerEmpleat(String nombre) {
        ObservableList<Horari> horaris = FXCollections.observableArrayList();

        String sql = "SELECT horario.fecha_inicio, horario.fecha_fin,\n"
                + "  IFNULL(restaurante.nombre, atraccion.nombre) AS nombre,\n"
                + "  IFNULL(restaurante.ubicacion, atraccion.ubicacion) AS ubicacion\n"
                + "FROM empleado\n"
                + "INNER JOIN asignacion ON asignacion.idEmpleado = empleado.id\n"
                + "INNER JOIN horario ON asignacion.idHorario = horario.id AND horario.fecha_inicio >= CURRENT_DATE()\n"
                + "LEFT JOIN restaurante ON restaurante.id = horario.idRestaurante\n"
                + "LEFT JOIN atraccion ON atraccion.id = horario.idAtraccion\n"
                + "WHERE empleado.nombre = ? \n"
                + "ORDER BY horario.fecha_inicio ASC;";

        Connection connection = new Connexio().connecta();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                horaris.add(new Horari(
                        resultSet.getTimestamp(1).toLocalDateTime(),
                        resultSet.getTimestamp(2).toLocalDateTime(),
                        new ZonaTrabajo(resultSet.getString(3), resultSet.getString(4)))
                
                );

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return horaris;
    }

   public EmpleadosClass dadesEmpleat(String nombre) {

    EmpleadosClass empleado = null;

    String sql = "SELECT empleado.nombre, empleado.direccion, empleado.telefono, empleado.email, empleado.foto\n"
            + "FROM empleado \n"
            + "WHERE empleado.nombre = ?;";

    Connection connection = new Connexio().connecta();

    try {

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, nombre);

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
