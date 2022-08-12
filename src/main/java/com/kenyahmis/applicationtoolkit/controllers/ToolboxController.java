package com.kenyahmis.applicationtoolkit.controllers;

import com.kenyahmis.applicationtoolkit.utils.PasswordDialog;
import com.kenyahmis.applicationtoolkit.Services.PackageBackupService;
import com.kenyahmis.applicationtoolkit.Services.PackageDownloadService;
import com.kenyahmis.applicationtoolkit.Services.RunRollBackService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

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

    String deploymentdir ="";
    String localproperties ="";
    String tookitversion="";
    String remoteurl="";
    String emrversion="";
   // String remoteproperties="";

   // public String emrurl="";
   // URL resource = getClass().getClassLoader().getResource("/opennmrs_backup_tools/opennmrs_backup.sh");
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
            String openmrsBackup = "openmrs-backup-tools/openmrs_backup.sh";
            URL resources = getClass().getClassLoader().getResource(openmrsBackup);
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            } else {
                configuration.setPathToBackupScript(resources.getPath());
                final PackageBackupService backupService = new PackageBackupService(this, configuration);
              //  upgradeButton.setDisable(true);
                backupService.start();
            }

            //Do Upgrade
            configuration.setPathToSetupScript(baseDir + fileNameWithoutExtension + "/rollback_script.sh");
            final PackageDownloadService service = new PackageDownloadService(this, configuration);
            //upgradeButton.setDisable(true);
            service.start();

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
            String openmrsBackup = "openmrs-backup-tools/openmrs_backup.sh";
            URL resources = getClass().getClassLoader().getResource(openmrsBackup);
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            } else {
                configuration.setPathToBackupScript(resources.getPath());
                final PackageBackupService backupService = new PackageBackupService(this, configuration);
                //upgradeButton.setDisable(true);
                backupService.start();
            }


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

            String rollbacksurl = "rollback-tools/rollback_script.sh";
            URL rollbackresources = getClass().getClassLoader().getResource(rollbacksurl);
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            } else {
                configuration.setPathToRollbackScript(rollbackresources.getPath());
                final RunRollBackService runRollBackService = new RunRollBackService(this, configuration);
                //upgradeButton.setDisable(true);
                runRollBackService.start();
            }



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
        //Check remote application.properties
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
        remoteurl=prop.getProperty("toolkit.emrurl");
        emrversion=prop.getProperty("toolkit.emrversion");
        configuration.setRemoteproperties(prop.getProperty("toolkit.remoteproperties"));

        System.out.println("Valuess "+configuration.getRemoteproperties());

        File f = new File(deploymentdir);
        if(f.exists() && f.isFile()) {
            System.out.println("Iko hapa sasa");
            //compare the two files
            //
        }else{
            System.out.println("hakuna hapa sasa");
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
                        // set the properties value
                        props.setProperty("toolkit.localproperties",localproperties);
                        props.setProperty("toolkit.version",tookitversion);
                        props.setProperty("toolkit.emrurl",remoteurl);
                        props.setProperty("toolkit.emrversion",emrversion);
                        props.setProperty("toolkit.remoteproperties",configuration.getRemoteproperties());

                        // save properties to project root folder
                        props.store(output, null);

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        //End of check

        msgData = FXCollections.observableArrayList();
        listMsgs.setItems(msgData);
        lblFooter.setText("Copyright 2022 KenyaHMIS ToolKit Version "+ tookitversion);
        lblEMR.setText("KenyaEMR Version ("+ emrversion +")");

        File folder = new File(ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY);
        if (folder.exists() && folder.isDirectory()) {
            addMessageToListFlow("Application initialization completed");

        } else {
            addMessageToListFlow("App directories not found. Please create them and continue");

        }
        }
    }
}