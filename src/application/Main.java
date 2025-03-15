package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
            
            // Cast the root element to StackPane (since our FXML file's root is StackPane)
            StackPane root = (StackPane) loader.load();
            
            // Create a scene with the root element
            Scene scene = new Scene(root);
            
            primaryStage.setTitle("Unkle Osaka") ;
            primaryStage.setMaxHeight(650);
            primaryStage.setMinWidth(450);
            
            
            // Set the scene to the stage and show it
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
