package com.kenyahmis.applicationtoolkit;

import com.kenyahmis.applicationtoolkit.Services.PackageBackupService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.Task.UpdateScheduler;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Timer;

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
        stage.setTitle("KenyaHMIS Application Toolbox");
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