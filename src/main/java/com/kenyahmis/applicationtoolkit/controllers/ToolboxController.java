package com.kenyahmis.applicationtoolkit.controllers;


import com.kenyahmis.applicationtoolkit.Services.AppUpdateService;
import com.kenyahmis.applicationtoolkit.Services.DownloadScriptService;
import com.kenyahmis.applicationtoolkit.Services.OfflineUpgradeService;
import com.kenyahmis.applicationtoolkit.Services.PackageBackupService;
import com.kenyahmis.applicationtoolkit.Services.PackageDownloadService;
import com.kenyahmis.applicationtoolkit.Services.RunRollBackService;
import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.Services.UpgradeToolkitService;
import com.kenyahmis.applicationtoolkit.utils.InfoAlerts;
import com.kenyahmis.applicationtoolkit.utils.PasswordDialog;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.util.zip.ZipInputStream;
import java.util.jar.JarInputStream;
import java.util.jar.JarEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;

import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.xml.sax.InputSource;

import java.net.URL;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.util.regex.*;

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

    @FXML
    private ScrollPane sasd;

    @FXML
    private ProgressIndicator showWait;

    @FXML
    private HBox topCommands;

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
        showProgress(true);
        listMsgs.getItems().clear();
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
        
        // Get current LINUX sudo user password
        String token = "";
        PasswordDialog dialog = new PasswordDialog();
        dialog.setTitle("Admin password");
        dialog.setHeaderText("Enter admin password:");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            token = dialog.getPasswordField().getText();
        }
        System.out.println("Current Linux Sudo User Password is: " + token);

        // Get current mysql root user password
        String mysqlPass = "";
        PasswordDialog mysqlDialog = new PasswordDialog();

        mysqlDialog.setTitle("MySQL password");
        mysqlDialog.setHeaderText("Enter MySQL password:");
        mysqlDialog.setContentText("Password:");

        Optional<String> mysqlResult = mysqlDialog.showAndWait();
        if (mysqlResult.isPresent()) {
            mysqlPass = mysqlDialog.getPasswordField().getText();
        }
        System.out.println("Current MySQL root User Password is: " + mysqlPass);

        if ("".equals(token) || "".equals(mysqlPass)) {
            addMessageToListFlow("Authorization required to proceed. Please provide sudo password and mysql password to proceed ");
            System.out.println("Authorization required to proceed. Please provide sudo password and mysql password to proceed ");
        } else {
            ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
            configuration.setPackageDownloadUrl(url);
            configuration.setPackageUnzipDir(baseDir + downloadedFileName);
            configuration.setBaseDir(baseDir);
            // Check if we have done a backup and if not, Do Backup
            // System.out.println("We need to do a backup first");
            // String openmrsBackup = "/opt/kehmisApplicationToolbox/Downloads/Scripts/openmrs-backup-tools/openmrs_backup.sh";
            // configuration.setPathToBackupScript(openmrsBackup);
            // final PackageBackupService backupService = new PackageBackupService(this, configuration);
            // backupService.start();
            //Do Upgrade
            System.out.println("Now we can upgrade");
            configuration.setPathToSetupScript(baseDir + fileNameWithoutExtension + "/toolkit_setup_script.sh");
            final PackageDownloadService service = new PackageDownloadService(this, configuration);
            service.start();

            //Update EMR version display
            System.out.println("KenyaEMR version is now at: " + remoteemrversion);
            updateVersionDisplay(remoteemrversion);

            PropertiesConfiguration wdirprop = null;
            try {
                wdirprop = new PropertiesConfiguration(localproperties);
                wdirprop.setProperty("toolkit.emrversion", remoteemrversion);
                wdirprop.save();
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
        showProgress(false);
    }

    @FXML
    protected void backupEMR(ActionEvent actionEvent) throws IOException {
        showProgress(true);
        listMsgs.getItems().clear();
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
        showProgress(false);
    }

    @FXML
    protected void rollbackEMR(ActionEvent actionEvent) throws IOException {
        showProgress(true);
        listMsgs.getItems().clear();
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
        showProgress(false);
    }

    @FXML
    protected void appupdate(ActionEvent actionEvent) throws IOException {
        showProgress(true);
        listMsgs.getItems().clear();
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
            String toolupgradesripts = "/opt/kehmisApplicationToolbox/Downloads/Scripts/toolkit/upgrade.sh";
            ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
            configuration.setPathToolkitUpgradeScript(toolupgradesripts);
            final UpgradeToolkitService appUpdateService = new UpgradeToolkitService(this, configuration);
            appUpdateService.start();

        }
        showProgress(false);
    }

    @FXML
    protected void upgradeEMR(ActionEvent actionEvent) throws IOException {
        showProgress(true);
        listMsgs.getItems().clear();
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
        showProgress(false);
    }

    @FXML
    protected void offliceupgrade() throws FileNotFoundException {

        listMsgs.getItems().clear();
        addMessageToListFlow("Authorization required to proceed. Please provide details to proceed ");

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
            final FileChooser fc = new FileChooser();
            Stage stage = new Stage();
            //stage.setTitle("File Chooser Sample");
            addMessageToListFlow("Uploading Package Please Wait ...");

            File file = fc.showOpenDialog(stage);
            if (file != null) {
                // openFile(file);
                String fpath = file.getAbsolutePath();
                String fname = file.getName();
                System.out.println("File name " + fpath);
                System.out.println("File name " + fname);
                ToolboxServiceConfiguration configuration = new ToolboxServiceConfiguration(token, mysqlPass);
                configuration.setPathToOfflineScript(fpath);
                //configuration.setPackageUnzipDir("/opt/kehmisApplicationToolbox/Downloads/"+fname);
                configuration.setBaseDir("/opt/kehmisApplicationToolbox/Downloads/");
                String fileNameWithoutExtension = fname.substring(0, fname.lastIndexOf('.'));
                configuration.setPathToSetupScript(configuration.getBaseDir() + fileNameWithoutExtension + "/toolkit_setup_script.sh"); //toolkit_setup_script
                configuration.setPathToOfflineApplicationProperties(configuration.getBaseDir() + fileNameWithoutExtension + "/application.properties");
                final OfflineUpgradeService offlineUpgradeService = new OfflineUpgradeService(this, configuration);
                offlineUpgradeService.start();
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

    /**
     * Gets the latest KenyaEMR version 
     * @return String - the latest kenyaEMR version
     */
    public String getLatestKenyaEMRVersion() {
        String ret = "";

        // Start from the most reliable method
        ret = getLatestKenyaEMRVersionUsingRemoteLatestReleaseTag();
        if(ret.trim().equalsIgnoreCase("")) {
            ret = getLatestKenyaEMRVersionUsingRemoteOmodVersion();
        }

        return(ret);
    }

    /**
     * Gets the latest kenyaEMR version from github using the TAG of the latest release 
     * using the release found at: https://github.com/palladiumkenya/kenyahmis-releases/releases/latest
     * @return String - The version number
     */
    public static String getLatestKenyaEMRVersionUsingRemoteLatestReleaseTag() {
        String ret = "";

        try {
            String owner = "palladiumkenya"; // Replace with the actual owner of the GitHub repository
            String repoName = "kenyahmis-releases"; // Replace with the actual name of the GitHub repository

            // Initialize a GitHub client
            GitHub github = new GitHubBuilder().withEndpoint("https://api.github.com").build();

            // Get the repository by owner and name
            GHRepository repository = github.getRepository(owner + "/" + repoName);

            // Get the latest release
            GHRelease latestRelease = repository.getLatestRelease();

            // Get the tag name of the latest release
            String latestReleaseTag = latestRelease.getTagName();
            System.out.println("Latest Release Tag: " + latestReleaseTag);

            // Check if the tag conforms to versioning standards e.g 18.6.2
            String pattern = "-(\\d+\\.\\d+\\.\\d+)\\.";

            // Compile the pattern
            Pattern r = Pattern.compile(pattern);

            // Create a Matcher object
            Matcher matcher = r.matcher(latestReleaseTag);

            // Check if the pattern is found
            if (matcher.find()) {
                // Extract the matched version number
                String version = matcher.group(1);
                System.out.println("Version Found: " + version);
                ret = version;
            } else {
                System.out.println("Version not found in the TAG");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return(ret);
    }

    /**
     * Gets the latest kenyaEMR version from github using the omod version -- NB: It takes a bit of time to get this but it is the most accurate
     * using the release found at: https://github.com/palladiumkenya/kenyahmis-releases/releases/latest
     * @return String - The version number
     */
    public static String getLatestKenyaEMRVersionUsingRemoteOmodVersion() {
        String ret = "";

        String owner = "palladiumkenya"; // Replace with the GitHub repository owner
        String repoName = "kenyahmis-releases"; // Replace with the GitHub repository name

        try {

            GitHub github = new GitHubBuilder().withEndpoint("https://api.github.com").build();

            GHRepository repository = github.getRepository(owner + "/" + repoName);

            GHRelease latestRelease = repository.getLatestRelease();

            if (latestRelease != null) {
                for (GHAsset asset : latestRelease.listAssets()) {
                    String name = asset.getName();
                    System.out.println("File Name: " + name);
                    // You can also get more information about the asset if needed
                    name = name.trim().toLowerCase();
                    System.out.println("Normalized: " + name);
                    if(name.startsWith("kenyaemr_") && name.endsWith(".zip")) {
                        System.out.println("Found KenyaEMR release zip: " + name);

                        URL zipFileUrl = new URL(asset.getBrowserDownloadUrl());
                        try (InputStream bistream = zipFileUrl.openStream();
                            BufferedInputStream zipInputStream = new BufferedInputStream(bistream);
                            ZipInputStream zis = new ZipInputStream(zipInputStream)) {
                            //We now read from the stream
                            ZipEntry entry;
                            while ((entry = zis.getNextEntry()) != null) {
                                String entryName = entry.getName().toLowerCase();
                                //System.out.println("Entry: " + entryName);
                                if (entryName.contains("modules/kenyaemr-") && entryName.endsWith(".omod")) {
                                    // Found the omod file
                                    System.out.println("Found KenyaEMR omod: " + entryName);

                                    try (JarInputStream jarInputStream = new JarInputStream(zis)) {
                                        JarEntry jarEntry;
                                        while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                                            if(jarEntry.getName().trim().equalsIgnoreCase("config.xml")) {
                                                // Read the config.xml
                                                System.out.println("We got the config.xml");
                                                
                                                String fileContent = "";
                                                try (Scanner scanner = new Scanner(jarInputStream, "UTF-8")) {
                                                    fileContent = scanner.useDelimiter("\\A").next();
                                                    // Print or use the file content as needed
                                                    // System.out.println("Got config.xml content: " + fileContent);
                                                    String version = extractEMRVersionFromConfig(fileContent);
                                                    System.out.println("The current version is: " + version);
                                                    return(version);
                                                } catch (Exception ex) {
                                                    System.err.println("ERROR: " + ex.getMessage());
                                                    ex.printStackTrace();
                                                }

                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("No releases found for the repository.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return(ret);
    }

    /**
     * Gets the installed KenyaEMR version using 3 methods
     * @return String - the installed kenyaEMR version
     */
    public String getInstalledKenyaEMRVersion() {
        String ret = "";

        // Start from the most reliable method
        ret = getInstalledKenyaEMRVersionUsingAPI();
        if(ret.trim().equalsIgnoreCase("")) {
            ret = getInstalledKenyaEMRVersionUsingOmodVersion();
            if(ret.trim().equalsIgnoreCase("")) {
                ret = getInstalledKenyaEMRVersionUsingOmodFileName();
            }
        }

        return(ret);
    }
    
    /**
     * Use the omod file name to get the installed EMR version. e.g if filename is kenyaemr-18.6.2.omod, the version is 18.6.2
     * @return String - the installed kenyaEMR version
     */
    public String getInstalledKenyaEMRVersionUsingOmodFileName() {
        String ret = "";

        String jarFilePath = getLocalOmodFileName();
        jarFilePath = jarFilePath.trim().toLowerCase();
        // Find the index of "popov-"
        int startIndex = jarFilePath.indexOf("kenyaemr-");

        if (startIndex != -1) {
            // Find the index of ".omod" starting from the position after "popov-"
            int endIndex = jarFilePath.indexOf(".omod", startIndex);

            if (endIndex != -1) {
                // Extract the text between "popov-" and ".omod"
                String extractedText = jarFilePath.substring(startIndex + 6, endIndex);
                System.out.println("Omod file version: " + extractedText);
                ret = extractedText;
            } else {
                System.out.println(".omod not found in the input string.");
            }
        } else {
            System.out.println("popov- not found in the input string.");
        }

        return(ret);
    }

    /**
     * Read the omod file, extract the config.xml and read the installed version from there
     * @return String - the installed kenyaEMR version
     */
    public String getInstalledKenyaEMRVersionUsingOmodVersion() {
        String ret = "";

        String jarFilePath = getLocalOmodFileName();
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            ZipEntry zipEntry = jarFile.getEntry("config.xml");
            if (zipEntry != null) {
                System.out.println("Got the config.xml");

                try (InputStream inputStream = jarFile.getInputStream(zipEntry)) {
                    // Read the entire file into a String
                    // String fileContent = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
                    String fileContent = "";
                    try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
                        fileContent = scanner.useDelimiter("\\A").next();
                        // Print or use the file content as needed
                        // System.out.println("Got config.xml content: " + fileContent);
                        String version = extractEMRVersionFromConfig(fileContent);
                        return(version);
                    } catch (Exception ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } catch (Exception em) {
                    System.err.println("ERROR: " + em.getMessage());
                    em.printStackTrace();
                }
            } else {
                System.out.println("File not found in the JAR.");
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return(ret);
    }

    /**
     * Use the API to get the installed kenyaEMR version -- Assumes the installed EMR is Up and Running -  using the API found at: http://127.0.0.1:8080/openmrs/ws/sysmon.form
     * @return String - the installed kenyaEMR version
     */
    public String getInstalledKenyaEMRVersionUsingAPI() {
        ObjectMapper objectMapper = new ObjectMapper();
        String urlStr = "http://127.0.0.1:8080/openmrs/ws/sysmon.form";
        String username = "admin";
        String password = "Admin123";

        try {
            // Create URL object
            URL url = new URL(urlStr);

            // Create connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set Basic Auth credentials
            String authCredentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(authCredentials.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);

            // Set GET request
            connection.setRequestMethod("GET");

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read response if successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON and extract version
                String jsonPayload = response.toString();
                System.err.println("Got the payload: " + jsonPayload);
                JsonNode rootNode = objectMapper.readTree(jsonPayload);
                JsonNode serverNode = rootNode.path("server");
                JsonNode kenyaemrNode = serverNode.path("kenyaemr");
                String version = kenyaemrNode.path("version").asText();

                return version;
            } else {
                System.err.println("Error in HTTP response " + responseCode);
            }
        } catch (Exception ex) {
            System.err.println("Error getting the current EMR version: " + ex.getMessage());
            ex.printStackTrace();
        }

        return ""; // Return empty string in case of errors
    }

    /**
     * Extracts the version from the config.xml file inside the omod (An omod is just a jar file)
     * @param xmlString - the contents of config.xml
     * @return String the version
     */
    public static String extractEMRVersionFromConfig(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            Document document = builder.parse(inputSource);
            
            Element rootElement = document.getDocumentElement();
            Node versionNode = rootElement.getElementsByTagName("version").item(0);
            
            if (versionNode != null) {
                return versionNode.getTextContent();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the local kenyaEMR omod absolute path
     * @return String - The path
     */
    public static String getLocalOmodFileName() {
        String ret = "";
        try {
            String directoryPath = "/opt/tomcat/.OpenMRS/kenyaemr/modules";
            String prefix = "kenyaemr-";
            String suffix = ".omod";

            Optional<Path> firstMatchingFile = Files.list(Paths.get(directoryPath))
                .filter(path -> {
                    String fileName = path.getFileName().toString();
                    return fileName.startsWith(prefix) && fileName.endsWith(suffix);
                })
                .findFirst();

            if (firstMatchingFile.isPresent()) {
                // ret = firstMatchingFile.get().getFileName().toString();
                ret = firstMatchingFile.get().toAbsolutePath().toString();
                System.out.println("First file starting with " + prefix + ": " + ret);
            } else {
                System.out.println("No matching file found.");
            }
        } catch (Exception ex) {}

        return(ret);
    }

    /**
     * Compare versions
     * @param version1 - first version
     * @param version2 - second version
     * @return true if version1 is greater than version2, false if version1 is less than version2
     */
    public static boolean compareVersions(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");

        int minLength = Math.min(v1Parts.length, v2Parts.length);

        for (int i = 0; i < minLength; i++) {
            int v1Part = Integer.parseInt(v1Parts[i]);
            int v2Part = Integer.parseInt(v2Parts[i]);

            if (v1Part < v2Part) {
                return false;
            } else if (v1Part > v2Part) {
                return true;
            }
        }

        // If we reach here, all common parts are equal, check for extra parts
        if (v1Parts.length < v2Parts.length) {
            return false;
        } else if (v1Parts.length > v2Parts.length) {
            return true;
        }

        // Versions are equal
        return false;
    }

    /** 
     * Show or hide the progress spinner
     * @param status - boolean - true: show spinner, false: hide spinner
    */
    public void showProgress(boolean status) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(status) {
                            lblUpdates.setText("Working. Please wait");
                        } else {
                            lblUpdates.setText("Finished ...");
                        }
                        topCommands.setVisible(!status);
                        listMsgs.setVisible(!status);
                        showWait.setVisible(status);
                        showWait.setOpacity(0.5);
                    } catch(Exception ex) {}    
                }
            });
        } catch(Exception ex) {}
    }

    /**
     * Update the version displayed on screen
     * @param version
     */
    public void updateVersionDisplay(String version) {
        lblEMR.setText("KenyaEMR Version ("+ version +")");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // System.out.println("Current EMR version is: " + getInstalledKenyaEMRVersion());
        // System.out.println("Latest EMR version is: " + getLatestKenyaEMRVersion());

        listMsgs.getItems().clear();
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
        Properties prop = new Properties();
        try {
            prop.load(propresources.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        deploymentdir = prop.getProperty("toolkit.deploymentdir");
        localproperties = prop.getProperty("toolkit.localproperties");
        tookitversion = prop.getProperty("toolkit.version");
        localappversion = prop.getProperty("toolkit.version");
        emrversion = prop.getProperty("toolkit.emrversion");
        appurl = prop.getProperty("toolkit.appurl");
        appdir = prop.getProperty("toolkit.appdir");
        scriptversion = prop.getProperty("toolkit.scriptversion");
        remoteseripturl = prop.getProperty("toolkit.scriptsurl");
        configuration.setRemoteproperties(prop.getProperty("toolkit.remoteproperties"));
        configuration.setPathToLocalApplicationProperties(localproperties);

        String propFileName = configuration.getRemoteproperties();
        System.out.println("Remote props " + propFileName);
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

        Properties remoteprop = new Properties();
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
            // Empty
        } else {
            File theDir = new File(deploymentdir);
            if (!theDir.exists()){
                theDir.mkdirs();
            }
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
            } else {
                try {
                    FileInputStream ip = new FileInputStream(localproperties);
                    Properties wdirprop = new Properties();
                    wdirprop.load(ip);
                    tookitversion = wdirprop.getProperty("toolkit.version");
                    localappversion = wdirprop.getProperty("toolkit.version");
                    emrversion = wdirprop.getProperty("toolkit.emrversion");
                    scriptversion = wdirprop.getProperty("toolkit.scriptversion");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            msgData = FXCollections.observableArrayList();
            listMsgs.setItems(msgData);
            lblFooter.setText("Copyright " + Calendar.getInstance().get(Calendar.YEAR) + " KenyaHMIS ToolKit Version: " + tookitversion);
            lblEMR.setText("KenyaEMR Version ("+ emrversion +")");
            String[] localV = emrversion.split("[.]");
            String[] remoteV = remoteemrversion.split("[.]");
            Double local = Double.parseDouble(localV[1]+"."+localV[2]);
            Double remote = Double.parseDouble(remoteV[1]+"."+remoteV[2]);

            // for (String a : localV)
            // // System.out.println(a);
            
            // If remote version is greater than local version
            // if(Integer.parseInt(remoteV[0]) > Integer.parseInt(localV[0])){
            if(compareVersions(remoteemrversion, emrversion)) {
                lblUpdates.setText("KenyaEMR "+ remoteemrversion +" is Available !!!");
                lblUpdates.setTextFill(Color.web("#5c0617"));
                final double MAX_FONT_SIZE = 18.0; // define max font size you need
                lblUpdates.setFont(new Font(MAX_FONT_SIZE));

            } else {
                lblUpdates.setText("No Update Available !!!");
                lblUpdates.setTextFill(Color.web("#5c0617"));
                final double MAX_FONT_SIZE = 18.0; // define max font size you need
                lblUpdates.setFont(new Font(MAX_FONT_SIZE));
                cmdupgrade.setDisable(true);
                cmdrollback.setDisable(true);
            }

            // Download Scripts
            // Check if the scripts version is higher than the local scripte version
            // if(Double.parseDouble(remotescriptversion) > Double.parseDouble(scriptversion)){
            if(compareVersions(remotescriptversion, scriptversion)) {

                String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
                URL remotescrp = null;
                try {
                    System.out.println("Script URL is: " + remoteseripturl);
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
            // end Download Scrips

            // Check if the application version is higher than the local installed version
            // if(Double.parseDouble(appversion) > Double.parseDouble(localappversion)){
            if(compareVersions(appversion, localappversion)) {
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

                }
                ToolboxServiceConfiguration config = new ToolboxServiceConfiguration(token,mysqlPass);

                String baseDir = ToolkitUtils.DEFAULT_APPLICATION_BASE_DIRECTORY + ToolkitUtils.DEFAULT_DOWNLOAD_DIRECTORY;
                URL apdurl = null;
                try {
                    apdurl = new URL(appurl);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                config.setAppulr(apdurl);
                Path fileName = Paths.get(appurl);
                String downloadedFileName = fileName.getFileName().toString() ;
                config.setAppulr(apdurl);
                config.setApppackageUnzipDir(baseDir + downloadedFileName);
                config.setBaseDir(baseDir);
                String toolupgradesripts = "/opt/kehmisApplicationToolbox/Downloads/Scripts/toolkit/upgrade.sh";
                config.setPathToolkitUpgradeScript(toolupgradesripts);
                File theDirs = new File("/opt/kehmisApplicationToolbox");
                File theDird = new File("/opt/kehmisApplicationToolbox/Downloads");
                if (!theDirs.exists()){
                    theDirs.mkdirs();
                }
                if (!theDird.exists()){
                    theDird.mkdirs();
                }

                final AppUpdateService appUpdateService = new AppUpdateService(this, config);
                appUpdateService.start();

                // NB: Direct copy required sudo. This will most definately fail
                Path sour = Paths.get("/opt/kehmisApplicationToolbox/Downloads/kenyahmistoolkit.jar");
                Path Dest = Paths.get("/usr/share/kenyahmistoolkit/kenyahmistoolkit.jar");
                System.out.println("Updating Toolkit package");
                try {
                    SeekableByteChannel destFileChannel = Files.newByteChannel(Dest);
                    // destFileChannel.close();  //removing this will throw java.nio.file.AccessDeniedException:
                    // Files.copy(sour, Dest, StandardCopyOption.REPLACE_EXISTING);
                    PropertiesConfiguration wdirprop = new PropertiesConfiguration(localproperties);
                    wdirprop.setProperty("toolkit.version",appversion);
                    wdirprop.save();
                } catch (IOException | ConfigurationException e) {
                    System.err.println("An error occured: " + e.getMessage() );
                } catch (Exception fd) {
                    System.err.println("An error occured: " + fd.getMessage() );
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
