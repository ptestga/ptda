package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.connectivity.Driver;
import sample.structs.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogsController {

    @FXML
    private Button btn_closelogs;

    @FXML
    private TableView<LogsFunc> tbl_log_func;
    @FXML
    private TableView<LogsPaciente> tbl_log_paciente;
    @FXML
    private TableView<LogsPacienteAlertas> tbl_log_alertas;
    @FXML
    private TableView<LogsTratamento> tbl_log_tratamentos;
    @FXML
    private TableView<LogsTreino> tbl_log_treinos;
    @FXML
    private TableView<LogsUtilizador> tbl_log_utilizador;

    @FXML
    private TableColumn<Object, Object> func_id, func_tipo, func_data, func_utilizador, func_id_func, func_id_utili, func_data_admissao,
            func_ativo;

    @FXML
    private TableColumn<Object, Object> pac_id, pac_tipo, pac_data, pac_utilizador, pac_id_paciente, pac_id_utili, pac_emtratamento,
            pac_emtreino;
    @FXML
    private TableColumn<Object, Object> alertas_id, alertas_tipo, alertas_data, alertas_utilizador, alertas_idpaciente,
            alertas_minpressmin, alertas_minpressmax, alertas_maxpressmin, alertas_maxpressmax,
            alertas_freqmin, alertas_freqmax;

    @FXML
    private TableColumn<Object, Object> trat_id, trat_tipo, trat_data, trat_utilizador, trat_idtrat,
            trat_desc, trat_notas, trat_datainicio, trat_datafim, trat_paciente, trat_medico;

    @FXML
    private TableColumn<Object, Object> treino_id, treino_tipo, treino_data, treino_utilizador, treino_idtreino,
            treino_desc, treino_notas, treino_datainicio, treino_datafim, treino_idfisio, treino_trat;

    @FXML
    private TableColumn<Object, Object> utilizador_id, utilizador_tipo, utilizador_data, utilizador_utilizador,
            utilizador_idutilizador, utilizador_password, utilizador_nome, utilizador_morada,
            utilizador_cod_postal, utilizador_localidade, utilizador_nacionalidade, utilizador_nif, utilizador_cc,
            utilizador_sexo, utilizador_data_nascimento, utilizador_contacto, utilizador_mail, utilizador_funcao;

    private Driver driver;

    public void initialize(){

        btn_closelogs.setOnAction(Main::closeStage);
        driver = Main.getDriver();

        tbl_log_func.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        setDataFunc();
        setDataPaciente();
        setDataPacienteAlertas();
        setDataTratamento();
        setDataTreino();
        setDataUtilizador();


        tbl_log_utilizador.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tbl_log_utilizador.setTableMenuButtonVisible(true);
        //tbl_log_utilizador.setColumnResizePolicy((param) -> true );
    }

    private void setDataFunc(){
        func_id.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        func_tipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));
        func_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        func_utilizador.setCellValueFactory(
                new PropertyValueFactory<>("utilizador"));
        func_id_func.setCellValueFactory(
                new PropertyValueFactory<>("id_funcionario"));
        func_id_utili.setCellValueFactory(
                new PropertyValueFactory<>("id_utilizador"));
        func_data_admissao.setCellValueFactory(
                new PropertyValueFactory<>("data_admissao"));
        func_ativo.setCellValueFactory(
                new PropertyValueFactory<>("ativo"));

        ResultSet resultSet = driver.getLogsFuncionario();
        final ObservableList<LogsFunc> data = FXCollections.observableArrayList();

        if(resultSet != null){
            try {
                while (resultSet.next()){
                    data.add(new LogsFunc(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)
                            , resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)
                            , resultSet.getString(7), resultSet.getString(8)));

                }
                tbl_log_func.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataPaciente(){

        pac_id.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        pac_tipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));
        pac_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        pac_utilizador.setCellValueFactory(
                new PropertyValueFactory<>("utilizador"));
        pac_id_paciente.setCellValueFactory(
                new PropertyValueFactory<>("id_paciente"));
        pac_id_utili.setCellValueFactory(
                new PropertyValueFactory<>("id_utilizador"));
        pac_emtratamento.setCellValueFactory(
                new PropertyValueFactory<>("em_tratamento"));
        pac_emtreino.setCellValueFactory(
                new PropertyValueFactory<>("em_treino"));

        ResultSet resultSet = driver.getLogsPaciente();
        final ObservableList<LogsPaciente> data = FXCollections.observableArrayList();

        if(resultSet != null){
            try {
                while (resultSet.next()){
                    data.add(new LogsPaciente(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)
                            , resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)
                            , resultSet.getString(7), resultSet.getString(8)));

                }
                tbl_log_paciente.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataPacienteAlertas(){
        alertas_id.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        alertas_tipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));
        alertas_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        alertas_utilizador.setCellValueFactory(
                new PropertyValueFactory<>("utilizador"));
        alertas_idpaciente.setCellValueFactory(
                new PropertyValueFactory<>("id_paciente"));
        alertas_minpressmin.setCellValueFactory(
                new PropertyValueFactory<>("min_press_min"));
        alertas_minpressmax.setCellValueFactory(
                new PropertyValueFactory<>("max_press_min"));
        alertas_maxpressmin.setCellValueFactory(
                new PropertyValueFactory<>("min_press_max"));
        alertas_maxpressmax.setCellValueFactory(
                new PropertyValueFactory<>("max_press_max"));
        alertas_freqmin.setCellValueFactory(
                new PropertyValueFactory<>("freq_min"));
        alertas_freqmax.setCellValueFactory(
                new PropertyValueFactory<>("freq_max"));

        ResultSet resultSet = driver.getLogsPacienteAlertas();
        final ObservableList<LogsPacienteAlertas> data = FXCollections.observableArrayList();

        if(resultSet != null){
            try {
                while (resultSet.next()){
                    data.add(new LogsPacienteAlertas(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)
                            , resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)
                            , resultSet.getString(7), resultSet.getString(8),
                            resultSet.getString(9 ), resultSet.getString(10),resultSet.getString(11)));

                }
                tbl_log_alertas.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    private void setDataTratamento(){
        trat_id.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        trat_tipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));
        trat_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        trat_utilizador.setCellValueFactory(
                new PropertyValueFactory<>("utilizador"));
        trat_idtrat.setCellValueFactory(
                new PropertyValueFactory<>("id_tratamento"));
        trat_desc.setCellValueFactory(
                new PropertyValueFactory<>("desc_tratamento"));
        trat_notas.setCellValueFactory(
                new PropertyValueFactory<>("notas_tratamento"));
        trat_datainicio.setCellValueFactory(
                new PropertyValueFactory<>("data_inicio"));
        trat_datafim.setCellValueFactory(
                new PropertyValueFactory<>("data_fim"));
        trat_paciente.setCellValueFactory(
                new PropertyValueFactory<>("paciente"));
        trat_medico.setCellValueFactory(
                new PropertyValueFactory<>("medico"));

        ResultSet resultSet = driver.getLogsTratamento();
        final ObservableList<LogsTratamento> data = FXCollections.observableArrayList();

        if(resultSet != null){
            try {
                while (resultSet.next()){
                    data.add(new LogsTratamento(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)
                            , resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)
                            , resultSet.getString(7), resultSet.getString(8),
                            resultSet.getString(9 ), resultSet.getString(10),resultSet.getString(11)));

                }
                tbl_log_tratamentos.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    private void setDataTreino(){
        treino_id.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        treino_tipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));
        treino_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        treino_utilizador.setCellValueFactory(
                new PropertyValueFactory<>("utilizador"));
        treino_idtreino.setCellValueFactory(
                new PropertyValueFactory<>("id_treino"));
        treino_desc.setCellValueFactory(
                new PropertyValueFactory<>("desc_treino"));
        treino_notas.setCellValueFactory(
                new PropertyValueFactory<>("notas_treino"));
        treino_datainicio.setCellValueFactory(
                new PropertyValueFactory<>("data_inicio"));
        treino_datafim.setCellValueFactory(
                new PropertyValueFactory<>("data_fim"));
        treino_idfisio.setCellValueFactory(
                new PropertyValueFactory<>("fisioterapeuta"));
        treino_trat.setCellValueFactory(
                new PropertyValueFactory<>("tratamento"));

        ResultSet resultSet = driver.getLogsTreino();
        final ObservableList<LogsTreino> data = FXCollections.observableArrayList();

        if(resultSet != null){
            try {
                while (resultSet.next()){
                    data.add(new LogsTreino(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)
                            , resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)
                            , resultSet.getString(7), resultSet.getString(8),
                            resultSet.getString(9 ), resultSet.getString(10),resultSet.getString(11)));

                }
                tbl_log_treinos.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    private void setDataUtilizador(){
        utilizador_id.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        utilizador_tipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));
        utilizador_data.setCellValueFactory(
                new PropertyValueFactory<>("data"));
        utilizador_utilizador.setCellValueFactory(
                new PropertyValueFactory<>("utilizador"));
        utilizador_idutilizador.setCellValueFactory(
                new PropertyValueFactory<>("id_utilizador"));
        utilizador_password.setCellValueFactory(
                new PropertyValueFactory<>("password"));
        utilizador_nome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));
        utilizador_morada.setCellValueFactory(
                new PropertyValueFactory<>("morada"));
        utilizador_cod_postal.setCellValueFactory(
                new PropertyValueFactory<>("cod_postal"));
        utilizador_localidade.setCellValueFactory(
                new PropertyValueFactory<>("localidade"));
        utilizador_nacionalidade.setCellValueFactory(
                new PropertyValueFactory<>("nacionalidade"));
        utilizador_nif.setCellValueFactory(
                new PropertyValueFactory<>("nif"));
        utilizador_cc.setCellValueFactory(
                new PropertyValueFactory<>("cc"));
        utilizador_sexo.setCellValueFactory(
                new PropertyValueFactory<>("sexo"));
        utilizador_data_nascimento.setCellValueFactory(
                new PropertyValueFactory<>("data_nascimento"));
        utilizador_contacto.setCellValueFactory(
                new PropertyValueFactory<>("contacto"));
        utilizador_mail.setCellValueFactory(
                new PropertyValueFactory<>("mail"));
        utilizador_funcao.setCellValueFactory(
                new PropertyValueFactory<>("funcao"));

        ResultSet resultSet = driver.getLogsUtilizador();
        final ObservableList<LogsUtilizador> data = FXCollections.observableArrayList();

        if(resultSet != null){
            try {
                while (resultSet.next()){
                    data.add(new LogsUtilizador(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)
                            , resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)
                            , resultSet.getString(7), resultSet.getString(8),
                            resultSet.getString(9 ), resultSet.getString(10),resultSet.getString(11),resultSet.getString(12)
                            ,resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),resultSet.getString(16)
                            ,resultSet.getString(17),resultSet.getString(18)));

                }
                tbl_log_utilizador.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
