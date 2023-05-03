package m03.projectefinalpa;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import m03.projectefinalpa.model.Connexio;
import m03.projectefinalpa.model.Usuario;

public class Inici {

    @FXML
    private TextField usuarioT;
    @FXML
    private PasswordField password;

    public Connection conexion;

    public Connexio connexio;
    public String usuariText;
    public String passwordText;
    public Usuario usuario;

    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void comprobar() throws IOException {

        if (!usuarioT.getText().equals("") || !password.getText().equals("")) {
            usuariText = usuarioT.getText().trim();
            passwordText = password.getText().trim();

            try {
                connexio = new Connexio(usuariText, passwordText);
                conexion = connexio.connecta();

                if (conexion != null) {

                    App.conexion = conexion;
                    cambiarPantallaCrear();
                } else {
                    alerta("Usuario y Password incorrectos");
                    usuarioT.setText("");
                    password.setText("");
                    usuarioT.requestFocus();
                }
            } catch (IOException ex) {

            }
        } else {
            alerta("Campos vacíos");
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
    private void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
    }

}
