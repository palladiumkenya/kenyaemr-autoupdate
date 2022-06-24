package com.kenyahmis.applicationtoolkit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class ToolboxController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private Button upgradeButton;
    @FXML
    private TextFlow upgradeProgress;

    @FXML
    private ListView listMsgs;

    private ObservableList<String> msgData;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("HMIS Application Toolkit");
    }
    @FXML
    protected void downloadEmrUpgrade(ActionEvent actionEvent) throws IOException {

        addMessageToListFlow("Initiating download");
        String baseDir = "/home/ojwang/Documents/testDownload/";

        String downloadUrl = "https://github.com/palladiumkenya/kenyahmis-kenyaemr-autoupdate/releases/download/v18.2.1/KenyaEMR_18.2.1.zip";

        if (baseDir.equals("") || downloadUrl.equals("")) {
            // exit with message
        }
        URL url = new URL(downloadUrl);
        System.out.println("running task");

        Path fileName = Paths.get(downloadUrl);
        String downloadedFileName = fileName.getFileName().toString() ;
        String fileNameWithoutExtension = downloadedFileName.substring(0, downloadedFileName.lastIndexOf('.'));
        System.out.println("Downloaded filename: " + downloadedFileName);

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
            addMessageToListFlow("Authorization required to proceed. The system will terminate ");
            System.out.println("Authorization required to proceed. The system will terminate ");

        } else {
            ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
            configuration.setPackageDownloadUrl(url);
            configuration.setPackageUnzipDir(baseDir + downloadedFileName);
            configuration.setBaseDir(baseDir);
            configuration.setPathToSetupScript(baseDir + fileNameWithoutExtension + "/setup_script.sh");

            final PackageDownloadService service = new PackageDownloadService(this, configuration);

            upgradeButton.setDisable(true);
            service.start();
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
    }
}