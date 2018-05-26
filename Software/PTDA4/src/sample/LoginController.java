package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField nif_input ;
    @FXML
    private PasswordField password_input;
    @FXML
    private Button btn_login, btn_sair;

    public void initialize(){

        btn_login.getStyleClass().addAll("primary");
        btn_sair.getStyleClass().addAll("danger");

        btn_sair.setOnAction(e -> {
            Node source = (Node) e.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        nif_input.setTextFormatter( new TextFormatter<>(c ->
        {
            if ( c.getControlNewText().isEmpty() )
            {
                return c;
            }

            if(c.getControlNewText().matches("^[0-9]{1,9}$"))
                return c;

            return null;
        }));

        password_input.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER))
                login();
        });

        btn_login.setOnAction(e -> login());
    }

    private void login(){
        String nif = nif_input.getText();
        String pass = password_input.getText();

        if(!nif.isEmpty() && !pass.isEmpty() && nif.length() == 9) {
            Main.doLogin(nif, pass);
            nif_input.clear();
            password_input.clear();
            nif_input.requestFocus();
        }else {
            if(nif.length() < 9)
                Main.errorAlert("Por favor insira um NIF com 9 digitos!");
            else
                Main.errorAlert("Por favor preencha todos os campos!");
        }
    }
}
