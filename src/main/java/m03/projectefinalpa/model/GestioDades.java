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
        return llistaHoraris();
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
            System.out.println("Error:"+throwables.getMessage());
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
            System.out.println("Error:"+throwables.getMessage());
        }

        return ok;
    }
}
