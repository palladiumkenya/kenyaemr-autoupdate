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

/**
 * A background task that executes a shell setup script
 */
public class RunUpgradeScriptTask extends Task implements ShowProgress {

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
        System.out.println("My path is this one "+configuration.getPathToSetupScript());
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
                System.out.println("Successfully executed the KenyaEMR setup script");
                controller.addMessageToListFlow("Successfully executed the KenyaEMR setup script");
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String error;
                while ((error = errorReader.readLine()) != null) {
                    System.out.println("An error occured upgrading kenyaEMR: " + error);
                    controller.addMessageToListFlow(error);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while upgrading kenyaEMR: " + e.getMessage());
            controller.addMessageToListFlow("An error occurred while upgrading kenyaEMR: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed executing kenyaEMR upgrade script");
        controller.addMessageToListFlow("Completed executing kenyaEMR upgrade script");

        return "success";
    }

    /** 
     * Show or hide the progress spinner
     * @param status - boolean - true: show spinner, false: hide spinner
    */
    public void showProgress(boolean status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.showProgress(status);
            }
        });
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
