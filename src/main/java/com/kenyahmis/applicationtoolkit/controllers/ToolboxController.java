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

import java.io.File;
import java.io.IOException;
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

    public String emrurl="";
   // URL resource = getClass().getClassLoader().getResource("/opennmrs_backup_tools/opennmrs_backup.sh");
   ClassLoader resource = getClass().getClassLoader();


    private ObservableList<String> msgData;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("HMIS Application Toolkit");
    }
    @FXML
    protected void downloadurls(ActionEvent actionEvent) throws IOException {


        URL propresources = getClass().getClassLoader().getResource("application.properties");
        Properties prop = new Properties();
        prop.load(propresources.openStream());
        System.out.println(prop.getProperty("toolkit.emrurl"));
        System.out.println(prop.getProperty("toolkit.ehtsurl"));
        /*URL fxmlLocation = getClass().getResource("/hello-view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.load();*/


     //   ToolboxPropsController toolboxPropsController = new ToolboxPropsController();
      //  toolboxPropsController.start();
    }

    @FXML
    protected void downloadEmrUpgrade(ActionEvent actionEvent) throws IOException {

        URL propresources = getClass().getClassLoader().getResource("application.properties");
        Properties prop=new Properties();
        prop.load(propresources.openStream());

        System.out.println(prop.getProperty("toolkit.emrurl"));
        System.out.println(prop.getProperty("toolkit.emrurl"));



        addMessageToListFlow("Prompting for user authentication");
        String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;

        String downloadUrl = "https://github.com/palladiumkenya/kenyahmis-releases/releases/download/v51/KenyaEMR_18.2.0.zip";

      //  String downloadUrl = "https://github.com/palladiumkenya/kenyahmis-kenyaemr-autoupdate/releases/download/v18.2.1/KenyaEMR_18.2.1.zip";
       // String downloadUrl = "https://github.com/palladiumkenya/kenyahmis-releases/releases/latest";

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

        msgData = FXCollections.observableArrayList();
        listMsgs.setItems(msgData);

        File folder = new File(ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY);
        if (folder.exists() && folder.isDirectory()) {
            addMessageToListFlow("Application initialization completed");

        } else {
            addMessageToListFlow("App directories not found. Please create them and continue");

            //TODO: create directories on startup
            /*try {
                Files.createDirectory(Paths.get(ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY),
                        PosixFilePermissions.asFileAttribute(
                                PosixFilePermissions.fromString("rwxr-x---")
                        ));
                addMessageToListFlow("Successfully created app directory");

            } catch (IOException e) {
                addMessageToListFlow("An error occurred when creating app directory. Error: " + e.getMessage());

                throw new RuntimeException(e);
            }*/

        }

    }
}