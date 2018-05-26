package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class EditTratController {

    @FXML
    private Button btn_trat_save, btn_trat_close;

    @FXML
    private DatePicker date_trat_inicio, date_trat_fim;

    @FXML
    private TextArea text_trat_desc, text_trat_notas;

    @FXML
    private ComboBox<String> combo_trat_pacient, combo_trat_medico;

    @FXML
    private TextField input_press_alta_min, input_press_alta_max, input_press_baixa_min, input_press_baixa_max, input_freq_min,
            input_freq_max;

    private String medico, paciente,  dataInicio,  dataFim,
            desc, notas, idTrat;

    EditTratController(String medico, String paciente, String dataInicio, String dataFim, String desc, String notas,
                       String idTrat) {
        this.medico = medico;
        this.paciente = paciente;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.desc = desc;
        this.notas = notas;
        this.idTrat = idTrat;
    }

    private void setDados(){

        date_trat_fim.setValue(LocalDate.parse(dataFim));
        date_trat_inicio.setValue(LocalDate.parse(dataInicio));
        combo_trat_pacient.setValue(paciente);
        text_trat_desc.setText(desc);
        text_trat_notas.setText(notas);
        ResultSet limites_paciente = Main.getDriver().getLimites(paciente);
        if(limites_paciente != null){
            try {
                System.out.println("lol");
                limites_paciente.next();
                input_freq_max.setText(limites_paciente.getString("max_freq_cardiaca"));
                input_freq_min.setText(limites_paciente.getString("min_freq_cardiaca"));
                input_press_baixa_max.setText(limites_paciente.getString("min_pressao_arterial_max"));
                input_press_baixa_min.setText(limites_paciente.getString("min_pressao_arterial_min"));
                input_press_alta_max.setText(limites_paciente.getString("max_pressao_arterial_max"));
                input_press_alta_min.setText(limites_paciente.getString("max_pressao_arterial_min"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize(){

        btn_trat_close.setOnAction(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        date_trat_fim.setConverter(new StringConverter<LocalDate>()
        {
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

        btn_trat_save.getStyleClass().addAll("primary");
        btn_trat_save.setOnAction(event -> {

            String paciente = "";
            if(combo_trat_pacient.getSelectionModel().getSelectedItem() != null)
                paciente = combo_trat_pacient.getSelectionModel().getSelectedItem();
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

            if(paciente.isEmpty() || medico.isEmpty() || dataInicio.isEmpty() || dataFim.isEmpty() ||
                    descricao.isEmpty() || notas.isEmpty() || press_alta_min.isEmpty() ||
                    press_alta_max.isEmpty() || press_baixa_min.isEmpty() || press_baixa_max.isEmpty() ||
                    freq_min.isEmpty() || freq_max.isEmpty()){

                Main.errorNotification("Por favor preencha todos os campos!");
            }else {

                if(Main.getDriver().setLimites(paciente,press_baixa_min, press_baixa_max, press_alta_min,
                        press_alta_max, freq_min, freq_max) &&
                        Main.getDriver().editTratamento(idTrat,descricao,notas,dataInicio,dataFim)){
                    Main.sucessNotification("Tratamento editado com sucesso!");
                    Main.closeStage(event);
                }else
                    Main.errorNotification("Erro ao editar tratamento!");

                Main.closeStage(event);
            }
        });

        setDados();
    }
}
