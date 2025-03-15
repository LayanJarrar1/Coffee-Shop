package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class SampleController {
    @FXML
    private AnchorPane Register_form;
    @FXML
    private Button create_account;
    @FXML
    private Button create_alreadyHaveAccount;
    @FXML
    private TextField er_username;
    @FXML
    private PasswordField lo_password;
    @FXML
    private TextField lo_username;
    @FXML
    private Button loginButton;
    @FXML
    private AnchorPane loginForm;
    @FXML
    private TextField re_answer;
    @FXML
    private PasswordField re_pass;
    @FXML
    private ComboBox<String> re_question;
    @FXML
    private Button re_signUp;
    @FXML
    private AnchorPane slide_form;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
    
    
    @FXML
    public void loginBtn() {
        System.out.println("loginBtn method called");

        String username = lo_username.getText();
        String password = lo_password.getText();

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        if (username.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password");
            alert.showAndWait();
        } else {
            String selectData = "SELECT username, password FROM employee WHERE username = ? AND password = ?";
            connect = database.connectDB();

            if (connect == null) {
                System.out.println("Database connection failed");
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to connect to the database. Please check your connection settings.");
                alert.showAndWait();
                return;
            }

            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, username);
                prepare.setString(2, password);

                result = prepare.executeQuery();
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Logged In!");
                    alert.showAndWait();

                    try {
                       

                        Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                        System.out.println("Trying to load mainForm.fxml...");
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setTitle("Cafe Shop Management");
                        stage.setMinWidth(1100);
                        stage.setMinHeight(600);
                        stage.setScene(scene);
                        stage.show();

                        Stage currentStage = (Stage) loginButton.getScene().getWindow();
                        currentStage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error message");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to load the main form: " + e.getMessage());
                        alert.showAndWait();
                    }

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username or password");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Login failed: " + e.getMessage());
                alert.showAndWait();
            } finally {
                try {
                    if (result != null) result.close();
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @FXML
    public void regBtn() {
        System.out.println("regBtn method called");

        // Retrieve and print the values to ensure they are not empty
        String username = er_username.getText();
        String password = re_pass.getText();
        String question = re_question.getSelectionModel().getSelectedItem();
        String answer = re_answer.getText();

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Question: " + question);
        System.out.println("Answer: " + answer);

        if (username.isEmpty() || password.isEmpty() || question == null || answer.isEmpty()) {
            System.out.println("One or more fields are empty");

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
        	String regData = "INSERT INTO employee (username, password, question, security_answer) VALUES (?, ?, ?, ?)";
            connect = database.connectDB();
            if (connect == null) {
                System.out.println("Database connection failed");
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to connect to the database. Please check your connection settings.");
                alert.showAndWait();
                return;
            }
            try {
            	prepare = connect.prepareStatement(regData);
                prepare.setString(1, username);
                prepare.setString(2, password);
                prepare.setString(3, question); 
                prepare.setString(4, answer);
                prepare.executeUpdate();
                System.out.println("Registration successful");

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully registered Account");
                alert.showAndWait();

                er_username.setText("");
                re_pass.setText("");
                re_question.getSelectionModel().clearSelection();
                re_answer.setText("");

                TranslateTransition slider = new TranslateTransition();
                slider.setNode(slide_form);
                slider.setToX(0);
                slider.setDuration(Duration.seconds(0.5));
                slider.setOnFinished((ActionEvent e) -> {
                    create_alreadyHaveAccount.setVisible(false);
                    create_account.setVisible(true);
                });
                slider.play();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Registration failed: " + e.getMessage());
                alert.showAndWait();
            } finally {
                // Close the statement and connection
                try {
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String[] questionList = {"What is your name", "What is your favorite food", "What is your favorite drink"};

    @FXML
    public void regLquestionList() {
        ArrayList<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        re_question.setItems(listData);
    }

    @FXML
    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        if (event.getSource() == create_account) {
            slider.setNode(slide_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(0.5));
            slider.setOnFinished((ActionEvent e) -> {
                create_alreadyHaveAccount.setVisible(true);
                create_account.setVisible(false);
                regLquestionList();
            });
            slider.play();
        } else if (event.getSource() == create_alreadyHaveAccount) {
            slider.setNode(slide_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(0.5));
            slider.setOnFinished((ActionEvent e) -> {
                create_alreadyHaveAccount.setVisible(false);
                create_account.setVisible(true);
            });
            slider.play();
        }
    }
}

