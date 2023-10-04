package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadScriptTask  extends Task implements ShowProgress {

    private ToolboxServiceConfiguration configuration;


    private final ToolboxController controller;
    public DownloadScriptTask(ToolboxController controller) {
        this.controller = controller;
    }

    public DownloadScriptTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Object call() throws Exception {
        try (InputStream in = configuration.getScriptsurl().openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(configuration.getScriptpackageUnzipDir())) {
            System.out.println("Downloading ...");
            controller.addMessageToListFlow("Downloading Scripts ...");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            System.out.println("An error occurred. " + e.getCause());
            controller.addMessageToListFlow("An error occurred. " + e.getCause());
            e.printStackTrace();
        }

        System.out.println("Scripts Download completed");

        controller.addMessageToListFlow("Download completed");

        System.out.println("Unzipping started");

        controller.addMessageToListFlow("Unzipping scripts started");
        ToolkitUtils.unzip(configuration.getScriptpackageUnzipDir(), configuration.getBaseDir());

        System.out.println("Unzipping completed");
        controller.addMessageToListFlow("Scripts Unzipping completed");

        System.out.println("Executing upgrade script");
        controller.addMessageToListFlow("Executing toolkit upgrade script");
        controller.addMessageToListFlow("Done Executing toolkit upgrade script");

        return "Success";
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
