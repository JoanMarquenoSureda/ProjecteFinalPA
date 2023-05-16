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
import m03.projectefinalpa.model.GestioDadesDatosAtraccion;
import m03.projectefinalpa.model.classes.Atraccio;

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
    @FXML
    Button buscar;
    
    String nombreBuscado; //datos del nombre buscado de la atracción
    String nombreDatos; // nombre de la atraccion encontrada
    int alturaDatos; // guardaremos la altura
    String descripcionDatos; // los datos del campo de descripcion
    String tipoDatos; // los datos de tipo atraccion
    String ubicacionDatos; //su ubicacion

    GestioDadesDatosAtraccion dades = new GestioDadesDatosAtraccion(); // gestionaremos la base de datos desde datosAtraccion
    Atraccio atraccion; //para guardar el objeto atraccion

    // Caracteres máximos permitidos para los campos de texto
    private static final int MAX_CARACTERES_NOMBRE_ATRACCION = 50;
    private static final int MAX_CARACTERES_DESCRIPCION = 2000;

    @FXML //metodo para agregar una atracción del formulario
    public void agregarAtraccion() {
        try {
            // Comprobar que no estén vacíos los campos
            if (tipo.getValue() == null || ubicacion.getValue() == null || altura.getText().equals("") || descripcion.getText().equals("") || nombre.getText().equals("")) {
                alerta("Debe completar todos los campos");
            } else {
                // Obtener los datos ingresados por el usuario
                nombreDatos = nombre.getText();
                tipoDatos = tipo.getValue().toString();
                ubicacionDatos = ubicacion.getValue().toString();
                alturaDatos = Integer.parseInt(altura.getText());
                descripcionDatos = descripcion.getText();
                
                //metodo para validar la longitud mediante los parámetros staticos
                if (validarLongitudTexto()) {
                    //comprobamos que el dato no sea superior a 200cm
                    if (alturaDatos < 0 || alturaDatos > 200) {
                        alerta("La altura no puede superar los 200 cm");
                    } else {

                        // Crear el objeto atraccion con los datos ingresados
                        atraccion = new Atraccio(nombreDatos, tipoDatos, ubicacionDatos, alturaDatos, descripcionDatos);

                        // Insertar la nueva atracción en la base de datos
                        boolean exito = dades.insertarAtraccion(atraccion);

                        if (exito) {
                            alerta("Atracción agregada con éxito");
                            vaciar();
                        } else {
                            alerta("La atracción ya está registrada");
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            alerta("La altura mínima tiene que ser con valores numéricos");
        } catch (IOException | SQLException e) {
            alerta(e.getMessage());
        }
    }

    @FXML //metodo para modificar una atraccion previamente buscada
    public void modificarAtraccion() {

        //comprobamos que no estén vacios los campos
        if (tipo.getValue() == null || ubicacion.getValue() == null || altura.getText().equals("") || descripcion.getText().equals("") || nombre.getText().equals("")) {
            alerta("Debe completar todos los campos");
        } else {
            //si no hay texto en el campo buscar, (improbable caso ya que està deshabilitado el boton para que el usuario no pueda borrarlo)
            if (nombreAtraccion.getText().equals("")) {
                alerta("Debes añadir un valor a buscar para modificarlo");

            } else {
                //confirarmos si quiere modificar los cambios.  
                int conf = Confirmacion("¿Desea modificar la atracción " + nombreAtraccion.getText() + "?");
                if (conf == 1) {
                    try {
                        // Obtener los datos ingresados por el usuario
                        nombreBuscado = nombreAtraccion.getText();
                        nombreDatos = nombre.getText();
                        tipoDatos = tipo.getValue().toString();
                        ubicacionDatos = ubicacion.getValue().toString();
                        alturaDatos = Integer.parseInt(altura.getText());
                        descripcionDatos = descripcion.getText();
                        
                        if (validarLongitudTexto()) {
                        //comprobamos que el dato no sea superior a 200cm
                        if (alturaDatos < 0 || alturaDatos > 200) {
                            alerta("La altura no puede superar los 200 cm");

                        } else {

                            // Crear el objeto atraccion con los datos actualizados
                            Atraccio atraccionModificada = new Atraccio(nombreDatos, tipoDatos, ubicacionDatos, alturaDatos, descripcionDatos);

                            // Actualizar la atracción en la base de datos pasando su antiguo nombre por si también quiere modificar el nombre
                            //, por eso lo extraemos del nombre buscado y no del propio objeto. Tambien pasamos el objeto con los datos nuevos a modificar
                            boolean exito = dades.modificarAtraccion(nombreBuscado, atraccionModificada);

                            if (exito) {
                                alerta("Atracción modificada con éxito");
                                vaciar();
                            } else {
                                alerta("No se ha podido modificar la atracción");
                            }
                        }
                        }
                        //comprobamos que el valor en altura sea numérico. 
                    } catch (NumberFormatException e) {
                        alerta("La altura mínima tiene que ser con valores numéricos");
                    } catch (IOException | SQLException e) {
                        alerta(e.getMessage());
                    }
                }
            }
        }
    }

    @FXML //metodo para eliminar una atraccion según el valor del nombre, que es clave única
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
                } catch (IOException | SQLException e) {
                    alerta(e.getMessage());
                }
            }
        }
    }

    @FXML //buscamos una atracción por el valor buscado, nombre, ya que es clave única
    public void buscarAtraccion() {

        if (nombreAtraccion.getText().equals("")) {
            alerta("Debe ingresar un nombre de atracción para buscar");
        } else {
            
            try {
                nombreBuscado = nombreAtraccion.getText();
                atraccion = dades.datosAtraccion(nombreBuscado);

                if (atraccion != null) {

                    // Mostramos los datos de la atracción encontrada en los campos correspondientes
                    nombre.setText(atraccion.getNombre());
                    tipo.setValue(atraccion.getTipo());
                    ubicacion.setValue(atraccion.getUbicacion());
                    altura.setText(atraccion.getAlturaminima() + "");
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
        // Agregamos los elementos a los combo box 
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

    //reiniciamos los campos como si volvieramos a recargar la página
    private void reiniciarCampos() {

        nombreAtraccion.setText("");
        nombre.setText("");
        descripcion.setText("");
        altura.setText("");
        tipo.setValue("");
        ubicacion.setValue("");

    }

    // a parte de recargar, si pulsamos el boton limpiar, vaciaremos todos los campos y pondremos los botones de crear habilitados. 
    public void vaciar() {

        reiniciarCampos();
        ordenBotonesHabilitar();
    }

    // es para habilitar los botones de modificar y eliminar, para deshabilitar el de crear. Así hay un control de errores. 
    private void ordenBotonesModificar() {
        agregar.setDisable(true);
        eliminar.setDisable(false);
        modificar.setDisable(false);
        nombreAtraccion.setEditable(false);// ponemos que no se pueda editar ya que cuando modifiquemos los campos, se extraera de aqui por
        //si se quiere tambien modificar el nombre de la atraccion. 
        buscar.setDisable(true);

    }

    // sería al revés de la anterior funcion, habilitamos poder agregar una atraccion, pero dehabilitamos poder modificarla o eliminarla. 
    private void ordenBotonesHabilitar() {
        agregar.setDisable(false);
        eliminar.setDisable(true);
        modificar.setDisable(true);
        nombreAtraccion.setEditable(true);

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
     //metodo para validar los campos según los parametros. 
     private boolean validarLongitudTexto() {

        if (nombreDatos.length() > MAX_CARACTERES_NOMBRE_ATRACCION) {
            alerta("El nombre de la atracción no puede exceder los " + MAX_CARACTERES_NOMBRE_ATRACCION + " caracteres.");
            return false;
        }

        if (descripcionDatos.length() > MAX_CARACTERES_DESCRIPCION) {
            alerta("La descripción no puede exceder los " + MAX_CARACTERES_DESCRIPCION + " caracteres.");
            return false;
        }

        return true;
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
