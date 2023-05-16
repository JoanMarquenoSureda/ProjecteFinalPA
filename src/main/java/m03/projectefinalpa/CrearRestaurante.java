/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import m03.projectefinalpa.model.GestioDadesDatosRestaurantes;
import m03.projectefinalpa.model.classes.Restaurant;

// misma estructura que clase CrearAtraccion
public class CrearRestaurante {

    @FXML
    TextField nombreRestaurante;
    @FXML
    TextField nombre;
    @FXML
    TextArea descripcion;
    @FXML
    ComboBox tipoComida;
    @FXML
    ComboBox ubicacion;

    @FXML
    Button agregar;
    @FXML
    Button modificar;
    @FXML
    Button eliminar;
    @FXML
    Button buscar;

    String nombreBuscado;
    String nombreDatos;
    String descripcionDatos;
    String tipoComidaDatos;
    String ubicacionDatos;

    GestioDadesDatosRestaurantes dades = new GestioDadesDatosRestaurantes();
    Restaurant restaurante;
    
    //caracteres que pueden soportar los textfield
    private static final int MAX_CARACTERES_NOMBRE_RESTAURANTE = 50;
    private static final int MAX_CARACTERES_DESCRIPCION = 2000;

@FXML
public void agregarRestaurante() {
    if (tipoComida.getValue() == null || ubicacion.getValue() == null || descripcion.getText().equals("") || nombre.getText().equals("")) {
        alerta("Debe completar todos los campos");
    } else {
        boolean ok = validarLongitudTexto();
        if (ok) {
            try {
                // Obtener los datos ingresados por el usuario
                nombreDatos = nombre.getText();
                tipoComidaDatos = tipoComida.getValue().toString();
                ubicacionDatos = ubicacion.getValue().toString();
                descripcionDatos = descripcion.getText();

                // Insertar la nueva atracción en la base de datos
                restaurante = new Restaurant(nombreDatos, ubicacionDatos, tipoComidaDatos, descripcionDatos);
                boolean exito = dades.insertarRestaurante(restaurante);

                if (exito) {
                    alerta("Restaurante agregado con éxito");
                    vaciar();
                } else {
                    alerta("El Restaurante ya está registrado");
                }
            } catch (IOException | SQLException e) {
                alerta(e.getMessage());
            }
        }
    }
}

@FXML
public void modificarRestaurante() {
    if (tipoComida.getValue() == null || ubicacion.getValue() == null || descripcion.getText().equals("") || nombre.getText().equals("")) {
        alerta("Debe completar todos los campos");
    } else {
        if (nombreRestaurante.getText().equals("")) {
            alerta("Debe añadir un valor a buscar para modificarlo");
        } else {
            if (validarLongitudTexto()) {
                int conf = Confirmacion("¿Desea modificar el restaurante " + nombreRestaurante.getText() + "?");
                if (conf == 1) {
                    try {
                        // Obtener los datos ingresados por el usuario
                        nombreBuscado = nombreRestaurante.getText();
                        nombreDatos = nombre.getText();
                        tipoComidaDatos = tipoComida.getValue().toString();
                        ubicacionDatos = ubicacion.getValue().toString();
                        descripcionDatos = descripcion.getText();

                        // Crear el objeto atraccion con los datos actualizados
                        Restaurant restauranteModificado = new Restaurant(nombreDatos, ubicacionDatos, tipoComidaDatos, descripcionDatos);

                        // Actualizar la atracción en la base de datos pasando su antiguo nombre para que sea la clave única, y todos los datos modificados.
                        boolean exito = dades.modificarRestaurante(nombreBuscado, restauranteModificado);

                        if (exito) {
                            alerta("Restaurante modificado con éxito");
                            vaciar();
                        } else {
                            alerta("No se ha podido modificar el Restaurante");
                        }
                    } catch (IOException | SQLException e) {
                        alerta(e.getMessage());
                    }
                }
            }
        }
    }
}


    @FXML
    public void eliminarRestaurante() {

        if (nombreRestaurante.getText().equals("")) {
            alerta("Debe buscar un restaurante antes de poder eliminarlo");
        } else {
            int conf = Confirmacion("¿Desea eliminar el restaurante " + nombreRestaurante.getText() + "?");
            if (conf == 1) {
                try {
                    nombreBuscado = nombreRestaurante.getText();
                    boolean exito = dades.eliminarRestaurante(nombreBuscado);
                    if (exito) {
                        alerta("Restaurante eliminado con éxito");
                        vaciar();
                    } else {
                        alerta("No se pudo eliminar el Restaurante");
                    }
                } catch (IOException | SQLException e) {
                    alerta(e.getMessage());
                }
            }
        }
    }

    @FXML
    public void buscarRestaurante() {

        if (nombreRestaurante.getText().equals("")) {
            alerta("Debe ingresar un nombre de un Restaurante para buscar");
        } else {
            try {
                nombreBuscado = nombreRestaurante.getText();
                restaurante = dades.datosRestaurant(nombreBuscado);

                if (restaurante != null) {

                    // Mostrar los datos de la atracción encontrada en los campos correspondientes
                    nombre.setText(restaurante.getNombre());
                    tipoComida.setValue(restaurante.getTipoComida());
                    ubicacion.setValue(restaurante.getUbicacion());
                    descripcion.setText(restaurante.getDescripcion());

                    ordenBotonesModificar();

                } else {
                    alerta("No se ha encontrado ningún restaurante con ese nombre");
                }
            } catch (Exception e) {
                alerta(e.getMessage());
            }
        }
    }
     @FXML //funcion del boton limpiar, para reinicar campos sin texto y cambiar orden botones para buscar restaurante
    public void vaciar() {

        reiniciarCampos();
        ordenBotonesHabilitar();
    }
    
    //validamos la longitud de los caracteres de cada campo, para no tener problemas con la base de datos
     private boolean validarLongitudTexto() {
        String nombreRestauranteTexto = nombre.getText();
        String descripcionTexto = descripcion.getText();

        if (nombreRestauranteTexto.length() > MAX_CARACTERES_NOMBRE_RESTAURANTE) {
            alerta("El nombre no puede exceder los " + MAX_CARACTERES_NOMBRE_RESTAURANTE + " caracteres.");
            return false;
        }

        if (descripcionTexto.length() > MAX_CARACTERES_DESCRIPCION) {
            alerta("La descripción no puede exceder los " + MAX_CARACTERES_DESCRIPCION + " caracteres.");
            return false;
        }

        return true;
    }

    public void initialize() {
        // Agregar los elementos al ComboBox
       tipoComida.getItems().addAll("Comida rápida", "Comida Americana", "Pescado y Marisco", "Comida Mejicana", "Tapas", "Comida China");
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

        nombreRestaurante.setText("");
        nombre.setText("");
        descripcion.setText("");
      
        tipoComida.setValue("");
        ubicacion.setValue("");

    }
   

    private void ordenBotonesModificar() {
        agregar.setDisable(true);
        eliminar.setDisable(false);
        modificar.setDisable(false);
        nombreRestaurante.setEditable(false);// ponemos que no se pueda editar ya que cuando modifiquemos los campos, se extraera de aqui por
        //si se quiere tambien modificar el nombre de la atraccion. 
        buscar.setDisable(true);

    }

    private void ordenBotonesHabilitar() {
        agregar.setDisable(false);
        eliminar.setDisable(true);
        modificar.setDisable(true);
        nombreRestaurante.setEditable(true);
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

    @FXML
    public void cambiarPantallaAsignar() throws IOException {
        App.setRoot("Asignar");
    }

    @FXML
    public void cambiarPantallaVisualizar() throws IOException {
        App.setRoot("Visualizar");
    }

    @FXML
    public void cambiarPantallaEmpleados() throws IOException {
        App.setRoot("Empleados");
    }

    @FXML
    public void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
    }

    @FXML
    public void cambiarPantallaEditar() throws IOException {
        App.setRoot("Ingresar_Datos");
    }
    
    
}
