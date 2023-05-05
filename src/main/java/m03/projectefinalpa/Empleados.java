package m03.projectefinalpa;

import javafx.scene.image.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.print.DocFlavor.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import m03.projectefinalpa.model.GestioDadesEmpleados;
import m03.projectefinalpa.model.classes.EmpleadosClass;
import m03.projectefinalpa.model.classes.Horari;

public class Empleados {

    @FXML
    TextField nombreEmpleado;
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

    private String nombreE; // guardar el nom de la persona cercada.
    GestioDadesEmpleados gestioDades = new GestioDadesEmpleados();
    ObservableList<Horari> horaris = FXCollections.observableArrayList(); // guardem els horaris de la consulta
    EmpleadosClass empleado; // guardem les dades de l'empleat de la consulta

    @FXML
    public void buscar() throws SQLException {

        horarios.getItems().clear();
        // comprobem que el camp no estigui buit
        if (!nombreEmpleado.equals("")) {
            // guardem el text del nom
            nombreE = nombreEmpleado.getText();

            // feim les dues consultes amb el nom cercat i retornem un empleat i una llista
            // d'horaris.
            empleado = gestioDades.dadesEmpleat(nombreE);
            horaris = gestioDades.horariPerEmpleat(nombreE);

            // si l'empleat retorna null és que no hi ha empleats amb el nom cercat
            if (empleado != null) {

                // omplim els camps amb les dades de l'empleat
                rellenarCampos(empleado);

                // si té foto assignada, la convertim en una Image i la posem dins el imageView
                if (empleado.getFoto() != null) {
                    InputStream is = empleado.getFoto().getBinaryStream();
                    Image image = new Image(is);
                    foto.setImage(image);

                    // si no té foto, posem una imatge genèrica per defecte.
                } else {
                    Image image1 = new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "imagenes/fotoPersona.png"));
                    foto.setImage(image1);

                }

                // si hi ha horaris associats, els posem dins el listView dels horarios. Si no,
                // posem missatge genèric
                if (horaris != null) {
                    horarios.getItems().addAll(horaris);
                }

                // si no troba l'empleat enviem una alerta
            } else {
                alerta("Empleado no encontrado");
            }
            // si el camp per cercar està buit, enviem una alerta.
        } else {
            alerta("Introduce el nombre de un empleado");
        }

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

}
