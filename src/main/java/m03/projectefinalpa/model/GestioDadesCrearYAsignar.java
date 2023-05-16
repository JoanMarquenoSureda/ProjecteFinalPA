/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import m03.projectefinalpa.model.classes.Restaurant;
import m03.projectefinalpa.model.classes.Atraccio;
import m03.projectefinalpa.model.classes.Horari;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import m03.projectefinalpa.model.classes.EmpleadosClass;

/**
 *
 * @author joanm
 */
public class GestioDadesCrearYAsignar {

    // retorna una llista d'horaris amb el nom del restaurant i la seva ubicació,
    // ordenat de forma ascendent per horari. Es busca lhorari entre dues dates
    // concretes enviades
    // per paràmetre.
    public ObservableList<Horari> llistaHorarisRestaurants(String nombre, Timestamp fechaHoraSQL) {

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        String sql = "SELECT horario.id, fecha_inicio, fecha_fin\n"
                + "FROM horario\n"
                + "INNER JOIN restaurante ON restaurante.nombre = horario.nombreRes\n"
                + "WHERE fecha_inicio >= ? AND fecha_inicio < ? AND restaurante.nombre = ? ORDER BY fecha_inicio ASC;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaSinTiempo = fechaHoraSQL.toLocalDateTime().toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(fechaSinTiempo));
            statement.setDate(2, Date.valueOf(fechaSinTiempo.plusDays(1)));
            statement.setString(3, nombre);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                horaris.add(
                        new Horari(
                                resultSet.getInt(1),
                                resultSet.getTimestamp(2).toLocalDateTime(),
                                resultSet.getTimestamp(3).toLocalDateTime()));

            }

            connection.close();

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return horaris;
    }

    // igual que la llista d'horaris de ¡restaurant pero només amb atraccions. A les
    // següents consultes, ho he agrupat amb una classe Zona perque sigui més òptim
    // el còdi.
    public ObservableList<Horari> llistaHorarisAtraccions(String nombre, Timestamp fechaHoraSQL) {

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        String sql = "SELECT horario.id, fecha_inicio, fecha_fin\n"
                + "FROM horario\n"
                + "INNER JOIN atraccion ON atraccion.nombre = horario.nombreAtr\n"
                + "WHERE fecha_inicio >= ? AND fecha_inicio < ? AND atraccion.nombre = ? ORDER BY fecha_inicio ASC;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaSinTiempo = fechaHoraSQL.toLocalDateTime().toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(fechaSinTiempo));
            statement.setDate(2, Date.valueOf(fechaSinTiempo.plusDays(1)));
            statement.setString(3, nombre);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                horaris.add(
                        new Horari(
                                resultSet.getInt(1),
                                resultSet.getTimestamp(2).toLocalDateTime(),
                                resultSet.getTimestamp(3).toLocalDateTime()));

            }
            connection.close();

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return horaris;
    }

    // llista totes les atraccions de la taula atraccions, afegint tots els camps.
    public ObservableList<Atraccio> llistaAtraccio() {
        ObservableList<Atraccio> itemList = FXCollections.observableArrayList();
        String sql = "SELECT nombre, descripcion, tipo, ubicacion FROM atraccion";
        Connection connection = new Connexio().connecta();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                itemList.add(
                        new Atraccio(
                               
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)));
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return itemList;
    }

    // llista tots els restaurant amb tots els camps dels restautants.
    public ObservableList<Restaurant> llistaRestaurants() {
        ObservableList<Restaurant> llistaRestaurants = FXCollections.observableArrayList();
        String sql = "SELECT nombre, tipoComida, ubicacion, descripcion FROM restaurante";
        Connection connection = new Connexio().connecta();
        try {
            Statement ordre = connection.createStatement();
            ResultSet resultSet = ordre.executeQuery(sql);
            while (resultSet.next()) {
                Restaurant restaurante = new Restaurant(
                        
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
                llistaRestaurants.add(restaurante);
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return llistaRestaurants;
    }

    // feim un insert d'un horari dins una atracció, segons els valors de l'objecte
    // horari passats per paràmetres, que també té l'id de l'atracció inclòs.
    public String afegeixHorariAtraccio(Horari horari) throws SQLException, FileNotFoundException, IOException {
    String missatge = "";
    Connection connection = new Connexio().connecta();
    String sql = "INSERT INTO horario (fecha_inicio, fecha_fin, nombreAtr) VALUES (?,?,?)";
    PreparedStatement ordre = connection.prepareStatement(sql);
    try {
        ordre.setTimestamp(1, Timestamp.valueOf(horari.getFecha_inici()));
        ordre.setTimestamp(2, Timestamp.valueOf(horari.getFecha_fin()));
        ordre.setString(3, horari.getNombreAtr().trim());
        
        int rowsAffected = ordre.executeUpdate();
        
        if (rowsAffected > 0) {
            missatge = "ok";
        }
    } catch (SQLException e) {
        if ("45000".equals(e.getSQLState())) {
            missatge = e.getMessage();
        }
    } 
    return missatge;
}


    // feim un insert d'un horari dins un restaurant, segons els valors de l'objecte
    // horari passats per paràmetres, que també té l'id del restaurant inclòs.
   public String afegeixHorariRestaurant(Horari horari) throws SQLException, FileNotFoundException, IOException {
    String missatge = "";
    Connection connection = new Connexio().connecta();
    String sql = "INSERT INTO horario (fecha_inicio, fecha_fin, nombreRes) VALUES (?,?,?)";
    PreparedStatement ordre = connection.prepareStatement(sql);
    try {
        ordre.setTimestamp(1, Timestamp.valueOf(horari.getFecha_inici()));
        ordre.setTimestamp(2, Timestamp.valueOf(horari.getFecha_fin()));
        ordre.setString(3, horari.getNombreRes());
        
        int rowsAffected = ordre.executeUpdate();
        
        if (rowsAffected > 0) {
            missatge = "ok";
        }
        //queremos que devuelva el mensaje del trigger de no se pueden repetir horarios con el mismo nombre de restaurante
    } catch (SQLException e) {
        if ("45000".equals(e.getSQLState())) {
            missatge = e.getMessage();
        }
    } 
    
    return missatge;
}

    // mètode que inserta un horari a un empleat, passats per paràmetres, on agafa
    // l'id de cada un i ho afegeix a la taula asignación.
    public String assignarHoraris(Horari horari, EmpleadosClass empleat) throws SQLException
           {
      
        String missatge = "";
        Connection connection = new Connexio().connecta();
        String sql = "INSERT INTO asignacion (idHorario, dniEmpleado) VALUES (?, ?);";
        PreparedStatement ordre = connection.prepareStatement(sql);
        try {
            ordre.setInt(1, horari.getId());
            ordre.setString(2, empleat.getDni());
            ordre.executeUpdate();
            missatge = "ok";

        } catch (SQLException e) {
             //queremos que devuelva el mensaje del trigger de no se pueden repetir horarios con el mismo nombre de restaurante
            if ("45000".equals(e.getSQLState())) {
               missatge = e.getMessage();
               // si aparece el mensaje de duplicate enviamos el mensaje de horario ya asignado, aunque primero se lanzara el trigger, así que este mensaje no de
               //deberia aparecer nunca. 
            } else if (e.getMessage().contains("Duplicate entry")) {
               missatge = "Horario ya asignado al empleado";
            } 
        }
        return missatge;
    }

    // retorna una llista dels empleats que son "Aprendiz" o "Trabajador", ja que
    // són els que treballen a les zones del parc.
    public ObservableList<EmpleadosClass> llistaEmpleatsHoraris() {
        ObservableList<EmpleadosClass> llistaEmpleats = FXCollections.observableArrayList();
        String sql = "SELECT dni, nombre FROM empleado WHERE categoria = 'Aprendiz' or categoria = 'Trabajador'";
        Connection connection = new Connexio().connecta();
        try {
            Statement ordre = connection.createStatement();
            ResultSet resultSet = ordre.executeQuery(sql);
            while (resultSet.next()) {
                EmpleadosClass empleado = new EmpleadosClass(
                        resultSet.getString(1),
                        resultSet.getString(2));
                llistaEmpleats.add(empleado);
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return llistaEmpleats;
    }

}
