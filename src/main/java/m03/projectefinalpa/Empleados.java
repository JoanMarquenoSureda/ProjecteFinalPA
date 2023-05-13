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
    ListView horarios;
    @FXML
    ImageView foto;

    private String dni; // guardar el dni de la persona cercada.
    GestioDadesEmpleados gestioDades = new GestioDadesEmpleados();
    ObservableList<Horari> horaris = FXCollections.observableArrayList(); // guardem els horaris de la consulta
    EmpleadosClass empleado; // guardem les dades de l'empleat de la consulta

    @FXML
    public void buscar() throws SQLException {
        horarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // comprobem que el camp no estigui buit
        if (!dniEmpleado.getText().equals("")) {
            // guardem el text del dni
            dni = dniEmpleado.getText();
            // retornem l'empleat amb els camps
            empleado = gestioDades.dadesEmpleat(dni);
            // si l'empleat és null és que no hi ha empleats amb el nom cercat
            if (empleado != null) {
                // omplim els camps amb les dades de l'empleat
                rellenarCampos(empleado);
                //carreguem els seus horaris
                CargarHorarios();
                // si té foto assignada la posem dins el imageView
                if (empleado.getFotoImage() != null) {

                    foto.setImage(empleado.getFotoImage());

                    // Ajustem la image al recuadre del imageView
                    foto.setPreserveRatio(true);
                    foto.setFitWidth(161);

                    // si no té foto, posem una imatge genèrica per defecte.
                } else {
                    Image image1 = new Image(getClass().getResourceAsStream("imagenes/fotoPersona.png"));
                    foto.setImage(image1);
                }

                // si no troba l'empleat enviem una alerta
            } else {
                alerta("Empleado no encontrado");
                borrarCampos();
            }
            // si el camp per cercar està buit, enviem una alerta.
        } else {
            alerta("Introduce el nombre de un empleado");

        }

    }

    @FXML
    private void eliminarAsignacion() {
        boolean exito= false;
        // Obtenemos el índice del elemento seleccionado en el ListView
        int index = horarios.getSelectionModel().getSelectedIndex();
        if (index >= 0) { // Si se ha seleccionado un elemento

            try {
                horaris.clear();
                // Obtenemos el elemento seleccionado en el ListView
               horaris = horarios.getSelectionModel().getSelectedItems();

                for (Horari horarioObjeto : horaris) {
                    
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

    // en caso de que no haya datos, reiniciamos los campos por defecto.
    private void borrarCampos() {
        nombre.setText("");
        telefono.setText("");
        direccio.setText("");
        correo.setText("");
        Image image1 = new Image(getClass().getResourceAsStream("imagenes/fotoPersona.png"));
        foto.setImage(image1);
        horarios.getItems().clear();

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
    private void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
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
    private void cambiarPantallaEditar() throws IOException {
        App.setRoot("Ingresar_Datos");
    }

    private void CargarHorarios() {
        horarios.getItems().clear();

        horaris = gestioDades.horariPerEmpleat(dni);
        // si hi ha horaris associats, els posem dins el listView dels horarios. 

        if (horaris != null) {
            horarios.getItems().addAll(horaris);
        }

    }

}
