package com.kenyahmis.applicationtoolkit.Task;

import com.kenyahmis.applicationtoolkit.Services.RunUpgradeScriptService;
import com.kenyahmis.applicationtoolkit.Services.ToolboxServiceConfiguration;
import com.kenyahmis.applicationtoolkit.controllers.ToolboxController;
import com.kenyahmis.applicationtoolkit.utils.ToolkitUtils;
import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class AppUpdateTask extends Task {

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

        return "Success";
    }

    public ToolboxServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ToolboxServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}

