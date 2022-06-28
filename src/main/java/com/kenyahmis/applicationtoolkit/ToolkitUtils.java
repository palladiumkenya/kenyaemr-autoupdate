package com.kenyahmis.applicationtoolkit;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ToolkitUtils {
    public static void unzip(String zipFilePath, String destDirt) throws IOException {
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


    public static void executeShellScript(String unzippedDirectory) {

        /*Text txtInitExecution = new Text("Executing shell ... ");
        txtInitExecution.setFont(new Font(15));
        txtInitExecution.setFill(Color.DARKSLATEBLUE);*/
        System.out.println("Inside the script.....");
        //addMessageToTextFlow("\nExecuting shell script...", Color.RED, new Font(15));

        // Run a shell command
        List<String> cmdList = new ArrayList<String>();
        // adding command and args to the list
        //echo mypassword | sudo -S

        cmdList.add("echo");
        cmdList.add("Warrior123#");
        cmdList.add("\n");
        cmdList.add("|");
        cmdList.add("sudo");
        cmdList.add("-S");

        cmdList.add("/home/ojwang/Documents/testDownload/KenyaEMR_18.2.0.1.2/setup_script.sh");
        ProcessBuilder processBuilder = new ProcessBuilder(cmdList);

        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                //addMessageToTextFlow(line + "\n", Color.GREEN, new Font(15));

            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                //addMessageToTextFlow("Success!", Color.DARKSLATEBLUE, new Font(15));
                System.exit(0);
            } else {
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                //addMessageToTextFlow("\nThere was a problem executing the script. Exit code " + exitVal, Color.RED, new Font(15));

                String error;
                while ((error = errorReader.readLine()) != null) {
                    //addMessageToTextFlow(error + "\n", Color.DARKRED, new Font(15));

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
