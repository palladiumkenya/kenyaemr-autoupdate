package com.kenyahmis.applicationtoolkit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ToolboxController implements Initializable {
    private boolean isOnline = false;
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
        welcomeText.setText(Config.name);
    }
    @FXML

   protected void downloadEmrUpgrade(ActionEvent actionEvent) throws IOException {


        if (checkInternetConnectionStatus()) {
            isOnline = true;
            try {
                downloadAndUnzip(Config.url,Config.zipFilePath,Config.destDir);
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
                bashScripting();
                //   ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
//        String downloadedFileName = fileName.getFileName().toString() ;
//        final PackageDownloadService service = new PackageDownloadService(url, baseDir + downloadedFileName, this, configuration);
                upgradeButton.setDisable(true);
                // service.start();

        /*Text downloadCompletedTxt = new Text("Download completed");
        downloadCompletedTxt.setFont(new Font(15));
        downloadCompletedTxt.setFill(Color.DARKSLATEBLUE);
        upgradeProgress.getChildren().add(downloadCompletedTxt);*/



            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

//    public void downloadFile(URL url, String outputFileName) throws IOException
//    {
//        try (InputStream in = url.openStream();
//             ReadableByteChannel rbc = Channels.newChannel(in);
//             FileOutputStream fos = new FileOutputStream(outputFileName)) {
//            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//        }
//    }
//

    private List<String> downloadAndUnzip(String url, String zipFilePath, String destDirectory) throws IOException {
        addMessageToListFlow("Initiate Download ...");
        URL destURL = new URL(url);
        URLConnection urlConnection = destURL.openConnection();

        ReadableByteChannel zipByteChannel = Channels.newChannel(urlConnection.getInputStream());
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        fos.getChannel().transferFrom(zipByteChannel,0,Long.MAX_VALUE);
        addMessageToListFlow("Download done");
        return unzip(zipFilePath,destDirectory);

    }

    private List<String> unzip(String zipFilePath, String destDirectory) throws IOException{
        addMessageToListFlow("Start Unzipping ...");
        List<String> unzippedFilesList = new ArrayList<>();

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zipIn.getNextEntry();

        while (zipEntry != null){

            String filePath = destDirectory+File.separator+zipEntry.getName();

            if (!zipEntry.isDirectory()){
                unzippedFilesList.add(extractFile(zipIn, filePath));
            } else{
                File dir = new File(filePath);
                dir.mkdir();
            }

            zipEntry = zipIn.getNextEntry();
        }
        addMessageToListFlow("Unzipping Done!");
        System.out.println("Unzipping Done!");
        return  unzippedFilesList;
    }

    private String extractFile(ZipInputStream zipIn, String filePath) throws IOException{

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));

        byte bytes[] = new byte[4096];
        int read = 0;
        read = zipIn.read(bytes);

        while (read != -1){

            bos.write(bytes,0,read);
            read = zipIn.read(bytes);

        }

        bos.close();
        addMessageToListFlow("Files Extracted Done!");
        System.out.println("Extracted a file");
        return filePath;
    }

    private void bashScripting(){
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        addMessageToListFlow(".............");
        addMessageToListFlow("Start bash scripting...!");
        ProcessBuilder builder = new ProcessBuilder();
        //  ProcessBuilder builder = new ProcessBuilder("C:\\Projects\\zipFiles\\echo.bat");
        if (isWindows){
            builder = new ProcessBuilder(Config.batFileDestination);
            // builder.command(System.getProperty("C:\\Projects\\zipFiles\\echo.bat"));
        }else{
            builder.command(Config.shFileDestination);
        }

        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = builder.start();

            ProcessReader task = new ProcessReader(process.getInputStream());
            Future<List<String>> future = pool.submit(task);


            List<String> results = future.get();
            addMessageToListFlow(""+results);
            for (String res : results){
                System.out.println(res);
            }

            int exitCode = process.waitFor();
            addMessageToListFlow(""+exitCode);
            System.out.println("Exit code" + exitCode);
        } catch (IOException | ExecutionException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        addMessageToListFlow("Bash scripting done!");

    }
    private void addMessageToTextFlow(String text, Color textColor, Font font) {
        Text txt = new Text(text);
        txt.setFont(font);
        txt.setFill(textColor);
        upgradeProgress.getChildren().add(txt);
    }
    private boolean checkInternetConnectionStatus() {
        boolean isConnected = false;

        try {
            URL url = new URL("https://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            isConnected = true;
        } catch (MalformedURLException e) {
            addMessageToListFlow("Internet is not connected");
        } catch (IOException e) {
            addMessageToListFlow("Internet is not connected");
        }

        return isConnected;
    }
    private static class ProcessReader implements Callable {
        private InputStream inputStream;


        public ProcessReader(InputStream inputStream){
            this.inputStream = inputStream;
        }

        @Override
        public Object call() throws Exception {
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.toList());
        }
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