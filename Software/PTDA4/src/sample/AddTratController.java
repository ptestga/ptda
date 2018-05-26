package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AddTratController {

    @FXML
    private Button btn_trat_save, btn_trat_close;
    @FXML
    private DatePicker date_trat_inicio, date_trat_fim;
    @FXML
    private TextArea text_trat_desc, text_trat_notas;
    @FXML
    private ComboBox<String> combo_trat_pacient;
    @FXML
    private TextField input_press_alta_min, input_press_alta_max, input_press_baixa_min, input_press_baixa_max, input_freq_min,
            input_freq_max;

    private String nif_medico;

    AddTratController(String nif_medico) {
        this.nif_medico = nif_medico;
    }

    public void initialize(){

        btn_trat_close.setOnAction(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        combo_trat_pacient.getItems().addAll(Main.getPacientesList("paciente"));

        final LocalDate[] dateToLimit = {null};

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
                        if (item.isBefore(dateToLimit[0]))
                        {
                            this.setDisable(true);
                        }
                    }
                };
            }
        };

        date_trat_fim.setConverter(new StringConverter<LocalDate>()
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

        date_trat_inicio.setConverter(new StringConverter<LocalDate>()
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

                dateToLimit[0] = LocalDate.parse(dateString,dateTimeFormatter);
                date_trat_fim.setDayCellFactory(dayCellFactory);

                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        btn_trat_save.getStyleClass().addAll("primary");
        btn_trat_save.setOnAction(event -> {

            String id_paciente = "";
            if(combo_trat_pacient.getSelectionModel().getSelectedItem() != null)
                id_paciente = combo_trat_pacient.getSelectionModel().getSelectedItem().substring(0,2).trim();
            String dataInicio = "";
            if(date_trat_inicio.getValue() != null)
                dataInicio = Main.normalizeDate(date_trat_inicio.getValue().toString());
            String dataFim = "";
            if(date_trat_fim.getValue() != null)
                dataFim = Main.normalizeDate(date_trat_fim.getValue().toString());
            String descricao = text_trat_desc.getText();
            String notas = text_trat_notas.getText();

            String press_alta_min = input_press_alta_min.getText();
            String press_alta_max = input_press_alta_max.getText();
            String press_baixa_min = input_press_baixa_min.getText();
            String press_baixa_max = input_press_baixa_max.getText();
            String freq_min = input_freq_min.getText();
            String freq_max = input_freq_max.getText();

            if(id_paciente.isEmpty() || dataInicio.isEmpty() || dataFim.isEmpty() ||
                    descricao.isEmpty() || notas.isEmpty() || press_alta_min.isEmpty() ||
                    press_alta_max.isEmpty() || press_baixa_min.isEmpty() || press_baixa_max.isEmpty() ||
                    freq_min.isEmpty() || freq_max.isEmpty()){

                Main.errorNotification("Por favor preencha todos os campos!");
            }else {

                //Adiciona o tratamento e os limites
                if(Main.getDriver().setLimites(id_paciente,press_baixa_min, press_baixa_max, press_alta_min,
                        press_alta_max, freq_min, freq_max) &&
                        Main.getDriver().addTratamento(descricao, notas, dataInicio, dataFim, id_paciente,
                                Main.getDriver().getFuncID(nif_medico))){

                    Main.sucessNotification("Tratamento adicionado com sucesso!");
                    Main.closeStage(event);
                }else
                    Main.errorNotification("Erro ao adicionar tratamento!");
            }
        });
    }
}
