package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RunBackupScriptTask extends Task {

    private final ToolboxController controller;
    private ToolboxServiceConfiguration configuration;

    public RunBackupScriptTask(ToolboxController controller) {
        this.controller = controller;
    }

    public RunBackupScriptTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }
    @Override
    protected Object call() throws Exception {

        // Run a shell command
        List<String> cmdList = new ArrayList<String>();
        // adding command and args to the list
        cmdList.add("bash");

        cmdList.add(configuration.getPathToBackupScript());
        cmdList.add(configuration.getUserPass());
        cmdList.add(configuration.getMysqlPass());

       ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
     // String[] cmdScript = new String[]{"bash", configuration.getPathToBackupScript()};
     //  ProcessBuilder processBuilder = new ProcessBuilder(cmdScript);

        try {

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String line;
                    while (true) {
                        try {
                            if (!((line = reader.readLine()) != null)) break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        controller.addMessageToListFlow(line);
                    }
                }
            });


            int exitVal = process.waitFor();

            if (exitVal == 0) {
                System.out.println("Successfully executed the Backup script");
                controller.addMessageToListFlow("Successfully executed the Backup script");
            } else {
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                //addMessageToTextFlow("\nThere was a problem executing the script. Exit code " + exitVal, Color.RED, new Font(15));

                String error;
                while ((error = errorReader.readLine()) != null) {
                    //addMessageToTextFlow(error + "\n", Color.DARKRED, new Font(15));
                    System.out.println("An error occured" + error);
                    controller.addMessageToListFlow(error);

                }
            }

        } catch (IOException e) {
            System.out.println("there was an error");
            controller.addMessageToListFlow("An error occurred while upgrading");

            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed executing Backup script");
        controller.addMessageToListFlow("Give it a few minutes. Downloading Upgrade packages");

        return "success";
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }

}
