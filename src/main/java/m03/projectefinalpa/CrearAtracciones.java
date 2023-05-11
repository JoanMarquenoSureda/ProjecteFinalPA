/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import m03.projectefinalpa.model.GestioDadesDatosAtraccion;
import m03.projectefinalpa.model.GestioDadesDatosEmpleado;
import m03.projectefinalpa.model.classes.Atraccio;
import m03.projectefinalpa.model.classes.EmpleadosClass;

/**
 *
 * @author User
 */
public class CrearAtracciones {

    @FXML
    TextField nombreAtraccion;
    @FXML
    TextField nombre;
    @FXML
    TextField altura;
    @FXML
    TextArea descripcion;
    @FXML
    ComboBox tipo;
    @FXML
    ComboBox ubicacion;

    @FXML
    Button agregar;
    @FXML
    Button modificar;
    @FXML
    Button eliminar;

    String nombreDatos;
    String alturaDatos;
    String descripcionDatos;
    String tipoDatos;
    String ubicacionDatos;

    GestioDadesDatosAtraccion dades = new GestioDadesDatosAtraccion();
    Atraccio atraccion;

    @FXML
    public void agregarAtraccion() {

        if (tipo.getValue() == null || ubicacion.getValue() == null || altura.getText().equals("") || descripcion.getText().equals("") || nombre.getText().equals("")) {
            alerta("Debe completar todos los campos");
        } else {
            try {
                // Obtener los datos ingresados por el usuario
                nombreDatos = nombre.getText();
                tipoDatos = tipo.getValue().toString();
                ubicacionDatos = ubicacion.getValue().toString();
                alturaDatos = altura.getText();
                descripcionDatos = descripcion.getText();

                // Insertar la nueva atracción en la base de datos
                Atraccio atraccion = new Atraccio(nombreDatos, tipoDatos, ubicacionDatos, alturaDatos, descripcionDatos);
                boolean exito = dades.insertarAtraccion(atraccion);

                if (exito) {
                    alerta("Atracción agregada con éxito");
                    vaciar();
                } else {
                    alerta("La atracción ya está registrada");
                }
            } catch (Exception e) {
                alerta(e.getMessage());
            }
        }
    }

    @FXML
    public void modificarAtraccion() {
        if (tipo.getValue() == null || ubicacion.getValue() == null || altura.getText().equals("") || descripcion.getText().equals("") || nombre.getText().equals("")) {
            alerta("Debe completar todos los campos");
        } else {
            if (nombreAtraccion.getText().equals("")) {
                alerta("Debes añadir un valor a buscar para modificarlo");

            } else {
                int conf = Confirmacion("¿Desea modificar la atracción " + nombreAtraccion.getText() + "?");
                if (conf == 1) {
                    try {
                        // Obtener los datos ingresados por el usuario
                        String nombreBuscado = nombreAtraccion.getText();
                        nombreDatos = nombre.getText();
                        tipoDatos = tipo.getValue().toString();
                        ubicacionDatos = ubicacion.getValue().toString();
                        alturaDatos = altura.getText();
                        descripcionDatos = descripcion.getText();

                        // Crear el objeto atraccion con los datos actualizados
                        Atraccio atraccionModificada = new Atraccio(nombreDatos, tipoDatos, ubicacionDatos, alturaDatos, descripcionDatos);

                        // Actualizar la atracción en la base de datos pasando su antiguo nombre para que sea la clave única, y todos los datos modificados.
                        boolean exito = dades.modificarAtraccion(nombreBuscado, atraccionModificada);

                        if (exito) {
                            alerta("Atracción modificada con éxito");
                            vaciar();
                        } else {
                            alerta("No se ha podido modificar la atracción");
                        }
                    } catch (Exception e) {
                        alerta(e.getMessage());
                    }
                }
            }
        }
    }

    @FXML
    public void eliminarAtraccion() {

        if (nombreAtraccion.getText().equals("")) {
            alerta("Debe buscar una atracción antes de poder eliminarla");
        } else {
            int conf = Confirmacion("¿Desea eliminar la atracción " + nombreAtraccion.getText() + "?");
            if (conf == 1) {
                try {
                    String nombreAEliminar = nombreAtraccion.getText();
                    boolean exito = dades.eliminarAtraccion(nombreAEliminar);
                    if (exito) {
                        alerta("Atracción eliminada con éxito");
                        vaciar();
                    } else {
                        alerta("No se pudo eliminar la atracción");
                    }
                } catch (Exception e) {
                    alerta(e.getMessage());
                }
            }
        }
    }

    @FXML
    public void buscarAtraccion() {

        if (nombreAtraccion.getText().equals("")) {
            alerta("Debe ingresar un nombre de atracción para buscar");
        } else {
            try {
                String nombreBuscado = nombreAtraccion.getText();
                atraccion = dades.datosAtraccion(nombreBuscado);

                if (atraccion != null) {

                    // Mostrar los datos de la atracción encontrada en los campos correspondientes
                    nombre.setText(atraccion.getNombre());
                    tipo.setValue(atraccion.getTipo());
                    ubicacion.setValue(atraccion.getUbicacion());
                    altura.setText(atraccion.getAlturaminima());
                    descripcion.setText(atraccion.getDescripcion());

                    ordenBotonesModificar();

                } else {
                    alerta("No se ha encontrado ninguna atracción con ese nombre");
                }
            } catch (Exception e) {
                alerta(e.getMessage());
            }
        }
    }

    public void initialize() {
        // Agregar los elementos al ComboBox
        tipo.getItems().addAll("Adrenalítica", "Infantil", "Acuática", "Familiar", "Infantil");
        ubicacion.getItems().addAll("Sésamo Aventura", "China", "Mediterranea", "Méjico", "Polinesia", "Far West");

    }

    private void alerta(String text) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Informació");
        alerta.setContentText(text);
        alerta.show();
    }

    private void reiniciarCampos() {

        nombreAtraccion.setText("");
        nombre.setText("");
        descripcion.setText("");
        altura.setText("");
        tipo.getSelectionModel().clearSelection();
        ubicacion.getSelectionModel().clearSelection();

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
}
