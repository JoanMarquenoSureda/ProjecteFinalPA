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

import m03.projectefinalpa.model.classes.Atraccio;


public class GestioDadesDatosAtraccion {

    public boolean insertarAtraccion(Atraccio atraccio) throws SQLException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();

        String sql = "insert into atraccion (nombre, tipo, ubicacion, alturaminima, descripcion) values (?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        try {

            ps.setString(1, atraccio.getNombre());
            ps.setString(2, atraccio.getTipo());
            ps.setString(3, atraccio.getUbicacion());
            ps.setInt(4, atraccio.getAlturaminima());
            ps.setString(5, atraccio.getDescripcion());
   
            
            ps.executeUpdate();
            ok = true;

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }

        return ok;
    }
    
    
    public Atraccio datosAtraccion(String nombre) {

        Atraccio atraccion = null;

        String sql = "SELECT nombre, tipo, ubicacion, alturaminima, descripcion FROM atraccion \n"
                + "WHERE nombre = ?;";

        Connection connection = new Connexio().connecta();

        try {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nombre);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                atraccion = new Atraccio(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5) 
                );
                
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return atraccion;
    }


    public boolean modificarAtraccion(String atraccionAnterior, Atraccio atraccionNueva) throws SQLException, IOException {
    boolean ok = false;
    Connection connection = new Connexio().connecta();

    String sql = "UPDATE atraccion SET tipo=?, ubicacion=?, alturaminima=?, descripcion=?, nombre=? WHERE nombre=?";

    PreparedStatement ps = connection.prepareStatement(sql);

    try {
        
        ps.setString(1, atraccionNueva.getTipo());
        ps.setString(2, atraccionNueva.getUbicacion());
        ps.setInt(3, atraccionNueva.getAlturaminima());
        ps.setString(4, atraccionNueva.getDescripcion());
        ps.setString(5, atraccionNueva.getNombre());
        ps.setString(6, atraccionAnterior);

        ps.executeUpdate();
        ok = true;

    } catch (SQLException throwables) {
        System.out.println("Error:" + throwables.getMessage());
    }

    return ok;
}


   public boolean eliminarAtraccion(String nombre) throws SQLException, IOException {
    boolean ok = false;
    Connection connection = new Connexio().connecta();

    String sql = "DELETE FROM atraccion WHERE nombre=?";

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

