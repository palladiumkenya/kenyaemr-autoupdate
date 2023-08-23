package com.kenyahmis.applicationtoolkit.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.Services.UpgradeToolkitService;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class AppUpdateTask extends Task implements ShowProgress {
    private ToolboxServiceConfiguration configuration;
    private final ToolboxController controller;
    public AppUpdateTask(ToolboxController controller) {
        this.controller = controller;
    }

    public AppUpdateTask(ToolboxController controller, ToolboxServiceConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    protected Object call() throws Exception {

        showProgress(true);
        try (InputStream in = configuration.getAppulr().openStream();
            ReadableByteChannel rbc = Channels.newChannel(in);
            FileOutputStream fos = new FileOutputStream(configuration.getApppackageUnzipDir())) {
            System.out.println("Downloading ...");
            controller.addMessageToListFlow("Downloading Toolkit Update ...");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            System.out.println("An error occurred. " + e.getCause());
            controller.addMessageToListFlow("An error occurred. " + e.getCause());
            e.printStackTrace();
        }
        System.out.println("Download completed");

        controller.addMessageToListFlow("Toolkit Download completed");

        System.out.println("Unzipping started");

        controller.addMessageToListFlow("Unzipping Toolkit started");
        ToolkitUtils.unzip(configuration.getApppackageUnzipDir(), configuration.getBaseDir());

        System.out.println("Unzipping completed");
        controller.addMessageToListFlow("Unzipping Toolkit completed");

        //Update the file
        UpgradeToolkitService service = new UpgradeToolkitService(controller, configuration);
        service.start();
        showProgress(false);

        // Exit showing the next task
        //Platform.exit();

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

