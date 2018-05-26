package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddFuncController {

    @FXML
    private DatePicker datePicker, date_admissao;
    @FXML
    private Button btn_cancel, btn_addfunc;
    @FXML
    private TextField input_nif, input_nome, input_nacionalidade, input_codpostal,
            input_localidade, input_cc, input_tele, input_rua, input_email;
    @FXML
    private PasswordField input_pass;
    @FXML
    private ComboBox combo_sexo, combo_funcao;

    private AdminController adminController;

    public AddFuncController(AdminController adminController) {
        this.adminController = adminController;
    }

    public void initialize(){

        btn_cancel.getStyleClass().addAll("danger");
        btn_addfunc.getStyleClass().addAll("primary");

        input_nif.setTextFormatter( new TextFormatter<>(c ->
        {
            if (c.getControlNewText().isEmpty())
                return c;

            if(c.getControlNewText().matches("^[0-9]{1,9}$"))
                return c;

            return null;
        }));

        input_cc.setTextFormatter( new TextFormatter<>(c ->
        {
            if (c.getControlNewText().isEmpty())
                return c;

            if(c.getControlNewText().matches("^[0-9]{1,12}$"))
                return c;

            return null;
        }));

        input_tele.setTextFormatter( new TextFormatter<>(c ->
        {
            if (c.getControlNewText().isEmpty())
                return c;

            if(c.getControlNewText().matches("^[0-9]{1,9}$"))
                return c;

            return null;
        }));

        datePicker.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        date_admissao.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        btn_cancel.setOnAction(Main::closeStage);

        btn_addfunc.setOnAction(event -> {

            String nome = input_nome.getText();
            String sexo = "";
            if(combo_sexo.getSelectionModel().getSelectedItem() != null) {
                if(combo_sexo.getSelectionModel().getSelectedItem().toString().equals("Masculino"))
                    sexo = "m";
                else
                    sexo = "f";
            }
            String data_nasc = "";
            if(datePicker.getValue() != null)
                data_nasc = Main.normalizeDate(datePicker.getValue().toString());
            String data_admissao = "";
            if(date_admissao.getValue() != null)
                data_admissao = Main.normalizeDate(date_admissao.getValue().toString());
            String nif = input_nif.getText();
            String nacionalidade = input_nacionalidade.getText();
            String rua = input_rua.getText();
            String codigoPostal = input_codpostal.getText();
            String localidade = input_localidade.getText();
            String cc = input_cc.getText();
            String tele = input_tele.getText();
            String email = input_email.getText();
            String password = input_pass.getText();
            String funcao = "";
            if(combo_funcao.getSelectionModel().getSelectedItem() != null) {
                if(combo_funcao.getSelectionModel().getSelectedItem().toString().equals("Fisioterapeuta"))
                    funcao = "fisioterapeuta";
                else
                    funcao = "medico";
            }

            if(nome.isEmpty() || sexo.isEmpty() || data_nasc.isEmpty() || nif.isEmpty() || nacionalidade.isEmpty() ||
                    rua.isEmpty() || codigoPostal.isEmpty() || localidade.isEmpty() ||  cc.isEmpty() ||
                    tele.isEmpty() || email.isEmpty() || password.isEmpty() || funcao.isEmpty() || data_admissao.isEmpty()){

                Main.errorNotification("Por favor preencha todos os campos!");

            }else {
                if(nif.length() == 9 && cc.length() == 12 && tele.length() == 9 && email.contains("@")) {

                    if (Main.getDriver().addFunc(data_admissao, "s", password, nome, rua, codigoPostal, localidade,
                            nacionalidade, nif, cc, sexo, data_nasc, tele, email, funcao)) {

                        Main.sucessNotification("Funcionário adicionado com sucesso!");
                        adminController.refreshList();
                        Main.closeStage(event);

                    } else
                        Main.errorNotification("Erro ao adicionar funcionário!");
                }else
                    Main.errorNotification("Por favor verifique o email e número de digitos\nminimos do NIF, CC e contacto\n");
            }
        });
    }
}
