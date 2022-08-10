package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.ToolboxController;
import com.kenyahmis.applicationtoolkit.ToolboxServiceConfiguration;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A background task that executes a shell setup script
 */
public class RunUpgradeScriptTask extends Task {

    private final ToolboxController controller;
    private ToolboxServiceConfiguration configuration;

    public RunUpgradeScriptTask(ToolboxController controller) {
        this.controller = controller;
    }

    public RunUpgradeScriptTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }
    @Override
    protected Object call() throws Exception {

        // Run a shell command
        List<String> cmdList = new ArrayList<String>();
        // adding command and args to the list
        cmdList.add("sh");

        cmdList.add(configuration.getPathToSetupScript());
        cmdList.add(configuration.getUserPass());
        cmdList.add(configuration.getMysqlPass());
        ProcessBuilder processBuilder = new ProcessBuilder(cmdList);

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
                System.out.println("Successfully executed the setup script");
                controller.addMessageToListFlow("Successfully executed the setup script");
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

        System.out.println("Completed executing script");
        controller.addMessageToListFlow("Service restarted. Give it a few minutes");

        return "success";
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}