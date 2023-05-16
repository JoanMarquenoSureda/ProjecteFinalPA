package m03.projectefinalpa;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import m03.projectefinalpa.model.Connexio;


public class Inici {

    @FXML
    private TextField usuarioT;
    @FXML
    private PasswordField password;

    private Connection conexion;

    private Connexio connexio;
    private String usuariText;
    private String passwordText;
 

    @FXML // hacemos un metodo para comprobar que el texto y el password sean compatibles con el usuario y que se pueda hacer una conexion real con la base de datos. 
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
    @FXML// metodo que envia a la web de ayuda para la aplicacion
    public void mandarAyuda()  {
        File file = new File("src\\main\\resources\\m03\\projectefinalpa\\web\\inici.html");
        try {
            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e) {
        }
    }
}
