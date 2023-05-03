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
import m03.projectefinalpa.model.classes.ZonaTrabajo;

/**
 *
 * @author joanm
 */
public class GestioDadesCrearYAsignar {

   
   // retorna una llista d'horaris amb el nom del restaurant i la seva ubicació, ordenat de forma ascendent per horari. Es busca lhorari entre dues dates concretes enviades
    // per paràmetre. 
    public ObservableList<Horari> llistaHorarisRestaurants(int id, Timestamp fechaHoraSQL) {

         ObservableList<Horari> horaris = FXCollections.observableArrayList();
        String sql = "SELECT horario.id, fecha_inicio, fecha_fin, restaurante.id, restaurante.nombre, restaurante.ubicacion\n"
                + "FROM horario\n"
                + "INNER JOIN restaurante ON restaurante.id = horario.idRestaurante\n"
                + "WHERE fecha_inicio >= ? AND fecha_inicio < ? AND restaurante.id = ? ORDER BY fecha_inicio ASC;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaSinTiempo = fechaHoraSQL.toLocalDateTime().toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(fechaSinTiempo));
            statement.setDate(2, Date.valueOf(fechaSinTiempo.plusDays(1)));
            statement.setInt(3, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                horaris.add(
                        new Horari(
                                resultSet.getInt(1),
                                resultSet.getTimestamp(2).toLocalDateTime(),
                                resultSet.getTimestamp(3).toLocalDateTime(),
                                new ZonaTrabajo(resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6))
                        )
                );
            }
            connection.close();

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return horaris;
    }

    // igual que la llista d'horaris de ¡restaurant pero només amb atraccions. A les següents consultes, ho he agrupat amb una classe Zona perque sigui més òptim el còdi. 
    public ObservableList<Horari> llistaHorarisAtraccions(int id, Timestamp fechaHoraSQL) {

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        String sql = "SELECT horario.id, fecha_inicio, fecha_fin, atraccion.id, atraccion.nombre, atraccion.ubicacion\n"
                + "FROM horario\n"
                + "INNER JOIN atraccion ON atraccion.id = horario.idAtraccion\n"
                + "WHERE fecha_inicio >= ? AND fecha_inicio < ? AND atraccion.id = ? ORDER BY fecha_inicio ASC;";

        Connection connection = new Connexio().connecta();
        try {
            LocalDate fechaSinTiempo = fechaHoraSQL.toLocalDateTime().toLocalDate();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(fechaSinTiempo));
            statement.setDate(2, Date.valueOf(fechaSinTiempo.plusDays(1)));
            statement.setInt(3, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                horaris.add(
                        new Horari(
                                resultSet.getInt(1),
                                resultSet.getTimestamp(2).toLocalDateTime(),
                                resultSet.getTimestamp(3).toLocalDateTime(),
                                new ZonaTrabajo(resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6))
                        )
                );
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
        String sql = "SELECT id, nombre, descripcion, tipo, alturaminima, ubicacion FROM atraccion";
        Connection connection = new Connexio().connecta();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                itemList.add(
                        new Atraccio(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getDouble(5),
                                resultSet.getString(6)
                        )
                );
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
        String sql = "SELECT id, nombre, tipoComida, ubicacion FROM restaurante";
        Connection connection = new Connexio().connecta();
        try {
            Statement ordre = connection.createStatement();
            ResultSet resultSet = ordre.executeQuery(sql);
            while (resultSet.next()) {
                Restaurant restaurante = new Restaurant(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
                llistaRestaurants.add(restaurante);
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return llistaRestaurants;
    }
    // feim un insert d'un horari dins una atracció, segons els valors de l'objecte horari passats per paràmetres, que també té l'id de l'atracció inclòs. 
    public boolean afegeixHorariAtraccio(Horari horari) throws SQLException, FileNotFoundException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();
        String sql = "INSERT INTO horario (fecha_inicio, fecha_fin, idAtraccion) VALUES (?,?,?)";
        PreparedStatement ordre = connection.prepareStatement(sql);
        try {
            ordre.setTimestamp(1, Timestamp.valueOf(horari.getFecha_inici()));
            ordre.setTimestamp(2, Timestamp.valueOf(horari.getFecha_fin()));
            ordre.setInt(3, horari.getIdAtraccion());
            ordre.executeUpdate();
            ok = true;

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }

        return ok;
    }

    // feim un insert d'un horari dins un restaurant, segons els valors de l'objecte horari passats per paràmetres, que també té l'id del restaurant inclòs. 
    public boolean afegeixHorariRestaurant(Horari horari) throws SQLException, FileNotFoundException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();
        String sql = "INSERT INTO horario (fecha_inicio, fecha_fin, idRestaurante) VALUES (?,?,?)";
        PreparedStatement ordre = connection.prepareStatement(sql);
        try {
            ordre.setTimestamp(1, Timestamp.valueOf(horari.getFecha_inici()));
            ordre.setTimestamp(2, Timestamp.valueOf(horari.getFecha_fin()));
            ordre.setInt(3, horari.getIdRestaurante());
            ordre.executeUpdate();
            ok = true;

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }

        return ok;
    }
    
    //mètode per esborrar un horari amb un delet, passant per paramatre l'objecte Horari que té el id assignat. 
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
    
    //mètode que inserta un horari a un empleat, passats per paràmetres, on agafa l'id de cada un i ho afegeix a la taula asignación. 
    public String assignarHoraris(Horari horari, EmpleadosClass empleat) throws SQLException, FileNotFoundException, IOException {
        boolean ok = false;
        String missatge="";
        Connection connection = new Connexio().connecta();
        String sql = "INSERT INTO asignacion (idHorario, idEmpleado) VALUES (?, ?);";
        PreparedStatement ordre = connection.prepareStatement(sql);
        try {
            ordre.setInt(1, horari.getId());
            ordre.setInt(2, empleat.getId());
            ordre.executeUpdate();
            ok = true;

        } catch (SQLException throwables) {
            missatge = throwables.getMessage()+"";
           
        }

        return missatge;
    }

    //retorna una llista dels empleats que son "Aprendiz" o "Trabajador", ja que són els que treballen a les zones del parc. 
    public ObservableList<EmpleadosClass> llistaEmpleatsHoraris() {
        ObservableList<EmpleadosClass> llistaEmpleats = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre FROM empleado WHERE categoria = 'Aprendiz' or categoria = 'Trabajador'";
        Connection connection = new Connexio().connecta();
        try {
            Statement ordre = connection.createStatement();
            ResultSet resultSet = ordre.executeQuery(sql);
            while (resultSet.next()) {
                EmpleadosClass empleado = new EmpleadosClass(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                );
                llistaEmpleats.add(empleado);
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return llistaEmpleats;
    }
    
}
