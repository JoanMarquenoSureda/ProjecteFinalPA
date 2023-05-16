package m03.projectefinalpa;

import javafx.scene.image.Image;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import m03.projectefinalpa.model.GestioDadesEmpleados;
import m03.projectefinalpa.model.classes.EmpleadosClass;
import m03.projectefinalpa.model.classes.Horari;

public class Empleados {

    @FXML
    TextField dniEmpleado;
    @FXML
    TextField nombre;
    @FXML
    TextField direccio;
    @FXML
    TextField telefono;
    @FXML
    TextField correo;
    @FXML
    ListView horariosView;
    @FXML
    ImageView foto;

    private String dni; // guardar el dni de la persona buscada
    GestioDadesEmpleados gestioDades = new GestioDadesEmpleados();
    ObservableList<Horari> listaHorarios = FXCollections.observableArrayList(); // guardamos los datos de los horarios de la consulta
    EmpleadosClass empleado; // guardamos los datos el empleado buscado o enviado

    //metodo para buscar un empleado
    @FXML
    public void buscar() throws SQLException {

        // la seleccion de filas podrá ser múltiple
        horariosView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // comprobamos que el campo dni no esté vacío
        if (!dniEmpleado.getText().equals("")) {
            // guardamos el texto del dni
            dni = dniEmpleado.getText();
            // enviamos el dni a la gestion de datos de empleados y devolvemos el objeto del empleado
            empleado = gestioDades.dadesEmpleat(dni);
            // si el empleado es nul, no existe
            if (empleado != null) {

                // con esta funciona, llenamos los campos con sus datos en la vista
                rellenarCampos(empleado);
                //cargamos tambien sus horarios
                CargarHorarios();
                // si tiene foto también la ponemos en el imageView
                if (empleado.getFotoImage() != null) {

                    foto.setImage(empleado.getFotoImage());

                    // Ajustem la imagen 
                    foto.setPreserveRatio(true);
                    foto.setFitWidth(161);

                    // si no tiene foto, ponemos una standard con el formato d ela vista
                } else {
                    Image image1 = new Image(getClass().getResourceAsStream("imagenes/fotoPersona.png"));
                    foto.setImage(image1);
                }

                // enviamos alerta de no encontrado
            } else {
                alerta("Empleado no encontrado");
                borrarCampos();
            }
            // si el campo dni està vacío, enviamos una alerta.
        } else {
            alerta("Introduce el nombre de un empleado");

        }

    }
    

    @FXML
    public void eliminarAsignacion() {
        boolean exito = false;
        // Obtenemos el índice del elemento seleccionado en el ListView
        int index = horariosView.getSelectionModel().getSelectedIndex();

        if (index >= 0) { // Si se ha seleccionado un elemento
            try {
                // Obtenemos los elementos seleccionado del ListView
                listaHorarios = horariosView.getSelectionModel().getSelectedItems();

                //recorremos la lista para ir eliminado uno por uno
                for (Horari horarioObjeto : listaHorarios) {

                    exito = gestioDades.eliminarAsociacionEmpleado(horarioObjeto, empleado);
                }

                if (exito) {
                    alerta("Asignación eliminada");
                    CargarHorarios();

                } else {
                    alerta("No se pudo eliminar la asignación");
                }
            } catch (IOException | SQLException e) {
                alerta(e.getMessage());
            }

        } else {
            alerta("Ningún horario seleccionado.");
        }
    }
    
     public void initialize() {
        
         horariosView.setDisable(true); //hacer inaccesible el listview

    }

    
    private void CargarHorarios() {

        horariosView.getItems().clear(); //limpiamos la lista de los horarios.

        //volvemos a buscar por el dni los horarios dentro de la conexión de mysql
        listaHorarios = gestioDades.horariPerEmpleat(dni);

        // si hay horarios asociados, los ponemos dentro el listView de los horarios. 
        if (!listaHorarios.isEmpty()) {
            horariosView.getItems().addAll(listaHorarios);
            horariosView.setDisable(false);
        } else {
     
            horariosView.getItems().add("Sin horarios asignados");
            horariosView.setDisable(true);
        }

    }

    // en caso de que no haya datos, reiniciamos los campos por defecto.
    private void borrarCampos() {
        nombre.setText("");
        telefono.setText("");
        direccio.setText("");
        correo.setText("");
        Image image1 = new Image(getClass().getResourceAsStream("imagenes/fotoPersona.png"));
        foto.setImage(image1);
        horariosView.getItems().clear();

    }

    private void alerta(String text) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Informació");
        alerta.setContentText(text);
        alerta.show();
    }

    // mètode per rellenar els camps amb les dades de l'empleat.
    private void rellenarCampos(EmpleadosClass empleado) {

        nombre.setText(empleado.getNombre());
        direccio.setText(empleado.getDireccion());
        telefono.setText(empleado.getTelefono());
        correo.setText(empleado.getEmail());
    }

    @FXML
    public void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
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
    public void cambiarPantallaEditar() throws IOException {
        App.setRoot("Ingresar_Datos");
    }

    

}
