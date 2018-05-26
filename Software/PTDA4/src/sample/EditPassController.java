package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.connectivity.Driver;

public class EditPassController {

    @FXML
    private Button btn_cancel, btn_savepass;
    @FXML
    private TextField input_newpass;
    @FXML
    private Label lbl_utilizador;

    private String utilizador;
    private Driver driver;

    EditPassController(String utilizador) {
        this.utilizador = utilizador;
    }

    public void initialize(){

        btn_savepass.getStyleClass().addAll("primary");
        btn_cancel.getStyleClass().addAll("danger");

        driver = Main.getDriver();

        lbl_utilizador.setText(utilizador);

        btn_cancel.setOnAction(Main::closeStage);

        btn_savepass.setOnAction(event -> {
            String pass = input_newpass.getText();

            if(pass != null){
                if(driver.resetPassword(utilizador, pass)){
                    Main.sucessNotification("Password alterada com sucesso!");
                    Main.closeStage(event);
                }else
                    Main.errorNotification("Erro ao guardar password!");
            }else {
                Main.errorNotification("Por favor introduza uma password!");
            }
        });
    }
}
