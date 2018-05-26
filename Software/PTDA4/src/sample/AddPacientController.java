package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddPacientController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button btn_cancel, btn_addpacient, btn_addfunc;

    @FXML
    private TextField input_nif, input_nome, input_nacionalidade, input_codpostal,
            input_localidade, input_cc, input_tele, input_rua, input_email;

    @FXML
    private PasswordField input_pass;

    @FXML
    private ComboBox combo_sexo;

    private AdminController adminController;

    public AddPacientController(AdminController adminController) {
        this.adminController = adminController;
    }

    public void initialize(){

        btn_cancel.getStyleClass().addAll("danger");
        btn_addpacient.getStyleClass().addAll("primary");

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

        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>()
        {
            public DateCell call(final DatePicker datePicker)
            {
                return new DateCell()
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty)
                    {
                        // Must call super
                        super.updateItem(item, empty);

                        // Disable all future date cells
                        if (item.isAfter(LocalDate.now()))
                        {
                            this.setDisable(true);
                        }
                    }
                };
            }
        };

        // Set the day cell factory to the DatePicker
        datePicker.setDayCellFactory(dayCellFactory);

        btn_cancel.setOnAction(Main::closeStage);

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

            String nif = input_nif.getText();
            String nacionalidade = input_nacionalidade.getText();
            String rua = input_rua.getText();
            String codigoPostal = input_codpostal.getText();
            String localidade = input_localidade.getText();
            String cc = input_cc.getText();
            String tele = input_tele.getText();
            String email = input_email.getText();
            String password = input_pass.getText();

            if(nome.isEmpty() || sexo.isEmpty() || data.isEmpty() || nif.isEmpty() || nacionalidade.isEmpty() ||
                    rua.isEmpty() || codigoPostal.isEmpty() || localidade.isEmpty() ||  cc.isEmpty() ||
                    tele.isEmpty() || email.isEmpty() || password.isEmpty()){

                //if(nif.length() < 9 || cartaoCidadao.length() < 12 || tele.length() < 9)

                Main.errorNotification("Por favor preencha todos os campos!");

            }else {

                if(nif.length() == 9 && cc.length() == 12 && tele.length() == 9 && email.contains("@")){

                    if(Main.getDriver().addPac(password, nome, rua, codigoPostal, localidade, nacionalidade, nif, cc,
                            sexo, data, tele, email)){

                        Main.sucessNotification("Utilizador adicionado com sucesso!");
                        adminController.refreshList();
                        Main.closeStage(event);
                    }
                    else{
                        Main.errorNotification("Erro ao adicionar paciente!");
                    }
                }else
                    Main.errorNotification("Por favor verifique o email e número de digitos\nminimos do NIF, CC e contacto\n");
            }
        });
    }
}
