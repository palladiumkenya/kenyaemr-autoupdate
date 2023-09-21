package com.kenyahmis.applicationtoolkit.Task;

import com.almasb.fxgl.net.Connection;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.utils.InfoAlerts;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.w3c.dom.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimerTask;

public class UpdateScheduler extends TimerTask implements ShowProgress {

    String localversion="";
    String remoteVersion="";
    //str
    public UpdateScheduler() throws IOException {
        ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration("","");
        URL propresources = getClass().getClassLoader().getResource("application.properties");
        //Check local application.properties
        Properties props=new Properties();
        try {
            props.load(propresources.openStream());
            localversion= props.getProperty("toolkit.emrversion");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        configuration.setRemoteproperties(props.getProperty("toolkit.remoteproperties"));
        String propFileName = configuration.getRemoteproperties();

        //Check Remote application.properties
        System.out.println("Remote props "+propFileName);
        URL u = new URL(propFileName);
        InputStream inputStream = u.openStream();
        Properties prop=new Properties();
        if (inputStream != null) {
            prop.load(inputStream);
            System.out.println(prop);
            remoteVersion = prop.getProperty("toolkit.emrversion");
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        System.out.println("Local Version "+ localversion+" remote version "+remoteVersion);
        if(localversion.equals(remoteVersion)){
           // System.out.println("No Update is Available");
        }else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //InfoAlerts infoAlerts = new InfoAlerts(Alert.AlertType.INFORMATION,"Updates Available", ButtonType.APPLY);
                    //infoAlerts.EMRUpdate(remoteVersion);
                }
            });
        }
    }

    /** 
     * Show or hide the progress spinner
     * @param status - boolean - true: show spinner, false: hide spinner
    */
    public void showProgress(boolean status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // controller.showProgress(status);
            }
        });
    }

    @Override
    public void run() {
        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                InfoAlerts infoAlerts = new InfoAlerts(Alert.AlertType.INFORMATION,"Updates Available", ButtonType.APPLY);
                infoAlerts.EMRUpdate(remoteVersion);
            }
        });*/
        System.out.println("Hi see you after 10 seconds");
    }

}
