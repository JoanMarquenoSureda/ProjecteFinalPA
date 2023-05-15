/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import m03.projectefinalpa.model.classes.Horari;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import m03.projectefinalpa.model.classes.EmpleadosClass;


/**
 *
 * @author joanm
 */


public class GestioDadesVisualitzar {
    
    //listamos todos los horarios de un restaurante durante un periodo de tiempo entre dos fechas pasadas. 
    
    public ObservableList<Horari> llistaHorarisRestaurants(String nombreRes, LocalDateTime fechaEntrada, LocalDateTime fechasalida) {
        int contador = 0;
        int identificador;

        ObservableList<Horari> horariosList = FXCollections.observableArrayList();
        
        
        //consulta que devuelve todos los horarios y el nombre de los empleados assignados, si no hay assignacion devuelve null en el campo nombre
        String sql = "SELECT horario.id, horario.fecha_inicio, horario.fecha_fin, empleado.dni, empleado.nombre, empleado.email \n"
                + "from horario\n"
                + "left join asignacion on asignacion.idHorario = horario.id\n"
                + "left join empleado on asignacion.dniEmpleado = empleado.dni\n"
                + "where horario.nombreRes = ? and horario.fecha_inicio >= ? AND horario.fecha_fin <= ?\n"
                + "order by horario.fecha_inicio asc;";

        Connection connection = new Connexio().connecta();
        try {
            
            //convertimos las fechas a LocalDate
            LocalDate fechaEntradaSQL = fechaEntrada.toLocalDate();
            LocalDate fechaSalidaSQL = fechasalida.toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nombreRes);
            statement.setDate(2, Date.valueOf(fechaEntradaSQL));
            statement.setDate(3, Date.valueOf(fechaSalidaSQL));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //revisamos primero los empleados y lo creamos en caso de que el nombre no sea null
                String dniEmpleado = resultSet.getString(4);
                String nombreEmpleado = resultSet.getString(5);
                String email = resultSet.getString(6);
                EmpleadosClass empleado = null;

               // si el nombre es diferente e null, se crea el objeto empleado.
                if (nombreEmpleado != null) {
                    empleado = new EmpleadosClass(dniEmpleado, nombreEmpleado, email);
                }
                //ahora leemos el horario de entrada y salida, y lo convertimos a un LocalDateTime de la clase java
                identificador = resultSet.getInt(1);
                LocalDateTime fechaInicio = resultSet.getTimestamp(2).toLocalDateTime();
                LocalDateTime fechaFin = resultSet.getTimestamp(3).toLocalDateTime();
                //Creamos un horario.
                Horari horario = new Horari(identificador, fechaInicio, fechaFin);

                // Comprobar si el horario ya existe en la lista mediante el metodo contains para que no se duplique la entrada. 
                if (!horariosList.contains(horario)) {
                    //si el horario no existe revisamos que el empleado no sea null para añadirlo en un nuevo array de empleados dentro del horario
                    if (empleado != null) {
                        horario.añadirEmpleado(empleado);
                    }
                    // añadimos el horario con el nuevo array de empleados dentro la lista horariosList
                    horariosList.add(horario);
                    //incrementamos el valor del contador para controlar cuantos elementos hay en la lista de horarios
                    contador++;

                } else {
                    
                    
                    // si  existe el horario, vamos a la ultima posicion de la lista mediante el contador y extraemos ese horari. 
                    Horari horari = horariosList.get(contador - 1);
                    
                    //volvemos a comprobar que el empleado no sea null para luego poder añadir el empleado con el metodo añadirEmpleado que comprueba que
                    //no se duplique el empleado en caso de que sea asi (No debería ya que solo se asocia un usuario unico por horario)
                    if (empleado != null) {
                        horari.añadirEmpleado(empleado);
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return horariosList;
    }

    //llista tots els horaris de les atraccions, i dins la classe horaris tenim un arrayList d'empleats que anem afegint tots els empleats que fan aquell horari. 
    public ObservableList<Horari> llistaHorarisAtraccions(String nombreAtr, LocalDateTime fechaEntrada, LocalDateTime fechasalida) {
        int contador = 0;
        int identificador;

        ObservableList<Horari> horariosList = FXCollections.observableArrayList();
       
        String sql = "SELECT horario.id, horario.fecha_inicio, horario.fecha_fin, empleado.dni, empleado.nombre, empleado.email \n"
                + "from horario\n"
                + "left join asignacion on asignacion.idHorario = horario.id\n"
                + "left join empleado on asignacion.dniEmpleado = empleado.dni\n"
                + "where horario.nombreAtr = ? and horario.fecha_inicio >= ? AND horario.fecha_fin <= ?\n"
                + "order by horario.fecha_inicio asc;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaEntradaSQL = fechaEntrada.toLocalDate();
            LocalDate fechaSalidaSQL = fechasalida.toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nombreAtr);
            statement.setDate(2, Date.valueOf(fechaEntradaSQL));
            statement.setDate(3, Date.valueOf(fechaSalidaSQL));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String dniEmpleado = resultSet.getString(4);
                String nombreEmpleado = resultSet.getString(5);
                String email = resultSet.getString(6);
                EmpleadosClass empleado = null;
                // si l'empleat no retorna null volem guardar les seves dades creant una instancia d'empleant.
                if (nombreEmpleado != null) {
                    empleado = new EmpleadosClass(dniEmpleado, nombreEmpleado, email);
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

    
        return horariosList;
    }
    
     // mètode per esborrar un horari amb un delet, passant per paramatre l'objecte
    // Horari que té el id assignat.
    public boolean esborraHorari(Horari horari) throws SQLException, FileNotFoundException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();
        String sql = "DELETE FROM horario WHERE id = ?";
        PreparedStatement ordre = connection.prepareStatement(sql);
        try {
            ordre.setInt(1, horari.getId());
            ordre.executeUpdate();
            ok = true;

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }

        return ok;
    }
}
