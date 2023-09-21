package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InitialSetupTask extends Task implements ShowProgress {

    private final ToolboxController controller;
    private ToolboxServiceConfiguration configuration;

    public InitialSetupTask(ToolboxController controller) {
        this.controller = controller;
    }

    public InitialSetupTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }
    @Override
    protected Object call() throws Exception {

        // Run a shell command
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("bash");
        cmdList.add(configuration.getPathToRollbackScript());
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
                System.out.println("Successfully executed the initial setup script");
                controller.addMessageToListFlow("Successfully executed the initail setup script");
            } else {
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));

                String error;
                while ((error = errorReader.readLine()) != null) {
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

        System.out.println("Completed executing Rollback script");
        controller.addMessageToListFlow("Give it a few minutes. Then check your browser");

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
