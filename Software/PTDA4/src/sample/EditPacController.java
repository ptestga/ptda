package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditPacController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button btn_cancel, btn_addpacient;
    @FXML
    private TextField input_nif, input_nome, input_nacionalidade, input_codpostal,
            input_localidade, input_cc, input_tele, input_rua, input_email;
    @FXML
    private ComboBox combo_sexo;

    String nif;

    EditPacController(String nif) {
        this.nif = nif;
    }

    public void initialize(){

        btn_addpacient.getStyleClass().addAll("primary");
        btn_cancel.getStyleClass().addAll("danger");

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
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
                    return null;

                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        btn_cancel.setOnAction(Main::closeStage);

        String idPaciente = Main.getDriver().getPacienteID(nif);
        ResultSet pacient_data = Main.getDriver().dadosPaciente(idPaciente);

        try {
            pacient_data.next();
            input_nome.setText(pacient_data.getString("nome"));
            if(pacient_data.getString("sexo").equals("f"))
                combo_sexo.getSelectionModel().select(0);
            else
                combo_sexo.getSelectionModel().select(1);
            datePicker.setValue(LocalDate.parse(pacient_data.getString("data_nascimento")));
            input_nacionalidade.setText(pacient_data.getString("nacionalidade"));
            input_rua.setText(pacient_data.getString("morada"));
            input_codpostal.setText(pacient_data.getString("cod_postal"));
            input_localidade.setText(pacient_data.getString("localidade"));
            input_nif.setText(pacient_data.getString("nif"));
            input_cc.setText(pacient_data.getString("cc"));
            input_tele.setText(pacient_data.getString("contacto"));
            input_email.setText(pacient_data.getString("mail"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btn_addpacient.setOnAction(event -> {

            String nome = input_nome.getText();
            String sexo = "";
            if(combo_sexo.getSelectionModel().getSelectedItem() != null) {
                if(combo_sexo.getSelectionModel().getSelectedItem().toString().equals("Masculino"))
                    sexo = "m";
                else
                    sexo = "f";
            }
            String data = "";
            if(datePicker.getValue() != null)
                data = Main.normalizeDate(datePicker.getValue().toString());

            String nacionalidade = input_nacionalidade.getText();
            String rua = input_rua.getText();
            String codigoPostal = input_codpostal.getText();
            String localidade = input_localidade.getText();
            String tele = input_tele.getText();
            String email = input_email.getText();

            if(nome.isEmpty() || sexo.isEmpty() || data.isEmpty() || nif.isEmpty() || nacionalidade.isEmpty() ||
                    rua.isEmpty() || codigoPostal.isEmpty() || localidade.isEmpty() ||
                    email.isEmpty()){

                Main.errorNotification("Por favor preencha todos os campos!");

            }else {
                if(tele.length() == 9 && email.contains("@")) {

                    if (Main.getDriver().editPaciente(idPaciente, nome, rua, codigoPostal, localidade, nacionalidade,
                            sexo, data, tele, email)) {

                        Main.sucessNotification("Utilizador adicionado com sucess!");
                        Main.closeStage(event);
                    } else {
                        Main.errorNotification("Erro ao adicionar paciente!");
                    }
                }else
                    Main.errorNotification("Por favor verifique o seu número\nde telemóvel e email");
            }
        });
    }
}
