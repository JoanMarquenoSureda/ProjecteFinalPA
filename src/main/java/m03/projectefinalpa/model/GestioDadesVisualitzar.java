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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    
    public ObservableList<Horari> llistaHorarisRestaurants(int id, LocalDateTime fechaEntrada, LocalDateTime fechasalida) {
        int contador = 0;
        int identificador;

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        ArrayList<Horari> horariosList = new ArrayList<>(); // ArrayList para almacenar los horarios
        
        //consulta que devuelve todos los horarios y el nombre de los empleados assignados, si no hay assignacion devuelve null en el campo nombre
        String sql = "SELECT horario.id, horario.fecha_inicio, horario.fecha_fin, empleado.id, empleado.nombre, empleado.email \n"
                + "from horario\n"
                + "left join asignacion on asignacion.idHorario = horario.id\n"
                + "left join empleado on asignacion.idEmpleado = empleado.id\n"
                + "where horario.idRestaurante = ? and horario.fecha_inicio >= ? AND horario.fecha_fin <= ?\n"
                + "order by horario.fecha_inicio asc;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaEntradaSQL = fechaEntrada.toLocalDate();
            LocalDate fechaSalidaSQL = fechasalida.toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(fechaEntradaSQL));
            statement.setDate(3, Date.valueOf(fechaSalidaSQL));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int idEmpleado = resultSet.getInt(4);
                String nombreEmpleado = resultSet.getString(5);
                String email = resultSet.getString(6);
                Empleados empleado = null;

               // si el nombre es diferente e null, se crea el objeto empleado.
                if (nombreEmpleado != null) {
                    empleado = new Empleados(idEmpleado, nombreEmpleado, email);
                }
                identificador = resultSet.getInt(1);
                LocalDateTime fechaInicio = resultSet.getTimestamp(2).toLocalDateTime();
                LocalDateTime fechaFin = resultSet.getTimestamp(3).toLocalDateTime();
                Horari horario = new Horari(identificador, fechaInicio, fechaFin);

                // Comprobar si el horario ya existe en la lista
                if (!horariosList.contains(horario)) {

                    if (empleado != null) {
                        horario.añadirEmpleado(empleado);
                    }
                    horariosList.add(horario);
                    contador++;

                } else {

                    Horari horari = horariosList.get(contador - 1);
                    if (empleado != null) {
                        horari.añadirEmpleado(empleado);
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Añadir los horarios de la lista a la lista de horarios
        horaris.addAll(horariosList);

        return horaris;
    }

    public ObservableList<Horari> llistaHorarisAtraccions(int id, LocalDateTime fechaEntrada, LocalDateTime fechasalida) {
        int contador = 0;
        int identificador;

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        ArrayList<Horari> horariosList = new ArrayList<>(); // ArrayList para almacenar los horarios
        String sql = "SELECT horario.id, horario.fecha_inicio, horario.fecha_fin, empleado.id, empleado.nombre, empleado.email \n"
                + "from horario\n"
                + "left join asignacion on asignacion.idHorario = horario.id\n"
                + "left join empleado on asignacion.idEmpleado = empleado.id\n"
                + "where horario.idAtraccion = ? and horario.fecha_inicio >= ? AND horario.fecha_fin <= ?\n"
                + "order by horario.fecha_inicio asc;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaEntradaSQL = fechaEntrada.toLocalDate();
            LocalDate fechaSalidaSQL = fechasalida.toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(fechaEntradaSQL));
            statement.setDate(3, Date.valueOf(fechaSalidaSQL));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int idEmpleado = resultSet.getInt(4);
                String nombreEmpleado = resultSet.getString(5);
                String email = resultSet.getString(6);
                Empleados empleado = null;

                if (nombreEmpleado != null) {
                    empleado = new Empleados(idEmpleado, nombreEmpleado, email);
                }
                identificador = resultSet.getInt(1);
                LocalDateTime fechaInicio = resultSet.getTimestamp(2).toLocalDateTime();
                LocalDateTime fechaFin = resultSet.getTimestamp(3).toLocalDateTime();
                Horari horario = new Horari(identificador, fechaInicio, fechaFin);

                // Comprobar si el horario ya existe en la lista
                if (!horariosList.contains(horario)) {

                    if (empleado != null) {
                        horario.añadirEmpleado(empleado);
                    }
                    horariosList.add(horario);
                    contador++;

                } else {

                    Horari horari = horariosList.get(contador - 1);
                    if (empleado != null) {
                        horari.añadirEmpleado(empleado);
                    }
                }

            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Añadir los horarios de la lista a la lista de horarios
        horaris.addAll(horariosList);

        return horaris;
    }
}
