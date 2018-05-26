package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.Objects;


public class LoggedUserController {

    private String nif;

    @FXML
    private Label lbl_func_nome, lbl_func_cc, lbl_func_contacto, lbl_func_mail, lbl_func_localidade,
            lbl_func_nif, lbl_func_type, lbl_func_id_func;

    @FXML
    private Button btn_func_logout, btn_func_close;

    @FXML
    private ImageView img_func;

    public LoggedUserController(String nif) {
        this.nif = nif;
    }

    public void initialize(){

        btn_func_close.getStyleClass().addAll("primary");

        lbl_func_nif.setText(nif);

        btn_func_close.setOnAction(event -> {
            Node source = (Node) event.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        });

        btn_func_logout.setOnAction(event -> {
            Main.logout();
            Main.closeStage(event);
        });

        lbl_func_nif.setText(nif);

        ResultSet resultSet = Main.getDriver().dadosFuncionario(nif);

        try{
            resultSet.next();
            System.out.println(resultSet.toString());
            lbl_func_type.setText(resultSet.getString("funcao"));

            if(resultSet.getString("funcao").equals("medico"))
                lbl_func_type.setText("Médico");
            else if(resultSet.getString("funcao").equals("fisioterapeuta"))
                lbl_func_type.setText("Fisioterapeuta");
            else
                lbl_func_type.setText("Administrador");
            lbl_func_id_func.setText(resultSet.getString("id_funcionario"));
            lbl_func_nome.setText(resultSet.getString("nome"));
            lbl_func_cc.setText(resultSet.getString("cc"));
            lbl_func_contacto.setText(resultSet.getString("contacto"));
            lbl_func_mail.setText(resultSet.getString("mail"));
            lbl_func_localidade.setText(resultSet.getString("localidade"));

            if(Objects.equals(resultSet.getString("sexo"), "f"))
                img_func.setImage(new Image(getClass().getResourceAsStream("res/img/doctor-female.png")));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
