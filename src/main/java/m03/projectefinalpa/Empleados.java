package m03.projectefinalpa;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import m03.projectefinalpa.model.Connexio;
import m03.projectefinalpa.model.GestioDades;
import m03.projectefinalpa.model.GestioDadesEmpleados;
import m03.projectefinalpa.model.Usuario;
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

    private String nombreE;
    GestioDadesEmpleados gestioDades = new GestioDadesEmpleados();
    ObservableList<Horari> horaris = FXCollections.observableArrayList();
    EmpleadosClass empleado;

    @FXML
    public void buscar() throws SQLException {
        
        horarios.getItems().clear();

        if (!nombreEmpleado.equals("")) {
            nombreE = nombreEmpleado.getText();
            empleado = gestioDades.dadesEmpleat(nombreE);
            horaris = gestioDades.horariPerEmpleat(nombreE);

            if (empleado != null) {
                
                rellenarCampos(empleado);
                
                
                if (empleado.getFoto() != null) {
                    InputStream is = empleado.getFoto().getBinaryStream();
                Image image = new Image(is);
                ImageView imageView = new ImageView(image);
                foto.setImage(image);
                    
                } else {
                    Image image1 = new Image(getClass().getResourceAsStream("imagenes/fotoPersona.png"));
                   
                    foto.setImage(image1);
                }
                

                if (horaris != null) {
                    horarios.getItems().addAll(horaris);
                }

            } else {
                alerta("Empleado no encontrado");
            }

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

   
     
    private void rellenarCampos(EmpleadosClass empleado) {

        nombre.setText(empleado.getNombre());
        direccio.setText(empleado.getDireccion());
        telefono.setText(empleado.getTelefono());
        correo.setText(empleado.getEmail());
    }
    
     @FXML private void cambiarPantallaCrear() throws IOException {
      App.setRoot("Crear"); }
     
      @FXML private void cambiarPantallaAsignar() throws IOException {
      App.setRoot("Asignar"); }
      
      @FXML private void cambiarPantallaVisualizar() throws IOException {
      App.setRoot("Visualizar"); }
      
     
     

}
