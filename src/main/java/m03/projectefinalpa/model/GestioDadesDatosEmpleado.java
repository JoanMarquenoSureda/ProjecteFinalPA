/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import m03.projectefinalpa.model.classes.EmpleadosClass;

public class GestioDadesDatosEmpleado {

    public boolean insertarEmpleado(EmpleadosClass empleado) throws SQLException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();

        String sql = "insert into empleado (dni, nombre, direccion, telefono, email, categoria, foto) values (?,?,?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        try {

            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getDireccion());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getEmail());
            ps.setString(6, empleado.getCategoria());

            Image ima = empleado.getFotoImage();
            
            //si hay una imagen la convertirmos a BLOB para mysql
            if (ima != null) {
                BufferedImage imagenB = SwingFXUtils.fromFXImage(ima, null);
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(imagenB, "jpg", s);
                byte[] imaBytes = s.toByteArray();
                Blob b = connection.createBlob();
                b.setBytes(1, imaBytes);
                ps.setBlob(7, b);
                //sino le pasamos un NULL
            } else {
                ps.setNull(7, java.sql.Types.BLOB);

            }

            ps.executeUpdate();
            ok = true;

        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        }

        return ok;
    }

    //mètode que retorna les dades d'un empleat segons el nom de l'empleat passat per paràmetres, només retorna un objecte ja que passem per paràmetres el =. 
    public EmpleadosClass datosEmpleado(String dni) {

        EmpleadosClass empleado = null;

        String sql = "SELECT dni, nombre, direccion, telefono, email, categoria, foto FROM empleado \n"
                + "WHERE dni = ?;";

        Connection connection = new Connexio().connecta();

        try {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, dni);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                //convertimos un BLOB  a IMAGE para pasarlo al constructor 
                Blob blob = resultSet.getBlob(7);
                Image image = null;
                if (blob != null) {
                    InputStream is = blob.getBinaryStream();
                    image = new Image(is);
                }
                empleado = new EmpleadosClass(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        image
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return empleado;
    }
// conculta para modificar empleados 
    public boolean modificarEmpleado(String dnianterior, EmpleadosClass empleadoNuevo) throws SQLException, IOException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();
        String sql = "update empleado set dni=?, nombre=?, direccion=?, telefono=?, email=?, categoria=?, foto=? where dni=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, empleadoNuevo.getDni());
            ps.setString(2, empleadoNuevo.getNombre());
            ps.setString(3, empleadoNuevo.getDireccion());
            ps.setString(4, empleadoNuevo.getTelefono());
            ps.setString(5, empleadoNuevo.getEmail());
            ps.setString(6, empleadoNuevo.getCategoria());
            ps.setString(8, dnianterior); // Establecemos el valor de la clave primaria "dni"

            Image ima = empleadoNuevo.getFotoImage();

            if (ima != null) {
                BufferedImage imagenB = SwingFXUtils.fromFXImage(ima, null);
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(imagenB, "jpg", s);
                byte[] imaBytes = s.toByteArray();
                Blob b = connection.createBlob();
                b.setBytes(1, imaBytes);
                ps.setBlob(7, b);
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                ok = true;
            }
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        } finally {
            connection.close();
        }
        return ok;
    }

    public boolean eliminarEmpleado(String dni) throws SQLException {
        boolean ok = false;
        Connection connection = new Connexio().connecta();
        String sql = "delete from empleado where dni=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dni); // Establecemos el valor de la clave primaria "dni"

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                ok = true;
            }
        } catch (SQLException throwables) {
            System.out.println("Error:" + throwables.getMessage());
        } finally {
            connection.close();
        }
        return ok;
    }

}
