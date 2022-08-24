package com.kenyahmis.applicationtoolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Designed to simplify application(s) setup process
 * Should download from a URL, unzip(if needed), and deploys
 */
public class ToolBoxApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ToolBoxApplication.class.getResource("toolkit-view.fxml"));
        stage.setResizable(false);
        Scene scene = new Scene(fxmlLoader.load(), 450, 700);
        scene.getStylesheets().add(getClass().getResource("toolkit.css").toExternalForm());
        stage.setTitle("KenyaHMIS Application Toolkit");
        stage.getIcons().add(
                new Image(
                        getClass().getResourceAsStream( "testIcon.png" )));
       // stage.
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
      /* Timer t = new Timer();
        UpdateScheduler updateScheduler = new UpdateScheduler();
        // This task is scheduled to run every 30 seconds
      //  t.scheduleAtFixedRate(updateScheduler, 0, 30000);*/
        launch();
    }
}