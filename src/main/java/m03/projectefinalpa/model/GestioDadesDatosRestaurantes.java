/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import m03.projectefinalpa.model.classes.Restaurant;

public class GestioDadesDatosRestaurantes {

  public boolean insertarRestaurante(Restaurant restaurante) throws SQLException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();

        String sql = "insert into restaurante (nombre, ubicacion, tipoComida, descripcion) values (?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        try {

            ps.setString(1, restaurante.getNombre());
            ps.setString(2, restaurante.getUbicacion());
             ps.setString(3, restaurante.getTipoComida());
            ps.setString(4, restaurante.getDescripcion());
           
   
            
            ps.executeUpdate();
            ok = true;

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }

        return ok;
    }
    
    
    public Restaurant datosRestaurant(String nombre) {

        Restaurant restaurante = null;

        String sql = "SELECT nombre, ubicacion, tipoComida, descripcion FROM restaurante \n"
                + "WHERE nombre = ?;";
       
        Connection connection = new Connexio().connecta();

        try {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nombre);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                restaurante = new Restaurant(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                        
                );
                
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return restaurante;
    }


    public boolean modificarRestaurante(String restauranteAnterior, Restaurant restauranteNuevo) throws SQLException, IOException {
    boolean ok = false;
    Connection connection = new Connexio().connecta();

    String sql = "UPDATE restaurante SET nombre=?, tipoComida=?, ubicacion=?, descripcion=? WHERE nombre=?";

    PreparedStatement ps = connection.prepareStatement(sql);

    try {
        
        ps.setString(1, restauranteNuevo.getNombre());
        ps.setString(2, restauranteNuevo.getTipoComida());
        ps.setString(3, restauranteNuevo.getUbicacion());
        ps.setString(4, restauranteNuevo.getDescripcion());
        ps.setString(5, restauranteAnterior);

        ps.executeUpdate();
        ok = true;

    } catch (SQLException throwables) {
        System.out.println("Error:" + throwables.getMessage());
    }

    return ok;
}


   public boolean eliminarRestaurante(String nombre) throws SQLException, IOException {
    boolean ok = false;
    Connection connection = new Connexio().connecta();

    String sql = "DELETE FROM restaurante WHERE nombre=?";

    PreparedStatement ps = connection.prepareStatement(sql);

    try {
        ps.setString(1, nombre);

        ps.executeUpdate();
        ok = true;

    } catch (SQLException throwables) {
        System.out.println("Error:" + throwables.getMessage());
    }

    return ok;
}
}

