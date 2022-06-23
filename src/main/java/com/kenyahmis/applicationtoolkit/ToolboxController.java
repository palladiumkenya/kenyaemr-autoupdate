package com.kenyahmis.applicationtoolkit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ToolboxController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextFlow upgradeProgress;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("HMIS Application Toolkit");
    }
    @FXML
    protected void downloadEmrUpgrade(ActionEvent actionEvent) throws IOException {

        addMessageToTextFlow("Initiating download ... ", Color.DARKSLATEBLUE, new Font(15));
        String baseDir = "/home/ojwang/Documents/testDownload/";
        String downloadedFileName = "KenyaEMR_18.2.0.zip"; //"Afyastat.2.0.zip";
        String exctractedDir = "KenyaEMR_18.2.0";
        //URL url = new URL("https://github.com/palladiumkenya/kenyahmis-releases/releases/download/v49/AFYASTAT.zip");

        //downloadFile(url, baseDir + downloadedFileName);
        //upgradeProgress.setText("Download completed");

        //unzip(baseDir + downloadedFileName, baseDir);

        //upgradeProgress.setText("Unzipping completed");
        executeShellScript(baseDir + exctractedDir );

    }

    public void downloadFile(URL url, String outputFileName) throws IOException
    {
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    private void unzip(String zipFilePath, String destDirt) throws IOException {
        File destDir = new File(destDirt);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directoryy " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public void executeShellScript(String unzippedDirectory) {
        //ProcessBuilder processBuilder = new ProcessBuilder();

        Text txtInitExecution = new Text("Executing shell ... ");
        txtInitExecution.setFont(new Font(15));
        txtInitExecution.setFill(Color.DARKSLATEBLUE);

        upgradeProgress.getChildren().add(txtInitExecution);

        // Run a shell command
        List<String> cmdList = new ArrayList<String>();
        // adding command and args to the list
        cmdList.add("sh");
        cmdList.add("/home/ojwang/Documents/testDownload/KenyaEMR_18.2.0/setup_script.sh");
        ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
       // processBuilder.command("bash", "-c", unzippedDirectory);

        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                addMessageToTextFlow(line + "\n", Color.GREEN, new Font(15));

            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                addMessageToTextFlow("Success!", Color.DARKSLATEBLUE, new Font(15));
                System.exit(0);
            } else {
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                addMessageToTextFlow("\nThere was a problem executing the script. Exit code " + exitVal, Color.RED, new Font(15));

                String error;
                while ((error = errorReader.readLine()) != null) {
                    addMessageToTextFlow(error + "\n", Color.DARKRED, new Font(15));

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addMessageToTextFlow(String text, Color textColor, Font font) {
        Text txt = new Text(text);
        txt.setFont(font);
        txt.setFill(textColor);
        upgradeProgress.getChildren().add(txt);
    }

}