/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import m03.projectefinalpa.model.classes.Restaurant;
import m03.projectefinalpa.model.classes.Atraccio;
import m03.projectefinalpa.model.classes.Horari;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import m03.projectefinalpa.model.classes.Empleados;
import m03.projectefinalpa.model.classes.ZonaTrabajo;

/**
 *
 * @author joanm
 */
public class GestioDadesVisualitzar {

    public ObservableList<Horari> llistaHorarisRestaurants(int id, Timestamp fechaEntrada, Timestamp fechasalida) {

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        String sql = "SELECT horario.fecha_inicio, horario.fecha_fin, empleado.nombre \n"
                + "from horario\n"
                + "left join asignacion on asignacion.idHorario = horario.id\n"
                + "left join empleado on asignacion.idEmpleado = empleado.id\n"
                + "where horario.idRestaurante = ? and horario.fecha_inicio >= ? AND horario.fecha_fin <= ?\n"
                + "order by horario.fecha_inicio asc;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaEntradaSQL = fechaEntrada.toLocalDateTime().toLocalDate();
            LocalDate fechaSalidaSQL = fechasalida.toLocalDateTime().toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(fechaEntradaSQL));
            statement.setDate(3, Date.valueOf(fechaSalidaSQL.plusDays(1)));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                
                String nombreEmpleado = resultSet.getString(3) != null ? resultSet.getString(3) : "Sin asignacion";
                horaris.add(
                        new Horari(
                                resultSet.getTimestamp(1).toLocalDateTime(),
                                resultSet.getTimestamp(2).toLocalDateTime(),
                                new Empleados(nombreEmpleado)
                        )
                );
            }
            connection.close();

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return horaris;
    }

    public ObservableList<Horari> llistaHorarisAtraccions(int id, Timestamp fechaEntrada, Timestamp fechasalida) {

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        String sql = "SELECT horario.fecha_inicio, horario.fecha_fin, empleado.nombre \n"
                + "from horario\n"
                + "left join asignacion on asignacion.idHorario = horario.id\n"
                + "left join empleado on asignacion.idEmpleado = empleado.id\n"
                + "where horario.idAtraccion = ? and horario.fecha_inicio >= ? AND horario.fecha_fin <= ?\n"
                + "order by horario.fecha_inicio asc;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaEntradaSQL = fechaEntrada.toLocalDateTime().toLocalDate();
            LocalDate fechaSalidaSQL = fechasalida.toLocalDateTime().toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(fechaEntradaSQL));
            statement.setDate(3, Date.valueOf(fechaSalidaSQL.plusDays(1)));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombreEmpleado = resultSet.getString(3) != null ? resultSet.getString(3) : "Sin asignacion";
                horaris.add(
                        new Horari(
                                resultSet.getTimestamp(1).toLocalDateTime(),
                                resultSet.getTimestamp(2).toLocalDateTime(),
                                new Empleados(nombreEmpleado)
                        )
                );
            }
            connection.close();

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return horaris;
    }

}
