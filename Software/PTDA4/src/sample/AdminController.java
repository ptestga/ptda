package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private Button btn_addpacient, btn_addfunc, btn_edit, btn_logout, btn_logs, btn_recuperarpass;
    @FXML
    private ListView<String> listview_admin_users;
    @FXML
    private ImageView imgbtn_refresh, img_admin;
    @FXML
    private Label lbl_nif;

    private String nif;

    AdminController(String nif) {
        this.nif = nif;
    }

    public void initialize(){

        btn_addpacient.getStyleClass().addAll("primary");
        btn_addfunc.getStyleClass().addAll("primary");
        btn_edit.getStyleClass().addAll("primary");
        btn_logs.getStyleClass().addAll("primary");
        btn_recuperarpass.getStyleClass().addAll("primary");

        btn_addpacient.setOnAction(event -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/add_paciente.fxml"));
            AddPacientController addPacientController = new AddPacientController(this);
            fxmlLoader.setController(addPacientController);
            Stage stage = new Stage();

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

            stage.setTitle("Adicionar paciente");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });

        btn_addfunc.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/add_func.fxml"));
            AddFuncController addFuncController = new AddFuncController(this);
            fxmlLoader.setController(addFuncController);
            Stage stage = new Stage();

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

            stage.setTitle("Adicionar funcionário");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });

        btn_edit.setOnAction(event -> {

            if(listview_admin_users.getSelectionModel().getSelectedItem() != null) {
                String nif = listview_admin_users.getSelectionModel().getSelectedItem().substring(0, 9);

                FXMLLoader fxmlLoader;
                Stage stage = new Stage();
                Parent root = null;

                String userType = Main.getDriver().userType(nif);

                if(userType.equals("paciente")) {

                    fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/edit_paciente.fxml"));

                    EditPacController editPacController = new EditPacController(nif);
                    fxmlLoader.setController(editPacController);

                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stage.setTitle("Editar paciente");
                } else {
                    fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/edit_func.fxml"));

                    EditFuncController editFuncController = new EditFuncController(nif);
                    fxmlLoader.setController(editFuncController);

                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stage.setTitle("Editar funcionário");
                }

                Scene scene = new Scene(root);
                scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }else {
                Main.errorAlert("Por favor selecione o utilizador que pretende editar!");
            }
        });

        btn_recuperarpass.setOnAction(event -> {
            if(listview_admin_users.getSelectionModel().getSelectedItem() != null) {

                String nif = listview_admin_users.getSelectionModel().getSelectedItem().substring(0,9);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/edit_pass.fxml"));
                EditPassController editPassController = new EditPassController(nif);
                fxmlLoader.setController(editPassController);
                Stage stage = new Stage();

                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scene scene = new Scene(root);
                scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

                stage.setTitle("Editar password");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }else {
                Main.errorAlert("Por favor selecione o utilizador que pretende editar!");
            }
        });

        btn_logout.setOnAction(event -> Main.logout());

        lbl_nif.setText(nif);
        img_admin.setOnMouseClicked(event -> {

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

        refreshList();

        imgbtn_refresh.setOnMouseClicked(event -> refreshList());

        btn_logs.setOnAction(event -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/layout/logs_admin.fxml"));
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("bootstrap3.css").toExternalForm());

            stage.setTitle("Logs do sistema");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        });
    }

    void refreshList(){
        ObservableList<String> items = FXCollections.observableArrayList(Main.getAllUsers());
        listview_admin_users.setItems(items);
    }
}
