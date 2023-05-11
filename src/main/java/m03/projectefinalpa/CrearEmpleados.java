/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Optional;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.crypto.AEADBadTagException;
import javax.imageio.ImageIO;
import m03.projectefinalpa.model.GestioDadesDatosEmpleado;
import m03.projectefinalpa.model.classes.EmpleadosClass;

/**
 *
 * @author User
 */
public class CrearEmpleados {

    @FXML
    TextField dniBuscar;
    @FXML
    TextField dni;
    @FXML
    TextField nombre;
    @FXML
    TextField direccion;
    @FXML
    TextField telefono;
    @FXML
    TextField email;
    @FXML
    ComboBox categoria;

    @FXML
    Button agregar;
    @FXML
    Button modificar;
    @FXML
    Button eliminar;
    @FXML
    ImageView foto;

    String dniDatos;
    String nombreDatos;
    String direccionDatos;
    String telefonoDatos;
    String emailDatos;
    String categoriaDatos;

    Image fotoCargada;
    GestioDadesDatosEmpleado dades = new GestioDadesDatosEmpleado();
    EmpleadosClass empleado;

    public void agregarEmpleado() {
        if (dni.getText().isEmpty() || nombre.getText().isEmpty() || direccion.getText().isEmpty() || telefono.getText().isEmpty() || email.getText().isEmpty() || categoria.getValue() == null) {
            alerta("Debe completar todos los campos");

        } else {
            
         

            try {
                // Obtener los datos ingresados por el usuario
                dniDatos = dni.getText();
                nombreDatos = nombre.getText();
                direccionDatos = direccion.getText();
                telefonoDatos = telefono.getText();
                emailDatos = email.getText();
                categoriaDatos = categoria.getValue().toString();
                fotoCargada = foto.getImage();

                // Insertar el nuevo empleado en la base de datos
                EmpleadosClass empleados = new EmpleadosClass(dniDatos, nombreDatos, direccionDatos, telefonoDatos, emailDatos, categoriaDatos, fotoCargada);
                boolean exito = dades.insertarEmpleado(empleados);

                if (exito) {
                    alerta("Empleado agregado con éxito");
                    vaciar();
                } else {
                    alerta("Usuario ya registrado");
                }
            } catch (Exception e) {
                alerta(e.getMessage());
            }
        }
    }

    @FXML
    public void buscarEmpleado() {

        if (dniBuscar.getText().equals("")) {
            alerta("Debe ingresar un número de DNI para buscar");
        } else {
            try {
                String dniBuscado = dniBuscar.getText();
                empleado = dades.datosEmpleado(dniBuscado);

                if (empleado != null) {

                    // Mostrar los datos del empleado encontrado en los campos correspondientes
                    dni.setText(empleado.getDni());
                    nombre.setText(empleado.getNombre());
                    direccion.setText(empleado.getDireccion());
                    telefono.setText(empleado.getTelefono());
                    email.setText(empleado.getEmail());
                    categoria.setValue(empleado.getCategoria());

                    if (empleado.getFoto() != null) {
                        InputStream is = empleado.getFoto().getBinaryStream();
                        Image image = new Image(is);
                        foto.setImage(image);
                    } else {
                        foto.setImage(null);
                    }

                    ordenBotonesModificar();

                } else {
                    alerta("No se ha encontrado ningún empleado con ese DNI");
                }
            } catch (Exception e) {
                alerta(e.getMessage());
            }
        }
    }

    public void modificarEmpleado() {

        if (dni.getText().isEmpty() || nombre.getText().isEmpty() || direccion.getText().isEmpty() || telefono.getText().isEmpty() || email.getText().isEmpty() || categoria.getValue() == null) {
            alerta("Debe completar todos los campos");
        } else {

           int conf = Confirmacion("¿Desea modificar al empleado con DNI "+ empleado.getDni()+ "?");
            if (conf == 1) {
                try {
                    // Obtener los datos ingresados por el usuario
                    dniDatos = dni.getText();
                    nombreDatos = nombre.getText();
                    direccionDatos = direccion.getText();
                    telefonoDatos = telefono.getText();
                    emailDatos = email.getText();
                    categoriaDatos = categoria.getValue().toString();
                    fotoCargada = foto.getImage();

                    // Crear el objeto empleado con los datos actualizados
                    EmpleadosClass empleadoModificado = new EmpleadosClass(dniDatos, nombreDatos, direccionDatos, telefonoDatos, emailDatos, categoriaDatos, fotoCargada);

                    // Actualizar el empleado en la base de datos pasando su antiguo dni para que sea la clave unica, y todos los datos modificados. 
                    boolean exito = dades.modificarEmpleado(empleado, empleadoModificado);

                    if (exito) {
                        alerta("Empleado modificado con éxito");
                        vaciar();
                    } else {
                        alerta("No se ha podido modificar el usuario");
                    }
                } catch (Exception e) {
                    alerta(e.getMessage());
                }
            }

        }
    }

    @FXML
    void eliminarEmpleado() {

        if (dniBuscar.getText().equals("Debe buscar un empleado antes de poder eliminarlo")) {
            alerta("");
        } else {
             int conf = Confirmacion("¿Desea eliminar al empleado con DNI "+dniBuscar.getText()+ "?");
            if (conf == 1) {
            try {
                String dniAEliminar = dniBuscar.getText();
                boolean exito = dades.eliminarEmpleado(dniAEliminar);
                if (exito) {
                    alerta("Empleado eliminado con éxito");
                    vaciar();
                } else {
                    alerta("No se pudo eliminar el empleado");
                }
            } catch (Exception e) {
                alerta(e.getMessage());
            }
            }
        }

    }

// Método para obtener un array de bytes a partir de una imagen
    private byte[] getBytesFromImage(Image image) throws IOException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    public void initialize() {
        // Agregar los elementos al ComboBox
        categoria.getItems().addAll("Aprendiz", "Trabajador", "Responsable de atracciones", "Responsable de restaurantes");

    }

    @FXML
    public void cargarFoto() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto");
        // Crear filtro de archivo para seleccionar solo archivos .jpg
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos JPG", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {

            fotoCargada = new Image(file.toURI().toString());
            foto.setImage(fotoCargada);

        }
    }

    private void alerta(String text) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Informació");
        alerta.setContentText(text);
        alerta.show();
    }

    @FXML
    private void cambiarPantallaAsignar() throws IOException {
        App.setRoot("Asignar");
    }

    @FXML
    private void cambiarPantallaVisualizar() throws IOException {
        App.setRoot("Visualizar");
    }

    @FXML
    private void cambiarPantallaEmpleados() throws IOException {
        App.setRoot("Empleados");
    }

    @FXML
    private void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
    }

    @FXML
    private void cambiarPantallaEditar() throws IOException {
        App.setRoot("Ingresar_Datos");
    }

    private void reiniciarCampos() {

        dni.setText("");
        nombre.setText("");
        direccion.setText("");
        telefono.setText("");
        email.setText("");
        categoria.getSelectionModel().clearSelection();
        foto.setImage(null);
        dniBuscar.setText("");

    }

    public void vaciar() {

        reiniciarCampos();
        ordenBotonesHabilitar();
    }

    private void ordenBotonesModificar() {
        agregar.setDisable(true);
        eliminar.setDisable(false);
        modificar.setDisable(false);

    }

    private void ordenBotonesHabilitar() {
        agregar.setDisable(false);
        eliminar.setDisable(true);
        modificar.setDisable(true);

    }

    // alerta de confirmación, para eliminar el horario.
    private int Confirmacion(String texto) {
        int valor;
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText(texto);
        ButtonType buttonTypeAceptar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancelar = new ButtonType("Cancelar");
        alerta.getButtonTypes().setAll(buttonTypeAceptar, buttonTypeCancelar);

        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == buttonTypeAceptar) {
            valor = 1;
        } else {
            valor = 0;
        }

        return valor;
    }
}
