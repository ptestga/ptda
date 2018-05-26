package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.connectivity.Driver;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Main extends Application {

    private static Stage stage;
    static private Driver driver;
    private static Scene loginScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        Image image = new Image(getClass().getResourceAsStream("res/img/icon.png"));
        stage.getIcons().add(image);

        FXMLLoader fxmlLoaderLogin = new FXMLLoader(getClass().getResource("res/layout/login_screen.fxml"));

        loginScene = new Scene(fxmlLoaderLogin.load());
        loginScene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

        showLoginScreen();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Mostra o ecra de login. Chamado no inicio do programa e quando um utilizador faz logout.
     */
    private static void showLoginScreen() {
        stage.setTitle("Login Screen");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Função que efetua o login e coneção à BD. Lança o stage adequado ao tipo de utilizador que fez login (médico,
     * fisioterapeuta ou admin). Função chamada a partir do ecra de login.
     * Criar um alerta de erro caso os dados de login estajam incorretos
     *
     * @param nif String, Nif introduzido no ecra de login (corresponde ao utilizador que irá realizar login na BD)
     * @param password String, Password do utilizador
     */
    static void doLogin(String nif, String password) {

        Parent root = null;
        driver = new Driver(nif, password);

        if(driver.isConnected()){
            if(Objects.equals(driver.userType(nif), "medico")){

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("res/layout/doc_screen.fxml"));
                DocController docController = new DocController(nif, password);
                fxmlLoader.setController(docController);
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(Objects.equals(driver.userType(nif), "administrador")){

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("res/layout/admin_screen.fxml"));
                AdminController adminController = new AdminController(nif);
                fxmlLoader.setController(adminController);
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(driver.userType(nif).equals("fisioterapeuta")){

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("res/layout/fisio_screen.fxml"));
                FisioController funcController = new FisioController(nif, password);
                fxmlLoader.setController(funcController);
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.setResizable(false);
            stage.show();

        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Os dados introduzidos estão incorretos");
            alert.showAndWait();
        }
    }

    /**
     * Método chamado quando uma janela (Stage) é fechada
     * @throws Exception Lançada quando ocorre um erro ao fechar uma janela (Stage)
     */
    @Override
    public void stop() throws Exception {
        if(driver == null)
            return;
        driver.logout();
    }

    /**
     * Função que fecha coneção á BD recorrendo á classe "Driver". Apenas fecha a ligação se existir uma feita, caso
     * contrário não faz nada. Apresenta uma mensagem positiva ou negativa caso tenho efetuado o logout com sucesso
     * ou não.
     */
    static void logout(){
        if(driver.isConnected()){
            if(driver.logout()){
                sucessNotification("Logout efetuado com sucesso!");
                showLoginScreen();
                return;
            }
            errorNotification("Logout failed!");
        }
    }


    /**
     * Método que mostra uma notificação de sucesso
     *
     * @param desc Descrição que será apresentada na notificação
     *
     */
    static void sucessNotification(String desc){
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Sucesso");
        tray.setMessage(desc);
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.seconds(3));
    }

    /**
     * Método que mostra uma notificação de sucesso
     *
     * @param desc Descrição que será apresentada na notificação
     */
    static void errorNotification(String desc){
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Erro");
        tray.setMessage(desc);
        tray.setNotificationType(NotificationType.ERROR);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.seconds(3));
    }

    /**
     *
     * @return Retorna a lista de todos os pacientes no sistema no formato "nif - nome"
     */
    static ArrayList<String> getPacientesList(String tipoDeID){

        switch (tipoDeID){
            case "paciente": tipoDeID = "id_paciente"; break;
            case "utilizador": tipoDeID = "id_utilizador"; break;
            case "nif": break;
            default: tipoDeID = "id_paciente";
        }

        ArrayList<String> pacientList = new ArrayList<>();
        ResultSet rs = driver.getAllPacientes();

        try{
            while (rs.next()){
                pacientList.add(rs.getString(tipoDeID) + " - " + rs.getString("nome"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pacientList;
    }

    /**
     * Cria uma lista com todos os utilizadores do sistema. Chama o método getPacientsList e depois adiciona à
     * lista já existente os resultados do método getAllFuncionarios
     * @return retorna uma lista com todos os utilizadores do sistema
     */
    static ArrayList<String> getAllUsers(){

        ArrayList<String> allUsersList = getPacientesList("nif");
        ResultSet rs = driver.getAllFuncionarios();

        try{
            while (rs.next()){
                allUsersList.add(rs.getString("nif") + " - " + rs.getString("nome"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return allUsersList;
    }

    /**
     * Cria uma lista de tratamentos de um paciente
     * @param pacienteID ID do paciente do qual queremos saber os tratamentos
     * @param full Variável boolean que serve para saber se é para ser devolvida uma lista completa (ID - id (data inicio)) ou
     *             apenas o ID do tratamento, devolvendo assim uma lista mais pequena e simples
     * @return retorna um array com a lista de tratamentos de um paciente
     */
    static ArrayList<String> getTratamentosList(String pacienteID, boolean full){
        ArrayList<String> trats = new ArrayList<>();
        ResultSet resultSet = driver.getTratamentos(pacienteID);
        if(resultSet != null) {
            try {
                while (resultSet.next()) {
                    if(full)
                        trats.add("ID - " + resultSet.getString("id_tratamento") + " (" + resultSet.getString("data_inicio") + ")");
                    else
                        trats.add(resultSet.getString("id_tratamento"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return trats;
    }

    /**
     * Cria uma lista de treinos em que apresenta o ID do treino mais a data desse treino (ou apenas o ID do treino, consoante
     * o paramentro passado na chamada ("full"))
     * @param idTratamento ID tratamento que terá os treinos associados ao mesmo
     * @param full Variável boolean que serve para saber se é para ser devolvida uma lista completa (ID - id (data inicio)) ou
     *             apenas o ID do treino, devolvendo assim uma lista mais pequena e simples
     * @return retorna um array com a lista de treinos de um paciente
     */
    static ArrayList<String> getTreinosList(String idTratamento, boolean full) {
        ArrayList<String> treinos = new ArrayList<>();
        ResultSet resultSet = driver.getTreinos(idTratamento);
        if(resultSet != null) {
            try {
                while (resultSet.next()) {
                    if(full)
                        treinos.add("ID - " + resultSet.getString("id_treino") + " (" + resultSet.getString("data_inicio") + ")");
                    else
                        treinos.add(resultSet.getString("id_treino"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return treinos;
    }

    /**
     * Retorna o objeto de login usado ao iniciar sessão. Função necesária para poder efetuar operações na BD
     * a partir de outras classes sem que seja necessário criar um novo login
     *
     * @return classe criada no login
     */
    static Driver getDriver() {
        return driver;
    }

    /**
     * Recebe uma data no formato mais comum e converte-a para o formato utilizado na BD
     * @param date data em formato string (dd-mm-yyyy)
     * @return data em formato string (yyyy-mm-dd)
     */
    static String normalizeDate(String date){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date data = null;
        try {
            data = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(data);
    }

    /**
     * Função usada para fechar uma janela (Stage) a partir de um evento emitido por um botão (que irá determinar qual
     * a janela que deverá ser fechada). Método criado para não repetir este código em várias classes
     * @param event Evento emitido pelo clique num botão
     */
    static void closeStage(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Cria e mostra um alerta erro (usado quando dados introduzidos estão incorretos) Recebe o texto que deverá ser
     * apresentado na descrição do alerta
     * @param desc Descrição a ser apresentada no corpo do alerta
     */
    static void errorAlert(String desc){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(desc);
        alert.showAndWait();
    }
}
