package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.connectivity.Driver;
import sample.structs.Alerta;
import sample.structs.Dado;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FisioController {
    @FXML
    private ListView<String> list_pacientes, list_treinos, list_tratamentos, list_trat_tabtreinos;
    @FXML
    private Label lbl_nome, lbl_sexo, lbl_nif, lbl_data_nascimento, lbl_nacionalidade, lbl_morada,
            lbl_localidade, lbl_cc, lbl_contacto, lbl_idpaciente, lbl_emtratamento, lbl_trat_paciente,
            lbl_trat_data_inicio, lbl_trat_data_fim,lbl_trat_medico,lbl_trat_id, lbl_treino_id, lbl_treino_data_inicio,
            lbl_treino_data_fim, lbl_treino_tratamento_associado,lbl_treino_notas,lbl_treino_desc,
            lbl_loggeduser, lbl_email, lbl_cod_postal, lbl_treino_fisio, lbl_press_alta_min, lbl_press_alta_max,
            lbl_press_baixa_min, lbl_press_baixa_max, lbl_freq_min, lbl_freq_max, lbl_emTreino;
    @FXML
    MenuItem menu_receit_trat, menu_sair, menu_logout, menu_sobre;
    @FXML
    private StackedAreaChart<String, Double> chart_freq, chart_pressao;
    @FXML
    private Button btn_treino_receitar, btn_treino_edit, btn_treino_concluir, btn_logout;
    @FXML
    private ImageView img_loggeduser;
    @FXML
    private MenuBar menubar;
    @FXML
    private TextArea textarea_trat_desc, textarea_trat_notas, textarea_treino_desc, textarea_treino_notas;
    @FXML
    private TableView<Alerta> table_alertas;
    @FXML
    private TableView<Dado> table_dados;
    @FXML
    private TableColumn<Alerta, String> column_idalerta;
    @FXML
    private TableColumn<Alerta, String> column_tipo;
    @FXML
    private TableColumn<Alerta, String> column_data;
    @FXML
    private TableColumn<Alerta, String> column_hora;

    @FXML
    private TableColumn<Dado, String> column_dados_data, column_dados_hora, column_dados_pressmax, column_dados_pressmin,
            column_dados_freq;

    @FXML
    private CategoryAxis xAxisPress, xAxisFreq ;

    @FXML
    private NumberAxis yAxisPress, yAxisFreq;


    private String nif;
    private String pass;
    private Driver driver;
    private String idPaciente;

    private ResultSet dadosPaciente;


    FisioController(String nif, String pass) {
        this.nif = nif;
        this.pass = pass;
    }

    @FXML
    public void initialize(){

        driver = Main.getDriver();

        //Nif do fisio
        lbl_loggeduser.setText(String.valueOf(nif));

        img_loggeduser.setOnMouseClicked(event -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/func_info.fxml"));
            Stage stage = new Stage();

            LoggedUserController loggedUserController = new LoggedUserController(nif);
            fxmlLoader.setController(loggedUserController);

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

            stage.setTitle("Dados do funcionário");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        });

        //Tratamentos tab
        btn_treino_edit.getStyleClass().addAll("primary");
        btn_treino_receitar.getStyleClass().addAll("primary");
        btn_treino_concluir.getStyleClass().addAll("primary");

        btn_treino_receitar.setOnAction(event -> receitarTreino());

        menu_receit_trat.setOnAction(event -> receitarTreino());

        btn_treino_edit.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/editTreino.fxml"));
            Stage stage = new Stage();

            EditTreinoController editTreinoController = new EditTreinoController(lbl_treino_id.getText(),
                    lbl_treino_data_inicio.getText(), lbl_treino_data_fim.getText(), textarea_treino_desc.getText(),
                    textarea_treino_notas.getText(), lbl_treino_tratamento_associado.getText(), idPaciente);
            fxmlLoader.setController(editTreinoController);

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

            stage.setTitle("Editar treino");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });


        btn_treino_concluir.setOnAction(event -> {
            driver.finalizarTreino(idPaciente);
            Main.sucessNotification("Treino concluido com sucesso!");
        });

        //Info tab
        menu_sair.setOnAction(e -> {
            Stage stage  = (Stage) menubar.getScene().getWindow();
            stage.close();
        });

        menu_logout.setOnAction(e -> Main.logout());

        ObservableList<String> lista_pacientes = FXCollections.observableArrayList(Main.getPacientesList("paciente"));
        list_pacientes.setItems(lista_pacientes);

        list_pacientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            String idOnList = newValue.substring(0,2).trim();

            ResultSet dados_paciente = driver.dadosPaciente(idOnList);

            try{
                dados_paciente.next();
                lbl_nif.setText(dados_paciente.getString("nif"));
                lbl_nome.setText(dados_paciente.getString("nome"));
                lbl_cc.setText(dados_paciente.getString("cc"));
                lbl_contacto.setText(dados_paciente.getString("contacto"));
                lbl_data_nascimento.setText(dados_paciente.getString("data_nascimento"));
                lbl_localidade.setText(dados_paciente.getString("localidade"));
                lbl_morada.setText(dados_paciente.getString("morada"));
                lbl_nacionalidade.setText(dados_paciente.getString("nacionalidade"));
                idPaciente = dados_paciente.getString("id_paciente");
                lbl_idpaciente.setText(idPaciente);
                if(dados_paciente.getString("em_tratamento").equals("s"))
                    lbl_emtratamento.setText("Sim");
                else
                    lbl_emtratamento.setText("Não");
                if(dados_paciente.getString("em_treino").equals("s"))
                    lbl_emTreino.setText("Sim");
                else
                    lbl_emTreino.setText("Não");
                if(dados_paciente.getString("sexo").equals("m"))
                    lbl_sexo.setText("Masculino");
                else
                    lbl_sexo.setText("Feminino");
                lbl_email.setText(dados_paciente.getString("mail"));
                lbl_cod_postal.setText(dados_paciente.getString("cod_postal"));

            }catch (Exception e){
                e.printStackTrace();
            }

            ArrayList<String> tratamentosPaciente = Main.getTratamentosList(idPaciente, true);
            if(tratamentosPaciente != null) {
                list_tratamentos.setItems(FXCollections.observableArrayList(tratamentosPaciente));
                list_trat_tabtreinos.setItems(FXCollections.observableArrayList(Main.getTratamentosList(idPaciente, false)));
            }

            if(list_trat_tabtreinos.getSelectionModel().getSelectedItems().isEmpty())
                list_treinos.getItems().clear();



            //TODO change funcion
            if(Main.getDriver().getTreinoEstado(idPaciente).equals("s")){

                btn_treino_concluir.setDisable(false);

                btn_treino_concluir.setOnAction(event -> {
                    if(Main.getDriver().finalizarTreino(idPaciente)){
                        Main.sucessNotification("Treino concluido com sucesso!");
                        btn_treino_concluir.setDisable(true);
                    }else
                        Main.errorNotification("Erro ao concluir o treino!");
                });

            }else {
                btn_treino_concluir.setDisable(true);
            }

            ResultSet limites_paciente = driver.getLimites(idPaciente);
            if(limites_paciente != null){
                try {
                    limites_paciente.next();
                    lbl_freq_max.setText(limites_paciente.getString("max_freq_cardiaca"));
                    lbl_freq_min.setText(limites_paciente.getString("min_freq_cardiaca"));
                    lbl_press_baixa_max.setText(limites_paciente.getString("min_pressao_arterial_max"));
                    lbl_press_baixa_min.setText(limites_paciente.getString("min_pressao_arterial_min"));
                    lbl_press_alta_max.setText(limites_paciente.getString("max_pressao_arterial_max"));
                    lbl_press_alta_min.setText(limites_paciente.getString("max_pressao_arterial_min"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //Graficos TAB

            setDadosTable();
            setAlertasData();
            setGraphicsData();

            System.out.println("Selected item: " + newValue);
        });

        list_tratamentos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String idTrat = newValue.substring(5, 7).trim();
                System.out.println(idTrat);

                ResultSet tratamentos_paciente = driver.getDadosTratamento(idTrat);

                if(tratamentos_paciente != null) {
                    try {
                        tratamentos_paciente.next();
                        lbl_trat_id.setText(idTrat);
                        lbl_trat_paciente.setText(tratamentos_paciente.getString("paciente"));
                        lbl_trat_medico.setText(tratamentos_paciente.getString("medico"));
                        textarea_trat_notas.setText(tratamentos_paciente.getString("notas_tratamento"));
                        textarea_trat_desc.setText(tratamentos_paciente.getString("desc_tratamento"));
                        lbl_trat_data_inicio.setText(tratamentos_paciente.getString("data_inicio"));
                        lbl_trat_data_fim.setText(tratamentos_paciente.getString("data_fim"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        list_treinos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, idTreino) -> {
            if(idTreino != null) {

                ResultSet treinosDoTratamento = Main.getDriver().getTreinos(list_trat_tabtreinos.
                        getSelectionModel().getSelectedItem());
                if (treinosDoTratamento != null) {
                    try {
                        treinosDoTratamento.next();
                        lbl_treino_id.setText(idTreino);
                        lbl_treino_tratamento_associado.setText(treinosDoTratamento.getString("tratamento"));
                        lbl_treino_fisio.setText(treinosDoTratamento.getString("fisioterapeuta"));

                        textarea_treino_desc.setText(treinosDoTratamento.getString("desc_treino"));
                        textarea_treino_notas.setText(treinosDoTratamento.getString("notas_treino"));
                        lbl_treino_data_inicio.setText(treinosDoTratamento.getString("data_inicio"));
                        lbl_treino_data_fim.setText(treinosDoTratamento.getString("data_fim"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        list_trat_tabtreinos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){

                //Populate lista treinos
                ArrayList<String> treinosPaciente = Main.getTreinosList(newValue, false);
                if(treinosPaciente != null)
                    list_treinos.setItems(FXCollections.observableArrayList(treinosPaciente));
            }
        });

        list_tratamentos.setPlaceholder(new Label("Sem tratamentos"));
        list_trat_tabtreinos.setPlaceholder(new Label("Vazio"));
        list_treinos.setPlaceholder(new Label("Vazio"));
        table_alertas.setPlaceholder(new Label("Não existem alertas para este paciente"));
        table_dados.setPlaceholder(new Label("Não existem dados para este paciente"));
        list_pacientes.setPlaceholder(new Label("Sem pacientes no sistema"));

        column_idalerta.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        column_tipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));
        column_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        column_hora.setCellValueFactory(
                new PropertyValueFactory<>("hora"));

        column_dados_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        column_dados_hora.setCellValueFactory(
                new PropertyValueFactory<>("hora"));
        column_dados_pressmax.setCellValueFactory(
                new PropertyValueFactory<>("pressMax"));
        column_dados_pressmin.setCellValueFactory(
                new PropertyValueFactory<>("pressMin"));
        column_dados_freq.setCellValueFactory(
                new PropertyValueFactory<>("freqCard"));


        yAxisFreq.setAutoRanging(false);
        yAxisFreq.setLowerBound(40);
        yAxisFreq.setUpperBound(120);

        yAxisPress.setAutoRanging(false);
        yAxisPress.setUpperBound(160);
        yAxisPress.setLowerBound(30);

        btn_logout.setOnAction(event -> Main.logout());
    }

    private void receitarTreino(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/addTreino.fxml"));
        Stage stage = new Stage();

        AddTreinoController addTreinoController = new AddTreinoController(nif, idPaciente);
        fxmlLoader.setController(addTreinoController);

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

        stage.setTitle("Adicionar treino");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void setGraphicsData(){
        //Graficos tab

        dadosPaciente = driver.getDados(idPaciente);

        chart_pressao.getData().clear();
        chart_freq.getData().clear();

        XYChart.Series<String, Double> freq_cardiaca = new XYChart.Series<>();
        freq_cardiaca.setName("Frequência cardiaca (bpm)");

        XYChart.Series<String, Double> pressao_min = new XYChart.Series<>();
        pressao_min.setName("Pressão arterial minima (mmHg)");

        XYChart.Series<String, Double> pressao_max = new XYChart.Series<>();
        pressao_max.setName("Pressão arterial máxima (mmHg)");

        if(dadosPaciente != null) {
            try {
                while (dadosPaciente.next()) {

                    String dataHora = dadosPaciente.getString("data") +
                            " " + dadosPaciente.getString("hora");

                    pressao_min.getData().add(new XYChart.Data(dataHora, Integer.parseInt(dadosPaciente.getString("pressao_arterial_min"))));
                    pressao_max.getData().add(new XYChart.Data(dataHora, Integer.parseInt(dadosPaciente.getString("pressao_arterial_max"))));
                    freq_cardiaca.getData().add(new XYChart.Data(dataHora, Integer.parseInt(dadosPaciente.getString("freq_cardiaca"))));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            chart_freq.getData().addAll(freq_cardiaca);
            chart_pressao.getData().addAll(pressao_min);
            chart_pressao.getData().addAll(pressao_max);
        }
    }

    private void setAlertasData(){

        ResultSet getAlertas = driver.getAlertas(idPaciente);
        final ObservableList<Alerta> data = FXCollections.observableArrayList();
        if(getAlertas != null){
            try {
                while (getAlertas.next()){
                    data.add(new Alerta(getAlertas.getString("id"), getAlertas.getString("tipo"),
                            getAlertas.getString("data"),getAlertas.getString("hora")));
                }

                table_alertas.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDadosTable(){

        final ObservableList<Dado> data = FXCollections.observableArrayList();
        dadosPaciente = driver.getDados(idPaciente);
        if(dadosPaciente != null){
            try {
                while (dadosPaciente.next()){
                    data.add(new Dado(dadosPaciente.getString("data"), dadosPaciente.getString("hora"),
                            dadosPaciente.getString("pressao_arterial_max"),
                            dadosPaciente.getString("pressao_arterial_min"), dadosPaciente.getString("freq_cardiaca")));
                }

                table_dados.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
