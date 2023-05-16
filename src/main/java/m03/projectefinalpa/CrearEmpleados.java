/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
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
    Button buscar;
    @FXML
    Button modificar;
    @FXML
    Button eliminar;
    @FXML
    ImageView foto;

    //campos donde guardaremos el texto ingresado por el usuario
    String dniDatos;
    String dniBuscado;
    String nombreDatos;
    String direccionDatos;
    String telefonoDatos;
    String emailDatos;
    String categoriaDatos;

    Image fotoCargada;
    GestioDadesDatosEmpleado dades = new GestioDadesDatosEmpleado();
    EmpleadosClass empleado;

    //caracteres que pueden soportar los textfield
    private static final int MAX_CARACTERES_NOMBRE = 50;
    private static final int MAX_CARACTERES_DNI = 9;
    private static final int MAX_CARACTERES_DIRECCION = 50;
    private static final int MAX_CARACTERES_TELEFONO = 15;
    private static final int MAX_CARACTERES_EMAIL = 50;

    @FXML //metodo para agregar un empleado 
    public void agregarEmpleado() {

        //comprobamos que los campos no estén vacios
        if (dni.getText().equals("") || nombre.getText().equals("") || direccion.getText().equals("") || telefono.getText().equals("") || email.getText().equals("") || categoria.getValue() == null) {
            alerta("Debe completar todos los campos");

        } else {

            try {

                if (validarLongitudTexto()) {

                    // Insertar el nuevo empleado en la base de datos
                    EmpleadosClass empleados = new EmpleadosClass(dniDatos, nombreDatos, direccionDatos, telefonoDatos, emailDatos, categoriaDatos, fotoCargada);
                    boolean exito = dades.insertarEmpleado(empleados);

                    if (exito) {
                        alerta("Empleado agregado con éxito");
                        vaciar();
                    } else {
                        alerta("El usuario no se ha registrado correctamente");
                    }
                }
            } catch (IOException | SQLException e) {
                alerta(e.getMessage());
            }

        }

    }

    @FXML //metodo para buscar un empleado por su dni
    public void buscarEmpleado() {

        if (dniBuscar.getText().equals("")) {
            alerta("Debe ingresar un número de DNI para buscar");
        } else {
            try {
                dniBuscado = dniBuscar.getText();
                empleado = dades.datosEmpleado(dniBuscado);

                if (empleado != null) {

                    // Mostrar los datos del empleado encontrado en los campos correspondientes
                    dni.setText(empleado.getDni());
                    nombre.setText(empleado.getNombre());
                    direccion.setText(empleado.getDireccion());
                    telefono.setText(empleado.getTelefono());
                    email.setText(empleado.getEmail());
                    categoria.setValue(empleado.getCategoria());

                    foto.setImage(empleado.getFotoImage());

                    //cambiamos la configuración de los botones una vez se encuentra al empleado
                    ordenBotonesModificar();

                } else {
                    alerta("No se ha encontrado ningún empleado con ese DNI");
                }
            } catch (Exception e) {
                alerta(e.getMessage());
            }
        }
    }

    @FXML //método para modificar un empleado, pasando su dni del campo buscar y pasando todo el objeto de nuevo con los campos modificados. 
    public void modificarEmpleado() {

        if (dniBuscar.getText().equals("") || dni.getText().equals("") || nombre.getText().equals("") || direccion.getText().equals("") || telefono.getText().equals("") || email.getText().equals("") || categoria.getValue() == null) {
            alerta("Debe completar todos los campos");
        } else {

            int conf = Confirmacion("¿Desea modificar al empleado con DNI " + empleado.getDni() + "?");
            if (conf == 1) {
                try {

                    if (validarLongitudTexto()) {

                        // Crear el objeto empleado con los datos actualizados
                        EmpleadosClass empleadoModificado = new EmpleadosClass(dniDatos, nombreDatos, direccionDatos, telefonoDatos, emailDatos, categoriaDatos, fotoCargada);

                        // Actualizar el empleado en la base de datos pasando su antiguo dni para que sea la clave unica, y todos los datos modificados. 
                        boolean exito = dades.modificarEmpleado(dniBuscado, empleadoModificado);

                        if (exito) {
                            alerta("Empleado modificado con éxito");
                            vaciar();
                        } else {
                            alerta("No se ha podido modificar el usuario");
                        }
                    }
                } catch (IOException | SQLException e) {
                    alerta(e.getMessage());
                }
            }

        }
    }

    @FXML //metodo para eliminar un empleado según si dni
    public void eliminarEmpleado() {

        if (dniBuscar.getText().equals("")) {
            alerta("Debe buscar un empleado antes de poder eliminarlo");
        } else {
            int conf = Confirmacion("¿Desea eliminar al empleado con DNI " + dniBuscar.getText() + "?");
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
                } catch (SQLException e) {
                    alerta(e.getMessage());
                }
            }
        }

    }

    public void initialize() {
        // Agregar los elementos al ComboBox
        categoria.getItems().addAll("Aprendiz", "Trabajador", "Responsable de atracciones", "Responsable de restaurantes");

    }

    @FXML //metodo para cargar una foto mediante el boton seleccionar foto en formato JPG que es el formato que soporta BLOB y no da errores a la base de datos. 
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

    //validamos la longitud de los caracteres de cada campo, para no tener problemas con la base de datos
    private boolean validarLongitudTexto() {
        dniDatos = dni.getText();
        nombreDatos = nombre.getText();
        direccionDatos = direccion.getText();
        telefonoDatos = telefono.getText();
        emailDatos = email.getText();
        categoriaDatos = categoria.getValue().toString();
        fotoCargada = foto.getImage();

        if (dniDatos.length() != MAX_CARACTERES_DNI) {
            alerta("Formato DNI incorrecto");
            return false;
        }

        if (nombreDatos.length() > MAX_CARACTERES_NOMBRE) {
            alerta("El nombre no puede exceder los " + MAX_CARACTERES_NOMBRE + " caracteres.");
            return false;
        }

        if (direccionDatos.length() > MAX_CARACTERES_DIRECCION) {
            alerta("La dirección no puede exceder los " + MAX_CARACTERES_DIRECCION + " caracteres.");
            return false;
        }

        if (telefonoDatos.length() > MAX_CARACTERES_TELEFONO) {
            alerta("El teléfono no puede exceder los " + MAX_CARACTERES_TELEFONO + " caracteres.");
            return false;
        }

        if (emailDatos.length() > MAX_CARACTERES_EMAIL) {
            alerta("El email no puede exceder los " + MAX_CARACTERES_EMAIL + " caracteres.");
            return false;
        }

        return true;
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

    //mismos metodos que las demás vistas
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
        dniBuscar.setEditable(false);// ponemos que no se pueda editar ya que cuando modifiquemos los campos, se extraera de aqui por
        //si se quiere tambien modificar el dni de la persona. 
        buscar.setDisable(true);

    }

    private void ordenBotonesHabilitar() {
        agregar.setDisable(false);
        eliminar.setDisable(true);
        modificar.setDisable(true);
        dniBuscar.setEditable(true);// ponemos que no se pueda editar ya que cuando modifiquemos los campos, se extraera de aqui por
        //si se quiere tambien modificar el dni de la persona. 
        buscar.setDisable(false);

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
