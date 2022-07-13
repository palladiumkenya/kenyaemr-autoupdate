package com.kenyahmis.applicationtoolkit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.synedra.validatorfx.Check;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ToolboxController implements Initializable {
    private int currentVersion = 1800;
    private boolean isOnline = false;
    private File file,downloadPath,unzipPath,linuxFile,linuxUnzipPath,linuxDownloadPath;
    private String zipFilePath,destDir, linuxDestDir,linuxZipFilePath;
    private boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    private  String currentPath = new java.io.File(".").getCanonicalPath();
    private File backupDirectory = new File(currentPath+"/KenyaHIMS/backup");
    private File linuxBackupDirectory = new File(currentPath+"/KenyaHIMS/backup");

    private  String backupPath = ""+backupDirectory;


    @FXML
    private Label welcomeText;

    @FXML
    private Button upgradeButton;
    @FXML
    private Button checkButton;
    @FXML
    private TextFlow upgradeProgress;

    @FXML
    private ListView listMsgs;

    private ObservableList<String> msgData;

    public ToolboxController() throws IOException {
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("HIMS");
    }
    @FXML

    protected void checkUpgrade() throws IOException {

        addMessageToListFlow("Check for updates");
        System.out.println("Check updates");
        if (checkInternetConnectionStatus()) {
            addMessageToListFlow("Connected to Internet");
            isOnline = true;
            file = new File(currentPath+"/KenyaHIMS");
            unzipPath = new File(currentPath+"/KenyaHIMS/unzip");
            downloadPath = new File(currentPath+"/KenyaHIMS/downloads");
            zipFilePath =  downloadPath+ "/KenyaEMR_18.2.1.zip";
            linuxDestDir = ""+linuxUnzipPath;
            destDir = ""+unzipPath;


            boolean bool = file.mkdir();
            boolean bool1 = downloadPath.mkdir();
            boolean bool2 = unzipPath.mkdir();
            boolean bool3 = backupDirectory.mkdir();
            if(bool && bool1 && bool2 && bool3){
                System.out.println("Directory created successfully");
            }else{
                if (Config.newVersion != 0 && Config.newVersion > currentVersion) {
                    upgradeButton.setVisible(true);
                    checkButton.setVisible(false);
                }else{
                    addMessageToListFlow("Your version is up to date");
                }
            }
        }
    }

    @FXML

    protected void downloadEmrUpgrade(){


        if (checkInternetConnectionStatus()) {
            isOnline = true;
            try {
                if (Config.newVersion != 0 && Config.newVersion > currentVersion) {
                    //check for backup
                    // user pass
                    dbBackup();
                    openMrsBackup();
                    downloadAndUnzip(Config.url,zipFilePath,destDir);
                    bashScripting();
                    upgradeButton.setDisable(true);
                }else{
                    addMessageToListFlow("Your version is up to date");
                }            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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

        addMessageToListFlow(".............");
        addMessageToListFlow("Start bash scripting...!");
       // builder ;
        unzipPath = new File(currentPath+"/KenyaHIMS/unzip");
        String shFileDestination = unzipPath+ "/KenyaEMR_18.2.1/setup_script.sh";
        System.out.println("unzip"+ shFileDestination);
        ProcessBuilder  builder = new ProcessBuilder("/bin/bash", shFileDestination);
        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = builder.start();
            ProcessReader task = new ProcessReader(process.getInputStream());
            Future<List<String>> future = pool.submit(task);
            List<String> results = future.get();
            for (String res : results){
                addMessageToListFlow( res);
                System.out.println(res);
            }
            int exitCode = process.waitFor();
            addMessageToListFlow(""+exitCode);
        } catch (IOException | ExecutionException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        addMessageToListFlow("Bash scripting done!");

    }

    private void dbBackup(){

        addMessageToListFlow(".............");
        addMessageToListFlow("Start Database backup...!");
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

        // builder ;
        unzipPath = new File(currentPath+"/openmrs-backup-tools");
        String shFileDestination = unzipPath+ "/openmrs-backup-tools/db_dump.sh";
        System.out.println("unzip"+ shFileDestination);
        ProcessBuilder  builder = new ProcessBuilder("/bin/bash", shFileDestination);
        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = builder.start();
            ProcessReader task = new ProcessReader(process.getInputStream());
            Future<List<String>> future = pool.submit(task);
            List<String> results = future.get();
            for (String res : results){
                addMessageToListFlow( res);
                System.out.println(res);
            }
            int exitCode = process.waitFor();
            addMessageToListFlow(""+exitCode);
        } catch (IOException | ExecutionException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        addMessageToListFlow("Database backup done!");

    }
    private void openMrsBackup(){

        addMessageToListFlow(".............");
        addMessageToListFlow("Start openmrs backup...!");
        // builder ;
        unzipPath = new File(currentPath+"/openmrs-backup-tools");
        String shFileDestination = unzipPath+ "/openmrs-backup-tools/openmrs_backup.sh";
        ProcessBuilder  builder = new ProcessBuilder("/bin/bash", shFileDestination);
        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = builder.start();
            ProcessReader task = new ProcessReader(process.getInputStream());
            Future<List<String>> future = pool.submit(task);
            List<String> results = future.get();
            for (String res : results){
                addMessageToListFlow( res);
                System.out.println(res);
            }
            int exitCode = process.waitFor();
            addMessageToListFlow(""+exitCode);
        } catch (IOException | ExecutionException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        addMessageToListFlow("Back up done!");

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
    protected static Map<String, Object> map(Object... keyValPairs) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (int p = 0; p < keyValPairs.length; p += 2) {
            String key = (String) keyValPairs[p];
            Object val = keyValPairs[p + 1];
            map.put(key, val);
        }
        return map;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        msgData = FXCollections.observableArrayList();
        listMsgs.setItems(msgData);
    }
}