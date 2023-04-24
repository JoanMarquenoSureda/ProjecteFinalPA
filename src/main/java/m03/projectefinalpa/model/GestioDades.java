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
public class GestioDades {

    public ObservableList<Horari> llistaHoraris() {

        ObservableList<Horari> horaris = FXCollections.observableArrayList();
        String sql = "SELECT fecha_inicio, fecha_fin, idAtraccion, idRestaurante FROM horario;";
        //String sql="select nom from usuaris";
        Connection connection = new Connexio().connecta();
        try {
            Statement ordre = connection.createStatement();
            ResultSet resultSet = ordre.executeQuery(sql);
            while (resultSet.next()) {
                horaris.add(
                        new Horari(
                                resultSet.getDate(2),
                                resultSet.getDate(3),
                                resultSet.getInt(4),
                                resultSet.getInt(5)
                        )
                );
            }
            connection.close();

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return horaris;
    }

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
                                resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("descripcion"),
                                resultSet.getString("tipo"),
                                resultSet.getDouble("alturaminima"),
                                resultSet.getString("ubicacion")
                        )
                );
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return itemList;
    }

    public ObservableList<Restaurant> llistaRestaurants() {
        ObservableList<Restaurant> llistaRestaurants = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre, tipoComida, ubicacion FROM restaurante";
        Connection connection = new Connexio().connecta();
        try {
            Statement ordre = connection.createStatement();
            ResultSet resultSet = ordre.executeQuery(sql);
            while (resultSet.next()) {
                Restaurant restaurante = new Restaurant(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipoComida"),
                        resultSet.getString("ubicacion")
                );
                llistaRestaurants.add(restaurante);
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }
        return llistaRestaurants;
    }

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

    public ObservableList<Empleados> llistaEmpleatsHoraris() {
        ObservableList<Empleados> llistaEmpleats = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre FROM empleado WHERE categoria = 'Aprendiz' or categoria = 'Trabajador'";
        Connection connection = new Connexio().connecta();
        try {
            Statement ordre = connection.createStatement();
            ResultSet resultSet = ordre.executeQuery(sql);
            while (resultSet.next()) {
                Empleados empleado = new Empleados(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre")
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
