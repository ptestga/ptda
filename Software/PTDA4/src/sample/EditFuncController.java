package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditFuncController {

    String nif;

    @FXML
    private DatePicker datePicker, date_admissao;

    @FXML
    private Button btn_cancel, btn_addfunc;

    @FXML
    private TextField input_nif, input_nome, input_nacionalidade, input_codpostal,
            input_localidade, input_cc, input_tele, input_rua, input_email;

    @FXML
    private ComboBox combo_sexo, combo_funcao;

    EditFuncController(String nif) {
        this.nif = nif;
    }

    public void initialize(){

        btn_cancel.getStyleClass().addAll("danger");
        btn_addfunc.getStyleClass().addAll("primary");

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

        ResultSet func_data = Main.getDriver().dadosFuncionario(nif);

        try {
            func_data.next();
            input_nome.setText(func_data.getString("nome"));
            if(func_data.getString("sexo").equals("f"))
                combo_sexo.getSelectionModel().select(0);
            else
                combo_sexo.getSelectionModel().select(1);
            datePicker.setValue(LocalDate.parse(func_data.getString("data_nascimento")));
            date_admissao.setValue(LocalDate.parse(func_data.getString("data_admissao")));
            input_nacionalidade.setText(func_data.getString("nacionalidade"));
            input_rua.setText(func_data.getString("morada"));
            input_codpostal.setText(func_data.getString("cod_postal"));
            input_localidade.setText(func_data.getString("localidade"));
            input_nif.setText(func_data.getString("nif"));
            input_cc.setText(func_data.getString("cc"));
            input_tele.setText(func_data.getString("contacto"));
            input_email.setText(func_data.getString("mail"));
            if(func_data.getString("funcao").equals("medico"))
                combo_funcao.getSelectionModel().select(0);
            else
                combo_funcao.getSelectionModel().select(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

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
            String cartaoCidadao = input_cc.getText();
            String tele = input_tele.getText();
            String email = input_email.getText();
            String funcao = "";
            if(combo_funcao.getSelectionModel().getSelectedItem() != null) {
                if(combo_funcao.getSelectionModel().getSelectedItem().toString().equals("Fisioterapeuta"))
                    funcao = "fisioterapeuta";
                else
                    funcao = "medico";
            }

            if(nome.isEmpty() || sexo.isEmpty() || data_nasc.isEmpty() || nif.isEmpty() || nacionalidade.isEmpty() ||
                    rua.isEmpty() || codigoPostal.isEmpty() || localidade.isEmpty() ||  cartaoCidadao.isEmpty() ||
                    tele.isEmpty() || email.isEmpty() || funcao.isEmpty() || data_admissao.isEmpty()){

                Main.errorNotification("Por favor preencha todos os campos!");

            }else {

                if(Main.getDriver().editFunc(Main.getDriver().getFuncID(nif), nome, rua, codigoPostal,
                        localidade, nacionalidade, sexo, data_nasc, tele, email)){

                    Main.sucessNotification("Funcionário editado com sucesso!");
                    Main.closeStage(event);
                }else
                    Main.errorNotification("Erro ao editar funcionário!");
            }
        });

    }
}
