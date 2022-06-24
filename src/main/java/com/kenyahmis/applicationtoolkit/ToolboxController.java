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
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

        //addMessageToTextFlow("Initiating download ... ", Color.DARKSLATEBLUE, new Font(15));
        addMessageToListFlow("Initiating download");
        String baseDir = "/home/ojwang/Documents/testDownload/";

        String downloadUrl = "https://github.com/palladiumkenya/kenyahmis-kenyaemr-autoupdate/releases/download/v18.2.1/KenyaEMR_18.2.1.zip";

        URL url = new URL(downloadUrl);
        System.out.println("running task");

        Path fileName = Paths.get(downloadUrl);

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
        System.out.println("Mysql pwd: " + mysqlPass);
        addMessageToListFlow("Authentication recorded successfully ");

        ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
        String downloadedFileName = fileName.getFileName().toString() ;
        final PackageDownloadService service = new PackageDownloadService(url, baseDir + downloadedFileName, this, configuration);


        upgradeButton.setDisable(true);
        service.start();

        /*Text downloadCompletedTxt = new Text("Download completed");
        downloadCompletedTxt.setFont(new Font(15));
        downloadCompletedTxt.setFill(Color.DARKSLATEBLUE);
        upgradeProgress.getChildren().add(downloadCompletedTxt);*/

    }

    public void downloadFile(URL url, String outputFileName) throws IOException
    {
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }




    private void addMessageToTextFlow(String text, Color textColor, Font font) {
        Text txt = new Text(text);
        txt.setFont(font);
        txt.setFill(textColor);
        upgradeProgress.getChildren().add(txt);
    }

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