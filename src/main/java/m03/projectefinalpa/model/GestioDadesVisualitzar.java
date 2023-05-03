/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

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
                EmpleadosClass empleado = null;

               // si el nombre es diferente e null, se crea el objeto empleado.
                if (nombreEmpleado != null) {
                    empleado = new EmpleadosClass(idEmpleado, nombreEmpleado, email);
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

    //llista tots els horaris de les atraccions, i dins la classe horaris tenim un arrayList d'empleats que anem afegint tots els empleats que fan aquell horari. 
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
                EmpleadosClass empleado = null;
                // si l'empleat no retorna null volem guardar les seves dades creant una instancia d'empleant.
                if (nombreEmpleado != null) {
                    empleado = new EmpleadosClass(idEmpleado, nombreEmpleado, email);
                }
                // guardem les dades de l'horari dins variables
                identificador = resultSet.getInt(1);
                LocalDateTime fechaInicio = resultSet.getTimestamp(2).toLocalDateTime();
                LocalDateTime fechaFin = resultSet.getTimestamp(3).toLocalDateTime();
                
                //crem un objecte horari amb les dades donades
                Horari horario = new Horari(identificador, fechaInicio, fechaFin);

                // comprobem si l'hhorari ja existeix dins la llista horaris, per no tornar a repeteir el mateix horari
                if (!horariosList.contains(horario)) {

                    // si hi ha un empleat associat a n'aquest horari, el guardem amb el mètode añadirEmpleado, que l'afegeix en cas de que no estigui afegit prèviament. 
                    if (empleado != null) {
                        horario.añadirEmpleado(empleado);
                    }
                    // una vegada associat l'horari i l'empleat, ho afegim a la llisya de horariosList
                    horariosList.add(horario);
                    contador++;

                } else {
                    // si l'horari ja existeix, retornem l'horari de la llista i si l'empleat no es null, l'afegim a l'arrayllist d'empleados de l'objecte Horari.
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
