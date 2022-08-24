package com.kenyahmis.applicationtoolkit.controllers;

import com.kenyahmis.applicationtoolkit.Services.*;
import com.kenyahmis.applicationtoolkit.ToolBoxApplication;
import com.kenyahmis.applicationtoolkit.utils.InfoAlerts;
import com.kenyahmis.applicationtoolkit.utils.PasswordDialog;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Handles toolbox events
 */
public class ToolboxController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private Button upgradeButton;
    @FXML
    private TextFlow upgradeProgress;

    @FXML
    private ListView listMsgs;
    @FXML
    private Label lblFooter;
    @FXML
    private Label lblEMR;
    @FXML
    private Label lblUpdates;
    @FXML
    private Hyperlink cmdbackup;
    @FXML
    private Hyperlink cmdupgrade;
    @FXML
    private Hyperlink cmdrollback;


    String deploymentdir ="";
    String localproperties ="";
    String tookitversion="";
    String remoteurl="";
    String emrversion="";
    String remoteemrversion="";
    String appversion="";
    String localappversion="";
    String appurl="";

    String appdir="";
    String scriptversion="";
    String remotescriptversion="";

    String remoteseripturl="";

    ClassLoader resource = getClass().getClassLoader();
    private ObservableList<String> msgData;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("HMIS Application Toolkit");
    }
    @FXML
    protected void downloadEmrUpgrade(ActionEvent actionEvent) throws IOException {
        addMessageToListFlow("Prompting for user authentication");
        String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
   //Add here
        String downloadUrl = remoteurl;

        if (baseDir.equals("") || downloadUrl.equals("")) {
            // exit with message
        }
        URL url = new URL(downloadUrl);
        Path fileName = Paths.get(downloadUrl);
        String downloadedFileName = fileName.getFileName().toString() ;
        String fileNameWithoutExtension = downloadedFileName.substring(0, downloadedFileName.lastIndexOf('.'));

        // user pass
        String token = "";
        PasswordDialog dialog = new PasswordDialog();

        dialog.setTitle("Admin password");
        dialog.setHeaderText("Enter admin password:");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            token = dialog.getPasswordField().getText();
        }

        String mysqlPass = "";
        PasswordDialog mysqlDialog = new PasswordDialog();

        mysqlDialog.setTitle("Mysql password");
        mysqlDialog.setHeaderText("Enter MySQL password:");
        mysqlDialog.setContentText("Password:");

        Optional<String> mysqlResult = mysqlDialog.showAndWait();
        if (mysqlResult.isPresent()) {
            mysqlPass = mysqlDialog.getPasswordField().getText();
        }
        if ("".equals(token) || "".equals(mysqlPass)) {
            addMessageToListFlow("Authorization required to proceed. Please provide details to proceed ");
            System.out.println("Authorization required to proceed. Please provide details to proceed ");

        } else {
            ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
            configuration.setPackageDownloadUrl(url);
            configuration.setPackageUnzipDir(baseDir + downloadedFileName);
            configuration.setBaseDir(baseDir);
            //Do Backup
            String openmrsBackup = "/opt/kehmisApplicationToolbox/Downloads/Scripts/openmrs-backup-tools/openmrs_backup.sh";
                configuration.setPathToBackupScript(openmrsBackup);
                final PackageBackupService backupService = new PackageBackupService(this, configuration);
                backupService.start();

            //Do Upgrade
            configuration.setPathToSetupScript(baseDir + fileNameWithoutExtension + "/toolkit_setup_script.sh");
            final PackageDownloadService service = new PackageDownloadService(this, configuration);
            service.start();

            PropertiesConfiguration wdirprop = null;
            try {
                wdirprop = new PropertiesConfiguration(localproperties);
                wdirprop.setProperty("toolkit.emrversion",remoteemrversion);
                wdirprop.save();
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }

        }
    }
    @FXML
    protected void backupEMR(ActionEvent actionEvent) throws IOException {

        addMessageToListFlow("Prompting for user authentication");
        String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
        // user pass
        String token = "";
        PasswordDialog dialog = new PasswordDialog();

        dialog.setTitle("Admin password");
        dialog.setHeaderText("Enter admin password:");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            token = dialog.getPasswordField().getText();
        }
        String mysqlPass = "";
        PasswordDialog mysqlDialog = new PasswordDialog();
        mysqlDialog.setTitle("Mysql password");
        mysqlDialog.setHeaderText("Enter MySQL password:");
        mysqlDialog.setContentText("Password:");

        Optional<String> mysqlResult = mysqlDialog.showAndWait();
        if (mysqlResult.isPresent()) {
            mysqlPass = mysqlDialog.getPasswordField().getText();
        }
        if ("".equals(token) || "".equals(mysqlPass)) {
            addMessageToListFlow("Authorization required to proceed. Please provide details to proceed ");
            System.out.println("Authorization required to proceed. Please provide details to proceed ");

        } else {
            ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
            //Do Backup
            String openmrsBackup = "/opt/kehmisApplicationToolbox/Downloads/Scripts/openmrs-backup-tools/openmrs_backup.sh";
                configuration.setPathToBackupScript(openmrsBackup);
                final PackageBackupService backupService = new PackageBackupService(this, configuration);
                //upgradeButton.setDisable(true);
                backupService.start();
            }

    }
    @FXML
    protected void rollbackEMR(ActionEvent actionEvent) throws IOException {

        addMessageToListFlow("Prompting for user authentication");
        String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
        // user pass
        String token = "";
        PasswordDialog dialog = new PasswordDialog();

        dialog.setTitle("Admin password");
        dialog.setHeaderText("Enter admin password:");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            token = dialog.getPasswordField().getText();
        }

        String mysqlPass = "";
        PasswordDialog mysqlDialog = new PasswordDialog();

        mysqlDialog.setTitle("Mysql password");
        mysqlDialog.setHeaderText("Enter MySQL password:");
        mysqlDialog.setContentText("Password:");

        Optional<String> mysqlResult = mysqlDialog.showAndWait();
        if (mysqlResult.isPresent()) {
            mysqlPass = mysqlDialog.getPasswordField().getText();
        }
        if ("".equals(token) || "".equals(mysqlPass)) {
            addMessageToListFlow("Authorization required to proceed. Please provide details to proceed ");
            System.out.println("Authorization required to proceed. Please provide details to proceed ");

        } else {
            ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
            String rollbacksurl = "/opt/kehmisApplicationToolbox/Downloads/Scripts/rollback-tools/rollback_script.sh";
                configuration.setPathToRollbackScript(rollbacksurl);
                final RunRollBackService runRollBackService = new RunRollBackService(this, configuration);
                runRollBackService.start();

        }
    }
    @FXML
    protected void upgradeEMR(ActionEvent actionEvent) throws IOException {

        addMessageToListFlow("Prompting for user authentication");
        String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
        // user pass
        String token = "";
        PasswordDialog dialog = new PasswordDialog();

        dialog.setTitle("Admin password");
        dialog.setHeaderText("Enter admin password:");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            token = dialog.getPasswordField().getText();
        }

        String mysqlPass = "";
        PasswordDialog mysqlDialog = new PasswordDialog();

        mysqlDialog.setTitle("Mysql password");
        mysqlDialog.setHeaderText("Enter MySQL password:");
        mysqlDialog.setContentText("Password:");

        Optional<String> mysqlResult = mysqlDialog.showAndWait();
        if (mysqlResult.isPresent()) {
            mysqlPass = mysqlDialog.getPasswordField().getText();
        }
        if ("".equals(token) || "".equals(mysqlPass)) {
            addMessageToListFlow("Authorization required to proceed. Please provide details to proceed ");
            System.out.println("Authorization required to proceed. Please provide details to proceed ");

        } else {
            ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
            String rollbacksurl = "/opt/kehmisApplicationToolbox/Downloads/Scripts/rollback-tools/rollback_script.sh";
            configuration.setPathToRollbackScript(rollbacksurl);
            final RunUpgradeScriptService runRollBackService = new RunUpgradeScriptService(this, configuration);
            runRollBackService.start();

        }
    }

    /**
     *
     * @param text
     * @param textColor
     * @param font
     */
    private void addMessageToTextFlow(String text, Color textColor, Font font) {
        Text txt = new Text(text);
        txt.setFont(font);
        txt.setFill(textColor);
        upgradeProgress.getChildren().add(txt);
    }

    /**
     * Add message to list flow
     * @param text
     */
    public void addMessageToListFlow(String text) {
        if (text != null && !"".equals(text)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    msgData.add(text);
                }
            });
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File ntheDirs = new File("/opt/kehmisApplicationToolbox");
        File ntheDird = new File("/opt/kehmisApplicationToolbox/Downloads");
        if (!ntheDirs.exists()){
            ntheDirs.mkdirs();
        }
        if (!ntheDird.exists()){
            ntheDird.mkdirs();
        }

        ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration("","");
        URL propresources = getClass().getClassLoader().getResource("application.properties");
        Properties prop=new Properties();
        try {
            prop.load(propresources.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        deploymentdir = prop.getProperty("toolkit.deploymentdir");
        localproperties =prop.getProperty("toolkit.localproperties");
        tookitversion=prop.getProperty("toolkit.version");
        localappversion=prop.getProperty("toolkit.version");
        emrversion=prop.getProperty("toolkit.emrversion");
        appurl =prop.getProperty("toolkit.appurl");
        appdir =prop.getProperty("toolkit.appdir");
        scriptversion=prop.getProperty("toolkit.scriptversion");
        remoteseripturl=prop.getProperty("toolkit.scriptsurl");
        configuration.setRemoteproperties(prop.getProperty("toolkit.remoteproperties"));

        String propFileName = configuration.getRemoteproperties();
        System.out.println("Remote props "+propFileName);
        URL u = null;
        try {
            u = new URL(propFileName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        InputStream inputStream = null;
        try {
            inputStream = u.openStream();
        } catch (IOException e) {

              InfoAlerts infoAlerts = new InfoAlerts(Alert.AlertType.INFORMATION,"Updates Available", ButtonType.APPLY);
              infoAlerts.NoConnection(e.getMessage());
           // throw new RuntimeException(e);
        }
        Properties remoteprop=new Properties();
        if (inputStream != null) {
            try {
                remoteprop.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            remoteurl = remoteprop.getProperty("toolkit.remoteemrurl");
            remoteemrversion = remoteprop.getProperty("toolkit.emrversion");
            appversion=remoteprop.getProperty("toolkit.version");
            remotescriptversion=remoteprop.getProperty("toolkit.scriptversion");
            remoteseripturl=remoteprop.getProperty("toolkit.scriptsurl");

        }
        File f = new File(deploymentdir);
        if(f.exists() && f.isFile()) {

        }else{
            File theDir = new File(deploymentdir);
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            else{
                File propsfile = new File(localproperties);
                if (!propsfile.exists()){
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(localproperties);
                        Properties props = new Properties();
                        props.setProperty("toolkit.localproperties",localproperties);
                        props.setProperty("toolkit.version",tookitversion);
                        props.setProperty("toolkit.emrurl",remoteurl);
                        props.setProperty("toolkit.emrversion",emrversion);
                        props.setProperty("toolkit.remoteproperties",configuration.getRemoteproperties());
                        props.setProperty("toolkit.scriptversion",scriptversion);
                        props.setProperty("toolkit.version",localappversion);
                        props.store(output, null);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        FileInputStream ip= new FileInputStream(localproperties);
                        Properties wdirprop=new Properties();
                        wdirprop.load(ip);
                        tookitversion=wdirprop.getProperty("toolkit.version");
                        localappversion=wdirprop.getProperty("toolkit.version");
                        emrversion=wdirprop.getProperty("toolkit.emrversion");
                        scriptversion=wdirprop.getProperty("toolkit.scriptversion");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        msgData = FXCollections.observableArrayList();
        listMsgs.setItems(msgData);
        lblFooter.setText("Copyright 2022 KenyaHMIS ToolKit Version "+ tookitversion);
        lblEMR.setText("KenyaEMR Version ("+ emrversion +")");

            String[] localV = emrversion.split("[.]");
            String[] remoteV = remoteemrversion.split("[.]");
             Double local = Double.parseDouble(localV[1]+"."+localV[2]);
             Double remote = Double.parseDouble(remoteV[1]+"."+remoteV[2]);

            for (String a : localV)
                System.out.println(a);

         //Main version
           if(Integer.parseInt(remoteV[0])>Integer.parseInt(localV[0])){
               lblUpdates.setText("KenyaEMR "+ remoteemrversion +" is Available !!!");
               lblUpdates.setTextFill(Color.web("#5c0617"));
               final double MAX_FONT_SIZE = 18.0; // define max font size you need
               lblUpdates.setFont(new Font(MAX_FONT_SIZE));

           }else{
               //sub version
               if(remote>local){
                   lblUpdates.setText("KenyaEMR "+ remoteemrversion +" is Available !!!");
                   lblUpdates.setTextFill(Color.web("#5c0617"));
                   final double MAX_FONT_SIZE = 18.0; // define max font size you need
                   lblUpdates.setFont(new Font(MAX_FONT_SIZE));
               }
               else{
                   lblUpdates.setText("No Update Available !!!");
                   lblUpdates.setTextFill(Color.web("#5c0617"));
                   final double MAX_FONT_SIZE = 18.0; // define max font size you need
                   lblUpdates.setFont(new Font(MAX_FONT_SIZE));
                   cmdupgrade.setDisable(true);
                   cmdrollback.setDisable(true);
               }
           }

            //Check application version
            if(Double.parseDouble(appversion) > Double.parseDouble(localappversion)){

                String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
                URL apdurl = null;
                try {
                    apdurl = new URL(appurl);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                configuration.setAppulr(apdurl);
                Path fileName = Paths.get(appurl);
                String downloadedFileName = fileName.getFileName().toString() ;
                configuration.setAppulr(apdurl);
                configuration.setApppackageUnzipDir(baseDir + downloadedFileName);
                configuration.setBaseDir(baseDir);
                File theDirs = new File("/opt/kehmisApplicationToolbox");
                File theDird = new File("/opt/kehmisApplicationToolbox/Downloads");
                if (!theDirs.exists()){
                    theDirs.mkdirs();
                }
                if (!theDird.exists()){
                    theDird.mkdirs();
                }
                final AppUpdateService appUpdateService = new AppUpdateService(this, configuration);
                appUpdateService.start();
                Path sour = Paths.get("/opt/kehmisApplicationToolbox/Downloads/kenyahmistoolkit.jar");
                Path Dest = Paths.get("/usr/share/kenyahmistoolkit/kenyahmistoolkit.jar");
                System.out.println("Updating Toolkit package");
                try {
                    SeekableByteChannel destFileChannel = Files.newByteChannel(Dest);
                    destFileChannel.close();  //removing this will throw java.nio.file.AccessDeniedException:
                    // Files.copy(sour, Dest, StandardCopyOption.REPLACE_EXISTING);
                      PropertiesConfiguration wdirprop = new PropertiesConfiguration(localproperties);
                      wdirprop.setProperty("toolkit.version",appversion);
                      wdirprop.save();

                } catch (IOException | ConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }

           if(Double.parseDouble(remotescriptversion) > Double.parseDouble(scriptversion)){

               String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
               URL remotescrp = null;
               try {
                   remotescrp = new URL(remoteseripturl);
               } catch (MalformedURLException e) {
                   throw new RuntimeException(e);
               }
               Path fileName = Paths.get(remoteseripturl);
               String downloadedFileName = fileName.getFileName().toString() ;
               configuration.setScriptsurl(remotescrp);
               configuration.setScriptpackageUnzipDir(baseDir + downloadedFileName);
               configuration.setBaseDir(baseDir);
               File theDirs = new File("/opt/kehmisApplicationToolbox");
               File theDird = new File("/opt/kehmisApplicationToolbox/Downloads");
               if (!theDirs.exists()){
                   theDirs.mkdirs();
               }
               if (!theDird.exists()){
                   theDird.mkdirs();
               }
               final DownloadScriptService service = new DownloadScriptService(this, configuration);
               service.start();
               PropertiesConfiguration wdirprop = null;
               try {
                   wdirprop = new PropertiesConfiguration(localproperties);
                   wdirprop.setProperty("toolkit.scriptversion",remotescriptversion);
                   wdirprop.save();
               } catch (ConfigurationException e) {
                   throw new RuntimeException(e);
               }

           }

        File folder = new File(ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY);
        if (folder.exists() && folder.isDirectory()) {
            addMessageToListFlow("Application initialization completed");

        } else {

            addMessageToListFlow("App directories not found. Please create them and continue");

        }
        }
    }
}
