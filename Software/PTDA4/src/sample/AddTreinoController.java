package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTreinoController {
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

    private String nif_fisio, id_paciente;

    public AddTreinoController(String nif_fisio, String nif_paciente) {

        this.nif_fisio = nif_fisio;
        this.id_paciente = nif_paciente;
    }

    public void initialize(){

        btn_trat_close.setOnAction(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        combo_trat_pacient.getItems().addAll(Main.getTratamentosList(id_paciente, true));

        ResultSet limites_paciente = Main.getDriver().getLimites(id_paciente);
        if(limites_paciente != null){
            try {
                limites_paciente.next();
                input_freq_max.setText(limites_paciente.getString("max_freq_cardiaca"));
                input_freq_min.setText(limites_paciente.getString("min_freq_cardiaca"));
                input_press_baixa_max.setText(limites_paciente.getString("min_pressao_arterial_max"));
                input_press_baixa_min.setText(limites_paciente.getString("min_pressao_arterial_min"));
                input_press_alta_max.setText(limites_paciente.getString("max_pressao_arterial_max"));
                input_press_alta_min.setText(limites_paciente.getString("max_pressao_arterial_min"));

                limites_paciente.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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

            String id_trat = "";
            if(combo_trat_pacient.getSelectionModel().getSelectedItem() != null)
                id_trat = combo_trat_pacient.getSelectionModel().getSelectedItem().substring(5,7).trim();
            String dataInicio = "";
            if(date_trat_inicio.getValue() != null)
                dataInicio = Main.normalizeDate(date_trat_inicio.getValue().toString());
            String dataFim = "";
            if(date_trat_fim.getValue() != null)
                dataFim = Main.normalizeDate(date_trat_fim.getValue().toString());
            String descricao = text_trat_desc.getText();
            String notas = text_trat_notas.getText();

            if(id_trat.isEmpty() || dataInicio.isEmpty() || dataFim.isEmpty() ||
                    descricao.isEmpty() || notas.isEmpty()){

                Main.errorNotification("Por favor preencha todos os campos!");
            }else {

                //Adiciona o treino
                if(Main.getDriver().addTreino(descricao, notas, dataInicio, dataFim, Main.getDriver().getFuncID(nif_fisio),
                        id_trat)){

                    Main.sucessNotification("Treino adicionado com sucesso!");
                    Main.closeStage(event);
                }else
                    Main.errorNotification("Erro ao adicionar treino!");
            }
        });
    }
}
